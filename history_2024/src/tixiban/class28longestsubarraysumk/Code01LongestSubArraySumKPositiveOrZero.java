package tixiban.class28longestsubarraysumk;

/**
 * 给定一个数组arr,值非负,给定一个整数k,找出arr所有累加和等于k的子数组中最长的那个,返回其长度.
 * <p>
 * 思路:
 * 值非负,那么数组长度变大,累加和一定单调增,数组长度变小,累加和一定单调减,累加和有单调性,考虑滑动窗口.
 */
public class Code01LongestSubArraySumKPositiveOrZero {
    public static int maxLength(int[] arr, int k) {
        int left = 0;
        int right = 0;
        int sum = arr[0];
        int length = 0;
        while (right < arr.length) {
            if (sum == k) {
                length = Math.max(length, right - length + 1);
                right++;
            } else if (sum < k) {
                right++;
            } else {
                left++;
            }
        }
        return length;
    }
}
