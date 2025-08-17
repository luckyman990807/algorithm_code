package shuatiban;

import java.util.HashSet;
import java.util.Set;

/**
 * https://leetcode.com/problems/minimum-insertion-steps-to-make-a-string-palindrome/
 *
 * 问题一：一个字符串至少需要添加多少个字符能整体变成回文串
 * 问题二：返回问题一的其中一种添加结果
 * 问题三：返回问题一的所有添加结果
 */
public class Code059最少加几个字母变成回文_返回方法数_返回回文串结果 {

    /**
     * 字符串s最少添加几个字符能变成回文？
     * 思路：范围上的尝试模型。dp表dp[i][j]=k表示从i下标到j下标的子串，最少加k个字母变成回文，那么dp[0][n - 1]就是答案
     * 初值：左下半边矩阵用不到，因为从i到j，j不能比j大。对角线上的值都是0，因为从i到i就一个字母本身就回文，不需要添加字母
     * 试法：3种可能性，求最小值:
     * 1、先把i到j-1的子串变成回文，然后在i前面添加一个str[j]就可以把i到j变成回文，最小添加数dp[i][j]=dp[i][j-1]+1
     * 2、先把i+1到j的子串变成回文，然后在j后面添加一个str[i]就可以把i到j变成回文，最小添加数dp[i][j]=dp[i+1][j]+1
     * 3、如果str[i]和str[j]相等，那么直接把i+1到j-1变成回文即可，最小添加数dp[i][j]=dp[i+1][j-1]
     */
    public int minInsertions(String s) {
        char[] str = s.toCharArray();
        // dp[i][j]=k表示从i下标到j下标的子串，最少加k个字母变成回文
        // dp[i][i]=0，因为从i到i一个字母本身就是回文
        int[][] dp = new int[str.length][str.length];

        for (int i = str.length - 2; i >= 0; i--) {
            for (int j = i + 1; j < str.length; j++) {
                int min = Integer.MAX_VALUE;
                // 试法改成状态转移
                min = Math.min(min, dp[i][j - 1] + 1);
                min = Math.min(min, dp[i + 1][j] + 1);
                if (str[i] == str[j]) {
                    min = Math.min(min, dp[i + 1][j - 1]);
                }
                dp[i][j] = min;
            }
        }
        return dp[0][str.length - 1];
    }

    /**
     * 返回问题一的其中一种添加结果
     * 动态规划表只记录最终答案，也就是最少添加的个数，但是可以通过回溯动态规划表一步一步恢复出结果对应的回文字符串
     * 思路：从动态规划表的答案位置开始回溯，面对不同试法，只要判断某一种试法是最优解，就按照这种试法继续回溯
     */
    public int minInsertionsReturnOneResult(String s) {
        // 动态规划
        char[] str = s.toCharArray();
        int[][] dp = new int[str.length][str.length];
        for (int i = str.length - 2; i >= 0; i--) {
            for (int j = i + 1; j < str.length; j++) {
                int min = Integer.MAX_VALUE;
                min = Math.min(min, dp[i][j - 1] + 1);
                min = Math.min(min, dp[i + 1][j] + 1);
                if (str[i] == str[j]) {
                    min = Math.min(min, dp[i + 1][j - 1]);
                }
                dp[i][j] = min;
            }
        }

        // 记录结果字符串，长度为原始串的长度+动态规划的结果（最少添加的个数）
        char[] result = new char[str.length + dp[0][str.length - 1]];
        // 记录结果字符串现在填到哪了
        int resultL = 0;
        int resultR = result.length - 1;
        // 记录现在dp表的哪个位置回溯，也就是回溯从strL到strR下标的答案
        int strL = 0;
        int strR = str.length - 1;
        // 回溯dp表，因为填的时候只填右上半个矩阵，所以回溯也只回溯右上半，也就是strL < strR
        while (strL < strR) {
            // 只要发现某种试法确实是最优解，就按照这种试法的思路去回溯、舍弃其他试法
            if (dp[strL][strR] == dp[strL][strR - 1] + 1) {
                // 如果从i到j的结果=从i到j-1的结果+1，说明先搞定i到j-1再在i前面添加str[j]是最优解（之一），那么结果可以确定的是开头和结尾一定是str[j]，结果剩下的位置是啥由dp[i][j-1]决定
                result[resultL++] = str[strR];
                result[resultR--] = str[strR--];
            } else if (dp[strL][strR] == dp[strL + 1][strR] + 1) {
                // 如果从i到j的结果=从i+1到j的结果+1，说明先搞定i+1到j再在j后面添加str[i]是最优解（之一），那么结果可以确定的是开头和结尾一定是str[i]，结果剩下的位置是啥由dp[i+1][j]决定
                result[resultL++] = str[strL];
                result[resultR--] = str[strL++];
            } else {
                // 否则就说明str[i]=str[j]，直接搞定i+1到j-1就是最优解
                result[resultL++] = str[strL++];
                result[resultR--] = str[strR--];
            }
            // strL == strR也就是只有一个字母的情况不需要添加字母，结果字符串直接就是当前字母。但因为strL和strR可能同时滑动而错过最后的中间位置，所以特意检查一下
            if (strL == strR) {
                result[resultL] = str[strL];
            }

        }
        System.out.println("原始串：" + s);
        System.out.println("结果：" + String.valueOf(result));

        return dp[0][str.length - 1];
    }

    /**
     * 返回问题一的所有添加结果
     * 思路：对于三种试法，把所有最优解的试法都深度优先遍历（递归）回溯
     */
    public int minInsertionsReturnAllResult(String s) {
        // 动态规划
        char[] str = s.toCharArray();
        int[][] dp = new int[str.length][str.length];
        for (int i = str.length - 2; i >= 0; i--) {
            for (int j = i + 1; j < str.length; j++) {
                int min = Integer.MAX_VALUE;
                min = Math.min(min, dp[i][j - 1] + 1);
                min = Math.min(min, dp[i + 1][j] + 1);
                if (str[i] == str[j]) {
                    min = Math.min(min, dp[i + 1][j - 1]);
                }
                dp[i][j] = min;
            }
        }

        Set<String> result = new HashSet<>();
        char[] ans = new char[str.length + dp[0][str.length - 1]];
        // 深搜
        process(dp, str, 0, str.length - 1, ans, 0, ans.length - 1, result);

        System.out.println("input :" + s);
        System.out.println("result: " + result);

        return dp[0][str.length - 1];
    }

    public void process(int[][] dp, char[] str, int strL, int strR, char[] ans, int ansL, int ansR, Set<String> result) {
        // 递归出口，两个指针错过去了，说明每一个位置都回溯完了
        if (strL > strR) {
            return;
        }
        // 递归出口，两个指针相等，说明就剩一个字母，不需要添加字母就是回文，结果字符串就是自己
        if (strL == strR) {
            ans[ansL] = str[strL];
            result.add(String.valueOf(ans));
            return;
        }

        // 三种试法都回溯，深度优先遍历（递归）
        if (dp[strL][strR] == dp[strL][strR - 1] + 1) {
            ans[ansL] = str[strR];
            ans[ansR] = str[strR];
            process(dp, str, strL, strR - 1, ans, ansL + 1, ansR - 1, result);
        }
        if (dp[strL][strR] == dp[strL + 1][strR] + 1) {
            ans[ansL] = str[strL];
            ans[ansR] = str[strL];
            process(dp, str, strL + 1, strR, ans, ansL + 1, ansR - 1, result);
        }
        if (str[strL] == str[strR] && dp[strL][strR] == dp[strL + 1][strR - 1]) {
            ans[ansL] = str[strL];
            ans[ansR] = str[strR];
            process(dp, str, strL + 1, strR - 1, ans, ansL + 1, ansR - 1, result);
        }
    }
}
