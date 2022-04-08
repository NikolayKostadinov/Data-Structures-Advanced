import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TwoThreeTree<K extends Comparable<K>> {
    private TreeNode<K> root;

    public void insert(K key) {
        if (this.root == null) {
            this.root = new TreeNode<>(key);
            return;
        }

        TreeNode<K> newRoot = this.insert(this.root, key);
        if (newRoot != null) this.root = newRoot;
    }

    private TreeNode<K> insert(TreeNode<K> node, K key) {

        if (node.isLeaf()) {
            if (node.isTwoNode()) {
                if (less(node.leftKey, key)) {
                    node.rightKey = key;
                } else if (greater(node.leftKey, key)) {
                    node.rightKey = node.leftKey;
                    node.leftKey = key;
                }
                return null;
            } else {
                List<K> keys = Stream.of(node.leftKey, node.rightKey, key)
                        .collect(Collectors.toList());
                Collections.sort(keys);
                return new TreeNode<>(keys.get(1), keys.get(0), keys.get(2));
            }
        }

        TreeNode<K> toFix;

        if (node.isTwoNode()) {
            if (greater(node.leftKey, key)) {
                toFix = insert(node.leftChild, key);
            } else {
                toFix = insert(node.rightChild, key);
            }
        } else {
            if (greater(node.leftKey, key)) {
                toFix = insert(node.leftChild, key);
            } else if (less(node.rightKey, key)) {
                toFix = insert(node.rightChild, key);
            } else {
                toFix = insert(node.middleChild, key);
            }
        }

        if (toFix == null) return null;


        if (node.isTwoNode()) {
            if (less(toFix.leftKey, node.leftKey)) {
                node.rightKey = node.leftKey;
                node.leftKey = toFix.leftKey;

                node.leftChild = toFix.leftChild;
                node.middleChild = toFix.rightChild;
            } else {

                node.rightKey = toFix.leftKey;

                node.middleChild = toFix.leftChild;
                node.rightChild = toFix.rightChild;
            }
            return null;
        } else {
            K promoteValue;
            TreeNode<K> left;
            TreeNode<K> right;
            if (less(toFix.leftKey, node.leftKey)) {
                left = toFix;
                promoteValue = node.leftKey;
                right = new TreeNode<K>(node.rightKey, node.middleChild, node.rightChild);
            } else if (greater(toFix.leftKey, node.rightKey)) {
                left = new TreeNode<>(node.leftKey, node.leftChild, node.middleChild);
                promoteValue = node.rightKey;
                right = toFix;
            } else {
                left = new TreeNode<>(node.leftKey, node.leftChild, toFix.leftChild);
                promoteValue = toFix.leftKey;
                right = new TreeNode<>(node.rightKey, toFix.rightChild, node.rightChild);
            }

            return new TreeNode<>(promoteValue, left, right);
        }
    }

    private boolean less(K first, K second) {
        return first.compareTo(second) < 0;
    }

    private boolean greater(K first, K second) {
        return first.compareTo(second) > 0;
    }

    private TreeNode<K> processLeaf(TreeNode<K> node, K key) {
        if (node.isTwoNode()) {
            insertIntoTwoNode(node, key);
            return null;
        }

        return insertIntoTreeNode(node, key);
    }

    private void insertIntoTwoNode(TreeNode<K> node, K key) {

    }

    private TreeNode<K> insertIntoTreeNode(TreeNode<K> node, K key) {
        List<K> keys = Stream.of(node.leftKey, node.rightKey, key)
                .collect(Collectors.toList());
        Collections.sort(keys);
        return new TreeNode<>(keys.get(1), keys.get(0), keys.get(2));
    }

    public String getAsString() {
        StringBuilder out = new StringBuilder();
        recursivePrint(this.root, out);
        return out.toString().trim();
    }

    private void recursivePrint(TreeNode<K> node, StringBuilder out) {
        if (node == null) {
            return;
        }
        if (node.leftKey != null) {
            out.append(node.leftKey)
                    .append(" ");
        }
        if (node.rightKey != null) {
            out.append(node.rightKey).append(System.lineSeparator());
        } else {
            out.append(System.lineSeparator());
        }
        if (node.isTwoNode()) {
            recursivePrint(node.leftChild, out);
            recursivePrint(node.rightChild, out);
        } else if (node.isThreeNode()) {
            recursivePrint(node.leftChild, out);
            recursivePrint(node.middleChild, out);
            recursivePrint(node.rightChild, out);
        }
    }

    public static class TreeNode<K> {
        private K leftKey;
        private K rightKey;

        private TreeNode<K> leftChild;
        private TreeNode<K> middleChild;
        private TreeNode<K> rightChild;

        private TreeNode(K key) {
            this.leftKey = key;
        }

        public TreeNode(K root, K left, K right) {
            this(root, new TreeNode<>(left), new TreeNode<>(right));
        }

        public TreeNode(K root, TreeNode<K> leftChild, TreeNode<K> rightChild) {
            this(root);
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }

        boolean isThreeNode() {
            return rightKey != null;
        }

        boolean isTwoNode() {
            return rightKey == null;
        }

        boolean isLeaf() {
            return this.leftChild == null && this.middleChild == null && this.rightChild == null;
        }
    }
}
