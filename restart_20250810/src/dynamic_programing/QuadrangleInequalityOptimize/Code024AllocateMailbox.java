package dynamic_programing.QuadrangleInequalityOptimize;

import java.util.TreeSet;

/**
 * 邮局选址问题/邮筒选址问题
 * https://leetcode.cn/problems/allocate-mailboxes/description/
 *
 * 给定一个数组houses，houses[i]表示i号房子在一条街上的位置。现在要在这条街上安排k个邮筒，求所有房子到离他最近的邮筒的举例之和的最小值。
 *
 * 思路：大思路和画家问题一致：从0到index交给k个邮筒负责，枚举最后一个邮筒负责什么范围
 * 预处理：提前预处理一个数据结构，能够快速拿到从l到r交给一个邮筒负责，最优的距离和是多少。对应到画家问题就是预处理前缀和数组，即从l到r交给最后一个画家负责，画完所需要的时间是多少。
 *
 */
public class Code024AllocateMailbox {
    /**
     * 这条街上只能安排一个邮筒，求house每个区间范围内，采取最优选址的情况下，所有房子到邮筒的举例之和的最小值。
     *
     * 前置知识：
     * 如果有奇数个房子，那么邮筒的最优选址一定是在中间那个房子；
     * 如果有偶数个房子，那么邮筒的最优选址是中间两个房子哪个都一样。那我们就固定选中间两个之中右边那个，也就是下标更大的那个。
     *
     * @param house
     * @return
     */
    public int[][] getOneBoxAllocation(int[] house) {
        int[][] alloc = new int[house.length][house.length];
        for (int l = 0; l < house.length; l++) {
            for (int r = l + 1; r < house.length; r++) {
                // 从l到r的最优距离和 = 从l到r-1的最优距离和 + r房子到中点房子的距离
                // 中点房子的下标，不论是严格中点，还是两个中点之中下标更大的那个，都=(l+r)/2
                alloc[l][r] = alloc[l][r - 1] + house[r] - house[(r + l) >> 1];
                System.out.println("从" + house[l] + "到" + house[r] + "只安排一个邮筒，最小距离和为：" + alloc[l][r]);
            }
        }
        return alloc;
    }

    /**
     * 解法第一阶段：暴力递归
     * @param house
     * @param k
     * @return
     */
    public int force(int[] house, int k) {
        // 使house有序
        TreeSet<Integer> set = new TreeSet<>();
        for (int e : house) {
            set.add(e);
        }
        for (int i = 0; i < house.length; i++) {
            house[i] = set.pollFirst();
        }

        // 从l到r只能安排一个邮筒，最优选址的前提下，l到r每个房子到邮筒的距离和
        // 对应到画家问题，就是从l到r范围内由一个画家负责，画完这些画的最小时间，也就是l到r的累加和
        int[][] oneBoxAlloc = getOneBoxAllocation(house);

        return process(house.length - 1, k, oneBoxAlloc);
    }

    public int process(int index, int rest, int[][] oneBoxAlloc) {
        if (rest == 1) {
            return oneBoxAlloc[0][index];
        }
        if (index == 0) {
            return 0;
        }
        int min = Integer.MAX_VALUE;
        for (int split = index - 1; split >= 0; split--) {
            int sum = process(split, rest - 1, oneBoxAlloc) + oneBoxAlloc[split + 1][index];
            min = Math.min(min, sum);
        }
        System.out.println("从0到" + index + "安排" + rest + "个邮筒，最小距离和为：" + min);
        return min;
    }

    /**
     * 解法第二阶段：动态规划
     * @param house
     * @param k
     * @return
     */
    public int dp(int[] house, int k) {
        // 使house有序
        TreeSet<Integer> set = new TreeSet<>();
        for (int e : house) {
            set.add(e);
        }
        for (int i = 0; i < house.length; i++) {
            house[i] = set.pollFirst();
        }

        int[][] oneBoxAlloc = getOneBoxAllocation(house);

        int[][] dp = new int[house.length][k + 1];
        for (int index = 0; index < house.length; index++) {
            dp[index][1] = oneBoxAlloc[0][index];
        }

        for (int index = 1; index < house.length; index++) {
            for (int rest = 2; rest <= k; rest++) {
                dp[index][rest] = Integer.MAX_VALUE;
                for (int split = index - 1; split >= 0; split--) {
                    int sum = dp[split][rest - 1] + oneBoxAlloc[split + 1][index];
                    dp[index][rest] = Math.min(dp[index][rest], sum);
                }
            }
        }

        return dp[house.length - 1][k];
    }

    /**
     * 解法第三阶段：四边形不等式优化
     * @param house
     * @param k
     * @return
     */
    public int dpOpt(int[] house, int k) {
        // 使house有序
        TreeSet<Integer> set = new TreeSet<>();
        for (int e : house) {
            set.add(e);
        }
        for (int i = 0; i < house.length; i++) {
            house[i] = set.pollFirst();
        }

        int[][] oneBoxAlloc = getOneBoxAllocation(house);

        int[][] dp = new int[house.length][k + 1];
        for (int index = 0; index < house.length; index++) {
            dp[index][1] = oneBoxAlloc[0][index];
        }
        int[][] bestSplit = new int[house.length][k + 1];
        for (int rest = 2; rest <= k; rest++) {
            for (int index = house.length - 1; index >= 1; index--) {
                dp[index][rest] = Integer.MAX_VALUE;
                // 左边格子的最优划分点，作为本次划分的下限
                int down = bestSplit[index][rest - 1];
                // 下边格子的最优划分点，作为本次划分的上限。
                // 如果没有下边格子，就没有基于历史划分的上限可拿了，那么上限默认就是index-1，即划出index-1到index范围交给最后一个邮筒负责，剩下的范围交给剩下的邮筒负责。
                int up = index == house.length - 1 ? index - 1 : bestSplit[index + 1][rest];

                for (int split = up; split >= down; split--) {
                    int sum = dp[split][rest - 1] + oneBoxAlloc[split + 1][index];
                    // 这里是碰到严格<的结果才更新最优划分点，还是碰到<=就可以更新？都有可能，两种情况都尝试，不证明。
                    if (sum <= dp[index][rest]) {
                        dp[index][rest] = sum;
                        bestSplit[index][rest] = split;
                    }
                }
            }
        }

        return dp[house.length - 1][k];
    }
}
