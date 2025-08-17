package tixiban.class1_13.dynamicprogramming;

/**
 * 给定一个数组，数组的值都是正整数，且不重复，表示不同面额，每种面额有无限张。给定一个整数表示目标金额。
 * 求组成目标金额所用货币最少张数
 */
public class Code16MoneyWaysMinCount {
    /**
     * 暴力递归法
     * @param arr 货币面额数组
     * @param index 当前遍历到第几个面额
     * @param curAim 当前剩余目标金额
     * @return
     */
    public static int processViolent(int[] arr, int index, int curAim) {
        // 当货币数组遍历完的时候，如果当前目标金额恰好=0，那么不用任何货币就能凑成，如果当前目标金额不=0，那么永远都凑不成
        if (index == arr.length) {
            return curAim == 0 ? 0 : Integer.MAX_VALUE;
        }

        int minCount = Integer.MAX_VALUE;
        for (int count = 0; count * arr[index] <= curAim; count++) {
            // 当前面额的货币使用count张，求用剩下的面额凑齐剩下的金额最少要用多少张
            int nextCount = processViolent(arr, index + 1, curAim - count * arr[index]);
            // 当前的count张+剩下的nextCount张才是当前一共要用的张数。为了防止count + nextCount溢出，先判断下是不是MaxValue
            if (nextCount != Integer.MAX_VALUE) {
                minCount = Math.min(minCount, count + nextCount);
            }
        }
        return minCount;
    }

    public static int minCountViolent(int[] arr, int aim) {
        int result = processViolent(arr, 0, aim);
        return result == Integer.MAX_VALUE ? -1 : result;
    }

    /**
     * 严格表依赖的动态规划
     */
    public static int minCountDp(int[] arr, int aim) {
        int[][] dp = new int[arr.length + 1][aim + 1];
        for (int curAim = 1; curAim <= aim; curAim++) {
            dp[arr.length][curAim] = Integer.MAX_VALUE;
        }

        for (int index = arr.length - 1; index >= 0; index--) {
            for (int curAim = 0; curAim <= aim; curAim++) {
                dp[index][curAim] = Integer.MAX_VALUE;
                for (int count = 0; count * arr[index] <= curAim; count++) {
                    int nextCount = dp[index + 1][curAim - count * arr[index]];
                    if (nextCount != Integer.MAX_VALUE) {
                        dp[index][curAim] = Math.min(dp[index][curAim], count + nextCount);
                    }
                }
            }
        }
        return dp[0][aim];
    }

    /**
     * 时间优化的动态规划
     */
    public static int minCountDpSuper(int[] arr, int aim) {
        int[][] dp = new int[arr.length + 1][aim + 1];
        for (int curAim = 1; curAim <= aim; curAim++) {
            dp[arr.length][curAim] = Integer.MAX_VALUE;
        }
        for (int index = arr.length - 1; index >= 0; index--) {
            for (int curAim = 0; curAim <= aim; curAim++) {
                // 分析怎样把for循环枚举可能性变成O(1)的计算，假设当前面额arr[i]=3，剩余金额curAim=14，
                // dp[i][14] = Min{dp[i+1][14]+0, dp[i+1][11]+1, dp[i+1][8]+2, dp[i+1][5]+3, dp[i+1][2]+4}
                // dp[i][11] = Min{dp[i+1][11]+0, dp[i+1][8]+1, dp[i+1][5]+2, dp[i+1][2]+3}
                // 1式-(2式+1)推导出dp[i][14] = Min{dp[i+1][14], dp[i][11]+1}
                // 推广得dp[index][curAim] = Min{dp[index+1][curAim], dp[index][curAim-arr[i]]+1}
                // 如果下标越界了就忽略，如果相关的格子是MaxValue就不比较防止溢出
                int count = dp[index + 1][curAim];
                if (curAim - arr[index] >= 0 && dp[index][curAim - arr[index]] != Integer.MAX_VALUE) {
                    count = Math.min(count, dp[index][curAim - arr[index]] + 1);
                }
                dp[index][curAim] = count;
            }
        }
        return dp[0][aim];
    }

    public static void main(String[] args) {
        int[] arr = {1, 30, 5, 2};
        int aim = 100;
        System.out.println(minCountViolent(arr, aim));
        System.out.println(minCountDp(arr, aim));
        System.out.println(minCountDpSuper(arr, aim));
    }
}
