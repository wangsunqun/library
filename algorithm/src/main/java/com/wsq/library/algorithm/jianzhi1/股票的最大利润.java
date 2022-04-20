package com.wsq.library.algorithm.jianzhi1;

public class 股票的最大利润 {
    public static int maxProfit(int[] prices) {
        int l =0;
        int r =prices.length - 1;
        int res = 0;

        while (l < r) {
            if (prices[l] > prices[r]) {
                l++;
            } else {
                res = Math.max(res, prices[r] - prices[l]);
                r--;
            }
        }

        return res;
    }

    public static void main(String[] args) {
        System.out.println(maxProfit(new int[] {7,6,4,3,1}));
    }
}
