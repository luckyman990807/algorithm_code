package shuatiban;

/**
 * 给定一个二维数组matrix,可以从任意一个位置出发,每次可以沿上/下/左/右走一步.问经过的数组值组成的递增链最长长度是多少.
 */
public class Code006上下左右走出的最长递增链 {
    /**
     * 暴力递归
     * 试法:在不越界的情况下,分别尝试4种可能性: 往上走、往下走、往左走、往右走. 4种结果取最大值
     * @param matrix
     * @param row
     * @param col
     * @return
     */
    public static int process(int[][] matrix, int row, int col) {
        // 这里递归前已经做了判断,所以不需要递归出口的判断了
        // 当前位置自身长度1,加上从下一个位置开始的长度
        int up = row > 0 && matrix[row - 1][col] > matrix[row][col] ? 1 + process(matrix, row - 1, col) : 1;
        int down = row < matrix.length - 1 && matrix[row + 1][col] > matrix[row][col] ? 1 + process(matrix, row + 1, col) : 1;
        int left = col > 0 && matrix[row][col - 1] > matrix[row][col] ? 1 + process(matrix, row, col - 1) : 1;
        int right = col < matrix[row].length - 1 && matrix[row][col + 1] > matrix[row][col] ? 1 + process(matrix, row, col + 1) : 1;

        return Math.max(Math.max(up, down), Math.max(left, right));
    }

    public static int violent(int[][] matrix) {
        int max = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                max = Math.max(max, process(matrix, i, j));
            }
        }
        return max;
    }

    /**
     * 改记忆化搜索
     */
    public static int processDp(int[][] matrix, int row, int col, int[][] dp) {
        // 先查缓存表
        // 因为递增链长度最短是1,即只有一个数的情况.所以==0就可以认为没写过缓存
        if (dp[row][col] != 0) {
            return dp[row][col];
        }

        // 这里递归前已经做了判断,所以不需要递归出口的判断了
        int up = row > 0 && matrix[row - 1][col] > matrix[row][col] ? 1 + process(matrix, row - 1, col) : 1;
        int down = row < matrix.length - 1 && matrix[row + 1][col] > matrix[row][col] ? 1 + process(matrix, row + 1, col) : 1;
        int left = col > 0 && matrix[row][col - 1] > matrix[row][col] ? 1 + process(matrix, row, col - 1) : 1;
        int right = col < matrix[row].length - 1 && matrix[row][col + 1] > matrix[row][col] ? 1 + process(matrix, row, col + 1) : 1;

        dp[row][col] = Math.max(Math.max(up, down), Math.max(left, right));
        return dp[row][col];
    }

    public static int violentDp(int[][] matrix) {
        int max = 0;
        int[][] dp = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                max = Math.max(max, processDp(matrix, i, j, dp));
            }
        }
        return max;
    }


    public static void main(String[] args) {
        int[][] matrix = {
                {1, 2, 3},
                {8, 5, 4},
                {0, 0, 0}
        };
        System.out.println(violent(matrix));
        System.out.println(violentDp(matrix));
    }

}
