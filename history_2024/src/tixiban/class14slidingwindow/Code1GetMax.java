package tixiban.class14slidingwindow;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * 一个固定大小为W的窗口，依次划过arr，返回这个窗口每次滑动时当下的最大值
 * 例如，arr=[4,3,5,4,3,3,6,7]，W=3，返回[5,5,5,4,6,7]
 */
public class Code1GetMax {
    public static int[] getMax(int[] arr, int windowSize) {
        if (arr == null || arr.length < windowSize || windowSize < 1) {
            return null;
        }

        // 双端队列用于每次窗口滑动时存最大值的下标
        LinkedList<Integer> queue = new LinkedList<>();

        // 存返回结果
        int[] result = new int[arr.length - windowSize + 1];
        int resultIndex = 0;

        for (int r = 0; r < arr.length; r++) {
            // 1、滑进窗口，下标加到双端队列
            // 加之前把队尾所有<=当前值的元素删除。因为只要有当前元素在，这些元素永远不会成为最大值，并且会先于当前元素滑出
            while (!queue.isEmpty() && arr[queue.peekLast()] <= arr[r]) {
                queue.pollLast();
            }
            queue.addLast(r);

            // 2、滑出窗口，下标从双端队列里删除
            if (queue.peekFirst() == r - windowSize) {
                queue.pollFirst();
            }

            // 3、统计结果
            // 只有当窗口右边界==windowSize - 1时，窗口才成型，在此之前窗口还没有成型，不用统计结果
            if (r >= windowSize - 1) {
                result[resultIndex++] = arr[queue.peekFirst()];
            }
        }
        return result;
    }

    public static void main(String[] args) {
        int[] arr = {4, 3, 5, 4, 3, 3, 6, 7};
        int W = 3;
        System.out.println(Arrays.toString(getMax(arr, W)));
    }
}
