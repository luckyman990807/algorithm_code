package shuatiban;

/**
 * 给定一个数组arr,只能对arr中的一个子数组排序,但是想排完序后让arr整体有序,问满足这一设定的子数组最短是多少.
 *
 * 最长的就是整个arr数组自身了.
 *
 * 思路:找出需要交换的最右的位置,找出需要交换的最左的位置,二者之间的就是答案.
 */
public class Code009最短排序子数组 {
    public static int minSubArrayForSort(int[] arr) {
        // 最大值所在的下标
        int max = 0;
        // 最右的需要交换的位置
        int right = 0;

        for (int i = 1; i < arr.length; i++) {
            // 如果有序的话,arr[i]应该大于等于前面的最大值
            if (arr[max] > arr[i]) {
                // arr[i]小于前面的最大值,说明arr[i]不该在i位置上,需要交换,记录需要交换的最右的位置,再往右就不需要交换了,已经在自己位置上了
                right = i;
            } else {
                // arr[i]大于等于前面的最大值,说明i有序,同时i之前的最大值变成arr[i]了
                max = i;
            }
        }

        // 最小值所在的下标
        int min = arr.length - 1;
        // 最左的需要交换的位置
        int left = arr.length - 1;
        for (int i = arr.length - 2; i >= 0; i--) {
            if (arr[min] < arr[i]) {
                left = i;
            } else {
                min = i;
            }
        }

        return right - left + 1;
    }

    public static void main(String[] args) {
        int[] arr = {1, 9, 5, 4, 6, 7, 8};
        System.out.println(minSubArrayForSort(arr));
    }
}
