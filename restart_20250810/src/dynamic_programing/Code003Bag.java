package dynamic_programing;

import java.util.Arrays;

public class Code003Bag {
    /**
     * 第一阶段解法：暴力递归尝试
     * @param weight
     * @param value
     * @param volume
     * @return
     */
    public static int force(int[] weight, int[] value, int volume) {
        return process(0, volume, weight, value);
    }

    /**
     * 背包剩余容量为rest，返回从下标l开始往后遍历物品所能获取到的最大价值
     * @param l
     * @param rest
     * @param weight
     * @param value
     * @return
     */
    public static int process(int l, int rest, int[] weight, int[] value) {
        // base case：只剩一件物品，如果剩余容量还能装下此物品，那么获取到的最大价值就是此物品的价值；否则获取到的最大价值是0
        if (l == weight.length - 1) {
            return weight[l] <= rest ? value[l] : 0;
        }
        // 可能性1:不要当前物品，容量不变，总价值=从下个物品往后遍历
        int p1 = process(l + 1, rest, weight, value);
        // 可能性2:如果剩余容量还能装下当前物品，就要当前物品，容量减去当前物品的重量，总价值=当前物品的价值+从下个物品往后遍历获取到的最大价值
        int p2 = weight[l] <= rest ? value[l] + process(l + 1, rest - weight[l], weight, value) : 0;
        // 选取最优可能性
        return Math.max(p1, p2);
    }

    /**
     * 解法第二阶段：动态规划
     * 根据暴力递归尝试的策略，转换成状态转移方程，不再赘述
     * @param weight
     * @param value
     * @param volume
     * @return
     */
    public static int dp(int[] weight, int[] value, int volume) {
        int[][] dp = new int[weight.length][volume + 1];
        for (int rest = 0; rest <= volume; rest++) {
            dp[weight.length - 1][rest] = weight[weight.length - 1] <= rest ? value[weight.length - 1] : 0;
        }

        for (int l = weight.length - 2; l >= 0; l--) {
            for (int rest = 0; rest <= volume; rest++) {
                int p1 = dp[l + 1][rest];
                int p2 = weight[l] <= rest ? value[l] + dp[l + 1][rest - weight[l]] : 0;
                dp[l][rest] = Math.max(p1, p2);
            }
        }

        return dp[0][volume];
    }


    /**
     * 对数器
     * @param args
     */
    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            // 物品数量
            int n = randomInt(5, 10);
            // weight数组
            int[] weight = randomIntArray(n, 0, 10);
            // value数组
            int[] value = randomIntArray(n, 0, 10);
            // 背包容量
            int volume = randomInt(0, 15);

            int force = force(weight, value, volume);
            int dp = tixiban.class1_13.dynamicprogramming.Code03Bag.bag(weight, value, volume);
            if (force != dp) {
                System.out.println("出错！");
                System.out.println("weight数组：" + Arrays.toString(weight));
                System.out.println("weight数组：" + Arrays.toString(weight));
                System.out.println("背包容量：" + volume);
                System.out.println("暴力递归：" + force);
                System.out.println("dp：" + dp);
            }
            System.out.println("通过！");
        }
    }

    public static int[] randomIntArray(int length, int minValue, int maxValue) {
        int[] arr = new int[length];
        for (int i = 0; i < length; i++) {
            // Math.random()取值范围[0,1)
            // 要求后面这块[0,max-min]
            arr[i] = minValue + (int) (Math.random() * (maxValue - minValue + 1));
        }
        return arr;
    }

    public static int randomInt(int minValue, int maxValue) {
        return minValue + (int) (Math.random() * (maxValue - minValue + 1));
    }
}
