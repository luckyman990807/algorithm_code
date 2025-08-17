package tixiban.class1_13.dynamicprogramming;

/**
 * 给定5个参数，N，M，row，col，k，表示在N*M的区域上，醉汉bob在初始位置（row，col），一共要迈出k步，
 * 每一步都会等概率向上/下/左/右走一个单位，只要bob超出N，M区域，就直接死亡，
 * 求k步之后bob活着的概率
 */
public class Code14BobDie {
    /**
     * 暴力递归法
     * 求从当前位置（curRow，curCol）出发，走restSteps步不死，一共有多少种走法
     * @param rows 区域有几行
     * @param cols 区域有几列
     * @param curRow 当前行数
     * @param curCol 当前列数
     * @param restSteps 剩余步数
     * @return
     */
    public static long processViolent(int rows, int cols, int curRow, int curCol, int restSteps) {
        // 如果越界，就死亡，说明没有找到新的走法，返回0
        if (curRow == rows || curRow < 0 || curCol == cols || curCol < 0) {
            return 0;
        }
        // 如果没死，并且步数已经走完了，说明找到了一种新的走法，返回1
        if (restSteps == 0) {
            return 1;
        }
        // 步数没走完，那么下一步可以走上下左右，把4种情况能产生的走法数累加
        long up = processViolent(rows, cols, curRow - 1, curCol, restSteps - 1);
        long down = processViolent(rows, cols, curRow + 1, curCol, restSteps - 1);
        long left = processViolent(rows, cols, curRow, curCol - 1, restSteps - 1);
        long right = processViolent(rows, cols, curRow, curCol + 1, restSteps - 1);

        return up + down + left + right;
    }

    public static double probabilityViolent(int rows, int cols, int initRow, int initCol, int steps) {
        // 存活的走法数/所有可能的走法
        return processViolent(rows, cols, initRow, initCol, steps) / Math.pow(4, steps);
    }

    /**
     * 严格表依赖的动态规划
     */
    public static double probabilityDp(int rows, int cols, int initRow, int initCol, int steps) {
        long[][][] dp = new long[rows][cols][steps + 1];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                dp[row][col][0] = 1;
            }
        }

        for (int restStep = 1; restStep <= steps; restStep++) {
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    dp[row][col][restStep] += pick(dp, rows, cols, row - 1, col, restStep - 1);
                    dp[row][col][restStep] += pick(dp, rows, cols, row + 1, col, restStep - 1);
                    dp[row][col][restStep] += pick(dp, rows, cols, row, col - 1, restStep - 1);
                    dp[row][col][restStep] += pick(dp, rows, cols, row, col + 1, restStep - 1);
                }
            }
        }

        return dp[initRow][initCol][steps] / Math.pow(4, steps);
    }

    public static double pick(long[][][] dp, int rows, int cols, int curRow, int curCol, int restStep) {
        // 超出区域范围就返回0，否则返回数组的值
        if (curRow < 0 || curRow == rows || curCol < 0 || curCol == cols) {
            return 0;
        }
        return dp[curRow][curCol][restStep];
    }

    /**
     * 主函数
     * @param args
     */
    public static void main(String[] args) {
        int rows = 6;
        int cols = 6;
        int initRow = 3;
        int initCol = 2;
        int steps = 10;
        System.out.println(probabilityViolent(rows, cols, initRow, initCol, steps));
        System.out.println(probabilityDp(rows, cols, initRow, initCol, steps));
    }
}
