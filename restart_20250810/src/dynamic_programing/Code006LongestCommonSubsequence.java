package dynamic_programing;

import java.util.Arrays;

/**
 * 给定两个字符串，求最长公共子序列
 * https://leetcode.cn/problems/longest-common-subsequence/description/
 *
 * 思路：样本对应模型动态规划。一个字符串做行，一个字符串做列，以结尾位置展开讨论可能性。
 *
 * 定义process(str1,str2,i,j)的含义是str1从0到i，和str2从0到j，这两个字串的最长公共子序列长度。
 *
 * 可能性1:
 * i和j都算进最长公共子序列，如果str1[i]=str2[j]那么这种可能性成立，递归1+process(str1,str2,i-1,j-1)，否则不成立得到0。
 * 可能性2:
 * i一定不算进最长公共子序列（j没有约束），那么递归process(str1,str2,i-1,j)
 * 问：为什么要排除而不是纳入？把i算进最长公共子序列但j不算进作为可能性2不行吗？
 * 答：证明可能性成立太复杂。如果i一定算进最长公共子序列，就得证明str2里也含有str1[i]，这个可能性才成立，而且要找到在[0,j]之间最后一次出现的位置，可能只能遍历才能找到，复杂度太高。而i一定不算进最长公共子序列，就不需要任何证明，因为我可以强制不要。
 * 可能性3:
 * j一定不算进最长公共子序列（i没有约束），那么递归process(str1,str2,i,j-1)
 *
 * 三种可能性取最大值。
 * 问：可能性2和可能性3有重合，即i和j都不算进最长公共子序列的情况，为什么不剔除？
 * 答：我们求的是最长长度，而不是所有方法数，因此重复计算不影响结果，不需要剔除。
 *
 */
public class Code006LongestCommonSubsequence {
    /**
     * 解法一阶段：暴力递归
     * @param text1
     * @param text2
     * @return
     */
    public static int force(String text1, String text2) {
        if (text1 == null || text1.isEmpty() || text2 == null || text2.isEmpty()) {
            return 0;
        }
        return process(text1.toCharArray(), text2.toCharArray(), text1.length() - 1, text2.length() - 1);
    }

    public static int process(char[] str1, char[] str2, int i, int j) {
        // 递归出口，边际条件
        // 两个子串都只剩一个字符：如果这俩字符相等，那么直接让最长公共子串长度+1，否则+0
        if (i == 0 && j == 0) {
            return str1[i] == str2[j] ? 1 : 0;
        }
        // str1只剩一个字符：如果这个字符和str2[j]相等，就直接让最长公共子串+1，返回（不需要递归了，因为str1到头了，不会有更多公共子序列了）；如果不相等，那么递归去和str2剩下的字符做匹配。
        if (i == 0) {
            return str1[i] == str2[j] ? 1 : process(str1, str2, i, j - 1);
        }
        // str2只剩一个字符：同理
        if (j == 0) {
            return str1[i] == str2[j] ? 1 : process(str1, str2, i - 1, j);
        }

        // 可能性1：i和j都算进最长公共子序列
        int p1 = str1[i] == str2[j] ? 1 + process(str1, str2, i - 1, j - 1) : 0;
        // 可能性2:i一定不算进最长公共子序列
        int p2 = process(str1, str2, i - 1, j);
        // 可能性3:j一定不算进最长公共子序列
        int p3 = process(str1, str2, i, j - 1);

        return Math.max(p1, Math.max(p2, p3));
    }

    /**
     * 解法二阶段：动态规划（严格位置依赖）
     * @param text1
     * @param text2
     * @return
     */
    public static int dp(String text1, String text2) {
        if (text1 == null || text1.isEmpty() || text2 == null || text2.isEmpty()) {
            return 0;
        }

        char[] str1 = text1.toCharArray();
        char[] str2 = text2.toCharArray();

        // 初始状态
        int[][] dp = new int[str1.length][str2.length];
        dp[0][0] = str1[0] == str2[0] ? 1 : 0;
        for (int j = 1; j < str2.length; j++) {
            dp[0][j] = str1[0] == str2[j] ? 1 : dp[0][j - 1];
        }
        for (int i = 1; i < str1.length; i++) {
            dp[i][0] = str1[i] == str2[0] ? 1 : dp[i - 1][0];
        }

        // 状态转移
        for (int i = 1; i < str1.length; i++) {
            for (int j = 1; j < str2.length; j++) {
                int p1 = str1[i] == str2[j] ? 1 + dp[i - 1][j - 1] : 0;
                int p2 = dp[i - 1][j];
                int p3 = dp[i][j - 1];
                dp[i][j] = Math.max(p1, Math.max(p2, p3));
            }
        }

        return dp[str1.length - 1][str2.length - 1];
    }



    public static void main(String[] args) {
        String text1 = "absdnxmc";
        String text2 = "ipbgnhblm";
        System.out.println(force(text1, text2));
        System.out.println(dp(text1, text2));
    }
}
