package me.jackstar.drakestab;

import me.jackstar.drakestab.commands.DrakesTabCommand;
import me.jackstar.drakestab.economy.VaultEconomyProvider;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class DrakesTabPlugin extends JavaPlugin {

    private static final String[] DRAGON_BANNER = {
            "              / \\  //\\",
            "      |\\___/|      /   \\//  \\\\",
            "      /O  O  \\__  /    //  | \\ \\",
            "     /     /  \\/_/    //   |  \\  \\",
            "     \\_^_\\'/   \\/_   //    |   \\   \\"
    };

    private TabManager tabManager;

    @Override
    public void onEnable() {
        logDragonBanner("DrakesTab");
        logLoading("Initializing economy provider");
        VaultEconomyProvider economyProvider = new VaultEconomyProvider(this);
        logLoading("Initializing tab manager");
        tabManager = new TabManager(this, economyProvider);
        logLoading("Registering listeners");
        getServer().getPluginManager().registerEvents(tabManager, this);
        PluginCommand tabCommand = getCommand("drakestab");
        if (tabCommand != null) {
            tabCommand.setExecutor(new DrakesTabCommand(tabManager));
        } else {
            getLogger().warning("Command 'drakestab' not found in plugin.yml.");
        }
        logLoading("Starting tab scheduler");
        tabManager.start();
        getLogger().info("[Ready] DrakesTab enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("[Shutdown] DrakesTab stopping...");
        if (tabManager != null) {
            tabManager.stop();
        }
        getLogger().info("[Shutdown] DrakesTab disabled.");
    }

    private void logLoading(String step) {
        getLogger().info("[Loading] " + step + "...");
    }

    private void logDragonBanner(String pluginName) {
        getLogger().info("========================================");
        getLogger().info(" " + pluginName + " - loading");
        for (String line : DRAGON_BANNER) {
            getLogger().info(line);
        }
        getLogger().info("========================================");
    }
}
