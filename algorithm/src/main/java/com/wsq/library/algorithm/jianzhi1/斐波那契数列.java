package com.wsq.library.algorithm.jianzhi1;

/**
 * @author wsq
 * @Description
 * @date 2022/1/28 11:48
 */
public class 斐波那契数列 {
    // 递归
//    public static int fib(int n) {
//        if (n == 0) return 0;
//        if (n == 1) return 1;
//
//        return fib(n - 1) + fib(n - 2);
//    }

    // 动态规划
    public static int fib(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;

        int a = 0;
        int b = 1;
        int c = 0;

        for (int i = 2; i <= n; i++) {
            c = a + b;
            a = b;
            b = c;
        }

        return c;
    }

    public static void main(String[] args) {
        System.out.println(fib(45));
    }
}
