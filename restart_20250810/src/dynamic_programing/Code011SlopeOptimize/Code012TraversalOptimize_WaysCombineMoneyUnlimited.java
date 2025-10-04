package dynamic_programing.Code011SlopeOptimize;

/**
 * 遍历优化案例：组成aim的方法数，每个面额的货币无限张
 * 给定一个正数数组表示货币面额，每个面额的货币有无限张，给定一个正数aim，求用货币组成aim的方法数
 *
 * 思路：由于每个面额的货币有无限张，因此尝试时每张货币需要穷举出所有可能的使用张数
 *
 * 优化点：斜率优化，消除穷举导致的复杂度升高
 */
public class Code012TraversalOptimize_WaysCombineMoneyUnlimited {
    /**
     * 解法第一阶段：暴力递归
     * @param money
     * @param aim
     * @return
     */
    public static int force(int[] money, int aim) {
        return process(money, 0, aim);
    }

    /**
     * 求面额数组money使用从index往后的所有面额组成restAim的所有方法数
     * @param money
     * @param index
     * @param restAim
     * @return
     */
    public static int process(int[] money, int index, int restAim) {
        // 边界条件，所有钱都用完了，如果恰好aim已经凑齐了，就返回一种方法数，否则返回0
        if (index == money.length) {
            return restAim == 0 ? 1 : 0;
        }

        // 遍历index使用张数的所有可能性，分别递归累计
        int ways = 0;
        for (int num = 0; num * money[index] <= restAim; num++) {
            ways += process(money, index + 1, restAim - num * money[index]);
        }

        return ways;
    }

    /**
     * 解法第二阶段：动态规划（严格位置依赖）
     * @param money
     * @param aim
     * @return
     */
    public static int dp(int[] money, int aim) {
        int[][] dp = new int[money.length + 1][aim + 1];

        // 初始状态
        dp[money.length][0] = 1;

        // 状态转移
        for (int index = money.length - 1; index >= 0; index--) {
            for (int restAim = 0; restAim <= aim; restAim++) {
                for (int num = 0; num * money[index] <= restAim; num++) {
                    dp[index][restAim] += dp[index + 1][restAim - num * money[index]];
                }
            }
        }

        return dp[0][aim];
    }

    /**
     * 解法第三阶段：动态规划（斜率优化）
     */
    public static int dp1(int[]money, int aim){
        int[][] dp = new int[money.length + 1][aim + 1];

        // 初始状态
        dp[money.length][0] = 1;

        // 状态转移
        for (int index = money.length - 1; index >= 0; index--) {
            for (int restAim = 0; restAim <= aim; restAim++) {
                // 斜率优化代替穷举，当前格子 = 下边格子 + 左边减去一张的格子

                // 举个例子，假设index=2，面额=3，restAim=10，于是根据位置依赖关系，求dp[2][10]=dp[3][10]+dp[3][7]+dp[3][4]+dp[3][1]
                // 而进一步观察发现，左边减去一张的格子dp[2][7]也根据依赖关系=dp[3][7]+dp[3][4]+dp[3][1]
                // 因此推导出dp[2][10]=dp[3][10]+dp[2][7]
                // 即 = 下边格子 + 左边减去一张的格子
                dp[index][restAim]=dp[index+1][restAim]+(restAim-money[index] >=0 ? dp[index][restAim-money[index]] : 0);
            }
        }

        return dp[0][aim];
    }

    public static void main(String[] args) {
        int[] money = new int[]{1, 2, 3};
        int aim = 7;
        System.out.println(force(money, aim));
        System.out.println(dp(money, aim));
        System.out.println(dp1(money, aim));
    }
}
