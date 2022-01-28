package com.wsq.library.algorithm.jianzhi1;

/**
 * @author wsq
 * @Description
 * @date 2022/1/28 14:58
 */
public class 二维数组中的查找 {
    public static boolean findNumberIn2DArray(int[][] matrix, int target) {
        if (matrix.length == 0 || matrix[0].length == 0) return false;

        for (int[] ints : matrix) {
            if (er(ints, target, 0, ints.length - 1)) return true;

            if (ints[0] > target) break;
        }

        return false;
    }

    private static boolean er(int[] m, int target, int start, int end) {
        if (start > end) return false;

        int i = (start + end) / 2;
        if (target == m[i]) return true;

        if (target > m[i]) {
            return er(m, target, i + 1, end);
        } else {
            return er(m, target, start, i - 1);
        }
    }

    public static void main(String[] args) {
        System.out.println(findNumberIn2DArray(new int[][]{{}}, 20));
    }
}
