package com.example.wpractice.config;

import com.example.wpractice.util.Constants;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Модель конфигурации меню.
 */
public class MenuConfig {
    private final Component title;
    private final int infoSlot;
    private final int sortSlot;
    private final int splitPartySlot;
    private final int nextPageSlot;
    private final int prevPageSlot;
    private final List<Integer> itemSlots;
    private final double maxDistance;
    private final String defaultKit;

    public MenuConfig(FileConfiguration config, String menuType) {
        this.title = MiniMessage.miniMessage().deserialize(
                "<reset><!italic>" + config.getString("menu.title", getDefaultTitle(menuType)));
        this.infoSlot = validateSlot(config.getInt("menu.info.slot", 4), menuType + " info");
        this.sortSlot = validateSlot(config.getInt("menu.sort.slot", 49), menuType + " sort");
        this.splitPartySlot = validateSlot(config.getInt("menu.split-party.slot", 49), menuType + " split-party");
        this.nextPageSlot = validateSlot(config.getInt("menu.next-page.slot", 53), menuType + " next-page");
        this.prevPageSlot = validateSlot(config.getInt("menu.prev-page.slot", 45), menuType + " prev-page");
        this.itemSlots = parseSlots(config, menuType);
        this.maxDistance = config.getDouble("max-distance", Constants.DEFAULT_MAX_DISTANCE);
        this.defaultKit = config.getString("default_kit", Constants.DEFAULT_KIT);
    }

    /**
     * Возвращает заголовок меню.
     *
     * @return Component заголовка.
     */
    public Component getTitle() {
        return title;
    }

    /**
     * Возвращает слот информационного предмета.
     *
     * @return Номер слота.
     */
    public int getInfoSlot() {
        return infoSlot;
    }

    /**
     * Возвращает слот предмета для сортировки.
     *
     * @return Номер слота.
     */
    public int getSortSlot() {
        return sortSlot;
    }

    /**
     * Возвращает слот предмета для разделения пати.
     *
     * @return Номер слота.
     */
    public int getSplitPartySlot() {
        return splitPartySlot;
    }

    /**
     * Возвращает слот кнопки следующей страницы.
     *
     * @return Номер слота.
     */
    public int getNextPageSlot() {
        return nextPageSlot;
    }

    /**
     * Возвращает слот кнопки предыдущей страницы.
     *
     * @return Номер слота.
     */
    public int getPrevPageSlot() {
        return prevPageSlot;
    }

    /**
     * Возвращает список слотов для предметов.
     *
     * @return Список номеров слотов.
     */
    public List<Integer> getItemSlots() {
        return itemSlots;
    }

    /**
     * Возвращает максимальное расстояние.
     *
     * @return Значение maxDistance.
     */
    public double getMaxDistance() {
        return maxDistance;
    }

    /**
     * Возвращает стандартный кит.
     *
     * @return Имя кита.
     */
    public String getDefaultKit() {
        return defaultKit;
    }

    private String getDefaultTitle(String menuType) {
        return switch (menuType) {
            case "fight" -> Constants.FIGHT_MENU_TITLE;
            case "spectate" -> Constants.SPECTATE_MENU_TITLE;
            case "partyfight" -> Constants.PARTY_FIGHT_MENU_TITLE;
            default -> "<red>Неизвестное меню";
        };
    }

    private int validateSlot(int slot, String context) {
        if (slot < 0 || slot >= Constants.INVENTORY_SIZE) {
            System.out.println("Неверный слот для " + context + ": " + slot);
            return -1;
        }
        return slot;
    }

    private List<Integer> parseSlots(FileConfiguration config, String menuType) {
        List<Integer> slots = new ArrayList<>();
        List<String> slotRanges = config.getStringList("slots");
        if (slotRanges.isEmpty()) {
            System.out.println("Слоты не указаны в конфиге для " + menuType + ". Используются слоты 10-16.");
            for (int i = 10; i <= 16; i++) slots.add(i);
            return slots;
        }

        for (String range : slotRanges) {
            if (range == null || range.trim().isEmpty()) continue;
            String[] parts = range.split("-");
            try {
                if (parts.length == 1) {
                    int slot = Integer.parseInt(parts[0].trim());
                    if (slot >= 0 && slot < Constants.INVENTORY_SIZE) slots.add(slot);
                } else if (parts.length == 2) {
                    int start = Integer.parseInt(parts[0].trim());
                    int end = Integer.parseInt(parts[1].trim());
                    if (start <= end && start >= 0 && end < Constants.INVENTORY_SIZE) {
                        for (int i = start; i <= end; i++) slots.add(i);
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат слотов в " + menuType + ": " + range);
            }
        }
        return slots.isEmpty() ? List.of(10, 11, 12, 13, 14, 15, 16) : slots;
    }
}