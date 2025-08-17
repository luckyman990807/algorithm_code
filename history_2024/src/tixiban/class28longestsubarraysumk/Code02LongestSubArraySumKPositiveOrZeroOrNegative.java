package tixiban.class28longestsubarraysumk;

import java.util.HashMap;
import java.util.Map;

/**
 * 给定一个数组arr,值可能正可能0可能负,给定一个整数k,求arr所有累加和等于k的子数组中最长的那个,返回其长度.
 *
 * 思路:
 * 有负数的话,累加和就不单调了,不能用滑动窗口了.
 *
 * 但凡出现子数组,一定可以用这个思路:
 * 以每个位置为结尾的子数组满足xx条件.
 * 找出以每个位置结尾的累加和=k的最长子数组,返回所有答案中最长的那个即可.
 *
 * 如果i位置前缀和为100,那么要求以i结尾累加和为k的最长子数组,只要找出前缀和=100-k最早出现的位置j,从j+1到i就是以i结尾累加和=k的最长子数组.
 */
public class Code02LongestSubArraySumKPositiveOrZeroOrNegative {
    public static int maxLength(int[] arr, int k) {
        int length = 0;
        int sum = 0;
        Map<Integer, Integer> map = new HashMap<>();
        /** 最开始一定要添加前缀和=0最早出现在-1位置,否则就会略过从0开始前缀和=k的子数组, */
        map.put(0, -1);
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            if (map.containsKey(sum - k)) {
                length = Math.max(length, i - map.get(sum - k) + 1);
            }
            if (!map.containsKey(sum)) {
                map.put(sum, i);
            }
        }
        return length;
    }

    /**
     * 扩展
     * 给定一个数组,值可能正可能负可能0,求所有-1和1数量相等的子数组中最长的那个,返回其长度.
     * 思路:
     * 原数组中=-1的不变,=1的不变,其他的都变成0,然后求累加和=0的子数组中最长的那个,跟上面的题一样.
     */
}
