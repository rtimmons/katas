package rytim.kata;

import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class Tree {
    final int data;
    final Tree left;
    final Tree right;

    public Tree(int data, Tree left, Tree right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }

    private void wLeft(Consumer<Tree> left) {
        if (this.left != null) {
            left.accept(this.left);
        }
    }
    private void wRight(Consumer<Tree> right) {
        if (this.right != null) {
            right.accept(this.right);
        }
    }

    //////////////////////////////////////////////
    // Pretty-Print
    //////////////////////////////////////////////

    int widthOf(int n) {
        int out = 1;
        if ( n < 0 ) { out++; n = -n; }
        while(n > 10) {
            out++;
            n /= 10;
        }
        return out;
    }

    int before() {
        return (this.left == null ? 0 : this.left.width());
    }

    int width() {
        return before()
        +      widthOf(this.data)
        +      (this.right == null ? 0 : this.right.width());
    }

    static void nspaces(StringBuilder out, int n) {
        while(n-- > 0) {
            out.append(" ");
        }
    }

    String prettyPrint() {
        StringBuilder out = new StringBuilder();
        levelOrder((t)-> {
            nspaces(out, t.before());
            out.append(t.data);
        }, i -> out.append("\n"));
        return out.toString();
    }

    ///////////////////////////////////////////////
    // Level-Order
    ///////////////////////////////////////////////

    /*
            8
        5       10
     3   6     9  11
     */
    public void levelOrderPrint() {
        levelOrder(System.out::print, i -> System.out.println());
    }

    public void levelOrder(Consumer<Tree> withLevel, Consumer<Integer> afterLevel) {
        Set<Tree> atLevel = new HashSet<>();
        atLevel.add(this);

        int level = 0;
        while(!atLevel.isEmpty()) {
            atLevel.forEach(withLevel::accept);
            afterLevel.accept(level);
            atLevel = atLevel.stream()
                    .flatMap(t -> Stream.of(t.left, t.right))
                    .filter(t -> t != null)
                    .collect(toSet());
            level++;
        }
    }


    void levelOrder(Stack<Tree> stack) {
        Tree top = stack.peek();
        wLeft(stack::push);
        wRight(stack::push);
    }

    // TODO: level-order iterator

    //////////////////////////////////////////////
    // In Order
    //////////////////////////////////////////////

    void inOrderPrint() {
        if (left != null) {
            left.inOrderPrint();
        }
        System.out.println(this.data);
        if (right != null) {
            right.inOrderPrint();
        }
    }

    static class InorderIterator implements Iterator<Integer> {

        final Stack<Tree> up;

        public InorderIterator(Tree root) {
            this.up = new Stack<>();
            this.up.push(root);
            traverse();
        }

        void traverse() {
            Tree top = up.peek();
            while (top.left != null) {
                up.push(top.left);
                top = top.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !this.up.empty();
        }

        @Override
        public Integer next() {
            Tree out = up.pop();
            if (out.right != null) {
                up.push(out.right);
                traverse();
            }
            return out.data;
        }
    }

    public String toString() {
//        return "<" + (left == null ? "" : "L,") + this.data + (right == null ? "" : ",R") + ">";
        return prettyPrint();

    }

    Iterator<Integer> inorder() {
        return new InorderIterator(this);
    }

    public static class Tests {
        @Test
        public void main() {
            Tree root = new Tree(100,
                    new Tree(50,
                            new Tree(40,
                                    new Tree(30, null, null), null),
                            new Tree(55, null, null)),
                    new Tree(175, null, null)
            );

//            for (Iterator<Integer> it = root.inorder(); it.hasNext();) {
//                System.out.println(it.next());
//            }
//            root.levelOrderPrint();
            System.out.println(root);
        }

    }

}
