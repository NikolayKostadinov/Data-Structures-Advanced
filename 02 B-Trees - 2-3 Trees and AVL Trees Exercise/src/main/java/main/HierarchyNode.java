package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HierarchyNode<T> {
    private T element;
    private HierarchyNode<T> parent;
    private List<HierarchyNode<T>> children;

    public HierarchyNode(T element) {
        this.element = element;
        this.children = new ArrayList<>();
    }

    public void setParent(HierarchyNode<T> parent) {
        this.parent = parent;
    }

    public HierarchyNode<T> getParent() {
        return parent;
    }

    public List<HierarchyNode<T>> getChildren() {
        return children;
    }

    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }
}
