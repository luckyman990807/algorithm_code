package shuatiban;

/**
 * https://leetcode.com/problems/largest-1-bordered-square/
 * 给定一个只有0和1组成的二维数组，返回边框全是1（内部无所谓）的最大正方形面积
 * 数据量:二维数组长和宽都不超过100
 *
 * 思路:
 * 二维数组中遍历所有正方形的复杂度:O(N^3),因为任意一个正方形都可以由左上角顶点(N^2种可能)+边长(N种可能)唯一指定
 * 遍历所有正方形已经N^3了,而验证每个正方形是否符合条件似乎又要遍历.如果能O(1)验证每个正方形,就能把复杂度控制在O(N^3).由于数据量N不超过100,所以N^3不超过10^6,小于10^8就不会超时
 *
 * 如何O(1)验证每个正方形是否边框都是1?
 * 如果知道每个点包括自己在内右边有几个连续的1,包括自己在内下边有几个连续的1,
 * 那么只要 左上角顶点的右边下边连续1的个数、右上角顶点下边连续的1的个数、左下角顶点右边连续的1的个数 都大于等于当前边长,就说明这个正方形符合条件.
 *
 * 而每个点右边下边有几个连续的1,可以在遍历正方形之前求出来,记录在辅助数组中,遍历正方形时直接查.
 *
 * 技巧:面对很多元素每个元素都要遍历求一个结果的时候,就要想到前置求一个辅助数组,到时候每个元素可以直接用结果.
 * 前缀和数组就是这样的技巧.
 */
public class Code018边框全是1的最大正方形面积 {
    public static int maxArea(int[][] matrix) {
        // 构建辅助数组,rightOne[i][j]=k表示原二维数组i,j位置右边连同自己在内有k个连续的1
        int[][] rightOne = new int[matrix.length][matrix[0].length];
        for (int r = 0; r < rightOne.length; r++) {
            int count = 1;
            for (int c = rightOne[0].length - 1; c >= 0; c--) {
                if (matrix[r][c] == 1) {
                    rightOne[r][c] = count++;
                } else {
                    rightOne[r][c] = 0;
                    count = 1;
                }
            }
        }

        // 构建辅助数组,downOne[i][j]=k表示原二维数组i,j位置下边连同自己在内有k个连续的1
        int[][] downOne = new int[matrix.length][matrix[0].length];
        for (int c = 0; c < downOne[0].length; c++) {
            int count = 1;
            for (int r = downOne.length - 1; r >= 0; r--) {
                if (matrix[r][c] == 1) {
                    downOne[r][c] = count++;
                } else {
                    downOne[r][c] = 0;
                    count = 1;
                }
            }
        }

        // 遍历所有正方形,记录满足条件的最大正方形边长
        int max = Integer.MIN_VALUE;
        for (int r = 0; r < matrix.length; r++) {
            for (int c = 0; c < matrix[0].length; c++) {
                for (int b = 1; b < Math.min(matrix.length - r, matrix[0].length - c); b++) {
                    if (rightOne[r][c] >= b && downOne[r][c] >= b && downOne[r][c + b - 1] >= b && rightOne[r + b - 1][c] >= b) {
                        max = Math.max(max, b);
                    }
                }
            }
        }

        return max * max;
    }

    public static void main(String[] args) {
        int[][]matrix={
                {1,1,1,1},
                {1,0,1,1},
                {1,1,1,0},
                {0,1,1,1}
        };
        System.out.println(maxArea(matrix));
    }
}
