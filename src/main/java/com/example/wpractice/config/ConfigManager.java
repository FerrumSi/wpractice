package com.example.wpractice.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Управляет загрузкой и хранением конфигурационных файлов плагина.
 */
public class ConfigManager {
    private static ConfigManager instance;
    private final JavaPlugin plugin;
    private final Map<String, FileConfiguration> configs = new HashMap<>();

    private ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Получает единственный экземпляр ConfigManager (паттерн Singleton).
     *
     * @param plugin Экземпляр плагина.
     * @return Экземпляр ConfigManager.
     */
    public static ConfigManager getInstance(JavaPlugin plugin) {
        if (instance == null) {
            instance = new ConfigManager(plugin);
        }
        return instance;
    }

    /**
     * Загружает конфигурационные файлы.
     */
    public void loadConfigs() {
        loadConfig("fightmenu.yml");
        loadConfig("spectate.yml");
        loadConfig("partyfight.yml");
    }

    /**
     * Загружает отдельный конфигурационный файл.
     *
     * @param fileName Имя файла.
     */
    private void loadConfig(String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
        configs.put(fileName, YamlConfiguration.loadConfiguration(file));
    }

    /**
     * Возвращает конфигурацию по имени файла.
     *
     * @param fileName Имя файла конфигурации.
     * @return FileConfiguration или null, если файл не загружен.
     */
    public FileConfiguration getConfig(String fileName) {
        return configs.get(fileName);
    }
}