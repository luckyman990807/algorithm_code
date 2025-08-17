package tixiban.class1_13.dynamicprogramming;

import java.util.HashMap;
import java.util.Map;

/**
 * 给定一个货币数组arr，其中的值都是正整数。给定一个正整数aim表示目标金额。
 * arr中每个值都认为是一张货币，两个相同的货币没有任何不同
 * 求组成aim的方法数。
 * <p>
 * 例如arr=[1,1,1]，aim=2，返回1（两张1）
 * 跟Code11的区别：Code11用[1,1,1]组成2，有3种方法，因为三个1都是不同的货币，而这道题三个1是相同面额的相同货币。这道题的组合中不能出现相同的面额组合。
 * 跟Code12的区别：Code12每种面额是无限张，这道题每种面额是有限张。基于Code12，把无限张改成有限张即可
 */
public class Code13MoneyWaysEveryValueLimit {
    public static int processViolent(int[] value, int[] count, int index, int restAim) {
        if (index == value.length) {
            return restAim == 0 ? 1 : 0;
        }
        int ways = 0;
        for (int num = 0; num <= count[index] && num * value[index] <= restAim; num++) {
            ways += processViolent(value, count, index + 1, restAim - (num * value[index]));
        }
        return ways;
    }

    public static int waysViolent(int[] arr, int aim) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int amount : arr) {
            if (map.containsKey(amount)) {
                map.put(amount, map.get(amount) + 1);
            } else {
                map.put(amount, 1);
            }
        }
        int[] value = new int[map.size()];
        int[] count = new int[map.size()];
        int index = 0;
        for (int amount : map.keySet()) {
            value[index] = amount;
            count[index++] = map.get(amount);
        }
        return processViolent(value, count, 0, aim);
    }

    /**
     * 严格表依赖的动态规划
     *
     * @param arr
     * @param aim
     * @return
     */
    public static int waysDp(int[] arr, int aim) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int amount : arr) {
            if (map.containsKey(amount)) {
                map.put(amount, map.get(amount) + 1);
            } else {
                map.put(amount, 1);
            }
        }
        int[] value = new int[map.size()];
        int[] count = new int[map.size()];
        int i = 0;
        for (int amount : map.keySet()) {
            value[i] = amount;
            count[i++] = map.get(amount);
        }

        int[][] dp = new int[value.length + 1][aim + 1];
        dp[value.length][0] = 1;
        for (int index = value.length - 1; index >= 0; index--) {
            for (int rest = 0; rest <= aim; rest++) {
                // 因为每个面额的货币有多张，所以要for循环遍历可能性。和Code11的区别是这道题除了张数*面额的限制，还加了张数的限制
                for (int num = 0; num <= count[index] && num * value[index] <= rest; num++) {
                    dp[index][rest] += dp[index + 1][rest - num * value[index]];
                }
            }
        }
        return dp[0][aim];
    }

    /**
     * 时间优化的动态规划，遍历可能性for循环优化成O(1)的计算
     *
     * @param arr
     * @param aim
     * @return
     */
    public static int waysDpSuper(int[] arr, int aim) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int amount : arr) {
            if (map.containsKey(amount)) {
                map.put(amount, map.get(amount) + 1);
            } else {
                map.put(amount, 1);
            }
        }
        int[] value = new int[map.size()];
        int[] count = new int[map.size()];
        int i = 0;
        for (int amount : map.keySet()) {
            value[i] = amount;
            count[i++] = map.get(amount);
        }

        int[][] dp = new int[value.length + 1][aim + 1];
        dp[value.length][0] = 1;
        for (int index = value.length - 1; index >= 0; index--) {
            for (int rest = 0; rest <= aim; rest++) {
                dp[index][rest] = dp[index + 1][rest];
                if (rest - value[index] >= 0) {
                    dp[index][rest] += dp[index][rest - value[index]];
                }
                // 因为这道题张数是有限的，假设第i个面额是3，张数是2张，当前剩余目标金额rest是14，
                // dp[i][14] = dp[i+1][14] + dp[i+1][11] + dp[i+1][8]，相加的3个分别是用0张、用1张、用2张的可能性
                // 按照Code11的套路，上面式子 = dp[i+1][14] + dp[i][11]，但是这是不正确的，因为
                // dp[i][11] = dp[i+1][11] + dp[i+1][8] + dp[i+1][5]，所以dp[i][14]就会 = dp[i+1][14] + dp[i][11] + dp[i+1][8] + dp[i+1][5]
                // 多加了一个dp[i+1][5]，所以要减去多出来的dp[i+1][5]
                if (rest - (count[index] + 1) * value[index] >= 0) {
                    dp[index][rest] -= dp[index + 1][rest - (count[index] + 1) * value[index]];
                }
            }
        }
        return dp[0][aim];
    }

    /**
     * 主函数
     * @param args
     */
    public static void main(String[] args) {
        int[] arr = {1, 2, 1, 1, 2, 1, 2};
        int aim = 4;
        System.out.println(waysViolent(arr, aim));
        System.out.println(waysDp(arr, aim));
        System.out.println(waysDpSuper(arr, aim));

    }
}
