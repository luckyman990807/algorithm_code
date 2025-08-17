package shuatiban;

import java.util.Arrays;

/**
 * https://leetcode.com/problems/closest-subsequence-sum/
 * 给定整数数组nums和目标值goal，需要从nums中选出一个子序列，使子序列元素总和最接近goal
 * 也就是说如果子序列元素和为sum ，需要最小化绝对差abs(sum - goal)，返回 abs(sum - goal)可能的最小值
 * 注意数组的子序列是通过移除原始数组中的某些元素（可能全部或无）而形成的数组.
 *
 * 本题数据量描述:
 * 1 <= nums.length <= 40
 * -10^7 <= nums[i] <= 10^7
 * -10^9 <= goal <= 10^9
 * 通过这个数据量描述可知，需要用到分治，因为数组长度不大(不分治直接按长度暴力递归会超时,分治后按长度暴力递归不会超时)
 * 而值很大，用动态规划的话，表会爆
 *
 * 思路:
 * 把数组分为左右两半,左右分别最多20个,分别求出所有可能的子序列累加和,分别最多2^20个(每个位置留/不留两种选择,一共最多20个位置)
 * 然后遍历出左边累加和最接近goal的、右边累加和最接近goal的、左边+右边最接近goal的,返回
 *
 */
public class Code020最接近目标的子序列累加和 {
    public static int minAbsDifference(int[] nums, int goal) {
        // 把nums均分为左右两边,leftAllSum和rightAllSum分别存储左右两边所有的累加和.因为nums长度最多40,所以一边最多20,所有累加和的数量最多2^20
        int[] leftAllSum = new int[1 << ((nums.length + 1) >> 1)];
        int[] rightAllSum = new int[leftAllSum.length];

        // 把nums左右两边的所有子序列的累加和分别写入leftAllSum和rightAllSum,返回写入的最后一个位置的下一个位置,-1得到写入的最后一个位置
        int leftEnd = getAllSum(nums, 0, (nums.length >> 1) - 1, leftAllSum, 0, 0) -1;
        int rightEnd = getAllSum(nums, nums.length >> 1, nums.length - 1, rightAllSum, 0, 0)-1;

        // 两个累加和数组排序
        Arrays.sort(leftAllSum, 0, leftEnd);
        Arrays.sort(rightAllSum, 0, rightEnd);

        int min = Integer.MAX_VALUE;
        // 遍历所有左边的累加和,从右边累加和里找一个跟左边配对.因为左右累加和里都有0,所以可以兼顾到只有左边或只有右边的情况
        for (int i = 0; i <= leftEnd; i++) {
            int rest = goal - leftAllSum[i];
            // rightEnd充当右指针,右指针可以不回退,因为要凑的累加和不变,左边变大右边一定要变小
            while (rightEnd > 0 && Math.abs(rest - rightAllSum[rightEnd - 1]) <= Math.abs(rest - rightAllSum[rightEnd])) {
                rightEnd--;
            }
            min = Math.min(min, Math.abs(rest - rightAllSum[rightEnd]));
        }
        return min;
    }

    /**
     * 暴力递归计算原数组sourceArr从index到end所有子序列的累加和,存到目标数组targetArr里,返回目标数组的有效长度(也就是最后一个插入的位置的下一个位置)
     * 实际上就是二叉树的树形dp,左右分支就是加index位置和不加index位置
     * @param targetIndex 目标数组插入到哪个位置了
     * @param curSum index之前已经积累的累加和
     */
    public static int getAllSum(int[] sourceArr, int index, int end, int[] targetArr, int targetIndex, int curSum) {
        if (index > end) {
            // 给定的范围遍历完了,相当于树形dp走到叶子结点了,就记录结果
            targetArr[targetIndex++] = curSum;
        } else {
            // 没走完,就分别向左右孩子索要信息,同时因为所有递归过程都在向同一个targetArr插入数,所以每次递归要接收更新后的targetIndex
            // 分支一:累加和选择加上index位置的数,然后递归到下一个位置,相当于递归到左孩子节点
            targetIndex = getAllSum(sourceArr, index + 1, end, targetArr, targetIndex, curSum + sourceArr[index]);
            // 分支二:累加和选择不加index位置的数
            targetIndex = getAllSum(sourceArr, index + 1, end, targetArr, targetIndex, curSum);
        }
        // 返回更新后的targetIndex
        return targetIndex;
    }

    public static void main(String[] args) {
        int[] nums = {4, 5, 8, 13, 14};
        int goal = 10;
        System.out.println(minAbsDifference(nums, goal));
    }
}
