package tixiban.class1_13.quicksort;

import tixiban.class1_13.recurrence.MergeSort;

import java.util.Arrays;

/**
 * 快排，递归实现，思路是每次以范围内最后一个数为基准，进行荷兰国旗问题处理，形成小于的在左边，等于的在中间，大于的在右边；
 * 然后等于区已经在它该在的位置了，对小于区和大于区分别递归一次。
 * <p>
 * 时间复杂度是O(N^2)。
 * 最好情况是每次找的基准都恰好是当前范围的中位数，荷兰国旗后小于区和大于区恰好均分，只需递归logN层，这时复杂度是O(N*logN)。
 * 最坏情况是数组初始已经有序，荷兰国旗后分布全部偏向小于区或全部偏向大于区，需要递归N层，这时复杂度是O(N^2)。
 *
 * 空间复杂度是O(N)。
 * 最好情况就是时间复杂度的最好情况，递归logN层，每层申请一个长度为2的数组，空间复杂度为O(logN)。
 * 最坏情况就是时间复杂度的最坏情况，递归N层，每层申请一个长度为2的数组，空间复杂度为O(N)
 */
public class QuickSort {
    public static void quickSort(int[] arr) {
        if (null == arr || arr.length < 2) {
            return;
        }
        process(arr, 0, arr.length - 1);
    }

    public static void process(int[] arr, int left, int right) {
        // 有可能出现left>right的情况，例如当前范围长度为2的时候
        if (left >= right) {
            return;
        }
        // 先进行一次荷兰国旗，把当前数组划分成小于区在左边，等于区在中间，大于区在右边，返回等于区的下标范围
        int[] equalArea = partition(arr, left, right);
        // 对小于区进行递归
        process(arr, left, equalArea[0] - 1);
        // 对大于区进行递归
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
            quickSort(arr2);

            for (int i = 0; i < arr1.length; i++) {
                if (arr1[i] != arr2[i]) {
                    System.out.println("原数组：" + Arrays.toString(arr));
                    System.out.println("系统排序：" + Arrays.toString(arr1));
                    System.out.println("快排：" + Arrays.toString(arr2));
                    break;
                }
            }
        }

    }
}
