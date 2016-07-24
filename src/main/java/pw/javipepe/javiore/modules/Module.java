package pw.javipepe.javiore.modules;


import org.bukkit.ChatColor;
import pw.javipepe.javiore.Javiore;

import java.sql.SQLException;

/**
 * @author: Javi
 * <p>
 * Class created on 22/05/16 as part of project Javiore
 **/
public class Module {


    /**
     * Initializes module (registers commands and listeners)
     */
    public void init(){}

    /**
     * @return String the modules name
     */
    public String getName(){ return ""; }

    /**
     * @return String the modules description
     */
    public String getDescription(){ return ""; }

    /**
     * @return Module's dev status
     */
    public DevelopmentStatus getStatus() { return DevelopmentStatus.UNKNOWN; }

    /**
     * @return Formatted info on module
     */
    public String getFormattedInfo() {
        ChatColor onoff = Javiore.getDisabledModuleList().contains(this) ? ChatColor.DARK_RED : ChatColor.GREEN;

        return Javiore.getStatusColorMap().get(getStatus()) + getStatus().toString() + "&7 » " + ChatColor.RESET + onoff + "" + ChatColor.BOLD + getName() + ChatColor.GRAY + ": " + ChatColor.GOLD + "" + ChatColor.ITALIC + getDescription();
    }

    public ModuleCommandHelp getHelp() {
        return null;
    }

}
