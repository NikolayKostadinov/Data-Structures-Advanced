package main;

import javax.swing.text.html.HTMLDocument;
import java.util.*;
import java.util.stream.Collectors;

public class Hierarchy<T> implements IHierarchy<T> {
    private Map<T, HierarchyNode<T>> data;
    private HierarchyNode<T> root;
    private int count;

    public Hierarchy(T element) {
        this.data = new HashMap<>();
        this.root = new HierarchyNode<>(element);
        this.data.put(element, root);
        count++;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void add(T element, T child) {
        ensureExist(element);
        ensureUnique(child);
        HierarchyNode<T> parentNode = this.data.get(element);
        HierarchyNode<T> childNode = new HierarchyNode<>(child);
        childNode.setParent(parentNode);
        parentNode.getChildren().add(childNode);
        this.data.put(child, childNode);
        count++;
    }

    private void ensureUnique(T element) {
        if (this.data.containsKey(element)) {
            throw new IllegalArgumentException();
        }
    }

    private void ensureExist(T element) {
        if (!this.data.containsKey(element)) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void remove(T element) {
        ensureExist(element);
        HierarchyNode<T> node = this.data.get(element);
        if (this.root.getElement().equals(element)) {
            throw new IllegalStateException();
        }
        HierarchyNode<T> parent = node.getParent();
        node.getChildren().forEach(n -> n.setParent(parent));
        parent.getChildren().addAll(node.getChildren());
        parent.getChildren().remove(node);
        this.data.remove(element);
        count--;
    }

    @Override
    public Iterable<T> getChildren(T element) {
        ensureExist(element);
        HierarchyNode<T> node = this.data.get(element);
        return node.getChildren()
                .stream()
                .map(HierarchyNode::getElement)
                .collect(Collectors.toList());
    }

    @Override
    public T getParent(T element) {
        ensureExist(element);
        HierarchyNode<T> node = this.data.get(element);

        return node.getParent() == null ? null : node.getParent().getElement();
    }

    @Override
    public boolean contains(T element) {
        return this.data.containsKey(element);
    }

    @Override
    public Iterable<T> getCommonElements(IHierarchy<T> other) {
        return this.data
                .keySet()
                .stream()
                .filter(other::contains)
                .collect(Collectors.toList());
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Deque<HierarchyNode<T>> deque = new ArrayDeque<>(Arrays.asList(root));

            @Override
            public boolean hasNext() {
                return !deque.isEmpty();
            }

            @Override
            public T next() {
                HierarchyNode<T> node = deque.poll();
                node.getChildren().forEach(deque::offer);
                return node.getElement();
            }
        };
    }
}
