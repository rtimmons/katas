package rytim.kata;

/*

Given n pairs of parentheses, write a function to generate all
combinations of well-formed parentheses.

For example, given n = 3, a solution set is:

[
  "((()))",
  "(()())",
  "(())()",
  "()(())",
  "()()()"
]

https://leetcode.com/problems/generate-parentheses/description/
*/

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class AllParens {

    public static Set<String> parens(int i) {
        if (i == 0) {
            return Collections.emptySet();
        }
        if (i == 1) {
            return Collections.singleton("()");
        }
        Set<String> remain = parens(i-1);
        Set<String> out = new HashSet<>();
        for(String r : remain) {
            out.add("(" + r + ")");
            out.add("()" + r);
            out.add(r + "()");
        }
        return out;
    }

    public static class Tests {
        @Test
        public void n0() {
            Set<String> actual = parens(0);
            Assert.assertEquals(actual, Collections.emptySet());
        }
        @Test
        public void n1() {
            Set<String> actual = parens(1);
            Assert.assertEquals(Collections.singleton("()"), actual);
        }
        @Test
        public void n2() {
            Set<String> actual = parens(2);
            Assert.assertEquals(Stream.of(
                    "(())",
                    "()()"
            ).collect(toSet()), actual);
        }
        @Test
        public void n3() {
            Set<String> actual = parens(3);
            Assert.assertEquals(Stream.of(
                    "((()))",
                    "(()())",
                    "(())()",
                    "()(())",
                    "()()()"
            ).collect(toSet()), actual);
        }
    }
}
