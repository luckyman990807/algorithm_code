package tixiban.class16MatrixFastPower;

import java.util.Arrays;

public class Code0快速幂和笔记 {
    /**
     * 快速幂
     * 快速求x的n次幂，时间复杂度O(logN)
     * 把n转成二进制，例如1010。让x每次乘以自己，得到x^1, x^2, x^4, x^8, x^16, x^32...。准备一个结果=1，不断往上乘东西。
     * 对于二进制n，从低位往高位，第一位如果=1，就往结果上乘x^1，否则不乘。第二位如果是1，就往结果上乘x^2，否则不乘。第三位如果=1，就往结果上乘x^4，否则不乘...
     * 直到遍历完n上所有的1，得到最后的乘积就是x的n次幂
     *
     * @param x 底数
     * @param n 指数
     * @return
     */
    public static int fastPower(int x, int n) {
        int result = 1;
        while (n > 0) {
            // (n & 1) == 1表示n最后一位是1
            if ((n & 1) == 1) {
                result *= x;
            }
            // n右移一位抹掉刚才的最后一位
            n >>= 1;
            // x每次乘自己，x^1, x^2, x^4, x^8, x^16, x^32...
            x *= x;
        }
        return result;
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

    /**
     * 矩阵快速幂用于求解递推式
     *
     * 如果递推公式：f(n)=f(n-1)+f(n-2)，
     * 那么一定满足：第n项和第n-1项组成的行列式 = 第n-1项和第n-2项组成的行列式 * 某个2维矩阵，即
     * |f(n), f(n-1)| = |f(n-1), f(n-2)| * |A|
     *
     * 同理|f(n-1), f(n-2)| = |f(n-2), f(n-3)| * |A|，可以一直列下去....
     * 依次把下式带入上式得：
     * |f(n), f(n-1)| = |f(2), f(1)| * |A|^n-2
     * 即：第n项和第n-1项组成的行列式 = 第2项和第1项组成的行列式 * 某个2维矩阵的n-2次方
     * 把递推式转换成了矩阵的幂。只要矩阵幂求的快，递推式就求的快。
     *
     *
     * 如何求这个2维矩阵？
     *                                     |a b|
     * |f(n), f(n-1)| = |f(n-1), f(n-2)| * |c d|
     * 展开可得|f(n), f(n-1)| = |a*f(n-1)+c*f(n-2), b*f(n-1)+d*f(n-2)|
     *
     * 两个行列式相等，必然每个元素相等，即
     * f(n) = a*f(n-1) + c*f(n-2)       1式
     * f(n-1) = b*f(n-1) + d*f(n-2)     2式
     *
     * 把递推公式带入1式得a=1，c=1
     * 把递推公式带入2式得f(n) - f(n-2) = b*f(n-1) + d*f(n-2)，得f(n) = b*f(n-1) + (d+1)*f(n-2)，得b=1，d=0
     *
     *            |1 1|
     * 所以2维矩阵为|1 0|
     *
     *
     *
     * 如何求f(n)？
     * 矩阵|A|有了，矩阵快速幂可以求出|A|^n-2，f(1)和f(2)可以根据递推公式算出或者题目给出，那么
     * |f(n), f(n-1)| = |f(2), f(1)| * |A|^n-2
     * f(n)可求
     *
     */


    /**
     * 矩阵快速幂求解递推式推广
     *
     * 只要是严格递推式（就一个公式，不会说某一条件下是这个公式，另一条件下是另一个公式）都可以利用矩阵快速幂优化成O(logN)
     *
     * 例如f(n) = a*f(n-1) + b*f(n-2) + ... + k*f(n-m)，（n阶递推式，中间的系数可以=0）那么一定满足：
     * |f(n), f(n-1)...f(n-m+1)| = |f(n-1), f(n-2)...f(n-m)| * |A|      3式
     * 多列几个2式，依次把下式带入上式得：
     * |f(n),f(n-1)...f(n-m+1)| = |f(m),f(m-1)...f(1)| * |A|^n-m        4式
     * |A|是m阶矩阵
     *
     * 把3式右边乘开，根据行列式相等得到m个等式，分别把递推公式带入可求|A|的每一个元素。
     * 把|A|、前m项带入4式可求f(n)
     *
     * m阶递推式，就要带入前m项，矩阵就是m阶，矩阵的次方就是n-m次。
     */



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

    public static void main(String[] args) {
        System.out.println(fastPower(2, 10));

        int[][] m = {
                {1, 1, 0},
                {1, 0, 1},
                {1, 1, 1}
        };
        int[][] power = matrixFastPower(m, 3);
        for (int i = 0; i < power.length; i++) {
            System.out.println(Arrays.toString(power[i]));
        }
    }
}
