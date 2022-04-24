package com.wsq.library.algorithm.jianzhi1;

public class 礼物的最大值 {
    public static int maxValue(int[][] grid) {
        int res = grid[0][0];
        int i = 0;
        int j = 0;

        while (i != grid.length - 1 || j != grid[0].length - 1) {
            if (i + 1 == grid.length) {
                res += grid[i][++j];
            } else if (j + 1 == grid[0].length) {
                res += grid[++i][j];
            } else {
                if (grid[i][j+1] > grid[i+1][j]) {
                    res += grid[i][++j];
                } else {
                    res += grid[++i][j];
                }
            }
        }

        return res;
    }

    public static void main(String[] args) {
        System.out.println(maxValue(new int[][] {{1,2,5},{3,2,1}}));
    }
}
