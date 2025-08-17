package shuatiban;

/**
 * https://leetcode.cn/problems/patching-array/
 * 给定一个已排序的正整数数组 nums ，和一个正整数 n 。从 [1, n] 区间内选取任意个数字补充到 nums 中，使得 [1, n] 区间内的任何数字都可以用 nums 中某几个数字的和来表示。
 * 请返回 满足上述要求的最少需要补充的数字个数 。
 *
 * 简述：往arr里面补数，使得1到n之间到任意数都能由arr子集累加得到，返回最少补几个数。
 *
 * 思路：定义一个range，表示从1到range之间的任意数都能由arr子集累加得到，range初始值=1。
 * （本题给的就是有序数组，如果无序就先排序）遍历数组，arr[i]如果<=range+1，那么就把arr[i]直接扩充到range上，扩充的方式是range+=arr[i]；
 * 如果arr[i]>range+1，那么就要先补数以使得从1到arr[i]-1之间的任意数都能由arr子集累加得到，也就是先让range扩充到arr[i]-1。扩充的方式是range+=range+1，也就是补range+1这个数。
 * 每次补数都记录补数次数，作为返回结果。每次range扩充，都判断一下range是否超过了n，如果超过了可以立即返回结果。
 */
public class Code083按要求补齐数组 {
    public int minPatches(int[] arr, int n) {
        int patches = 0;
        // range用long类型，防止n太大，扩充range时int溢出导致结果有误
        long range = 0;
        // 遍历arr，如果arr[i]>range+1，就不断补range+1，一直补到arr[i]<=range+1；如果arr[i]<=range+1，就把arr[i]直接扩充到range上
        for (int num : arr) {
            while (num > range + 1) {
                range += range + 1;
                patches++;
                if (range >= n) {
                    return patches;
                }
            }
            range += num;
            if (range >= n) {
                return patches;
            }
        }
        // 如果遍历完了arr，range还没达到n，就一直补range+1
        while (range < n) {
            range += range + 1;
            patches++;
        }
        return patches;
    }
}
