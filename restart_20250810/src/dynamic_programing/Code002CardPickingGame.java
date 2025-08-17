package dynamic_programing;

/**
 * 给定一个数组，数组中每个元素代表一张牌的分数。有两个玩家，轮流从数组中取牌，每次只能从数组开头或末尾取牌，返回最后赢家的得分。
 *
 * 拆解问题：
 * 从l到r位置先手取牌得到的最大分数 = max(l位置分数 + 从l+1到r位置后手取牌得到的最大分数，r位置分数 + 从l到r-1位置后手取牌得到的最大分数)
 */
public class Code002CardPickingGame {
    /**
     * 第一阶段解法：暴力递归
     * @param arr
     * @return
     */
    public static int force(int[] arr) {
        // 在数组arr从0到n-1范围内，作为先手取牌，能拿到的最大分数
        int firstMax = processFirst(arr, 0, arr.length - 1);
        // 在数组arr从0到n-1范围内，作为后手取牌，能拿到的最大分数
        int secondMax = processSecond(arr, 0, arr.length - 1);
        // 返回最大值
        return Math.max(firstMax, secondMax);
    }

    // 在数组arr从l到r范围内，我作为先手取牌，最终能拿到的最大分数
    public static int processFirst(int[] arr, int l, int r) {
        // base case：如果只剩一张牌了，作为先手，能拿到的分数就是这张牌，没有别的选择
        if (l == r) {
            return arr[l];
        }

        // common case：有两种可能性
        // 1、我取l位置的牌，然后在l+1到r位置上作为后手去争取最大分数
        // 2、我取r位置的牌，然后在l到r-1位置上作为后手去争取最大分数
        // 我选择那种情况呢？我必然会选择得分更大的情况
        return Math.max(arr[l] + processSecond(arr, l + 1, r), arr[r] + processSecond(arr, l, r - 1));
    }

    // 在数组arr从l到r范围内，我作为后手取牌，最终能拿到的最大分数
    public static int processSecond(int[] arr, int l, int r) {
        // base case：如果只剩一张牌了，作为后手，什么也拿不到，因为牌会被先手拿走
        if (l == r) {
            return 0;
        }

        // common case：有两种可能性
        // 1、l位置的牌被先手取走，我在l+1到r位置上作为先手去争取最大分数
        // 2、r位置的牌被先手取走，我在l到r-1位置上作为先手去争取最大分数
        // 这里就不是我来选择哪种情况了，而是我能得到哪种情况，因为选择权在先手手里。而先手必然会选择让他得分更大的情况
        // 又因为这个游戏是零和博弈，先手得分更大的情况，就是我得分更小的情况，所以我得到的只能是两种情况中我得分更小的情况
        // 也就是说我能拿到的最大分数是 min(l被先手取走，我在l+1到r作为先手争取最大分数, r被先手取走，我在l到r-1作为先手争取最大分数)
        return Math.min(processFirst(arr, l + 1, r), processFirst(arr, l, r - 1));
    }

    /**
     * 阶段二解法：记忆化搜索（自顶向下的动态规划）（递归+缓存）
     * @param arr
     * @return
     */
    public static int memory(int[] arr) {
        // 缓存表，有先手和后手两个递归，所以建两张缓存表
        int[][] dpFirst = new int[arr.length][arr.length];
        int[][] dpSecond = new int[arr.length][arr.length];
        // 缓存初始化，-1表示没有缓存
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                dpFirst[i][j] = -1;
                dpSecond[i][j] = -1;
            }
        }

        return Math.max(processFirst(arr, 0, arr.length - 1, dpFirst), processSecond(arr, 0, arr.length - 1, dpSecond));
    }

    // 在数组arr从l到r范围内，我作为先手取牌，最终能拿到的最大分数，缓存中间结果
    public static int processFirst(int[] arr, int l, int r, int[][] dp) {
        // 如果有缓存，就直接返回
        if (dp[l][r] != -1) {
            return dp[l][r];
        }

        // 没有缓存，就递归计算，并缓存结果
        int ans;
        if (l == r) {
            ans = arr[l];
        } else {
            ans = Math.max(arr[l] + processSecond(arr, l + 1, r), arr[r] + processSecond(arr, l, r - 1));
        }
        dp[l][r] = ans;
        return ans;
    }

    // 在数组arr从l到r范围内，我作为后手取牌，最终能拿到的最大分数，缓存中间结果
    public static int processSecond(int[] arr, int l, int r, int[][] dp) {
        // 如果有缓存，就直接返回
        if (dp[l][r] != -1) {
            return dp[l][r];
        }

        // 没有缓存，就递归计算，并缓存结果
        int ans;
        if (l == r) {
            ans = 0;
        } else {
            ans = Math.min(processFirst(arr, l + 1, r), processFirst(arr, l, r - 1));
        }
        dp[l][r] = ans;
        return ans;
    }

    /**
     * 第三阶段解法：动态规划
     * @param arr
     * @return
     */
    public static int dp(int[] arr) {
        // dp表
        // dpFirst[l][r]代表在数组从l到r范围内作为先手取牌，最终能拿到的最大分数
        // dpSecond[l][r]代表在数组从l到r范围内作为后手取牌，最终能拿到的最大分数
        int[][] dpFirst = new int[arr.length][arr.length];
        int[][] dpSecond = new int[arr.length][arr.length];

        // 初始状态：当只剩一张牌时，先手在这一步的分数就是这张牌的分数，后手这一步的分数就是0
        // 即：当l=r时，dpFirst[l][r]=arr[l]，dpSecond[l][r]=0
        // 即：dpFirst对角线元素为arr[i]，dpSecond对角线元素为0
        for (int i = 0; i < arr.length; i++) {
            dpFirst[i][i] = arr[i];
            dpSecond[i][i] = 0;
        }

        // 状态转移：
        // l到r上先手的分数，依赖l+1到r上后手的分数和l到r-1上后手的分数；l到r上后手的分数，依赖l+1到r上先手的分数和l到r+1上先手的分数
        // 即：dpFirst[l][r]依赖dpSecond[l+1][r]和dpSecond[l][r-1]，dpSecond[l][r]依赖dpFirst[l+1][r]和dpFirst[l][r-1]，一张表依赖另一张表的下方和左方
        // 由于两张表对角线的值是初始已知的，因此可以一条斜对角线一条斜对角线的往上推
        for (int startCol = 1; startCol < arr.length; startCol++) {
            // startCol是斜对角线起始的列
            // l是斜对角线元素的行，业务上代表本轮取数的左边界
            // r是斜对角线元素的列，业务上代表本轮取数的右边界
            int l = 0;
            int r = startCol;
            // 从0,startCol位置开始斜着推一条线
            while (r < arr.length) {
                dpFirst[l][r] = Math.max(arr[l] + dpSecond[l + 1][r], arr[r] + dpSecond[l][r - 1]);
                dpSecond[l][r] = Math.min(dpFirst[l + 1][r], dpFirst[l][r - 1]);
                l++;
                r++;
            }
        }

        // 返回从0到n-1先手取数的最大分数，和从0到n-1后手取数的最大分数，之中较大的那个
        return Math.max(dpFirst[0][arr.length - 1], dpSecond[0][arr.length - 1]);
    }

    public static void main(String[] args) {
        int[] arr = {50, 100, 20, 10};
        System.out.println(force(arr));
        System.out.println(memory(arr));
        System.out.println(dp(arr));
    }

}
