package trees.impl;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RedBlackTreeImplTest {
    @Test
    public void TestAddMany() {
        RedBlackTreeImpl<Integer, Integer> tree = new RedBlackTreeImpl<>();
        for (int i = 0; i < 10; i++) {
            tree.add(i, i);
        }

        assertEquals(4, tree.height());

        Integer[] expected = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        List<Integer> result = new ArrayList<>();

        tree.forEachInOrder(x -> result.add(x.value));
        Integer[] actual = result.toArray(Integer[]::new);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void TestAdd3NodesCorrect() {
        RedBlackTreeImpl<Integer, Integer> tree = new RedBlackTreeImpl<>();
        tree.add(6,6);
        tree.add(4,4);
        tree.add(8,8);

        assertEquals(1, tree.height());
        assertEquals(3, tree.getSize());
        assertEquals(Integer.valueOf(6), tree.getRoot().key);
        assertTrue(tree.isBlack(tree.getRoot()));
        assertEquals(Integer.valueOf(4), tree.getRoot().left.key);
        assertFalse(tree.isBlack(tree.getRoot().left));
        assertEquals(Integer.valueOf(8), tree.getRoot().right.key);
        assertFalse(tree.isBlack(tree.getRoot().right));
    }
}
