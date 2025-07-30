package com.example.wpractice;

import com.example.wpractice.command.*;
import com.example.wpractice.config.ConfigManager;
import com.example.wpractice.event.InventoryClickListener;
import com.example.wpractice.event.PlayerCommandListener;
import com.example.wpractice.event.PlayerInteractListener;
import com.example.wpractice.event.PlayerQuitListener;
import com.example.wpractice.util.StrikePracticeCache;
import ga.strikepractice.StrikePractice;
import ga.strikepractice.api.StrikePracticeAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Главный класс плагина WPractice.
 * Отвечает за инициализацию плагина, регистрацию команд и событий.
 */
public class WPracticePlugin extends JavaPlugin {

    private ConfigManager configManager;
    private StrikePracticeCache strikePracticeCache;
    private StrikePracticeAPI strikePracticeAPI;

    @Override
    public void onEnable() {
        // Инициализация конфигураций
        configManager = ConfigManager.getInstance(this);
        configManager.loadConfigs();
        getLogger().info("Конфигурации загружены.");

        // Инициализация StrikePractice API
        initializeStrikePracticeAPI();
        strikePracticeCache = new StrikePracticeCache(strikePracticeAPI);
        getLogger().info("StrikePractice API инициализирован.");

        // Регистрация команд
        CommandHandler commandHandler = new CommandHandler(this);
        commandHandler.registerCommand("wpractice", new WPracticeCommand(this, strikePracticeCache));
        commandHandler.registerCommand("party", new PartyCommand(this, strikePracticeCache));
        commandHandler.registerCommand("partyfight", new PartyFightCommand(this, strikePracticeCache));
        commandHandler.registerCommand("acceptpartyfight", new AcceptPartyFightCommand(this, strikePracticeCache, new PartyFightCommand(this, strikePracticeCache)));
        getCommand("wpractice").setExecutor(commandHandler);
        getCommand("party").setExecutor(commandHandler);
        getCommand("partyfight").setExecutor(commandHandler);
        getCommand("acceptpartyfight").setExecutor(commandHandler);

        // Регистрация слушателей событий
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this, strikePracticeCache), this);
        getServer().getPluginManager().registerEvents(new PlayerCommandListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);

        getLogger().info("Плагин WPractice успешно запущен.");
    }

    /**
     * Возвращает менеджер конфигураций.
     *
     * @return Экземпляр ConfigManager.
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * Возвращает кэш StrikePractice.
     *
     * @return Экземпляр StrikePracticeCache.
     */
    public StrikePracticeCache getStrikePracticeCache() {
        return strikePracticeCache;
    }

    /**
     * Возвращает API StrikePractice.
     *
     * @return Экземпляр StrikePracticeAPI или null, если API недоступен.
     */
    public StrikePracticeAPI getStrikePracticeAPI() {
        return strikePracticeAPI;
    }

    /**
     * Инициализирует StrikePractice API.
     */
    private void initializeStrikePracticeAPI() {
        if (Bukkit.getPluginManager().getPlugin("StrikePractice") == null) {
            getLogger().severe("StrikePractice не найден. Плагин не будет работать корректно.");
            strikePracticeAPI = null;
            return;
        }

        try {
            strikePracticeAPI = StrikePractice.getAPI();
            getLogger().info("StrikePracticeAPI инициализирован.");
        } catch (Exception e) {
            getLogger().severe("Ошибка инициализации StrikePracticeAPI: " + e.getMessage());
            strikePracticeAPI = null;
        }
    }
}