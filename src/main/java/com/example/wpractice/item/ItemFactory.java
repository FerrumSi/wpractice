package com.example.wpractice.item;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Фабрика для создания предметов меню.
 */
public class ItemFactory {
    private final FileConfiguration config;

    public ItemFactory(FileConfiguration config) {
        this.config = config;
    }

    /**
     * Создает информационный предмет.
     *
     * @return Построенный ItemStack.
     */
    public ItemStack createInfoItem() {
        Material material = Material.valueOf(config.getString("menu.info.type", "BOOK"));
        String displayName = config.getString("menu.info.displayname", "<yellow>Информация");
        List<String> lore = config.getStringList("menu.info.lore");
        int customModelData = config.getInt("menu.info.custom-model-data", -1);

        return new ItemBuilder(material)
                .setDisplayName(displayName)
                .setLore(lore.isEmpty() ? List.of("<red>Описание не настроено!") : lore)
                .setCustomModelData(customModelData)
                .build();
    }

    /**
     * Создает предмет для разделения пати.
     *
     * @return Построенный ItemStack.
     */
    public ItemStack createSplitPartyItem() {
        Material material = Material.valueOf(config.getString("menu.split-party.type", "DIAMOND_SWORD"));
        String displayName = config.getString("menu.split-party.displayname", "<gold>Разделить пати");
        List<String> lore = config.getStringList("menu.split-party.lore");
        int customModelData = config.getInt("menu.split-party.custom-model-data", -1);

        return new ItemBuilder(material)
                .setDisplayName(displayName)
                .setLore(lore.isEmpty() ? List.of("<gray>Нажмите, чтобы начать бой внутри пати!") : lore)
                .setCustomModelData(customModelData)
                .setPersistentDataString("action", "split_party")
                .build();
    }
}