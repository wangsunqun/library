package com.wsq.library.algorithm.stack;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author wsq
 * @Description
 * @date 2021/12/30 13:50
 */
public class 有效的括号 {
    public static boolean isValid(String s) {
        Map<Character, Character> map = new HashMap<>(3);
        map.put('(', ')');
        map.put('[', ']');
        map.put('{', '}');

        Deque<Character> stack = new LinkedList<>();
        for (char c : s.toCharArray()) {
            if (map.containsKey(c)) {
                stack.push(c);
            } else {
                if (stack.size() == 0) return false;

                Character pop = stack.pop();
                if (map.get(pop).equals(c)) {
                    continue;
                }

                return false;
            }
        }

        return stack.size() == 0;
    }

    public static void main(String[] args) {
    }
}
