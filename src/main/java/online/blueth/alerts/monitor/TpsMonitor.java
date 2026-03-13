package online.blueth.alerts.monitor;

import online.blueth.alerts.BluethAlerts;
import online.blueth.alerts.config.AlertsConfig;
import online.blueth.alerts.webhook.DiscordEmbed;
import online.blueth.alerts.webhook.WebhookClient;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.time.Instant;

public class TpsMonitor {

    private final BluethAlerts plugin;
    private final AlertsConfig config;
    private final WebhookClient webhookClient;
    private BukkitTask task;
    private long lastAlertTime = 0;

    public TpsMonitor(BluethAlerts plugin, AlertsConfig config, WebhookClient webhookClient) {
        this.plugin = plugin;
        this.config = config;
        this.webhookClient = webhookClient;
    }

    public void start() {
        // Check every 30 seconds (600 ticks)
        task = Bukkit.getScheduler().runTaskTimer(plugin, this::check, 600L, 600L);
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    private void check() {
        double tps = Bukkit.getTPS()[0];
        double threshold = config.getTpsThreshold();

        if (tps >= threshold) return;

        long now = System.currentTimeMillis() / 1000;
        int cooldown = config.getTpsCooldown();
        if (now - lastAlertTime < cooldown) return;

        lastAlertTime = now;

        var embed = new DiscordEmbed();
        embed.setTitle("\u26A0 Low TPS Warning");
        embed.setDescription("Server TPS has dropped below the threshold!");
        embed.setColor(config.getEventColor("low-tps"));
        embed.addField("Current TPS", String.format("%.1f", tps), true);
        embed.addField("Threshold", String.format("%.1f", threshold), true);
        embed.addField("Online Players", String.valueOf(Bukkit.getOnlinePlayers().size()), true);
        embed.setFooter(config.getServerName());
        embed.setTimestamp(Instant.now());
        webhookClient.send(embed);
    }
}
