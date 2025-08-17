package shuatiban;

/**
 * 给定一个数组arr，返回如果排序之后，相邻两数的最大差值。要求时间复杂度O(N)，不能使用非基于比较的排序
 *
 * 思路:假设数组有N个数,找出最小值和最大值,把这个范围等分N+1份,对应设置N+1个桶,把每个数落到桶里.
 * 因为多出来1个桶,所以必然有空桶.然后遍历所有不空的桶,用后一个桶的的最小值减去前一个桶的最大值,记录所有差值,求最大差值.
 * 问:为什么非得多出来一个桶?
 * 答:多出来一个桶就必然有空桶,有空桶就必然有排完序的相邻两数中间隔着空桶,这两数的差值一定是大于同一个桶内的相邻两数的差值的,所以就可以理所当然地舍弃同一个桶内的可能性,只考虑不同桶内的即可.
 * 问:为什么要用后一个桶的的最小值减去前一个桶的最大值?
 * 答:后一个桶的的最小值 和 前一个桶的最大值 才是排序后的相邻两数.
 */
public class Code041数组假设排序后相邻两数之差的最大值 {
    public static int maxGap(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }

        int minNum = Integer.MAX_VALUE;
        int maxNum = Integer.MIN_VALUE;

        for (int i = 0; i < arr.length; i++) {
            minNum = Math.min(minNum, arr[i]);
            maxNum = Math.max(maxNum, arr[i]);
        }

        // 记录i号桶里有没有数
        boolean[] hasNum = new boolean[arr.length + 1];
        // 记录i号桶的最小值
        int[] min = new int[arr.length + 1];
        // 记录i号桶的最大值
        int[] max = new int[arr.length + 1];

        // 所有数进桶
        for (int i = 0; i < arr.length; i++) {
            // 计算arr[i]应该进入哪个桶.
            // maxNum - minNum这个范围上桶的编号到arr.length, 那么arr[i] - minNum这个范围上痛的编号到几?
            int bucket = arr.length * (arr[i] - minNum) / (maxNum - minNum);

            // arr[i]进桶,更新桶的状态
            min[bucket] = hasNum[bucket] ? Math.min(min[bucket], arr[i]) : arr[i];
            max[bucket] = hasNum[bucket] ? Math.max(max[bucket], arr[i]) : arr[i];
            hasNum[bucket] = true;
        }

        // 遍历所有不为空的桶,记录最大相邻差值
        int answer = 0;
        // pre记录上一个桶的最大值,也就是排序后的上一个位置的数
        int pre = max[0];
        for (int i = 1; i < hasNum.length; i++) {
            if (hasNum[i]) {
                answer = Math.max(answer, min[i] - pre);
                pre = max[i];
            }
        }

        return answer;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 6};
        System.out.println(maxGap(arr));
    }

}
