import java.util.function.Consumer;

public class AATree<T extends Comparable<T>> {

    private Node<T> root;

    public AATree() {
    }

    private AATree(Node<T> node) {
        this.preOrderCopy(node);
    }

    private void preOrderCopy(Node<T> node) {
        if (node == null) {
            return;
        }

        this.insert(node.value);
        this.preOrderCopy(node.left);
        this.preOrderCopy(node.right);
    }

    // TODO:
    //  Insert using iteration over the nodes
    //  You can make a recursive one it is up to you
    //  The difference is that the recursive call should
    //  return Node
    public void insert(T value) {
        this.root = this.insert(root, value);
    }

    private Node<T> insert(Node<T> node, T value) {
        if (node == null) {
            return new Node<>(value);
        }

        if (node.value.compareTo(value) > 0) {
            node.left = this.insert(node.left, value);
        } else if (node.value.compareTo(value) < 0) {
            node.right = this.insert(node.right, value);
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
            node= temp;
        }
        return node;
    }

    private Node<T> skew(Node<T> node) {
        if (node.left != null && node.left == node.left.left) {
            Node<T> temp = node.left;
            temp.right = node;
            node.left = null;
            node = temp;
        }

        return node;
    }


    public boolean contains(T value) {
        return this.findElement(value) != null;
    }

    public AATree<T> search(T item) {
        return new AATree<>(this.findElement(item));
    }

    private Node<T> findElement(T item) {
        Node<T> current = this.root;
        while (current != null) {
            if (item.compareTo(current.value) < 0) {
                current = current.left;
            } else if (item.compareTo(current.value) > 0) {
                current = current.right;
            } else {
                break;
            }
        }
        return current;
    }

    public void eachInOrder(Consumer<T> consumer) {
        this.eachInOrder(this.root, consumer);
    }

    private void eachInOrder(Node<T> node, Consumer<T> consumer) {
        if (node == null) {
            return;
        }

        this.eachInOrder(node.left, consumer);
        consumer.accept(node.value);
        this.eachInOrder(node.right, consumer);
    }


    public static class Node<T extends Comparable<T>> {
        private T value;
        private Node<T> left;
        private Node<T> right;
        private int level;

        public Node(T value) {
            this.value = value;
            this.level = 1;
        }
    }
}
