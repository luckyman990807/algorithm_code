package shuatiban;

import java.util.HashSet;
import java.util.Set;

/**
 * str是一个大字符串，arr是去重的单词表, 每个单词都不是空字符串且可以使用任意次。使用arr中的单词有多少种拼接str的方式，返回方法数。
 * 假设所有字符都是小写字母
 *
 * 思路:动态规划,从左往右的尝试模型
 * 试法:f(i) = str从i开始到结尾,拆分成单词表里的单词,有几种拆法?
 * 而f(i) = i到i前缀能拆成一个单词?f(i+1):0 + i到i+1前缀能拆成一个单词?f(i+2):0 + i到i+2前缀能拆成一个单词?f(i+3):0 + ...
 * N个格子,每个格子都要遍历求方法数的和,每个方法数又要遍历拼前缀,所以时间复杂度O(N^3)
 *
 * 优化思路:前缀树
 * 先把所有单词录入前缀树,
 * 试法依然是f(i)=str从i开始到结尾,拆分成单词表里的单词,有几种拆法?
 * str从i开始遍历,前缀数从根节点开始遍历,只要str当前j位置在前缀树中是一个结束节点,就说明i到j能拆成一个单词.
 * 只需要一轮O(N)遍历,就能知道i开头的每个前缀能不能拆成一个单词,同时累加方法数
 * 所以时间复杂度是O(N^2)+O(M)  (加上生成前缀树的复杂度)
 */
public class Code043用单词表拼字符串的方法数 {
    public static int dp(String str, String[] arr) {
        Set<String> words = new HashSet<>();
        for (String word : arr) {
            words.add(word);
        }

        int[] dp = new int[str.length() + 1];
        dp[str.length()] = 1;
        for (int i = str.length() - 1; i >= 0; i--) {
            for (int end = i + 1; end <= str.length(); end++) {
                dp[i] += words.contains(str.substring(i, end)) ? dp[end] : 0;
            }
        }
        return dp[0];
    }

    public static void main(String[] args) {
        String str = "aabbcd";
        String[] arr = {"a", "aa", "ab", "bb", "bcd", "cd"};
        System.out.println(dp(str, arr));
        System.out.println(dpTrie(str,arr));
    }

    /**
     * 前缀树优化的解,复杂度O(M)+O(N^2)
     */

    /**
     * 前缀树节点
     */
    public static class Node {
        boolean end;
        Node[] next;

        public Node() {
            end = false;
            next = new Node[26];
        }
    }

    /**
     * 前缀树优化的动态规划
     */
    public static int dpTrie(String str, String[] arr) {
        // 把单词都录入前缀树
        Node trie = new Node();
        for (String s : arr) {
            char[] chars = s.toCharArray();
            Node cur = trie;
            for (char c : chars) {
                cur.next[c - 'a'] = cur.next[c - 'a'] == null ? new Node() : cur.next[c - 'a'];
                cur = cur.next[c - 'a'];
            }
            cur.end = true;
        }

        int[] dp = new int[str.length() + 1];
        // 有两种理解法:
        // 1、从N位置到N-1位置,拆成单词表里的单词,有几种拆法?有1种拆法,就是什么都不要
        // 2、递归出口,当尝试完所有位置,说明得到了一种拆法,返回1
        dp[arr.length] = 1;

        char[] chars = str.toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            Node cur = trie;
            for (int end = i; end < chars.length; end++) {
                // 如果前缀i到end在前缀树中没有路,那么前缀i到end+1、i到end+2更没有路了
                if (cur.next[chars[end]-'a']==null){
                    break;
                }
                // 如果前缀i到end在前缀树中有路,且恰好是结束节点,说明前缀i到end是单词表中的单词
                if (cur.next[chars[end] - 'a'].end) {
                    dp[i] += dp[end + 1];
                }
                // 如果前缀i到end在前缀树中有路,但不是结束节点,说明前缀i到end不是单词但i到end+1可能是单词

                // 不管是不是单词,都要继续尝试下一个字符
                cur = cur.next[chars[end] - 'a'];
            }
        }
        return dp[0];
    }
}
