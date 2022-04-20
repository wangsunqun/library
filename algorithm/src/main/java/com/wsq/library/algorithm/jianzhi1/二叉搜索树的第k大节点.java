package com.wsq.library.algorithm.jianzhi1;

import java.util.ArrayList;
import java.util.List;

public class 二叉搜索树的第k大节点 {
    public int kthLargest(TreeNode root, int k) {
        List<Integer> list = new ArrayList<>();
        mid(root, list);

        return list.get(list.size() -k);
    }

    private void mid(TreeNode node, List<Integer> list) {
        if (node == null) return;

        if (node.left != null) mid(node.left, list);
        list.add(node.val);
        if (node.right != null) mid(node.right, list);
    }

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }
}
