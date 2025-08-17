package tixiban.class16MatrixFastPower;

/**
 * 给定一个数n，想象只由0/1组成的所有长度为n的字符串
 * 如果某个字符串，任意0字符左边都有1紧挨着，则认为这个字符串达标
 * 返回达标字符串的个数
 * <p>
 * 例如
 * n=1时，0不达标，1达标，返回1
 * n=2时，00不达标，01不达标，10达标，11达标，返回2
 * n=3时，000不达标，001不达标，010不达标，011不达标，100不达标，101达标，110达标，111达标，返回3
 * <p>
 * 思路
 * 1、观察法
 * 多列几项观察可知是第1项为1、第2项为2的斐波那契数列，矩阵快速幂O(logN)可求
 * 2、尝试法
 * 递归函数f(i)含义是剩余i位没填，前一位是1的前提下，剩下i位有几种达标的填法（因为完整字符串第一位只能是1）
 * 可能性1、第1位取1，那么接下来就要计算f(i-1)，即前一位是1的前提下，剩余i-1位有几种达标的填法
 * 可能性2、第1位取0，那么第2位必然取1，取0的话第2位0左边没有1不达标。那么接下来就要计算f(i-2)，即前一位是1的前提下，剩余i-2位有几种达标的填法
 * 即 f(i)=f(i-1)+f(i-2)，斐波那契数列 1，2，3，5，8，13...
 */
public class Code03StandardString {
    public static int standards(int n) {
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        // 递推公式和斐波那契一样，状态矩阵就一样
        int[][] matrix = {{1, 1}, {1, 0}};
        matrix = matrixFastPower(matrix, n - 2);
        // 前两项是1、2
        return matrix[0][0] + 2 * matrix[1][0];
    }

    // 矩阵快速幂，逻辑和整数快速幂一样，只不过单位1换成列单位矩阵，相乘变成了矩阵相乘
    public static int[][] matrixFastPower(int[][] matrix, int n) {
        // 矩阵中的「1」是单位矩阵，对角线都是1
        int[][] result = new int[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            result[i][i] = 1;
        }

        while (n > 0) {
            if ((n & 1) == 1) {
                result = matrixMultiply(result, matrix);
            }
            n >>= 1;
            matrix = matrixMultiply(matrix, matrix);
        }

        return result;
    }

    // 矩阵相乘，m1第i行和m2第i列每个元素对应相乘累加，就是result[i][j]的结果
    // 要求m1的行数==m2的列数,m1的列数==m2的行数
    public static int[][] matrixMultiply(int[][] m1, int[][] m2) {
        // m1的行数
        int n = m1.length;
        // m1的列数
        int m = m1[0].length;

        int[][] result = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < m; k++) {
                    result[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        return result;
    }
}
