package tixiban.class1_13.merge;

import tixiban.class1_13.recurrence.MergeSort;

import java.util.Arrays;

public class BiggerThanTwice {
    public static int biggerThanTwice(int[] arr) {
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
        return process(arr, left, mid)
                + process(arr, mid + 1, right)
                + merge(arr, left, mid, right);
    }

    public static int merge(int[] arr, int left, int mid, int right) {
        int leftPoint = left;
        int rightPoint = mid + 1;

        // 先算个数再merge，因为这道题，算个数和merge对左右指针的移动有不同的要求：算个数要求右指针一直右移到左边不大于两倍右边停下，然后左指针右移；而merge要求左右两边谁小谁右移
        int count = 0;
        while (leftPoint <= mid) {
            // 相当于左边的数轮流把符合要求的窗口往右推
            while (rightPoint <= right && arr[leftPoint] > (arr[rightPoint] << 1)) {
                rightPoint++;
            }
            // 推到推不动了，计算当前左数有几个大于两倍的右数
            count += rightPoint - (mid + 1);

            // 下一个右数继续推
            leftPoint++;
        }

        leftPoint = left;
        rightPoint = mid + 1;

        int[] help = new int[right - left + 1];
        int helpPoint = 0;

        while (leftPoint <= mid && rightPoint <= right) {
            help[helpPoint++] = arr[leftPoint] < arr[rightPoint] ? arr[leftPoint++] : arr[rightPoint++];
        }

        while (leftPoint <= mid) {
            help[helpPoint++] = arr[leftPoint++];
        }

        while (rightPoint <= right) {
            help[helpPoint++] = arr[rightPoint++];
        }

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
                for (int j = i; j < arr.length; j++) {
                    if (arr[i] > arr[j] * 2) {
                        countNormal++;
                    }
                }
            }

            int countMerge = biggerThanTwice(arr);

            if (countMerge != countNormal) {
                System.out.println(Arrays.toString(arr));
                System.out.println("暴力法结果：" + countNormal);
                System.out.println("merge法结果：" + countMerge);
            }
        }
    }

}
