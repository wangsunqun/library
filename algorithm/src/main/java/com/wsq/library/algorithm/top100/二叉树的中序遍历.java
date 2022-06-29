package com.wsq.library.algorithm.top100;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wsq
 * @Description
 * @date 2022/6/14 13:47
 */
public class 二叉树的中序遍历 {
    public static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;

        mid(root, result);
        return result;
    }

    private static void mid(TreeNode node, List<Integer> result) {
        if (node.left != null) mid(node.left, result);
        result.add(node.val);
        if (node.right != null) mid(node.right, result);
    }

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
}
