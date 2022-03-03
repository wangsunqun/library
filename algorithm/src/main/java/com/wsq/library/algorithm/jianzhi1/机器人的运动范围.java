package com.wsq.library.algorithm.jianzhi1;

/**
 * @author wsq
 * @Description
 * @date 2022/3/3 16:38
 */
public class 机器人的运动范围 {
    public static int movingCount(int m, int n, int k) {
        boolean[][] x = new boolean[m][n];
        return dfs(x, m, n, k, 0, 0);
    }

    private static int dfs(boolean[][] x, int m, int n, int k, int i, int j) {
        if (i >= m || j >= n || x[i][j] || i / 10 + i % 10 + j / 10 + j % 10 > k) return 0;
        x[i][j] = true;

        return 1 + dfs(x, m, n, k, i + 1, j) + dfs(x, m, n, k, i, j + 1);
    }

    public static void main(String[] args) {
        System.out.println(movingCount(2, 3, 1));
    }
}