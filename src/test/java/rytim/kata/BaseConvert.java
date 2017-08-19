package rytim.kata;

import org.junit.Assert;
import org.junit.Test;

public class BaseConvert {



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
            Assert.assertEquals("0", toOctal(0));
            Assert.assertEquals("1", toOctal(1));
            Assert.assertEquals("7", toOctal(7));
            Assert.assertEquals("10", toOctal(8));
            Assert.assertEquals("11", toOctal(9));
            Assert.assertEquals("-11", toOctal(-9));
            Assert.assertEquals("144", toOctal(100));
        }
    }
}
