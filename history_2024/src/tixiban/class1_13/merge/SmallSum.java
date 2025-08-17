package tixiban.class1_13.merge;

import tixiban.class1_13.recurrence.MergeSort;

import java.util.Arrays;

public class SmallSum {

    public static int smallSum(int[] arr) {
        if (null == arr || arr.length < 2) {
            return 0;
        }
        return process(arr, 0, arr.length - 1);
    }

    /**
     * 每个数求小和的过程，是左边内部求小和 + 右边内部求小和 + 左边每个数在右边求小和，没有重复计算
     * @param arr
     * @param left
     * @param right
     * @return
     */
    public static int process(int[] arr, int left, int right) {
        if (left == right) {
            return 0;
        }
        int mid = left + ((right - left) >> 1);

        // 左组每个数求小和 + 右组每个数求小和 + 左组每个数在右组中求小和
        return process(arr, left, mid)
                + process(arr, mid + 1, right)
                + merge(arr, left, mid, right);
    }

    /**
     * merge的过程，是左边每个数在右组中求小和的过程
     * @param arr
     * @param left
     * @param mid
     * @param right
     * @return
     */
    public static int merge(int[] arr, int left, int mid, int right) {
        // 左组第一个位置
        int leftPoint = left;
        // 右组第一个位置
        int rightPoint = mid + 1;

        // 辅助数组
        int[] help = new int[right - left + 1];
        int helpPoint = 0;

        // 结果
        int sum = 0;

        while (leftPoint <= mid && rightPoint <= right) {
            if (arr[leftPoint] < arr[rightPoint]) {
                // 如果左边小，说明右边有比它大的数，求小和
                sum += arr[leftPoint] * (right - rightPoint + 1);
                help[helpPoint++] = arr[leftPoint++];
            } else {
                // 如果左边大，说明右边没有比它大的数，没有小和可求
                // 因为小和的定义是严格小，不是小于等于，所以左边和右边相等时不求小和
                help[helpPoint++] = arr[rightPoint++];
            }
        }

        // 如果右边越界左边没遍历完，说明右边没有比左边大的数了，没有小和可求
        while (leftPoint <= mid) {
            help[helpPoint++] = arr[leftPoint++];
        }

        // 如果左边越界了，说明左边的数都求完小和了，不再求小和
        // 为什么要排序：每次merge时，有序就可以轻松算出右边有几个比左边大的
        while (rightPoint <= right) {
            help[helpPoint++] = arr[rightPoint++];
        }

        // 把辅助数组中排好序的数拷贝到原数组
        for (int i = 0; i < help.length; i++) {
            arr[left + i] = help[i];
        }

        return sum;
    }

    public static void main(String[] args) {
        for (int n = 0; n < 1000000; n++) {

            int[] arr = MergeSort.generateArr(10, 100);

            // 暴力法求小和，两重for循环，O(N^2)
            int sumNormal = 0;
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < i; j++) {
                    if (arr[j] < arr[i]) {
                        sumNormal += arr[j];
                    }
                }
            }

            // merge法求小和
            int sumMerge = smallSum(arr);

            if (sumMerge != sumNormal) {
                System.out.println(Arrays.toString(arr));
                System.out.println("暴力法求小和：" + sumNormal);
                System.out.println("merge法求小和：" + sumMerge);
                break;
            }
        }
    }
}
