package online.blueth.alerts.command;

import online.blueth.alerts.BluethAlerts;
import online.blueth.alerts.webhook.DiscordEmbed;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

public class AlertsCommand implements CommandExecutor, TabCompleter {

    private static final List<String> SUBCOMMANDS = List.of("reload", "test", "status");

    private final BluethAlerts plugin;

    public AlertsCommand(BluethAlerts plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("blueth.alerts.admin")) {
            sender.sendMessage("\u00a7cYou don't have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("\u00a76Blueth Alerts \u00a7fv" + plugin.getDescription().getVersion());
            sender.sendMessage("\u00a77Usage: /alerts <reload|test|status>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload" -> handleReload(sender);
            case "test" -> handleTest(sender);
            case "status" -> handleStatus(sender);
            default -> sender.sendMessage("\u00a7cUnknown subcommand. Use: reload, test, status");
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                 @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("blueth.alerts.admin")) return List.of();
        if (args.length == 1) {
            String prefix = args[0].toLowerCase();
            return SUBCOMMANDS.stream().filter(s -> s.startsWith(prefix)).toList();
        }
        return List.of();
    }

    private void handleReload(CommandSender sender) {
        plugin.reload();
        sender.sendMessage("\u00a7aBlueth Alerts configuration reloaded!");
    }

    private void handleTest(CommandSender sender) {
        var config = plugin.getAlertsConfig();
        if (config.getWebhookUrl().isEmpty()) {
            sender.sendMessage("\u00a7cNo webhook URL configured! Set it in config.yml.");
            return;
        }

        var embed = new DiscordEmbed();
        embed.setTitle("\u2705 Test Notification");
        embed.setDescription("If you see this, Blueth Alerts is working!");
        embed.setColor(0x00AAFF);
        embed.addField("Server", config.getServerName(), true);
        embed.setFooter(config.getServerName());
        embed.setTimestamp(Instant.now());
        plugin.getWebhookClient().send(embed);

        sender.sendMessage("\u00a7aTest webhook sent! Check your Discord channel.");
    }

    private void handleStatus(CommandSender sender) {
        var config = plugin.getAlertsConfig();
        sender.sendMessage("\u00a76Blueth Alerts Status:");
        sender.sendMessage("\u00a77Webhook: " + (config.getWebhookUrl().isEmpty() ? "\u00a7cNot set" : "\u00a7aConfigured"));

        String[] events = {"player-join", "player-leave", "player-death", "player-chat",
                "server-start", "server-stop", "player-command", "low-tps", "first-join", "advancement"};

        sender.sendMessage("\u00a77Events:");
        for (String event : events) {
            boolean enabled = config.isEventEnabled(event);
            sender.sendMessage("  " + (enabled ? "\u00a7a\u2714" : "\u00a7c\u2718") + " \u00a7f" + event);
        }
    }
}
