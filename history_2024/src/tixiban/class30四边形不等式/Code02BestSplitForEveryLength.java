package tixiban.class30四边形不等式;

import java.util.Arrays;

/**
 * 上一道题求了从0到n-1的最佳划分,这道题求0到0、0到1..0到n-1的最佳划分,返回数组result[i]表示0到i的最佳划分中左右较小的累加和.
 *
 * 思路:最佳划分点不回退
 * 假如0到17的最佳划分是0到9为左,10到17为右,那么0到18的最佳划分点只能在9或9右边,不可能在左边.
 * 理论证明(不太严谨):
 * 情况1. 先前0到9是min,10到17是max,那么加上18后10到18依然是max,划分点不可能往左移,因为往左移min越来越小了.
 * 情况2. 先前0到9是max,10到17是min,那么
 *       情况2.1. 加上18之后0到18变成了max,如果划分点再往左移,左边min就会越来越小.
 *       情况2.2. 加上18之后0到18依然是min.既然10到17是min,就意味着9到17是max而且0到8的min比10到17的min更小,所以0到8也比现在的10到18更小,为什么要回退选择更小的0到8,不从更大的10到18开始呢?
 * 实践证明:
 * 直接按照不回退编码,和暴力解对数,每次答案一模一样,证明不回退是对的.
 */
public class Code02BestSplitForEveryLength {
    public static int[] bestSplit(int[] arr) {
        // 前缀和数组
        int[] sum = new int[arr.length];
        sum[0] = arr[0];
        for (int i = 1; i < arr.length; i++) {
            sum[i] = sum[i - 1] + arr[i];
        }

        int[] result = new int[arr.length];
        result[0] = 0;

        // 划分点,左半部分的最后一个位置.0到range-1上的最优划分是左边0到split,右边split+1到range-1
        int split = 0;
        // 为什么range从1开始,因为0到0已经有答案了,最优划分的最小值就是0
        for (int range = 1; range < arr.length; range++) {
            // 不断尝试划分点要不要右移到下一个位置
            while (split + 1 < range) {
                int before = Math.min(sum[split], sum[range] - sum[split]);
                int after = Math.min(sum[split + 1], sum[range] - sum[split + 1]);
                // 如果划分点右移到下一位置,得到的较小累加和更大,就把划分点右移,否则说明再往右移结果就更小了,跳出记录当前范围的结果
                // 为什么是大于等于? 例如数组[5,0,0,0,5,9],最佳划分点是第二个5,但是split右移到第二个5之前,after一直==before
                if (after >= before) {
                    split++;
                } else {
                    break;
                }
            }
            result[range] = Math.min(sum[split], sum[range] - sum[split]);
        }
        return result;
    }

    /**
     * 抽象化
     * 每个位置上的答案result = max{min{左累加和,右累加和}, min{左累加和,右累加和}, ...}
     * 那么答案不回退.
     *
     * 再抽象化
     * 每个位置上的答案result = 最优{最差{左指标,右指标}, 最差{左指标,右指标}, ...}
     * 或者最差{最优{},最优{},...}
     * 并且指标在区间上单调,
     * 那么很可能都答案不回退.
     *
     * 不需要严格证明,直接暴力解对数.
     */

    public static void main(String[] args) {
        int[] arr = {5, 0, 0, 0, 5, 9};
        System.out.println(Arrays.toString(bestSplit(arr)));

        int[] arr1 = {1, 7, 3, 0, 6, 0, 3, 8, 4, 5, 2};
        System.out.println(Arrays.toString(bestSplit(arr1)));
    }

}
