package com.wsq.library.algorithm.jianzhi1;

import java.util.*;

public class 扑克牌中的顺子 {
    public static boolean isStraight(int[] nums) {
        int joker = 0;
        Arrays.sort(nums);
        for (int i = 0; i < 4; i++) {
            if (nums[i] == 0) {
                joker++;
                continue;
            }

            if (nums[i] == nums[i + 1]) return false;
        }

        return nums[4] - nums[joker] < 5;
    }

    public static void main(String[] args) {
        System.out.println(isStraight(new int[] {8,7,11,0,9}));
    }
}
