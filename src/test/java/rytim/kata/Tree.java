package rytim.kata;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;

public class Tree {
    final int data;
    Tree left;
    Tree right;

    public Tree(int data, Tree left, Tree right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }

    //////////////////////////////////////////
    // Construct from level-order traversal //
    //////////////////////////////////////////

    static Tree fromTraversal(List<Integer> remain) {
        if (remain == null || remain.isEmpty()) {
            return null;
        }
        Queue<Integer> toAdd = new LinkedList<>(remain);
        return fromTraversal(toAdd);
    }

    private static Tree fromTraversal(Queue<Integer> toAdd) {
        Tree root = new Tree(toAdd.remove(), null, null);
        List<Tree> atLevel = Collections.singletonList(root);
        List<Tree> nextLevel = new LinkedList<>();
        while(!toAdd.isEmpty()) {
            for(Tree t : atLevel) {
                Integer left = toAdd.remove();
                if (left != null) {
                    t.left = new Tree(left, null, null);
                    nextLevel.add(t.left);
                }

                Integer right = toAdd.isEmpty() ? null : toAdd.remove();
                if (right != null) {
                    t.right = new Tree(right, null, null);
                    nextLevel.add(t.right);
                }
            }
            atLevel = nextLevel;
            nextLevel = new LinkedList<>();
        }
        return root;
    }

    public static class FromInorder {
        @Test
        public void testInOrder() {
            Tree root = Tree.fromTraversal(Arrays.asList(1,null,2,3));
            assertEquals(1,     root.data);
            assertEquals(2,     root.right.data);
            assertEquals(3,     root.right.left.data);
            assertEquals(null,  root.right.right);
        }
    }

    ///////////////////////////////////
    // Closest Common Ancestor
    ///////////////////////////////////

    Queue<Tree> pathTo(Tree other) {
        Queue<Tree> ancestors = new LinkedList<>();
        if (this.pathTo(other, ancestors)) {
            return ancestors;
        }
        return null;
    }
    
    boolean pathTo(Tree other, Queue<Tree> ancestors) {
        ancestors.add(this);
        if (other == this) {
            return true;
        }
        if (this.left != null) {
            if (left.pathTo(other, ancestors)) {
                return true;
            }
        }
        if (this.right != null) {
            if (right.pathTo(other, ancestors)) {
                return true;
            }
        }
        ancestors.remove(this);
        return false;
    }

    Tree closestCommonAncestor(Tree a, Tree b) {

    // can we reach both from here?
        Queue<Tree> toA = this.pathTo(a);
        if (toA == null) {
            return null;
        }

        Queue<Tree> toB = this.pathTo(b);
        if (toB == null) {
            return null;
        }

    // cool we can so let's just figure out where the paths diverge

        Tree out = divergence(toA, toB);
        if (out == null) { return this; }
        return out;
    }

    private static <T> T divergence(Queue<T> toA, Queue<T> toB) {
        T previous = null;

        while(!toA.isEmpty() && !toB.isEmpty()) {
            T a = toA.remove();
            T b = toB.remove();
            if (!Objects.equals(a, b)) {
                break;
            }
            previous = a; // a == b so pick one
        }

        return previous;
    }

    public static class ClosestCommonAncestorTests {
        @Test
        public void testSimple() {
            Tree a = new Tree(50, null, null);
            Tree b = new Tree(100, null, null);
            Tree c = new Tree(30, a, b);

            Tree root = new Tree(200, c, null);
            assertEquals(c, root.closestCommonAncestor(a, b));
        }
    }


    /////////////////////////////////////

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

    // aka breadth-first search
    public void levelOrder(Consumer<Tree> withLevel, Consumer<Integer> afterLevel) {
        Set<Tree> atLevel = new HashSet<>();
        atLevel.add(this);

        int level = 0;
        while(!atLevel.isEmpty()) {
            atLevel.forEach(withLevel::accept);
            afterLevel.accept(level);
            atLevel = atLevel.stream()
                    .flatMap(t -> Stream.of(t.left, t.right))
                    .filter(Objects::nonNull)
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
