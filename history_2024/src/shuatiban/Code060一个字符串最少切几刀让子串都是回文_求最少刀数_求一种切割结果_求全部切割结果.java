package shuatiban;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode.com/problems/palindrome-partitioning-ii/
 * 问题一：一个字符串至少要切几刀能让切出来的子串都是回文串
 * 问题二：返回问题一的其中一种划分结果
 * 问题三：返回问题一的所有划分结果
 *
 * 思路：从左到右的尝试模型
 * 试法：如果前i个是回文，那么在i后面切一刀就是一种切割方法，切割刀数=1+f(i+1)，i从0到n-1尝试。
 * 改成动态规划就是一维，dp[i]=k表示从i到结尾的子串，最少切k刀能让切出来的都是回文。
 * 因为依赖右边，所以遍历从右往左求（一层遍历）求每个格子都要找出i开头的所有前缀（二层遍历），每个前缀要验证回文（三层遍历），所以复杂度O(N^3)。
 * 而多做一道动态规划就能让复杂度降到O(N^2)，方法是提前简历一张表，map[i][j]表示从i到j是不是回文。
 * 求这张表也好求：对角线i=j的值都是true，因为一个字母本就是回文。对角线上面一条斜线，两个字母，相等就true，不相等就false。普遍位置：如果[i]=[j]并且i+1到j-1是回文（map[i+1][j-1]），那i到j就是回文。
 *
 * 总结动态规划求所有可能性：
 * 1、求出动态规划表
 * 2、dp表倒着按所有可能性回溯，如果求一种可能性，就只找出一种最优的可能性即可，
 * 3、如果求所有可能性，就要找出所有最优的可能性都走一边（深度优先遍历）
 */
public class Code060一个字符串最少切几刀让子串都是回文_求最少刀数_求一种切割结果_求全部切割结果 {
    public int minCut(String s) {
        char[] str = s.toCharArray();

        // 第一道动态规划，构建回文判断表
        boolean[][] map = new boolean[str.length][str.length];
        for (int i = 0; i < str.length; i++) {
            map[i][i] = true;
        }
        for (int i = 0; i < str.length - 1; i++) {
            map[i][i + 1] = str[i] == str[i + 1];
        }
        for (int i = str.length - 3; i >= 0; i--) {
            for (int j = i + 2; j < str.length; j++) {
                map[i][j] = str[i] == str[j] && map[i + 1][j - 1];
            }
        }

        // 第二道动态规划，求最少切几刀
        int[] dp = new int[str.length + 1];
        for (int i = str.length - 1; i >= 0; i--) {
            if (map[i][str.length - 1]) {
                dp[i] = 1;
                continue;
            }
            int min = str.length - i;
            for (int j = i; j < str.length; j++) {
                if (map[i][j]) {
                    min = Math.min(min, 1 + dp[j + 1]);
                }
            }
            dp[i] = min;
        }
        // dp表记录的其实是被分成几段，减1就是刀数
        return dp[0] - 1;
    }

    /**
     * 返回一个结果
     */
    public int minCutReturnOneResult(String s) {
        char[] str = s.toCharArray();

        // 第一道动态规划，构建回文判断表
        boolean[][] map = new boolean[str.length][str.length];
        for (int i = 0; i < str.length; i++) {
            map[i][i] = true;
        }
        for (int i = 0; i < str.length - 1; i++) {
            map[i][i + 1] = str[i] == str[i + 1];
        }
        for (int i = str.length - 3; i >= 0; i--) {
            for (int j = i + 2; j < str.length; j++) {
                map[i][j] = str[i] == str[j] && map[i + 1][j - 1];
            }
        }

        // 第二道动态规划，求最少切几刀
        int[] dp = new int[str.length + 1];
        for (int i = str.length - 1; i >= 0; i--) {
            if (map[i][str.length - 1]) {
                dp[i] = 1;
                continue;
            }
            int min = str.length - i;
            for (int j = i; j < str.length; j++) {
                if (map[i][j]) {
                    min = Math.min(min, 1 + dp[j + 1]);
                }
            }
            dp[i] = min;
        }

        // 回溯求一个结果
        List<String> result = new ArrayList<>();
        // left和right两个指针框出的前缀，如果left到right-1是回文，并且dp[left] == 1 + dp[right]（即left到结尾全回文切割的最少段数=1+right到结尾全回文切割的最少段数），
        // 就说明从left到right-1这个回文子串是答案中的回文子串之一，于是把它收集到结果数组中，然后left来到right的位置，right++，继续寻找后面的答案回文子串
        // 否则的话，说明从left到right-1这个子串不是答案中的回文子串，直接right++查看更长的前缀
        for (int left = 0, right = 1; right < dp.length; right++) {
            if (map[left][right - 1] && dp[left] == 1 + dp[right]) {
                result.add(String.valueOf(str, left, right - left));
                left = right;
            }
        }
        System.out.println("input : " + s);
        System.out.println("result: " + result);

        // dp表记录的其实是被分成几段，减1就是刀数
        return dp[0] - 1;
    }

    /**
     * 返回所有结果
     */
    public static int minCutReturnAllResult(String s) {
        char[] str = s.toCharArray();

        // 第一道动态规划，构建回文判断表
        boolean[][] map = new boolean[str.length][str.length];
        for (int i = 0; i < str.length; i++) {
            map[i][i] = true;
        }
        for (int i = 0; i < str.length - 1; i++) {
            map[i][i + 1] = str[i] == str[i + 1];
        }
        for (int i = str.length - 3; i >= 0; i--) {
            for (int j = i + 2; j < str.length; j++) {
                map[i][j] = str[i] == str[j] && map[i + 1][j - 1];
            }
        }

        // 第二道动态规划，求最少切几刀
        int[] dp = new int[str.length + 1];
        for (int i = str.length - 1; i >= 0; i--) {
            if (map[i][str.length - 1]) {
                dp[i] = 1;
                continue;
            }
            int min = str.length - i;
            for (int j = i; j < str.length; j++) {
                if (map[i][j]) {
                    min = Math.min(min, 1 + dp[j + 1]);
                }
            }
            dp[i] = min;
        }

        // 回溯求所有结果
        List<List<String>> result = new ArrayList<>();
        process(str, 0, 1, dp, map, new ArrayList<>(), result);

        System.out.println("input : " + s);
        System.out.println("result: " + result);

        // dp表记录的其实是被分成几段，减1就是刀数
        return dp[0] - 1;
    }

    /**
     * 深度优先遍历（递归），本轮递归检查原字符串str从left到right-1的子串，是不是最优解的回文子串之一
     * @param str
     * @param left
     * @param right
     * @param dp dp表
     * @param map 回文判断表
     * @param ans 记录当前分支的划分结果
     * @param result 记录所有划分结果
     */
    public static void process(char[] str, int left, int right, int[] dp, boolean[][] map, List<String> ans, List<List<String>> result) {
        // 递归出口，如果right已经到头了，就判断left到right-1这个后缀是不是回文，如果是，就说明找到了一个完整的分割方案，收入结果集，然后恢复现场，返回上层。
        if (right == str.length) {
            if (map[left][right - 1] && dp[left] == 1 + dp[right]) {
                ans.add(String.valueOf(str, left, right - left));
                result.add(new ArrayList<>(ans));
                ans.remove(ans.size() - 1);
            }
            return;
        }

        // right没到头，也判断left到right-1是不是回文，是的话就说明找到了某种最优分割方案中的一个分割回文串，加入结果集，恢复现场
        if (map[left][right - 1] && dp[left] == 1 + dp[right]) {
            ans.add(String.valueOf(str, left, right - left));
            process(str, right, right + 1, dp, map, ans, result);
            ans.remove(ans.size() - 1);
        }
        // 即便发现了回文串，也可以放弃这个回文串，去寻找其他分割方案。
        // 比如aabaab，可以在left=0，right=3的时候选择aa这个串，后面再选择baab，也可以在left=0，right=3的时候放弃aa，在right=5的时候选择aabaa，再后面再选择b
        process(str, left, right + 1, dp, map, ans, result);
    }


    public static void main(String[] args) {
        String s = "aabaab";
        System.out.println(minCutReturnAllResult(s));
    }
}
