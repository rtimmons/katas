package rytim.kata;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * @author rtimmons@
 * @since 8/17/17
 */
public class JSonParser {

    Object root;
    Stack<Context> contexts;
    
    public <T> T parse(String s, Class<T> type) {
        Scanner scanner = new Scanner(s);
        scanner.useDelimiter(Pattern.compile("/\\s|,|\"|:|\\{|}|[|]/"));
        while(scanner.hasNext()) {
            String tok = scanner.next();
            this.accept(tok);
        }
        return (T)root;
    }

    private void accept(String tok) {
        // TODO
    }

    enum State {
    }
    private static class Context {
        // TODO
    }


    public static class Tests {
        JSonParser p = new JSonParser();
        @Test
        @Ignore // TODO: need a cohesive data model
        public void testPrimitives() {
            Assert.assertEquals((Integer)1, p.parse("1", Integer.class));
        }
    }

}
