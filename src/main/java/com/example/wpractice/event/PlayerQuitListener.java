package com.example.wpractice.event;

import com.example.wpractice.WPracticePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Слушатель выхода игроков для очистки данных.
 */
public class PlayerQuitListener implements Listener {
    private final WPracticePlugin plugin;
    private final ConcurrentHashMap<UUID, Long> lastInteractTimes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, String> playerSortModes = new ConcurrentHashMap<>();

    public PlayerQuitListener(WPracticePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        lastInteractTimes.remove(playerId);
        playerSortModes.remove(playerId);
        // Очистка pendingRequests осуществляется в PartyFightCommand
    }
}