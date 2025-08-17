package tixiban.class15monotonousstack;

import java.util.Stack;

/**
 * 给定一个二维数组，其中的值不是0就是1，返回由1组成的最大子矩形有多少个1
 *
 * 思路：
 * 求以第0行为底的子矩形，第1行为底的子矩形...
 * 以每一行为底的最大子矩形大小怎么求？压缩成一个数组，求直方图最大矩形面积。
 */
public class Code4AllOneMatrixMaxArea {
    public static int maxArea(int[][] matrix) {
        // 存结果
        int max = 0;
        // 依次把第0行，第0～1行，第0～2行...压缩成一个数组，每一行压缩数组当作直方图去求最大矩形面积
        int[] height = new int[matrix[0].length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                // 压缩数组，如果当前行某个位置为0，那么最大子矩阵肯定不会以这个位置为底，直接整个赋成0。如果不是0就累加上之前行的1的个数
                height[j] = matrix[i][j] == 0 ? 0 : height[j] + matrix[i][j];
            }
            max = Math.max(max, maxAreaMonotonousStack(height));
        }

        return max;
    }

    // 求直方图最大矩形面积
    public static int maxAreaMonotonousStack(int[] arr) {
        int max = 0;
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < arr.length; i++) {
            while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
                Integer index = stack.pop();
                max = Math.max(max, (stack.isEmpty() ? i : i - stack.peek() - 1) * arr[index]);
            }
            stack.push(i);
        }

        while (!stack.isEmpty()) {
            Integer index = stack.pop();
            max = Math.max(max, (stack.isEmpty() ? arr.length : arr.length - stack.peek() - 1) * arr[index]);
        }

        return max;
    }

    public static void main(String[] args) {
        int[][] matrix={
                {1,1,1,1,1},
                {1,1,1,0,1},
                {0,1,0,1,1}
        };

        System.out.println(maxArea(matrix));
    }
}
