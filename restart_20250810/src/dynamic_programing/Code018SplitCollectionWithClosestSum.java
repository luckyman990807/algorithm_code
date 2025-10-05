package dynamic_programing;

/**
 * 给定一个集合，把这个集合拆分成两个子集，要求两个子集的累加和最接近，返回其中较小的累加和
 *
 * 思路：可以转换为背包问题
 * 所谓累加和最接近情况下较小的累加和，就等价于：
 * 全集的累加和为sum，求不超过sum/2的最大累加和
 *
 * 注释不写了，详见背包问题的注释
 */
public class Code018SplitCollectionWithClosestSum {
    public static int force(int[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }

        return process(arr, 0, sum / 2);
    }

    public static int process(int[] arr, int index, int restVolume) {
        if (index == arr.length) {
            return 0;
        }

        int p1 = process(arr, index + 1, restVolume);
        int p2 = arr[index] <= restVolume ? arr[index] + process(arr, index + 1, restVolume - arr[index]) : Integer.MIN_VALUE;

        return Math.max(p1, p2);
    }


    public static int dp(int[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }

        int[][] dp = new int[arr.length + 1][sum / 2 + 1];

        for (int index = arr.length - 1; index >= 0; index--) {
            for (int rest = 0; rest < dp[0].length; rest++) {
                if (rest - arr[index] >= 0) {
                    dp[index][rest] = Math.max(dp[index + 1][rest], arr[index] + dp[index + 1][rest - arr[index]]);
                } else {
                    dp[index][rest] = dp[index + 1][rest];
                }
            }
        }

        return dp[0][sum / 2];
    }

    public static void main(String[] args) {
        int[] arr = new int[]{1, 2, 3, 1, 2, 4};
        System.out.println(force(arr));
        System.out.println(dp(arr));
    }
}
