package tixiban.class1_13.bucketsort;

/**
 * 桶排序是一种思想，桶就是容器，用一些容器来给数据分类，帮助进行排序。
 * 桶排序是基于分类的排序，不是基于比较的排序。
 * 桶排序只适合用于数据范围相对较小的场景，理由很简单，假如100个数据，取值范围是任意整数，那么按照int的范围需要准备40亿个桶，显然是不合适的。
 * <p>
 * 计数排序通过记录每个元素出现的个数，再把每个元素按照个数输出，达到排序的效果。
 * 计数排序是桶排序的一种，记录某个元素出现的次数，就相当于有个无形的桶把这些元素装在一起。
 * <p>
 * 计数排序时间复杂度O(N)，但是限制太多，对数据状况要求太多。
 * <p>
 * 计数排序额外空间复杂度是O(Max)，因为最大值多大就要申请多大的辅助数组。
 */
public class countSort {
    // 假设这个题目要求的数据都是大于零的整数，且数据范围较集中
    public static void countSort(int[] arr) {
        if (null == arr || arr.length < 2) {
            return;
        }

        // 找出数组元素的最大值
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            max = Math.max(max, arr[i]);
        }
        // 因为用的是桶数组的下标，要创建最大下标是max的数组，那么长度就是max+1，因为下标从0开始
        int[] bucket = new int[max + 1];
        for (int i = 0; i < arr.length; i++) {
            bucket[arr[i]]++;
        }

        // bucket的下标就是原arr的元素，bucket[i]等于几，就说明原数组里有几个i
        int index = 0;
        for (int i = 0; i < bucket.length; i++) {
            while (bucket[i]-- > 0) {
                arr[index] = i;
            }
        }
    }
}
