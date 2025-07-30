package com.example.wpractice.event;

import com.example.wpractice.WPracticePlugin;
import com.example.wpractice.command.AcceptPartyFightCommand;
import com.example.wpractice.command.PartyCommand;
import com.example.wpractice.command.PartyFightCommand;
import com.example.wpractice.util.StrikePracticeCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;

/**
 * Слушатель команд игроков для перехвата специфичных команд.
 */
public class PlayerCommandListener implements Listener {
    private final WPracticePlugin plugin;
    private final PartyCommand partyCommand;
    private final PartyFightCommand partyFightCommand;
    private final AcceptPartyFightCommand acceptPartyFightCommand;

    public PlayerCommandListener(WPracticePlugin plugin) {
        this.plugin = plugin;
        StrikePracticeCache cache = plugin.getStrikePracticeCache();
        this.partyCommand = new PartyCommand(plugin, cache);
        this.partyFightCommand = new PartyFightCommand(plugin, cache);
        this.acceptPartyFightCommand = new AcceptPartyFightCommand(plugin, cache, partyFightCommand);
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().toLowerCase().trim();
        String[] args = message.split("\\s+");
        if (args.length == 0) return;

        event.setCancelled(true);
        if (message.startsWith("/party split") || message.startsWith("/partysplit")) {
            partyCommand.onCommand(event.getPlayer(), null, "party", Arrays.copyOfRange(args, 1, args.length));
        } else if (message.startsWith("/partyfight") || message.startsWith("/pfight")) {
            partyFightCommand.onCommand(event.getPlayer(), null, "partyfight", Arrays.copyOfRange(args, 1, args.length));
        } else if (message.equals("/acceptpartyfight")) {
            acceptPartyFightCommand.onCommand(event.getPlayer(), null, "acceptpartyfight", new String[]{});
        }
    }
}