package com.wsq.library.algorithm.array;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wsq
 * @Description
 * @date 2022/1/7 15:24
 */
public class 三数之和 {
    public static List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums.length < 3) return result;

        Arrays.sort(nums);

        for (int i = 0; i < nums.length - 2; i++) {
            if (nums[i] > 0) return result;

            if (i > 0 && nums[i] == nums[i - 1]) continue;

            int l = i + 1;
            int r = nums.length - 1;

            while (l < r) {
                int k = nums[i] + nums[l] + nums[r];
                if (k == 0) {
                    result.add(Arrays.asList(nums[i], nums[l], nums[r]));
                    while(l < r && nums[l+1] == nums[l]) ++l;
                    while (l < r && nums[r-1] == nums[r]) --r;
                    r--;
                    l++;
                } else if (k > 0) {
                    r--;
                } else {
                    l++;
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(threeSum(new int[]{-2,0,0,2,2})));
    }
}