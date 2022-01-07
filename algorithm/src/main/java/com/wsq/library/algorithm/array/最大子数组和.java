package com.wsq.library.algorithm.array;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * @author wsq
 * @Description
 * @date 2022/1/6 19:45
 */
public class 最大子数组和 {
    public static int maxSubArray(int[] nums) {
        int[] s = new int[nums.length];
        s[0] = nums[0];

        for (int i = 1; i < nums.length; i++) {
            s[i] = nums [i] + s[i - 1] ;
        }

        int[] min = new int[s.length];
        int max = Integer.MIN_VALUE;
        min[0] = s[0];
        for (int i = 1; i < s.length; i++) {
            min[i] = Math.min(min[i - 1], s[i]);
            max = Math.max(max, s[i] - min[i - 1]);
        }

        return max;
    }

    public static void main(String[] args) {
        System.out.println(maxSubArray(new int[]{-2,1,-3,4,-1,2,1,-5,4}));
    }
}
