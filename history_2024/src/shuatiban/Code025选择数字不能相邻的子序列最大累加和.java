package shuatiban;

/**
 * 返回一个数组中所选数字不能相邻的情况下最大子序列累加和
 *
 * 思路:
 * 从左往右的动态规划模型
 * 递归求从0到i上不相邻子序列的最大累加和,有3种可能性:
 * 1.只选择i位置的 **注意不要漏了这个
 * 2.i位置算入累加和,答案 = i位置 + 0到i-2位置上不相邻子序列的最大累加和
 * 3.i位置不算入累加和,答案 = 0到i-1位置上不相邻子序列的最大累加和
 */
public class Code025选择数字不能相邻的子序列最大累加和 {
    /**
     * 递归尝试
     */
    public static int violent(int[] arr) {
        return process(arr, arr.length - 1);
    }

    public static int process(int[] arr, int index) {
        // 递归出口,因为递归index最多-2,所以必会落到0或1之中的某一个上
        // 0到0上子序列最大累加和,就是arr[0]自己
        if (index == 0) {
            return arr[0];
        }
        // 0到1上子序列最大累加和,因为不能取相邻的,所以只能返回arr[0]和arr[1]两个中较大的哪个
        if (index == 1) {
            return Math.max(arr[0], arr[1]);
        }
        // 三种可能性:1.只把index位置当累加和;2.算入index位置 + 0到index-2位置最大累加和;3.不算index位置,0到index-1位置最大累加和
        return Math.max(Math.max(arr[index], arr[index] + process(arr, index - 2)), process(arr, index - 1));
    }

    /**
     * 改动态规划
     */
    public static int dp(int[] arr) {
        if (arr.length == 1) {
            return arr[0];
        }
        if (arr.length == 2) {
            return Math.max(arr[0], arr[1]);
        }
        int dp0 = arr[0];
        int dp1 = arr[1];
        int max = Integer.MIN_VALUE;
        for (int i = 3; i < arr.length; i++) {
            int cur = Math.max(Math.max(arr[i], arr[i] + dp0), dp1);
            max = Math.max(max, cur);
            // 两个变量滚动,代替dp表
            dp0 = dp1;
            dp1 = cur;
        }
        return max;
    }

    public static void main(String[] args) {
        int[] arr = {-1, -2, 3, 4, -5, 6};
        System.out.println(violent(arr));
        System.out.println(dp(arr));
    }
}
