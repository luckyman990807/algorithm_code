package tixiban.class30四边形不等式;

/**
 * 有n堆石子,现要将石子有次序地合并成一堆,
 * 规定每次只能选相邻的两堆合并,
 * 将新的一堆石子的数量记为该次合并的得分,
 * 求将n堆石子合并成一堆的最小得分(或最大得分)合并方案.
 *
 * 思路:范围上的动态规划
 */
public class Code03GroupStones {
    /**
     * 暴力递归
     * arr从left到right合并成一组,最少代价是多少.前缀和数组sum辅助.
     */
    public static int process(int[] arr, int[] sum, int left, int right) {
        if (left == right) {
            return 0;
        }
        int result = Integer.MAX_VALUE;
        for (int i = left; i < right; i++) {
            result = Math.min(
                    result,
                    process(arr, sum, left, i) + process(arr, sum, i + 1, right) + sum[right + 1] - sum[left]);
        }
        return result;
    }

    public static int violent(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        // 前缀和数组sum[0]放0,而不是放arr[0],目的是统一累加和计算,i到j的累加和统一为sum[j+1]-sum[i],不用判断是否==0
        int[] sum = new int[arr.length + 1];
        sum[0] = 0;
        for (int i = 1; i < sum.length; i++) {
            sum[i] = sum[i - 1] + arr[i - 1];
        }

        return process(arr, sum, 0, arr.length - 1);
    }

    /**
     * 改动态规划
     * 不做任何优化的动态规划,枚举所有可能性,复杂度O(N^3)
     * l表示合并范围左边界,r表示右边界(包含),dp[l][r]表示从l到r合并成一组的最小代价
     */
    public static int dp1(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }

        int[] sum = new int[arr.length + 1];
        sum[0] = 0;
        for (int i = 1; i < sum.length; i++) {
            sum[i] = sum[i - 1] + arr[i - 1];
        }

        int[][] dp = new int[arr.length][arr.length];

        for (int l = arr.length - 2; l >= 0; l--) {
            for (int r = l + 1; r < arr.length; r++) {
                int result = Integer.MAX_VALUE;
                // 枚举所有可能性
                for (int i = l; i < r; i++) {
                    result = Math.min(result, dp[l][i] + dp[i + 1][r] + sum[r + 1] - sum[l]);
                }
                dp[l][r] = result;
            }
        }
        return dp[0][arr.length - 1];
    }

    /**
     * 优化的动态规划,减少枚举可能性的次数
     * 思路:
     * 现在要求dp[3][17],也就是从3到17合并成一组的最小代价.
     * 假如,已知左边格子,从3到16合并的最小代价划分点为8,也就是3到8合并9到16合并再整体合并,
     * 已知下边格子,从4到17合并的最小代价划分点为13,也就是4到13合并,14到17合并,再整体合并,
     * 那么枚举可能性只需要在8到13之间就可以,不需要在3到17之间枚举.
     *
     * 怎么证明:实践证明,各种方法求解对数.
     */
    public static int dp2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }

        int[] sum = new int[arr.length + 1];
        sum[0] = 0;
        for (int i = 1; i < sum.length; i++) {
            sum[i] = sum[i - 1] + arr[i - 1];
        }

        // dp[i][j]表示从i到j合并成一组的最小代价
        int[][] dp = new int[arr.length][arr.length];
        // split[i][j]表示让从i到j合并成一组的代价最小的最佳划分点
        int[][] split = new int[arr.length][arr.length];
        // 为倒数第二条对角线(l=r-1)赋初值
        for (int i = 0; i < arr.length - 1; i++) {
            split[i][i + 1] = i;
            dp[i][i + 1] = arr[i] + arr[i + 1];
        }

        for (int l = arr.length - 3; l >= 0; l--) {
            for (int r = l + 2; r < arr.length; r++) {
                int result = Integer.MAX_VALUE;
                int point = -1;
                // 枚举可能性只在[左边格子对应的最佳划分点,右边格子对应的最佳划分点]之间,不需要在[l,r]之间
                for (int i = split[l][r - 1]; i <= split[l + 1][r]; i++) {
                    int cur = dp[l][i] + dp[i + 1][r] + sum[r + 1] - sum[l];
                    if (cur < result) {
                        result = cur;
                        point = i;
                    }
                }
                dp[l][r] = result;
                split[l][r] = point;
            }
        }
        return dp[0][arr.length - 1];
    }

    public static void main(String[] args) {
        int[] arr = {1, 1, 1, 10};
        System.out.println(violent(arr));
        System.out.println(dp1(arr));
        System.out.println(dp2(arr));
    }
}
