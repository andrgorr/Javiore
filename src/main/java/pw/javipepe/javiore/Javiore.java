package pw.javipepe.javiore;

import com.sk89q.minecraft.util.commands.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import pw.javipepe.javiore.commands.JavioreCmd;
import pw.javipepe.javiore.modules.DevelopmentStatus;
import pw.javipepe.javiore.modules.Module;
import pw.javipepe.javiore.modules.faceRecognition.FaceRecognitionModule;
import pw.javipepe.javiore.modules.friendly.FriendlyModule;
import pw.javipepe.javiore.modules.functions.FunctionsModule;
import pw.javipepe.javiore.modules.rankManager.RankManagerModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author: Javi
 * <p>
 * Class created on 22/05/16 as part of project Javiore
 **/
public class Javiore extends JavaPlugin {

    @Getter public static Map<DevelopmentStatus, ChatColor> statusColorMap = new HashMap<>();
    @Getter public static List<Module> moduleList = new ArrayList<>();
    @Getter public static List<Module> disabledModuleList = new ArrayList<>();
    @Getter @Setter public static boolean connectedToDb = false;

    @Getter private Class[] coreCommands = {
            JavioreCmd.class
    };

    /**
     * Plugin desc.
     */

    public static String PLUGIN_NAME = "Javiore";
    public static Plugin instance() { return Bukkit.getPluginManager().getPlugin(PLUGIN_NAME); }

    public void onEnable() {
        moduleStartup();
        setupColorMap();

        this.saveDefaultConfig();
    }

    /**
     * moduleStartup() inits the modules
     */
    private void moduleStartup(){
        //Stage Javiore Core Commands
        Handler handler = new Handler();
        handler.stageCommands(coreCommands);


        Module[] modules = {
                new FunctionsModule(),
                new FaceRecognitionModule(),
                new FriendlyModule(),
                new RankManagerModule()
        };

        for (Module module : modules) {
            moduleList.add(module);
            if(!disabledModuleList.contains(module)) {
                module.init();
                Bukkit.getLogger().log(Level.INFO , "Started module " + module.getName().toUpperCase());
            }
        }


        handler.registerCommands(Handler.getStagedCommandClasses());
        handler.registerListeners(Handler.getStagedListeners());
    }

    /**
     * setupColorMap() gives k/vS to the color / dv status map
     */
    private void setupColorMap() {
        statusColorMap.put(DevelopmentStatus.FINISHED, ChatColor.GREEN);
        statusColorMap.put(DevelopmentStatus.IDEA, ChatColor.AQUA);
        statusColorMap.put(DevelopmentStatus.WIP, ChatColor.GOLD);
        statusColorMap.put(DevelopmentStatus.UNKNOWN, ChatColor.GRAY);
    }


    /**
     * sq command framework core
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        try {
            Handler.commands.execute(cmd.getName(), args, sender, sender);
        } catch (CommandPermissionsException e) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to perform that command.");
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (CommandUsageException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                sender.sendMessage(ChatColor.RED + "Number expected, text received instead.");
            } else {
                sender.sendMessage(ChatColor.RED + "There was an internal error while performing this command.");
                e.printStackTrace();
            }
        } catch (CommandException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }

        return true;
    }

    public static Module getModuleByName(String query) {
        query = query.toLowerCase().replace(" ", "");
        Module target = null;

        for (Module module : Javiore.getModuleList()) {
            if (module.getName().toLowerCase().replace(" ", "").equals(query))
                target = module;
        }

        return target;
    }
}
