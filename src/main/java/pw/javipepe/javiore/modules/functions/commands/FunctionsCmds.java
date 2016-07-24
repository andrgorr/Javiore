package pw.javipepe.javiore.modules.functions.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pw.javipepe.javiore.modules.functions.util.Plot;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author: Javi
 * <p>
 * Class created on 22/05/16 as part of project Javiore
 **/
public class FunctionsCmds {

    public static List<Plot> plots = new ArrayList<>();

    @Command(aliases = {"plot"}, desc = "Create cartesian axises", usage = "<quartierSize> <name>", min = 2, max = 2)
    public static void plot (final CommandContext cmd, final CommandSender sender) throws CommandException {

        if(!(sender instanceof Player)) throw new CommandException("Sender needs to be a player");

        int quartierSize = cmd.getInteger(0);

        for (Plot plot : plots) {
            if (plot.getName().equalsIgnoreCase(cmd.getString(1))) throw new CommandException("There is already a plot with that name");
        }

        Player s = (Player) sender;
        Location initial = s.getLocation();
        Plot plot = new Plot(cmd.getString(1), initial, quartierSize);
        plots.add(plot);
        buildPlot(plot.getCentre(), plot.getQuartierSize());
    }

    @Command(aliases = {"function"}, desc = "Display a function", usage = "<name> <function>", min = 2, max = -1)
    public static void function (final CommandContext cmd, final CommandSender sender) throws CommandException, ScriptException {

        if(!(sender instanceof Player)) throw new CommandException("Sender needs to be a player");

        Plot target = null;

        for (Plot plot : plots) {
            if (plot.getName().equalsIgnoreCase(cmd.getString(0)))
                target = plot;
        }

        if (target == null) throw new CommandException("Couldn't find plot");

        target.buildFunction(cmd.getJoinedStrings(1));
    }

    @Command(aliases = {"clearplot"}, desc = "Clear a plot", usage = "<name>", min = 1)
    public static void clearplot (final CommandContext cmd, final CommandSender sender) throws CommandException, ScriptException {

        if(!(sender instanceof Player)) throw new CommandException("Sender needs to be a player");

        Plot target = null;

        for (Plot plot : plots) {
            if (plot.getName().equalsIgnoreCase(cmd.getString(0)))
                target = plot;
        }

        if (target == null) throw new CommandException("Couldn't find plot");

        target.clear();
        sender.sendMessage(ChatColor.GREEN + "Cleared function out of plot " + target.getName());
    }

    private static void buildPlot (Location location, int quartierSize) {
        try {
            //y axis
            for (int i = -quartierSize; i < (quartierSize*2)-3; i++) {
                Block block = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() + i, location.getBlockZ());
                block.setType(Material.WOOL);
                block.setData(DyeColor.BLACK.getData());
            }

            //x axis
            for (int i = -quartierSize; i < (quartierSize*2)-3; i++) {
                Block block = location.getWorld().getBlockAt(location.getBlockX() + i, location.getBlockY(), location.getBlockZ());
                block.setType(Material.WOOL);
                block.setData(DyeColor.BLACK.getData());
            }
        } catch (Exception ignored) {

        }
    }
}
