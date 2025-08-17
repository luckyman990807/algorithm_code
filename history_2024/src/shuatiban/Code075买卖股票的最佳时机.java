package shuatiban;

/**
 * https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/description/
 * 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
 * 你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票。设计一个算法来计算你所能获取的最大利润。
 * 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。
 *
 * 思路：
 * 选择在第x天卖出，求获得的最大利润（什么时候买入获利最大？在x前面找最小值），x从1到n-1分别求最大利润，最终返回最大值
 */
public class Code075买卖股票的最佳时机 {
    public int maxProfit(int[] prices) {
        // 记录i以及i之前的所有数的最小值
        int preMin = Integer.MAX_VALUE;
        // 记录最终的答案
        int ans = Integer.MIN_VALUE;
        for (int i = 0; i < prices.length; i++) {
            // 更新i及之前的最小值
            preMin = Math.min(preMin, prices[i]);
            // i时间卖出，那么最好的买入时间就是i及之前的最小值
            ans = Math.max(ans, prices[i] - preMin);
        }
        return ans;
    }
}
