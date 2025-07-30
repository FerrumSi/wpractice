package com.example.wpractice.command;

import com.example.wpractice.WPracticePlugin;
import com.example.wpractice.menu.FightMenuHandler;
import com.example.wpractice.menu.PartyFightMenuHandler;
import com.example.wpractice.menu.SpectateMenuHandler;
import com.example.wpractice.util.StrikePracticeCache;
import com.example.wpractice.util.ValidationUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Обработчик команды /wpractice.
 */
public class WPracticeCommand implements CommandExecutor {
    private final WPracticePlugin plugin;
    private final StrikePracticeCache cache;
    private final FightMenuHandler fightMenuHandler;
    private final SpectateMenuHandler spectateMenuHandler;
    private final PartyFightMenuHandler partyFightMenuHandler;

    public WPracticeCommand(WPracticePlugin plugin, StrikePracticeCache cache) {
        this.plugin = plugin;
        this.cache = cache;
        this.fightMenuHandler = new FightMenuHandler(plugin);
        this.spectateMenuHandler = new SpectateMenuHandler(plugin);
        this.partyFightMenuHandler = new PartyFightMenuHandler(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!ValidationUtils.validatePlayer(sender, "<red>Команда только для игроков!")) {
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            sendError(player, "<red>Использование: /wpractice [fightmenu|spectate|partyfight|reload]");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "fightmenu":
                fightMenuHandler.openMenu(player, 1);
                plugin.getLogger().info("Игрок " + player.getName() + " открыл меню дуэлей.");
                return true;
            case "spectate":
                spectateMenuHandler.openMenu(player, 1);
                plugin.getLogger().info("Игрок " + player.getName() + " открыл меню наблюдения.");
                return true;
            case "partyfight":
                partyFightMenuHandler.openMenu(player, 1);
                plugin.getLogger().info("Игрок " + player.getName() + " открыл меню пати.");
                return true;
            case "reload":
                plugin.getConfigManager().loadConfigs();
                cache.refreshCache();
                sendMessage(player, "<green>Конфигурация перезагружена!");
                plugin.getLogger().info("Конфигурация перезагружена игроком " + player.getName());
                return true;
            default:
                sendError(player, "<red>Использование: /wpractice [fightmenu|spectate|partyfight|reload]");
                return false;
        }
    }

    private void sendError(Player player, String message) {
        player.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    private void sendMessage(Player player, String message) {
        player.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }
}