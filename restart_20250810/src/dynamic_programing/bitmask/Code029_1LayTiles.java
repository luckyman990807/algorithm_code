package dynamic_programing.Bitmask;


/**
 * https://leetcode.cn/problems/domino-and-tromino-tiling/submissions/723469627/
 * 多米诺与托米诺平铺
 * 两种瓷砖，一种是2*1的，另一种是L型（2*1和1*1拼在一起），铺满2*n的区域，求总共有几种铺法。
 * 给定n，返回总铺法数对10的9次方+7取模
 */
public class Code029_1LayTiles {

    /**
     * 阶段一解法：暴力递归法
     * @param n
     * @return
     */
    public static int force(int n) {
        // 如果是2*1的区域，只有1种铺法。如果是2*2的区域，有2种铺法
        if (n <= 2) {
            return n;
        }

        // 递归，当前要铺第0列，第0列的初始状态是00（上面和下面都还没铺），总列数是n
        return process(0, 0, n);
    }

    /**
     * 递归函数
     * 当前要铺第col列，第col列的初始状态是status，总列数是cols
     * @param status
     * @param col
     * @param cols
     * @return
     */
    public static int process(int status, int col, int cols) {
        // 如果所有列都铺完了，那么第cols列（越界的那列）应该是空的，不应该有从col列延伸出来的瓷砖，才算一种有效的铺法
        if (col == cols) {
            return status == 0 ? 1 : 0;
        }
        // 如果col是无效列，返回0种铺法
        if (col > cols) {
            return 0;
        }

        // 遍历当前列所有可能的铺法
        int ways = 0;
        if (status == 0) {
            // 当前列的初始状态：上下都没铺
            // |铺一块，下一步该铺+1列，初始状态：上下都没铺
            ways += process(0, col + 1, cols);
            // =铺两块，下一步该铺+2列，初始状态：上下都没铺
            ways += process(0, col + 2, cols);
            // L铺一块，下一步该铺+1列，初始状态：上面没铺，下面铺了
            ways += process(2, col + 1, cols);
            // F铺一块，下一步该铺+1列，初始状态：上面没铺，下面铺了
            ways += process(1, col + 1, cols);
        } else if (status == 1) {
            // 当前列的初始状态：上面铺了，下面没铺
            // J铺一块
            ways += process(0, col + 2, cols);
            // 下面-铺一块
            ways += process(2, col + 1, cols);
        } else if (status == 2) {
            // 当前列的初始状态：上面没铺，下面铺了
            // 7铺一块
            ways += process(0, col + 2, cols);
            // 上面-铺一块
            ways += process(1, col + 1, cols);
        }

        return ways;
    }

    /**
     * 阶段二解法：改写严格位置依赖的动态规划
     * @param n
     * @return
     */
    public static int dp(int n) {
        // dp表，既然让返回取模，就说明结果会很大，超int范围，需要用long
        long[][] dp = new long[3][n + 2];
        // 根据递归出口对dp表初始化
        dp[0][n] = 1;

        long mode = (long) Math.pow(10, 9) + 7;

        // 填dp表，分析依赖关系，依赖+1列、+2列，所以col从大往小遍历
        for (int col = n - 1; col >= 0; col--) {
            dp[0][col] += ((dp[0][col + 1] % mode) + (dp[0][col + 2] % mode) + (dp[2][col + 1] % mode) + (dp[1][col + 1] % mode)) % mode;
            dp[1][col] += ((dp[0][col + 2] % mode) + (dp[2][col + 1] % mode)) % mode;
            dp[2][col] += ((dp[0][col + 2] % mode) + (dp[1][col + 1] % mode)) % mode;
        }

        return (int) (dp[0][0]);
    }
}
