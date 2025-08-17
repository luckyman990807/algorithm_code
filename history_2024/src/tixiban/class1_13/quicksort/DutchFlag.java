package tixiban.class1_13.quicksort;

/**
 * 荷兰国旗问题
 * 题目：给定一个数（比如数组最后一个元素），把数组中小于等于这个数的元素整体放在左边，大于这个数的元素整体放在右边，要求时间复杂度O(N)。
 * 或者另一种题目：小于等于的放左边，大于的放右边，左边最后一个数是等于的。
 * 或者另一种题目：小于这个数的放左边，大于这个数的放中间，等于这个数的放中间。
 */
public class DutchFlag {
    /**
     * 以给定范围的最后一个元素为基准，大于等于这个数的放左边，小于的放右边，且左边最后一个数是等于的。返回左边最后一个下标。
     * <p>
     * 解法：对数组划分一个小于等于区的边界，初始边界在第一个数左边，遍历数组如果小于等于目标，就划到小于等于区中，指针+1，否则直接指针+1。
     * 怎么划到小于等于区：当前数和小于等于区下一个数交换，并把小于等于边界+1右扩一位
     * 算法执行过程中数组的分布是：[小于等于区][大于区]当前数[还没遍历到]。
     */
    public static int dutchFlag2p(int[] arr, int left, int right) {
        if (left > right) {
            return -1;
        }
        if (left == right) {
            return left;
        }

        // 小于等于区的右边界
        int lessEqual = left - 1;
        // 当前数
        int cur = left;

        while (cur < right) {
            if (arr[cur] <= arr[right]) {
                // 如果当前数小于等于目标值，就划到小于等于区（当前数和小于等于区下一个做交换，小于等于区右扩一位），当前下标+1比较下一个
                swap(arr, cur++, ++lessEqual);
            } else {
                // 如果当前数大于目标值，就不动，留在大于区，当前下标+1比较下一个
                cur++;
            }
        }
        // 最后一个数（等于目标）和小于等于区的下一个（也是大于区的第一个）交换，保证小于等于区最后一个是等于的
        swap(arr, right, ++lessEqual);
        return lessEqual;
    }

    /**
     * 以给定范围的最后一个元素为基准，小于的放左边，等于的放中间，大于的放右边，以数组的形式返回等于区域的第一个和最后一个下标
     * <p>
     * 解法：对数组划分一个小于区边界,初始为left-1，再划分一个大于区边界，初始为right。
     * right位置是基准，直接划到大于区，不参与判断，算法最后会交换到正确的位置。
     * 遍历数组如果小于基准，就划到小于区，指针+1；如果大于基准，就划到大于区，指针不动（因为换过来的是没判断过的数）；如果等于基准直接指针+1
     * 怎么划分到小于区：当前数和小于区下一个数交换，小于区边界+1右扩一位
     * 怎么划分到大于区：当前数和大于区前一个数交换，大于区边界-1左扩一位
     * 算法执行过程中数组的分布是：[小于区][等于区]当前数[还没遍历到][大于区]
     */
    public static int[] dutchFlag3p(int[] arr, int left, int right) {
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
        // 把基准和大于区第一个交换后，more不再是大于区的第一个数，而是等于区的最后一个数，也是大于区的前一个数
        return new int[]{less + 1, more};
    }


    public static void swap(int[] arr, int a, int b) {
        if (a == b) {
            return;
        }
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }
}
