package dynamic_programing;

/**
 * 给定4个参数：
 * N：一共有1-N，N个位置
 * start：机器人起始位置
 * aim：机器人的目标位置
 * K：一共能走K步
 *
 * 机器人行走规则：
 * 如果当前在1位置，那么只能向右走到2位置。如果当前在N位置，那么只能向左走到N-1位置。否则既能向左，也能向右。
 *
 * 描述：机器人从起始位置开始，每次只能向左或者向右移动一步，最多走K步，求从起始位置到目标位置有多少种不同的路径
 */
public class Code001NumberOfRobotPaths {
    /**
     * 第一阶段解法：暴力递归
     */
    // 递归入口
    public static int force(int N, int start, int aim, int K) {
        return process(N, start, aim, K);
    }

    // 递归过程
    // @param N 一共1～N N个位置
    // @param cur 当前所在位置
    // @param aim 目标位置
    // @param rest 剩余能走几步
    public static int process(int N, int cur, int aim, int rest) {
        // base case：剩余步数为0，如果当前恰好在目标位置，那么说明找到了一种路径
        if (rest == 0) {
            return cur == aim ? 1 : 0;
        }
        // common case：分为3种可能性，每种可能性分别拆解子问题：
        // 1、当前在1位置：没得选下一步只能走2，所以从1位置走rest步到达aim的路径数 = 从2位置走rest-1步到达aim的路径数
        // 2、当前在N位置：没得选下一步只能走N-1，从N位置走rest步到达aim的路径数 = 从N-1位置走rest-1步到达aim的路径数
        // 3、当前在中间位置l：从l走rest步到达aim的路径数 = 从l-1走rest-1步到达aim的路径数 + 从l+1走rest-1步到达aim的路径数
        if (cur == 1) {
            return process(N, 2, aim, rest - 1);
        }
        if (cur == N) {
            return process(N, N - 1, aim, rest - 1);
        }
        return process(N, cur - 1, aim, rest - 1) + process(N, cur + 1, aim, rest - 1);
    }

    /**
     * 第二阶段解法：记忆化搜索（自顶向下的动态规划）
     * 相较于第一阶段暴力递归，增加了缓存表（记忆化），可以避免重复计算子问题
     */
    public static int force1(int N, int start, int aim, int K) {
        // 定义缓存表
        // 整个计算流程中，aim和N是固定不变的常量，变量是当前位置cur和剩余步数rest，一组cur加rest就对应一个结果，因此以cur和rest为key构建缓存表
        // cur的范围：从1到N，rest的范围：从0到K。为了能覆盖他俩的取值范围，缓存表的大小应设为[N + 1][K + 1]
        int[][] dp = new int[N + 1][K + 1];

        // 初始化缓存表，-1代表没有缓存
        for (int i = 0; i <= N; i++) {
            for (int j = 0; j <= K; j++) {
                dp[i][j] = -1;
            }
        }

        return process1(dp, start, K, aim, N);
    }

    public static int process1(int[][] dp, int cur, int rest, int aim, int N) {
        // 有缓存就直接返回，不重复计算
        if (dp[cur][rest] != -1) {
            return dp[cur][rest];
        }

        int ans;
        if (rest == 0) {
            ans = cur == aim ? 1 : 0;
        } else if (cur == 1) {
            ans = process1(dp, 2, rest - 1, aim, N);
        } else if (cur == N) {
            ans = process1(dp, N - 1, rest - 1, aim, N);
        } else {
            ans = process1(dp, cur - 1, rest - 1, aim, N) + process1(dp, cur + 1, rest - 1, aim, N);
        }

        // 缓存中间结果
        dp[cur][rest] = ans;

        return ans;
    }

    /**
     * 第三阶段解法：动态规划
     * 递归策略转换成dp表元素之间的依赖关系（也就是状态转移方程），递归baseCase转换成dp表的初始元素（也就是初始状态）
     * 从初始状态开始，按照状态转移方程完成整个dp表的填赋值，最后返回顶层结果所需的dp表元素即可。
     */
    public static int dp(int N, int start, int aim, int K) {
        // 变量：当前位置cur，范围1～N；剩余步数rest，范围0～K
        // 定义dp表：dp[cur][rest]，代表从cur位置开始，走rest步，到达aim位置的路径数
        int[][] dp = new int[N + 1][K + 1];
        // 初始状态：当rest=0时，只有cur=aim，结果才=1，表示发现了一种路径。否则结果=0
        dp[aim][0] = 1;
        // 状态转移：
        // 当前位置为1，剩余步数为rest时，依赖位置为2、剩余步数为rest-1的结果；
        // 当前位置为N，剩余步数为rest时，依赖位置为N-1、剩余步数为rest-1的结果；
        // 中间位置cur，剩余步数为rest时，依赖位置为cur-1、剩余步数为rest-1的结果+位置为cur+1、剩余步数为rest-1的结果
        // 画dp表分析计算方向：已知第0列的值了，每个位置的值依赖上一列上一行和上一列下一行的值，所以整体dp表从第1列向第K列推，每一列从0行往N行推或者从N行往0行推都可以
        for (int rest = 1; rest <= K; rest++) {
            dp[1][rest] = dp[2][rest - 1];
            dp[N][rest] = dp[N - 1][rest - 1];
            for (int cur = 2; cur < N; cur++) {
                dp[cur][rest] = dp[cur - 1][rest - 1] + dp[cur + 1][rest - 1];
            }
        }

        // 返回从start位置出发，走K步，到达aim位置的路径数
        return dp[start][K];
    }


    public static void main(String[] args) {
        System.out.println(force(4, 2, 4, 4));
        System.out.println(force1(4, 2, 4, 4));
        System.out.println(dp(4, 2, 4, 4));
    }
}
