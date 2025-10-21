package dynamic_programing.QuadrangleInequalityOptimize;

/**
 * 合并石头的最小成本
 *
 * 摆放着n堆石子，求将n堆石子有序地合并成一堆的最小成本。
 * 规定每次只能选相邻的两堆石子合并成新的一堆，并将新的一堆石子的总数记为该次合并的成本
 *
 * 思路：范围尝试模型，求l到r范围上合并成一堆所需的最小成本，正好是2个可变参数，用二维动态规划。（如果是从左往右的尝试模型，就凑不齐2个可变参数了）
 *
 */
public class Code022MiniCostToMergeStones {
    public static int force(int[] stones) {
        int[] sum = new int[stones.length + 1];
        for (int i = 0; i < stones.length; i++) {
            sum[i + 1] = sum[i] + stones[i];
        }
        return process(0, stones.length - 1, sum);
    }

    public static int process(int l, int r, int[] sum) {
        if (l == r) {
            return 0;
        }

        int min = Integer.MAX_VALUE;
        // 从0到split递归合并成1堆，从split+1到r递归合并成1堆，最后把这两堆合并成一堆
        // 枚举split的所有可能性：
        for (int split = l; split < r; split++) {
            int lCost = process(l, split, sum);
            int rCost = process(split + 1, r, sum);
            min = Math.min(min, lCost + rCost + sum[r + 1] - sum[l]);
        }
        return min;
    }

    public static int dp(int[] stones) {
        int[] sum = new int[stones.length + 1];
        for (int i = 0; i < stones.length; i++) {
            sum[i + 1] = sum[i] + stones[i];
        }

        // dp[l][r]表示从l到r的最小合并成本
        int[][] dp = new int[stones.length][stones.length];
        // 画图分析表格位置依赖关系，可知dp[l][r]依赖左边一溜和下边一溜，因此从左往右，从下往上填
        for (int l = stones.length - 2; l >= 0; l--) {
            for (int r = l + 1; r < stones.length; r++) {
                int min = Integer.MAX_VALUE;
                for (int split = l; split < r; split++) {
                    int lCost = dp[l][split];
                    int rCost = dp[split + 1][r];
                    min = Math.min(min, lCost + rCost + sum[r + 1] - sum[l]);
                }
                dp[l][r] = min;
            }
        }

        return dp[0][stones.length - 1];
    }

    public static int dpOpt(int[] stones) {
        int[] sum = new int[stones.length + 1];
        for (int i = 0; i < stones.length; i++) {
            sum[i + 1] = sum[i] + stones[i];
        }


        // dpCost[l][r]表示从l到r的最小合并成本
        int[][] dpCost = new int[stones.length][stones.length];
        // dpSplit[l][r]表示从l到r达成最小合并成本的划分点
        int[][] dpSplit = new int[stones.length][stones.length];
        for (int i = 0; i < dpSplit.length; i++) {
            dpSplit[i][i] = i;
        }

        // 画图分析表格位置依赖关系，可知dp[l][r]依赖左边一溜和下边一溜，因此从左往右，从下往上填
        for (int l = stones.length - 2; l >= 0; l--) {
            for (int r = l + 1; r < stones.length; r++) {
                int min = Integer.MAX_VALUE;
                for (int split = dpSplit[l][r - 1]; split <= Math.min(dpSplit[l + 1][r], stones.length - 2); split++) {
                    int lCost = dpCost[l][split];
                    int rCost = dpCost[split + 1][r];
                    int allCost = lCost + rCost + sum[r + 1] - sum[l];
                    if (allCost <= min) {
                        min = allCost;
                        dpSplit[l][r] = split;
                        dpCost[l][r] = allCost;
                    }
                }
            }
        }

        return dpCost[0][stones.length - 1];
    }

    public static void main(String[] args) {

        int[] arr = new int[]{3, 6, 4, 2};
        System.out.println(force(arr));
        System.out.println(dp(arr));
        System.out.println(dpOpt(arr));
    }
}
