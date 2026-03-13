package online.blueth.alerts.webhook;

import online.blueth.alerts.config.AlertsConfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebhookClient {

    private static final int MAX_REQUESTS = 5;
    private static final long WINDOW_MS = 5_000;

    private volatile AlertsConfig config;
    private final Logger logger;
    private final HttpClient httpClient;
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private final ScheduledExecutorService scheduler;
    private final AtomicBoolean running = new AtomicBoolean(true);

    private final long[] timestamps = new long[MAX_REQUESTS];
    private int timestampIndex = 0;

    public WebhookClient(AlertsConfig config, Logger logger) {
        this.config = config;
        this.logger = logger;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            var t = new Thread(r, "BluethAlerts-Webhook");
            t.setDaemon(true);
            return t;
        });
        this.scheduler.scheduleWithFixedDelay(this::processQueue, 100, 100, TimeUnit.MILLISECONDS);
    }

    public void updateConfig(AlertsConfig config) {
        this.config = config;
    }

    public void send(DiscordEmbed embed) {
        String payload = buildPayload(embed);
        queue.offer(payload);
    }

    public void sendSync(DiscordEmbed embed) {
        String payload = buildPayload(embed);
        doSend(payload);
    }

    public void shutdown() {
        running.set(false);
        scheduler.shutdown();
        // Flush remaining messages
        String payload;
        while ((payload = queue.poll()) != null) {
            doSend(payload);
        }
        try {
            scheduler.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        httpClient.close();
    }

    private String buildPayload(DiscordEmbed embed) {
        var sb = new StringBuilder();
        sb.append("{");
        String botName = config.getBotName();
        if (botName != null && !botName.isEmpty()) {
            sb.append("\"username\":").append(jsonStr(botName)).append(",");
        }
        String avatarUrl = config.getAvatarUrl();
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            sb.append("\"avatar_url\":").append(jsonStr(avatarUrl)).append(",");
        }
        sb.append("\"embeds\":[").append(embed.toJson()).append("]}");
        return sb.toString();
    }

    private void processQueue() {
        if (!running.get()) return;
        String payload = queue.poll();
        if (payload == null) return;

        waitForRateLimit();
        doSend(payload);
    }

    private void waitForRateLimit() {
        long now = System.currentTimeMillis();
        long oldest = timestamps[timestampIndex];
        if (oldest != 0 && now - oldest < WINDOW_MS) {
            long delay = WINDOW_MS - (now - oldest);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void doSend(String payload) {
        String url = config.getWebhookUrl();
        if (url == null || url.isEmpty()) return;

        try {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 429) {
                // Rate limited by Discord — re-queue and wait
                queue.offer(payload);
                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else if (response.statusCode() >= 400) {
                logger.warning("Discord webhook returned HTTP " + response.statusCode() + ": " + response.body());
            }

            timestamps[timestampIndex] = System.currentTimeMillis();
            timestampIndex = (timestampIndex + 1) % MAX_REQUESTS;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to send Discord webhook", e);
        }
    }

    private String jsonStr(String value) {
        if (value == null) return "null";
        return "\"" + value.replace("\\", "\\\\")
                            .replace("\"", "\\\"")
                            .replace("\n", "\\n")
                            .replace("\r", "\\r")
                            .replace("\t", "\\t") + "\"";
    }
}
