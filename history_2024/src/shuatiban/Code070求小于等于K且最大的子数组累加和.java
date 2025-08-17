package shuatiban;

import java.util.TreeSet;

/**
 * 思路：
 * 看到子数组，就想以xx结尾的子数组小于等于K且最大的累加和是多少，所有位置的答案求最大值
 * 问：如果0到i位置的累加和（前缀和）=100，那么怎么求以i结尾的子数组小于等于30的最大累加和？
 * 答：找i左边哪个位置的前缀和大于等于100-30=70，假设是j，那么从j+1到i的累加和（i前缀和-j前缀和）就是当前位置的答案
 */
public class Code070求小于等于K且最大的子数组累加和 {
    public static int maxSubSum(int[] arr, int k) {
        int max = Integer.MIN_VALUE;
        // 记录当前位置前面的前缀和，并且有序
        TreeSet<Integer> set = new TreeSet<>();
        /** 这里很关键，一个数都没有的时候，就已经有0这个累加和了 */
        set.add(0);
        // 记录累加和
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            // 更新累加和
            sum += arr[i];
            // 求以i结尾小于等于k最大的累加和，就是先求i前面大于等于sum-k最小的前缀和，然后sum-这个前缀和就是当前i位置的答案。
            // 如果没有大于等于sum-k的前缀和，就说明i位置没有小于等于k的累加和，就不更新最终答案
            Integer preSum = set.ceiling(sum - k);
            if (preSum != null) {
                max = Math.max(max, sum - preSum);
            }
            // 当前累加和作为i位置的前缀和加入set
            set.add(sum);
        }
        return max;
    }

    public static void main(String[] args) {
        int[] arr = {1, -2, 4, 5, -3, 6, 9, -11};
        System.out.println(maxSubSum(arr, 10));
    }
}
