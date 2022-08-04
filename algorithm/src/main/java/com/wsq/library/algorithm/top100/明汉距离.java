package com.wsq.library.algorithm.top100;

/**
 * @author wsq
 * @Description
 * @date 2022/7/22 16:50
 */
public class 明汉距离 {
    public static int hammingDistance(int x, int y) {
        x ^= y;
        int n = 0;
        while (x != 0) {
            x &= x-1;
            n++;
        }

        return n;
    }

    public static void main(String[] args) {
        System.out.println(hammingDistance(3,1));
    }
}
