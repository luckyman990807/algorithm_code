package dynamic_programing.SlopeOptimize;

/**
 * 给定正数无重复数组arr，每个值表示一种面额，给定正数aim，求组成aim的最小张数
 */
public class Code016SlopeOptimize_MinNumCombineMoneyUnlimited {
    /**
     * 解法第一阶段：暴力递归
     * @param money
     * @param aim
     * @return
     */
    public static int force(int[] money, int aim) {
        return process(money, 0, aim);
    }

    public static int process(int[] money, int index, int restAim) {
        // 边界条件
        // 不需要判断restAim<0，因为枚举可能性的时候限制了张数，不会让restAim减到<0
        // 如果所有货币都尝试完了，只有当恰好凑齐aim时，才算找到了一种方法，这种方法当前阶段（凑齐0元）所需最少张数为0；否则是无效方法
        if (index == money.length) {
            return restAim == 0 ? 0 : Integer.MAX_VALUE;
        }

        int min = Integer.MAX_VALUE;
        for (int num = 0; num * money[index] <= restAim; num++) {
            // 枚举所有可能的张数，比较出有效且最小值
            int next = process(money, index + 1, restAim - num * money[index]);
            if (next != Integer.MAX_VALUE) {
                min = Math.min(min, next + num);
            }
        }

        return min;
    }

    /**
     * 解法第二阶段：动态规划（严格位置依赖）
     * @param money
     * @param aim
     * @return
     */
    public static int dp(int[] money, int aim) {
        int[][] dp = new int[money.length + 1][aim + 1];
        dp[money.length][0] = 0;
        for (int restAim = 1; restAim <= aim; restAim++) {
            dp[money.length][restAim] = Integer.MAX_VALUE;
        }

        for (int index = money.length - 1; index >= 0; index--) {
            for (int restAim = 0; restAim <= aim; restAim++) {
                int min = Integer.MAX_VALUE;
                for (int num = 0; num * money[index] <= restAim; num++) {
                    int next = dp[index + 1][restAim - num * money[index]];
                    if (next != Integer.MAX_VALUE) {
                        min = Math.min(min, num + next);
                    }
                }
                dp[index][restAim] = min;
            }
        }

        return dp[0][aim];
    }

    /**
     * 解法第三阶段：动态规划，斜率优化
     * @param money
     * @param aim
     * @return
     */
    public static int dpSlopeOptimize(int[] money, int aim) {
        int[][] dp = new int[money.length + 1][aim + 1];
        dp[money.length][0] = 0;
        for (int restAim = 1; restAim <= aim; restAim++) {
            dp[money.length][restAim] = Integer.MAX_VALUE;
        }

        for (int index = money.length - 1; index >= 0; index--) {
            for (int restAim = 0; restAim <= aim; restAim++) {
                // 斜率优化，当前格子 = min(下边格子, 左边减去一张的格子+1)

                // 举个例子，假设index=2，面额=3，restAim=10，
                // 根据位置依赖关系，dp[2][10]=min(0+dp[3][10], 1+dp[3][7], 2+dp[3][4], 3+dp[3][1])
                // 而观察相邻位置发现，同样根据位置依赖关系，dp[2][7]=min(0+dp[3][7], 1+dp[3][4], 2+dp[3][1])
                // 等式两边都+1得：1+dp[2][7]=min(1+dp[3][7], 2+dp[3][4], 3+dp[3][1])
                // 带入dp[2][10]的依赖关系可得：dp[2][10]=min(0+dp[3][10], 1+dp[2][7])
                dp[index][restAim] = Math.min(
                        dp[index + 1][restAim],
                        restAim - money[index] >= 0 && dp[index][restAim - money[index]] != Integer.MAX_VALUE ?
                                dp[index][restAim - money[index]] + 1 :
                                Integer.MAX_VALUE);
            }
        }

        return dp[0][aim];
    }

    public static void main(String[] args) {
        int[] money = new int[]{46, 10, 20, 30, 5};
        int aim = 100;
        System.out.println(force(money, aim));
        System.out.println(dp(money, aim));
        System.out.println(dpSlopeOptimize(money, aim));
    }
}
