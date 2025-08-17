package shuatiban;

/**
 * 给定三个字符串s1、s2、s3，请你帮忙验证s3是否是由s1和s2交错组成的
 * Leetcode题目：https://leetcode.com/problems/interleaving-string/
 *
 * 思路:
 * 动态规划,样本对应模型
 * 尝试str1拿前i个,str2拿前j个,能否组成st3前i+j个,组织dp表
 *
 * 为什么不能直接用merge?例如遍历str3判断是否来自str1或者str2
 * 答:因为如果str1和str2存在相同字符,就无法区分来自谁,一旦找错了,后面就可能对不上了.
 * 例如str1=111asd,str2=111fgh,str3=111asd111fgh,如果认为str3开始的111都来自str2,那么后边的就都对不上了.但实际是可以组成的.
 */
public class Code028字符串交错组成 {
    public static boolean isInterleaveViolent(String s1, String s2, String s3) {
        if (s1.length() + s2.length() != s3.length()) {
            return false;
        }
        return process(s1.toCharArray(), s1.length(), s2.toCharArray(), s2.length(), s3.toCharArray());
    }

    /**
     * 尝试思路:str1拿前i个,str2拿前j个,能否组成st3前i+j个
     */
    public static boolean process(char[] str1, int i, char[] str2, int j, char[] str3) {
        // 递归出口1.str1拿前0个,str2拿前0个,可以组成str3前0个
        if (i == 0 && j == 0) {
            return true;
        }
        // 递归出口2.如果str1拿完了,只剩str2了,那么必须保证str2和str3剩下的所有字符都相等,才算达标
        if (i == 0) {
            for (int k = 0; k < j; k++) {
                if (str2[k] != str3[k]) {
                    return false;
                }
            }
            return true;
        }
        // 递归出口3.如果str2拿完了,只剩str1了,那么必须保证str1和str3剩下的所有字符都相等,才算达标
        if (j == 0) {
            for (int k = 0; k < i; k++) {
                if (str1[k] != str3[k]) {
                    return false;
                }
            }
            return true;
        }

        // 可能性1.str3最后一个字符由str1组成,那么递归判断str3除去最后一个 能否由str1除去最后一个 和str2当前的 组成
        // 可能性2.str3最后一个字符由str2组成,那么递归判断str3除去最后一个 能否由str1当前的 和str2除去最后一个 组成
        // 两种可能性取或
        return (str3[i + j - 1] == str1[i - 1] && process(str1, i - 1, str2, j, str3)) || (str3[i + j - 1] == str2[j - 1] && process(str1, i, str2, j - 1, str3));
    }

    public static void main(String[] args) {
        String s1 = "111asd";
        String s2 = "111fgh";
        String s3 = "111agh111fsd";
        System.out.println(isInterleave(s1, s2, s3));
    }

    /**
     * 改动态规划
     * 二维dp表,dp[i][j]=true表示 str1取前i个,str2取前j个,能组成str3前i+j个.
     * =false则表示不能组成.
     */
    public static boolean isInterleave(String s1, String s2, String s3) {
        if (s1.length() + s2.length() != s3.length()) {
            return false;
        }

        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        char[] str3 = s3.toCharArray();

        boolean[][] dp = new boolean[str1.length + 1][str2.length + 1];

        dp[0][0] = true;
        for (int k = 1; k < str1.length; k++) {
            if (str1[k-1] != str3[k-1]) {
                // 匹配不上了可以直接退出,因为只要str1前5个没法组成str3前5个,那么str1前6个也绝对没法组成str3前6个
                break;
            }
            dp[k][0] = true;
        }
        for (int k = 1; k < str2.length; k++) {
            if (str2[k-1] != str3[k-1]) {
                break;
            }
            dp[0][k] = true;
        }

        for (int i = 1; i < str1.length; i++) {
            for (int j = 1; j < str2.length; j++) {
                dp[i][j] = (str3[i + j - 1] == str1[i - 1] && dp[i - 1][j]) || (str3[i + j - 1] == str2[j - 1] && dp[i][j - 1]);
            }
        }

        return dp[str1.length][str2.length];
    }

}
