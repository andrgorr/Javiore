package pw.javipepe.javiore.modules.rankManager.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.PermissionAttachmentInfo;
import pw.javipepe.javiore.Javiore;
import pw.javipepe.javiore.modules.rankManager.RankManagerModule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author: Javi
 * <p>
 * Class created on 3/07/16 as part of project Javiore
 **/
public class RankManagerCmds {

    @Command(aliases = {"rankmanager", "rm"}, desc = "Manage rank manager", usage = "(list)", min = 1)
    public static void rankmanager (final CommandContext cmd, final CommandSender sender) throws CommandException, SQLException {

        //rm rank
        if (cmd.getString(0).equalsIgnoreCase("rank")) {
            if (cmd.getString(1).equalsIgnoreCase("new")) {
                if (cmd.argsLength() == 4) {
                    String name = cmd.getString(2);
                    Integer priority = cmd.getInteger(3);

                    if (priority > 255) throw new CommandException("Priority can't be higher than 255.");
                    if (rankExists(name)) throw new CommandException("There is already a rank with that name.");

                    Statement statement = RankManagerModule.getDbConnection().createStatement();
                    statement.executeUpdate("INSERT INTO ranks (name, mc_perms, priority, created_at) VALUES ('" + name + " ', '', " + priority + ", NOW())");

                    sender.sendMessage(ChatColor.GREEN + "Rank " + name + " with priority " + priority + "/255 was successfully created. Use /rm rank prefix " + name + " <prefix> to set a prefix or /rm rank node " + name + " <permission node> to add a permission to the rank.");
                    return;
                } else throw new CommandException("Invalid arguments");
            }

            if (cmd.getString(1).equalsIgnoreCase("delete")) {
                if (cmd.argsLength() == 3) {
                    String name = cmd.getString(2);

                    if (!rankExists(name)) throw new CommandException("There are no ranks with that name.");

                    Statement statement = RankManagerModule.getDbConnection().createStatement();
                    statement.executeUpdate("UPDATE users SET ranks = REPLACE(ranks, '" + name + ",', '')");

                    Statement statement1 = RankManagerModule.getDbConnection().createStatement();
                    statement1.executeQuery("SELECT * FROM ranks WHERE name='" + name + "'");
                    ResultSet rs = statement1.getResultSet();
                    rs.next();
                    String oldprefix = ChatColor.translateAlternateColorCodes('&', rs.getString("mc_prefix"));

                    Statement statement2 = RankManagerModule.getDbConnection().createStatement();
                    statement2.executeUpdate("DELETE FROM ranks WHERE name='" + name + "'");

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getDisplayName().contains(oldprefix)) {
                            p.setDisplayName(p.getDisplayName().replace(oldprefix, ""));
                            RankManagerModule.updateRankPlayer(p);
                        }
                    }

                    sender.sendMessage(ChatColor.GREEN + "Rank " + name + " was successfully deleted.");
                    return;
                } else throw new CommandException("Invalid arguments");
            }

            if (cmd.getString(1).equalsIgnoreCase("prefix")) {
                if (cmd.argsLength() == 4) {
                    String name = cmd.getString(2);
                    String prefix = cmd.getString(3);

                    if (!rankExists(name)) throw new CommandException("There are no ranks with that name.");

                    Statement statement = RankManagerModule.getDbConnection().createStatement();
                    statement.executeQuery("SELECT * FROM ranks WHERE name='" + name + "'");
                    ResultSet rs = statement.getResultSet();
                    rs.next();
                    String oldprefix = ChatColor.translateAlternateColorCodes('&', rs.getString("mc_prefix"));

                    Statement statement1 = RankManagerModule.getDbConnection().createStatement();
                    statement1.executeUpdate("UPDATE ranks SET mc_prefix='" + prefix + " ' WHERE name='" + name + "'");

                    sender.sendMessage(ChatColor.GREEN + "Rank " + name + " now has the following prefix: ");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix));

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getDisplayName().contains(oldprefix)) {
                            p.setDisplayName(p.getDisplayName().replace(oldprefix, ""));
                            RankManagerModule.updateRankPlayer(p);
                        }
                    }
                    return;
                } else throw new CommandException("Invalid arguments");
            }

            if (cmd.getString(1).equalsIgnoreCase("node")) {
                if (cmd.argsLength() == 5) {
                    String name = cmd.getString(2);
                    String operator = cmd.getString(3);
                    String node = cmd.getString(4);

                    if (!operator.equals("+") && !operator.equals("-")) throw new CommandException("Invalid operator.");
                    if (!rankExists(name)) throw new CommandException("There are no ranks with that name.");

                    boolean add = operator.equals("+");

                    Statement statement = RankManagerModule.getDbConnection().createStatement();
                    statement.executeQuery("SELECT * FROM ranks WHERE name='" + name + "'");
                    ResultSet rs = statement.getResultSet();
                    rs.next();
                    String currentperms = rs.getString("mc_perms");

                    if (add && currentperms.contains(node)) throw new CommandException("Rank already has that permission.");
                    if (!add && !currentperms.contains(node)) throw new CommandException("Rank doesn't have that permission.");

                    Statement statement1 = RankManagerModule.getDbConnection().createStatement();
                    if (add)
                        statement1.executeUpdate("UPDATE ranks SET mc_perms='" + currentperms + "," + node + "' WHERE name='" + name + "'");
                    else
                        statement1.executeUpdate("UPDATE ranks SET mc_perms='" + currentperms.replace(node + ",", "").replace(node, "") + "' WHERE name='" + name + "'");

                    sender.sendMessage(ChatColor.GREEN + (add ? "Added" : "Removed") + " node " + node + " " + (add ? "to" : "from") + " " + name);

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        RankManagerModule.updateRankPlayer(p);
                    }
                    return;
                } else throw new CommandException("Invalid arguments");
            }
        }

        //rm list
        if (cmd.getString(0).equalsIgnoreCase("list")) {
            Statement statement = RankManagerModule.getDbConnection().createStatement();
            if (cmd.argsLength() == 1) {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM ranks");

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2&m----------- &r&a&l Current Ranks &2&m-----------"));
                boolean next = resultSet.next();
                while (next) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " - " + resultSet.getString("mc_prefix") + "&7&l" + capitalize(resultSet.getString("name"))));
                    next = resultSet.next();
                }
            } else if (cmd.argsLength() == 2) {
                String user = cmd.getString(1);
                if (Bukkit.getPlayer(user) == null)
                    throw new CommandException("User not found.");

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2&m----------- &r&a&l" + user + "'s Ranks &2&m-----------"));
                ResultSet usrs = statement.executeQuery("SELECT * FROM users WHERE uuid='" + Bukkit.getPlayer(user).getUniqueId() + "' LIMIT 1");
                usrs.next();
                for (String rank : usrs.getString("ranks").split(",")) {
                    Statement statement2 = RankManagerModule.getDbConnection().createStatement();
                    statement2.executeQuery("SELECT * FROM ranks WHERE name='" + rank + "'");
                    ResultSet result = statement2.getResultSet();
                    result.next();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " - " + result.getString("mc_prefix") + "&7&l" + capitalize(result.getString("name"))));
                }
            }
        }

        //rm perms
        if (cmd.getString(0).equalsIgnoreCase("perms")) {
            Statement statement = RankManagerModule.getDbConnection().createStatement();
            if (cmd.argsLength() == 2) {
                String rankname = cmd.getString(1);

                if (!rankExists(rankname)) throw new CommandException("There are no ranks with that name.");

                statement.executeQuery("SELECT * FROM ranks WHERE name='" + rankname + "'");
                ResultSet resultSet = statement.getResultSet();
                resultSet.next();

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2&m----------- &r&a&l " + rankname + " Permissions &2&m-----------"));
                String[] nodes = resultSet.getString("mc_perms").split(",");
                for (int i = 0; i < nodes.length; i++) {
                    sender.sendMessage(ChatColor.AQUA + nodes[i]);
                }
            } else if (cmd.argsLength() == 3 && cmd.getString(1).equals("user")) {
                String user = cmd.getString(2);

                if (Bukkit.getPlayer(user) == null) throw new CommandException("Player not found");

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2&m----------- &r&a&l " + user + "'s Permissions &2&m-----------"));
                for (PermissionAttachmentInfo pai : Bukkit.getPlayer(user).getEffectivePermissions()) {
                    sender.sendMessage(ChatColor.AQUA + pai.getPermission());
                }
            } else throw new CommandException("Invalid arguments.");
        }

        //rm user (user) +/- (rank)
        if (cmd.getString(0).equalsIgnoreCase("user")) {
            String user = cmd.getString(1);
            if (Bukkit.getPlayer(user) == null)
                throw new CommandException("User not found.");

            Player player = Bukkit.getPlayer(user);

            String operator = cmd.getString(2);
            if (!operator.equals("+") && !operator.equals("-"))
                throw new CommandException("Invalid operator.");

            boolean add = operator.equals("+");
            String rank = cmd.getString(3);

            if (!rankExists(rank))
                throw new CommandException("That rank doesn't exist. Use /rm list for a list of available ranks");

            if (add && hasRank(player, rank))
                throw new CommandException("Player already has that rank");

            if (!add && !hasRank(player, rank))
                throw new CommandException("Player doewn't have that rank.");

            Statement statement = RankManagerModule.getDbConnection().createStatement();

            if (add)
                statement.executeUpdate("UPDATE users SET ranks = '" + getRanks(player) + rank + ",' WHERE uuid='" + player.getUniqueId() + "'");
            else {
                statement.executeUpdate("UPDATE users SET ranks = '" + getRanks(player).replace(rank + ",", "") + "' WHERE uuid='" + player.getUniqueId() + "'");

                //clear perms
                Statement perms = RankManagerModule.getDbConnection().createStatement();
                perms.executeQuery("SELECT * FROM ranks WHERE name='" + rank + "'");
                ResultSet rs = perms.getResultSet();
                rs.next();
                for (String node : rs.getString("mc_perms").split(",")) {
                    player.addAttachment(Javiore.instance(), node, false);
                }

                //clear display name

                player.setDisplayName(player.getDisplayName().replace(ChatColor.translateAlternateColorCodes('&', rs.getString("mc_prefix")), ""));
                System.out.println("Player's disp name is now " + player.getDisplayName());
            }

            RankManagerModule.updateRankPlayer(player);
            sender.sendMessage(ChatColor.GREEN + "Updated " + player.getName() + "'s ranks!");
        }
    }

    private static String capitalize (String string) {
        return string.substring(0,1).toUpperCase() + string.substring(1);
    }

    private static String getRanks (Player p) {
        try {
            Statement statement = RankManagerModule.getDbConnection().createStatement();
            ResultSet usrs = statement.executeQuery("SELECT * FROM users WHERE uuid='" + p.getUniqueId() + "' LIMIT 1");
            usrs.next();
            return usrs.getString("ranks").toLowerCase();
        } catch (SQLException ex) {}
        return "";
    }

    private static boolean hasRank (Player p, String rank) {
        try {
            Statement statement = RankManagerModule.getDbConnection().createStatement();
            ResultSet usrs = statement.executeQuery("SELECT * FROM users WHERE uuid='" + p.getUniqueId() + "' LIMIT 1");
            usrs.next();
            return usrs.getString("ranks").toLowerCase().contains(rank.toLowerCase());
        } catch (SQLException ex) {}
        return false;
    }

    private static boolean rankExists (String query) {
        try {
            Statement statement = RankManagerModule.getDbConnection().createStatement();
            ResultSet usrs = statement.executeQuery("SELECT * FROM ranks WHERE name='" + query.toLowerCase() + "'");
            return usrs.next();
        } catch (SQLException ex) {}

        return false;
    }
}
