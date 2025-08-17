package shuatiban;

/**
 * 给定一个每一行有序、每一列也有序，整体可能无序的二维数组，再给定一个数num，返回二维数组中有没有num这个数
 * 水题，但常考。
 *
 * 思路：
 * 无脑解：直接遍历矩阵，复杂度n*m
 * 最优解：充分利用每行、每列的单调性，从右上角出发，
 * 如果当前数大于num，那么下方所有数都大于num，下方所有数都淘汰，往左走；
 * 如果当前数小于num，那么左边所有数都小于num，左边所有数都淘汰，往下走；
 * 直到越界或者找到num
 *
 * 从左下角出发也行，也能充分利用行列的单调性，不再赘述。
 */
public class Code086整数矩阵每一行有序每一列有序_给定num返回矩阵中是否存在num {
    public static boolean contains(int[][] matrix, int num) {
        int i = 0;
        int j = matrix[0].length - 1;
        while (i <= matrix.length - 1 && j >= 0) {
            if (matrix[i][j] > num) {
                j--;
            } else if (matrix[i][j] < num) {
                i++;
            } else {
                return true;
            }
        }
        return false;
    }
}
