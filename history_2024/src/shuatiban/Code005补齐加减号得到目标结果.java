package shuatiban;

import java.util.HashMap;
import java.util.Map;

/**
 * 给定一个整数数组arr,有正有负有零,给定一个整数target.
 * 在每个数字前面决定加号还是减号,让最终累加和=target,问最多有几种方法
 *
 *
 * 思路:
 *
 * 基本递归试法:从i往后补齐加减号,让结果=t,分两种可能性:1.i补加号,2.i补减号
 * 改动态规划:直接按照递归试法改状态转移方程.dp[i][j]=c表示从i开始补加减号凑齐j有c种方法
 *
 *
 * 优化:
 * 前提:生成一个绝对值数组,也就是arr中正数不变,负数变相反数.绝对值数组累加和=sum
 *
 * 优化1:arr能凑齐的最大值是让补完的数都是正的,也就是绝对值数组的累加和sum.所以如果target大于这个累加和就不可能凑齐,直接返回0
 *
 * 优化2:同样一批数,要么加要么减,结果的奇偶性不会变化,因此如果target的奇偶性和绝对值累加和的奇偶性不同,就不可能凑齐,直接返回0
 *
 * 优化3:对于绝对值数组,假设被冠以加号的累加和为p,被冠以减号的累加和为n,那么一定有p+n=sum,
 * 如果要满足凑出的结果为target,那么一定有p-n=target, 两边加p+n得 2p=target+p+n=target+sum, p=(target+sum)/2
 * 所以原问题等价于,绝对值数组中,任意取数,累加和凑齐(target+sum)/2有几种方法————背包问题.
 *
 * 优化4:压缩空间的动态规划,求背包问题的时候,可以用一个一位数组不断滚动来代替二维数组,节省空间.
 */
public class Code005补齐加减号得到目标结果 {
    /**
     * 暴力递归,arr中从index开始往后决定加还是减,最终得到结果target,返回有几种方法
     */
    public static int process(int[] arr, int index, int target) {
        // 递归出口,数都用完的时候,如果目标值也不剩了,说明找到了一种方法,返回1.否则如果目标值还有剩下,就再也凑不出来了,返回0.
        if (index == arr.length) {
            return target == 0 ? 1 : 0;
        }

        // 可能性1:index位置决定加,那么目标还剩target-arr[index]. 可能性2:index位置决定减,那么目标还剩target+arr[index]. 把目标交给index+1位置
        return process(arr, index + 1, target - arr[index]) + process(arr, index + 1, target + arr[index]);
    }

    public static int violent(int[] arr, int target) {
        return process(arr, 0, target);
    }

    /**
     * 改记忆化搜索
     * 因为缓存要记录从i开始补符号,凑齐目标j,有c种方法,有两重key,所以用Map套Map的方式
     * 为什么不用二维数组当缓存表?因为目标j的取值范围是-绝对值累加和到+绝对值累加和,不好转成数组下标
     */
    public static int processDp(int[] arr, int index, int target, Map<Integer, Map<Integer, Integer>> dp) {
        // 先查缓存
        if (dp.containsKey(index) && dp.get(index).containsKey(target)) {
            return dp.get(index).get(target);
        }

        // 没有缓存

        int count = 0;

        // 递归出口,数都用完的时候,如果目标值也不剩了,说明找到了一种方法,返回1.否则如果目标值还有剩下,就再也凑不出来了,返回0.
        if (index == arr.length) {
            count = target == 0 ? 1 : 0;
        }

        // 可能性1:index位置决定加,那么目标还剩target-arr[index]. 可能性2:index位置决定减,那么目标还剩target+arr[index]. 把目标交给index+1位置
        count = process(arr, index + 1, target - arr[index]) + process(arr, index + 1, target + arr[index]);

        // 记录缓存
        if (!dp.containsKey(index)) {
            dp.put(index, new HashMap<>());
        }
        dp.get(index).put(target, count);

        return count;
    }

    public static int violentDp(int[] arr, int target) {
        return processDp(arr, 0, target, new HashMap<>());
    }

    /**
     * 改动态规划不太好改,因为dp[i][j]=c表示从i开始往后决定加减号,凑出目标j,有c种方法,j的取值范围是-绝对值累加和到+绝对值累加和,不太好转换成下标.
     */

    /**
     * 背包问题暴力递归
     */
    public static int processBag(int[] arr, int index, int target) {
        if (index == arr.length) {
            return target == 0 ? 1 : 0;
        }

        return processBag(arr, index + 1, target) + processBag(arr, index + 1, target - arr[index]);
    }

    /**
     * 背包问题改动态规划
     */
    public static int dpBag(int[] arr, int target) {
        // 优化5,空间压缩
        int[] dp = new int[target + 1];
        dp[0] = 1;

        for (int i = arr.length - 1; i >= 0; i--) {
            // 因为当前格子依赖左上,所以要从下往上填,确保填下面的时候上面没被覆盖.
            for (int t = target; t >= 0; t--) {
                dp[t] = dp[t] + (t - arr[i] >= 0 ? dp[t - arr[i]] : 0);
            }
        }
        return dp[target];
    }

    /**
     * 优化后的求最大方法数
     */
    public static int targetSumCount(int[] arr, int target) {
        // 转换成绝对值数组
        int[] absArr = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < 0) {
                absArr[i] = -arr[i];
            } else {
                absArr[i] = arr[i];
            }
        }

        // 计算绝对值累加和
        int absSum = 0;
        for (int i = 0; i < absArr.length; i++) {
            absSum += absArr[i];
        }

        // 优化1和优化2
        if(target > absSum || ((target & 1) ^ (absSum & 1)) == 1){
            return 0;
        }

        // 优化4,转换成背包问题
        return dpBag(absArr, (target + absSum) >> 1);
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};
        int target = 5;
        System.out.println(violent(arr, target));
        System.out.println(violentDp(arr, target));
        System.out.println(targetSumCount(arr, target));
    }
}
