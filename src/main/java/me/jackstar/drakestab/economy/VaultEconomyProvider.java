package me.jackstar.drakestab.economy;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

public class VaultEconomyProvider {

    private final JavaPlugin plugin;
    private Economy economy;

    public VaultEconomyProvider(JavaPlugin plugin) {
        this.plugin = plugin;
        setup();
    }

    public boolean isAvailable() {
        return economy != null;
    }

    public double getBalance(Player player) {
        if (economy == null || player == null) {
            return 0.0D;
        }
        return economy.getBalance(player);
    }

    public String format(double amount) {
        if (economy != null) {
            return economy.format(amount);
        }
        return String.format(Locale.US, "%.2f", amount);
    }

    private void setup() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> provider = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (provider == null) {
            return;
        }
        economy = provider.getProvider();
        if (economy == null) {
            plugin.getLogger().warning("Vault detected but no Economy provider was found.");
        }
    }
}
