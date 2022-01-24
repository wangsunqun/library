package com.wsq.library.algorithm.array;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author wsq
 * @Description
 * @date 2022/1/24 19:56
 */
public class 柱状图中最大的矩形 {
    public int largestRectangleArea(int[] heights) {
        Deque<Integer> stack = new LinkedList<>();
        for (int height : heights) {

            int weight = 0;
            while (!stack.isEmpty() && heights[stack.peek()] >= height) {
                Integer pop = stack.pop();

            }
        }
    }
}