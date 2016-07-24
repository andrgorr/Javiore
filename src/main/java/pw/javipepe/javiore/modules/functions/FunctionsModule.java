package pw.javipepe.javiore.modules.functions;

import lombok.Getter;
import pw.javipepe.javiore.Handler;
import pw.javipepe.javiore.modules.DevelopmentStatus;
import pw.javipepe.javiore.modules.Module;
import pw.javipepe.javiore.modules.ModuleCommandHelp;
import pw.javipepe.javiore.modules.functions.commands.FunctionsCmds;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Javi
 * <p>
 * Class created on 22/05/16 as part of project Javiore
 **/
public class FunctionsModule extends Module {


    public Class[] commands = {
        FunctionsCmds.class
    };

    @Override
    public void init() {
        Handler h = new Handler();
        h.stageCommands(commands);
    }


    /**
     *  MODULE INFORMATION
     */

    @Override
    public String getName(){
        return "Functions";
    }

    @Override
    public String getDescription(){
        return "Create cartesian plots and represent math functions in it!";
    }

    @Override
    public DevelopmentStatus getStatus(){
        return DevelopmentStatus.WIP;
    }

    @Override
    public ModuleCommandHelp getHelp() {
        HashMap<String, String> help = new HashMap<>();
        help.put("plot <quartierSize> <name>", "Create a plot of a set quartier size and give it a name");
        help.put("function <plotname> <function>", "Display a function in the plot you wish");
        help.put("clearplot <plotname>", "Clear out all the functions displayed on a plot");

        return new ModuleCommandHelp(help, this);
    }
}
