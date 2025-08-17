package tixiban.class1_13.recurrence;

import java.util.Arrays;

public class MergeSort {
    public static void process(int[] arr, int left, int right) {
        // 递归出口：当只有一个元素时，已经有序了，直接返回
        if (left == right) {
            return;
        }
        // 拆分成等规模的两个子问题
        int mid = left + ((right - left) >> 1);
        process(arr, left, mid);
        process(arr, mid + 1, right);
        // 根据子问题的解整合出原问题的解
        merge(arr, left, mid, right);
    }

    public static void merge(int[] arr, int left, int mid, int right) {
        // 申请一个和当前操作范围等长的辅助数组，并定义一个辅助指针
        int[] help = new int[right - left + 1];
        int helpPoint = 0;

        // arr左半部分的指针
        int leftPoint = left;
        // arr右半部分的指针
        int rightPoint = mid + 1;

        // 左数组和右数组哪边元素小取就取哪个，直到左右有一方越界
        while (leftPoint <= mid && rightPoint <= right) {
            help[helpPoint++] = arr[leftPoint] < arr[rightPoint] ? arr[leftPoint++] : arr[rightPoint++];
        }

        // 如果是右边越界了，就把左边剩下的通通放到辅助数组
        while (leftPoint <= mid) {
            help[helpPoint++] = arr[leftPoint++];
        }

        // 要么就是左边越界了，把右边剩下的通通放到辅助数组
        while (rightPoint <= right) {
            help[helpPoint++] = arr[rightPoint++];
        }

        // 把辅助数组拷贝到原数组
        for (int i = 0; i < help.length; i++) {
            arr[left + i] = help[i];
        }
    }

    public static void sort(int[] arr) {
        // 步长，每次merge的左右两组长度均为mergeSize
        int mergeSize = 1;
        while (true) {
            // 每次merge时左组的第一个位置
            int left = 0;
            while (true) {
                // 每次merge时左组最后一个位置
                int mid = left + mergeSize - 1;
                // 如果左组已经覆盖了整个数组，说明上一轮已经把数组左半部分和右半部分merge过了，直接退出
                if (mid >= arr.length - 1) {
                    break;
                }
                // 如果剩下的凑不够右组，那右组有几个算几个
                int right = Math.min(mid + mergeSize, arr.length - 1);
                // 左右组归并
                merge(arr, left, mid, right);
                // 下一次merge左组的第一个位置
                left = right + 1;
            }
            // 步长每次*2，如果步长已经覆盖了整个数组，说明这一轮已经把数组左半和右半部分merge过了，直接退出
            // 为什么不mergeSize * 2 > arr.length？为了避免整数mergeSize溢出
            if (mergeSize > arr.length >> 1) {
                break;
            }
            mergeSize <<= 1;
        }
    }

    public static int[] generateArr(int maxLength, int maxValue) {
        if (maxLength <= 0) {
            return new int[0];
        }
        int length = (int) (Math.random() * (maxLength + 1));
        int[] arr = new int[length];
        for (int i = 0; i < length; i++) {
            arr[i] = (int) (Math.random() * (maxValue + 1));
        }
        return arr;
    }

    public static void main(String[] args) {

        for (int j = 0;j<1000000;j++){
            int[] arr = generateArr(10, 100);
            System.out.println(Arrays.toString(arr));
            int[] arrCp = new int[arr.length];
            System.arraycopy(arr, 0, arrCp, 0, arr.length);

            sort(arr);
            Arrays.sort(arrCp);

            System.out.println(Arrays.toString(arr));
            System.out.println(Arrays.toString(arrCp));
            for (int i=0;i<arr.length;i++){
                if(arr[i] != arrCp[i]){
                    System.out.println("排序失败");
                    System.out.println();
                    break;
                }
            }
        }
    }
}
