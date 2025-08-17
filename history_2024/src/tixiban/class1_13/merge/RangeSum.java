package tixiban.class1_13.merge;

import tixiban.class1_13.recurrence.MergeSort;

import java.util.Arrays;

/**
 * 前置知识：前缀和
 * 数组中下标i的前缀和=下标0~i所有数相加的和
 * 如果算法中用到数组的前缀和，可以提前把每个数的前缀和求出来放到一个前缀和数组，这样第一次求是O(N)，后面每次用都是O(1)。否则每次用都现求的话每次都是O(N)。
 */
public class RangeSum {
    public static int countRangeSum(int[] arr, int lower, int upper) {
        if (null == arr || arr.length == 0) {
            return 0;
        }
        // 前缀和数组
        int[] sum = new int[arr.length];
        sum[0] = arr[0];
        for (int i = 1; i < arr.length; i++) {
            sum[i] = sum[i - 1] + arr[i];
        }
        // 用前缀和数组递归
        return process(sum, 0, arr.length - 1, lower, upper);
    }

    /**
     * 求一个数组中累加和∈[lower,upper]的子数组的个数。
     * =>求以下标j结尾的子数组中，有几个子数组累加和∈[lower,upper]，然后j∈[0,length-1]求和。
     * 那么怎么求以下标j结尾的子数组中有几个子数组累加和∈[lower,upper]？
     * =>从i到j的累加和S(i,j)=S(0,j)-S(0,i-1)，
     * =>S(0,j)-S(0,i-1)∈[lower,upper]
     * =>S(0,i-1)∈[S(0,j)-upper,S(0,j)-lower]
     * =>求j前面有几个前缀和落在[S(0,j)-upper,S(0,j)-lower]范围上
     * =>对于前缀和数组而言，就是求j前面有几个数落在[]范围上（merge问题）
     *
     * 为什么把每个元素前缀和作为子数组累加和的情况放在base case里判断？
     * 因为merge判断累加和时涵盖不到从0开始到j的子数组的累加和，即涵盖不到j的前缀和。
     * 为什么涵盖不到？
     * 解释1：merge的时候左边前缀和最左也就到sum[0]，即S(0,0)亦即arr[0]；转换成子数组累加和最多是S(0,j)-S(0,0)=S(1,j)，涵盖不到S(0,j)。
     * 解释2：merge时的判断条件是基于这个式子展开的：S(i,j)=S(0,j)-S(0,i-1)，而这个式子无法表示i=0的情况，因为S(0,-1)没这个东西。
     */
    public static int process(int[] sum, int left, int right, int lower, int upper) {
        // 每个元素都会经历base case，所以把每个元素前缀和的情况放到base case里处理。
        if (left == right) {
            if (sum[left] >= lower && sum[left] <= upper) {
                return 1;
            } else {
                return 0;
            }
        }
        // 对于前缀和数组，先求左组内部每个数之前有几个数符合（∈[]），再求右组内部每个数之前有几个数符合，最后求右组每个数在左组中能找到几个数符合
        int mid = left + ((right - left) >> 1);
        return process(sum, left, mid, lower, upper)
                + process(sum, mid + 1, right, lower, upper)
                + merge(sum, left, mid, right, lower, upper);
    }

    public static int merge(int[] sum, int left, int mid, int right, int lower, int upper) {
        // 这两个指针只在左组滑动，形成一个窗口，记录符合条件的个数
        int leftPoint = left;
        int rightPoint = left;

        int count = 0;

        // 遍历右组
        for (int j = mid + 1; j <=right; j++) {
            // merge时已经经历了左组的递归，左组已经有序
            // 把左窗口滑到第一个不小于S(0,j)-upper的位置，即从这个位置开始，符合左闭条件
            while (leftPoint <= mid && sum[leftPoint] < sum[j] - upper) {
                leftPoint++;
            }
            // 把右窗口滑到第一个大于S(0,j)-lower的位置，即从这个位置开始，不符合右闭条件
            while (rightPoint <= mid && sum[rightPoint] <= sum[j] - lower) {
                rightPoint++;
            }
            // 计算符合的个数，由于右窗口所在那个位置是不符合的，所以符合的个数=左右窗口之间的个数-1，即(rightPoint-leftPoint+1)-1
            count += rightPoint - leftPoint;
        }

        // 排序并拷贝数组
        int[] help = new int[right - left + 1];
        int helpPoint = 0;

        leftPoint = left;
        rightPoint = mid + 1;
        while (leftPoint <= mid && rightPoint <= right) {
            help[helpPoint++] = sum[leftPoint] < sum[rightPoint] ? sum[leftPoint++] : sum[rightPoint++];
        }
        while (leftPoint <= mid) {
            help[helpPoint++] = sum[leftPoint++];
        }
        while (rightPoint <= right) {
            help[helpPoint++] = sum[rightPoint++];
        }

        for (int i = 0; i < help.length; i++) {
            sum[left + i] = help[i];
        }

        return count;
    }

    public static void main(String[] args) {
        for (int n = 0; n < 1000000; n++) {

            int lower = 30;
            int upper = 50;

            int[] arr = MergeSort.generateArr(6, 20);

            int countNormal = 0;
            for (int i = 0; i < arr.length; i++) {
                int sum = 0;
                for (int j = i; j < arr.length; j++) {
                    // O(N^2)复杂度
                    sum+=arr[j];
                    if(sum < lower){
                        continue;
                    }else if(sum>upper){
                        break;
                    }else {
                        countNormal++;
                    }
//                    // O(N^3)复杂度
//                    int sum = 0;
//                    for (int k = i; k <= j; k++) {
//                        sum += arr[k];
//                    }
//                    if (sum >= lower && sum <= upper) {
//                        countNormal++;
//                    }
                }
            }

            int countMerge = countRangeSum(arr, lower, upper);

            if (countMerge != countNormal) {
                System.out.println(Arrays.toString(arr));
                System.out.println("暴力法：" + countNormal);
                System.out.println("merge法：" + countMerge);
                break;
            }
        }
    }
}
