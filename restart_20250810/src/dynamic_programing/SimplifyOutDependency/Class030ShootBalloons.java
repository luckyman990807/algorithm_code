package dynamic_programing.SimplifyOutDependency;

/**
 * 打气球问题
 * https://leetcode.cn/problems/burst-balloons/submissions/723459492/
 * 给定一个数组arr，代表一排有分数的气球，打爆一个气球的得分=当前气球分数*左边最近的没爆的气球的分数*右边最近的没爆的气球的分数，如果左边已经没有没爆的气球了，那么*1，右边同理。
 * 求最大得分
 * 
 * 看到题目猜试法的时候比较容易想到：从l到r范围内，遍历尝试先打第一个、先打第二个。。。最后所有可能性取max。
 * 但是会遇到一个问题：尝试从l到r的时候，我怎么知道l往左以及r往右又没有没爆的气球了？也就是说，我依赖外部的信息才能完成l到r范围的尝试，而且这个信息很难传过来。
 * 为什么不能用状态压缩：因为这个不是判断某个位置非0即1，而是寻找某个位置左侧最近的一个1和右侧最近的一个1，可能涉及遍历，复杂度升高了。
 * 为什么从左往右的尝试模型不行：先打第3个，再打第2个，和先打第2个，再打第3个，得分是不一样的，但是从左往右的尝试模型无法覆盖这两种情况，因为尝试右边的时候左边必须已经确定了，不支持先尝试右边的再回过头尝试左边的。
 * 
 * 思路：外部信息简化
 * 既然我依赖外部信息，并且外部信息很难传过来，那么我就想办法让外部信息固定下来，成为潜规则，不需要传进来；
 * 既然尝试先打哪个很依赖外部信息，那么我就尝试【最后打哪个】
 * 试法：
 * 首先生成辅助数组[1,arr,1]，arr左右两边各填充1，形成一个潜规则：左右边界相邻的那个位置一定是没爆的。填充1不影响计算分数。
 * 然后递归尝试函数process(helpArr, l, r)代表helpArr从l到r范围都打爆的最高得分，返回process(helpArr, 1, n)即所求。
 * 先分析首次调用：process(helpArr, 1, n)，从1到n范围，遍历尝试最后打哪个，返回得分最高的可能性。比如最后打第i个，我们发现从1到i-1范围，左右边界相邻的位置也是没爆的，从i+1到n范围，左右边界相邻的位置也是没爆的，也就是说生成辅助数组后，【左右一定没爆】这个潜规则可以随着递归一路透传下来。
 * 再分析一般位置的递归调用：process(helpArr, l, r)，从l到r范围内，遍历尝试最后打第i个，那么：
 * 1、从l到i-1范围满足【左右都没爆】的潜台词，可以递归计算这部分的最高得分process(helpArr, l, i-1)，
 * 2、同理递归计算从i+1到r范围的最高得分process(helpArr, i+1, r)，
 * 3、最后打爆i计算得分：helpArr[i]*helpArr[l-1]*helpArr[r+1]，为什么要*helpArr[l-1]，因为i是l到r范围最后打爆的，打i的时候l到r已经没有其他没爆的了，左边最近的没爆的就是l-1，这是潜台词。右边也同理。
 */
public class Class030ShootBalloons {
    /**
     * 阶段一解法：暴力递归法
     * @param nums
     * @return
     */
    public static int force(int[] nums) {
        // 辅助数组，左右两边各插一个1
        int[] help = new int[nums.length + 2];
        help[0] = 1;
        help[help.length - 1] = 1;
        for (int i = 0; i < nums.length; i++) {
            help[i + 1] = nums[i];
        }

        // 递归，辅助数组0和n+1位置没爆，要把1到n都打爆，返回最大得分
        return process(help, 1, nums.length);
    }

    /**
     * 递归函数
     * 从l到r的气球全部打爆，返回最大得分。默认潜台词：l-1和r+1位置都还没爆
     * @param arr
     * @param l
     * @param r
     * @return
     */
    private static int process(int[] arr, int l, int r) {
        // 递归出口
        // 如果从l到r只有一个气球，那么直接计算得分
        if (l == r) {
            return arr[l] * arr[l - 1] * arr[r + 1];
        }
        // 如果是无效区域，返回得分0
        if (l > r) {
            return 0;
        }

        // 枚举每个位置作为最后一个被打爆的可能性，选取得分最大的可能性
        int max = Integer.MIN_VALUE;
        for (int i = l; i <= r; i++) {
            // i位置作为最后一个被打爆的，默认潜规则：l-1和r+1都没打爆，那么这种可能性的得分是：打爆l到i-1的得分 + 打爆i+1到r的得分 + 打爆i的得分
            max = Math.max(max, process(arr, l, i - 1) + process(arr, i + 1, r) + arr[i] * arr[l - 1] * arr[r + 1]);
        }

        return max;
    }

    /**
     * 第二阶段解法：完全位置依赖的动态规划
     * 按照暴力递归法改写即可
     * @param nums
     * @return
     */
    public static int dp(int[] nums) {
        // 辅助数组
        int[] help = new int[nums.length + 2];
        help[0] = 1;
        help[help.length - 1] = 1;
        for (int i = 0; i < nums.length; i++) {
            help[i + 1] = nums[i];
        }

        // dp表，按照递归出口对dp表初始化
        int[][] dp = new int[nums.length + 2][nums.length + 2];
        for (int i = 1; i <= nums.length; i++) {
            dp[i][i] = help[i] * help[i - 1] * help[i + 1];
        }

        // 分析依赖关系，依赖左侧格子和下方格子，因此l从大到小遍历，r从小到大遍历
        for (int l = nums.length; l > 0; l--) {
            for (int r = l; r <= nums.length; r++) {
                // 按照递归逻辑改写状态转移方程
                for (int i = l; i <= r; i++) {
                    dp[l][r] = Math.max(dp[l][r], dp[l][i - 1] + dp[i + 1][r] + help[i] * help[l - 1] * help[r + 1]);
                }
            }
        }

        return dp[1][nums.length];
    }
}
