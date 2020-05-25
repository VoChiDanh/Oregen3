package me.banbeucmas.oregen3.utils;

import me.banbeucmas.oregen3.Oregen3;
import me.banbeucmas.oregen3.data.DataManager;
import me.banbeucmas.oregen3.data.MaterialChooser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static me.banbeucmas.oregen3.Oregen3.getHook;

public class PluginUtils {
    public static final Random RANDOM = ThreadLocalRandom.current();

    public static OfflinePlayer getOwner(final Location loc) {
        if (!getHook().isOnIsland(loc)) {
            return null;
        }

        final UUID uuid = getHook().getIslandOwner(loc);
        if (uuid == null) {
            return null;
        }

        return Bukkit.getServer().getOfflinePlayer(uuid);
    }

    public static OfflinePlayer getOwner(final UUID uuid) {
        final UUID p = getHook().getIslandOwner(uuid);
        if (p == null) {
            return null;
        }
        return Bukkit.getServer().getOfflinePlayer(p);
    }

    public static MaterialChooser getChooser(final Location loc) {
        final Oregen3 plugin = Oregen3.getPlugin();
        MaterialChooser mc = DataManager.getChoosers().get(plugin.getConfig().getString("defaultGenerator", ""));
        if (plugin.getConfig().getBoolean("hooks.skyblock.getLowestGenerator", false)) {
            MaterialChooser lowestChooser = null;
            for (final UUID uuid : getHook().getMembers(Objects.requireNonNull(getOwner(loc)).getUniqueId())) {
                final OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
                final MaterialChooser chooser = getMaterialChooser(loc, mc, p);
                if (lowestChooser == null || lowestChooser.getPriority() > chooser.getPriority()) {
                    lowestChooser = chooser;
                }
            }
            return lowestChooser;
        }
        if (plugin.hasDependency()) {
            final OfflinePlayer p = getOwner(loc);
            if (p == null) {
                return mc;
            }
            mc = getMaterialChooser(loc, mc, p);
        }
        return mc;
    }

    public static MaterialChooser getChooser(final UUID uuid) {
        final Oregen3 plugin = Oregen3.getPlugin();
        MaterialChooser mc = DataManager.getChoosers().get(plugin.getConfig().getString("defaultGenerator"));
        if (plugin.hasDependency()) {
            final UUID p = getHook().getIslandOwner(uuid);
            if (p == null) {
                return mc;
            }
            for (final MaterialChooser chooser : DataManager.getChoosers().values()) {
                //TODO: Support island-only world?
                if (Oregen3.getPermissionManager().checkPerm(null, Bukkit.getOfflinePlayer(p), chooser.getPermission())
                        && chooser.getPriority() >= mc.getPriority()
                        && getHook().getIslandLevel(p, null) >= chooser.getLevel()) {
                    mc = chooser;
                }
            }
        }
        return mc;
    }

    private static MaterialChooser getMaterialChooser(final Location loc, MaterialChooser mc, final OfflinePlayer p) {
        for (final MaterialChooser chooser : DataManager.getChoosers().values()) {
            if (Oregen3.getPermissionManager().checkPerm(null, p, chooser.getPermission())
                    && chooser.getPriority() >= mc.getPriority()
                    && getHook().getIslandLevel(p.getUniqueId(), loc) >= chooser.getLevel()) {
                mc = chooser;
            }
        }
        return mc;
    }
}
