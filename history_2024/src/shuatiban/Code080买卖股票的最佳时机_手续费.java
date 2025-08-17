package shuatiban;

/**
 * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/
 * 给定一个整数数组 prices，其中 prices[i]表示第 i 天的股票价格 ；整数 fee 代表了交易股票的手续费用。
 * 你可以无限次地完成交易，但是你每笔交易都需要付手续费。如果你已经购买了一个股票，在卖出它之前你就不能再继续购买股票了。
 * 返回获得利润的最大值。
 * 注意：这里的一笔交易指买入持有并卖出股票的整个过程，每笔交易你只需要为支付一次手续费。
 * <p>
 * 思路：跟冷冻期的思路一样，还是通过buy数组维护一个「前面几次最好收益-最后一次买入价格」指标的最优，通过sail数组维护「0到i时刻无限次买卖的最好收益」指标的最优，
 * 只不过这次sail[i]等于buy[i-1]+prices[i]还要-fee，就加了这么一个限定而已
 */
public class Code080买卖股票的最佳时机_手续费 {
    public int maxProfit(int[] prices, int fee) {
        if (prices.length == 1) {
            return 0;
        }
        int[] buy = new int[prices.length];
        int[] sail = new int[prices.length];
        // 因为这道题没有一天冷冻期，所以for循环从1开始，初始值只设置0
        // 0到0时刻，最后一次动作是买入，能获得的最大收益，那么只可能是0时刻买入，并且是严格0时刻买入，不是0时刻买0时刻卖然后再0时刻买，因为卖一次就要交手续费，能不买就不卖。
        buy[0] = -prices[0];
        // 0到0时刻，最后一次动作是卖出，能获得的最大收益。如果真要卖的话，只能是0时刻买0时刻卖，但是这样不仅没有任何收益，反而要赔上手续费，所以最优的选择不是0买0卖，而是直接不交易。
        // 一旦这里选择了0买0卖在-fee，那么就会给后续过程带来问题，举个极端的例子，如果prices是递减的，那么最优的选择是不做任何交易，最终收益为0，而最初sail[0]=-fee的话，不做任何交易最终收益是-fee，答案不对
        sail[0] = 0;
        for (int i = 1; i < prices.length; i++) {
            buy[i] = Math.max(buy[i - 1], sail[i - 1] - prices[i]);
            // 0到i时刻最后一次动作是卖出的前提下最好的收益是，可能性1，i时刻不交易，=sail[i-1]，可能性2，就要在i时刻交易，就是要卖，那么=0到i-1最后一个动作是买入的前提下的最好收益+i时刻卖出的价格-手续费
            sail[i] = Math.max(sail[i - 1], buy[i - 1] + prices[i] - fee);
        }
        return sail[prices.length - 1];
    }

    /**
     * 空间优化，用两个变量滚动代替两个数组
     */
    public int maxProfit2(int[] prices, int fee) {
        if (prices.length == 1) {
            return 0;
        }
        int buyBest = -prices[0];
        int sellBest = 0;
        int curBuy;
        for (int i = 1; i < prices.length; i++) {
            curBuy = sellBest - prices[i];
            sellBest = Math.max(sellBest, buyBest + prices[i] - fee);
            buyBest = Math.max(buyBest, curBuy);
        }
        return sellBest;
    }
}
