package online.blueth.alerts.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import online.blueth.alerts.BluethAlerts;
import online.blueth.alerts.webhook.DiscordEmbed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.time.Instant;

public class PlayerChatListener implements Listener {

    private final BluethAlerts plugin;

    public PlayerChatListener(BluethAlerts plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChat(AsyncChatEvent event) {
        var config = plugin.getAlertsConfig();
        if (!config.isEventEnabled("player-chat")) return;

        var player = event.getPlayer();
        String message = PlainTextComponentSerializer.plainText().serialize(event.message());

        var embed = new DiscordEmbed();
        embed.setTitle("Player Chat");
        embed.setColor(config.getEventColor("player-chat"));
        embed.setAuthor(player.getName(), crafatarUrl(player.getUniqueId().toString()));
        embed.setDescription(message);
        embed.setFooter(config.getServerName());
        embed.setTimestamp(Instant.now());
        plugin.getWebhookClient().send(embed);
    }

    private String crafatarUrl(String uuid) {
        return "https://crafatar.com/avatars/" + uuid + "?size=64";
    }
}
