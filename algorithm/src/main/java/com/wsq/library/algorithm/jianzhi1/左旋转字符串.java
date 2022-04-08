package com.wsq.library.algorithm.jianzhi1;

public class 左旋转字符串 {
    public static String reverseLeftWords(String s, int n) {
        return s.substring(n) + s.substring(0, n);
    }

    public static void main(String[] args) {
        System.out.println(reverseLeftWords("abcdefgh", 2));
    }
}
