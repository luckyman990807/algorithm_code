package tixiban.class1_13.quicksort;

import tixiban.class1_13.recurrence.MergeSort;

import java.util.Arrays;

/**
 * 随机快排，和快排的唯一区别就是多了随机，快排每次固定以最后一个数为基准（或者固定以其他位置的数为基准），遇到最差情况就100% O(N^2)；
 * 而随机快排每次随机选取一个位置作为基准，恰好每次选到中位数则O(NlogN)，恰好每次选到最大或最小值则O(N^2)，在二者之间等概率分布。
 * <p>
 * 通过概率计算，随机快排的时间复杂度的期望值收敛到O(NlogN)。
 * <p>
 * 同理，随即快排的空间复杂度的期望值收敛到O(logN)。
 */
public class RandomQuickSort {
    public static void randomQuickSort(int[] arr) {
        if (null == arr || arr.length < 2) {
            return;
        }
        process(arr, 0, arr.length - 1);
    }

    public static void process(int[] arr, int left, int right) {
        if (left >= right) {
            return;
        }

        // 随机快排对比快排，就特殊在基准是随机找的，不是固定找某一个
        // 在left到right上随机找一个位置，交换到right位置作为基准
        swap(arr, left + (int) (Math.random() * (right - left + 1)), right);
        // 先整体来一个荷兰国旗，把当前数组分成小于区、等于区和大于区，其中等于区已经在该在的位置上了
        int[] equalArea = partition(arr, left, right);
        // 对小于区递归
        process(arr, left, equalArea[0] - 1);
        // 对大于区递归
        process(arr, equalArea[1] + 1, right);
    }

    // 处理荷兰国旗问题
    public static int[] partition(int[] arr, int left, int right) {
        if (left > right) {
            return new int[]{-1, -1};
        }
        if (left == right) {
            return new int[]{left, left};
        }

        int less = left - 1;
        int more = right;
        int cur = left;

        while (cur < more) {
            if (arr[cur] < arr[right]) {
                swap(arr, cur++, ++less);
            } else if (arr[cur] > arr[right]) {
                swap(arr, cur, --more);
            } else {
                cur++;
            }
        }

        swap(arr, right, more);
        return new int[]{less + 1, more};
    }

    public static void swap(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    public static void main(String[] args) {
        for (int n = 0; n < 1000000; n++) {
            int[] arr = MergeSort.generateArr(20, 100);
            int[] arr1 = new int[arr.length];
            System.arraycopy(arr, 0, arr1, 0, arr.length);
            int[] arr2 = new int[arr.length];
            System.arraycopy(arr, 0, arr2, 0, arr.length);

            Arrays.sort(arr1);
            randomQuickSort(arr2);

            for (int i = 0; i < arr1.length; i++) {
                if (arr1[i] != arr2[i]) {
                    System.out.println("原始数组：" + Arrays.toString(arr));
                    System.out.println("系统排序：" + Arrays.toString(arr1));
                    System.out.println("随机快排：" + Arrays.toString(arr2));
                    break;
                }
            }
        }
    }
}
