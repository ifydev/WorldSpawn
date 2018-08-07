package me.ifydev.worldspawn.spigot.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Innectic
 * @since 08/07/2018
 */
public class MiscUtil {

    public static String fixColors(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String[] getRemainingArgs(int starting, String[] arguments) {
        List<String> args = new ArrayList<>(Arrays.asList(arguments));
        return args.subList(starting, args.size()).toArray(new String[]{});
    }

    public static boolean isInt(String... checking) {
        try {
            for (String check : checking) Integer.parseInt(check);
        } catch (NumberFormatException ignored) return false;
        return true;
    }
}
