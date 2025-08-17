package tixiban.class27根据对数器找规律_根据数据量猜解法;

import java.util.Set;
import java.util.TreeSet;

/**
 * 给定一个int[] arr和一个int m,求arr所有子序列中,累加和取模m得到的最大值.
 */
public class Code05SubSequenceMaxModel {
    /**
     * 思路一:用数组下标和累加和做动态规划
     */

    /**
     * 暴力递归
     * 思路:数组从0到index的所有子序列中,能不能凑出累加和sum?能就返回true,不能就返回false.
     */
    public static boolean process1(int[] arr, int index, long sum) {
        // 递归出口,下标从0到0的所有子序列,能凑出的累加和只有arr[0]和0
        if (index == 0) {
            return sum == arr[0] || sum == 0;
        }
        // 可能性1:index没在子序列中,没参与凑累加和sum
        if (process1(arr, index - 1, sum)) {
            return true;
        }
        // 可能性2:index在子序列中,参与了凑累加和sum
        if (process1(arr, index - 1, sum - arr[index])) {
            return true;
        }
        return false;
    }

    public static int violent1(int[] arr, int m) {
        long maxSum = 0;
        for (int i = 0; i < arr.length; i++) {
            maxSum += arr[i];
        }
        int max = 0;
        for (int sum = 0; sum <= maxSum; sum++) {
            if (process1(arr, arr.length - 1, sum)) {
                max = Math.max(max, sum % m);
            }
        }
        return max;
    }

    /**
     * 改动态规划
     * i表示arr下标,j表示累加和,dp[i][j]==true表示从0到i可能的子序列中,有累加和模m得j的.
     */
    public static int dp1(int[] arr, int m) {
        int maxSum = 0;
        for (int i = 0; i < arr.length; i++) {
            maxSum += arr[i];
        }
        boolean[][] dp = new boolean[arr.length][maxSum + 1];
        // 根据递归出口条件,赋初始值
        for (int sum = 0; sum <= maxSum; sum++) {
            dp[0][sum] = false;
        }
        dp[0][0] = true;
        dp[0][arr[0]] = true;

        for (int index = 1; index < arr.length; index++) {
            for (int sum = 0; sum <= maxSum; sum++) {
                // 两种可能性或在一起
                dp[index][sum] = (dp[index - 1][sum]) | (sum >= arr[index] && dp[index - 1][sum - arr[index]]);
            }
        }
        // 从所有可能的累加和中,找出确定能凑出的累加和,并记录模m的最大值
        int max = 0;
        for (int sum = 0; sum <= maxSum; sum++) {
            if (dp[arr.length - 1][sum]) {
                max = Math.max(max, sum % m);
            }
        }
        return max;
    }


    /**
     * 思路二:用数组下标和模m的余数做动态规划
     */

    /**
     * 暴力递归
     * 思路:下标从0到index的子序列中,如果有累加和模m得rest的,返回true,否则返回false;
     */
    public static boolean process2(int[] arr, int m, int index, int rest) {
        // 递归出口,下标从0到0,可能的累加和只有0和arr[0],可能的余数只有0和arr[0]%m
        if (index == 0) {
            return rest == 0 || rest == arr[0] % m;
        }
        // 可能性1:在子序列中没有index的情况下,累加和模m也能得rest
        if (process2(arr, m, index - 1, rest)) {
            return true;
        }
        // 可能性2:在子序列中含有index的情况下,累加和模m得rest
        if (arr[index] <= rest) {
            // 可能性2.1: m为8,要求的余数为7,当前数arr[index]为5,那么只要0到index-1得到余数7-5=2即可
            if (process2(arr, m, index - 1, rest - arr[index])) {
                return true;
            }
        } else {
            // 可能性2.2: m为8,要求的余数为7,当前数arr[index]为10,那么只要0到index-1得到余数8+7-10=5即可
            if (process2(arr, m, index - 1, m + rest - arr[index])) {
                return true;
            }
        }
        return false;
    }

    public static int violent2(int[] arr, int m) {
        int max = 0;
        for (int rest = 0; rest < m; rest++) {
            if (process2(arr, m, arr.length - 1, rest)) {
                max = Math.max(max, rest);
            }
        }
        return max;
    }

    /**
     * 改动态规划
     * i表示arr下标,j表示余数,dp[i][j]==true表示从0到i的可能的子序列中,有累加和取模m得j的.
     */
    public static int dp2(int[] arr, int m) {
        boolean[][] dp = new boolean[arr.length][m];
        // 按照递归出口条件赋初始值
        for (int rest = 0; rest < m; rest++) {
            dp[0][rest] = false;
        }
        dp[0][0] = true;
        dp[0][arr[0] % m] = true;

        for (int index = 1; index < arr.length; index++) {
            for (int rest = 0; rest < m; rest++) {
                // 三种可能性或在一起
                dp[index][rest] = dp[index - 1][rest]
                        | (arr[index] <= rest && dp[index - 1][rest - arr[index]])
                        | (arr[index] > rest && m + rest > arr[index] && dp[index - 1][m + rest - arr[index]]);
            }
        }
        // 在所有可能的余数中,找出确定能得到的余数,并选出最大值
        int max = 0;
        for (int rest = 0; rest < m; rest++) {
            if (dp[arr.length - 1][rest]) {
                max = Math.max(max, rest);
            }
        }
        return max;
    }

    /**
     * 思路三：分治，每部分暴力递归形成一个表，最后整合。
     */

    /**
     * 暴力递归计算所有子序列的累加和
     * 用记录arr下标从cur到end所有子序列取模m后的余数
     */
    public static void process3(int[] arr, int cur, int end, int m, int curSum, Set<Integer> restSet) {
        if (cur > end) {
            restSet.add(curSum % m);
            return;
        }
        // 当前cur不计入子序列
        process3(arr, cur + 1, end, m, curSum, restSet);
        // 当前cur计入子序列
        process3(arr, cur + 1, end, m, curSum + arr[cur], restSet);
    }

    /**
     * 分治
     * @param arr
     * @param m
     * @return
     */
    public static int divide3(int[] arr, int m) {
        // 左半部分求出所有出现的余数
        TreeSet<Integer> leftRestList = new TreeSet<>();
        process3(arr, arr.length / 2, m, 0, 0, leftRestList);
        // 右半部分求出所有出现的余数
        TreeSet<Integer> rightRestList = new TreeSet<>();
        process3(arr, arr.length - 1, m, arr.length / 2 + 1, 0, rightRestList);
        // 对于余数最大值有3种可能，1.全在左半部分的子序列的累加和取余，2.全在右半部分的子序列的累加和取余，3.既有左半部分又有右半部分
        int max = 0;
        for (Integer leftRest : leftRestList) {
            max = Math.max(max, leftRest + rightRestList.floor(m - 1 - leftRest));
        }
        return max;
    }

    /**
     * 两种思路如何选择?
     * <p>
     * 如果arr的值巨大,累加和的返回也会巨大,dp表就巨大,不能用思路一,只能思路二.
     * <p>
     * 如果m的值巨大,那么余数的返回也就巨大,dp表就巨大,不能用思路二,只能思路一.
     */

    public static void main(String[] args) {
        int[] arr = {23, 65, 8, 12, 345, 567, 786, 34, 23, 2};
        int m = 8;
        System.out.println(violent1(arr, m));
        System.out.println(dp1(arr, m));
        System.out.println(violent2(arr, m));
        System.out.println(dp2(arr, m));
        System.out.println(divide3(arr, m));
    }

}
