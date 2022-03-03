package com.wsq.library.algorithm.jianzhi1;

/**
 * @author wsq
 * @Description
 * @date 2022/3/3 16:38
 */
public class 机器人的运动范围 {
//    public static int movingCount(int m, int n, int k) {
//        int count = 0;
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < n; j++) {
//                if (i / 10 + i % 10 + j / 10 + j % 10 <= k) {
//                    System.out.println("i=" + i + ", j=" + j);
//                    count++;
//                } else {
//                    break;
//                }
//            }
//        }
//
//        return count;
//    }

    public static int movingCount(int m, int n, int k) {
        boolean[][] visited = new boolean[m][n];
        return dfs(visited, m, n, k, 0, 0);
    }

    private static int dfs(boolean[][] visited, int m, int n, int k, int i, int j) {
        if(i >= m || j >= n || visited[i][j] || bitSum(i) + bitSum(j) > k) return 0;
        visited[i][j] = true;
        System.out.println("i=" + i + ", j=" + j);
        return 1 + dfs(visited, m, n, k, i + 1, j) + dfs(visited, m, n, k, i, j + 1) ;
    }

    private static int bitSum(int n) {
        int sum = 0;
        while(n > 0) {
            sum += n % 10;
            n /= 10;
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(movingCount(16, 8, 4));
    }
}