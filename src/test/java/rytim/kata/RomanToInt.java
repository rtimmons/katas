package rytim.kata;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RomanToInt {
    static final Map<Character, Integer> R = new LinkedHashMap<Character, Integer>() {{
        put('M', 1000);
        put('D', 500);
        put('C', 100);
        put('L', 50);
        put('X', 10);
        put('V', 5);
        put('I', 1);
    }};

    public static String toR(int d) {
        StringBuilder out = new StringBuilder();
        while(d > 0) {
            // order matters!
            // TODO handle subtractive cases (need iterator)
            for(Map.Entry<Character, Integer> pair : R.entrySet()) {
                int many = d / pair.getValue();
                appendN(out, pair.getKey(), many);
                d = d % pair.getValue();
            }
        }
        return out.toString();
    }

    static void appendN(StringBuilder out, char n, int times) {
        while(times > 0) {
            out.append(n);
            times--;
        }
    }


    public static class ToIntTests {
        @Test
        public void testSimple() {
            assertEquals("I", toR(1));
            assertEquals("II", toR(2));
            assertEquals("V", toR(5));
            assertEquals("DV", toR(505));
            assertEquals("CCVII", toR(207));
        }
    }


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

    public static class ToRomanTests {
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
