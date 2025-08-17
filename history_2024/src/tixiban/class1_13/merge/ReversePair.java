package tixiban.class1_13.merge;

import tixiban.class1_13.recurrence.MergeSort;

import java.util.Arrays;

public class ReversePair {

    public static int reservePair(int[] arr) {
        if (null == arr || arr.length < 2) {
            return 0;
        }
        return process(arr, 0, arr.length - 1);
    }

    public static int process(int[] arr, int left, int right) {
        if (left == right) {
            return 0;
        }
        int mid = left + ((right - left) >> 1);
        // 左边内部找逆序对 + 右边内部找逆序对 + 左边每个数在右边找逆序对，没有重复计算
        return process(arr, left, mid)
                + process(arr, mid + 1, right)
                + merge(arr, left, mid, right);
    }

    public static int merge(int[] arr, int left, int mid, int right) {
        int leftPoint = left;
        int rightPoint = mid + 1;

        int[] help = new int[right - left + 1];
        int helpPoint = 0;

        // 逆序对个数
        int count = 0;

        while (leftPoint <= mid && rightPoint <= right) {
            // 按照倒序排序，大的先放
            if (arr[leftPoint] > arr[rightPoint]) {
                // 如果左边大，说明右边再往右全是比左边小的数
                count += right - rightPoint + 1;
                help[helpPoint++] = arr[leftPoint++];
            } else {
                // 如果右边大，说明再往右也没有比左边小的了
                // 因为逆序对的定义是严格小，不是小于等于，所以左边等于右边时不是逆序对
                help[helpPoint++] = arr[rightPoint++];
            }
        }

        // 如果右边越界左边没遍历完，说明右边已经没有比左边小的了，也就没有逆序对
        while (leftPoint <= mid) {
            help[helpPoint++] = arr[leftPoint++];
        }

        // 如果左边越界，说明左边所有数都求完了逆序对
        while (rightPoint <= right) {
            help[helpPoint++] = arr[rightPoint++];
        }

        // 把排好序的数组拷贝到原数组
        for (int i = 0; i < help.length; i++) {
            arr[left + i] = help[i];
        }

        return count;
    }

    public static void main(String[] args) {
        for (int n = 0; n < 1000000; n++) {
            int[] arr = MergeSort.generateArr(10, 100);

            int countNormal = 0;
            for (int i = 0; i < arr.length; i++) {
                for (int j = i + 1; j < arr.length; j++) {
                    if (arr[j] < arr[i]) {
                        countNormal++;
                    }
                }
            }

            int countMerge = reservePair(arr);

            if (countMerge != countNormal) {
                System.out.println(Arrays.toString(arr));
                System.out.println("暴力法求逆序对个数：" + countNormal);
                System.out.println("merge法求逆序对个数：" + countMerge);
                break;
            }
        }
    }
}
