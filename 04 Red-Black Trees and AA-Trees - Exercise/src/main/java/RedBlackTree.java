import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class RedBlackTree<Key extends Comparable<Key>, Value> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node root;     // root of the BST

    // BST helper node data type
    private class Node {
        private Key key;           // key
        private Value val;         // associated data
        private Node left, right;  // links to left and right subtrees
        private boolean color;     // color of parent link
        private int size;          // subtree count

        public Node(Key key, Value val) {
            this.key = key;
            this.val = val;
            this.color = RED;
            this.size = 1;
        }

        public Node(Key key, Value val, boolean color, int size) {
            this.key = key;
            this.val = val;
            this.color = color;
            this.size = size;
        }
    }

    public RedBlackTree(Node root) {
        this.root = root;
        this.root.color = BLACK;
        this.root.size = 1;
    }

    public RedBlackTree() {
    }

    // is node x red; false if x is null ?
    private boolean isRed(Node node) {
        return node == null ? false : node.color == RED;
    }

    // number of node in subtree rooted at x; 0 if x is null
    private int size(Node x) {
        return x == null ? 0 : x.size;
    }


    /**
     * Returns the number of key-value pairs in this symbol table.
     *
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return size(this.root);
    }

    /**
     * Is this symbol table empty?
     *
     * @return {@code true} if this symbol table is empty and {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() != 0;
    }

    public Value get(Key key) {
        return get(root, key);
    }


    // value associated with the given key in subtree rooted at x; null if no such key
    private Value get(Node x, Key key) {
        //not found
        if (x == null) return null;

        if (greater(key, x.key)) {
            return get(x.right, key);
        } else if (less(key, x.key)) {
            return get(x.left, key);
        }

        return x.val;
    }

    private boolean greater(Key first, Key second) {
        return first.compareTo(second) > 0;
    }

    private boolean less(Key first, Key second) {
        return first.compareTo(second) < 0;
    }

    public boolean contains(Key key) {
        return get(key) != null;
    }

    public void put(Key key, Value val) {
        this.root = this.put(root, key, val);
        this.root.color = BLACK;
    }

    private Node put(Node node, Key key, Value val) {
        if (node == null) return new Node(key, val);

        if (greater(key, node.key)) {
            node.right = put(node.right, key, val);
        } else if (less(key, node.key)) {
            node.left = put(node.left, key, val);
        }

        if (!isRed(node.left) && isRed(node.right)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }

        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }


    public void deleteMin() {
    }

    // delete the key-value pair with the minimum key rooted at h
    private Node deleteMin(Node h) {
        return null;
    }

    public void deleteMax() {
    }

    // delete the key-value pair with the maximum key rooted at h
    private Node deleteMax(Node h) {
        return null;
    }

    public void delete(Key key) {
    }

    // delete the key-value pair with the given key rooted at h
    private Node delete(Node h, Key key) {
        return null;
    }

    private Node rotateRight(Node node) {
        Node temp = node.left;
        node.left = temp.right;
        temp.right = node;

        temp.color = BLACK;
        node.color = RED;

        node.size = size(node.left) + size(node.right) + 1;
        return temp;
    }

    // make a right-leaning link lean to the left
    private Node rotateLeft(Node node) {
        Node temp = node.right;
        node.right = temp.left;
        temp.left = node;

        temp.color = BLACK;
        node.color = RED;

        node.size = size(node.left) + size(node.right) + 1;
        return temp;
    }

    // flip the colors of a node and its two children
    private void flipColors(Node node) {
        node.color = RED;
        node.left.color = BLACK;
        node.right.color = BLACK;

        node.size = size(node.left) + size(node.right) + 1;
    }

    // Assuming that h is red and both h.left and h.left.left
    // are black, make h.left or one of its children red.
    private Node moveRedLeft(Node h) {
        return null;
    }

    // Assuming that h is red and both h.right and h.right.left
    // are black, make h.right or one of its children red.
    private Node moveRedRight(Node h) {
        return null;
    }

    // restore red-black tree invariant
    private Node balance(Node h) {
        return null;
    }

    public int height() {
        return this.root == null ? -1 : size(root);
    }

    private int height(Node x) {
        return 0;
    }

    public Key min() {
        return null;
    }

    // the smallest key in subtree rooted at x; null if no such key
    private Node min(Node x) {
        return null;
    }

    public Key max() {
        return null;
    }

    // the largest key in the subtree rooted at x; null if no such key
    private Node max(Node x) {
        return null;
    }

    public Key floor(Key key) {
        return null;
    }

    // the largest key in the subtree rooted at x less than or equal to the given key
    private Node floor(Node x, Key key) {
        return null;
    }

    public Key ceiling(Key key) {
        return null;
    }

    // the smallest key in the subtree rooted at x greater than or equal to the given key
    private Node ceiling(Node x, Key key) {
        return null;
    }

    public Key select(int rank) {
        return null;
    }

    // Return key in BST rooted at x of given rank.
    // Precondition: rank is in legal range.
    private Key select(Node x, int rank) {
        return null;
    }

    public int rank(Key key) {
        return 0;
    }

    // number of keys less than key in the subtree rooted at x
    private int rank(Key key, Node x) {
        return 0;
    }

    public Iterable<Key> keys() {
        List<Key> keys = new ArrayList<>();
        getKeysInorder(this.root, keys);
        return keys;
    }

    private void getKeysInorder(Node node, List<Key> keys) {
        if (node == null) return;
        getKeysInorder(node.left, keys);
        keys.add(node.key);
        getKeysInorder(node.right, keys);
    }

    public Iterable<Key> keys(Key lo, Key hi) {
        return null;
    }

    // add the keys between lo and hi in the subtree rooted at x
    // to the queue
    private void keys(Node x, Deque<Key> queue, Key lo, Key hi) {
    }

    public int size(Key lo, Key hi) {
        return 0;
    }

    private boolean check() {
        return false;
    }

    // does this binary tree satisfy symmetric order?
    // Note: this test also ensures that data structure is a binary tree since order is strict
    private boolean isBST() {
        return false;
    }

    // is the tree rooted at x a BST with all keys strictly between min and max
    // (if min or max is null, treat as empty constraint)
    private boolean isBST(Node x, Key min, Key max) {
        return false;
    }

    // are the size fields correct?
    private boolean isSizeConsistent() {
        return false;
    }

    private boolean isSizeConsistent(Node x) {
        return false;
    }

    // check that ranks are consistent
    private boolean isRankConsistent() {
        return false;
    }

    // Does the tree have no red right links, and at most one (left)
    // red links in a row on any path?
    private boolean isTwoThree() {
        return false;
    }

    private boolean isTwoThree(Node x) {
        return false;
    }

    // do all paths from root to leaf have same number of black edges?
    private boolean isBalanced() {
        return false;
    }

    // does every path from the root to a leaf have the given number of black links?
    private boolean isBalanced(Node x, int black) {
        return false;
    }
}
