package com.wsq.library.algorithm.jianzhi1;

public class 礼物的最大值 {
    public static int maxValue(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                if (i == 0) {
                    grid[i][j] = grid[i][j] + grid[i][j - 1];
                    continue;
                }

                if (j == 0) {
                    grid[i][j] = grid[i][j] + grid[i - 1][j];
                    continue;
                }

                grid[i][j] = grid[i][j] + Math.max(grid[i][j - 1], grid[i - 1][j]);
            }
        }

        return grid[grid.length - 1][grid[0].length - 1];
    }

    public static void main(String[] args) {
        System.out.println(maxValue(new int[][] {{1,3,1},{1,5,1},{4,2,1}}));
    }
}
