package rytim.kata;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RomanToInt {
    static final Map<Character, Integer> R = new LinkedHashMap<Character, Integer>() {{
        put('I', 1);
        put('V', 5);
        put('L', 50);
        put('C', 100);
        put('D', 500);
        put('M', 1000);
    }};

    public static int r(String roman) {
        return r(roman.toCharArray(), 0);
    }

    public static int r(char[] cs, int fromix) {
        if (fromix >= cs.length) {
            return 0;
        }
        if (fromix + 1 < cs.length // at least 2 more chars
                && R.get(cs[fromix+1]) > R.get(cs[fromix])) { // increasing
            return R.get(cs[fromix + 1]) - R.get(cs[fromix])
                    + r(cs, fromix + 2);
        }
        return R.get(cs[fromix]) + r(cs, fromix + 1);
    }

    public static class Tests {
        @Test
        public void tests() {
            assertEquals(1, r("I"));
            assertEquals(2, r("II"));
            assertEquals(3, r("III"));
            assertEquals(4, r("IV"));
            assertEquals(5, r("V"));
        }
        @Test public void bigger() {
            assertEquals(207, r("CCVII"));
        }
        @Test public void evenBigger() {
            assertEquals(1000,  r("M"));
            assertEquals(900,   r("CM"));
            assertEquals(904,   r("CMIV"));
            assertEquals(1904,  r("MCMIV"));
        }
    }
}
