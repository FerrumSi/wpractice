package com.example.wpractice.util;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Утилиты для валидации входных данных.
 */
public final class ValidationUtils {
    private ValidationUtils() {
        // Запрещаем создание экземпляров
    }

    /**
     * Проверяет, является ли отправитель игроком.
     *
     * @param sender       Отправитель команды.
     * @param errorMessage Сообщение об ошибке.
     * @return true, если отправитель — игрок, иначе false.
     */
    public static boolean validatePlayer(CommandSender sender, String errorMessage) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(errorMessage));
            return false;
        }
        return true;
    }

    /**
     * Проверяет, не null ли объект.
     *
     * @param object       Объект для проверки.
     * @param errorMessage Сообщение об ошибке.
     * @param sender       Получатель сообщения.
     * @return true, если объект не null, иначе false.
     */
    public static boolean validateNotNull(Object object, String errorMessage, CommandSender sender) {
        if (object == null) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(errorMessage));
            return false;
        }
        return true;
    }
}