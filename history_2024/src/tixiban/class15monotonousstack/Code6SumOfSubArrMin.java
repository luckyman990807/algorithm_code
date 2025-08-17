package tixiban.class15monotonousstack;

import java.util.Stack;

/**
 * 给定一个数组arr，返回所有子数组最小值的累加和
 *
 * 思路：
 * 不是求出每个子数组然后再求最小值再累加，
 * 而是遍历数组，求以当前元素作为最小值的子数组有几个，然后乘以当前元素，每个元素都这么算然后累加
 * 怎么计算以当前元素作为最小值的子数组有几个？找到当前元素左边最近的小于的位置，找到当前元素右边最近的小于的位置，计算中间的范围，包含当前元素的子数组有几个。
 *
 * 注意：
 * 有重复元素怎么办？相等按照小于算
 * 例如数组里有3个6，第二个6压栈的时候第一个6结算，为了避免重复计算，第一个6结算的子数组范围不能延伸到第二个6，第二个6结算的子数组范围包含第一个6但不能包含第三个6，第三个6结算的范围可以包含3个6.
 * 这跟正常单调栈结算的时候在右边最近的小于的位置停住，是一样的操作，因此相等就按照小于算即可。
 */
public class Code6SumOfSubArrMin {
    public static int sumOfMin(int[] arr) {
        int sum = 0;
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < arr.length; i++) {
            while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
                Integer cur = stack.pop();
                int leftLess = stack.isEmpty() ? -1 : stack.peek();
                // 假设当前位置是i，左边到不了的位置是l，右边到不了的位置是r，求中间有几个包含i的子数组？
                // 子数组无非就是两个位置：开始位置和结束位置，
                // 包含i的子数组的开始位置有几种可能？从l+1到i都可以，数量为i-l
                // 包含i的子数组的结束位置有几种可能？从i到r-1都可以，数量为r-i
                // 所以(l,r)之间包含i的子数组个数为(i-l)*(r-i)
                sum += (cur - leftLess) * (i - cur) * arr[cur];
            }
            stack.push(i);
        }

        while (!stack.isEmpty()) {
            Integer cur = stack.pop();
            int leftLess = stack.isEmpty() ? -1 : stack.peek();
            sum += (cur - leftLess) * (arr.length - cur) * arr[cur];
        }

        return sum;
    }

    public static void main(String[] args) {
        int[] arr={2,4,3,5,1};
        System.out.println(sumOfMin(arr));
    }
}
