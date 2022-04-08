package com.wsq.library.algorithm.jianzhi1;

public class 缺失的数字 {
    public static int missingNumber(int[] nums) {
        int l = 0;
        int r = nums.length - 1;
        int index = 0;
        while (l <= r) {
            index = (r + l) / 2;
            if (index == nums[index]) {
                l = index + 1;
                if (index == nums.length - 1) index++;
            } else {
                r--;
            }
        }

        return index;
    }

    public static void main(String[] args) {
        System.out.println(missingNumber(new int[] {0,1,2}));
    }
}
