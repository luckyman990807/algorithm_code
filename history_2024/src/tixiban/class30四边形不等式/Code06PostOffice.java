package tixiban.class30四边形不等式;

/**
 * 邮局问题
 *
 * 一条直线上有居民点,邮局只能建在居民点上.给定一个有序正数数组arr,每个值表示居民点的一维坐标,再给定一个正数num,表示邮局的数量.
 * 选择num个居民点建立num个邮局,使所有居民点到最近邮局的总距离最短,返回最短的总距离.
 *
 * 思路:和画家问题一样,枚举可能性的方式也是让第一个邮局负责几个点.
 *
 * w[l][r]表示从l点到r点只建一个邮局,这个邮局到[l,r]范围内的总代价是多少
 *
 */
public class Code06PostOffice {
    /**
     * 构建辅助数组bestForOne[i][j]表示从i到j只建1个邮局,最优建在哪个点
     */
    public static int[][] bestForOneOffice(int[] arr) {
        int[][] bestForOne = new int[arr.length + 1][arr.length + 1];
        for (int l = 0; l < arr.length; l++) {
            for (int r = l + 1; r < arr.length; r++) {
                // 前置知识:高中数学,不管权重多少,中点到所有点的距离和最短.如果是奇数个,取严格中点,如果是偶数个,左右两个中点都一样最短.
                // 如果j前面有奇数个,那么之前的邮局一定在中点,把j加进来,邮局可以还在原来的中点不动,所以当前总距离=原来总距离+邮局到j的距离
                // 如果j前面有偶数个,那么之前的邮局可以在左中点也可以在右中点,就假设右中点.把j加进来,邮局就变成在严格中点,也不用动,也只需要+邮局到j的距离.
                bestForOne[l][r] = bestForOne[l][r - 1] + arr[r] - arr[(r + l) / 2];
            }
        }

        return bestForOne;
    }

    /**
     * 动态规划
     * dp[i][j]表示从0到i,建j个邮局,总距离最短是多少
     */
    public static int dp1(int[] arr, int num) {
        int[][] bestForOne = bestForOneOffice(arr);

        int[][] dp = new int[arr.length][num + 1];
        // 初始值
        // 建0个邮局,不管从哪到哪,总距离都是0.从0到0,不管建多少个邮局,总距离都是0.
        // 从0到i,建1个邮局,结果直接从辅助数组中取
        for (int i = 0; i < arr.length; i++) {
            dp[i][1] = bestForOne[0][i];
        }

        for (int i = 1; i < arr.length; i++) {
            for (int n = 2; n <= Math.min(num, i); n++) {
                int result = Integer.MAX_VALUE;
                for (int index = 0; index <= i; index++) {
                    result = Math.min(result, dp[index][n - 1] + bestForOne[index + 1][i]);
                }
                dp[i][n] = result;
            }
        }
        return dp[arr.length - 1][num];
    }

    public static int dp2(int[] arr, int num) {
        int[][] bestForOne = bestForOneOffice(arr);

        int[][] dp = new int[arr.length][num + 1];
        int[][] split = new int[arr.length][num + 1];
        // 初始值
        // 建0个邮局,不管从哪到哪,总距离都是0.从0到0,不管建多少个邮局,总距离都是0.
        // 从0到i,建1个邮局,结果直接从辅助数组中取,最佳划分点-1,因为不需要划分(划分指的是某个范围建1个邮局,剩下的范围交给剩下到邮局)
        for (int i = 0; i < arr.length; i++) {
            dp[i][1] = bestForOne[0][i];
            split[i][1] = -1;
        }


        for (int j = 2; j <= num; j++) {
            for (int i = arr.length - 1; i > 0; i--) {
                int result = Integer.MAX_VALUE;
                int splitPoint = -1;
                // 从左边格子拿下限
                int down = split[i][j - 1];
                // 从下边格子拿上限
                int up = i == arr.length - 1 ? arr.length - 1 : split[i + 1][j];
                for (int leftEnd = down; leftEnd <= up; leftEnd++) {
                    // leftEnd是第一个邮局和剩下邮局管辖范围的分界线,leftEnd+1到i建一个邮局,0到leftEnd建剩下到邮局.
                    // 如果从左边格子拿到到下限是-1,说明这个划分下,都交给第一个邮局,剩下的邮局啥也不干,所以剩下邮局的总距离就是0
                    int othersCost = leftEnd == -1 ? 0 : dp[leftEnd][j - 1];
                    int oneCost = bestForOne[leftEnd + 1][i];
                    int cost = othersCost + oneCost;
                    /** 这道题< 和<=都能过,不证明,对数器试 */
                    if (cost <= result) {
                        result = cost;
                        splitPoint = leftEnd;
                    }
                }
                dp[i][j] = result;
                split[i][j] = splitPoint;
            }
        }
        return dp[arr.length - 1][num];
    }

    public static void main(String[] args) {
        int[] arr = {2, 4, 7, 9};
        int num = 2;
        System.out.println(dp1(arr, num));
        System.out.println(dp2(arr, num));
    }
}
