package com.example.wpractice.util;

public final class Constants {
    public static final int INVENTORY_SIZE = 54;
    public static final long REQUEST_TIMEOUT = 30_000; // 30 секунд
    public static final String DEFAULT_SORT_MODE = "all";
    public static final String DEFAULT_KIT = "classic";
    public static final String WORLD_NAME = "world";
    public static final double DEFAULT_MAX_DISTANCE = 100.0;
    public static final String FIGHT_MENU_TITLE = "<black>Создать сражение";
    public static final String SPECTATE_MENU_TITLE = "<black>Наблюдение за сражениями";
    public static final String PARTY_FIGHT_MENU_TITLE = "<black>Сражения пати";

    private Constants() {
        // Запрещаем создание экземпляров
    }
}