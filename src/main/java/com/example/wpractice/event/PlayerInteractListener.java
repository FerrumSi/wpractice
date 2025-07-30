package com.example.wpractice.event;

import com.example.wpractice.WPracticePlugin;
import com.example.wpractice.util.StrikePracticeCache;
import com.example.wpractice.util.ValidationUtils;
import ga.strikepractice.api.StrikePracticeAPI;
import ga.strikepractice.party.Party;
import ga.strikepractice.playersettings.PlayerSettings;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * Слушатель взаимодействий игроков с сущностями.
 */
public class PlayerInteractListener implements Listener {
    private final WPracticePlugin plugin;
    private final StrikePracticeCache cache;

    public PlayerInteractListener(WPracticePlugin plugin, StrikePracticeCache cache) {
        this.plugin = plugin;
        this.cache = cache;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Player target) || !event.getPlayer().getWorld().getName().equalsIgnoreCase("world")) {
            return;
        }

        Player player = event.getPlayer();
        if (!player.isSneaking() || !ValidationUtils.validateNotNull(plugin.getStrikePracticeAPI(), "<red>StrikePracticeAPI не доступен!", player)) {
            return;
        }

        StrikePracticeAPI api = plugin.getStrikePracticeAPI();
        PlayerSettings settings = api.getPlayerSettings(target);
        if (settings != null && settings.isDuelRequestsDisabled()) {
            sendError(player, "<red>Игрок отключил дуэли!");
            return;
        }

        if (Party.getParty(target) != null) {
            sendError(player, "<red>Игрок в пати!");
            return;
        }

        String command = plugin.getConfigManager().getConfig("fightmenu.yml").getString("menu.item.click.command", "duel %player%").replace("%player%", target.getName());
        player.performCommand(command);
    }

    private void sendError(Player player, String message) {
        player.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }
}