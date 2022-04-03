package com.wsq.library.algorithm.jianzhi1;

public class 在排序数组中查找数字 {
    public int search(int[] nums, int target) {
        int count = 0;
        for (int num : nums) {
            if (num == target) count++;

            if (num > target) break;
        }

        return count;
    }
}
