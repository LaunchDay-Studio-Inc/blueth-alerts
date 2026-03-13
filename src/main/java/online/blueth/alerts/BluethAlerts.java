package online.blueth.alerts;

import online.blueth.alerts.command.AlertsCommand;
import online.blueth.alerts.config.AlertsConfig;
import online.blueth.alerts.listener.*;
import online.blueth.alerts.monitor.TpsMonitor;
import online.blueth.alerts.webhook.DiscordEmbed;
import online.blueth.alerts.webhook.WebhookClient;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;

public final class BluethAlerts extends JavaPlugin {

    private AlertsConfig alertsConfig;
    private WebhookClient webhookClient;
    private TpsMonitor tpsMonitor;
    private Instant startTime;

    @Override
    public void onEnable() {
        startTime = Instant.now();
        saveDefaultConfig();

        alertsConfig = new AlertsConfig(this);
        webhookClient = new WebhookClient(alertsConfig, getLogger());

        registerListeners();
        registerCommands();

        if (alertsConfig.isEventEnabled("low-tps")) {
            tpsMonitor = new TpsMonitor(this, alertsConfig, webhookClient);
            tpsMonitor.start();
        }

        if (alertsConfig.isEventEnabled("server-start")) {
            sendServerStartEmbed();
        }

        getLogger().info("Blueth Alerts v" + getDescription().getVersion() + " enabled!");
    }

    @Override
    public void onDisable() {
        if (alertsConfig != null && alertsConfig.isEventEnabled("server-stop")) {
            sendServerStopEmbed();
        }

        if (tpsMonitor != null) {
            tpsMonitor.stop();
        }

        if (webhookClient != null) {
            webhookClient.shutdown();
        }

        getLogger().info("Blueth Alerts disabled.");
    }

    public void reload() {
        reloadConfig();
        alertsConfig = new AlertsConfig(this);
        webhookClient.updateConfig(alertsConfig);

        if (tpsMonitor != null) {
            tpsMonitor.stop();
            tpsMonitor = null;
        }

        if (alertsConfig.isEventEnabled("low-tps")) {
            tpsMonitor = new TpsMonitor(this, alertsConfig, webhookClient);
            tpsMonitor.start();
        }
    }

    public AlertsConfig getAlertsConfig() {
        return alertsConfig;
    }

    public WebhookClient getWebhookClient() {
        return webhookClient;
    }

    public Instant getStartTime() {
        return startTime;
    }

    private void registerListeners() {
        var pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerJoinLeaveListener(this), this);
        pm.registerEvents(new PlayerDeathListener(this), this);
        pm.registerEvents(new PlayerChatListener(this), this);
        pm.registerEvents(new PlayerCommandListener(this), this);
        pm.registerEvents(new PlayerAdvancementListener(this), this);
    }

    private void registerCommands() {
        var cmd = getCommand("alerts");
        if (cmd != null) {
            var handler = new AlertsCommand(this);
            cmd.setExecutor(handler);
            cmd.setTabCompleter(handler);
        }
    }

    private void sendServerStartEmbed() {
        var embed = new DiscordEmbed();
        embed.setTitle("Server Started");
        embed.setDescription("The server is now online!");
        embed.setColor(alertsConfig.getEventColor("server-start"));
        embed.addField("Version", getServer().getVersion(), true);
        embed.addField("Players", "0/" + getServer().getMaxPlayers(), true);
        embed.setFooter(alertsConfig.getServerName());
        embed.setTimestamp(Instant.now());
        webhookClient.send(embed);
    }

    private void sendServerStopEmbed() {
        var uptime = java.time.Duration.between(startTime, Instant.now());
        long hours = uptime.toHours();
        long minutes = uptime.toMinutesPart();
        long seconds = uptime.toSecondsPart();
        String uptimeStr = String.format("%dh %dm %ds", hours, minutes, seconds);

        var embed = new DiscordEmbed();
        embed.setTitle("Server Stopped");
        embed.setDescription("The server is shutting down.");
        embed.setColor(alertsConfig.getEventColor("server-stop"));
        embed.addField("Uptime", uptimeStr, true);
        embed.addField("Version", getServer().getVersion(), true);
        embed.setFooter(alertsConfig.getServerName());
        embed.setTimestamp(Instant.now());
        webhookClient.sendSync(embed);
    }
}
