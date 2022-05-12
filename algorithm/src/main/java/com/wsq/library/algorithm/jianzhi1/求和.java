package com.wsq.library.algorithm.jianzhi1;

public class 求和 {
    public int sumNums(int n) {
        boolean b = n > 1 && (n += sumNums(n - 1)) > 0;
        return n;
    }
}
