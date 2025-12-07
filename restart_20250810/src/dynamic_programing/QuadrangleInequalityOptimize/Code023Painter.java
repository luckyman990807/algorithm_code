package dynamic_programing.QuadrangleInequalityOptimize;

/**
 * https://leetcode.cn/problems/split-array-largest-sum/description/
 *
 * 给定一个数组arr,数组中每个值都为正数,表示完成一幅画所需要的时间,再给定一个整数num,表示画家的数量,每个画家只能画连在一起的画.
 * 所有画家并行工作,返回完成所有的画所需要的最少时间.
 * 例如,arr=[3,1,4],num=2,返回最小时间=4.方法是让第一个画家画3和1,用时4,同时让第二个画家画4,用时4,最终所需时间为4.
 * 如果让第一个画家画3,用时3,同时第二个画家画1和4,用时5,最终所需时间为5.
 */
public class Code023Painter {
    public static int force(int[] arr, int num) {
        int[] sum = new int[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            sum[i + 1] = sum[i] + arr[i];
        }

        return process(arr.length - 1, num, sum);
    }

    /**
     * 从0到index范围交给num个画家来画，返回所需要的最少时间
     * 试法：枚举最后一个画家负责啥范围
     * @param index
     * @param num
     * @param sum
     * @return
     */
    public static int process(int index, int num, int[] sum) {
        if (index == 0) {
            return sum[1];
        }
        if (num == 1) {
            return sum[index + 1];
        }

        int min = Integer.MAX_VALUE;
        // 定义一个划分点split，从0到split递归交给剩余num-1个画家来画，从split+1到index交给其中一个画家来画
        // 枚举split的所有可能性
        for (int split = index - 1; split >= 0; split--) {
            int restPainMin = process(split, num - 1, sum);
            int curPain = sum[index + 1] - sum[split + 1];
            int cost = Math.max(restPainMin, curPain);
            min = Math.min(min, cost);
        }

        return min;
    }

    /**
     * 动态规划
     * @param nums
     * @param k
     * @return
     */
    public int dp(int[] nums, int k) {
        int[] sum = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            sum[i + 1] = sum[i] + nums[i];
        }

        int[][] dp = new int[nums.length][k + 1];
        for (int index = 0; index < nums.length; index++) {
            dp[index][1] = sum[index + 1];
        }
        for (int rest = 0; rest <= k; rest++) {
            dp[0][rest] = sum[1];
        }

        for (int index = 1; index < nums.length; index++) {
            for (int rest = 2; rest <= k; rest++) {
                dp[index][rest] = Integer.MAX_VALUE;
                for (int split = index - 1; split >= 0; split--) {
                    int curSum = sum[index + 1] - sum[split + 1];
                    int restSum = dp[split][rest - 1];

                    dp[index][rest] = Math.min(dp[index][rest], Math.max(curSum, restSum));
                }
            }
        }

        return dp[nums.length - 1][k];
    }

    /**
     * 四边形不等式优化
     * @param nums
     * @param k
     * @return
     */
    public int dpOpt(int[] nums, int k) {
        int[] sum = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            sum[i + 1] = sum[i] + nums[i];
        }

        int[][] dp = new int[nums.length][k + 1];
        for (int index = 0; index < nums.length; index++) {
            dp[index][1] = sum[index + 1];
        }
        for (int rest = 0; rest <= k; rest++) {
            dp[0][rest] = sum[1];
        }

        // 记录每次的最佳划分点
        int[][] bestSplit = new int[nums.length][k + 1];

        // 为什么要外层从做到右，内层从下到上，思路：
        // 使用四边形不等式时，如果左+下可得到，就把左+下作为尝试的上下限。这道题每个格子依赖的是左边一列，不依赖同列的格子，所以可以从下到上填，所以左+下都是可以得到的。
        // 怎么判断左和下哪个是上限哪个是下限，思路：
        // 左边格子的含试试：rest--，也就是画家数量--，画家数量少了，那么当前这个画家要负责的范围要么增加，要么不变，而范围增加就意味着split--，所以左边对应split的下限。
        for (int rest = 2; rest <= k; rest++) {
            for (int index = nums.length - 1; index >= 1; index--) {
                dp[index][rest] = Integer.MAX_VALUE;
                for (int split = index == nums.length - 1 ? index - 1 : bestSplit[index + 1][rest]; split >= bestSplit[index][rest - 1]; split--) {
                    int curSum = sum[index + 1] - sum[split + 1];
                    int restSum = dp[split][rest - 1];
                    int maxSum = Math.max(curSum, restSum);

                    // 这里是遇到严格<的结果才更新最佳划分点，还是遇到<=的就可以更新最佳划分点，每道四边形不等式的题目可能不一样，不证明，<和<=都试一下即可。
                    if (maxSum <= dp[index][rest]) {
                        dp[index][rest] = maxSum;
                        bestSplit[index][rest] = split;
                    }
                }
            }
        }

        return dp[nums.length - 1][k];
    }

    public static void main(String[] args) {
        int[] arr = new int[]{3, 1, 4, 5, 2};
        System.out.println(force(arr, 3));
    }
}
