package com.wsq.library.algorithm.jianzhi1;

import java.util.*;

public class 最长不含重复字符的子字符串 {
    public static int lengthOfLongestSubstring(String s) {
        if (s == null || s.equals("")) return 0;

        List<Character> win = new ArrayList<>();

        int max = Integer.MIN_VALUE;

        for (Character c : s.toCharArray()) {
            if (!win.contains(c)) {
                win.add(c);
                max = Math.max(win.size(), max);
            } else {
                Iterator<Character> iterator = win.iterator();
                while (iterator.hasNext()) {
                    Character next = iterator.next();
                    if (next != c) {
                        iterator.remove();
                    } else {
                        iterator.remove();
                        break;
                    }
                }
                win.add(c);
            }
        }

        return max;
    }

    public static void main(String[] args) {
        System.out.println(lengthOfLongestSubstring("pwwkew"));
    }
}
