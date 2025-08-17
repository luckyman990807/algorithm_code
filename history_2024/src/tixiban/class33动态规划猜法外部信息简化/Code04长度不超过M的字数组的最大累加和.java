package tixiban.class33动态规划猜法外部信息简化;

import java.util.LinkedList;

/**
 * 给定一个数组arr,和一个整数M
 * 返回arr的子数组在长度不超过M的情况下,子数组累加和的最大值
 *
 * 思路:滑动窗口更新最大值
 */
public class Code04长度不超过M的字数组的最大累加和 {
    public static int maxSum(int[] arr, int maxLength) {
        if (arr == null || arr.length == 0) {
            return 0;
        }

        // 前缀和数组
        int[] sum = new int[arr.length];
        sum[0] = arr[0];
        for (int i = 1; i < sum.length; i++) {
            sum[i] = sum[i - 1] + arr[i];
        }

        // 结果
        int max = 0;

        // 最大值队列,保存当前划过的位置的前缀和的最大值
        LinkedList<Integer> maxQueue = new LinkedList();
        // 滑动窗口左边界
        int left = 0;
        // 滑动窗口右边界
        int right = 0;

        // 1.滑动窗口逐渐形成的过程,只需要右边界右移,同时窗口右端试图进最大值队列,进去要先把最大值队列里小于自己的都挤出来,始终让最大值队列保持从大到小的顺序
        for (; right < Math.min(maxLength, arr.length); right++) {
            while (!maxQueue.isEmpty() && sum[maxQueue.peekLast()] <= sum[right]) {
                maxQueue.pollLast();
            }
            maxQueue.push(right);
            System.out.println("左边界:" + arr[left] + "右边界:" + arr[right] + "最大累加和:" + sum[maxQueue.peekFirst()]);
        }
        // 现在sum[maxQueue.peekFirst()]就是以0开头的长度不超过M的最大累加和.
        max = Math.max(max, sum[maxQueue.peekFirst()]);

        // 2.滑动窗口常规滑动的过程,每次左边界和右边界都右移,同时先窗口左端出队列,在窗口右端进队列
        for (; right < arr.length; left++, right++) {
            // 窗口左端的数,弹出最大值队列.最左端的数如果在最大值队列里面,那么一定是最大的,因为如果不是最大的就会在后面数进队列的时候被挤出去.
            if (maxQueue.peekFirst() == left) {
                maxQueue.pollFirst();
            }
            // 窗口右端进队列之前,先把比右端这个数小的都挤出去
            while (!maxQueue.isEmpty() && sum[maxQueue.peekLast()] <= sum[right]) {
                maxQueue.pollLast();
            }
            // 窗口右端进队列
            maxQueue.push(right);
            // 每滑动一次就选一次最大值,现在sum[maxQueue.peekFirst()]就是以left+1开头的长度不超过M的最大累加和
            max = Math.max(max, sum[maxQueue.peekFirst()] - sum[left]);
            System.out.println("左边界:" + arr[left + 1] + "右边界:" + arr[right] + "最大累加和:" + max);
        }

        // 3.滑动窗口逐渐消亡的过程,只需要左边界右移,同时窗口左端出队列
        for (; left < arr.length - 1; left++) {
            if (maxQueue.peekFirst() == left) {
                maxQueue.pollFirst();
            }
            // 每滑动一次就选一次最大值,因为最大值在滑动过程中可能会出队列
            max = Math.max(max, sum[maxQueue.peekFirst()] - sum[left]);
            System.out.println("左边界:" + arr[left + 1] + "右边界:" + arr[right - 1] + "最大累加和:" + max);
        }

        return max;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, -1, -3, 5, 9};
        int m = 3;
        System.out.println(maxSum(arr, 3));
    }
}
