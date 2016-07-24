package pw.javipepe.javiore.modules.functions.util;

import lombok.Getter;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.Wool;

import javax.script.ScriptException;

/**
 * @author: Javi
 * <p>
 * Class created on 23/05/16 as part of project Javiore
 **/
public class Plot {

    @Getter String name;
    @Getter Location centre;
    @Getter int quartierSize;

    public Plot (String name, Location centre, int quartierSize) {
        this.name = name;
        this.centre = centre;
        this.quartierSize = quartierSize;
    }

    public void buildFunction(String function) throws ScriptException {
        for (int i = -quartierSize; i < (2*quartierSize)-3; i++) {
            int y = Math.round(MathExpressionParserUtil.digest(function.replace("x", "" + i)));
            //if (this.centre.getBlockY() + y > quartierSize) continue;
            Block target = this.centre.getWorld().getBlockAt(this.centre.getBlockX() + i, this.centre.getBlockY() + y, this.centre.getBlockZ());
            target.setType(Material.WOOL);
            target.setData(DyeColor.RED.getData());
        }

    }

    public void clear() {
        for (int i = -quartierSize; i < (2*quartierSize)-3; i++) {
            Block target = this.centre.getWorld().getBlockAt(this.centre.getBlockX() + i, this.centre.getBlockY() + i, this.centre.getBlockZ());
            if (target.getState() instanceof Wool && ((Wool)target.getState()).getColor() == DyeColor.RED) {
                if (target.getX() == this.centre.getBlockX() || target.getY() == this.centre.getBlockY())
                    ((Wool)target.getState()).setColor(DyeColor.BLACK);
                else
                    target.setType(Material.AIR);
            }
        }
    }
}
