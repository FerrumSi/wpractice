package com.example.wpractice.menu;

import com.example.wpractice.WPracticePlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Абстрактный класс для обработки меню.
 */
public abstract class MenuHandler {
    protected final WPracticePlugin plugin;

    protected MenuHandler(WPracticePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Открывает меню для игрока.
     *
     * @param player Игрок, открывающий меню.
     * @param page   Номер страницы.
     */
    public abstract void openMenu(Player player, int page);

    /**
     * Заполняет инвентарь элементами меню.
     *
     * @param inventory Инвентарь для заполнения.
     * @param player    Игрок, для которого заполняется меню.
     * @param page      Номер страницы.
     */
    protected abstract void populateMenu(Inventory inventory, Player player, int page);

    /**
     * Обрабатывает клик по элементу меню.
     *
     * @param player Игрок, кликнувший по элементу.
     * @param slot   Слот, по которому кликнули.
     * @param item   Элемент, по которому кликнули.
     */
    public abstract void handleClick(Player player, int slot, ItemStack item);
}