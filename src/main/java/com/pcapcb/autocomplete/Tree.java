package com.pcapcb.autocomplete;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pca006132 on 2016/5/7.
 */
public class Tree implements Iterable {
    public String data;
    List<Tree> children = new ArrayList<>();

    @Override
    public Iterator iterator() {
        return children.iterator();
    }
    public Tree(String data) {
        this.data = data;
    }
    public void addChild(Tree child) {
        children.add(child);
    }
    public Tree getChild(String data) {
        for (Tree tree : children) {
            if (tree.data == data)
                return tree;
        }
        return null;
    }
    public boolean contains(String data) {
        for (Tree tree : children) {
            if (tree.data == data)
                return true;
        }
        return false;
    }
}
