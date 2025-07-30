package com.example.wpractice.item;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Построитель предметов для инвентаря Minecraft.
 */
public class ItemBuilder {
    private final ItemStack item;
    private final ItemMeta meta;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    /**
     * Создает новый построитель предметов.
     *
     * @param material Материал предмета.
     */
    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = Objects.requireNonNull(item.getItemMeta(), "Item meta cannot be null");
    }

    /**
     * Устанавливает отображаемое имя предмета.
     *
     * @param name Имя в формате MiniMessage.
     * @return Экземпляр ItemBuilder для цепочного вызова.
     */
    public ItemBuilder setDisplayName(String name) {
        meta.displayName(miniMessage.deserialize("<reset><!italic>" + name));
        return this;
    }

    /**
     * Устанавливает описание (lore) предмета.
     *
     * @param lore Список строк в формате MiniMessage.
     * @return Экземпляр ItemBuilder для цепочного вызова.
     */
    public ItemBuilder setLore(List<String> lore) {
        meta.lore(lore.stream()
                .filter(Objects::nonNull)
                .map(line -> miniMessage.deserialize("<reset><!italic>" + line))
                .collect(Collectors.toList()));
        return this;
    }

    /**
     * Устанавливает пользовательскую модель предмета.
     *
     * @param customModelData Значение custom-model-data.
     * @return Экземпляр ItemBuilder.
     */
    public ItemBuilder setCustomModelData(int customModelData) {
        if (customModelData >= 0) meta.setCustomModelData(customModelData);
        return this;
    }

    /**
     * Устанавливает данные в PersistentDataContainer.
     *
     * @param key   Ключ данных.
     * @param value Значение данных.
     * @return Экземпляр ItemBuilder.
     */
    public ItemBuilder setPersistentDataString(String key, String value) {
        meta.getPersistentDataContainer().set(new NamespacedKey("wpractice", key), PersistentDataType.STRING, value);
        return this;
    }

    /**
     * Создает готовый предмет.
     *
     * @return Построенный ItemStack.
     */
    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
}