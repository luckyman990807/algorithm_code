package shuatiban;

public class Code077买卖股票的最佳时机_每个时刻手里只能持一股_只能买卖2次 {
    public static int maxProfit(int[] prices) {
        int firstMax = 0;
        int secondMax = 0;
        int curProfit = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] >= prices[i - 1]) {
                curProfit += prices[i] - prices[i - 1];
                System.out.println("爬坡，累加收益，当前收益：" + curProfit);
            } else {
                System.out.print("波峰，结算，当前收益：" + curProfit);
                if (curProfit > firstMax) {
                    secondMax = firstMax;
                    firstMax = curProfit;
                } else if (curProfit > secondMax) {
                    secondMax = curProfit;
                }
                System.out.println("最大：" + firstMax + "，次大：" + secondMax);
                curProfit = 0;
            }
        }
        if (curProfit > firstMax) {
            secondMax = firstMax;
            firstMax = curProfit;
        } else if (curProfit > secondMax) {
            secondMax = curProfit;
        }
        return firstMax + secondMax;
    }

    public static void main(String[] args) {
        int[] prices = {1, 2, 4, 2, 5, 7, 2, 4, 9, 0};
        System.out.println(maxProfit(prices));
    }
}
