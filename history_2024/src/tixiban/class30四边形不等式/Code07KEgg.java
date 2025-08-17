package tixiban.class30四边形不等式;

/**
 * K蛋问题
 * 一座大楼有0到n层,地面是0层,最高层是n层.已知蛋从0层掉落一定不会碎,从第i层(1<=i<=n)掉落可能会碎,也可能不会碎.
 * 给定整数n作为楼层数,给定整数k作为蛋数,返回 如果想找到蛋不会摔碎的最高楼层,最差情况需要扔的最小次数.一次只能扔一个蛋.
 *
 * 举例,n=10,k=1,返回10.因为只有一个蛋,一旦碎了就没有蛋可试了,所以只能从1层一直试到10层.
 * n=3,k=2,返回2.先在2层扔一颗,如果碎了,试第1层,如果没碎,试第3层.
 */
public class Code07KEgg {

    /**
     * 暴力递归
     * 划分可能性的思路:按照第一颗蛋扔在几层楼划分.
     * 最高层为n层,有k颗蛋,f(n,k),
     * 试法i:第一颗扔在第i层,可能性1:碎了,还剩i-1层要试,还剩k-1颗蛋,f(i-1,k-1).可能性2:没碎,还剩n-i层要试,还剩k颗蛋,f(n-1,k).两种可能取最大值.
     * i从1到n遍历一边,所有试法取最小值.
     *
     * restLevel表示还剩restLevel层要试,也就是1到restLevel层
     * restEgg表示还剩restEgg颗蛋
     */
    public static int process(int restLevel, int restEgg) {
        if (restLevel == 0) {
            // 如果试到第0层,不需要试,因为题目告诉了0层一定不会碎
            return 0;
        }
        if (restEgg == 1) {
            return restLevel;
        }
        int result = Integer.MAX_VALUE;
        for (int i = 1; i <= restLevel; i++) {
            int curResult = Math.max(process(i - 1, restEgg - 1), process(restLevel - i, restEgg)) + 1;
            result = Math.min(result, curResult);
        }
        return result;
    }

    public static int violent(int level, int egg) {
        if (level <= 0) {
            return 0;
        }
        if (egg <= 0) {
            return Integer.MAX_VALUE;
        }
        return process(level, egg);
    }

    /**
     * 改动态规划,不优化
     */
    public static int dp1(int level, int egg) {
        if (level <= 0) {
            return 0;
        }
        if (egg <= 0) {
            return Integer.MAX_VALUE;
        }

        int[][] dp = new int[level + 1][egg + 1];
        // 根据递归出口赋初始值
        for (int e = 1; e <= egg; e++) {
            // 如果还剩1层要试,不管几颗蛋,都只需要试一次
            dp[1][e] = 1;
        }
        for (int l = 1; l <= level; l++) {
            // 如果还剩1颗蛋,剩几层楼就是要最多试几次
            dp[l][1] = l;
        }

        for (int restL = 2; restL <= level; restL++) {
            for (int restE = 2; restE <= egg; restE++) {
                int result = Integer.MAX_VALUE;
                // 枚举所有可能性,firstL表示第一颗蛋扔在第几层
                for (int firstL = 1; firstL < restL; firstL++) {
                    int curResult = Math.max(dp[firstL - 1][restE - 1], dp[restL - firstL][restE]) + 1;
                    result = Math.min(result, curResult);
                }
                dp[restL][restE] = result;
            }
        }

        return dp[level][egg];
    }

    /**
     * 动态规划,四边形不等式优化
     */
    public static int dp2(int level, int egg) {
        if (level <= 0) {
            return 0;
        }
        if (egg <= 0) {
            return Integer.MAX_VALUE;
        }

        int[][] dp = new int[level + 1][egg + 1];
        // 记录第一颗蛋选择试哪一层
        int[][] choice = new int[level + 1][egg + 1];
        // 根据递归出口赋初始值
        for (int e = 1; e <= egg; e++) {
            // 如果还剩1层要试,不管几颗蛋,都只需要试一次
            dp[1][e] = 1;
            // 第一颗蛋都选则试第一层
            choice[1][e] = 1;
        }
        for (int l = 1; l <= level; l++) {
            // 如果还剩1颗蛋,剩几层楼就是要最多试几次
            dp[l][1] = l;
            // 第一颗蛋都选则试第一层
            choice[l][1] = 1;
        }


        for (int restL = 2; restL <= level; restL++) {
            for (int restE = egg; restE > 1; restE--) {
                int result = Integer.MAX_VALUE;
                int choiceL = -1;
                int down = choice[restL - 1][restE];
                int up = restE == egg ? restL : choice[restL][restE + 1];
                // 只在右边和下边给出的直到范围中枚举可能性,firstL表示第一颗蛋扔在第几层
                for (int firstL = down; firstL <= up; firstL++) {
                    int curResult = Math.max(dp[firstL - 1][restE - 1], dp[restL - firstL][restE]) + 1;
                    // 这道题这里(当取得同样优秀的答案试跳过还是保留)必须是<=,不证明,对数器验
                    if (curResult <= result) {
                        result = curResult;
                        choiceL = firstL;
                    }
                    result = Math.min(result, curResult);
                }
                dp[restL][restE] = result;
                choice[restL][restE] = choiceL;
            }
        }

        return dp[level][egg];
    }

    /**
     * K蛋问题最优解
     * 思路:dp[i][j]表示i个蛋扔j次最差情况可以解决几层楼的问题,转移方程是当前格子=左边格子+左上方格子+1.先从上往下,再从左到右推,直到出现大于level的格子,返回对应的列号
     * 举例,假设dp[3][5]=10,dp[4][5]=15,那么dp[4][6]一定=26.
     * 为什么?
     * 既然3颗蛋扔5次可以解决10层楼的问题,4颗蛋扔5次可以解决15层楼的问题,那么当4颗蛋扔6次时,第一颗蛋可以扔第11层,
     * 如果碎了,下面的10层楼可以由3颗蛋、扔5次解决.
     * 如果没碎,上面的15层楼可以由4颗蛋、扔5次解决.
     * 加上当前扔的11层,一共能解决26层.
     *
     */
    public static int kEgg(int level, int egg) {
        // 最多尝试次数应该是楼层数,但是没必要,直接不限定列数,只拿一列从左往右推,空间压缩法
        int[] dp = new int[egg + 1];
        // 初始值,扔1次,不管有几颗蛋,最差都只能解决1层的问题
        for (int e = 1; e <= egg; e++) {
            dp[e] = 1;
        }
        // 扔的次数
        int time = 1;
        while (true) {
            time++;
            // 空间压缩法就要从下往上填,因为下面依赖上面,不能让上面先改
            for (int e = egg; e > 0; e--) {
                dp[e] = dp[e] + dp[e - 1] + 1;
                if (dp[e] >= level) {
                    return time;
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(violent(10, 1));
        System.out.println(violent(3, 2));

        System.out.println("动态规划");

        System.out.println(dp1(10, 1));
        System.out.println(dp1(3, 2));

        System.out.println("动态规划四边形不等式优化");

        System.out.println(dp2(10, 1));
        System.out.println(dp2(3, 2));

        System.out.println("最优解");
        System.out.println(kEgg(10, 1));
        System.out.println(kEgg(3, 2));
    }
}
