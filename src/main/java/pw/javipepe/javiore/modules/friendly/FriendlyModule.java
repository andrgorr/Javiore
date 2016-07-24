package pw.javipepe.javiore.modules.friendly;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import pw.javipepe.javiore.Handler;
import pw.javipepe.javiore.Javiore;
import pw.javipepe.javiore.modules.DevelopmentStatus;
import pw.javipepe.javiore.modules.Module;
import pw.javipepe.javiore.modules.friendly.commands.FriendCmds;
import pw.javipepe.javiore.modules.rankManager.commands.RankManagerCmds;
import pw.javipepe.javiore.modules.rankManager.listeners.ConnectionListener;
import pw.javipepe.javiore.mysql.mysql.MySQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

/**
 * @author: Javi
 * <p>
 * Class created on 8/07/16 as part of project Javiore
 **/
public class FriendlyModule extends Module {

    @Getter public static Connection dbConnection = null;

    public Class[] commands = {
            FriendCmds.class
    };

    public Listener[] listeners = {
    };

    @Override
    public void init(){
        Handler h = new Handler();
        h.stageCommands(commands);
        h.stageListeners(listeners);

        if (!Javiore.isConnectedToDb()) {
            //DATABASE
            try {
                FileConfiguration config = Javiore.instance().getConfig();
                String host = config.getString("db.host");
                String port = config.getString("db.port");
                String name = config.getString("db.database");
                String user = config.getString("db.username");
                String pass = config.getString("db.password");

                MySQL sql = new MySQL(host, port, name, user, pass);
                dbConnection = sql.openConnection();
                Javiore.setConnectedToDb(true);
            } catch (SQLException | ClassNotFoundException e) {
                Javiore.instance().getLogger().log(Level.SEVERE, "Unable to connect to database.");
            }
        }
    }

    /**
     *  MODULE INFORMATION
     */

    @Override
    public String getName(){
        return "Friendly - Friend Manager";
    }

    @Override
    public String getDescription(){
        return "A friend manager using MySQL database!";
    }

    @Override
    public DevelopmentStatus getStatus(){
        return DevelopmentStatus.WIP;
    }

    public static boolean areFriends (Player p1, Player p2) {
        try {
            Statement statement = getDbConnection().createStatement();
            statement.executeQuery("SELECT * FROM friends WHERE ((uuid1 = '" + p1.getUniqueId() + "' AND uuid2='" + p2.getUniqueId() + "') OR (uuid1 = '" + p2.getUniqueId() + "' AND uuid2 = '" + p2.getUniqueId() + "')) AND accepted=1");
            ResultSet resultSet = statement.getResultSet();

            return resultSet.next();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

}
