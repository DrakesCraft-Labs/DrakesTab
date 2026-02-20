package me.jackstar.drakestab;

import me.jackstar.drakestab.economy.VaultEconomyProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class DrakesTabPlugin extends JavaPlugin {

    private TabManager tabManager;

    @Override
    public void onEnable() {
        VaultEconomyProvider economyProvider = new VaultEconomyProvider(this);
        tabManager = new TabManager(this, economyProvider);
        getServer().getPluginManager().registerEvents(tabManager, this);
        tabManager.start();
    }

    @Override
    public void onDisable() {
        if (tabManager != null) {
            tabManager.stop();
        }
    }
}
