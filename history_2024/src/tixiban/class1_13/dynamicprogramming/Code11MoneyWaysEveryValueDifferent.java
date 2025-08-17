package tixiban.class1_13.dynamicprogramming;

/**
 * 给定一个货币数组arr，其中的值都是正整数。给定一个正整数aim表示目标金额。
 * arr中每个值都认为是一张货币，两个相同的值认为是面额相同的两张货币。
 * 求组成aim的方法数。
 * <p>
 * 例如arr=[1,1,1]，aim=2，返回3（第一张+第二张、第二张+第三张、第一张+第三张）
 */
public class Code11MoneyWaysEveryValueDifferent {
    public static int processViolent(int[] arr, int index, int restAmount) {
        // 如果剩余金额<0，说明这条路走错了，没有找到新的方法，返回0
        if (restAmount < 0) {
            return 0;
        }
        // 如果index越界了，说明没有货币可遍历了，如果恰好把金额都凑齐了，说明找到了一种方法，返回1，否则就是没找到新方法，返回0
        if (index == arr.length) {
            return restAmount == 0 ? 1 : 0;
        }
        // 可以加这个吧，有时候能提前返回
//        if(restAmount==0){
//            return 1;
//        }

        // 可能性1、选中当前货币，index+1该决策下一张货币了，剩余金额-当前面额表示当前货币用于抵消目标金额了
        int p1 = processViolent(arr, index + 1, restAmount - arr[index]);
        // 可能性2、没选中当前货币，剩余金额不变
        int p2 = processViolent(arr, index + 1, restAmount);
        // 返回两种可能性的方法数之和
        return p1 + p2;
    }

    public static int waysDp(int[] arr, int aim) {
        int[][] dp = new int[arr.length + 1][aim + 1];
        dp[arr.length][0] = 1;
        for (int index = arr.length - 1; index >= 0; index--) {
            for (int rest = 0; rest <= aim; rest++) {
                int p1 = rest < arr[index] ? 0 : dp[index + 1][rest - arr[index]];
                int p2 = dp[index + 1][rest];
                dp[index][rest] = p1 + p2;
            }
        }
        return dp[0][aim];
    }

    public static void main(String[] args) {
        int[] arr = new int[]{1,1,1};
        int aim = 2;
        System.out.println(processViolent(arr, 0, aim));
        System.out.println(waysDp(arr, aim));
    }
}
