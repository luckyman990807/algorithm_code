package dynamic_programing.SlopeOptimize;

/**
 * 拼凑货币的方法数
 * 给定一个正数数组，每个值都代表一个货币，即便值相同，两张货币也认为是不同的。给定一个正数aim，求用货币组合出aim的方法数。
 */
public class Code011WaysCombineMoney {
    public static int force(int[] money, int aim) {
        return process(money, 0, aim);
    }

    public static int process(int[] money, int index, int restAim) {
        if (restAim < 0) {
            return 0;
        }
        if (index == money.length) {
            return restAim == 0 ? 1 : 0;
        }

        // 可能性1:不要当前货币
        int p1 = process(money, index + 1, restAim);
        // 可能性2:要当前货币
        int p2 = process(money, index + 1, restAim - money[index]);

        // 返回两种可能性的方法数之和
        return p1 + p2;
    }

    public static int dp(int[] money, int aim) {
        int[][] dp = new int[money.length + 1][aim + 1];

        // 初始状态
        dp[money.length][0] = 1;

        // 状态转移
        for (int index = money.length - 1; index >= 0; index--) {
            for (int restAim = 0; restAim <= aim; restAim++) {
                dp[index][restAim] = dp[index + 1][restAim] + (restAim - money[index] >= 0 ? dp[index + 1][restAim - money[index]] : 0);
            }
        }

        return dp[0][aim];
    }


    public static void main(String[] args) {
        int[] money = new int[]{1, 1, 1, 2};
        int aim = 3;
        System.out.println(force(money, aim));
        System.out.println(dp(money, aim));

    }
}
