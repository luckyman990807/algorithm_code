package shuatiban;

/**
 * 给定一个每一行有序、每一列也有序，整体可能无序的二维数组，再给定一个正数k，返回二维数组中第k小的数
 * Leetcode题目：https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/
 *
 * 思路：
 * 首先一个概念：如果矩阵中小于等于500的数有100个，并且小于等于500的最接近的数是490，那么490就是第100小的数。
 * 因此只要找到矩阵中小于等于几的数有k个，k个中最接近这个「几」的数就是第k小的数。
 * 因此需要想一个方法，技能求小于等于某个数的个数，又能求小于等于这个数最接近的数是谁，这个方法的原理和上一道题接近，充分利用行列的单调性，从右上角（或左下角）遍历
 * 有了这个方法之后，就可以在最小值（左上角）和最大值（右上角）之间二分，找到恰好有k个数小于等于他的那个数，返回最接近的那个数就是答案
 */
public class Code087从一个每一行有序每一列也有序的矩阵中找到第k小的数 {
    public static int kthSmallest(int[][] matrix, int k) {
        int left = matrix[0][0];
        int right = matrix[matrix.length - 1][matrix[0].length - 1];
        int ans = 0;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            Info info = lessEqual(matrix, mid);
            if (info.count < k) {
                left = mid + 1;
            } else {
                right = mid - 1;
                /** 注意，只有当info.count >= k时才能更新ans */
                // 因为如果小于等于当前数的有200个，我要找第100小的，那么这个数也有可能是答案，也就是第100到200个数是相等的；
                // 而如果小于等于当前数的有50个，我要找第100小的，那么这个数绝对不可能是答案，不能把它更新到ans
                ans = info.near;
            }
        }
        return ans;
    }

    public static Info lessEqual(int[][] matrix, int num) {
        int row = 0;
        int col = matrix[0].length - 1;
        int count = 0;
        int near = Integer.MIN_VALUE;
        while (row <= matrix.length - 1 && col >= 0) {
            if (matrix[row][col] <= num) {
                // 符合条件，把当前数及其左边的所有数都计入count，把当前数计入near
                count += col + 1;
                /** 如何找到小于等于num且离num最近的数near？ */
                // 先给near赋初始值Integer.MIN_VALUE，然后每当遇到小于等于num的数matrix[row][col]，就令near = Math.max(near, matrix[row][col])
                near = Math.max(near, matrix[row][col]);
                // 左边的数都计入了，不需要查看了，接下来要往下查看
                row++;
            } else {
                // 不符合条件，matrix[row][col]>num，那么下方的数都>num，不需要查看了，接下来往左查看
                col--;
            }
        }
        return new Info(count, near);
    }

    public static class Info {
        // 小于等于某个数的个数
        int count;
        // 小于等于某个数且最接近他的数是谁
        int near;

        public Info(int count, int near) {
            this.count = count;
            this.near = near;
        }
    }
}
