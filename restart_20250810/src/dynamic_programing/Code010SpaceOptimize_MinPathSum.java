package dynamic_programing;

/**
 * 空间优化案例：最小路径和
 * https://leetcode.cn/problems/minimum-path-sum/description/
 * 给定一个mxn的二维网格，每个格子包含一个非负整数，表示经过这个格子的路径长度，每次只能向右或者向下走一步。求从左上角到右下角的最短路径长度。
 *
 * 思路：动态规划，
 * 尝试方法：process(grid,x,y)表示从grip的0，0位置出发到达x，y位置的最小路径和
 * 尝试策略：可能性1，从左边过来到达x，y，递归process(grid,x,y-1)+grid[x][y]；可能性2，从上边过来到达x，y，递归process(grid,x-1,y)+grid[x][y]。两种可能性取最小值。
 * 边界条件：x，y就是0，0时，从0，0到0，0的最小路径和就是grip[0][0]；x=0时，只可能从左边过来，递归process(grid,x,y-1)；y=0时，只可能从上边过来，递归process(grid,x-1,y)。
 */
public class Code010SpaceOptimize_MinPathSum {
    /**
     * 解法第一阶段：暴力递归
     * @param grid
     * @return
     */
    public static int force(int[][] grid) {
        return process(grid, grid.length - 1, grid[0].length - 1);
    }

    /**
     * 求二维网格grid从0，0出发，到达x，y位置的最短路径和
     */
    public static int process(int[][] grid, int x, int y) {
        // 到达0，0的最短路径和就是0，0格子的路径值
        if (x == 0 && y == 0) {
            return grid[x][y];
        }
        // 如果要到达的是第0行的位置，那么只可能从左边过来
        if (x == 0) {
            return process(grid, x, y - 1) + grid[x][y];
        }
        // 如果要到达的是第0列的位置，那么只可能从上边过来
        if (y == 0) {
            return process(grid, x - 1, y) + grid[x][y];
        }

        // 可能性1:从左边来到x，y
        int p1 = process(grid, x, y - 1) + grid[x][y];
        // 可能性2:从上边来到x，y（不越界的前提下
        int p2 = process(grid, x - 1, y) + grid[x][y];

        return Math.min(p1, p2);
    }

    /**
     * 解法第二阶段：动态规划（严格位置依赖）
     * @param grid
     * @return
     */
    public static int dp(int[][] grid) {
        // dp[i][j]表示从0，0出发到达i，j位置的最短路径和
        int[][] dp = new int[grid.length][grid[0].length];

        // 初始状态
        dp[0][0] = grid[0][0];
        for (int i = 1; i < grid[0].length; i++) {
            dp[0][i] = dp[0][i - 1] + grid[0][i];
        }
        for (int i = 1; i < grid.length; i++) {
            dp[i][0] = dp[i - 1][0] + grid[i][0];
        }

        // 状态转移
        for (int i = 1; i < grid.length; i++) {
            for (int j = 1; j < grid[0].length; j++) {
                dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + grid[i][j];
            }
        }

        return dp[dp.length - 1][dp[0].length - 1];
    }

    /**
     * 解法第三阶段：动态规划空间优化
     * 位置依赖关系是依赖左和上，赋值某一行的时候只依赖当前行和上一行，其他行都没用了，
     * 因此没必要申请一个二维数组的空间，可以用一个数组滚动赋值模拟填二维数组的效果。
     * @param grid
     * @return
     */
    public static int dp1(int[][] grid) {
        int[] dp = new int[grid[0].length];
        // 初始状态（也就是第一行的状态
        dp[0] = grid[0][0];
        for (int i = 1; i < grid[0].length; i++) {
            dp[i] = dp[i - 1] + grid[0][i];
        }
        // 状态转移
        for (int i = 1; i < grid.length; i++) {
            // 当前要赋值第i行，此时dp[j]更新之前代表二维的dp[i-1][j]，更新之后代表二维的dp[i][j]
            // 第一列，只依赖dp[i-1][j]+grid[i][j]，也就是更新前的自己+grid[i][j]
            dp[0] += grid[i][0];
            for (int j = 1; j < grid[0].length; j++) {
                // 除第一列以外的列，依赖max(dp[i-1][j],dp[i][j-1])+grid[i][j]，也就是max(更新前的自己,左边的格子)+grid[i][j]
                dp[j] = Math.min(dp[j], dp[j - 1]) + grid[i][j];
            }
        }
        return dp[grid[0].length - 1];
    }
}
