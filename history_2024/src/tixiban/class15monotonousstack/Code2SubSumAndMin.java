package tixiban.class15monotonousstack;

import java.util.Stack;

/**
 * 给定一个只包含正数的数组arr，arr中任意一个子数组sub一定都可以计算一个指标：sub累加和 * sub中的最小值，
 * 求所有子数组中指标最大是多少
 * <p>
 * 思路：
 * 遍历数组，针对每个元素，找到以这个元素作为最小值的范围最大的子数组，累加和*当前元素计算指标，求每个元素的指标的最大值。
 * 怎么找到这个元素作为最小值的范围最大的数组？单调栈。找到左边最近的小于他的，右边最近的小于他的，中间的就是要求的子数组。
 * 怎么快速计算累加和？前缀和数组。
 */
public class Code2SubSumAndMin {
    public static int subSumAndMin(int[] arr) {
        // 计算前缀和数组
        int[] sum = new int[arr.length];
        sum[0] = arr[0];
        for (int i = 1; i < arr.length; i++) {
            sum[i] = sum[i - 1] + arr[i];
        }

        // 存返回结果
        int max = 0;

        // 栈，存下标，保持单调性，栈内下标对应元素大的在顶，小的在底
        Stack<Integer> stack = new Stack<>();

        // 遍历数组压栈
        for (int i = 0; i < arr.length; i++) {
            // 只要栈顶大于或等于当前，就弹出栈顶结算
            // 注意：栈顶==当前的时候，栈顶结算肯定是错的，因为栈顶右边小于他的最近的还没遍历到。
            // 但是无所谓，因为相等的栈顶和当前，是共享小于位置的，也就是说等到当前元素遇到真正的右小弹栈的时候自然会有正确的右小位置。而左小位置已经在栈中了。
            // 总结，如果有重复值a，如果是因为==条件弹出栈顶的a，必然会算错，直到因为>条件弹出栈顶的a，才会算对。只要有一个算对的就行，他和在此之前的a是共享结果的。
            // 前面的a扩展不到的右边界，后边的总有一个a能扩展到。
            while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
                Integer index = stack.pop();
                // i就是右边到不了的位置，stack.peek()就是左边到不了的位置，求前缀和
                int subSum = stack.isEmpty() ? sum[i - 1] : sum[i - 1] - sum[stack.peek()];
                // 计算指标
                int result = subSum * arr[index];
                max = Math.max(max, result);
            }

            stack.push(i);
        }

        while (!stack.isEmpty()) {
            Integer index = stack.pop();
            int subSum = stack.isEmpty() ? sum[sum.length - 1] : sum[sum.length - 1] - sum[stack.peek()];
            int result = subSum * arr[index];
            max = Math.max(max, result);
        }

        return max;
    }

    public static void main(String[] args) {
        int[] arr = {2, 4, 6, 4, 3, 4, 7};
        System.out.println(subSumAndMin(arr));
    }

}
