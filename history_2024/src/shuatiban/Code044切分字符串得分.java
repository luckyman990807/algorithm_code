package shuatiban;

/**
 * String s, int K, String[] parts, int[] record
 * Parts和records的长度一样长，s一定要分割成k个部分，分割出来的每部分在parts里必须得有，那一部分的得分在record里，请问s切成k个部分，返回最大得分
 */
public class Code044切分字符串得分 {
    /**
     * 前缀树节点
     */
    public static class Node {
        // 26个字母,26条路
        Node[] next = new Node[26];
        // 结束节点记录当前字符串的得分,非结束节点等于-1
        int record = -1;
    }

    /**
     * 前缀树+动态规划
     */
    public static int dp(String s, int k, String[] parts, int[] record) {
        // 构建前缀树
        Node trie = new Node();
        for (int i = 0; i < parts.length; i++) {
            char[] chars = parts[i].toCharArray();
            Node cur = trie;
            for (char c : chars) {
                int path = c - 'a';
                cur.next[path] = cur.next[path] == null ? new Node() : cur.next[path];
                cur = cur.next[path];
            }
            // parts[i]这个单词的结束节点,记录这个单词的分数
            cur.record = record[i];
        }

        char[] str = s.toCharArray();
        int[][] dp = new int[str.length + 1][k + 1];

        // 递归出口,填充dp表的最后一行和第一列
        // 当str一个字符都不剩时,只有剩余要切分的份数也=0,才算是有效拆法,剩余要切分的份数!=0都是无效解
        dp[str.length][0] = 0;
        for (int restK = 1; restK <= k; restK++) {
            dp[str.length][restK] = -1;
        }
        // 当剩余要切分的份数=0时,只有str也一个字符都不剩了,才算是有效拆法,str还有剩余都是无效解
        for (int i = 0; i < str.length; i++) {
            dp[i][0] = -1;
        }

        for (int start = str.length - 1; start >= 0; start--) {
            for (int restK = 1; restK <= k; restK++) {
                Node cur = trie;
                // 初始值=-1,因为递归出口的初始值也是-1
                dp[start][restK] = -1;
                // 遍历每个前缀字符串,从start到end
                for (int end = start; end < str.length; end++) {
                    int path = str[end] - 'a';
                    if (cur.next[path] == null) {
                        break;
                    }
                    cur = cur.next[path];
                    // 如果当前从start到end的前缀是单词表中的单词,并且剩下后缀能成功拆成k-1份,那么找到了一种新的拆分方式,分数=前缀单词的分数+后缀拆分的总分数,更新分数最大值
                    if (cur.record != -1 && dp[end + 1][restK - 1] != -1) {
                        dp[start][restK] = Math.max(dp[start][restK], cur.record + dp[end + 1][restK - 1]);
                    }
                }
            }
        }
        return dp[0][k];
    }

    public static void main(String[] args) {
        String str = "abcdefg";
        int K = 3;
        String[] parts = {"abc", "def", "g", "ab", "cd", "efg", "defg"};
        int[] record = {1, 1, 1, 3, 3, 3, 2};
        System.out.println(dp(str, K, parts, record));
    }
}
