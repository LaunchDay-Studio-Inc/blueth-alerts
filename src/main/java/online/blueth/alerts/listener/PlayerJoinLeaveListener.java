package online.blueth.alerts.listener;

import online.blueth.alerts.BluethAlerts;
import online.blueth.alerts.webhook.DiscordEmbed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.Instant;

public class PlayerJoinLeaveListener implements Listener {

    private final BluethAlerts plugin;

    public PlayerJoinLeaveListener(BluethAlerts plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        var config = plugin.getAlertsConfig();

        if (player.hasPlayedBefore()) {
            if (!config.isEventEnabled("player-join")) return;

            var embed = new DiscordEmbed();
            embed.setTitle("Player Joined");
            embed.setColor(config.getEventColor("player-join"));
            embed.setAuthor(player.getName(), crafatarUrl(player.getUniqueId().toString()));
            embed.addField("Player", player.getName(), true);
            embed.addField("UUID", player.getUniqueId().toString(), true);
            embed.addField("Online", String.valueOf(plugin.getServer().getOnlinePlayers().size()), true);
            embed.setFooter(config.getServerName());
            embed.setTimestamp(Instant.now());
            plugin.getWebhookClient().send(embed);
        } else {
            if (!config.isEventEnabled("first-join")) return;

            var embed = new DiscordEmbed();
            embed.setTitle("\u2B50 New Player!");
            embed.setDescription("Welcome **" + player.getName() + "** to the server!");
            embed.setColor(config.getEventColor("first-join"));
            embed.setAuthor(player.getName(), crafatarUrl(player.getUniqueId().toString()));
            embed.setThumbnail(crafatarUrl(player.getUniqueId().toString()));
            embed.addField("Player", player.getName(), true);
            embed.addField("UUID", player.getUniqueId().toString(), true);
            embed.addField("Online", String.valueOf(plugin.getServer().getOnlinePlayers().size()), true);
            embed.setFooter(config.getServerName());
            embed.setTimestamp(Instant.now());
            plugin.getWebhookClient().send(embed);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        var config = plugin.getAlertsConfig();
        if (!config.isEventEnabled("player-leave")) return;

        var player = event.getPlayer();
        var embed = new DiscordEmbed();
        embed.setTitle("Player Left");
        embed.setColor(config.getEventColor("player-leave"));
        embed.setAuthor(player.getName(), crafatarUrl(player.getUniqueId().toString()));
        embed.addField("Player", player.getName(), true);
        embed.addField("UUID", player.getUniqueId().toString(), true);
        embed.addField("Online", String.valueOf(Math.max(0, plugin.getServer().getOnlinePlayers().size() - 1)), true);
        embed.setFooter(config.getServerName());
        embed.setTimestamp(Instant.now());
        plugin.getWebhookClient().send(embed);
    }

    private String crafatarUrl(String uuid) {
        return "https://crafatar.com/avatars/" + uuid + "?size=64";
    }
}
