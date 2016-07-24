package pw.javipepe.javiore;

import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.CommandsManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * @author: Javi
 * <p>
 * Class created on 22/05/16 as part of project Javiore
 **/
public class Handler {

    @Getter public static List<Class> stagedCommandClasses = new ArrayList<>();
    @Getter public static List<Listener> stagedListeners = new ArrayList<>();

    public void stageCommands(Class[] classes) {
        for (Class clazz : classes) {
            if (!stagedCommandClasses.contains(clazz))
                stagedCommandClasses.add(clazz);
        }
    }

    public void stageListeners(Listener[] listeners) {
        for (Listener listener : listeners) {
            if (!stagedListeners.contains(listener))
                stagedListeners.add(listener);
        }
    }

    // DON'T USE THE ONES BELOW IN MODULE'S init()

    public static CommandsManager<CommandSender> commands;

    public void registerCommands(List<Class> classes) {
        commands = new CommandsManager<CommandSender>() {
            @Override
            public boolean hasPermission(CommandSender sender, String perm) {
                return sender instanceof ConsoleCommandSender || sender.hasPermission(perm);
            }
        };
        CommandsManagerRegistration cmdRegister = new CommandsManagerRegistration(Javiore.instance(), commands);

        for (Class c : classes) {
            cmdRegister.register(c);
            Bukkit.getLogger().log(Level.INFO, "Registered command class " + c);
        }
    }

    public void registerListeners(List<Listener> listeners) {
        PluginManager pm = Bukkit.getPluginManager();

        for (Listener l : listeners) {
            pm.registerEvents(l, Javiore.instance());
        }
    }


}
