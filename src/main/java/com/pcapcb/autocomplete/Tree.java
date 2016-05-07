package com.pcapcb.autocomplete;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pca006132 on 2016/5/7.
 */
public class Tree<T> implements Iterable<T> {
    private T data;
    List<Tree> children = new ArrayList<>();

    @Override
    public Iterator iterator() {
        return children.iterator();
    }
    public Tree(T data) {
        this.data = data;
    }
    public void addChild(Tree child) {
        children.add(child);
    }
    public Tree getChild(T data) {
        for (Tree tree : children) {
            if (tree.data == data)
                return tree;
        }
        return null;
    }
    public boolean contains(T data) {
        for (Tree tree : children) {
            if (tree.data == data)
                return true;
        }
        return false;
    }
}
