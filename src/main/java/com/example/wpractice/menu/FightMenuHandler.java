package com.example.wpractice.menu;

import com.example.wpractice.WPracticePlugin;
import com.example.wpractice.config.MenuConfig;
import com.example.wpractice.item.ItemFactory;
import com.example.wpractice.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Обработчик меню дуэлей.
 */
public class FightMenuHandler extends MenuHandler {
    private final MenuConfig config;
    private final ItemFactory itemFactory;

    public FightMenuHandler(WPracticePlugin plugin) {
        super(plugin);
        this.config = new MenuConfig(plugin.getConfigManager().getConfig("fightmenu.yml"), "fight");
        this.itemFactory = new ItemFactory(plugin.getConfigManager().getConfig("fightmenu.yml"));
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
        // Добавить логику для заполнения игроками (аналогично исходному коду)
    }

    @Override
    public void handleClick(Player player, int slot, ItemStack item) {
        // Обработка кликов по меню (будет добавлена позже)
    }
}