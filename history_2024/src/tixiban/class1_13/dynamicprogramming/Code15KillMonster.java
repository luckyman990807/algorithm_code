package tixiban.class1_13.dynamicprogramming;

/**
 * 给定三个参数N，M，k，表示怪兽一共有N滴血，英雄打一下等概率掉0~M滴血，英雄能打k次
 * 求把怪兽打死的概率
 */
public class Code15KillMonster {
    /**
     * 暴力递归法
     *
     * @param restHP      怪兽剩余血量
     * @param hurtRange   伤害范围0~hurtRange
     * @param restAttacks 剩余攻击次数
     * @return
     */
    public static double processViolent(int restHP, int hurtRange, int restAttacks) {
        // 攻击次数用完了的时候，如果怪兽血量<=0，那么说明找到了一种打死怪兽的方法
        if (restAttacks == 0) {
            return restHP <= 0 ? 1 : 0;
        }
        // 攻击次数没用完，如果怪兽血量已经<=0了，可以提前剪枝，这里剪枝不是直接丢弃，而是快速返回所有方法数
        // 每次攻击的伤害有M+1种可能，还剩余k次攻击，一共有M+1的k次方种可能，都是能打死怪兽的方法
        if (restHP <= 0) {
            return Math.pow(hurtRange + 1, restAttacks);
        }
        // 遍历M+1种可能性
        long ways = 0;
        for (int hurt = 0; hurt <= hurtRange; hurt++) {
            ways += processViolent(restHP - hurt, hurtRange, restAttacks - 1);
        }
        return ways;
    }

    public static double killMonsterViolent(int N, int M, int k) {
        // 打死怪兽的方法数/所有可能的方法数
        return processViolent(N, M, k) / Math.pow(M + 1, k);
    }

    /**
     * 严格表依赖的动态规划
     */
    public static double killMonsterDp(int N, int M, int k) {
        // 可变参数restHP、restAttacks，变化范围0~N、0~k
        long[][] dp = new long[N + 1][k + 1];
        // base case 1
        dp[0][0] = 1;

        for (int attacks = 1; attacks <= k; attacks++) {
            for (int hp = 0; hp <= N; hp++) {
                for (int hurt = 0; hurt <= M; hurt++) {
                    // 暴力递归中剩余血量是可以<0的，但是取负值会增加动态规划表的复杂度，所以表还是0~N，如果剩余血量能在表中取到，就按照位置依赖取，取不到就返回剪枝策略的累计方法数
                    if (hp - hurt > 0) {
                        // 减去本次伤害后的剩余血量>0，能在数组中取到，就按照位置依赖，找之前算过的格子
                        dp[hp][attacks] += dp[hp - hurt][attacks - 1];
                    } else {
                        // 减去本次伤害后的剩余血量<=0，在数组中取不到，但是剩下的attacks - 1次攻击每种可能性都是有效方法数，就直接剪枝快速返回方法数
                        dp[hp][attacks] += Math.pow(M + 1, attacks - 1);
                    }
                }
            }
        }
        return dp[N][k] / Math.pow(M + 1, k);
    }

    /**
     * 优化时间复杂度的动态规划
     */
    public static double killMonsterDpSuper(int N, int M, int k) {
        long[][] dp = new long[N + 1][k + 1];
        dp[0][0] = 1;

        for (int attacks = 1; attacks <= k; attacks++) {
            for (int hp = 0; hp <= N; hp++) {
                // 分析for循环枚举可能性的位置依赖，假设当前剩余血量2，每次攻击伤害范围0~3，剩余攻击次数3，根据动态规划中所有可能性的方法数累加和=
                // dp[2][3] = dp[2-0][2] + dp[2-1][2] + dp[2-2][2] + dp[2-3][2] = dp[2][2] + dp[1][2] + dp[0][2] + dp[-1][2]
                // 表示攻击伤害分别为0、1、2、3，攻击次数-1的方法数累加
                // 而当当前剩余血量1的时候，所有可能性的累计方法数=
                // dp[1][3] = dp[1-0][2] + dp[1-1][2] + dp[1-2][2] + dp[1-3][2] = dp[1][2] + dp[0][2] + dp[-1][2] + dp[-2][2]
                // 由上面两个式子可以推出，
                // dp[2][3] = dp[2][2] + dp[1][3] - dp[-2][2]
                // 推广得到
                // dp[hp][rest] = dp[hp][rest-1] + dp[hp-1][rest] - dp[hp-1-M][rest-1]
                // hp-1、hp-1-M 有可能越界，但是逻辑上是允许hp<0的，只是我们数组里没记，这种情况不能忽略，而是要返回这个位置的理论数据，即Math.pow(M+1, rest)

                dp[hp][attacks] = dp[hp][attacks - 1];

                // 能从表中取到就取，取不到就返回Math.pow(M+1, rest)
                if (hp - 1 >= 0) {
                    dp[hp][attacks] += dp[hp - 1][attacks];
                } else {
                    dp[hp][attacks] += Math.pow(M + 1, attacks);
                }

                if (hp - 1 - M >= 0) {
                    dp[hp][attacks] -= dp[hp - 1 - M][attacks - 1];
                } else {
                    dp[hp][attacks] -= Math.pow(M + 1, attacks - 1);
                }
            }
        }
        return dp[N][k] / Math.pow(M + 1, k);
    }


    /**
     * 主函数
     *
     * @param args
     */
    public static void main(String[] args) {
        int N = 20;
        int M = 5;
        int k = 10;
        System.out.println(killMonsterViolent(N, M, k));
        System.out.println(killMonsterDp(N, M, k));
        System.out.println(killMonsterDpSuper(N, M, k));
    }
}
