package rytim.hello;

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

    ///////////////////////////////////////////////
    // Level-Order
    ///////////////////////////////////////////////

    /*
            8
        5       10
     3   6     9  11
     */
    public void levelOrderPrint() {
        Set<Tree> atLevel = new HashSet<>();
        atLevel.add(this);

        while(!atLevel.isEmpty()) {
            atLevel.forEach(System.out::print);
            System.out.println();
            atLevel = atLevel.stream()
                    .flatMap(t -> Stream.of(t.left, t.right))
                    .filter(t -> t != null)
                    .collect(toSet());
        }
    }

    void levelOrder(Stack<Tree> stack) {
        Tree top = stack.peek();
        wLeft(stack::push);
        wRight(stack::push);
    }

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
        return "<" + (left == null ? "" : "L,") + this.data + (right == null ? "" : ",R") + ">";
    }

    Iterator<Integer> inorder() {
        return new InorderIterator(this);
    }

    public static void main(String... args) {
        Tree root = new Tree(100,
                new Tree(50,
                        new Tree(40,
                                new Tree(30, null, null), null),
                        new Tree(55, null, null)),
                new Tree(175, null, null)
        );

//        for (Iterator<Integer> it = root.inorder(); it.hasNext();) {
//            System.out.println(it.next());
//        }
        root.levelOrderPrint();
    }
}
