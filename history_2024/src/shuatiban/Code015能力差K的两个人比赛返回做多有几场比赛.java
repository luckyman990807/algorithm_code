package shuatiban;

import java.util.Arrays;

/**
 * 给定一个数组arr，代表每个人的能力值。再给定一个非负数k，如果两个人能力差值正好为k，那么可以凑在一起比赛
 * 一局比赛只有两个人，返回最多可以同时有多少场比赛
 *
 * 思路:排序后滑动窗口
 * 左边界和右边界,差值小则右边滑,差值大则左边滑,相等则比赛并左右都滑
 */
public class Code015能力差K的两个人比赛返回做多有几场比赛 {
    public static int maxMatches(int[] arr, int k) {
        Arrays.sort(arr);
        int left = 0;
        int right = 0;
        int result = 0;
        boolean[] used = new boolean[arr.length];
        while (left < arr.length && right < arr.length) {
            // 如果这个数已经比赛过了,就跳过.
            // right不会碰到已经用过的数,只有right用过的数被left碰到
            if (used[left]) {
                left++;
            }

            // 如果左边界追上右边界了,右边界滑动
            if (left >= right) {
                right++;
            }

            if (arr[right] - arr[left] < k) {
                // 左右差值小于k,右边滑
                right++;
            } else if (arr[right] - arr[left] > k) {
                // 左右差值大于k,左边滑
                left++;
            } else {
                // 左右差值等于k,可以比赛
                result++;
                // 右数标记已使用,避免left再用来比赛
                used[right]=true;
                // 左右都用过了,都滑
                left++;
                right++;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        int[] arr = {3, 5, 1, 7};
        System.out.println(maxMatches(arr, 2));
    }
}
