package online.blueth.alerts.listener;

import online.blueth.alerts.BluethAlerts;
import online.blueth.alerts.webhook.DiscordEmbed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.time.Instant;

public class PlayerDeathListener implements Listener {

    private final BluethAlerts plugin;

    public PlayerDeathListener(BluethAlerts plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(PlayerDeathEvent event) {
        var config = plugin.getAlertsConfig();
        if (!config.isEventEnabled("player-death")) return;

        var player = event.getEntity();
        var embed = new DiscordEmbed();
        embed.setTitle("Player Died");
        embed.setColor(config.getEventColor("player-death"));
        embed.setAuthor(player.getName(), crafatarUrl(player.getUniqueId().toString()));

        String deathMessage = event.deathMessage() != null
                ? net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(event.deathMessage())
                : player.getName() + " died";
        embed.setDescription(deathMessage);

        var loc = player.getLocation();
        embed.addField("Location", String.format("%.0f, %.0f, %.0f (%s)",
                loc.getX(), loc.getY(), loc.getZ(), loc.getWorld().getName()), true);

        var killer = player.getKiller();
        if (killer != null) {
            embed.addField("Killer", killer.getName(), true);
        }

        embed.setFooter(config.getServerName());
        embed.setTimestamp(Instant.now());
        plugin.getWebhookClient().send(embed);
    }

    private String crafatarUrl(String uuid) {
        return "https://crafatar.com/avatars/" + uuid + "?size=64";
    }
}
