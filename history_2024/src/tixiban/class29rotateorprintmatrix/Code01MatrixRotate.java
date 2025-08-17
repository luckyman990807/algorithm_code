package tixiban.class29rotateorprintmatrix;

import java.util.Arrays;

/**
 * 给定一个正方形矩阵,返回顺时针旋转90度的结果,例如给定
 * a b c
 * d e f
 * h i j
 * 返回
 * h d a
 * i e b
 * j f c
 *
 * 思路:矩阵分圈
 * 如上面的例子,最外层的一圈是
 * a b c
 * d   f
 * h i j
 * 这一圈可以分为2组,第一组为
 * a   c
 *
 * h   j
 * 旋转,四个位置互相占位,a来到c,c来到j...
 * 第二组为
 *   b
 * d   f
 *   i
 * 旋转,四个位置互相占位,b来到f,f来到i...
 * 这一圈都旋转完了来到下一圈,只有一个e,不需要动.
 */
public class Code01MatrixRotate {
    public static void rotateEdge(int[][] m, int x1, int y1, int x2, int y2) {
        // (x1,y1)左上角和(x2,y2)右下角所划定的层,一共有x2-x1组
        for (int group = 0; group < x2 - x1; group++) {
            // 每组的四个点,依次占位
            int temp = m[x1][y1 + group];
            m[x1][y1 + group] = m[x2 - group][y1];
            m[x2 - group][y1] = m[x2][y2 - group];
            m[x2][y2 - group] = m[x1 + group][y2];
            m[x1 + group][y2] = temp;
        }
    }

    public static void rotateMatrix(int[][] m) {
        // (x1,y1)左上角
        int x1 = 0;
        int y1 = 0;
        // (x2,y2)右下角
        int x2 = m.length - 1;
        int y2 = m[x2].length - 1;
        // 旋转左上和右下所在的层,然后左上往右下推,右下往左上推,直到两个点相遇或者错开
        while (x1 < x2) {
            rotateEdge(m, x1++, y1++, x2--, y2--);
        }
    }

    public static void main(String[] args) {
        int[][] m = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        rotateMatrix(m);
        for (int[] arr : m) {
            System.out.println(Arrays.toString(arr));
        }

        int[][] m1 = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };
        rotateMatrix(m1);
        for (int[] arr : m1) {
            System.out.println(Arrays.toString(arr));
        }
    }
}
