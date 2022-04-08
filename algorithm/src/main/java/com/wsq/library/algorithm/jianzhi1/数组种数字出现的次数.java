package com.wsq.library.algorithm.jianzhi1;

import com.alibaba.fastjson.JSON;

public class 数组种数字出现的次数 {
    public static int[] singleNumbers(int[] nums) {
        int n = 0;
        for (int num : nums) {
            n ^= num;
        }

        int m = 1;
        while ((m & n) == 0) {
            m = m << 1;
        }

        int[] res = new int[2];
        for (int num : nums) {
            if ((num & m) == 0) {
                res[0] ^= num;
            } else {
                res[1] ^= num;
            }
        }

        return res;
    }

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(singleNumbers(new int[]{1,1,3,4,4,5,6,6})));
    }
}
