package dynamic_programing;

/**
 * 给定5个参数，N，M，row，col，k
 * 表示在NxM的范围上，醉汉bob的初始位置是row，col，bob必须走k步，每步会等概率向上下左右四个方向之一走一个单位，任何时候只要bob离开NxM区域就直接死亡。
 * 求k步后bob活着的概率
 *
 * 思路：
 * 马跳棋盘问题。
 * 概率怎么求？所有活着的方法数/所有可能的方法数。所有可能的方法数=4的k次方。
 */
public class Code014DrunkBob {
    public static double force(int n, int m, int row, int col, int k) {
        return (double) process(n, m, row, col, k) / Math.pow(4, k);
    }

    /**
     * 求在nxm的范围上，从curRow，curCol位置出发，走restK步，还活着的概率
     */
    public static int process(int n, int m, int curRow, int curCol, int restK) {
        if (curRow > n - 1 || curRow < 0 || curCol > m - 1 || curCol < 0) {
            return 0;
        }
        if (restK == 0) {
            return 1;
        }

        int ways = 0;
        ways += process(n, m, curRow - 1, curCol, restK - 1);
        ways += process(n, m, curRow + 1, curCol, restK - 1);
        ways += process(n, m, curRow, curCol - 1, restK - 1);
        ways += process(n, m, curRow, curCol + 1, restK - 1);

        return ways;
    }

    public static double dp(int n, int m, int row, int col, int k) {
        // dp[curRow][curRol][restK]的值表示从curRow，curRol位置出发走restK步，活着的概率
        int[][][] dp = new int[n][m][k + 1];

        // 初始状态
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                dp[i][j][0] = 1;
            }
        }

        // 状态转移
        for (int restK = 1; restK <= k; restK++) {
            for (int curRow = 0; curRow < n; curRow++) {
                for (int curCol = 0; curCol < m; curCol++) {
                    dp[curRow][curCol][restK] += get(dp, curRow - 1, curCol, restK - 1);
                    dp[curRow][curCol][restK] += get(dp, curRow + 1, curCol, restK - 1);
                    dp[curRow][curCol][restK] += get(dp, curRow, curCol - 1, restK - 1);
                    dp[curRow][curCol][restK] += get(dp, curRow, curCol + 1, restK - 1);
                }
            }
        }

        return dp[row][col][k] / Math.pow(4, k);
    }

    public static int get(int[][][] dp, int curRow, int curCol, int restK) {
        if (curRow > dp.length - 1 || curRow < 0 || curCol > dp[0].length - 1 || curCol < 0) {
            return 0;
        }
        return dp[curRow][curCol][restK];
    }


    public static void main(String[] args) {
        int n = 3;
        int m = 2;
        int row = 0;
        int col = 0;
        int k = 2;
        System.out.println(force(n, m, row, col, k));
        System.out.println(dp(n, m, row, col, k));
    }

}
