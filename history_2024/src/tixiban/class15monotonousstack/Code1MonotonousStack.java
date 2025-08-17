package tixiban.class15monotonousstack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * 实现单调栈
 */
public class Code1MonotonousStack {

    /**
     * 给定一个数组，无重复值，返回每个元素返回两个信息：左边小于他的离他最近的位置、右边小于他的离他最近的位置
     *
     * @param arr
     * @return
     */
    public static int[][] closestLessNoRepeat(int[] arr) {
        // 保存结果，数组每个元素有两个信息：左边小于我的最近的位置、右边小于我的最近的位置，所以结果的大小是n*2
        int[][] result = new int[arr.length][2];
        // 栈，存下标，保证栈内单调性，大的压小的（是下标对应的元素单调，不是下标单调）
        Stack<Integer> stack = new Stack<>();

        // 遍历数组
        for (int i = 0; i < arr.length; i++) {
            // 如果栈顶元素比当前元素大，就弹出栈顶元素结算结果，栈顶元素左边小于他最近的就是下一个栈顶元素，右边小于他最近的就是当前i元素
            while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {
                Integer index = stack.pop();
                result[index][0] = stack.isEmpty() ? -1 : stack.peek();
                result[index][1] = i;
            }
            // 当前元素下标压栈
            stack.push(i);
        }

        // 全部压栈完成后，依次弹出栈顶元素出并结算结果，栈顶元素左边最近的小于他的就是下一个栈顶元素，右边最近的小于他的没有
        while (!stack.isEmpty()) {
            Integer index = stack.pop();
            result[index][0] = stack.isEmpty() ? -1 : stack.peek();
            result[index][1] = -1;
        }

        return result;
    }

    /**
     * 给定一个数组，可能存在重复值，每个元素返回两个信息：左边小于他的离他最近的位置、右边小于他的离他最近的位置
     *
     * @param arr
     * @return
     */
    public static int[][] monotonousStackCanRepeat(int[] arr) {
        int[][] result = new int[arr.length][2];
        // 因为有重复值，所以栈存的是下标列表
        Stack<List<Integer>> stack = new Stack<>();

        for (int i = 0; i < arr.length; i++) {
            // 如果栈顶列表的任一下标对应的元素大于当前元素，就弹出栈顶列表全部结算
            while (!stack.isEmpty() && arr[stack.peek().get(0)] > arr[i]) {
                List<Integer> indexList = stack.pop();
                int leftClosestLess = stack.isEmpty() ? -1 : stack.peek().get(stack.peek().size() - 1);
                for (int index : indexList) {
                    result[index][0] = leftClosestLess;
                    result[index][1] = i;
                }
            }

            // 如果现在的栈顶==当前，就不用push，直接在栈顶列表中add当前下标。否则把当前下标作为一个新列表push
            if (!stack.isEmpty() && arr[stack.peek().get(0)] == arr[i]) {
                stack.peek().add(i);
            } else {
                stack.push(Collections.singletonList(i));
            }
        }

        // 全部push完之后，依次pop清空栈
        while (!stack.isEmpty()) {
            List<Integer> indexList = stack.pop();
            int leftClosestLess = stack.isEmpty() ? -1 : stack.peek().get(stack.peek().size() - 1);
            for (int index : indexList) {
                result[index][0] = leftClosestLess;
                result[index][1] = -1;
            }
        }

        return result;
    }

    public static void main(String[] args) {
        int[] arr = {4, 6, 7, 5, 9, 8, 2, 1};
        int[][] result1 = closestLessNoRepeat(arr);
        for (int[] result : result1) {
            System.out.println(Arrays.toString(result));
        }
        System.out.println("=================");
        int[][] result2 = monotonousStackCanRepeat(arr);
        for (int[] result : result2) {
            System.out.println(Arrays.toString(result));
        }
    }
}
