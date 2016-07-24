package pw.javipepe.javiore.modules.faceRecognition.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import pw.javipepe.javiore.modules.faceRecognition.util.Face;

/**
 * @author: Javi
 * <p>
 * Class created on 29/05/16 as part of project Javiore
 **/
public class FaceRecognitionCmds {

    @Command(aliases = {"faceinfo"}, desc = "Analyse a face", usage = "{face link}", min = 1)
    public static void faceinfo (final CommandContext cmd, final CommandSender sender) throws Exception {
        String URL = cmd.getString(0);

        try {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2&m----------- &r&a&lFace Analysis &2&m-----------"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&oURL: &r&b&n&o" + URL));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', new Face(URL).scan().fetch().generateParagraph()));
            sender.sendMessage(" ");
            sender.sendMessage((ChatColor.translateAlternateColorCodes('&',"&3Analysis brought to you by &c&lFaceRecognition &3handcrafted by &bJavipepe. &ePlease direct any issues to &b&nme@javipepe.pw&r&e!")));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
