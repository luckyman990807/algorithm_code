package tixiban.class1_13.dynamicprogramming;

/**
 * 给定两个字符串，求最大公共子序列的长度
 * 什么是子序列：一个字符串，每一位字符都可以选择要/不要，最终留下的新字符串
 * <p>
 * 经验：处理两个样本的时候，通常两个样本从右往左处理
 */
public class Code06MaxPublicSubsequence {
    public static int processViolent(char[] s1, int i1, char[] s2, int i2) {
        // baseCase
        // 当前计算的是s1[0]和s2[0]的最大公共子序列，两个都只有1位，那么很显然，如果相等，公共子序列长度为1，不相等为0
        if (i1 == 0 && i2 == 0) {
            return s1[i1] == s2[i2] ? 1 : 0;
        }
        // 当前计算的是s1[0]和s2[0~i2]的最大公共子序列，因为s1只有一位所以公共子序列要么1位要么0位。
        // 如果s1[i1] == s2[i2]，说明s1仅剩的i1就是公共子序列，否则，就递归换s2的i2来比较
        if (i1 == 0) {
            return s1[i1] == s2[i2] ? 1 : processViolent(s1, i1, s2, i2 - 1);
        }
        // 同上
        if (i2 == 0) {
            return s1[i1] == s2[i2] ? 1 : processViolent(s1, i1 - 1, s2, i2);
        }

        // 可能性1、子序列一定不以i1结尾，但可能以i2结尾的情况
        int p1 = processViolent(s1, i1 - 1, s2, i2);
        // 可能性2、子序列可能以i1结尾，但一定不以i2结尾的情况
        int p2 = processViolent(s1, i1, s2, i2 - 1);
        // 可能性3、子序列一定以i1和i2结尾的情况
        int p3 = s1[i1] == s2[i2] ? 1 + processViolent(s1, i1 - 1, s2, i2 - 1) : 0;

        // 3种情况选最大值
        return Math.max(Math.max(p1, p2), p3);
    }

    public static int maxPublicSubsequenceViolent(String s1, String s2) {
        if (s1 == null || s1.length() == 0 || s2 == null || s2.length() == 0) {
            return 0;
        }
        return processViolent(s1.toCharArray(), s1.length() - 1, s2.toCharArray(), s2.length() - 1);
    }

    /**
     * 动态规划法
     */

    /**
     * 强依赖动态规划表
     */
    public static int maxPublicSubsequence(String s1, String s2) {
        if (s1 == null || s1.length() == 0 || s2 == null || s2.length() == 0) {
            return 0;
        }
        char[] s1Chars = s1.toCharArray();
        char[] s2Chars = s2.toCharArray();

        int[][] dpMap = new int[s1.length()][s2.length()];

        // 根据baseCase1
        dpMap[0][0] = s1Chars[0] == s2Chars[0] ? 1 : 0;
        // 根据baseCase2
        for (int i = 1; i < s2.length(); i++) {
            dpMap[0][i] = s1Chars[0] == s2Chars[i] ? 1 : dpMap[0][i - 1];
        }
        // 根据baseCase3
        for (int i = 1; i < s1.length(); i++) {
            dpMap[i][0] = s1Chars[i] == s2Chars[0] ? 1 : dpMap[i - 1][0];
        }

        // 普遍情况，取3种可能性的最大值
        for (int i = 1; i < s1.length(); i++) {
            for (int j = 1; j < s2.length(); j++) {
                int p1 = dpMap[i - 1][j];
                int p2 = dpMap[i][j - 1];
                int p3 = s1Chars[i] == s2Chars[j] ? 1 + dpMap[i - 1][j - 1] : 0;
                dpMap[i][j] = Math.max(Math.max(p1, p2), p3);
            }
        }
        return dpMap[s1.length() - 1][s2.length() - 1];
    }

    public static void main(String[] args) {
        String s1 = "asdf1jio23sd";
        String s2 = "q123op";
        System.out.println(maxPublicSubsequenceViolent(s1, s2));
        System.out.println(maxPublicSubsequence(s1, s2));
    }
}
