package com.wsq.library.algorithm.top100;

/**
 * @author wsq
 * @Description
 * @date 2022/6/15 11:03
 */
public class 对称二叉树 {
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

    public static boolean isSymmetric(TreeNode root) {
        return check(root, root);
    }

    private static boolean check(TreeNode left, TreeNode right) {
        if (left == null && right == null) return true;
        if (left == null || right == null) return false;
        return left.val == right.val && check(left.left, right.right) && check(left.right, right.left);
    }
}
