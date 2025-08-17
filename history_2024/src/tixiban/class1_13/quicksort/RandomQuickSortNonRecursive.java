package tixiban.class1_13.quicksort;

import tixiban.class1_13.recurrence.MergeSort;

import java.util.Arrays;
import java.util.Stack;

/**
 * 随机快排非递归实现，思路是用自己的栈代替系统栈，递归动作变成了往自己栈里压任务
 */
public class RandomQuickSortNonRecursive {
    // 自定义类，表示一个排序任务，记录了要排序的范围
    public static class ProcessArea {
        int left;
        int right;

        public ProcessArea(int left, int right) {
            this.left = left;
            this.right = right;
        }
    }

    public static void randomQuickSortNonRecursive(int[] arr) {
        if (null == arr || arr.length < 2) {
            return;
        }

        Stack<ProcessArea> stack = new Stack<>();
        // 初始状态先往栈里压一个从0到length-1的排序任务，以开启循环
        stack.push(new ProcessArea(0, arr.length - 1));

        // 只要栈里有任务，就循环
        while (!stack.empty()) {
            // 弹栈，拿到本次排序的范围
            ProcessArea processArea = stack.pop();
            int left = processArea.left;
            int right = processArea.right;
            // 相当于递归出口
            if (left >= right) {
                continue;
            }
            // 随机找基准
            swap(arr, left + (int) (Math.random() * (right - left + 1)), right);
            // 先整体荷兰国旗一下
            int[] equalArea = partition(arr, left, right);
            // 把小于区作为一个新任务压入栈中
            stack.push(new ProcessArea(left, equalArea[0] - 1));
            // 把大于区作为一个新任务压入栈中
            stack.push(new ProcessArea(equalArea[1] + 1, right));
        }
    }

    public static int[] partition(int[] arr, int left, int right) {
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
        return new int[]{less + 1, more};
    }

    public static void swap(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    public static void main(String[] args) {
        for (int n = 0; n < 1000000; n++) {
            int[] arr = MergeSort.generateArr(20, 100);
            int[] arr1 = new int[arr.length];
            System.arraycopy(arr, 0, arr1, 0, arr.length);
            int[] arr2 = new int[arr.length];
            System.arraycopy(arr, 0, arr2, 0, arr.length);

            Arrays.sort(arr1);
            randomQuickSortNonRecursive(arr2);

            for (int i = 0; i < arr1.length; i++) {
                if (arr1[i] != arr2[i]) {
                    System.out.println("原始数组：" + Arrays.toString(arr));
                    System.out.println("系统排序：" + Arrays.toString(arr1));
                    System.out.println("随机快排：" + Arrays.toString(arr2));
                    break;
                }
            }
        }
    }
}
