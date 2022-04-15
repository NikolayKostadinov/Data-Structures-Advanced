import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

class AATree<T extends Comparable<T>> {
    public static class  Node<T extends Comparable<T>> {
        private T value;
        private Node<T> left;
        private Node<T> right;
        private int level;

        public Node(T value) {
            this.value = value;
            this.level = 1;
        }

        int count;
    }

    private Node<T> root;
    public AATree() {

    }

    public boolean isEmpty() {
        return countNodes() == 0;
    }

    public void clear() {
        root = null;
    }

    public void insert(T element) {
        this.root = this.insert(root, element);
    }

    private Node<T> insert(Node<T> node, T element) {

        if (node == null) {
            return new Node<>(element);
        }

        if (node.value.compareTo(element) > 0) {
            node.left = this.insert(node.left, element);
        } else if (node.value.compareTo(element) < 0) {
            node.right = this.insert(node.right, element);
        } else {
            throw new IllegalArgumentException();
        }

        node = skew(node);
        node = split(node);

        return node;
    }

    private Node<T> split(Node<T> node) {
        if (node.right != null && node.right.right != null) {
            Node<T> temp = node.right;
            temp.left = node;
            temp.right = null;
            temp.level++;
            return temp;
        }
        return node;
    }

    private Node<T> skew(Node<T> node) {
        if (node.left != null && node.left == node.left.left) {
            Node<T> temp = node.left;
            temp.right = node;
            node.left = null;
            return temp;
        }

        return node;
    }

    public int countNodes() {
        if (root == null) return -1;
        var count = new AtomicInteger();
        inOrder(x-> count.getAndIncrement());
        return count.get();
    }

    public boolean search(T element) {
        return false;
    }

    public void inOrder(Consumer<T> consumer) {
        inOrder(root, consumer);
    }

    private void inOrder(Node<T> node, Consumer<T> consumer) {
        if (node == null) return;
        inOrder(node.left, consumer);
        consumer.accept(node.value);
        inOrder(node.right, consumer);
    }

    public void preOrder(Consumer<T> consumer) {
        preOrder(root, consumer);
    }

    private void preOrder(Node<T> node, Consumer<T> consumer) {
        consumer.accept(node.value);
        preOrder(node.left, consumer);
        preOrder(node.right, consumer);
    }

    public void postOrder(Consumer<T> consumer) {
        postOrder(root, consumer);
    }

    private void postOrder(Node<T> node, Consumer<T> consumer) {
        postOrder(node.left, consumer);
        postOrder(node.right, consumer);
        consumer.accept(node.value);
    }
}
