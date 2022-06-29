package com.wsq.library.algorithm.top100;

/**
 * @author wsq
 * @Description
 * @date 2022/6/15 15:59
 */
public class 把二叉搜索树转换为累加树 {
    public class TreeNode {
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

//    public TreeNode convertBST(TreeNode root) {
//        int sum = sum(root);
//
//    }
//
//    private int sum(TreeNode node) {
//        if (node == null) return 0;
//        return node.val + sum(node.left) + sum(node.right);
//    }
}
