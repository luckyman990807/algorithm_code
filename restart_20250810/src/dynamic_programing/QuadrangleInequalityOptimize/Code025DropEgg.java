package dynamic_programing.QuadrangleInequalityOptimize;

/**
 * https://leetcode.cn/problems/super-egg-drop/description/
 * 扔蛋问题
 * 有n层楼，k枚鸡蛋，需要测试出楼层f，使得从>=f的楼层扔鸡蛋一定会碎，从f以下的楼层扔鸡蛋一定不会碎，求最小测试次数。
 *
 * 思路：
 * 如果只有一枚鸡蛋，那么最小测试次数=n。
 * 因为只有从1楼到n楼依次扔一遍，才能确认能够测出来结果，但凡跳着测，一旦鸡蛋碎了，就再也没法测试出结果了。
 *
 * 尝试策略：
 * 如果n层待测试的楼层交给k枚鸡蛋负责，那么枚举第一枚鸡蛋仍在i楼（使用了一次测试次数），
 * 如果碎了，递归下面i-1层楼交给k-1枚鸡蛋负责；
 * 如果没碎，递归上面n-i层楼交给k枚鸡蛋负责。
 *
 * 关于n层的解释：任何情况下递归传进来的n，都当作1到n层处理，不管上一轮递归传过来的是1-5还是6到10，都当作1-5处理，
 * 因为试法只关心相对这n层里面的基层，不关心实际的几层。
 *
 */
public class Code025DropEgg {
    /**
     * 解法一阶段：暴力递归
     * @param k
     * @param n
     * @return
     */
    public int force(int k, int n) {
        return process(n, k);
    }

    /**
     * 把总共restN层楼交给restK枚鸡蛋负责，返回确定能测试出结果的最小测试次数。
     * @param restN
     * @param restK
     * @return
     */
    public int process(int restN, int restK) {
        // 如果只剩一层，那么测试一次（在这层扔鸡蛋试试）就可以得出结果
        if (restN == 1) {
            return 1;
        }
        // 如果只剩一枚鸡蛋，那么还剩几层，最差情况就需要测几次
        if (restK == 1) {
            return restN;
        }

        // 一般情况，枚举第一枚鸡蛋测试第几层
        int min = Integer.MAX_VALUE;
        for (int first = 2; first <= restN; first++) {
            int p1 = 1 + process(first - 1, restK - 1);
            int p2 = 1 + process(restN - first, restK);
            // 因为题意是要在最坏情况下也能确认测试出结果，所以需要取两种可能性当中较大的次数
            int count = Math.max(p1, p2);

            min = Math.min(min, count);
        }

        return min;
    }

    /**
     * 解法二阶段：动态规划
     * @param k
     * @param n
     * @return
     */
    public int dp(int k, int n) {
        int[][] dp = new int[n + 1][k + 1];
        for (int restN = 0; restN <= n; restN++) {
            dp[restN][1] = restN;
        }
        for (int restK = 0; restK <= k; restK++) {
            dp[1][restK] = 1;
        }

        // 从暴力递归改写过来即可
        for (int restN = 2; restN <= n; restN++) {
            for (int restK = 2; restK <= k; restK++) {
                dp[restN][restK] = Integer.MAX_VALUE;
                for (int first = 2; first <= restN; first++) {
                    int p1 = 1 + dp[first - 1][restK - 1];
                    int p2 = 1 + dp[restN - first][restK];
                    int count = Math.max(p1, p2);
                    dp[restN][restK] = Math.min(dp[restN][restK], count);
                }
            }
        }

        return dp[n][k];
    }

    /**
     * 解法三阶段：四边形不等式优化
     * @param k
     * @param n
     * @return
     */
    public int dpOpt(int k, int n) {
        int[][] dp = new int[n + 1][k + 1];
        // 记录最优决策时第一枚鸡蛋从几层扔的
        int[][] bestFirst = new int[n + 1][k + 1];
        for (int restN = 0; restN <= n; restN++) {
            dp[restN][1] = restN;
            // 只剩一枚鸡蛋时，划分出这一枚鸡蛋负责的范围就只能是全部楼层，也就是从1到restN，所以最优划分点（第一枚鸡蛋从哪层扔）=1
            bestFirst[restN][1] = 1;
        }
        for (int restK = 0; restK <= k; restK++) {
            dp[1][restK] = 1;
            // 只剩一层时，最优划分点（第一枚鸡蛋从哪扔）肯定只能是第1层
            bestFirst[1][restK] = 1;
        }

        // 为什么外层循环从上到下，内层循环从右到左？
        // 使用四边形不等式优化时，需要考虑能否拿到枚举指导的位置对（右+上或者左+下）
        // 画表格分析根据这道题的位置依赖关系可知，某个位置依赖的是上边的全部一溜，以及左边位置的上边全部一溜。
        // 所以他肯定能拿到左+上。但是拿不到下（下边位置依赖当前位置），可以拿到右（因为不依赖本行，所以从左往右求和从右往左求都可以，从右往左求就可以拿到右）
        // 所以能拿到右+上。
        for (int restN = 2; restN <= n; restN++) {
            for (int restK = k; restK >= 2; restK--) {
                dp[restN][restK] = Integer.MAX_VALUE;
                // 分析右和上是上限还是下限：
                // 右边位置的含义是：鸡蛋数量增加，鸡蛋数量增加了那么需要尝试的次数肯定减少，因为鸡蛋足够多就可以二分法，也就是说第一枚鸡蛋测试的楼层变高（要么不变），也就是说对于最优划分点来说是上限；鸡蛋太少就只能遍历
                // 上边位置的含义是：楼层数减少，那么第一枚鸡蛋测试的楼层肯定相应地变低，也就是对于最优划分点来说是下限。
                int down = bestFirst[restN - 1][restK];
                int up = restK == k ? restN : bestFirst[restN][restK + 1];
                for (int first = down; first <= up; first++) {
                    int p1 = 1 + dp[first - 1][restK - 1];
                    int p2 = 1 + dp[restN - first][restK];
                    int count = Math.max(p1, p2);
                    if (count < dp[restN][restK]) {
                        dp[restN][restK] = count;
                        bestFirst[restN][restK] = first;
                    }
                }
            }
        }

        return dp[n][k];
    }

    /**
     * 本题最优解
     * 很独特的试法：dp[i][j]表示
     */
}
