package me.jackstar.drakestab.commands;

import me.jackstar.drakestab.TabManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DrakesTabCommand implements CommandExecutor {

    private final TabManager tabManager;

    public DrakesTabCommand(TabManager tabManager) {
        this.tabManager = tabManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        if (!sender.hasPermission("drakestab.admin")) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        }

        if (args.length == 0) {
            usage(sender, label);
            return true;
        }

        if ("reload".equalsIgnoreCase(args[0])) {
            tabManager.reload();
            tabManager.start();
            sender.sendMessage(ChatColor.GREEN + "DrakesTab reloaded.");
            return true;
        }

        if ("status".equalsIgnoreCase(args[0])) {
            sender.sendMessage(ChatColor.YELLOW + "DrakesTab status:");
            sender.sendMessage(ChatColor.GRAY + "Online players: " + ChatColor.AQUA + Bukkit.getOnlinePlayers().size());
            return true;
        }

        usage(sender, label);
        return true;
    }

    private void usage(CommandSender sender, String label) {
        sender.sendMessage(ChatColor.YELLOW + "/" + label + " reload");
        sender.sendMessage(ChatColor.YELLOW + "/" + label + " status");
    }
}
