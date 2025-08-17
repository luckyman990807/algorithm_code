package tixiban.class16MatrixFastPower;

/**
 * 地面长度是n，宽度是2，瓷砖长度是2，宽度是1，问用瓷砖铺满地面有几种铺法
 *
 * 例如
 * n=1，只有一种铺法，返回1
 * n=2，横着两块或竖着两块，返回2
 * n=3，三块竖着摆，或 左边一块竖着右边两块横着，或 左边两块横着右边一块竖着，返回3
 *
 * 思路
 * 尝试法
 * 递归函数f(i)，含义是剩余i长度没铺（完整的i*2区域），计算剩余从i长度有几种铺法
 * 可能性1、第一块竖着铺，那么接下来就要计算f(i-1)，也就是剩余i-1长度没铺，计算有几种铺法
 * 可能性2、第一块横着铺，那么第二块也只能横着铺，接下来就要算f(i-2)，也就是剩余i-2长度没铺，计算有几种铺法
 * 即 f(i)=f(i-1)+f(i-2)，又是斐波那契，前两项是1、2，跟上一道题达标字符串一摸一样。
 */
public class Code04Cover2XNFloorWith2X1Tile {
    public static int cover(int n) {
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
