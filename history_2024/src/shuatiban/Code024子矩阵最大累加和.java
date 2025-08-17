package shuatiban;

/**
 * https://leetcode.cn/problems/max-submatrix-lcci/description/
 * 求一个二维数组中子矩阵最大累加和,返回一个4长度的数组,元素分别为:左上角行和列、右上角行和列.
 *
 * 思路:
 * 只要枚举出 以第0行为高的子矩阵最大累加和、以0,1行为高的子矩阵最大累加和、以0,1,2行为高的子矩阵最大累加和...以第1行为高的、以1,2行为高的...以第2行为高的、以第2,3行为高的...
 * 那么答案必在其中,找出累加和的最大的一个即可.
 *
 * 如何求以第某一行为高的子矩阵最大累加和? 这一行就是一个数组,求子数组最大累加和.
 * 关键优化:压缩数组法.如何求以第0,1行为高的子矩阵最大累加和? 两个数组,相同下标的元素累加在一起合成一个数组,求子数组最大累加和.
 *
 * 复杂度:
 * 假设二维数组是N*M的,遍历所有连续行的组合需要N^2,每次遍历求一次子数组最大累加和需要M(动态规划),所以整体复杂度O(N^2 * M)
 *
 * 另一个优化:
 * 旋转二维数组保证始终让行数比列数小,因为行要平方.这里没加,因为已经够快了.
 */
public class Code024子矩阵最大累加和 {
    public static int[] getMaxMatrix(int[][] matrix) {
        // 存放答案
        int[] result = new int[4];
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            // 存放压缩数组
            int[] help = new int[matrix[0].length];
            for (int j = i; j < matrix.length; j++) {
                int curSum = 0;
                // 用于记录累加和最大的子数组的起始下标
                int left = 0;
                for (int k = 0; k < help.length; k++) {
                    // 记录第k列压缩数组的值
                    help[k] += matrix[j][k];
                    // 顺便也可以在压缩数组的过程中尝试求子数组最大累加和
                    curSum += help[k];
                    if (curSum > max) {
                        max = curSum;
                        result[0] = i;
                        result[1] = left;
                        result[2] = j;
                        result[3] = k;
                    }
                    if (curSum < 0) {
                        curSum = 0;
                        // 如果以k结尾的最大子数组累加和小于0,那么最大子数组必然不包含以k结尾的子数组,所以起始下标设为k+1
                        left = k + 1;
                    }
                }
            }
        }
        return result;
    }
}
