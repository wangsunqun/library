package com.wsq.library.ac;

import lombok.Data;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.list.TreeList;

import java.util.*;

@Data
public class Trie {
    private TrieNode root = new TrieNode();

    @Data
    public static class TrieNode {
        private Character value;
        private TrieNode parent;
        private TrieNode fail;
        private Boolean isEnd = false;
        private List<Integer> output = new TreeList<>();
        private Map<Character, TrieNode> children = new HashMap<>();
    }

    public synchronized void addWord(String word) {
        word = word.trim();

        TrieNode old = root;
        TrieNode cur;
        for (Character c : word.toCharArray()) {
            cur = old.children.get(c);
            if (Objects.isNull(cur)) {
                cur = new TrieNode();
                cur.parent = old;
                cur.value = c;
                old.children.put(c, cur);
            }

            old = cur;
        }

        old.isEnd = true;
        old.output.add(word.length());
    }

    public synchronized void buildFail() {
        Queue<TrieNode> queue = new LinkedList<>();

        for (Map.Entry<Character, TrieNode> entry : root.children.entrySet()) {
            entry.getValue().fail = root;
            queue.add(entry.getValue());
        }

        while (!queue.isEmpty()) {
            TrieNode cur = queue.poll();
            if (MapUtils.isNotEmpty(cur.children)) {
                cur.children.forEach((key, value) -> queue.add(value));
            }

            TrieNode parent = cur.parent;

            while (Objects.nonNull(parent)) {
                TrieNode fail = parent.fail;
                if (Objects.isNull(fail)) break;

                TrieNode node = fail.children.get(cur.value);
                if (Objects.nonNull(node)) {
                    cur.fail = node;
                    cur.output.addAll(node.output);
                    break;
                }

                parent = fail;
            }
        }
    }

    public boolean hasSensitiveWord(String keyWord) {
        return search(keyWord).size() != 0;
    }

    public String filter(String keyWord) {
        List<SearchResult> result = search(keyWord);

        char[] chars = keyWord.toCharArray();
        for (SearchResult searchResult : result) {
            for (int i = searchResult.start; i <= searchResult.end; i++) {
                chars[i] = '*';
            }
        }

        return new String(chars);
    }

    public List<SearchResult> search(String keyWord) {
        List<SearchResult> result = new TreeList<>();

        TrieNode node = root;
        keyWord = preHandle(keyWord);
        for (int i = 0; i < keyWord.length(); i++) {
            while (Objects.nonNull(node)) {
                TrieNode tmp = node.children.get(keyWord.charAt(i));
                if (Objects.nonNull(tmp)) {
                    node = tmp;
                    isEndNode(i, node, result);
                    break;
                }

                node = node.fail;
            }

            node = Objects.isNull(node) ? root : node;
        }

        return result;
    }

    private static String preHandle(String keyWord) {
        // 去空格
        // 如果空格前面是英文那么不去空格，这段是老顾之前为了兼容英文搜索加的，但是其实有bug，比如c 你妈，按照老顾的逻辑是不会被匹配到的
        keyWord = keyWord.trim();

        char[] chars = keyWord.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ' ') {
                char preC = chars[i - 1];
                if ((preC < 'a' || preC > 'z') && (preC < 'A' || preC > 'Z')) {
                    chars[i] = '\u0000';
                }
            }
        }

        keyWord = new String(chars);

        // 统一转成小写
        keyWord = keyWord.toLowerCase();

        return keyWord;
    }

    private void isEndNode(int index, TrieNode node, List<SearchResult> res) {
        for (int o : node.output) {
            SearchResult result = new SearchResult();
            result.setStart(index - o + 1);
            result.setEnd(index);

            res.add(result);
        }
    }

    @Data
    public static class SearchResult {
        private Integer start;
        private Integer end;
    }

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.addWord("he");
        trie.addWord("she");
        trie.addWord("his");
        trie.addWord("hers");

        TrieNode root = trie.root;
        trie.buildFail();

//        List<SearchResult> isherso = trie.search("sheriheo");
//        System.out.println(JSON.toJSONString(isherso));

        System.out.println(trie.filter("sh eriheo"));
    }
}
