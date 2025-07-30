package com.example.wpractice.command;

import com.example.wpractice.WPracticePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

/**
 * Обработчик команд плагина WPractice.
 * Регистрирует и маршрутизирует команды к соответствующим обработчикам.
 */
public class CommandHandler implements CommandExecutor {
    private final WPracticePlugin plugin;
    private final Map<String, CommandExecutor> commands = new HashMap<>();

    public CommandHandler(WPracticePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Регистрирует команду.
     *
     * @param commandName Имя команды.
     * @param executor    Обработчик команды.
     */
    public void registerCommand(String commandName, CommandExecutor executor) {
        commands.put(commandName.toLowerCase(), executor);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CommandExecutor executor = commands.get(command.getName().toLowerCase());
        if (executor == null) {
            sender.sendMessage("Команда не найдена!");
            return false;
        }
        return executor.onCommand(sender, command, label, args);
    }
}