package dynamic_programing.Code011SlopeOptimize;

/**
 * 整数拆分问题
 * 给定一个整数，返回拆分的方法数。拆分要求：后面的数不能比前面的数小，例如给定3，可以拆成1,1,1， 1,2， 3，一共3种拆法，不能拆成2,1。
 *
 * 思路：
 * 尝试策略：process(min, rest)，表示还剩rest待拆分，拆出来的数不能小于min，求拆分的的方法数
 * 可能性罗列：
 * 可能性1、本次拆出来min，递归计算剩余rest-min的拆分方法数，也就是process(min, rest-min)
 * 可能性2、本次拆出来min+1，递归计算剩余rest-min-1的拆分方法数，也就是process(min+1, rest-min-1)
 * ......
 * 本次拆出来rest，递归计算剩余0的拆分方法数，也就是process(rest, 0)
 * 累加所有可能性的方法数
 */
public class Code017WaysNumberSplit {
    /**
     * 解法第一阶段：暴力递归
     * @param number
     * @return
     */
    public static int force(int number) {
        return process(1, number);
    }

    /**
     * 当前剩余rest待拆分，拆出来的数不能小于min，求可行的拆分方法数
     * @param min
     * @param rest
     * @return
     */
    public static int process(int min, int rest) {
        // 边界条件
        // 如果这个整数已经正好拆完了，说明找到一种可行的方法
        if (rest == 0) {
            return 1;
        }
        // 如果还没有拆完，但是剩下的数已经小于min了，说明已经没有办法拆出来不小于min的数了，这枝尝试失败
        if (rest < min) {
            return 0;
        }

        int ways = 0;
        // 本次可以拆出来几？从min开始穷举所有可能性，每种可能性递归求后续的方法数，累加
        for (int curSplit = min; curSplit <= rest; curSplit++) {
            ways += process(curSplit, rest - curSplit);
        }

        return ways;
    }


    /**
     * 解法第二阶段：动态规划（严格位置依赖
     * @param number
     * @return
     */
    public static int dp(int number) {
        // dp[min][rest]表示当前还剩rest待拆分，拆出来的数不能小于min，有多少种拆分方法
        // 取值范围，min[1,number]，rest[0,number]
        int[][] dp = new int[number + 1][number + 1];

        // 初始状态
        // 剩余待拆分为0时，拆分已经成功了，记录方法数1。即第一列都为1
        for (int min = 1; min <= number; min++) {
            dp[min][0] = 1;
        }
        // 剩余待拆分小于min时，拆分已经失败了，记录方法数0。即对角线左下方除了第一列都为0，初始化就是0，不需要单独赋值

        // 状态转移
        // 根据递归可能性可知，依赖左下方的格子，因此整体从下往上（第0行不用，min不可能=0），每行从左到右填充
        for (int min = number; min > 0; min--) {
            for (int rest = min; rest <= number; rest++) {
                // 根据递归尝试
                for (int curSplit = min; curSplit <= rest; curSplit++) {
                    dp[min][rest] += dp[curSplit][rest - curSplit];
                }
            }
        }

        return dp[1][number];
    }


    /**
     * 解法第三阶段：动态规划，斜率优化
     * @param number
     * @return
     */
    public static int dpSlopeOptimize(int number) {
        int[][] dp = new int[number + 1][number + 1];

        for (int min = 1; min <= number; min++) {
            dp[min][0] = 1;
        }

        for (int min = number; min > 0; min--) {
            for (int rest = min; rest <= number; rest++) {
                // 斜率优化优化思路：
                // 举个例子，假设min=3，rest=10，要求dp[3][10]
                // 根据位置依赖关系，dp[3][10]=dp[3][7]+dp[4][6]+dp[5][5]+dp[10][0]（中间那些rest<min的无效=0，略掉了）
                // 而进一步观察邻近位置，同样根据位置依赖关系，dp[4][10]=dp[4][6]+dp[5][5]+dp[10][0]（中间无效的略掉）
                // 因此得到dp[3][10]=dp[3][7]+dp[4][10]，也就是左边减去min的格子 + 下边格子
                int ways = dp[min][rest - min];
                if (min + 1 <= number) {
                    ways += dp[min + 1][rest];
                }
                dp[min][rest] = ways;
            }
        }

        return dp[1][number];
    }

    public static void main(String[] args) {
        int number = 4;
        System.out.println(force(number));
        System.out.println(dp(number));
        System.out.println(dpSlopeOptimize(number));
    }

}
