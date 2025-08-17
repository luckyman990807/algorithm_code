package shuatiban;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定一个有正、有负、有0的数组arr，
 * 给定一个整数k，
 * 返回arr的子集是否能累加出k
 * 1）正常怎么做？
 * 思路：基本思路是背包问题，难点是有正有负，这样累加和也有正有负，而dp表下标只能从0开始，所以需要额外做位置映射。
 * 2）如果arr中的数值很大，但是arr的长度不大，怎么做？
 * 思路：分治+暴力法，这种题往往是原数据量直接暴力会超时，但是分成两部分之后，每部分暴力不会超时，难点在于分治的合并。
 * 首先把整个数组分成两部分，每一部分暴力求出所有可能的累加和，这时有3种情况：
 * 1、第一部分的累加和中就包含k，直接返回。2、第二部分的累加和中就包含k，直接返回。3、用第一部分的某个累加和+第二部分的某个累加和才能得到k，需要到一个结果集中找[k-另一个结果集的某个累加和]。
 */
public class Code081是否存在累加和为K的子集 {
    /**
     * 第一问，背包问题+下标转换即可，难点是下标转换
     */
    public static boolean sumK(int[] arr, int k) {
        // 数组中所有负数的累加和，也就是子集累加和的最小值
        int min = 0;
        // 数组中所有正数的累加和，也就是子集累加和的最大值
        int max = 0;
        for (int num : arr) {
            if (num > 0) {
                max += num;
            } else if (num < 0) {
                min += num;
            }
        }
        // dp[i][j]表示从0到i能否凑出累加和j。j的取值是从min到max，但是数组下标不能是负数，所以用0到max-min表示min到max
        boolean[][] dp = new boolean[arr.length][max - min + 1];
        // 初始值
        // 从arr[0到0]只能凑出累加和arr[0]，不能凑出其他累加和
        dp[0][arr[0] - min] = true;
        // 从arr[任意范围]都能凑出累加和0（一个数也不要）
        for (int i = 0; i < dp.length; i++) {
            // 累加和0对应数组上的下标是0 + -min
            dp[i][-min] = true;
        }

        for (int i = 1; i < dp.length; i++) {
            for (int j = min; j <= max; j++) {
                // 填dp[i][j]，也就是arr[从0到i]能否凑出累加和j
                // 可能性1，不要arr[i]，那么arr[0到i-1]就要凑出累加和j。累加和j对应到数组下标是j+-min
                dp[i][j - min] = dp[i - 1][j - min];
                // 可能性2，要arr[i]，那么arr[0到i-1]就要凑出累加和j-arr[i]，对应到数组下标是j-arr[j]
                int next = j - arr[i] - min;
                if (!dp[i][j - min] && next >= 0 && next <= max - min) {
                    dp[i][j - min] |= dp[i - 1][j - arr[i] - min];
                }
            }
        }
        // arr[从0到n-1]，能凑凑出累加和k。累加和k对应到数组下标是k-min
        return dp[arr.length - 1][k - min];
    }

    public static void main(String[] args) {
        int k = 10;
        int[] arr = {2, 3, 50, -20, -30, 50, 60, -3};
        System.out.println(sumK(arr, k));
        System.out.println(sumK2(arr, k));
    }

    /**
     * 第二问，分治+暴力枚举+merge
     */
    public static boolean sumK2(int[] arr, int k) {
        // 得到左半边数组的所有可能的累加和结果集1，包含累加和0
        List<Integer> leftAns = new ArrayList<>();
        process(arr, 0, (arr.length >> 1) - 1, 0, leftAns);
        // 得到右半边数组的所有可能的累加和结果集2，包含累加和0
        List<Integer> rightAns = new ArrayList<>();
        process(arr, (arr.length >> 1), arr.length - 1, 0, rightAns);
        // 从结果集2中找等于k-结果集1[i]的结果
        for (int leftSum : leftAns) {
            if (rightAns.contains(k - leftSum)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 递归，start之前的数的累加和已经是curSum了，计算arr从start到end的所有可能的累加和，把最终结果写入ans集合中。
     * 试法：可能性1，要start位置的数，那么从start+1到end就要带着curSum+arr[start]作为当前累加和继续统计。可能性2，不要start位置的数，那么从start+1到end依然带着curSum作为当前累加和继续统计。
     */
    public static void process(int[] arr, int start, int end, int curSum, List<Integer> ans) {
        if (start > end) {
            // 递归出口，如果start到end范围上所有的数都统计过了，那么把一种可能的累加和放入结果集中
            ans.add(curSum);
        } else {
            process(arr, start + 1, end, curSum + arr[start], ans);
            process(arr, start + 1, end, curSum, ans);
        }
    }
}
