package com.example.wpractice.event;

import com.example.wpractice.WPracticePlugin;
import com.example.wpractice.menu.FightMenuHandler;
import com.example.wpractice.menu.PartyFightMenuHandler;
import com.example.wpractice.menu.SpectateMenuHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Слушатель событий кликов по инвентарю.
 */
public class InventoryClickListener implements Listener {
    private final WPracticePlugin plugin;
    private final FightMenuHandler fightMenuHandler;
    private final SpectateMenuHandler spectateMenuHandler;
    private final PartyFightMenuHandler partyFightMenuHandler;

    public InventoryClickListener(WPracticePlugin plugin) {
        this.plugin = plugin;
        this.fightMenuHandler = new FightMenuHandler(plugin);
        this.spectateMenuHandler = new SpectateMenuHandler(plugin);
        this.partyFightMenuHandler = new PartyFightMenuHandler(plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player) || event.getInventory().getHolder() != null) {
            return;
        }

        Component title = event.getView().title();
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getItemMeta() == null) {
            return;
        }

        event.setCancelled(true);

        if (title.equals(fightMenuHandler.getConfig().getTitle())) {
            fightMenuHandler.handleClick(player, event.getSlot(), clickedItem);
        } else if (title.equals(spectateMenuHandler.getConfig().getTitle())) {
            spectateMenuHandler.handleClick(player, event.getSlot(), clickedItem);
        } else if (title.equals(partyFightMenuHandler.getConfig().getTitle())) {
            partyFightMenuHandler.handleClick(player, event.getSlot(), clickedItem);
        }
    }
}