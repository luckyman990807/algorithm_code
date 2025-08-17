package shuatiban;

/**
 * https://leetcode.com/problems/maximum-subarray/
 * 返回一个数组中子数组最大累加和
 *
 * 思路:
 * 看到子数组,就要想到:以0结尾满足xx条件的结果,以1结尾满足xx条件的结果..从所有结果中取最大值
 * 以i位置结尾的累加和最大的子数组,有两种可能性:
 * 1.只含有i位置自己
 * 2.从i位置往左扩.扩到哪呢?i-1位置结尾的子数组最大累加和扩到哪,i位置就扩到哪
 */
public class Code023子数组最大累加和 {
    /**
     * 暴力递归
     */
    public static int violent(int[] arr) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            max = Math.max(max, process(arr, i));
        }
        return max;
    }

    public static int process(int[] arr, int index) {
        if (index == 0) {
            return arr[0];
        }
        return Math.max(arr[index], arr[index] + process(arr, index - 1));
    }

    /**
     * 改动态规划
     */
    public static int maxSubArray(int[] arr) {
        // 因为只有一个自变量,一维动态规划,而且只依赖左边位置,所以可以用一个变量滚动代替dp表
        int dp = arr[0];
        int max = dp;
        for (int i = 1; i < arr.length; i++) {
            // 这里相当于在填写dp表的i位置,作为比较的dp相当于dp表的i-1位置
            dp = Math.max(arr[i], arr[i] + dp);
            max = Math.max(max, dp);
        }
        return max;
    }

    /**
     * 另一种写法
     */
    public static int maxSum(int[] arr) {
        int max = Integer.MIN_VALUE;
        int curSum = 0;
        for (int i = 0; i < arr.length; i++) {
            curSum += arr[i];
            max = Math.max(max, curSum);
            // 如果以i结尾的累加和小于0,那么下个循环以i+1结尾的累加和肯定不会向左扩了,只能选择i+1位置自己作为以i+1结尾的累加和.
            if (curSum < 0) {
                curSum = 0;
            }
        }
        return max;
    }
}
