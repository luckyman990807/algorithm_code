package shuatiban;

import java.util.Arrays;

/**
 * 给定一个正数数组arr，
 * 返回arr的子集不能累加出的最小正数
 *
 * 1）正常怎么做？
 * 思路：和上一个子集累加和的题类似，
 * 暴力递归：统计arr从0到n-1所有可能的累加和（每个数要或不要）放到结果集中，最后从正整数1开始检查是否结果集中是否包含，返回第一个不包含的正整数
 * 动态规划：dp[i][j]表示arr从0到i能否凑出累加和j。填好dp表之后，找出让dp[arr.length-1][j]=false的最小的j就是答案
 *
 * 2）如果arr中肯定有1这个值，怎么做？
 * 定义一个range变量，表示从1到range中任意一个数都可以被arr的子集累加出来。
 * 将arr排序，那么排序后的arr[0]肯定是1，遍历arr，假设arr[i-1及之前]已经搞定了range=10，
 * 可能性1，如果arr[i]<=range+1例如arr[i]=11，那么算上arr[i]就一定能搞定range=21，因为首先arr[i]自己就能搞定累加和11，而之前的range=10，就说明i之前肯定能搞定1，加上arr[i]就能搞定累加和12，同理i之前肯定能搞定2，加上arr[i]就能搞定累加和13...递推可知加上arr[i]能搞定range=21
 * 可能性2，如果arr[i]>range+1例如arr[i]=12，那么直接得到答案：一定搞不定累加和11，因为i前面只能搞定1到10，i=12，i后面的都>=12，所以再也不会有机会能搞定11
 *
 * 为什么确定arr中肯定有1就能用range这个方法？因为只有arr中肯定有1这个数，才能保证range=1的初始值是合法的，才能一路推下来。假如没有1，排序后最小的数是4，range初始值=4，就不合法了，因为只能搞定4，不不能搞定1，2，3，不符合range的定义。
 */
public class Code082数组子集不能累加出的最小正整数 {
    /**
     * 第一问，动态规划
     */
    public static int minUnFormedNumDp(int[] arr) {
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }
        boolean[][] dp = new boolean[arr.length][sum + 1];
        dp[0][arr[0]] = true;
        dp[0][0] = true;
        for (int i = 1; i < arr.length; i++) {
            for (int j = 0; j <= sum; j++) {
                dp[i][j] = dp[i - 1][j];
                if (!dp[i][j] && j - arr[i] >= 0 && j - arr[i] <= sum) {
                    dp[i][j] |= dp[i - 1][j - arr[i]];
                }
            }
        }
        for (int j = 0; j <= sum; j++) {
            if (!dp[arr.length - 1][j]) {
                return j;
            }
        }
        return sum + 1;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4};
        System.out.println(minUnFormedNumDp(arr));
        System.out.println(minUnformedNumRange(arr));
    }

    /**
     * 第二问，可以确定arr中一定有1这个数，引入range变量，复杂度O(N*logN)，主要在于排序。
     * 其实第一问也可以这样做：先排序，如果排序后第一个数不是1，那么返回答案1；如果第一个数是1，那么按照第二问这个思路来做。
     *
     */
    public static int minUnformedNumRange(int[] arr) {
        Arrays.sort(arr);
        int range = 1;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] <= range + 1) {
                range += arr[i];
            } else {
                return range + 1;
            }
        }
        return range + 1;
    }
}
