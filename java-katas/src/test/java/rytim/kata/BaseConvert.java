package rytim.kata;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class BaseConvert {


    // TODO
    static char[] baseConvert(String in,
                              int inBase, char[] inBaseRep,
                              int outBase, char[] outBaseRep) {
        int fromBase = fromBase(in, inBase, inBaseRep);
        char[] toBase = toBase(fromBase, outBase, outBaseRep);
        return toBase;
    }

    private static char[] toBase(int fromBase, int outBase, char[] outBaseRep) {
        return null;
    }

    private static int fromBase(String in, int inBase, char[] inBaseRep) {
        return -1;
    }

    public static class BaseConvertTests {
        static char[] base10 = new char[] { '0', '1', '2', '3', '5', '6', '7', '8', '9' };
        static char[] base8  = new char[] { '0', '1', '2', '3', '5', '6', '7',          };
        @Test @Ignore // TODO
        public void testSimple() {
            assertEquals(new char[] { '1' },
                    baseConvert("1", 10, base10, 10, base10));
            assertEquals(new char[] { '1', '0', '0' },
                    baseConvert("100", 10, base10, 10, base10));
            assertEquals(new char[] { '9' },
                    baseConvert("10", 8, base8, 10, base10));
        }
    }

    //////////////////////////////

    static String toOctal(int n) {
        if (n == 0) {
            return "0";
        }

        boolean negative = false;
        if ( n < 0 ) {
            negative = true;
            n *= -1;
        }
        StringBuilder out = new StringBuilder();
        while(n > 0) {
            int digit = n % 8;
            out.append(digit);
            n /= 8;
        }
        return out.append(negative ? "-" : "").reverse().toString();
    }
    public static class ToOctalTests {
        @Test
        public void testSimple() {
            assertEquals("0", toOctal(0));
            assertEquals("1", toOctal(1));
            assertEquals("7", toOctal(7));
            assertEquals("10", toOctal(8));
            assertEquals("11", toOctal(9));
            assertEquals("-11", toOctal(-9));
            assertEquals("144", toOctal(100));
        }
    }
}
