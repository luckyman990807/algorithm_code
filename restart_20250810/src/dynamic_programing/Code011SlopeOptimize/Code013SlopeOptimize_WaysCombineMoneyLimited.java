package dynamic_programing.Code011SlopeOptimize;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 给定一个money数组，每个值表示一张货币，相同的值表示有多张相同的货。给定一个正数aim，求组成aim的方法数
 */
public class Code013SlopeOptimize_WaysCombineMoneyLimited {
    public static int force(int[] money, int aim) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int curMoney : money) {
            if (!map.containsKey(curMoney)) {
                map.put(curMoney, 1);
            } else {
                map.put(curMoney, map.get(curMoney) + 1);
            }
        }

        int[] moneyDistinct = new int[map.size()];
        int[] num = new int[map.size()];
        int index = 0;
        for (int curMoney : map.keySet()) {
            moneyDistinct[index] = curMoney;
            num[index++] = map.get(curMoney);
        }

        return process(moneyDistinct, num, 0, aim);
    }

    public static int process(int[] money, int[] num, int index, int restAim) {
        if (index == money.length) {
            return restAim == 0 ? 1 : 0;
        }

        int ways = 0;
        for (int curNum = 0; curNum <= num[index] && curNum * money[index] <= restAim; curNum++) {
            ways += process(money, num, index + 1, restAim - curNum * money[index]);
        }

        return ways;
    }


    public static int dp_slope_opt(int[] money, int aim) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int curMoney : money) {
            if (!map.containsKey(curMoney)) {
                map.put(curMoney, 1);
            } else {
                map.put(curMoney, map.get(curMoney) + 1);
            }
        }

        int[] moneyDistinct = new int[map.size()];
        int[] num = new int[map.size()];
        int i = 0;
        for (int curMoney : map.keySet()) {
            moneyDistinct[i] = curMoney;
            num[i++] = map.get(curMoney);
        }

        int[][] dp = new int[moneyDistinct.length + 1][aim + 1];

        // 初始状态
        dp[moneyDistinct.length][0] = 1;

        // 状态转移
        for (int index = moneyDistinct.length - 1; index >= 0; index--) {
            for (int restAim = 0; restAim <= aim; restAim++) {
                // 有限张数的斜率优化，当前格子 = 下边格子 + 右边减去一张的格子 - 下一行左边减去最大张数+1张的格子

                // 举个例子，假设index=2，面额=3，张数=2，restAim=10，根据位置依赖关系以及张数限制，求dp[2][10]=dp[3][10]+dp[3][7]+dp[3][4]
                // 而进一步观察发现左边减去一张的格子，同样根据位置依赖关系及张数限制得dp[2][7]=dp[3][7]+dp[3][4]+dp[3][1]
                // 这时候跟无限张数的斜率优化不同的是，左边减去一张的格子多了一个dp[3][1]，需要单独减掉。这个格子是左边减去最大张数+1张的格子。

                // 先把下边格子加上
                dp[index][restAim] = dp[index + 1][restAim];
                // 如果左边减去一张的格子没越界，就加上
                if (restAim - moneyDistinct[index] >= 0) {
                    dp[index][restAim] += dp[index][restAim - moneyDistinct[index]];
                }
                // 如果左边减去最大张数+1张的格子没越界，就减去
                if (restAim - (num[index] + 1) * moneyDistinct[index] >= 0) {
                    dp[index][restAim] -= dp[index + 1][restAim - (num[index] + 1) * moneyDistinct[index]];
                }
            }
        }

        return dp[0][aim];
    }

    public static void main(String[] args) {
        int[] money = new int[]{1, 1, 1, 2, 2, 3};
        int aim = 7;
        System.out.println(force(money, aim));
        System.out.println(dp_slope_opt(money, aim));
    }
}