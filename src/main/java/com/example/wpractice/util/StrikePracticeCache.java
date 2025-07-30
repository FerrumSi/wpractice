package com.example.wpractice.util;

import ga.strikepractice.api.StrikePracticeAPI;
import ga.strikepractice.arena.Arena;
import ga.strikepractice.battlekit.BattleKit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Кэш для данных StrikePractice API.
 */
public class StrikePracticeCache {
    private final StrikePracticeAPI api;
    private final Map<String, BattleKit> kitCache = new ConcurrentHashMap<>();
    private final Map<String, Arena> arenaCache = new ConcurrentHashMap<>();

    public StrikePracticeCache(StrikePracticeAPI api) {
        this.api = api;
        refreshCache();
    }

    /**
     * Обновляет кэш китов и арен.
     */
    public void refreshCache() {
        kitCache.clear();
        arenaCache.clear();
        api.getKits().forEach(kit -> kitCache.put(kit.getName().toLowerCase(), kit));
        api.getArenas().forEach(arena -> arenaCache.put(arena.getName().toLowerCase(), arena));
    }

    /**
     * Находит кит по имени.
     *
     * @param kitName Имя кита.
     * @return BattleKit или null, если не найден.
     */
    public BattleKit getKit(String kitName) {
        return kitCache.get(kitName.toLowerCase());
    }

    /**
     * Находит доступную арену для кита.
     *
     * @param kitName Имя кита.
     * @return Arena или null, если не найдена.
     */
    public Arena findAvailableArena(String kitName) {
        return arenaCache.values().stream()
                .filter(a -> a.getCurrentFight() == null || a.getCurrentFight().hasEnded())
                .filter(a -> a.getKits() != null && a.getKits().stream().anyMatch(k -> k.equalsIgnoreCase(kitName)))
                .findFirst()
                .orElse(null);
    }
}