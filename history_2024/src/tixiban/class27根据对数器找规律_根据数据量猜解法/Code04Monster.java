package tixiban.class27根据对数器找规律_根据数据量猜解法;

/**
 * int[] d, d[i]表示i号怪兽的战力,
 * int[] p, p[i]表示i号怪兽要求的钱,
 * 开始你的能力是0,你的目标是从0号怪兽开始通关所有怪兽.
 * 如果当前战力小于i号怪兽战力,那么必须花p[i]买下它才能通过,同时i号怪兽的战力叠加到你身上.
 * 如果当前战力大于等于i号怪兽的战力,那么既可以直接通过,也可以花p[i]买下他,战力叠加到你身上.
 * 战力在任何时候都不会下降.
 * 返回通关所有怪兽需要花的最小钱数.
 *
 */
public class Code04Monster {
    /**
     * 思路一:由怪兽号和战力值展开动态规划
     */

    /**
     * 暴力递归
     * 尝试思路:当前战力是ability,从index号怪兽开始通关到最后一头,最少需要多少钱.
     * f(0,0)就是最后的答案.
     */
    public static long process1(int[] d, int[] p, int index, int ability) {
        // 递归出口,如果index越界了,返回0
        if (index == d.length) {
            return 0;
        }
        // 如果当前战力小于当前怪兽,只能花钱,战力叠加,返回当前花的钱+后续花的钱
        if (ability < d[index]) {
            return p[index] + process1(d, p, index + 1, ability + d[index]);
        }
        // 如果当前战力大于当前怪兽,那么比较一下直接过更省钱省钱,还是买下当前怪兽,战力叠加后面对后续怪兽更省钱
        return Math.min(
                process1(d, p, index + 1, ability),
                p[index] + process1(d, p, index + 1, ability + d[index])
        );
    }

    public static long violent1(int[] d, int[] p) {
        return process1(d, p, 0, 0);
    }

    /**
     * 改动态规划
     * 分析自变量:index和ability,
     * i表示当前怪兽号,j表示当前战力,dp[i][j]表示以j战力从i号怪兽通关到最后所需要的最少钱数.
     * 根据不同尝试的条件,直接改成状态转移方程.
     */
    public static long dp1(int[] d, int[] p) {
        int maxAbility = 0;
        for (int i = 0; i < d.length; i++) {
            maxAbility += d[i];
        }
        long[][] dp = new long[d.length + 1][maxAbility + 1];

        // 由递归出口条件改出dp初始值
        for (int j = 0; j <= maxAbility; j++) {
            dp[d.length][j] = 0;
        }

        for (int index = d.length - 1; index >= 0; index--) {
            for (int ability = 0; ability <= maxAbility; ability++) {
                // 跳过不会出现的状态,防止数组越界
                if (ability + d[index] > maxAbility) {
                    continue;
                }
                // 根据尝试条件直接改厨状态转移方程
                if (ability < d[index]) {
                    dp[index][ability] = p[index] + dp[index + 1][ability + d[index]];
                } else {
                    dp[index][ability] = Math.min(
                            dp[index + 1][ability],
                            p[index] + dp[index + 1][ability + d[index]]
                    );
                }
            }
        }
        return dp[0][0];
    }


    /**
     * 思路二:由怪兽号和钱数展开动态规划
     */

    /**
     * 暴力递归
     * 尝试思路:当前已经通关了index号怪兽,严格花money钱,所能达到的最大战力是多少.如果没法正好出money钱,或者花money钱没法通过index,返回-1.
     * f(n-1,money)money从左到右那个最先不等于-1,对应的money就是答案.
     */
    public static long process2(int[] d, int[] p, int index, int money) {
        if (index == 0) {
            if (money == p[index]) {
                return d[index];
            } else {
                return -1;
            }
        }

        // 可能性1:当前战力是没花钱通过第index号怪兽达到的,那么战力=以同样money通过第index-1时的战力(没花钱就不会增加战力)
        // 怎么样才能以money不花钱通过index号怪兽?
        // 首先得以money通过了index-1号怪兽. 其次以money通过index-1号怪兽时的战力要大于index号怪兽.
        long preAbility1 = process2(d, p, index - 1, money);
        long curAbility1 = -1;
        if (preAbility1 != -1 && preAbility1 > d[index]) {
            curAbility1 = preAbility1;
        }
        // 可能性2:当前战力是花钱买第index号怪兽达到的,那么战力=index号怪兽的战力+以同样money通关index-1号怪兽时的战力
        // 怎么样才能花钱通过index号怪兽?
        // 只要以买index号怪兽之前的money通关了index-1号怪兽即可,对战力没有要求(反正都要花钱了)
        long preAbility2 = process2(d, p, index - 1, money - p[index]);
        long curAbility2 = -1;
        if (preAbility2 != -1) {
            curAbility2 = d[index] + preAbility2;
        }

        return Math.max(curAbility1, curAbility2);
    }

    public static long violent2(int[] d, int[] p) {
        int maxMoney = 0;
        for (int i = 0; i < p.length; i++) {
            maxMoney += p[i];
        }

        for (int money = 0; money <= maxMoney; money++) {
            // 什么时候以money能通关最后一头怪兽,就返回当前money
            if (process2(d, p, d.length - 1, money) != -1) {
                return money;
            }
        }
        // 否则就返回所有怪兽所需的money之和(最差情况每头怪兽都买了,肯定能通关)
        return maxMoney;
    }

    /**
     * 改动态规划
     * 分析自变量:index和money.
     * i表示怪兽号,j表示严格花的钱数,dp[i][j]表示以j钱通关到i号怪兽能达到的最大战力.dp[i][j]=-1表示没法整好花掉j钱,或者j钱不足以通关到i号怪兽.
     */
    public static long dp2(int[] d, int[] p) {
        // 最大钱数:把所有怪兽全买下所需要的钱数
        int maxMoney = 0;
        for (int i = 0; i < p.length; i++) {
            maxMoney += p[i];
        }
        // index从0到d.length-1, money从0到maxMoney
        long[][] dp = new long[d.length][maxMoney + 1];
        // 根据递归出口条件:index=0时,只有money=p[0]时战力为d[0],money=其他时战力都为-1.
        for (int money = 0; money <= maxMoney; money++) {
            dp[0][money] = -1;
        }
        dp[0][p[0]] = d[0];

        for (int index = 1; index < d.length; index++) {
            for (int money = 0; money <= maxMoney; money++) {
                // 把尝试的可能性直接改成状态转移方程
                long curAbility1 = -1;
                if (dp[index - 1][money] != -1 && dp[index - 1][money] > d[index]) {
                    curAbility1 = dp[index - 1][money];
                }

                long curAbility2 = -1;
                // 防止money - p[index]越界
                if (money >= p[index] && dp[index - 1][money - p[index]] != -1) {
                    curAbility2 = d[index] + dp[index - 1][money - p[index]];
                }

                dp[index][money] = Math.max(curAbility1, curAbility2);
            }
        }

        // 找到最后一行第一个不等于-1的战力,对应的money就是答案.
        for (int money = 0; money <= maxMoney; money++) {
            if (dp[d.length - 1][money] != -1) {
                return money;
            }
        }
        return maxMoney;
    }


    /**
     * 两种思路如何选择?
     *
     * 当战力的范围较小,钱的范围较大时,选择思路一,根据怪兽号和战力来动态规划.
     *
     * 例如,怪兽范围是10^2头,每头怪兽战力范围是10^3,每头怪兽的钱数范围是10^6,要求Java运行时间2~4秒,
     * 那么就用怪兽号和战力做动态规划,dp表的复杂度为(10^3*10^2)*10^2=10^7,在10^8以内.
     * 而如果用怪兽号和钱数做动态规划的话,dp表复杂度为(10^6*10^2)*10^2=10^10,远超10^8,必定超时.
     *
     * 一句话:哪个的dp表规模小选哪个.
     *
     * 给定int[]arr,长度N范围是10^12,arr值的范围1000,要想不超时,要么在N上做二分,要么解法跟N无关跟值有关.
     */


    public static void main(String[] args) {
        int[] d = {3, 6, 2, 45, 8, 10};
        int[] p = {57, 23, 68, 35, 28, 9};
        System.out.println(violent1(d, p));
        System.out.println(dp1(d, p));
        System.out.println(violent2(d, p));
        System.out.println(dp2(d, p));
    }
}
