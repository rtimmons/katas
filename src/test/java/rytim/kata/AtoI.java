package rytim.kata;

import org.junit.Assert;
import org.junit.Test;

public class AtoI {

    static int atoi(String in) {
        return atoi(in.toCharArray());
    }
    static int atoi(char[] in) {
        int out = 0;
        boolean negative = in[0] == '-';
        for(int base= negative ? 1 : 0; base<in.length; base++) {
            out *= 10;
            int digit = in[base] - '0';
            if (digit < 0 || digit > 9) {
                throw new IllegalArgumentException("Character " + base + " invalid");
            }
            out += digit;
        }
        return out * (negative ? -1 : 1);
    }

    public static class Tests {
        @Test
        public void test() {
            Assert.assertEquals(123,  atoi("123"));
            Assert.assertEquals(0,    atoi("0"));
            Assert.assertEquals(50,   atoi("50"));
            Assert.assertEquals(400,  atoi("400"));
            Assert.assertEquals(-100, atoi("-100"));
            Assert.assertEquals(0,    atoi("0"));
        }
    }

}
