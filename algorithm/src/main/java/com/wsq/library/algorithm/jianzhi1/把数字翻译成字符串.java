package com.wsq.library.algorithm.jianzhi1;

public class 把数字翻译成字符串 {
    public static int translateNum(int num) {
        String s = String.valueOf(num);

        int a = 1;
        int b = 1;
        for (int i = 1; i < s.length(); i++) {
            String ss = s.substring(i - 1, i + 1);
            int c = ss.compareTo("10") >= 0 && ss.compareTo("25") <= 0 ? a + b : a;
            b = a;
            a = c;
        }

        return a;
    }

    public static void main(String[] args) {
        System.out.println(translateNum(0));
    }
}
