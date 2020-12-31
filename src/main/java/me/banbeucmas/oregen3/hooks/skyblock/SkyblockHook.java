package me.banbeucmas.oregen3.hooks.skyblock;

import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

public interface SkyblockHook {
    /**
     * Gets the island level for this player's uuid. A player location is provided if the plugin requires a location
     *
     * @param uuid the user's uuid
     * @param loc  the user's location (can be different from the uuid's location) and can be null
     *
     * @return island level by player uuid, extra provided with the location, or null (value of 0)
     */
    double getIslandLevel(UUID uuid, Location loc);

    /**
     * Gets the island owner's uuid on this location.
     *
     * @param loc the current location
     *
     * @return the owner's uuid on this island, or null if found none
     */
    UUID getIslandOwner(Location loc);

    /**
     * Gets the island owner's uuid with the given uuid.
     *
     * @param uuid the player's uuid
     *
     * @return the owner's uuid on this island, or null if found none
     */
    UUID getIslandOwner(UUID uuid);

    /**
     * Gets the uuid list of island members
     *
     * @param uuid the player's uuid
     *
     * @return the uuid list of island members
     */
    List<UUID> getMembers(UUID uuid);
}
