package shuatiban;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定两个字符串s1和s2，问s2最少删除多少字符可以成为s1的子串？比如 s1 = "abcde"，s2 = "axbc"，s2删掉'x'即可，返回1
 * s1长度为N,s2长度为M
 *
 *
 * 思路:三种方法:
 *
 * 1、最优解,动态规划,试法:s2前l2个字符,最少删除几个,就能变成s1前l1个字符的后缀?
 * 时间复杂度O(N*M).
 * 所以,看到字符串,就要想到前缀、后缀、子序列、以xx结尾、以xx开头等等..
 *
 * 其他解
 * 2、s2的所有子序列列出来(2^M种情况,每个字符决定删掉还是留下),然后每个子序列判断是不是s1的字串(KMP算法,或者直接用Java里String的indexOf(),复杂度O(N)).
 * 整体复杂的O(2^M * N).
 * 3、s1的所有子串列出来(复杂度N^2,注意子串连续,子序列不连续),然后计算用s2转换成每个子串的最小编辑距离(上一道题,复杂度N*M).
 * 整体复杂度O(N^3 * M).
 * 如果M特别小,用方法2.如果M特别大,用方法3.具体可以用实践证明哪种情况下用哪种方法合适.
 */
public class Code033字符串二只通过删除得到字符串一 {
    /**
     * 最优解
     */
    public static int dp(String s1, String s2) {
        char[] str2 = s1.toCharArray();
        char[] str1 = s2.toCharArray();

        // dp[l1][l2]=k表示str1前l1个字符,最少删除k个,就能成为str2前l2个字符的后缀
        int[][] dp = new int[str1.length + 1][str2.length + 1];

        // 边界条件,当str2取前0个字符,也就是str1前l1个字符最少删除几个就能成为空字符的后缀?答案是全删了,有l1个就删l1个
        for (int l1 = 1; l1 <= str1.length; l1++) {
            dp[l1][0] = l1;
        }
        // 另外的边界条件,当str1取前0个字符,也就是空字符最少删除几个就能成为str2前l2个字符的后缀?这里认为不需要要删除,空字符是任意字符的后缀(如果面试官不这么认为,那么可以改成删除Integer.Max个,也就是空字符无论如何也变不成另一个字符的后缀.)

        for (int l1 = 1; l1 <= str1.length; l1++) {
            for (int l2 = 1; l2 <= str2.length; l2++) {
                int min = Integer.MAX_VALUE;
                // 可能性1、如果str1最后一个字符跟str2最后一个字符相等,那么可以尝试保留str1的最后一个字符,用str1前面的去凑str2前面的后缀
                if (str1[l1 - 1] == str2[l2 - 1]) {
                    min = Math.min(min, dp[l1 - 1][l2 - 1]);
                }
                // 可能性2、str1删掉最后一个,用前面的去凑str2的后缀
                min = Math.min(min, 1 + dp[l1 - 1][l2]);
                dp[l1][l2] = min;
                // 或者也可以这样写,当str1最后一个字符跟str2最后一个字符相等时,必须让str1保留最后一个字符
//                if (str1[l1] == str2[l2]) {
//                    dp[l1][l2] = dp[l1 - 1][l2 - 1];
//                } else {
//                    dp[l1][l2] = 1 + dp[l1 - 1][l2];
//                }
            }
        }
        return dp[str1.length][str2.length];
    }


    /**
     * 方法2、子序列+KMP
     */
    public static int subSequenceKMP(String s1, String s2) {
        // 列出s2所有的子序列
        List<String> subSeqs = new ArrayList<>();
        process(s2.toCharArray(), 0, "", subSeqs);
        // 子序列按照从长到短排序,因为越长的子序列删的字符越少
        subSeqs.sort((o1, o2) -> o2.length() - o1.length());
        // 遍历所有子序列
        for (String subSeq : subSeqs) {
            // 判断这个子序列是不是s1的子串.indexOf底层和KMP算法代价几乎一样,用哪个都行
            if (s1.indexOf(subSeq) != -1) {
                // 子序列长度跟s2的差值就是删除的字符数
                return s2.length() - subSeq.length();
            }
        }
        return Integer.MAX_VALUE;
    }

    /**
     * 递归求字符串str从index位置开始的所有子序列,将结果保存到results中
     */
    public static void process(char[] str, int index, String curSubSeq, List<String> results) {
        if (index == str.length) {
            results.add(curSubSeq);
            return;
        }
        // 可能性1、index位置的字符删掉
        process(str, index + 1, curSubSeq, results);
        // 可能性2、index位置的字符保留
        process(str, index + 1, curSubSeq + str[index], results);
    }


    /**
     * 方法3、子串+编辑距离
     */
    public static int subStrEditDistance(String s1, String s2) {
        int min = Integer.MAX_VALUE;
        for (int start = 0; start < s1.length(); start++) {
            for (int end = start + 1; end <= s1.length(); end++) {
                min = Math.min(min, editDistance(s2, s1.substring(start, end)));
            }
        }
        return min;
    }

    /**
     * 动态规划求s1只通过删除转换成s2的最小编辑距离
     */
    public static int editDistance(String s1, String s2) {
        if (s1.length() < s2.length()) {
            return Integer.MAX_VALUE;
        }
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();

        int[][] dp = new int[str1.length + 1][str2.length + 1];
        for (int l1 = 0; l1 < str2.length; l1++) {
            for (int l2 = l1 + 1; l2 <= str2.length; l2++) {
                dp[l1][l2] = Integer.MAX_VALUE;
            }
        }
        for (int l1 = 0; l1 <= str1.length; l1++) {
            dp[l1][0] = l1;
        }

        for (int l1 = 1; l1 <= str1.length; l1++) {
            for (int l2 = 1; l2 <= Math.min(l1, str2.length); l2++) {
                int min = Integer.MAX_VALUE;
                if (str1[l1 - 1] == str2[l2 - 1]) {
                    min = Math.min(min, dp[l1 - 1][l2 - 1]);
                }
                if (dp[l1 - 1][l2] != Integer.MAX_VALUE) {
                    min = Math.min(min, 1 + dp[l1 - 1][l2]);
                }
                dp[l1][l2] = min;
            }
        }

        return dp[str1.length][str2.length];
    }


    /**
     * 最优解,时间复杂度
     */

    /**
     * 目前字串+编辑距离法没把s2删到只剩空字符串算作一个答案,子序列+KMP法把s2删到空字符串算作一个答案了.具体面试时可以问面试官这个算不算答案.
     */
    public static void main(String[] args) {
        String s1 = "zsd";
        String s2 = "h";
        System.out.println(subStrEditDistance(s1, s2));
        System.out.println(subSequenceKMP(s1, s2));
        System.out.println(dp(s1, s2));
    }

}
