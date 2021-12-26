package com.wsq.library.algorithm.array;

import com.alibaba.fastjson.JSON;

public class 移动零 {
    public static void moveZeroes(int[] nums) {
        int k = -1;
        for (int i = 0; i < nums.length; i++) {
            if (k == -1 && nums[i] == 0) {
                k = i;
                continue;
            }

            if (nums[i] == 0) continue;

            if (k != -1 && nums[k] == 0) {
                int t = nums[i];
                nums[i] = nums[k];
                nums[k] = t;
                k++;
            }
        }
    }

    public static void main(String[] args) {
        int[] num = {1};
        moveZeroes(num);
        System.out.println(JSON.toJSONString(num));
    }
}
