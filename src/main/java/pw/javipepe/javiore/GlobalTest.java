package pw.javipepe.javiore;


import pw.javipepe.javiore.modules.Databaseable;
import pw.javipepe.javiore.modules.Module;
import pw.javipepe.javiore.modules.faceRecognition.FaceRecognitionModule;
import pw.javipepe.javiore.modules.friendly.FriendlyModule;
import pw.javipepe.javiore.modules.functions.FunctionsModule;
import pw.javipepe.javiore.modules.rankManager.RankManagerModule;

public class GlobalTest {

    public static void main(String[] args) throws Exception {

        Module[] modules = {
                new FunctionsModule(),
                new FaceRecognitionModule(),
                new FriendlyModule(),
                new RankManagerModule()
        };

        for (Module m : modules) {
            System.out.println(m.getName() + "is" + (!isDatabaseAble(m) ? "n't" : "") + " databaseable");
        }

    }

    private static boolean isDatabaseAble(Object object){
        return Databaseable.class.isInstance(object);
    }
}
