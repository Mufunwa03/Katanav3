package Grim.Katanav3.commands;

import Grim.Katanav3.item.Katana;
import Grim.Katanav3.item.KatanaFactory;
import Grim.Katanav3.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class GiveKatanaCommand implements CommandExecutor, TabCompleter {

    private final KatanaFactory katanaFactory;

    public GiveKatanaCommand(KatanaFactory katanaFactory) {
        this.katanaFactory = katanaFactory;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatUtils.color("&cUsage: /givekatana <id> [player]"));
            return true;
        }

        Katana katana = katanaFactory.getKatana(args[0]);
        if (katana == null) {
            sender.sendMessage(ChatUtils.color("&cError: Katana with ID '" + args[0] + "' not found."));
            return true;
        }

        Player target = (sender instanceof Player) ? (Player) sender : null;
        if (args.length >= 2) {
            target = Bukkit.getPlayer(args[1]);
        }

        if (target == null) {
            sender.sendMessage(ChatUtils.color("&cError: Player not specified or not found."));
            return true;
        }

        target.getInventory().addItem(katana.getItemStack());
        target.sendMessage(ChatUtils.color("&aYou have received a " + katana.getItemStack().getItemMeta().getDisplayName()));
        if (target != sender) {
            sender.sendMessage(ChatUtils.color("&aGave " + target.getName() + " a " + katana.getItemStack().getItemMeta().getDisplayName()));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], katanaFactory.getAllKatanaIds(), new ArrayList<>());
        }
        if (args.length == 2) {
            return null; // Bukkit default player completion
        }
        return new ArrayList<>();
    }
}