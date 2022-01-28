package com.wsq.library.algorithm.jianzhi1;

/**
 * @author wsq
 * @Description
 * @date 2022/1/28 14:02
 */
public class 数组中重复的数字 {

//    public static int findRepeatNumber(int[] nums) {
//        BitSet bitSet = new BitSet(nums.length);
//
//        for (int num : nums) {
//            if (bitSet.get(num)) {
//                return num;
//            }
//
//            bitSet.set(num);
//        }
//
//        return 0;
//    }

    public static int findRepeatNumber(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            if (i == nums[i]) continue;

            if (nums[nums[i]] != nums[i]) {
                int tmp = nums[i];
                nums[i] = nums[nums[i]];
                nums[tmp] = tmp;
            } else {
                return nums[i];
            }
        }

        return 0;
    }

    public static void main(String[] args) {
        System.out.println(findRepeatNumber(new int[]{2, 3, 1, 0, 2, 5, 3}));
    }
}
