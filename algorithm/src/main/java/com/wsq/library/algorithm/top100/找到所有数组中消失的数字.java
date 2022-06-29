package com.wsq.library.algorithm.top100;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wsq
 * @Description
 * @date 2022/6/16 14:36
 */
public class 找到所有数组中消失的数字 {
    public static List<Integer> findDisappearedNumbers(int[] nums) {
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < nums.length;) {
            if (nums[i] == i + 1) {
                i++;
                continue;
            }
            if (nums[i] == nums[nums[i] - 1]) {
                i++;
                continue;
            }

            int num = nums[i];
            nums[i] = nums[nums[i] - 1];
            nums[num - 1] = num;
        }

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != i + 1) result.add(nums[i]);
        }

        return result;
    }

    public static void main(String[] args) {
        System.out.println(findDisappearedNumbers(new int[] {4,1,1,2}));
    }
}
