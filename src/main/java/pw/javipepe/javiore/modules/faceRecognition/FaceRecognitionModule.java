package pw.javipepe.javiore.modules.faceRecognition;

import pw.javipepe.javiore.Handler;
import pw.javipepe.javiore.modules.DevelopmentStatus;
import pw.javipepe.javiore.modules.Module;
import pw.javipepe.javiore.modules.ModuleCommandHelp;
import pw.javipepe.javiore.modules.faceRecognition.commands.FaceRecognitionCmds;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Javi
 * <p>
 * Class created on 28/05/16 as part of project Javiore
 **/
public class FaceRecognitionModule extends Module {

    public Class[] commands = {
            FaceRecognitionCmds.class
    };


    @Override
    public void init(){
        Handler h = new Handler();
        h.stageCommands(commands);
    }

    /**
     *  MODULE INFORMATION
     */

    @Override
    public String getName(){
        return "Face Recognition";
    }

    @Override
    public String getDescription(){
        return "Analyze pictures and provide a paragraph result!";
    }

    @Override
    public DevelopmentStatus getStatus(){
        return DevelopmentStatus.FINISHED;
    }

    @Override
    public ModuleCommandHelp getHelp() {
        HashMap<String, String> help = new HashMap<>();
        help.put("faceinfo <url>", "Analyse a face of a photo found in a URL you provide!");

        return new ModuleCommandHelp(help, this);
    }
}
