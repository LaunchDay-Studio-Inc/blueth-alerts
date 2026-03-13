package online.blueth.alerts.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class AlertsConfig {

    private final FileConfiguration config;

    public AlertsConfig(JavaPlugin plugin) {
        this.config = plugin.getConfig();
    }

    public String getWebhookUrl() {
        return config.getString("discord.webhook-url", "");
    }

    public String getBotName() {
        return config.getString("discord.bot-name", "Blueth Alerts");
    }

    public String getAvatarUrl() {
        return config.getString("discord.avatar-url", "");
    }

    public boolean isEventEnabled(String event) {
        return config.getBoolean("events." + event + ".enabled", false);
    }

    public int getEventColor(String event) {
        String hex = config.getString("events." + event + ".color", "#FFFFFF");
        return parseColor(hex);
    }

    public String getServerName() {
        return config.getString("messages.server-name", "My Server");
    }

    public String getCommandMonitorPermission() {
        return config.getString("events.player-command.permission", "blueth.alerts.monitor");
    }

    public double getTpsThreshold() {
        return config.getDouble("events.low-tps.threshold", 18.0);
    }

    public int getTpsCooldown() {
        return config.getInt("events.low-tps.cooldown", 300);
    }

    private int parseColor(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        return Integer.parseInt(hex, 16);
    }
}
