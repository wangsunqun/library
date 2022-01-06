package com.wsq.library.algorithm.math.差分;

import com.alibaba.fastjson.JSON;

/**
 * @author wsq
 * @Description
 * @date 2022/1/6 18:17
 */
public class 航班预订统计 {
    public static int[] corpFlightBookings(int[][] bookings, int n) {
        int[] d = new int[n];

        for (int[] booking : bookings) {
            int start = booking[0];
            int end = booking[1];
            int num = booking[2];

            d[start - 1] += num;

            if (end != n) {
                d[end] -= num;
            }
        }

        for (int i = 1; i < d.length; i++) {
            d[i] += d[i - 1];
        }

        return d;
    }

    /**
     * [[1,2,10],[2,3,20],[2,5,25]]
     * 5
     */
    public static void main(String[] args) {
        int[][] p = {{1,2,10},{2,3,20}, {2,5,25}};
        System.out.println(JSON.toJSONString(corpFlightBookings(p, 5)));
    }
}
