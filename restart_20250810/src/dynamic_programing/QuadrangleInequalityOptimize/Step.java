package dynamic_programing.QuadrangleInequalityOptimize;

/**
 * 报班入学测试Q31：算法题：爬楼梯
 * 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
 * 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
 * 注意：给定 n 是一个正整数。
 * 示例 1：
 * 输入： 2
 * 输出： 2
 * 解释： 有两种方法可以爬到楼顶。
 * 1.  1 阶 + 1 阶
 * 2.  2 阶
 * 示例 2：
 * 输入： 3
 * 输出： 3
 * 解释： 有三种方法可以爬到楼顶。
 * 1.  1 阶 + 1 阶 + 1 阶
 * 2.  1 阶 + 2 阶
 * 3.  2 阶 + 1 阶
 */
public class Step {
    public static int force(int n) {
        return process(n);
    }

    public static int process(int rest) {
        if (rest == 0) {
            return 1;
        }

        int sum = process(rest - 1);
        if (rest - 2 >= 0) {
            sum += process(rest - 2);
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(force(3));
        System.out.println(dp(2));
    }

public static int dp(int n) {
    int[] dp = new int[n + 1];
    dp[0] = 1;
    dp[1] = dp[0];
    for (int i = 2; i < dp.length; i++) {
        dp[i] = dp[i - 1] + dp[i - 2];
    }
    return dp[n];
}
}
