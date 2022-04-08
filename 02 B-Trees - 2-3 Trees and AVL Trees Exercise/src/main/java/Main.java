public class Main {

    public static void main(String[] args) {
        TwoThreeTree<Integer> tree = new TwoThreeTree<>();

        tree.insert(3);
        tree.insert(5);
        tree.insert(7);

        System.out.println(tree.getAsString());

    }
}
