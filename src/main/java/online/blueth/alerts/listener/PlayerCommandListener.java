package online.blueth.alerts.listener;

import online.blueth.alerts.BluethAlerts;
import online.blueth.alerts.webhook.DiscordEmbed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.time.Instant;

public class PlayerCommandListener implements Listener {

    private final BluethAlerts plugin;

    public PlayerCommandListener(BluethAlerts plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        var config = plugin.getAlertsConfig();
        if (!config.isEventEnabled("player-command")) return;

        Player player = event.getPlayer();
        String permission = config.getCommandMonitorPermission();
        if (!player.hasPermission(permission)) return;

        var embed = new DiscordEmbed();
        embed.setTitle("Command Executed");
        embed.setColor(config.getEventColor("player-command"));
        embed.setAuthor(player.getName(), crafatarUrl(player.getUniqueId().toString()));
        embed.setDescription("`" + event.getMessage() + "`");
        embed.addField("Player", player.getName(), true);
        embed.setFooter(config.getServerName());
        embed.setTimestamp(Instant.now());
        plugin.getWebhookClient().send(embed);
    }

    private String crafatarUrl(String uuid) {
        return "https://crafatar.com/avatars/" + uuid + "?size=64";
    }
}
