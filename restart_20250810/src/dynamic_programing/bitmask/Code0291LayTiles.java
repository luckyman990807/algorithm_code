package dynamic_programing.Bitmask;


/**
 * 你有无限的1*2的瓷砖，要铺满M*N的区域，不同的铺法有多少种？
 * 
 * 每个一般位置的可能性：
 * 看上去有5种：1、当前格子+上格子铺一块瓷砖，2、当前格子+右格子铺一块瓷砖，3、当前格子+下格子铺一块瓷砖，4、当前格子+左格子铺一块瓷砖，5、不铺
 * 实际上可以优化成3种：1、当前格子+上格子铺一块瓷砖，2、当前格子+右格子铺一块瓷砖，3、不铺
 * 为什么：因为当前格子+左格子，实际上就是左边格子的「当前格子+右格子」，当前格子+下格子，实际上就是下边格子的「当前格子+上格子」
 * 
 * 试法：
 * 给定一个行号和上一行的状态（每个格子铺了or没铺），返回从该行开始把剩余区域全铺完，有几种铺法
 * 每个格子要么上面没铺只能向上铺把上面填满，要么向右铺，要么不铺等着被下一行填满
 */
public class Code0291LayTiles {
    public static int force(int n) {
        if (n <= 2) {
            return n;
        }

        return process(0, 0, n);
    }

    public static int process(int status, int col, int cols) {
        if (col == cols) {
            return status == 0 ? 1 : 0;
        }
        if (col > cols) {
            return 0;
        }

        int ways = 0;
        if (status == 0) {
            // 当前列上下都没贴
            // |贴一块
            ways += process(0, col + 1, cols);
            // =贴两块
            ways += process(0, col + 2, cols);
            // L贴一块
            ways += process(2, col + 1, cols);
            // F贴一块
            ways += process(1, col + 1, cols);
        } else if (status == 1) {
            // 当前列上面贴了下面没贴
            // J贴一块
            ways += process(0, col + 2, cols);
            // 下面-贴一块
            ways += process(2, col + 1, cols);
        } else if (status == 2) {
            // 当前列上面没贴下面贴了
            // 7贴一块
            ways += process(0, col + 2, cols);
            // 上面-贴一块
            ways += process(1, col + 1, cols);
        }

        return ways;
    }

    public static int dp(int n) {
        long[][] dp = new long[3][n + 2];
        dp[0][n] = 1;

        long mode = (long) Math.pow(10, 9) + 7;
        for (int col = n - 1; col >= 0; col--) {
            dp[0][col] += ((dp[0][col + 1] % mode) + (dp[0][col + 2] % mode) + (dp[2][col + 1] % mode) + (dp[1][col + 1] % mode)) % mode;
            dp[1][col] += ((dp[0][col + 2] % mode) + (dp[2][col + 1] % mode)) % mode;
            dp[2][col] += ((dp[0][col + 2] % mode) + (dp[1][col + 1] % mode)) % mode;
        }

        return (int) (dp[0][0]);
    }
}
