package shuatiban;

/**
 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/
 * 给你一个整数数组 prices ，其中 prices[i] 表示某支股票第 i 天的价格。
 * 在每一天，你可以决定是否购买和/或出售股票。你在任何时候 最多 只能持有 一股 股票。你也可以先购买，然后在 同一天 出售。
 * 返回 你能获得的 最大 利润 。
 *
 * 思路：收益最大的方法是股票价格波动曲线的每个波谷位置买入，每个波峰位置卖出。也就是曲线的每个爬坡阶段我都赚。
 * 但是严格找出每一个波峰波谷不太容易，可以换一个思路：
 * 只要i时刻的价格大于i-1时刻，就认为我在i-1时刻买入，i时刻卖出，计算收益累加。
 */
public class Code076买卖股票的最佳时机_每个时刻手里只能持一股_可以买无限次 {
    public static int maxProfit(int[] prices) {
        int ans = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1]) {
                ans += prices[i] - prices[i - 1];
            }
        }
        return ans;
    }
}
