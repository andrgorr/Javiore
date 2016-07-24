package pw.javipepe.javiore.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pw.javipepe.javiore.Javiore;
import pw.javipepe.javiore.modules.Module;

/**
 * @author: Javi
 * <p>
 * Class created on 29/05/16 as part of project Javiore
 **/
public class JavioreCmd {

    @Command(aliases = {"javiore", "jo"}, usage = "(list/reload/help) (help? <module>)", desc = "Various plugin functions", min = 1)
    public static void javiore (final CommandContext cmd, final CommandSender sender) throws CommandException {

        if (cmd.getString(0).equals("list")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&c&m--------------- &r&4&l Module List &r&8(&7" + Javiore.getModuleList().size() + "&8R&7" + (Javiore.getModuleList().size() - Javiore.getDisabledModuleList().size()) + "&8A) &c&m---------------"));
            sender.sendMessage(ChatColor.RED + "       Get specific module help with /jo help <module>");
            for (Module module : Javiore.getModuleList()) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "· " + module.getFormattedInfo()));
            }
            return;
        }

        if (cmd.getString(0).equals("reload") || cmd.getString(0).equals("rl")) {
            ((Player)sender).performCommand("reload");
            return;
        }

        if (cmd.getString(0).equals("help")) {
            if (cmd.argsLength() < 2) throw new CommandException("/javiore help <module>");

            String query = cmd.getJoinedStrings(1);
            if (Javiore.getModuleByName(query) == null) throw new CommandException("No modules matched query.");

            Javiore.getModuleByName(query).getHelp().aid(sender);
            return;
        }


        throw new CommandException("No nested command found by name '" + cmd.getString(0) + "'");
    }
}
