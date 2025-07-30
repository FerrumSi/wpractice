package com.example.wpractice.command;

import com.example.wpractice.WPracticePlugin;
import com.example.wpractice.util.StrikePracticeCache;
import com.example.wpractice.util.ValidationUtils;
import ga.strikepractice.StrikePractice;
import ga.strikepractice.arena.Arena;
import ga.strikepractice.battlekit.BattleKit;
import ga.strikepractice.fights.party.partyfights.PartySplit;
import ga.strikepractice.party.Party;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Обработчик команды /party.
 */
public class PartyCommand implements CommandExecutor {
    private final WPracticePlugin plugin;
    private final StrikePracticeCache cache;

    public PartyCommand(WPracticePlugin plugin, StrikePracticeCache cache) {
        this.plugin = plugin;
        this.cache = cache;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!ValidationUtils.validatePlayer(sender, "<red>Команда только для игроков!")) {
            return true;
        }
        Player player = (Player) sender;

        if (args.length < 1 || !args[0].equalsIgnoreCase("split")) {
            sendError(player, "<red>Использование: /party split <Набор>");
            return true;
        }

        if (!ValidationUtils.validateNotNull(plugin.getStrikePracticeAPI(), "<red>StrikePracticeAPI не доступен!", player)) {
            return true;
        }

        Party party = Party.getParty(player);
        if (!ValidationUtils.validateNotNull(party, "<red>Вы не состоите в пати!", player)) {
            return true;
        }

        if (party.getPlayers().size() < 2) {
            sendError(player, "<red>В пати должно быть минимум 2 игрока для Party Split!");
            return true;
        }

        String leaderName = getPartyLeaderName(party);
        if (leaderName == null || !player.getName().equalsIgnoreCase(leaderName)) {
            sendError(player, "<red>Только лидер пати может запускать Party Split!");
            return true;
        }

        String kitName = args.length > 1 ? args[1] : plugin.getConfigManager().getConfig("partyfight.yml").getString("default_kit", "classic");
        BattleKit kit = cache.getKit(kitName);
        if (!ValidationUtils.validateNotNull(kit, "<red>Кит '" + kitName + "' не найден!", player)) {
            return true;
        }

        Arena arena = cache.findAvailableArena(kitName);
        if (!ValidationUtils.validateNotNull(arena, "<red>Нет доступных арен для кита '" + kitName + "'!", player)) {
            return true;
        }

        Plugin strikePracticePlugin = plugin.getServer().getPluginManager().getPlugin("StrikePractice");
        if (!(strikePracticePlugin instanceof StrikePractice)) {
            sendError(player, "<red>Ошибка: Плагин StrikePractice не найден!");
            return true;
        }

        try {
            PartySplit partySplit = new PartySplit((StrikePractice) strikePracticePlugin, party, kit);
            partySplit.setArena(arena);

            if (!partySplit.canStart()) {
                sendError(player, "<red>Party Split не начат: проверьте конфигурацию StrikePractice!");
                return true;
            }

            partySplit.start();
            sendMessage(player, "<green>Party Split бой начат с китом '" + kitName + "'!");
        } catch (Exception e) {
            sendError(player, "<red>Ошибка при запуске Party Split: " + e.getMessage());
            plugin.getLogger().severe("Ошибка при запуске Party Split: " + e.getMessage());
        }
        return true;
    }

    private String getPartyLeaderName(Party party) {
        return party.getPlayers().stream()
                .filter(p -> Party.getParty(p) == party)
                .map(Player::getName)
                .findFirst()
                .orElse(null);
    }

    private void sendError(Player player, String message) {
        player.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    private void sendMessage(Player player, String message) {
        player.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }
}