package tixiban.class31状态压缩的动态规划;

/**
 * 你有无限的1*2的砖,要铺满N*M的地面,有多少种不同的铺法?
 *
 * 2*M的问题很简单,斐波那契数列,现在是N*M
 *
 * 思路:
 * N*M中的一个格子,可以往上铺(在当前格子和上面格子上铺一块砖)、往下铺、往左铺、往右铺,
 * 现规定只能往上铺(上面格子没铺的前提下)、往右铺、不铺(留给下面格子铺),那么每一行的格子的状态可以用一个数组表示,0代表没铺,1代表铺了.
 * 认为第0行全是1,从第一行开始递归尝试所有铺法.
 * 然后状态压缩把数组换成int.
 */
public class Code03NMFilledWith12 {
    /**
     * 暴力递归
     */
    public static int violent(int n, int m) {
        int row = Math.max(n, m);
        int col = Math.min(n, m);

        // 认为第-1行全铺满了,第0行不能向上铺
        int[] preRowStatus = new int[col];
        for (int i = 0; i < preRowStatus.length; i++) {
            preRowStatus[i] = 1;
        }
        // 递归从第0行开始
        return process(row, 0, preRowStatus);
    }

    /**
     * 暴力递归
     * 给定当前行和上一行的状态,返回从这行一直铺满可以有几种铺法
     */
    public static int process(int row, int curRow, int[] preRowStatus) {
        if (curRow == row) {
            // 把所有行都过完了,如果最后一行有空位,说明没铺满.否则就是找到了一个新的铺法.
            for (int curCol = 0; curCol < preRowStatus.length; curCol++) {
                if (preRowStatus[curCol] == 0) {
                    return 0;
                }
            }
            return 1;
        }

        int[] curRowStatus = new int[preRowStatus.length];
        for (int curCol = 0; curCol < curRowStatus.length; curCol++) {
            // 上面是0,当前就是一定要是1,因为如果当前行不把上一行的空位铺满,就再也铺不满了.上面是1,当前先设为0,后面自由发挥
            curRowStatus[curCol] = preRowStatus[curCol] ^ 1;
        }

        // 剩下=0自由发挥的格子,深度优先遍历尝试所有可能性.
        return dfs(curRowStatus, 0, curRow, row);
    }

    /**
     * 深度优先遍历
     * 每个位置可以选择1.不铺,2.横着往右铺
     * curRow和row都是用来递归process的
     */
    public static int dfs(int[] curRowStatus, int curCol, int curRow, int row) {
        // 如果试到本行的最后一个了,就换下一行递归
        if (curCol == curRowStatus.length) {
            return process(row, curRow + 1, curRowStatus);
        }
        int ways = 0;
        // 可能性1.当前位置不铺
        ways += dfs(curRowStatus, curCol + 1, curRow, row);
        // 可能行2.当前位置和右边位置横着铺一块砖
        if (curCol + 1 < curRowStatus.length && curRowStatus[curCol + 1] == 0 && curRowStatus[curCol] == 0) {
            curRowStatus[curCol] = 1;
            curRowStatus[curCol + 1] = 1;
            ways += dfs(curRowStatus, curCol + 2, curRow, row);
            curRowStatus[curCol] = 0;
            curRowStatus[curCol + 1] = 0;
        }

        return ways;
    }

    /**
     * 状态压缩+傻缓存
     */
    public static int dp(int n, int m) {
        int row = Math.max(n, m);
        int col = Math.min(n, m);

        int[][] dp = new int[1 << col][row + 1];
        for (int i = 0; i < dp.length; i++) {
            for (int j = 0; j < dp[i].length; j++) {
                dp[i][j] = -1;
            }
        }

        // 递归从第0行开始
        return dpProcess(row, col, 0, (1 << col) - 1, dp);
    }

    public static int dpProcess(int row, int col, int curRow, int preStatus, int[][] dp) {
        if (dp[preStatus][curRow] != -1) {
            return dp[preStatus][curRow];
        }
        int ways = 0;
        if (curRow == row) {
            // (1 << col) - 1 就是1111111格式,说明全部都铺满了,没有空位
            ways = preStatus == (1 << col) - 1 ? 1 : 0;
        } else {
            // preStatus 异或上 ((1 << col) - 1) 就相当于把preStatus的低col位取反
            int curStatus = preStatus ^ ((1 << col) - 1);
            ways = dpDfs(curStatus, 0, col, curRow, row, dp);
        }

        dp[preStatus][curRow] = ways;
        return ways;
    }

    /**
     * 深度优先遍历
     * curRow、row都是递归用的
     */
    public static int dpDfs(int curStatus, int curCol, int col, int curRow, int row, int[][] dp) {
        if (curCol == col) {
            return dpProcess(row, col, curRow + 1, curStatus, dp);
        }
        int ways = 0;
        ways += dpDfs(curStatus, curCol + 1, col, curRow, row, dp);
        if (curCol + 1 < col && (curStatus & (3 << curCol)) == 0) {
            ways += dpDfs(curStatus | (3 << curCol), curCol + 2, col, curRow, row, dp);
        }
        return ways;
    }

    public static void main(String[] args) {
        int n = 5;
        int m = 6;
        System.out.println(violent(n, m));
        System.out.println(dp(n, m));
    }
}
