package tixiban.class28longestsubarraysumk;

/**
 * 给定一个数组arr,值可能正可能负可能0,给定一个整数k,求arr中所有累加和<=k的子数组中最长的那个,返回其长度.
 *
 * 这道题的思想很难,舍弃了乍一看就不可能满足条件的可能性.
 *
 * 思路:
 * 辅助数组+滑动窗口
 *
 * 第一步:建立辅助数组
 * 辅助数组minSum[i]表示从i开始的最小累加和,辅助数组minSumEnd[i]表示从i开始的最小累加和的右边界(包含).
 * 从最后一个位置n-1开始最小的累加和就等于他自己,右边界也是n-1自己,直接填.
 * 从n-2开始,如果从他下一位开始最小累加和<=0,那么说明加上下一位开始的最小累加和就可以把从n-2开始的累加和变得更小,
 * 所以从n-2开始的最小累加和=n-2位置的数+n-1开始的最小累加和,即minSumEnd[n-2]=arr[n-2]+minSumEnd[n-1],从n-2开始最小累加和的右边界也=n-1的右边界,即minSumEnd[n-2]=minSumEnd[n-1]
 * 如果从他下一位开始最小的累加和>0,那么加上下一位开始的最小累加和只会让自己变得更大,所以不加.
 * 所以从n-2开始的最小累加和就是n-2位置自己,右边界也是n-2自己.
 * 依次从右往左算,每个位置看下个位置开始的累加和能不能加到自己身上让自己累加和变小...最后求出两个辅助数组.
 *
 * 第二步:滑动窗口
 * 左边界和右边界从0开始,首先看以0开始的最小累加和minSum[0]是否小于等于k,如果是,就把右边界扩到以0开始最小累加和的右边界的下一个,假设5,
 * 再看累加上以5开始的最小累加和minSum[5]是否小于等于k,如果是,就把右边界再扩到以5开始最小累加和的下一个,假设7...直到累加上以i开始的最小累加和大于k停止,假设i=14.
 * 到现在就求出了以0开始累加和小于等于k的最长子数组,记录长度=14-0=14.
 * 再求以1开始累加和小于等于k的最长子数组,左边界来到1,当前窗口累加和sum=sum-arr[0],看sum加上以14开始最小累加和是否小于等于k,如果是,就把右边界继续扩,如果不是,直接左边界右移,从1开始的就不看了.
 * 为什么从1开始的可以直接不看了?因为从1开始如果不能在0的基础上继续往右扩,那么从1开始的最大长度必然小于从0开始的,而我们只关心能不能更长,所以直接舍弃无用可能性.
 * 如果左边界来到右边界位置了,那么二者一块来到下个位置,重新开始扩.
 *
 */
public class Code03LongestSubArraySumNoMoreThanK {
    public static int maxLength(int[] arr, int k) {
        // 构建辅助数组
        // minSum[表示]从i开始的最小累加和
        int[] minSum = new int[arr.length];
        // minSumEnd[i]表示从i开始最小累加和的右边界
        int[] minSumEnd = new int[arr.length];
        // 最后一个位置直接赋值,前面位置依赖后面位置
        minSum[arr.length - 1] = arr[arr.length - 1];
        minSumEnd[arr.length - 1] = arr.length - 1;
        for (int i = arr.length - 2; i >= 0; i--) {
            if (minSum[i + 1] <= 0) {
                // 如果下一位置往后的最小累加和能让自己的累加和更小,就纳入块中
                minSum[i] = arr[i] + minSum[i + 1];
                minSumEnd[i] = minSumEnd[i + 1];
            } else {
                // 否则,就自己单独成块
                minSum[i] = arr[i];
                minSumEnd[i] = i;
            }
        }
        // 辅助数组的作用就是把原数组分成了一块一块,比如:0到5是一块,表示从0开始最小的累加和是从0加到5;6自己是一块,表示从6开始最小的累加和是6自己;7到8是一块,表示从7开始最小的累加和是从7加到8...

        // 滑动窗口的right,记录从left开始最小累加和的右边界的下一位,也就是right是left能扩到的最远的位置的下一个位置(0例外)
        int right = 0;
        int sum = 0;
        int length = 0;
        for (int left = 0; left < minSum.length; left++) {
            // 从每一个left位置往右扩,扩到从left开始的累加和只增不减了,停在扩不进来的位置
            while (right < minSum.length && sum + minSum[right] <= k) {
                // 只要窗口累加和加上从right开始的最小累加和小于等于k,就往右扩
                sum += minSum[right];
                right = minSumEnd[right] + 1;
            }
            // 记录结果
            length = Math.max(length, right - left);

            if (left < right) {
                // 如果窗口还在,就换下一个left继续往右扩,能扩的更长就会记录结果
                sum -= arr[left];
            } else {
                // 如果窗口已经没了,说明窗口内没有left接替往下扩了,那么left和right一起去下个位置(left马上++了),从下个位置往下扩
                right = left + 1;
            }
        }
        return length;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, -2, 4, 5, -7, 6, 7, -9, 8, 9, -3};
        int k = 6;
        System.out.println(maxLength(arr, k));
    }

}
