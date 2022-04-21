import java.util.function.Consumer;

class AATree<T extends Comparable<T>> {
    public static class Node<T> {
        private T value;
        private Node<T> left;
        private Node<T> right;
        private int level;

        public Node(T key) {
            this.value = key;
            this.level = 1;
        }
    }

    private Node<T> root;

    public AATree() {

    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public void clear() {
        this.root = null;
    }

    public void insert(T element) {
        this.root = this.insert(root, element);
    }

    private Node<T> insert(Node<T> node, T element) {
        if (node == null) {
            return new Node<>(element);
        }

        int comparison = element.compareTo(node.value);

        if (comparison < 0) {
            node.left = insert(node.left, element);
        } else if (comparison > 0) {
            node.right = insert(node.right, element);
        } else {
            throw new IllegalArgumentException();
        }

        node = skew(node);
        node = split(node);

        return node;
    }

    /**
     * Skew when an insertion or deletion creates a horizontal left link
     * 10 is newly added. We need "skew" operation
     *
     *          17                                     17
     *      /       \                               /       \
     *    9          25            =>              9          25
     *  /   \       /  \                         /   \       /  \
     * 5    10-(12)  22  32-55                  5    10-12  22  32-55
     *
     * Skew operation is a single right rotation in this case around 12
     */
    private Node<T> skew(Node<T> node) {
        if (node.left != null && node.left.level == node.level) {
            return rotateRight(node);
        }

        return node;
    }

    /**
     * Split when an insertion or deletion two consecutive right horizontal links
     * 71 is newly added. We need "split" operation.
     *
     *              17                                      17
     *          /       \                               /       \
     *        9          25            =>             9          25 - 55
     *      /   \       /  \                        /   \       /    /  \
     *     5    10-12  22  (32)-55-71              5    10-12  22   32  71
     *
     * Split operation is a single left rotation in this case around 32
     */
    private Node<T> split(Node<T> node) {
        if (node.right != null && node.right.level == node.level &&
            node.right.right != null && node.right.right.level == node.right.level) {

            node = rotateLeft(node);
            node.level++;
        }
        return node;
    }

    private Node<T> rotateLeft(Node<T> node) {
        Node<T> temp = node.right;
        node.right = temp.left;
        temp.left = node;

        return temp;
    }

    private Node<T> rotateRight(Node<T> node) {
        Node<T> temp = node.left;
        node.left = temp.right;
        temp.right = node;

        return temp;
    }

    public int countNodes() {
        return this.countChildren(root);
    }

    private int countChildren(Node<T> node) {
        if (node == null) return 0;
        return countChildren(node.left) + countChildren(node.right) + 1;
    }

    public boolean search(T element) {
        return this.search(this.root, element);
    }

    private boolean search(Node<T> node, T element) {
        if (node == null) return false;

        int comparing = element.compareTo(node.value);

        if (comparing < 0) {
            return search(node.left, element);
        } else if (comparing > 0) {
            return this.search(node.right, element);
        }
        return true;
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
        if (node == null) return;
        consumer.accept(node.value);
        preOrder(node.left, consumer);
        preOrder(node.right, consumer);
    }

    public void postOrder(Consumer<T> consumer) {
        postOrder(root, consumer);
    }

    private void postOrder(Node<T> node, Consumer<T> consumer) {
        if (node == null) return;
        postOrder(node.left, consumer);
        postOrder(node.right, consumer);
        consumer.accept(node.value);
    }
}
