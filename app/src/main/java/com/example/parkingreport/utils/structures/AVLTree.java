package com.example.parkingreport.utils.structures;

import java.util.ArrayList;
import java.util.List;
import com.example.parkingreport.data.local.api.HasID;


/**
 * 通用泛型AVL树，只要求存储的元素实现了Comparable接口
 * @param <T> 实现了Comparable的类型，比如User, Report等
 */
public class AVLTree<T extends Comparable<T> & HasID> {

    private class Node {
        T data;
        Node left, right;
        int height;

        Node(T data) {
            this.data = data;
            this.height = 1;
        }
    }

    private Node root;

    public void insert(T data) {
        root = insert(root, data);
    }

    public void delete(T data) {
        root = delete(root, data);
    }

    public List<T> inorderList() {
        List<T> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    @Override
    public String toString() {
        List<T> list = inorderList();
        return list.toString();
    }

    private Node insert(Node node, T data) {
        if (node == null) return new Node(data);

        int cmp = data.compareTo(node.data);
        if (cmp < 0) {
            node.left = insert(node.left, data);
        } else if (cmp > 0) {
            node.right = insert(node.right, data);
        } else {
            node.data = data;
            return node;
        }

        updateHeight(node);
        return balance(node);
    }

    private Node delete(Node node, T data) {
        if (node == null) return null;

        int cmp = data.compareTo(node.data);
        if (cmp < 0) {
            node.left = delete(node.left, data);
        } else if (cmp > 0) {
            node.right = delete(node.right, data);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node minNode = getMin(node.right);
            node.data = minNode.data;
            node.right = delete(node.right, minNode.data);
        }

        updateHeight(node);
        return balance(node);
    }

    public T find(int id) {
        Node node = find(root, id);
        return node == null ? null : node.data;
    }

    private Node find(Node node, int id) {
        if (node == null) return null;

        int cmp = Integer.compare(id, node.data.getID());
        if (cmp < 0) return find(node.left, id);
        else if (cmp > 0) return find(node.right, id);
        else return node;
    }

    private void inorder(Node node, List<T> list) {
        if (node != null) {
            inorder(node.left, list);
            list.add(node.data);
            inorder(node.right, list);
        }
    }

    private Node getMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private void updateHeight(Node node) {
        node.height = Math.max(height(node.left), height(node.right)) + 1;
    }

    private int getBalance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private Node balance(Node node) {
        int balance = getBalance(node);

        if (balance > 1) {
            if (getBalance(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }

        if (balance < -1) {
            if (getBalance(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }

        return node;
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }
}
