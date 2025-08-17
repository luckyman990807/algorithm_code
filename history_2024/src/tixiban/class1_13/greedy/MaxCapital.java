package tixiban.class1_13.greedy;

import java.util.PriorityQueue;

/**
 * 给定cost数组记录项目的成本，profit数组记录项目的纯利润，w记录初始资金，k记录有几次做项目的机会
 * 做一个项目后利润立马追加到w上，同一时间只能做一个项目，求w能达到的最大值
 */
public class MaxCapital {
    public static class Program {
        int cost;
        int profit;

        public Program(int cost, int profit) {
            this.cost = cost;
            this.profit = profit;
        }
    }

    public int getMaxProfit(int[] cost, int[] profit, int w, int k) {
        // 项目成本小根堆，成本小的放上面
        PriorityQueue<Program> minCostHeap = new PriorityQueue<>((o1, o2) -> o1.cost - o2.cost);
        // 项目利润大根堆，利润大的放上面
        PriorityQueue<Program> maxProfitHeap = new PriorityQueue<>((o1, o2) -> o2.profit - o1.profit);

        // 遍历cost和profit数组，new出program对象压入小根堆
        for (int i = 0; i < cost.length; i++) {
            minCostHeap.add(new Program(cost[i], profit[i]));
        }

        for (int i = 0; i < k; i++) {
            // 小根堆里，成本在当前资金范围内的，压入大根堆
            while (!minCostHeap.isEmpty() && minCostHeap.peek().cost <= w) {
                maxProfitHeap.add(minCostHeap.poll());
            }

            // 如果大根堆为空，说明以当前的w，一个能做的项目都没有，直接返回当前w
            if (maxProfitHeap.isEmpty()) {
                return w;
            }

            // 做了一个项目，利润追加到w上
            w += maxProfitHeap.poll().profit;
        }
        return w;
    }
}
