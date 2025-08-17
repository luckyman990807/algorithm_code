package tixiban.class1_13.bucketsort;

/**
 * 基数排序是桶排序的一种，传统的桶排序是这样的：
 * 准备10个桶，最大值是十进制几位数，最外层就循环几次，
 * 第一次按照个位数排序，个位数是几就进到第几个桶，进完之后依次出桶，实现了个位数不同数值之间有序，相同数值之间维持原来的顺序，
 * 第二次按照十位数排序，一样的进桶出桶……
 * ……
 * 越高位在排序中的权重越高，所以越靠后排。排高位的时候，低位已有的顺序也能保留下来。
 * 桶可以是数组、栈等。
 * 这里采用的是优化版本，把10个桶节省到2个数组。
 * <p>
 * 基数排序的时间复杂度是O(N)
 * 严格讲是O(N*log(10,Max))，N*以10为底最大值的对数，因为最大值有几位就循环几次，每次都遍历。但是位数通常不会趋于无穷，所以log(10,Max)可以认为是常数
 * <p>
 * 基数排序额外空间复杂度是O(N)，因为只需要申请2个辅助数组
 */
public class RadixSort {
    public void radixSort(int[] arr) {
        if (null == arr || arr.length < 2) {
            return;
        }

        int maxBits = getMaxBits(arr);

        radixSort(arr, maxBits);

    }

    public void radixSort(int[] arr, int maxBits) {
        // 十进制位最大有几位，就循环进出桶几次
        for (int bit = 1; bit <= maxBits; bit++) {
            /**
             * 这里的桶也是抽象的，用一个count数组记录第bit位等于i的个数，相当于把第bit位==i的装进一个桶里
             * 为什么不用真的桶：为了节省空间，用真的桶要申请10个栈，而这里的方法只需要2个数组
             */
            int[] count = new int[10];
            // count最初记录的是，数组所有元素，第bit位是0的有几个，是1的有几个，……，是9的有几个
            for (int i = 0; i < arr.length; i++) {
                // 先算出arr[i]这个数十进制位第bit位是几，就在count下标是几++
                int digit = getDigit(arr[i], bit);
                count[digit]++;
            }
            // count现在记录的是，数组所有元素，第bit位小于等于0的有几个，小于等于1的有几个，……，小于等于9的有几个
            for (int i = 1; i < arr.length; i++) {
                count[i] += count[i - 1];
            }

            int[] help = new int[arr.length];
            // 数组从右往左遍历
            for (int i = arr.length - 1; i >= 0; i--) {
                // arr[i]放在help哪个位置？
                // arr[i]第bit位的数值是digit
                int digit = getDigit(arr[i], bit);
                // help数组从右往左填，第bit位小于等于digit的有count[digit]个，这个数就填在count[digit]-1位置
                // 为什么从右往左，因为等于digit的从右边填，左边留给小于digit的，可以达到不同digit之间有序的效果
                // 同时count[digit]要-1，才能依次把下一个数填上
                int index = --count[digit];
                help[index] = arr[i];
            }
            // help数组拷贝到原数组
            for (int i = 0; i < help.length; i++) {
                arr[i] = help[i];
            }
        }

    }

    /**
     * 求数组中所有元素的最大十进制位数
     */
    public int getMaxBits(int[] arr) {
        // 先求最大值
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            max = Math.max(max, arr[i]);
        }
        // 再求最大值有几位
        int maxBits = 0;
        while (max > 0) {
            max /= 10;
            maxBits++;
        }
        return maxBits;
    }

    /**
     * 求value这个数十进制位第bit位的数值
     */
    public int getDigit(int value, int bit) {
        // value整除10的bit-1次方，再对10取余
        // 例如128这个数，求第2位的数值（2），128整除10的1次方等于12，12对10取余等于2
        return (value / (int) Math.pow(10, bit - 1)) % 10;
    }
}
