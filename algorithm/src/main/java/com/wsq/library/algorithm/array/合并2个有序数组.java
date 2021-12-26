package com.wsq.library.algorithm.array;

import com.alibaba.fastjson.JSON;

public class 合并2个有序数组 {
    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        int k = m + n - 1;
        m--;
        n--;
        for (; k >= 0; k--) {
            if (n < 0 || (m >= 0 && nums1[m] > nums2[n])) {
                nums1[k] = nums1[m--];
            } else {
                nums1[k] = nums2[n--];
            }
        }
    }

    public static void main(String[] args) {
        int[] num = {1,2,3,0,0,0};
        int[] num1 = {0,2,4};
        merge(num, 3, num1, 3);
        System.out.println(JSON.toJSONString(num));
    }
}
