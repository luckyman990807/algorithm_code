package shuatiban;

/**
 * 给定一个矩阵matrix，值有正、负、0。蛇可以空降到最左列的任何一个位置，初始增长值是0。蛇每一步可以选择右上、右、右下三个方向的任何一个前进
 * 沿途的数字累加起来，作为增长值；但是蛇一旦增长值为负数，就会死去。蛇有一种能力，可以使用一次：把某个格子里的数变成相反数
 * 蛇可以走到任何格子的时候停止，返回蛇能获得的最大增长值
 *
 *
 * 思路:
 *
 * 可以等效理解为:
 * 蛇可以空降到任意一个位置,每一步可以任意选择左、左上、左下三个方向移动,只要移动到最左列就停止,每到一个格子就吃掉成长值,有一次将当前格子的成长值变为相反数的能力,
 * 求最佳空降法能获得的最大成长值
 *
 * 动态规划,试法是:f(i,j)表示从(i,j)走到最左列最大可以获得多少成长值.
 */
public class Code047蛇走矩阵的成长值 {
    /**
     * 对每个格子用递归求一下f(i,j)算出从这个格子走到最左列的最大成长值,返回所有结果中最大的那个
     */
    public static int snake(int[][] matrix) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                Info info = process(matrix, i, j);
                max = Math.max(max, Math.max(info.unused, info.used));
            }
        }
        return max;
    }

    /**
     * 递归试法,蛇从(i,j)走到最左列,返回不用能力和只用一次能力的分别得分
     */
    public static Info process(int[][] matrix, int i, int j) {
        // 递归出口,如果当前位置越界了,返回无效值
        if (i < 0 || j < 0 || i >= matrix.length || j >= matrix[i].length) {
            return new Info(Integer.MIN_VALUE, Integer.MIN_VALUE);
        }
        // 递归出口,如果当前走到了最左列,那么此时不用能力的话就获得当前成长值,用能力的话就获得当前成长值的相反数
        if (j == 0) {
            return new Info(matrix[i][j], -matrix[i][j]);
        }
        // 一般位置

        // 下一个位置有3种可能性:1、往左边走,2、往左上走,3、往左下走
        Info left = process(matrix, i, j - 1);
        Info leftUp = process(matrix, i - 1, j - 1);
        Info leftDown = process(matrix, i + 1, j - 1);
        // 从下一个位置走到最左列,不用能力和用一次能力分别的最大成长值,
        int preUnused = Math.max(left.unused, Math.max(leftUp.unused, leftDown.unused));
        int preUsed = Math.max(left.used, Math.max(leftUp.used, leftDown.used));
        // 整合出从当前位置走到最左列的最大成长值
        // 从当前位置走到最左列不用能力,即当前位置不用能力,下个位置到左左列也不用能力
        int unused = matrix[i][j] + preUnused;
        // 从当前位置走到最左列不用能力,即max(当前位置不用能力,下个位置到最左列用一次能力, 当前位置用一次能力,下个位置到最左列不用能力)
        int used = Math.max(matrix[i][j] + preUsed, -matrix[i][j] + preUnused);

        return new Info(unused, used);
    }

    public static class Info {
        int unused;
        int used;

        public Info(int unused, int used) {
            this.unused = unused;
            this.used = used;
        }
    }

    public static void main(String[] args) {
        int[][] matrix = {
                {1, 2, 300},
                {1, -100, -100},
                {4, -500, 6}
        };
        System.out.println(snake(matrix));
        System.out.println(dp(matrix));
    }

    /**
     * 改动态规划
     */
    public static int dp(int[][] matrix) {
        // 记录最终答案
        int max = Integer.MIN_VALUE;

        // 根据递归出口(到了最左列)赋初值
        int[][][] dp = new int[matrix.length][matrix[0].length][2];
        for (int row = 0; row < matrix.length; row++) {
            dp[row][0][0] = matrix[row][0];
            dp[row][0][1] = -matrix[row][0];
        }

        // 因为每个位置依赖左侧、左上、左下,所以从最左列往右一列一列填
        for (int col = 1; col < matrix[0].length; col++) {
            for (int row = 0; row < matrix.length; row++) {
                // 求出所有可能的下个位置 走到最左列的最大成长值
                int afterUnused = dp[row][col - 1][0];
                int afterUsed = dp[row][col - 1][1];
                if (row > 0) {
                    afterUnused = Math.max(afterUnused, dp[row - 1][col - 1][0]);
                    afterUsed = Math.max(afterUsed, dp[row - 1][col - 1][1]);
                }
                if (row < matrix.length - 1) {
                    afterUnused = Math.max(afterUnused, dp[row + 1][col - 1][0]);
                    afterUsed = Math.max(afterUsed, dp[row + 1][col - 1][1]);
                }
                // 整合当前位置的答案
                dp[row][col][0] = matrix[row][col] + afterUnused;
                dp[row][col][1] = Math.max(matrix[row][col] + afterUsed, -matrix[row][col] + afterUnused);
                // 更新最终答案
                max = Math.max(max, Math.max(dp[row][col][0], dp[row][col][1]));
            }
        }
        return max;
    }
}
