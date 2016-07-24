package pw.javipepe.javiore.modules;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Javi
 * <p>
 * Class created on 29/05/16 as part of project Javiore
 **/
public class ModuleCommandHelp {

    HashMap<String, String> help;
    Module module;

    public ModuleCommandHelp (HashMap<String, String> help, Module module) {
        this.help = help;
        this.module = module;
    }

    public void aid (CommandSender sender) {
        sender.sendMessage("Showing command help for module " + ChatColor.GOLD + "" + ChatColor.BOLD + this.module.getName() + ChatColor.RESET + "...");
        for (Map.Entry<String, String> entry: this.help.entrySet()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "· &c/" + entry.getKey() + " &r→ &7&o" + entry.getValue()));
        }
    }
}
