package me.jackstar.drakestab;

import me.jackstar.drakescraft.utils.MessageUtils;
import me.jackstar.drakescraft.utils.PlaceholderUtils;
import me.jackstar.drakestab.economy.VaultEconomyProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class TabManager implements Listener {

    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection();
    private static final int MAX_SIDEBAR_LINES = 15;

    private final JavaPlugin plugin;
    private final VaultEconomyProvider economyProvider;

    private BukkitTask task;
    private List<String> headerFrames = new ArrayList<>();
    private List<String> footerFrames = new ArrayList<>();
    private List<String> sidebarLines = new ArrayList<>();
    private String sidebarTitle = "<gradient:gold:yellow><b>DrakesCore</b></gradient>";
    private String moneyFormat = "<green>$%amount%</green>";
    private String tpsFormat = "%.2f";
    private boolean sidebarEnabled;
    private int frameIndex;
    private int intervalTicks = 20;
    private int sidebarIntervalTicks = 20;
    private int maxVisibleSidebarChars = 48;
    private int headerTick;
    private int sidebarTick;
    private final SidebarRenderer sidebarRenderer = new SidebarRenderer();

    public TabManager(JavaPlugin plugin, VaultEconomyProvider economyProvider) {
        this.plugin = plugin;
        this.economyProvider = economyProvider;
        saveDefaultConfigFile();
        reload();
    }

    public void reload() {
        FileConfiguration config = loadConfig();
        headerFrames = config.getStringList("tab.header-frames");
        footerFrames = config.getStringList("tab.footer-frames");
        intervalTicks = Math.max(1, config.getInt("tab.update-interval-ticks", 20));

        sidebarEnabled = config.getBoolean("sidebar.enabled", true);
        sidebarTitle = config.getString("sidebar.title", sidebarTitle);
        sidebarLines = config.getStringList("sidebar.lines");
        sidebarIntervalTicks = Math.max(1, config.getInt("sidebar.update-interval-ticks", intervalTicks));
        maxVisibleSidebarChars = Math.max(0, config.getInt("sidebar.max-visible-characters", 48));
        moneyFormat = config.getString("sidebar.money-format", moneyFormat);
        tpsFormat = config.getString("sidebar.tps-format", tpsFormat);

        if (sidebarLines.isEmpty()) {
            sidebarLines = defaultSidebarLines();
        }
    }

    public void start() {
        stop();
        int period = Math.max(1, Math.min(intervalTicks, sidebarIntervalTicks));
        task = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> tick(period), 20L, period);
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        sidebarRenderer.remove(event.getPlayer());
    }

    private void tick(int period) {
        headerTick += period;
        sidebarTick += period;

        if (headerTick >= intervalTicks) {
            updateHeaderFooter();
            headerTick = 0;
        }

        if (sidebarEnabled && sidebarTick >= sidebarIntervalTicks) {
            updateSidebar();
            sidebarTick = 0;
        }
    }

    private void updateHeaderFooter() {
        String headerFrame = headerFrames.isEmpty() ? "<gold>DrakesCore</gold>" : headerFrames.get(frameIndex % headerFrames.size());
        String footerFrame = footerFrames.isEmpty() ? "<gray>play.drakescraft.net</gray>" : footerFrames.get(frameIndex % footerFrames.size());

        for (Player player : Bukkit.getOnlinePlayers()) {
            String parsedHeader = applyInternalPlaceholders(player, headerFrame);
            parsedHeader = PlaceholderUtils.applyPlaceholders(player, parsedHeader);
            String parsedFooter = applyInternalPlaceholders(player, footerFrame);
            parsedFooter = PlaceholderUtils.applyPlaceholders(player, parsedFooter);
            Component header = MessageUtils.parse(parsedHeader);
            Component footer = MessageUtils.parse(parsedFooter);
            player.sendPlayerListHeaderAndFooter(header, footer);
        }
        frameIndex++;
    }

    private void updateSidebar() {
        Component title = MessageUtils.parse(sidebarTitle);
        for (Player player : Bukkit.getOnlinePlayers()) {
            List<Component> lines = new ArrayList<>();
            for (String line : sidebarLines) {
                lines.add(parseSidebarLine(player, line));
            }
            sidebarRenderer.update(player, title, lines);
        }
    }

    private Component parseSidebarLine(Player player, String raw) {
        String line = applyInternalPlaceholders(player, raw);
        line = PlaceholderUtils.applyPlaceholders(player, line);
        Component parsed = MessageUtils.parse(line);
        if (maxVisibleSidebarChars <= 0) {
            return parsed;
        }

        String plain = PlainTextComponentSerializer.plainText().serialize(parsed);
        if (plain.length() <= maxVisibleSidebarChars) {
            return parsed;
        }

        int cut = Math.max(1, maxVisibleSidebarChars - 1);
        return Component.text(plain.substring(0, cut) + "...");
    }

    private String applyInternalPlaceholders(Player player, String raw) {
        if (raw == null) {
            return "";
        }

        double tps = Bukkit.getTPS()[0];
        String tpsText = String.format(Locale.US, tpsFormat, tps);

        String moneyText = "N/A";
        if (economyProvider != null && economyProvider.isAvailable()) {
            double balance = economyProvider.getBalance(player);
            String formatted = economyProvider.format(balance);
            moneyText = moneyFormat.replace("%amount%", formatted);
        }

        return raw
                .replace("%player_name%", player.getName())
                .replace("%player%", player.getName())
                .replace("%money%", moneyText)
                .replace("%ping%", String.valueOf(player.getPing()))
                .replace("%tps%", tpsText);
    }

    private List<String> defaultSidebarLines() {
        List<String> lines = new ArrayList<>();
        lines.add("<gray>Rank:</gray> <yellow>%drakesranks_rank%</yellow>");
        lines.add("<gray>Money:</gray> <green>%money%</green>");
        lines.add("<gray>Ping:</gray> <aqua>%ping%</aqua>");
        lines.add("<gray>TPS:</gray> <green>%tps%</green>");
        lines.add("<dark_gray>drakescraft.net</dark_gray>");
        return lines;
    }

    private FileConfiguration loadConfig() {
        return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "tab.yml"));
    }

    private void saveDefaultConfigFile() {
        File file = new File(plugin.getDataFolder(), "tab.yml");
        if (!file.exists() && plugin.getResource("tab.yml") != null) {
            plugin.saveResource("tab.yml", false);
            return;
        }
        if (file.exists()) {
            return;
        }
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }
            file.createNewFile();
        } catch (IOException ignored) {
        }
    }

    private final class SidebarRenderer {

        private final Map<UUID, SidebarState> states = new HashMap<>();

        void update(Player player, Component title, List<Component> lines) {
            if (player == null) {
                return;
            }
            SidebarState state = states.computeIfAbsent(player.getUniqueId(), uuid -> new SidebarState(player));
            state.update(title, lines);
        }

        void remove(Player player) {
            if (player == null) {
                return;
            }
            SidebarState state = states.remove(player.getUniqueId());
            if (state != null) {
                state.clear();
            }
        }
    }

    private final class SidebarState {

        private final Player player;
        private final Scoreboard scoreboard;
        private final Objective objective;
        private final Team[] teams = new Team[MAX_SIDEBAR_LINES];
        private final String[] entries = new String[MAX_SIDEBAR_LINES];
        private final String[] lastLines = new String[MAX_SIDEBAR_LINES];
        private String lastTitle;

        SidebarState(Player player) {
            this.player = player;
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            if (manager == null) {
                throw new IllegalStateException("Scoreboard manager not available");
            }

            Scoreboard existing = player.getScoreboard();
            if (existing == null || existing == manager.getMainScoreboard()) {
                existing = manager.getNewScoreboard();
                player.setScoreboard(existing);
            }
            scoreboard = existing;

            Objective existingObjective = scoreboard.getObjective("drakestab");
            if (existingObjective == null) {
                existingObjective = scoreboard.registerNewObjective("drakestab", "dummy", Component.empty());
            }
            existingObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective = existingObjective;

            for (int i = 0; i < MAX_SIDEBAR_LINES; i++) {
                entries[i] = ChatColor.values()[i].toString();
                Team team = scoreboard.getTeam("dt_line_" + i);
                if (team == null) {
                    team = scoreboard.registerNewTeam("dt_line_" + i);
                }
                team.addEntry(entries[i]);
                teams[i] = team;
            }
        }

        void update(Component title, List<Component> lines) {
            String serializedTitle = LEGACY_SERIALIZER.serialize(title == null ? Component.empty() : title);
            if (lastTitle == null || !lastTitle.equals(serializedTitle)) {
                objective.displayName(title == null ? Component.empty() : title);
                lastTitle = serializedTitle;
            }

            int lineCount = Math.min(MAX_SIDEBAR_LINES, lines == null ? 0 : lines.size());
            for (int i = 0; i < lineCount; i++) {
                Component line = lines.get(i);
                String serialized = LEGACY_SERIALIZER.serialize(line == null ? Component.empty() : line);
                if (lastLines[i] == null || !lastLines[i].equals(serialized)) {
                    teams[i].prefix(line == null ? Component.empty() : line);
                    teams[i].suffix(Component.empty());
                    lastLines[i] = serialized;
                }

                objective.getScore(entries[i]).setScore(lineCount - i);
            }

            for (int i = lineCount; i < MAX_SIDEBAR_LINES; i++) {
                if (lastLines[i] != null) {
                    lastLines[i] = null;
                }
                scoreboard.resetScores(entries[i]);
            }
        }

        void clear() {
            for (int i = 0; i < MAX_SIDEBAR_LINES; i++) {
                scoreboard.resetScores(entries[i]);
            }
        }
    }
}
