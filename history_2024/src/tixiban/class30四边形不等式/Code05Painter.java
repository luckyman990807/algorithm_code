package tixiban.class30四边形不等式;

/**
 * 给定一个数组arr,数组中每个值都为正数,表示完成一幅画所需要的时间,再给定一个整数num,表示画家的数量,每个画家只能画连在一起的画.
 * 所有画家并行工作,返回完成所有的画所需要的最少时间.
 * 例如,arr=[3,1,4],num=2,返回最小时间=4.方法是让第一个画家画3和1,用时4,同时让第二个画家画4,用时4,最终所需时间为4.
 * 如果让第一个画家画3,用时3,同时第二个画家画1和4,用时5,最终所需时间为5.
 */
public class Code05Painter {
    /**
     * 暴力递归
     * 思路:从0到index号画,交给num个画家,需要最少多少时间完成
     * 列举可能性:
     * 一个画家啥也不干,剩下的画家画0到index号
     * 一个画家画index号,剩下的画家画0到index-1号
     * 一个画家画index-1到index号,剩下的画家画0到index-2号
     * ...
     * 就是列举一个画家画的数量,剩下的画家交给递归.
     */
    public static int process(int[] arr, int[] sum, int index, int num) {
        // 如果是0到0,只剩0号一幅画了,返回0号画的耗时
        if (index == 0) {
            return arr[0];
        }
        // 如果只剩一个画家,返回0到index所有画的耗时累加和
        if (num == 1) {
            return sum[index];
        }
        // 尝试所有可能性,一个画家的耗时是i到index号画的累加和,剩下的画交给剩下的画家
        int result = Integer.MAX_VALUE;
        for (int i = index; i >= 0; i--) {
            result = Math.min(result, Math.max(sum[index] - sum[i], process(arr, sum, i, num - 1)));
        }
        return result;
    }

    public static int violent(int[] arr, int num) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (num <= 0) {
            return Integer.MAX_VALUE;
        }
        int[] sum = new int[arr.length];
        sum[0] = arr[0];
        for (int i = 1; i < sum.length; i++) {
            sum[i] = sum[i - 1] + arr[i];
        }

        return process(arr, sum, arr.length - 1, num);
    }

    /**
     * 改动态规划
     *
     */
    public static int dp1(int[] arr, int num) {
        // 前缀和数组加速计算
        int[] sum = new int[arr.length];
        sum[0] = arr[0];
        for (int i = 1; i < sum.length; i++) {
            sum[i] = sum[i - 1] + arr[i];
        }

        // 按照递归出口赋初值
        int[][] dp = new int[arr.length][num + 1];
        for (int n = 1; n <= num; n++) {
            dp[0][n] = arr[0];
        }
        for (int i = 0; i < arr.length; i++) {
            dp[i][1] = sum[i];
        }

        // 枚举所有可能性
        for (int index = 1; index < arr.length; index++) {
            for (int n = 2; n <= num; n++) {
                int result = Integer.MAX_VALUE;
                // 一个画家啥都不干,剩下画家画0到index
                // 一个画家画index,剩下画家画0到index-1
                // 一个画家画index-1到index,剩下画家画0到index-2...
                for (int i = index; i >= 0; i--) {
                    result = Math.min(result, Math.max(sum[index] - sum[i], dp[i][n - 1]));
                }
                dp[index][n] = result;
            }
        }
        return dp[arr.length - 1][num];
    }

    /**
     * 四边形不等式优化动态规划
     * 画图可知,dp表先填好第0行和第1列然后剩下的每个格子都以来自己左边一列从当前行往上的所有格子,
     * 即,dp[2][2]依赖dp[2][1]、dp[1][1]、dp[0][1],
     * 完全不依赖当前列的格子,因此在一列中我们可以从下往上求,符合四边形不等式的前提(单调性显而易见,画不变画家减少,结果单调增;画家不变画减少,结果单调减)
     * 用左边格子作为下限,下边格子所谓上限,减少可能性枚举的数量.
     *
     * 如何理解上下限?
     * 如果10幅画交给3个画家,第一个画家画7到9时最佳划分,那么当画家变成4个时,第一个画家有必要画比7到9更多的画吗?
     */
    public static int dp2(int[] arr, int num) {
        // 前缀和数组加速计算
        int[] sum = new int[arr.length];
        sum[0] = arr[0];
        for (int i = 1; i < sum.length; i++) {
            sum[i] = sum[i - 1] + arr[i];
        }

        // dp表
        int[][] dp = new int[arr.length][num + 1];
        // 最佳划分点,split[5][3]=4表示:0到5号画交给3个画家画的时候,最佳划分点是一个画家画4到5,剩下的画家画0到3
        int[][] split = new int[arr.length][num + 1];
        // 按照递归出口赋初值
        for (int n = 1; n <= num; n++) {
            dp[0][n] = arr[0];
        }
        for (int i = 0; i < arr.length; i++) {
            dp[i][1] = sum[i];
            // 最佳划分点都是0,即一个画家画0到最后一个
            split[i][1] = 0;
        }

        // 填格子的顺序改为从下往上填一列,从左到右推所有列
        for (int n = 2; n <= num; n++) {
            int result = Integer.MAX_VALUE;
            int splitPoint = -1;
            // 每一列最后一个格子单独算,因为只有左没有下,左边格子的最佳划分点作为下限
            for (int i = arr.length - 1; i >= split[arr.length - 1][n - 1]; i--) {
                int curResult = Math.max(sum[arr.length - 1] - sum[i], dp[i][n - 1]);
                if (curResult < result) {
                    result = curResult;
                    splitPoint = i;
                }
            }
            dp[arr.length - 1][n] = result;
            split[arr.length - 1][n] = splitPoint;

            result = Integer.MAX_VALUE;
            splitPoint = -1;
            for (int index = arr.length - 2; index > 0; index--) {
                // 下边格子的最佳划分点是上限,左边格子的最佳划分点是下限
                for (int i = split[index + 1][n]; i >= split[index][n - 1]; i--) {
                    int curResult = Math.max(sum[index] - sum[i], dp[i][n - 1]);
                    /** 注意,四边形不等式的问题,有的这里取<=,有的取<,直接对数器验,不证明 */
                    if (curResult < result) {
                        result = curResult;
                        splitPoint = i;
                    }
                }
                dp[index][n] = result;
                split[index][n] = splitPoint;
            }
        }
        return dp[arr.length - 1][num];
    }


    /**
     * 最优解
     * 思路:
     * 假设有5幅画[2,3,1,5,4],有2个画家.
     * 首先无论几个画家,最多花费的时间也就是5幅画的累加和=15,
     * 然后在总时间15内二分法遍历,求按照当前时间,最少要用几个画家,大于2就往右遍历,小于等于2就往左遍历,直到遍历到底,记录沿途对应画家数小于等于2的最小时间.
     *
     * 怎么求按当前时间最少要用几个画家?
     * 思路:装油瓶.
     * 想像成5份油[2,3,1,5,4],每个瓶子只能装相邻的油,
     * 现在规定每个瓶子只能装不超过15的油,装法是5份全装一个瓶子,得到瓶子数=1;
     * 小于等于2,往左,现在规定每个瓶子装7,装法是[2,3][1,5][4],瓶子数=3;
     * 大于2,往右,现在规定每个瓶子装11,装法是[2,3,1,5][4],瓶子数=2;
     * 小于等于2,往左,现在规定每个瓶子装9,装法是[2,3,1][5,4],瓶子数=2;
     * 小于等于2,往左,现在规定每个瓶子装8,装法是[2,3,1][5][4],瓶子数=3;
     * 大于2,往右,已经到头了,记录瓶子数=2的最小瓶子容积是9
     *
     * 类比得到,2个画家画完5幅画花费的最少时间是9
     *
     * 复杂度O(N),四边形不等式复杂度O(N*num)
     */
    public static int geiMinNum(int[] arr, int time) {
        int sum = 0;
        int num = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > time) {
                return Integer.MAX_VALUE;
            }
            if (sum + arr[i] > time) {
                num++;
                sum = arr[i];
            } else {
                sum += arr[i];
            }
        }
        // 最后一幅画没触发num++,这里补偿一下
        return num + 1;
    }

    public static int best(int[] arr, int num) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }

        int left = 0;
        int right = sum;
        int result = Integer.MAX_VALUE;
        while (left <= right) {
            int time = (left + right) / 2;
            if (geiMinNum(arr, time) > num) {
                left = time + 1;
            } else {
                result = time;
                right = time - 1;
            }
        }
        return result;
    }


    public static void main(String[] args) {
        int[] arr = {2, 3, 1, 5, 4};
        int num = 2;
        System.out.println(violent(arr, num));
        System.out.println(dp1(arr, num));
        System.out.println(dp2(arr, num));
        System.out.println(best(arr, num));
    }
}
