package com.yourname.katanas.commands;

import com.yourname.katanas.KatanasPlugin;
import com.yourname.katanas.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadKatanasCommand implements CommandExecutor {

    private final KatanasPlugin plugin;

    public ReloadKatanasCommand(KatanasPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.reloadConfig();
        plugin.getKatanaFactory().loadKatanas();
        sender.sendMessage(ChatUtils.color("&aKatanas configuration and items have been reloaded!"));
        return true;
    }
}