package com.example.wpractice.command;

import com.example.wpractice.WPracticePlugin;
import com.example.wpractice.model.PartyFightRequest;
import com.example.wpractice.util.StrikePracticeCache;
import com.example.wpractice.util.ValidationUtils;
import ga.strikepractice.StrikePractice;
import ga.strikepractice.fights.party.partyfights.PartyVsParty;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Обработчик команды /acceptpartyfight.
 */
public class AcceptPartyFightCommand implements CommandExecutor {
    private final WPracticePlugin plugin;
    private final StrikePracticeCache cache;
    private final PartyFightCommand partyFightCommand;

    public AcceptPartyFightCommand(WPracticePlugin plugin, StrikePracticeCache cache, PartyFightCommand partyFightCommand) {
        this.plugin = plugin;
        this.cache = cache;
        this.partyFightCommand = partyFightCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!ValidationUtils.validatePlayer(sender, "<red>Команда только для игроков!")) {
            return true;
        }
        Player player = (Player) sender;

        PartyFightRequest request = partyFightCommand.getPendingRequests().get(player.getUniqueId());
        if (request == null || request.isExpired()) {
            sendError(player, "<red>Нет активных запросов на дуэль или запрос истёк!");
            partyFightCommand.getPendingRequests().remove(player.getUniqueId());
            return true;
        }

        if (!ValidationUtils.validateNotNull(plugin.getStrikePracticeAPI(), "<red>StrikePracticeAPI не доступен!", player)) {
            return true;
        }

        Plugin strikePracticePlugin = Bukkit.getPluginManager().getPlugin("StrikePractice");
        if (!(strikePracticePlugin instanceof StrikePractice)) {
            sendError(player, "<red>Ошибка: Плагин StrikePractice не найден!");
            return true;
        }

        try {
            PartyVsParty partyVsParty = new PartyVsParty((StrikePractice) strikePracticePlugin, request.getRequesterParty(), request.getTargetParty(), request.getKit());
            partyVsParty.setArena(request.getArena());

            if (!partyVsParty.canStart()) {
                sendError(player, "<red>PartyVsParty не начат: проверьте конфигурацию StrikePractice!");
                return true;
            }

            partyVsParty.start();
            Player requester = Bukkit.getPlayer(request.getRequesterId());
            if (requester != null) {
                sendMessage(requester, "<green>Ваш запрос на дуэль пати принят игроком '" + player.getName() + "' с китом '" + request.getKit().getName() + "' на арене '" + request.getArena().getName() + "'!");
            }
            sendMessage(player, "<green>Вы приняли запрос на дуэль пати от игрока '" + (requester != null ? requester.getName() : "Неизвестный") + "' с китом '" + request.getKit().getName() + "' на арене '" + request.getArena().getName() + "'!");
            partyFightCommand.getPendingRequests().remove(player.getUniqueId());
        } catch (Exception e) {
            sendError(player, "<red>Ошибка при запуске боя: " + e.getMessage());
            plugin.getLogger().severe("Ошибка при запуске PartyVsParty: " + e.getMessage());
        }
        return true;
    }

    private void sendError(Player player, String message) {
        player.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    private void sendMessage(Player player, String message) {
        player.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }
}