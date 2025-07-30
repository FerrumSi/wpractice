package com.example.wpractice.command;

import com.example.wpractice.WPracticePlugin;
import com.example.wpractice.model.PartyFightRequest;
import com.example.wpractice.util.StrikePracticeCache;
import com.example.wpractice.util.ValidationUtils;
import ga.strikepractice.arena.Arena;
import ga.strikepractice.battlekit.BattleKit;
import ga.strikepractice.party.Party;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Обработчик команды /partyfight.
 */
public class PartyFightCommand implements CommandExecutor {
    private final WPracticePlugin plugin;
    private final StrikePracticeCache cache;
    private final Map<UUID, PartyFightRequest> pendingRequests = new ConcurrentHashMap<>();

    public PartyFightCommand(WPracticePlugin plugin, StrikePracticeCache cache) {
        this.plugin = plugin;
        this.cache = cache;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!ValidationUtils.validatePlayer(sender, "<red>Команда только для игроков!")) {
            return true;
        }
        Player player = (Player) sender;

        if (args.length < 1) {
            sendError(player, "<red>Использование: /partyfight <Лидер> [Набор]");
            return true;
        }

        if (!ValidationUtils.validateNotNull(plugin.getStrikePracticeAPI(), "<red>StrikePracticeAPI не доступен!", player)) {
            return true;
        }

        Party playerParty = Party.getParty(player);
        if (!ValidationUtils.validateNotNull(playerParty, "<red>Вы не состоите в пати!", player)) {
            return true;
        }

        String playerLeaderName = getPartyLeaderName(playerParty);
        if (playerLeaderName == null || !player.getName().equalsIgnoreCase(playerLeaderName)) {
            sendError(player, "<red>Только лидер пати может отправлять приглашения!");
            return true;
        }

        String targetLeaderName = args[0];
        Player targetLeader = Bukkit.getPlayerExact(targetLeaderName);
        if (!ValidationUtils.validateNotNull(targetLeader, "<red>Игрок '" + targetLeaderName + "' не найден или не в сети!", player)) {
            return true;
        }

        Party targetParty = Party.getParty(targetLeader);
        if (!ValidationUtils.validateNotNull(targetParty, "<red>Игрок '" + targetLeaderName + "' не состоит в пати!", player)) {
            return true;
        }

        String targetPartyLeaderName = getPartyLeaderName(targetParty);
        if (targetPartyLeaderName == null || !targetLeaderName.equalsIgnoreCase(targetPartyLeaderName)) {
            sendError(player, "<red>Игрок '" + targetLeaderName + "' не является лидером пати!");
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

        pendingRequests.put(targetLeader.getUniqueId(), new PartyFightRequest(player.getUniqueId(), playerParty, targetParty, kit, arena));
        sendMessage(player, "<green>Вы отправили запрос на дуэль с набором '" + kitName + "' игроку '" + targetLeaderName + "' на арене '" + arena.getName() + "'!");
        sendMessage(targetLeader, "<gold>Ваша пати приглашена на бой пати игрока '" + player.getName() + "' с китом '" + kitName + "' на арене '" + arena.getName() + "'! " +
                "<click:run_command:/acceptpartyfight><green>/acceptpartyfight</green></click> для принятия.");

        return true;
    }

    public Map<UUID, PartyFightRequest> getPendingRequests() {
        return pendingRequests;
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
        Component component = MiniMessage.miniMessage().deserialize(message);
        if (message.contains("<click")) {
            component = component.clickEvent(ClickEvent.runCommand("/acceptpartyfight"));
        }
        player.sendMessage(component);
    }
}