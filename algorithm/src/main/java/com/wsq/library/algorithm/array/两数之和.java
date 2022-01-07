package com.wsq.library.algorithm.array;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wsq
 * @Description
 * @date 2022/1/7 14:23
 */
public class 两数之和 {
    public static int[] twoSum(int[] nums, int target) {
        int[] result = new int[2];

        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            Integer index = map.get(nums[i]);
            if (index != null) {
                result[0] = index;
                result[1] = i;
                return result;
            }

            map.put(target - nums[i], i);
        }

        return result;
    }

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(twoSum(new int[]{3,3}, 6)));
    }
}
