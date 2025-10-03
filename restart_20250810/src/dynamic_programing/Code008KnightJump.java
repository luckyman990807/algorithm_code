package dynamic_programing;

/**
 * 马跳棋盘
 * 10x9的棋盘上，马走日，给定x，y表示目标位置，给定k表示走的步数，求马从0，0位置走k步恰好到达x，y位置的方法数有多少
 *
 * 思路：三维动态规划。
 * 边界条件：1、如果越界，则返回方法数0，表示这一枝尝试白走了。2、如果剩余步数=0，则计算收益，如果当前位置恰好是目标位置，则返回方法数1，否则返回0。
 * 可能性罗列：8个方向分别罗列
 */
public class Code008KnightJump {
    /**
     * 解法一阶段：暴力递归
     * @param x
     * @param y
     * @param k
     * @return
     */
    public static int force(int x, int y, int k) {
        return process(x, y, k);
    }

    public static int process(int x, int y, int rest) {
        // 越界判断
        if (x < 0 || x > 9 || y < 0 || y > 8) {
            return 0;
        }
        // 边界条件
        if (rest == 0) {
            return x == 0 && y == 0 ? 1 : 0;
        }

        // 可能性罗列（马走日，有8个方向8种可能性）
        int ways = process(x + 2, y + 1, rest - 1);
        ways += process(x + 2, y - 1, rest - 1);
        ways += process(x - 2, y + 1, rest - 1);
        ways += process(x - 2, y - 1, rest - 1);
        ways += process(x + 1, y + 2, rest - 1);
        ways += process(x + 1, y - 2, rest - 1);
        ways += process(x - 1, y + 2, rest - 1);
        ways += process(x - 1, y - 2, rest - 1);

        return ways;
    }

    /**
     * 解法二阶段：动态规划（严格位置依赖）
     * @param x
     * @param y
     * @param k
     * @return
     */
    public static int dp(int x, int y, int k) {
        // 初始状态（注意，第一维表示马所在位置的行，变化范围是0～9，共10行。第二维表示马所在位置的列，变化范围是0～8，共9列。第三维表示剩余次数，变化范围是0～k）
        int[][][] dp = new int[10][9][k + 1];
        dp[0][0][0] = 1;

        // 状态转移
        for (int rest = 1; rest <= k; rest++) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 9; j++) {
                    dp[i][j][rest] += get(i + 2, j + 1, rest - 1, dp);
                    dp[i][j][rest] += get(i + 2, j - 1, rest - 1, dp);
                    dp[i][j][rest] += get(i - 2, j + 1, rest - 1, dp);
                    dp[i][j][rest] += get(i - 2, j - 1, rest - 1, dp);
                    dp[i][j][rest] += get(i + 1, j + 2, rest - 1, dp);
                    dp[i][j][rest] += get(i + 1, j - 2, rest - 1, dp);
                    dp[i][j][rest] += get(i - 1, j + 2, rest - 1, dp);
                    dp[i][j][rest] += get(i - 1, j - 2, rest - 1, dp);
                }
            }
        }

        return dp[x][y][k];
    }

    /**
     * 获取dp表的值，如果越界则返回0
     * @param x
     * @param y
     * @param rest
     * @param dp
     * @return
     */
    public static int get(int x, int y, int rest, int[][][] dp) {
        if (x < 0 || x > 9 || y < 0 || y > 8) {
            return 0;
        }
        return dp[x][y][rest];
    }

    public static void main(String[] args) {
        int x = 2, y = 1, k = 3;
        System.out.println(force(x, y, k));
        System.out.println(dp(x, y, k));
    }

}
