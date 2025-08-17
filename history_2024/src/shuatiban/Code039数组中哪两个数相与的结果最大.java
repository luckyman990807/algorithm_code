package shuatiban;

/**
 * 给定一个非负数组成的数组，长度一定大于1，想知道数组中哪两个数&的结果最大，返回这个最大结果。要求时间复杂度O(N)，额外空间复杂度O(1)
 */
public class Code039数组中哪两个数相与的结果最大 {
    public static int maxAnd(int[] arr) {
        int answer = 0;
        // left到right-1之间的位置是有效区,right后面是无效区
        int right = arr.length;
        for (int bit = 30; bit >= 0; bit--) {
            int left = 0;
            // 记录一下本轮的原始right
            int temp = right;
            // 遍历有效区的数,如果第bit位不是1,就移到无效区,丢弃
            while (left < right) {
                if (((arr[left] >> bit) & 1) == 0) {
                    // 交换时会把有效区最后一个没遍历过的数交换到left位置,所以left不需要++
                    swap(arr, left, --right);
                } else {
                    left++;
                }
            }
            // 如果只有两个数的当前bit位=1,那最佳答案就是这俩相与
            if (right == 2) {
                return arr[0] & arr[1];
            }

            if (right < 2) {
                // 如果只有一个数的当前bit位=1,那么说明答案的当前bit位一定是0.
                // answer |= (0 << bit);
                // 也不需要丢弃当前bit=0的数,因为丢弃的话剩下的数不够两个没法相与了.直接让right回到本轮循环之前的初始值
                right = temp;
            } else {
                // 如果有>2个数的当前bit位=1,那么丢弃当前bit=0的数,在剩下的数中开始下一轮筛选
                // 另外,如果有>2个数的当前bit位=1,那么答案的当前bit必然=1
                answer |= (1 << bit);
            }
        }
        return answer;
    }

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4};
        System.out.println(maxAnd(arr));
    }
}
