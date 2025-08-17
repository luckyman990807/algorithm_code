package shuatiban;

import java.util.Scanner;

/**
 * 给定一个矩阵matrix，先从左上角开始，每一步只能往右或者往下走，走到右下角。然后从右下角出发，每一步只能往上或者往左走，再回到左上角。任何一个位置的数字，只能获得一遍。返回最大路径和。
 * 输入描述:
 * 第一行输入两个整数M和N，M,N<=200
 * 接下来M行，每行N个整数，表示矩阵中元素
 * 输出描述:
 * 输出一个整数，表示最大路径和
 * 牛客网题目：https://www.nowcoder.com/questionTerminal/8ecfe02124674e908b2aae65aad4efdf
 *
 * 思路：如何处理一来一回？
 * 解法：转换成两个人同时走，都从左上走到右下，这样其中一个人的路径就是回来的路径
 */
public class Code093来回的最大路径和 {

    public static void mainViolent(String[] args) {
        Scanner in = new Scanner(System.in);
        int rows = in.nextInt();
        int cols = in.nextInt();
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = in.nextInt();
            }
        }
        System.out.println(processViolent(matrix, 0, 0, 0, 0));
    }

    /**
     * 递归试法，两个人分别从矩阵的(r1,c1)(r2,c2)出发，走到矩阵的右下角，返回经过的最大路径和，如果两人某一时刻走到同一个点，那么路径只算一次
     * 递归出口：是两个人都走到右下角了，就直接返回右下角的路径值
     * 一般情况：计算当前的路径和，再加上递归求的后续路径和，返回。后续路径和，因为有两个人，每个人有右和下两种走法，所以有4种可能性
     *
     * 是对的解法，但是会超时
     */
    public static int processViolent(int[][] matrix, int r1, int c1, int r2, int c2) {
        if (r1 == matrix.length - 1 && c1 == matrix[0].length - 1) {
            return matrix[r1][c1];
        }
        int cur = r1 == r2 && c1 == c2 ? matrix[r1][c1] : matrix[r1][c1] + matrix[r2][c2];
        int next = Integer.MIN_VALUE;
        if (r1 < matrix.length - 1) {
            if (r2 < matrix.length - 1) {
                next = Math.max(next, processViolent(matrix, r1 + 1, c1, r2 + 1, c2));
            }
            if (c2 < matrix[0].length - 1) {
                next = Math.max(next, processViolent(matrix, r1 + 1, c1, r2, c2 + 1));
            }
        }
        if (c1 < matrix[0].length - 1) {
            if (r2 < matrix.length - 1) {
                next = Math.max(next, processViolent(matrix, r1, c1 + 1, r2 + 1, c2));
            }
            if (c2 < matrix[0].length - 1) {
                next = Math.max(next, processViolent(matrix, r1, c1 + 1, r2, c2 + 1));
            }
        }
        return cur + next;
    }



    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int rows = in.nextInt();
        int cols = in.nextInt();
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = in.nextInt();
            }
        }
        int[][][] dp = new int[rows][cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < rows; k++) {
                    dp[i][j][k] = -1;
                }
            }
        }
        System.out.println(processDp(matrix, 0, 0, 0, dp));
    }

    /**
     * 递归+缓存法，可变参数减少到3个，因为r1+c1必然等于r2+c2（两个人分别的步数和，因为同步走，所以步数和必然相同），所以c2 = r1 + c1 - r2
     */
    public static int processDp(int[][] matrix, int r1, int c1, int r2, int[][][] dp) {
        if (dp[r1][c1][r2] != -1) {
            return dp[r1][c1][r2];
        }
        int c2 = r1 + c1 - r2;

        if (r1 == matrix.length - 1 && c1 == matrix[0].length - 1) {
            dp[r1][c1][r2] = matrix[r1][c1];
            return dp[r1][c1][r2];
        }

        int cur = r1 == r2 && c1 == c2 ? matrix[r1][c1] : matrix[r1][c1] + matrix[r2][c2];
        int next = Integer.MIN_VALUE;
        if (r1 < matrix.length - 1) {
            if (r2 < matrix.length - 1) {
                next = Math.max(next, processDp(matrix, r1 + 1, c1, r2 + 1, dp));
            }
            if (c2 < matrix[0].length - 1) {
                next = Math.max(next, processDp(matrix, r1 + 1, c1, r2, dp));
            }
        }
        if (c1 < matrix[0].length - 1) {
            if (r2 < matrix.length - 1) {
                next = Math.max(next, processDp(matrix, r1, c1 + 1, r2 + 1, dp));
            }
            if (c2 < matrix[0].length - 1) {
                next = Math.max(next, processDp(matrix, r1, c1 + 1, r2, dp));
            }
        }
        dp[r1][c1][r2] = cur + next;
        return dp[r1][c1][r2];
    }
}
