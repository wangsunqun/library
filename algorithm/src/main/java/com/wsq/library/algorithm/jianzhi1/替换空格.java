package com.wsq.library.algorithm.jianzhi1;

/**
 * @author wsq
 * @Description
 * @date 2022/2/24 14:05
 */
public class 替换空格 {
    public static String replaceSpace(String s) {
        StringBuilder sb = new StringBuilder();

        for (Character cc : s.toCharArray()) {
            if (((int)cc) == 32) {
                sb.append("%20");
            } else {
                sb.append(cc);
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(replaceSpace("   "));
    }
}