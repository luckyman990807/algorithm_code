package shuatiban;

import java.util.Arrays;

/**
 * 给定一个数组arr，求最长递增子序列长度
 * 本题测试链接 : https://leetcode.com/problems/longest-increasing-subsequence
 *
 * 思路:
 * 解法1:动态规划,看到子序列就想以i结尾的子序列,i从0到n-1,求以每个位置结尾的最长递增子序列长度,返回最大值.
 * 复杂度O(N^2)
 * 解法2:辅助数组end[i]=k表示长度为i的递增子序列的最小结尾是k.遍历数组,每个元素在辅助数组中用二分法找目前能作为最多多么长的子序列的结尾,最后辅助数组中有效区的长度就是答案.
 * 复杂度O(N*logN)
 */
public class Code051最长递增子序列 {
    public static int lengthOfLISDp(int[] arr) {
        int max = 1;
        int[] dp = new int[arr.length];
        dp[0] = 1;
        for (int i = 1; i < arr.length; i++) {
            int preMax = Integer.MIN_VALUE;
            for (int j = 0; j < i; j++) {
                if (arr[j] < arr[i] && dp[j] > preMax) {
                    preMax = dp[j];
                }
            }
            dp[i] = preMax == Integer.MIN_VALUE ? 1 : preMax + 1;
            max = Math.max(max, dp[i]);
        }
        return max;
    }

    public static void main(String[] args) {
        int[] arr = {1, 3, 6, 7, 9, 4, 10, 5, 6};
        System.out.println(lengthOfLIS(arr));
    }

    public static int lengthOfLIS(int[] arr) {
        int[] end = new int[arr.length];
        end[0] = arr[0];
        int endRight = 0;

        for (int i = 1; i < arr.length; i++) {
            int left = 0;
            int right = endRight;
            while (left <= right) {
                int mid = left + ((right - left) >> 1);
                // 注意,=跟>放在一起,作为目标右边的数被排除掉,说明最终找的是<目标的最接近的数
                if (end[mid] < arr[i]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            end[left] = arr[i];
            endRight = Math.max(endRight, left);
        }
        System.out.println(Arrays.toString(end));
        return endRight + 1;
    }

}
