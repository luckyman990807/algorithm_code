package shuatiban;

/**
 * https://leetcode.com/problems/first-missing-positive/
 * 给你一个未排序的整数数组 nums ，请你找出其中没有出现的最小的正整数。请你实现时间复杂度为 O(n) 并且只使用常数级别额外空间的解决方案。
 *
 * 思路：
 * 从左往右把每个位置的数都换成这个位置应该有的正整数（0下标放1，1下标放2...）直到哪个位置无法放置对应的正整数，那么这个正整数就是缺失的最小正整数。
 * 问：如何把每个位置的数都换成这个位置应该有的正整数？
 * 答：1、维护两个区域：有效区和无效区。2、不断缩小目标范围。
 */
public class Code072找出没有出现的最小的正整数 {
    public static int firstMissingPositive(int[] nums) {
        // left代表两层意思：1、从left-1开始往左就是有效区。2、当前正在处理left位置
        int left = 0;
        // right代表两层意思：1、从right开始往右就是无效区。2、缺失的正整数最大的情况就是0～length-1放置了1～length，缺失的是length+1，也就是right+1
        int right = nums.length;

        while (left < right) {
            if(nums[left] == left + 1){
                // 如果left位置已经放置了他该放的数，就把这个位置纳入有效区，left++
                left++;
            } else if (nums[left] <= left || nums[left] > right || nums[nums[left] - 1] == nums[left]) {
                // 当前left到right-1的区间内，最多能放下left+1到right这些正整数
                // 如果left位置的数不属于[left+1, right]，说明被无效的数占了一个位置，把他移到无效区，同时因为无效区左扩，能放置正整数的位置就少了一个，现在最多只能放下left+1到right-1这些正整数了
                // 注：如果left位置的数属于预期区间[left+1, right]，但是这个数该去的位置已经有了同样的数，那么这个数也没用了，也移入无效区
                swap(nums, left, --right);
            } else {
                // 否则这个数就要移到他该待的位置
                swap(nums, left, nums[left] - 1);
            }
        }
        return right + 1;
    }

    public static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
