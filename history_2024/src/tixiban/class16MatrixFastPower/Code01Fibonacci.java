package tixiban.class16MatrixFastPower;

/**
 * 斐波那契数列，f(1)=1,f(2)=1，后面每一项都等于前面两项的和。求第n项
 * <p>
 * 思路：
 * 暴力递归O(logN)，矩阵快速幂O(logN)
 * f(n) = f(n-1) + f(n-2)                       递推公式
 * |f(n),f(n-1)| = |f(n-1),f(n-2)| * |A|        1式
 * ...
 * |f(n),f(n-1)| = |f(2),f(1)| * |A|^n-2        2式
 * |a b|                               |1 1|
 * |A|是2阶矩阵|c d|，把1式右边乘开，把递推公式带入得|A|=|1 0|
 * 先求A的矩阵快速幂，再带入f(1)=1 f(2)=1可求f(n)
 */
public class Code01Fibonacci {
    public static int fibonacci(int n) {
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 1;
        }
        int[][] matrix = {{1, 1}, {1, 0}};
        matrix = matrixFastPower(matrix, n - 2);
        return matrix[0][0] + matrix[1][0];
    }

    public static void main(String[] args) {
        System.out.println(fibonacci(10));
    }

    /**
     * 矩阵快速幂，逻辑和整数快速幂一样，只不过单位1换成列单位矩阵，相乘变成了矩阵相乘
     */
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
