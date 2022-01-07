package com.wsq.library.algorithm.array;

import java.util.HashMap;
import java.util.Map;

/** 前缀和
 * @author wsq
 * @Description
 * @date 2021/12/31 14:53
 */
public class 统计优美子数组 {
    public int numberOfSubarrays(int[] nums, int k) {
        int[] s = new int[nums.length + 1];
        Map<Integer, Integer> count = new HashMap<>();

        count.put(0, 1);
        for (int i = 1; i <= nums.length; i++) {
            int x = s[i - 1] + nums[i - 1] % 2;
            s[i] = x;
            count.put(x, count.getOrDefault(x, 0) + 1);
        }

        int result = 0;
        for (int n : s) {
            result += count.getOrDefault(n - k, 0);
        }

        return result;
    }
}
