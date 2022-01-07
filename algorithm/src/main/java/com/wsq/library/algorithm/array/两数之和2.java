package com.wsq.library.algorithm.array;

import com.alibaba.fastjson.JSON;

/**
 * @author wsq
 * @Description
 * @date 2022/1/7 14:23
 */
public class 两数之和2 {
    public static int[] twoSum(int[] numbers, int target) {
        int i = 0;
        int j = numbers.length - 1;

        while (i < j) {
            int n = target - numbers[i];
            while (n < numbers[j]) j--;
            if (n == numbers[j]) break;

            i++;
        }

        return new int[] {++i, ++j};
    }

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(twoSum(new int[]{-1,0}, -1)));
    }
}
