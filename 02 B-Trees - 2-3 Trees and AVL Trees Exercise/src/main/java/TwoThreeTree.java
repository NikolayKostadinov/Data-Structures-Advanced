public class TwoThreeTree<K extends Comparable<K>> {
    private TreeNode<K> root;

    public void insert(K key) {
        TreeNode<K> newNode = new TreeNode<>(key);
        if (this.root == null) {
            this.root = newNode;
            return;
        }

        TreeNode<K> newRoot = this.insert(this.root, newNode);
        if (newRoot != null) this.root = newRoot;
    }

    private TreeNode<K> insert(TreeNode<K> node, TreeNode<K> newNode) {
        if (node.isLeaf()) {
            return insertIntoNode(node, newNode);
        }

        TreeNode<K> nodeToInsert = navigateToLeave(node, newNode);
        return insertIntoNode(node, nodeToInsert);
    }

    private TreeNode<K> insertIntoNode(TreeNode<K> node, TreeNode<K> newNode) {
        if (newNode == null) return null;

        if (node.isTwoNode()) {
            insertInToTwoNode(node, newNode);
            return null;
        }

        return InsertIntoThreeNode(node, newNode);
    }

    private TreeNode<K> navigateToLeave(TreeNode<K> node, TreeNode<K> newNode) {
        if (less(newNode.leftKey, node.leftKey)) {
            return insert(node.leftChild, newNode);
        }

        if (node.isTwoNode() && greater(newNode.leftKey, node.leftKey)) {
            return insert(node.rightChild, newNode);
        }

        if (node.isThreeNode() && greater(newNode.leftKey, node.rightKey)) {
            return insert(node.rightChild, newNode);
        }

        return insert(node.middleChild, newNode);

    }

    private void insertInToTwoNode(TreeNode<K> node, TreeNode<K> newNode) {
        if (less(newNode.leftKey, node.leftKey)) {
            node.rightKey = node.leftKey;
            node.leftKey = newNode.leftKey;

            node.leftChild = newNode.leftChild;
            node.middleChild = newNode.rightChild;
        } else {
            node.rightKey = newNode.leftKey;

            node.middleChild = newNode.leftChild;
            node.rightChild = newNode.rightChild;
        }
    }

    private TreeNode<K> InsertIntoThreeNode(TreeNode<K> node, TreeNode<K> newNode) {

        K promoteValue = newNode.leftKey;
        TreeNode<K> left = new TreeNode<>(node.leftKey, node.leftChild, newNode.leftChild);
        TreeNode<K> right = new TreeNode<>(node.rightKey, newNode.rightChild, node.rightChild);

        if (less(newNode.leftKey, node.leftKey)) {
            promoteValue = node.leftKey;
            left = newNode;
            right = new TreeNode<>(node.rightKey, node.middleChild, node.leftChild);
        } else if (greater(newNode.leftKey, node.rightKey)) {
            promoteValue = node.rightKey;
            left = new TreeNode<>(node.leftKey, node.leftChild, node.middleChild);
            right = newNode;
        }

        return new TreeNode<>(promoteValue, left, right);
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

    private boolean less(K first, K second) {
        return first.compareTo(second) < 0;
    }

    private boolean greater(K first, K second) {
        return first.compareTo(second) > 0;
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
