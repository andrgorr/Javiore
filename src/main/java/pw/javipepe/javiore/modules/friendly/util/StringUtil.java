package pw.javipepe.javiore.modules.friendly.util;


import java.util.Collection;

public class StringUtil {

    public static String listToEnglishCompound(Collection<?> list, String prefix, String suffix) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for(Object str : list) {
            if(i != 0) {
                if(i == list.size() - 1) {
                    builder.append(" and ");
                } else {
                    builder.append(", ");
                }
            }

            builder.append(prefix).append(str).append(suffix);
            i++;
        }

        return builder.toString();
    }
}
