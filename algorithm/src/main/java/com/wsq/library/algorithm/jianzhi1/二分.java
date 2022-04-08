package com.wsq.library.algorithm.jianzhi1;

public class 二分 {
    public static void main(String[] args) {
        System.out.println(splite(new int[]{1,2,3,4,5,6}, 0, 5, 3));
    }

    private static int splite(int[] nums, int left, int right, int target) {
        int index = (left + right) / 2;
        if (nums[index] == target) return index;

        if (nums[index] > target) {
            return splite(nums, left, index - 1, target);
        } else {
            return splite(nums, index + 1, right, target);
        }
    }
}
