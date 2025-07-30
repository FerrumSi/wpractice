package com.example.wpractice.menu;

import com.example.wpractice.WPracticePlugin;
import com.example.wpractice.config.MenuConfig;
import com.example.wpractice.item.ItemFactory;
import com.example.wpractice.util.Constants;
import ga.strikepractice.api.StrikePracticeAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Обработчик меню наблюдения за боями.
 */
public class SpectateMenuHandler extends MenuHandler {
    private final MenuConfig config;
    private final ItemFactory itemFactory;
    private final StrikePracticeAPI api;

    public SpectateMenuHandler(WPracticePlugin plugin) {
        super(plugin);
        this.config = new MenuConfig(plugin.getConfigManager().getConfig("spectate.yml"), "spectate");
        this.itemFactory = new ItemFactory(plugin.getConfigManager().getConfig("spectate.yml"));
        this.api = plugin.getStrikePracticeAPI();
    }

    /**
     * Возвращает конфигурацию меню.
     *
     * @return Экземпляр MenuConfig.
     */
    public MenuConfig getConfig() {
        return config;
    }

    @Override
    public void openMenu(Player player, int page) {
        Inventory menu = Bukkit.createInventory(null, Constants.INVENTORY_SIZE, config.getTitle());
        populateMenu(menu, player, page);
        player.openInventory(menu);
    }

    @Override
    protected void populateMenu(Inventory inventory, Player player, int page) {
        if (config.getInfoSlot() >= 0) {
            inventory.setItem(config.getInfoSlot(), itemFactory.createInfoItem());
        }
        // Добавить логику для заполнения боями (аналогично исходному populateSpectateMenu)
    }

    @Override
    public void handleClick(Player player, int slot, ItemStack item) {
        // Обработка кликов по меню (будет добавлена позже)
    }
}