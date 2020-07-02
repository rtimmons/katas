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

    private boolean charMatch(int rexIx, char[] str, int strIx) {
        return rex[rexIx] == '.' || str[strIx] == rex[rexIx];
    }

    private boolean matches(char[] str, final int strIx, final int rexIx) {
        return rexIx >= rex.length // no more rex
                // star
                || (rexIx + 1 < rex.length && rex[rexIx + 1] == '*' && matchStar(str, strIx, rexIx))
                // simple match and rest matches
                || (strIx < str.length && charMatch(rexIx, str, strIx) && matches(str, strIx + 1, rexIx + 1));
    }

    private boolean matchStar(char[] str, int strIx, int rexIx) {
        int curr = strIx;
        do {
            boolean goodFromHere =
                    (strIx >= str.length - 1 || charMatch(rexIx, str, strIx))
                    && matches(str, curr, rexIx + 2);
            if (goodFromHere) {
                return true;
            }
            curr++;
        } while (curr <= str.length - 1);

        return false;
    }


    // region testing
    @RunWith(Parameterized.class)
    public static class Tests {
        // keep test cases array as all strings to appease the
        // compiler gods.
        static final String T = "true";
        static final String F = "false";

        static String[][] cases = new String[][]{
                // rex, str, expec

                {"", "abc", T},
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

                {"a*", "", T},
                {"a*", "aa", T},
                {".*", "a", T},
                {".*", "", T},
                {"a*b*a", "aa", T},
                {"a*b*a", "aaaaaaa", T},
                {"a*b*a", "aaaaaaba", T},
                {"a*b*a", "abaaaaaa", T},
                {"a*b*a", "", F},
                {"a*a", "ab", T},

                {".*", "ab", T},
                {"ab*a", "abba", T},
                {"ab*a", "aa", T}
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
