import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    private static String word;
    private static Map<String, Integer> wordsCount = new HashMap<>();
    private static Map<Integer, TreeSet<String>> tree = new HashMap<>();
    private static List<String> buffer = new ArrayList<>();
    private static StringBuilder output = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputString = Arrays.asList(reader.readLine().split(", "));
        word = reader.readLine();

        inputString
                .stream()
                .filter(string -> word.contains(string))
                .forEach(string ->
                {
                    fillWords(string);
                    formTree(string);
                });

        dfsTraversal(0);

        System.out.println( output.toString().trim());
    }

    private static void fillWords(String string) {
        wordsCount.putIfAbsent(string, 0);
        wordsCount.put(string, wordsCount.get(string) + 1);
    }

    private static void formTree(String string) {
        int index = word.indexOf(string);
        while (index != -1) {
            if (!tree.containsKey(index)) {
                tree.put(index, new TreeSet<>());
            }
            tree.get(index).add(string);
            index = word.indexOf(string, index + 1);
        }
    }

    private static void dfsTraversal(int index) {
        if (index == word.length()) {
            appendResult();
        } else {
            if (!tree.containsKey(index)) {
                return;
            }

            for (String str : tree.get(index)) {
                if (wordsCount.get(str) > 0) {
                    buffer.add(str);
                    wordsCount.put(str, wordsCount.get(str) - 1);
                    dfsTraversal(index + str.length());
                    wordsCount.put(str, wordsCount.get(str) + 1);
                    buffer.remove(buffer.size() - 1);
                }
            }
        }
    }

    private static void appendResult() {
        String result = String.join("", buffer);
        if (result.equals(word))
            output.append(String.join(" ", buffer)).append(System.lineSeparator());
    }
}
