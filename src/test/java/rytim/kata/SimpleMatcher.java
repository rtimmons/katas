package rytim.kata;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/*
Write a simple regex matcher that recognizes the following subset of RE

    a-z    literal match
    0-9    literal match
    .      matches any character
    *      matches 0 or more of the previous character

Return true/false if a string matches

Examples in form (regex, string, expect)

*/
public class SimpleMatcher {

    private final char[] rex;

    public SimpleMatcher(String rex) {
        this.rex = rex.toCharArray();
    }

    private boolean matches(String str) {
        char[] bits = str.toCharArray();
        return matches(bits, 0, 0);
    }

    private boolean matches(char[] str, final int strIx, final int rexIx) {
//        System.out.println(String.format("%s %d %d of %s", Arrays.toString(str), strIx, rexIx, Arrays.toString(rex)));
        if (rexIx >= rex.length) {
            return true; // nothing left to match
        }
        // else more rex to match

        if (strIx >= str.length) {
            return false; // end of string
        }


        boolean hasNext = rexIx + 1 < str.length;
        if (hasNext) {
            char next = rex[rexIx + 1];
            if (next == '*') {
                // TODO
                throw new UnsupportedOperationException("TODO support *");
//                if (matchStar(str, strIx, rexIx)) {
//                    return true;
//                }
            }
        }


        boolean charMatch =
                rex[rexIx] == '.' || str[strIx] == rex[rexIx];

        return charMatch && matches(str, strIx + 1, rexIx + 1);
    }


    // region testing
    @RunWith(Parameterized.class)
    public static class Tests {
        // keep test cases array as all strings to appease the
        // compiler gods.
        static final String T = "true";
        static final String F = "false";

        static String[][] cases = new String[][]{
//                {".*", "foo", T},
//                {".*", "", T},
                {".", "a", T},
                {".", "", F},
                {"a", "a", T},
                {"aa", "aa", T},
                {"a.", "aa", T},
                {"aa.", "aab", T},
                {"...", "???", T},
                {"....", "****", T},
                {"a.", "ab", T},
                {".a", "aa", T},
                {".a", "ba", T},
                {".", ".", T},
        };

        @Parameterized.Parameters
        public static Iterable<String[]> params() {
            return Arrays.asList(cases);
        }

        final String re, str;
        final boolean expect;

        public Tests(String re, String str, String expect) {
            this.re = re;
            this.str = str;
            this.expect = expect == T;
        }

        @Test
        public void testMatches() {
            SimpleMatcher m = new SimpleMatcher(re);
            boolean actual = m.matches(str);
            assertEquals(this.str + (this.expect ? " ~ " : " !~ ") + this.re,
                    this.expect, actual);
        }
    }
    // endregion
}
