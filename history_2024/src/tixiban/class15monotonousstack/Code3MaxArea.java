package tixiban.class15monotonousstack;

import java.util.Stack;

/**
 * 给定一个非负数组arr代表直方图，返回直方图中最大矩形面积
 * <p>
 * 思路：
 * 遍历每个直方，计算以当前直方作为高度的矩形的面积，求所有面积的最大值
 * 矩形的长怎么求？单调栈，找到左边小于的最近的，找到右边小于的最近的，中间的长度就是矩形的长。当前直方的高就是矩形的高。
 */
public class Code3MaxArea {
    public static int maxArea(int[] arr) {
        int max = 0;
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < arr.length; i++) {
            while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
                Integer index = stack.pop();
                max = Math.max(max, (stack.isEmpty() ? i : i - stack.peek() - 1) * arr[index]);
            }
            stack.push(i);
        }

        while (!stack.isEmpty()) {
            Integer index = stack.pop();
            max = Math.max(max, (stack.isEmpty() ? arr.length : arr.length - stack.peek() - 1) * arr[index]);
        }

        return max;
    }

    public static void main(String[] args) {
        int[] arr = {2, 4, 6, 5, 8, 7, 3};
        System.out.println(maxArea(arr));
    }
}
