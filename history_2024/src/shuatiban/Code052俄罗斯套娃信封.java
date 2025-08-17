package shuatiban;

import java.util.Arrays;

/**
 * https://leetcode.com/problems/russian-doll-envelopes/
 * 给你一个二维整数数组 envelopes ，其中 envelopes[i] = [wi, hi] ，表示第 i 个信封的宽度和高度
 * 当另一个信封的宽度和高度都比这个信封大的时候，这个信封就可以放进另一个信封里，如同俄罗斯套娃一样
 * 请计算 最多能有多少个 信封能组成一组“俄罗斯套娃”信封（即可以把一个信封放到另一个信封里面）
 * 注意：不允许旋转信封
 *
 * 解法：信封按照宽度升序、高度降序排序，然后在高度数组中求醉成递增子序列
 * 宽度升序是为了保证不同宽度的信封前面能放在后面里
 * 高度降序是为了套娃的时候避免宽度相同的信封之间互相干扰。前面的想套进后面里，就只能选宽度不同的，而宽度又是升序的，就保证了套娃的准确。
 */
public class Code052俄罗斯套娃信封 {
    public static int maxEnvelopes(int[][] envelopes) {
        // 宽度升序、高度降序排列
        Arrays.sort(envelopes, (en1, en2) -> en1[0] != en2[0] ? en1[0] - en2[0] : en2[1] - en1[1]);

        // 在高度数组中求最长递增子序列
        int[] end = new int[envelopes.length + 1];
        end[1] = envelopes[0][1];
        int endRight = 1;

        for (int[] en : envelopes) {
            int left = 1;
            int right = endRight;
            while (left <= right) {
                int mid = left + (right - left >> 1);
                if (end[mid] < en[1]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            end[left] = en[1];

            endRight = Math.max(endRight, left);
        }

        return endRight;
    }


    /**
     * 最长递增子序列的不同解法
     */

    /**
     * 递归法，递归计算以每个位置结尾的最长递增子序列长度，最后返回最大的那个
     */
    public static int lengthOfLIS_violent(int[] arr) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            max = Math.max(max, process(arr, i));
        }
        return max;
    }

    /**
     * 递归试法：arr以i为结尾的最长递增子序列长度=i前面位置为结尾的最长递增子序列+1
     */
    public static int process(int[] arr, int i) {
        if (i == 0) {
            return 1;
        }
        int max = 0;
        for (int j = i - 1; j >= 0; j--) {
            if (arr[j] < arr[i]) {
                max = Math.max(max, process(arr, j));
            }
        }
        return max + 1;
    }

    /**
     * 改动态规划
     */
    public static int lengthOfLIS_dp(int[] arr) {
        int result = Integer.MIN_VALUE;
        int[] dp = new int[arr.length];
        dp[0] = 1;
        for (int i = 0; i < arr.length; i++) {
            int max = 0;
            for (int j = i - 1; j >= 0; j--) {
                if (arr[j] < arr[i]) {
                    max = Math.max(max, dp[j]);
                }
            }
            dp[i] = max + 1;
            result = Math.max(result, dp[i]);
        }
        return result;
    }

    /**
     * 最优解，动态规划求i为结尾的最长递增子序列长度，复杂度是O(N)，这里是O(logN)
     */
    public static int lengthOfLIS(int[] arr) {
        // 辅助数组，end[i]=k表示：目前已知的长度为i的递增子序列，最小的结尾是k。
        int[] end = new int[arr.length + 1];
        // 赋初值，长度为1的递增子序列，最小的结尾是arr[0]
        end[1] = arr[0];
        // 辅助数组的有效边界，也就代表目前已知的最长递增子序列长度
        int endRight = 1;

        // 遍历arr每个位置，利用辅助数组计算以每个位置结尾的最长递增子序列长度，再记录到辅助数组
        for (int i = 0; i < arr.length; i++) {
            // 二分法，把arr[i]放在最后一个小于他的数的下一个。
            // 例如辅助数组end=[0,2,4]，来了个arr[i]=3，那么就应该把3放在4的位置取代4。因为end[2]=4表示长度为2的递增子序列最左结尾是4，现在来了个3，end记录了长度为1的递增子序列最左结尾是2，3在end这些数的右边，那么3自然可以作为长度为2的递增子序列的结尾，3还比4小，自然可以取代4
            int left = 1;
            int right = endRight;
            while (left <= right) {
                int mid = left + ((right - left) >> 1);
                if (end[mid] < arr[i]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            end[left] = arr[i];
            // 更新辅助数组的有效右边界
            endRight = Math.max(endRight, left);
        }
        return endRight;
    }
}
