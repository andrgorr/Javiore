package pw.javipepe.javiore.modules.functions.util;

import com.fathzer.soft.javaluator.DoubleEvaluator;

import javax.script.ScriptException;

/**
 * @author: Javi
 * <p>
 * Class created on 23/05/16 as part of project Javiore
 **/
public class MathExpressionParserUtil {

    public static int digest(String expression) throws ScriptException {
        expression = expression.replace("y = ", "");

        return new DoubleEvaluator().evaluate(expression).intValue();
    }
}
