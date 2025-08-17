package tixiban.class30四边形不等式;

/**
 * 给定一个数组,求一个把数组分成左右两份的方案,使得两份分别的累加和中的最小值最大,返回这个最小值.
 *
 * 思路:
 * 先便利一遍求出所有累加和sum,再遍历记录左边累加和leftSum,那么当前右边累加和就是rightSum,记录二者中的最小值.返回所有最小值中最大的那个.
 */
public class Code01BestSplitForAll {
    public static int bestSplit(int[] arr) {
        if (arr == null || arr.length < 2) {
            // 如果只有一个数,要么左部分是0要么有部分是0,最小值必然是0
            return 0;
        }

        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }

        int max = 0;
        int leftSum = 0;
        int rightSum = 0;
        for (int i = 0; i < arr.length; i++) {
            leftSum += sum;
            rightSum = sum - leftSum;
            max = Math.max(max, Math.min(leftSum, rightSum));
        }

        return max;
    }
}
