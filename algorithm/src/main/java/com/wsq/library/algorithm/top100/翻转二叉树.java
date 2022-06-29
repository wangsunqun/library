package com.wsq.library.algorithm.top100;

/**
 * @author wsq
 * @Description
 * @date 2022/6/28 16:50
 */
public class 翻转二叉树 {
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

        public TreeNode invertTree(TreeNode root) {
                if (root == null) return null;

                TreeNode left = invertTree(root.left);
                TreeNode right = invertTree(root.right);

                root.left = right;
                root.right = left;

                return root;
        }
    }
}