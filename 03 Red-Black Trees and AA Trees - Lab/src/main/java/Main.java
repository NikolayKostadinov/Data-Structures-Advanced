public class Main {
    public static void main(String[] args) {
        Integer[] input = {
                18,
                13,
                1,
                7,
                42,
                73,
                56,
                24,
                6,
                2,
                74,
                69,
                55
        };


        AATree<Integer> aaTree = new AATree<>();

        for (Integer integer : input) {
            aaTree.insert(integer);
        }
    }
}
