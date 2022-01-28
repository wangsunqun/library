package com.wsq.library.algorithm.jianzhi1;

/**
 * @author wsq
 * @Description
 * @date 2022/1/28 16:20
 */
public class 旋转数组的最小数字 {
    public static int minArray(int[] numbers) {
        int ans = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] >= ans) {
                ans = numbers[i];
                continue;
            }

            return numbers[i];
        }

        return ans;
    }

    public static void main(String[] args) {
        System.out.println(minArray(new int[] {3,4,5,1,2}));
    }
}
