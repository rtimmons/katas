package rytim.kata;

import org.junit.Assert;
import org.junit.Test;

public class ItoA {
    public static String itoa(int in) {
        StringBuilder out = new StringBuilder();
        if (in == 0) {
            out.append("0");
        }
        boolean negative = false;
        if (in < 0) {
            negative = true;
            in *= -1;
        }
        while(in > 0) {
            out.append(in % 10);
            in = in - (in % 10);
            in /= 10;
        }
        return (negative ? "-" : "") + out.reverse().toString();
    }
    public static class Tests {
        @Test
        public void tests() {
            Assert.assertEquals("0", itoa(0));
            Assert.assertEquals("1", itoa(1));
            Assert.assertEquals("-1", itoa(-1));
            Assert.assertEquals("10", itoa(10));
            Assert.assertEquals("-10", itoa(-10));
            Assert.assertEquals("101", itoa(101));
            Assert.assertEquals("9999", itoa(9999));
        }
    }
}
