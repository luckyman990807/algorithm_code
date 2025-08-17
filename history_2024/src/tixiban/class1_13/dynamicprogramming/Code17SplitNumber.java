package tixiban.class1_13.dynamicprogramming;

/**
 * 给定一个正整数，把这个数分裂成一串加数，要求后面的加数不能比前面的小，求分裂的最多方法数
 */
public class Code17SplitNumber {
    /**
     * 暴力递归法
     *
     * @param pre  上一次分裂出来的数，pre=0没有意义，所以主函数初次调用递归函数的时候，pre传1，假设是number+1这个数第一次分裂出1剩余number继续分裂
     * @param rest 还剩余的数
     * @return
     */
    public static int processViolent(int pre, int rest) {
        // 如果剩余的数=0，说明正好分裂完了，新发现了一种方法，返回1
        if (rest == 0) {
            return 1;
        }
        // 有了前面if，这个if就不需要了，因为pre==rest的时候，下一层递归就是rest==0了。但是为了动态规划看base case方便，可以加上
        if (rest == pre) {
            return 1;
        }
        // 因为后面的数不能比前面的小，如果剩余的数就比前一个小了，就没法分裂了，返回0
        if (rest < pre) {
            return 0;
        }

        int ways = 0;
        // pre + i <= rest控制每次递归rest都是>=0的
        for (int i = 0; pre + i <= rest; i++) {
            // pre+i就是本次要分裂出的数，作为下一层递归的pre。下一层的rest要减去本次分裂的数
            ways += processViolent(pre + i, rest - (pre + i));
        }
        return ways;
    }

    /**
     * 严格表依赖的动态规划
     */
    public static int waysDp(int number) {
        int[][] dp = new int[number + 1][number + 1];
        // 根据递归的base case，第一列、对角线都是1，剩余的下半三角是0，第一行弃而不用因为pre不会=0
        for (int pre = 1; pre <= number; pre++) {
            dp[pre][0] = 1;
            dp[pre][pre] = 1;
        }

        for (int pre = number - 1; pre > 0; pre--) {
            for (int rest = 1; rest <= number; rest++) {
                int ways = 0;
                for (int i = 0; pre + i <= rest; i++) {
                    ways += dp[pre + i][rest - (pre + i)];
                }
                dp[pre][rest] = ways;
            }
        }

        return dp[1][number];
    }

    /**
     * 时间优化的动态规划
     *
     * @param number
     * @return
     */
    public static int waysDpSuper(int number) {
        int[][] dp = new int[number + 1][number + 1];
        for (int pre = 1; pre <= number; pre++) {
            dp[pre][0] = 1;
            dp[pre][pre] = 1;
        }

        for (int pre = number - 1; pre > 0; pre--) {
            for (int rest = 1; rest <= number; rest++) {
                // 把for循环枚举可能性改成O(1)的位置依赖计算，假设pre=2，rest=4，根据枚举遍历可能性的公式可得
                // dp[2][4] = dp[2][2] + dp[3][1] + dp[4][0]
                // 根据转移方程可知依赖下和左方向，所以分析上或右相邻格子。下方相邻格子
                // dp[3][4] = dp[3][1] + dp[4][0]
                // 1式-2式得
                // dp[2][4] = dp[3][4] + dp[2][2]
                // 推广得
                // dp[pre][rest] = dp[pre + 1][rest] + dp[pre][rest - pre]
                // rest - pre越界的话直接忽略。因为递归函数递归下一层时的校验保证了剩余数rest不可能<0，所以剩余数<0的情况是无效的，可以忽略
                int ways = dp[pre + 1][rest];
                if (rest - pre >= 0) {
                    ways += dp[pre][rest - pre];
                }
                dp[pre][rest] = ways;
            }
        }

        return dp[1][number];
    }

    public static void main(String[] args) {
        int number = 5;
        // pre=0没有意义，所以主函数初次调用递归函数的时候，pre传1，假设是number+1这个数第一次分裂出1剩余number继续分裂
        System.out.println(processViolent(1, number));
        System.out.println(waysDp(number));
        System.out.println(waysDpSuper(number));
    }
}
