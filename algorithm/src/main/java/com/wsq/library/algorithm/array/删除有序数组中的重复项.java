package com.wsq.library.algorithm.array;

import com.alibaba.fastjson.JSON;

public class 删除有序数组中的重复项 {
    public static int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;

        int k = 0;
        for (int n = 1; n < nums.length; n++) {
            if (nums[k] != nums[n]) {
                nums[++k] = nums[n];
            }
        }

        return k + 1;
    }

    public static void main(String[] args) {
        int[] nums = new int[] {1,1,2};
        System.out.println(removeDuplicates(nums));
        System.out.println(JSON.toJSONString(nums));
    }
}
