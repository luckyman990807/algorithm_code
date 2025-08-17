package tixiban.class1_13.dynamicprogramming;

/**
 * 给定一个货币面额数组arr，其中的值都是正整数且不重复。给定一个正整数aim表示目标金额。
 * arr中每个值都认为是一种面额，每种面额的货币有无限张
 * 求组成aim的方法数。
 * <p>
 * 例如arr=[1,2]，aim=4，返回3（四张1、两张1一张2、两张2）
 */
public class Code12MoneyWaysEveryValueLimitless {
    public static int processViolent(int[] arr, int index, int restAim) {
        if (index == arr.length) {
            return restAim == 0 ? 1 : 0;
        }
        int ways = 0;
        // 之前的暴力递归尝试，可能性是有限的，O(1)，这道题的可能性是个for循环，因为每种面额的货币是无限张
        for (int num = 0; num * arr[index] <= restAim; num++) {
            ways += processViolent(arr, index + 1, restAim - (num * arr[index]));
        }
        return ways;
    }

    public static int waysDp(int[] arr, int aim) {
        int[][] dp = new int[arr.length + 1][aim + 1];
        dp[arr.length][0] = 1;
        for (int index = arr.length - 1; index >= 0; index--) {
            for (int rest = 0; rest <= aim; rest++) {
                // num是张数
                for (int num = 0; num * arr[index] <= rest; num++) {
                    dp[index][rest] += dp[index + 1][rest - (num * arr[index])];
                }
            }
        }
        return dp[0][aim];
    }

    /**
     * for循环枚举可能性，优化成O(1)
     * 举个例子，
     * 状态1：
     * i位置的面额arr[i]=3，当前剩余目标金额rest=14，根据动态规划状态转移方程里的张数for循环，
     * 0张的时候，当前格子依赖[i+1][rest-0*3]，也就是下一行的14列
     * 1张的时候，当前格子依赖[i+1][rest-1*3]，也就是下一行的11列
     * 4张的时候，当前格子依赖[i+1][rest-4*3]，也就是下一行的2列
     * 5张的时候剩余金额rest-5*3 < 0了，无效不计入
     * 综上，i行14列的格子 = i-1行14、11、8、5、2列的格子相加
     * 状态2：
     * i位置的面额arr[i]=3，当前剩余目标金额rest=11，根据动态规划状态转移方程里的张数for循环，
     * 0张的时候，当前格子依赖[i+1][rest-0*3]，也就是下一行的11列
     * 1张的时候，当前格子依赖[i+1][rest-1*3]，也就是下一行的8列
     * 3张的时候，当前格子依赖[i+1][rest-2*3]，也就是下一行的2列
     * 4张的时候剩余金额rest-4*3 < 0了，无效不计入
     * 综上，i行11列的格子 = i-1行11、8、5、2列的格子相加
     *
     * 经过观察，把状态2带入状态1，可得，i行14列的格子 = i-1行14列 + i行11列的格子
     * 成功把for循环节省成一次计算
     *
     * 推广得到，如果存在有效的张数，那么
     * dp[index][rest] = dp[index+1][rest] + dp[index][rest-arr[index]]
     * 如果不存在有效的张数，那么
     * dp[index][rest] = dp[index+1][rest]
     */
    public static int waysDpSuper(int[] arr, int aim){
        int[][] dp = new int[arr.length + 1][aim + 1];
        dp[arr.length][0] = 1;
        for (int index = arr.length - 1; index >= 0; index--) {
            for (int rest = 0; rest <= aim; rest++) {
                // for循环枚举可能性，优化成 dp[index][rest] = dp[index+1][rest] + dp[index][rest-arr[index]]
                dp[index][rest]=dp[index+1][rest];
                if(rest-arr[index]>=0){
                    dp[index][rest] += dp[index][rest-arr[index]];
                }
            }
        }
        return dp[0][aim];
    }

    public static void main(String[] args) {
        int[] arr = new int[]{1, 2};
        int aim = 4;
        System.out.println(processViolent(arr, 0, aim));
        System.out.println(waysDp(arr, aim));
        System.out.println(waysDpSuper(arr, aim));
    }
}
