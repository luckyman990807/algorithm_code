package shuatiban;

/**
 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/
 * 给定一个整数数组prices，其中第  prices[i] 表示第 i 天的股票价格。
 * 设计一个算法计算出最大利润。在满足以下约束条件下，你可以尽可能地完成更多的交易（多次买卖一支股票）:
 * 卖出股票后，你无法在第二天买入股票 (即冷冻期为 1 天)。
 * 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
 * <p>
 * 思路：
 * 上一道题最多买卖K次，抽出来一个dp[i][j - 1] - prices[i]，含义是最后一次交易之前的几次交易的最大收益-最后一次交易的买入价格，简述为 前几次收益-最后一次买入。
 * 我们可以用这个指标来动态规划，让他达到最优，因为这个指标最优之后，+最后一次卖出的价格（即prices[i]）不就是0到i时刻无限交易的最大收益吗？
 * <p>
 * 根据这个思路，我们维护一个buy数组，buy[i]=k表示0到i时刻最后一次动作一定是买入的前提下，获得的最大收益为k（那么buy[i]+prices[i]就等于0到i时刻无限次买卖的最大收益）
 * 分析buy[i]的转移方程：
 * 可能性1，i时刻不参与交易，则buy[i]=buy[i-1]
 * 可能性2，i时刻参与交易，要求一定是买入动作，由于有1天冷冻期，所以buy[i] = 0到i-2时刻最后动作是卖出的前提下获得的最大收益 - prices[i]，即buy[i]=sail[i-2]-prices[i]
 * 因此我们还要维护一个sail数组，sail[i]=k表示0到i时刻最后动作是卖出的前提下获得的最大收益为k
 * 分析sail[i]转移方程：
 * 可能性1，i时刻不参与交易，则sail[i]=sail[i-1]
 * 可能性2，i时刻参与交易，要求一定是卖出动作，则sail[i]=0到i时刻最后动作是买入的前提下获得的最大收益 + prices[i]，即sail[i]=buy[i]+prices[i]
 * <p>
 * 最后返回sail[n-1]就是最终答案
 */
public class Code079买卖股票的最佳时机_冷冻期 {
    public int maxProfit(int[] prices) {
        if (prices.length == 1) {
            return 0;
        }
        int[] buy = new int[prices.length];
        int[] sail = new int[prices.length];
        // 为什么初始值设置到1而不是0，因为buy的转移方程用到sail[i-2]，for循环需要从2开始，所以初始值设置到1
        // buy[0]表示0到0时刻最后动作是买入，能获得的最大收益，因为有冷冻期，0时刻不可能有卖出，只可能是0时刻买，因此收益是-prices[0]
        buy[0] = -prices[0];
        // buy[1]表示0到1时刻最后动作是买入，能获得的最大收益，因为有冷冻期，0到1时刻不可能有卖出，要么是0时刻买，要么是1时刻买，因此收益是max(-prices[0],-prices[1])
        buy[1] = Math.max(-prices[0], -prices[1]);
        // sail[0]表示0到0时刻最后动作是卖出，能获得的最大收益，只可能是0时刻买0时刻卖，因此收益是0
        sail[0] = 0;
        // sail[1]表示0到1时刻最后动作是卖出，能获得的最大收益，要么是0买0卖，要么是1买1卖，要么是0买1卖，前两种收益是0，最后一种收益是prices[1]-prices[0]，取最大值
        sail[1] = Math.max(0, prices[1] - prices[0]);
        for (int i = 2; i < prices.length; i++) {
            buy[i] = Math.max(buy[i - 1], sail[i - 2] - prices[i]);
            sail[i] = Math.max(sail[i - 1], buy[i] + prices[i]);
        }
        return sail[prices.length - 1];
    }
}
