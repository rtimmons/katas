package rytim.kata;

import org.junit.Test;

import java.util.*;

public class TwistList implements Iterable<Integer> {

    Node head;

    // region delegate
    static class Node {
        int data;
        Node next;
        Node other;

        Node deepClone(Map<Node, Node> clones) {
            if (clones.containsKey(this)) {
                return clones.get(this);
            }
            Node cloned = new Node();
            cloned.data = this.data;
            clones.put(this, cloned);
            if (this.next != null) {
                cloned.next = this.next.deepClone(clones);
            }
            if (this.other != null) {
                cloned.other = this.other.deepClone(clones);
            }
            return cloned;
        }
    }
    // endregion

    // region contains
    boolean contains(int needle) {
        for (Node current = head; current != null; current = current.next) {
            if (current.data == needle) {
                return true;
            }
        }
        return false;
    }

    boolean containsTwoValuesThatSumTo(int needle) {
        Set<Integer> seen = new HashSet<>();
        for (Node current = head; current != null; current = current.next) {
            if (seen.contains(needle - current.data)) {
                return true;
            }
            seen.add(current.data);
        }
        return false;
    }
    // endregion

    // region clone
    TwistList deepClone() {
        Map<Node, Node> clones = new HashMap<>();
        Node nhead = this.head.deepClone(clones);

        TwistList out = new TwistList();
        out.head = nhead;
        return out;
    }
    // endregion

    // region Iteration
    public Iterator<Integer> iterator() {
        return new NodeIterator(this.head);
    }

    private static class NodeIterator implements Iterator<Integer> {
        Node current;

        NodeIterator(Node current) {
            this.current = current;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Integer next() {
            Integer out = current.data;
            current = current.next;
            return out;
        }
    }
    // endregion
}
