package pw.javipepe.javiore.modules.friendly.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pw.javipepe.javiore.modules.friendly.FriendlyModule;
import pw.javipepe.javiore.modules.friendly.util.StringUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FriendCmds {

    @Command(aliases = {"friend"}, desc = "Add/Remove/Manage friends.", usage = "(add/remove/accept/reject/list) <username>", min = 1, max = 2)
    public static void friend (final CommandContext cmd, final CommandSender sender) throws CommandException, SQLException {

        if (cmd.getString(0).equalsIgnoreCase("list")) {
            if (!(sender instanceof Player)) throw new CommandException("Console can't execute friend commands");
            Player player = (Player) sender;

            Statement statement = FriendlyModule.getDbConnection().createStatement();
            statement.executeQuery("SELECT * FROM friends WHERE uuid1 = '" + player.getUniqueId() + "' OR uuid2 = '" + player.getUniqueId() + "'");
            ResultSet resultSet = statement.getResultSet();

            if (!resultSet.next()) throw new CommandException("No friends found.");

            StringBuilder str = new StringBuilder(ChatColor.GREEN + "Online Friends: ");
            List<String> friends = new ArrayList<>();

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (FriendlyModule.areFriends(player, p)) {
                    friends.add(p.getName());
                }
            }

            String friendList = StringUtil.listToEnglishCompound(friends, ChatColor.DARK_AQUA + "", ChatColor.GRAY + "");

            str.append(friendList);

            sender.sendMessage(str.toString());
        }
    }


}
