package shuatiban;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数组中所有数都异或起来的结果，叫做异或和。
 * 给定一个数组arr，可以任意切分成若干个不相交的子数组。其中一定存在一种最优方案，使得切出异或和为0的子数组最多，返回这个最多数量
 */
public class Code037异或和为0的子数组个数最多的切分方案 {
    /**
     * 暴力解
     * 递归试法:每次判断index位置之前的一坨要不要切分,如果要切分就把index加入cutPoint作为一个切分点,index-1属于前一组,index属于后一组
     * 时间复杂度O(2^N),因为每个位置都有切分与不切分两种选择,一共N个位置
     */
    public static int process(int[] xor, int index, List<Integer> cutPoint) {
        // 递归出口,当所有位置都尝试完了
        if (index == xor.length) {
            // 把这个越界的位置加入切分点,要不然最后一组没有结束的位置
            cutPoint.add(index);
            int result = 0;
            // 遍历每个分组,left表示每组第一个,right表示每组最后一个的下一个,也就是下个组的第一个
            int left = 0;
            for (int right : cutPoint) {
                // 如果这个分组的异或和=0,就更新答案.异或和用前缀异或和求,i的前缀异或和异或上j的前缀异或和等于从j+1到i的异或和(假设i>j)
                if ((xor[right - 1] ^ (left == 0 ? 0 : xor[left - 1])) == 0) {
                    result++;
                }
                left = right;
            }
            // 深度优先遍历还原现场.因为递归深搜的过程会顺着这个分支一路走到底并且修改公共数据,返回之前如果不还原的话,回到上一层递归走下一个分支时,公共数据就不是上层递归应有的数据了.
            cutPoint.remove(cutPoint.size() - 1);
            return result;
        }

        // 可能性1、index位置不切分,递归下个位置
        int p1 = process(xor, index + 1, cutPoint);

        // 可能性2、index位置切分,把index加入切分点数组,再递归下一个位置
        cutPoint.add(index);
        int p2 = process(xor, index + 1, cutPoint);

        // 深度优先遍历,当前这一层递归对公共数据做的所有改变,都要在返回之前还原现场
        cutPoint.remove(cutPoint.size() - 1);
        return Math.max(p1, p2);
    }

    public static int violent(int[] arr) {
        int[] xor = new int[arr.length];
        int temp = 0;
        for (int i = 0; i < arr.length; i++) {
            xor[i] = arr[i] ^ temp;
            temp = xor[i];
        }
        return process(xor, 1, new ArrayList<>());
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 0, 1, 2, 3, 4, 5, 0, 0};
        System.out.println(violent(arr));
        System.out.println(dp(arr));
    }


    /**
     * 最优解,时间复杂度O(N)
     * 思路:动态规划(递归试法跳过)
     * dp[i]=k表示0到i位置的最优划分方案,有k个子数组异或和=0.
     * 那么dp[i]等于几有两种可能性:
     * 1.0到i的最优划分方案让最后一组的异或和!=0,而i必然属于最后一组,也就是i没有贡献答案,所以dp[i]=dp[i-1]
     * 2.0到i的最优划分方案让最后一组的异或和=0,那么我们要在0到i之间找出切分点j,使j到i的异或和=0,作为最后一组,所以dp[i]=1+dp[j-1].
     *   那怎么找出这个切分点?解法:如果i的前缀异或和=100,那么找出i前面最后一个前缀异或和也=100的位置j,就是切分点.因为只要i和j的前缀异或和相等,他俩异或在一起就=0,即j+1到i的异或和就=0
     *   只有i前面能找到和自己前缀异或和相等的位置,才能走这个可能性.
     *   找相等的前缀异或和时一定要保证最后一个,要不然会把多个异或和=0的子数组分到一组里.
     */
    public static int dp(int[] arr) {
        // key:前缀异或和的值, value:最后一次出现的下标
        Map<Integer, Integer> map = new HashMap<>();
        // 一个数都没有的时候就已经存在异或和=0了,假设位置=-1
        map.put(0, -1);

        int[] dp = new int[arr.length];

        // 记录当前位置的前缀异或和
        int preXor = 0;

        for (int i = 0; i < arr.length; i++) {
            int p1 = 0;
            int p2 = 0;

            preXor = arr[i] ^ preXor;
            // 如果i前面有和他前缀异或和相等的位置
            if (map.containsKey(preXor)) {
                // 找到这个相等位置作为切分点,答案=1+切分点前面的答案.如果这个相等位置=-1,说明整个0到i作为一组,答案=1
                p1 = 1 + (map.get(preXor) == -1 ? 0 : dp[map.get(preXor)]);
            }
            // 如果前面还有位置的话
            if (i > 0) {
                // i位置对答案没有贡献,直接等于0到i-1的答案
                p2 = dp[i - 1];
            }
            // 答案取最大值
            dp[i] = Math.max(p1, p2);
            // 更新当前前缀异或和出现的最后位置
            map.put(preXor, i);
        }

        return dp[arr.length - 1];
    }


}
