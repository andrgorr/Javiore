package pw.javipepe.javiore.modules.rankManager;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import pw.javipepe.javiore.Handler;
import pw.javipepe.javiore.Javiore;
import pw.javipepe.javiore.modules.Databaseable;
import pw.javipepe.javiore.modules.DevelopmentStatus;
import pw.javipepe.javiore.modules.Module;
import pw.javipepe.javiore.modules.ModuleCommandHelp;
import pw.javipepe.javiore.modules.rankManager.commands.RankManagerCmds;
import pw.javipepe.javiore.modules.rankManager.listeners.ConnectionListener;
import pw.javipepe.javiore.mysql.mysql.MySQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

/**
 * @author: Javi
 * <p>
 * Class created on 3/07/16 as part of project Javiore
 **/
public class RankManagerModule extends Module implements Databaseable {

    @Getter public static Connection dbConnection = null;

    @Override
    public void connectDB (Connection connection) {
        dbConnection = connection;
    }

    public Class[] commands = {
            RankManagerCmds.class
    };

    public Listener[] listeners = {
            new ConnectionListener()
    };

    @Override
    public void init(){
        Handler h = new Handler();
        h.stageCommands(commands);
        h.stageListeners(listeners);
    }

    /**
     *  MODULE INFORMATION
     */

    @Override
    public String getName(){
        return "Rank Manager";
    }

    @Override
    public String getDescription(){
        return "A rank manager using MySQL database!";
    }

    @Override
    public DevelopmentStatus getStatus(){
        return DevelopmentStatus.FINISHED;
    }

    @Override
    public ModuleCommandHelp getHelp() {
        HashMap<String, String> help = new HashMap<>();
        //help.put("faceinfo <url>", "Analyse a face of a photo found in a URL you provide!");

        return new ModuleCommandHelp(help, this);
    }
    
    public static void updateRankPlayer(Player player) {
        try {
            Statement statement = RankManagerModule.getDbConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE uuid='" + player.getUniqueId() + "' LIMIT 1");
            if (!resultSet.next()) {
                Javiore.instance().getLogger().log(Level.INFO, "User " + player.getName() + " not found in DB, adding entry...");
                Statement statement1 = RankManagerModule.getDbConnection().createStatement();
                statement1.executeUpdate("INSERT INTO users (username,uuid,created_at) VALUES ('" + player.getName() + "', '" + player.getUniqueId() + "', NOW())");
                updateRankPlayer(player);
                return;
            }

            List<String> ranks = new ArrayList<>();
            List<Integer> priorities = new ArrayList<>();

            Statement statement4 = RankManagerModule.getDbConnection().createStatement();
            ResultSet usrs = statement4.executeQuery("SELECT * FROM users WHERE uuid='" + player.getUniqueId() + "' LIMIT 1");
            usrs.next();
            for (String rank : usrs.getString("ranks").split(",")) {
                ranks.add(rank);
                Statement statement2 = RankManagerModule.getDbConnection().createStatement();
                statement2.executeQuery("SELECT * FROM ranks WHERE name='" + rank + "'");
                ResultSet result = statement2.getResultSet();
                result.next();
                for (String node : result.getString("mc_perms").split(",")) {
                    player.addAttachment(Javiore.instance(), node, true);
                }
                priorities.add(result.getInt("priority"));
            }


            Statement statement3 = RankManagerModule.getDbConnection().createStatement();
            statement3.executeQuery("SELECT * FROM ranks WHERE priority=" + Collections.max(priorities) + " LIMIT 1");
            ResultSet prexixResult = statement3.getResultSet();
            prexixResult.next();

            String prefix = prexixResult.getString("mc_prefix");

            if (!player.getDisplayName().contains(prefix))
                player.setDisplayName(ChatColor.translateAlternateColorCodes('&', prefix) + player.getDisplayName());


        } catch (SQLException ex) {
            Javiore.instance().getLogger().log(Level.SEVERE, "Unable to connect to database when trying to fetch " + player.getName() + "'s information.");
            ex.printStackTrace();
        }
    }
}
