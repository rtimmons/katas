package rytim.kata;

import org.junit.Ignore;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RomanNumerals {
    static final Map<Character, Integer> R = new LinkedHashMap<Character, Integer>() {{
        put('M', 1000);
        put('D', 500);
        put('C', 100);
        put('L', 50);
        put('X', 10);
        put('V', 5);
        put('I', 1);
    }};

    static final Map<String, Integer> SUB = new LinkedHashMap<String, Integer>() {{
        put("IX", 9);
        put("IV", 4);
        put("CM", 900);
    }};

    public static String toRoman(int d) {
        Map.Entry[] entries = new Map.Entry[R.size()];
        {
            int i = 0;
            for (Map.Entry e : R.entrySet()) {
                entries[i++] = e;
            }
        }

        StringBuilder out = new StringBuilder();
        while (d > 0) {
            // order matters!
            // TODO: subtractive case
            for (int pairIx = 0; pairIx < entries.length; pairIx++) {
                Map.Entry<Character, Integer> pair = entries[pairIx];
                while (d > pair.getValue()) {
                    out.append(pair.getKey());
                    d -= pair.getValue();
                }
                int many = d / pair.getValue();
                appendN(out, pair.getKey(), many);
                d = d % pair.getValue();
            }
        }
        return out.toString();
    }

    static void appendN(StringBuilder out, char n, int times) {
        while (times > 0) {
            out.append(n);
            times--;
        }
    }


    public static class ToRomanTests {
        @Test
        public void testSimple() {
            assertEquals("I", toRoman(1));
            assertEquals("II", toRoman(2));
            assertEquals("V", toRoman(5));
            assertEquals("X", toRoman(10));
            assertEquals("DV", toRoman(505));
            assertEquals("CCVII", toRoman(207));
        }
        @Test @Ignore // TODO
        public void testSubtractive() {
            assertEquals("IX", toRoman(9));
            assertEquals("XIX", toRoman(19));
            assertEquals("IV", toRoman(4));
            assertEquals("XIV", toRoman(14));
        }
    }


    public static int toInt(String roman) {
        return toInt(roman.toCharArray(), 0);
    }

    public static int toInt(char[] cs, int fromix) {
        if (fromix >= cs.length) {
            return 0;
        }
        if (fromix + 1 < cs.length // at least 2 more chars
                && R.get(cs[fromix + 1]) > R.get(cs[fromix])) { // increasing
            return R.get(cs[fromix + 1]) - R.get(cs[fromix])
                    + toInt(cs, fromix + 2);
        }
        return R.get(cs[fromix]) + toInt(cs, fromix + 1);
    }

    public static class ToIntTests {
        @Test
        public void tests() {
            assertEquals(1, toInt("I"));
            assertEquals(2, toInt("II"));
            assertEquals(3, toInt("III"));
            assertEquals(4, toInt("IV"));
            assertEquals(5, toInt("V"));
            assertEquals(9, toInt("IX"));
        }

        @Test
        public void bigger() {
            assertEquals(207, toInt("CCVII"));
        }

        @Test
        public void evenBigger() {
            assertEquals(1000, toInt("M"));
            assertEquals(900, toInt("CM"));
            assertEquals(904, toInt("CMIV"));
            assertEquals(1904, toInt("MCMIV"));
        }
    }
}
