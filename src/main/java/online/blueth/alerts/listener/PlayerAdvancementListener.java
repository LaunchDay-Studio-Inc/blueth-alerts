package online.blueth.alerts.listener;

import online.blueth.alerts.BluethAlerts;
import online.blueth.alerts.webhook.DiscordEmbed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import java.time.Instant;

public class PlayerAdvancementListener implements Listener {

    private final BluethAlerts plugin;

    public PlayerAdvancementListener(BluethAlerts plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        var config = plugin.getAlertsConfig();
        if (!config.isEventEnabled("advancement")) return;

        // Skip recipe advancements
        var key = event.getAdvancement().getKey().getKey();
        if (key.startsWith("recipes/")) return;

        var display = event.getAdvancement().getDisplay();
        if (display == null) return;

        var player = event.getPlayer();
        String title = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText()
                .serialize(display.title());

        var embed = new DiscordEmbed();
        embed.setTitle("Advancement Earned");
        embed.setColor(config.getEventColor("advancement"));
        embed.setAuthor(player.getName(), crafatarUrl(player.getUniqueId().toString()));
        embed.addField("Player", player.getName(), true);
        embed.addField("Advancement", title, true);
        embed.setFooter(config.getServerName());
        embed.setTimestamp(Instant.now());
        plugin.getWebhookClient().send(embed);
    }

    private String crafatarUrl(String uuid) {
        return "https://crafatar.com/avatars/" + uuid + "?size=64";
    }
}
