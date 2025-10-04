package dynamic_programing.Code011SlopeOptimize;

/**
 * 给定3个参数n，m，k
 * 怪兽有n滴血，每次攻击会让怪兽掉[0,m]滴血，求k次攻击之后怪兽被打死的概率
 *
 * 思路：
 * 穷举可能性，斜率优化
 * 概率 = k次攻击后怪被杀死的情况数 / k次攻击的所有情况数
 */
public class Code015KillTheMonster {
    /**
     * 解法第一阶段：暴力递归
     * @param n
     * @param m
     * @param k
     * @return
     */
    public static double force(int n, int m, int k) {
        return (double) process(n, m, k) / Math.pow(m + 1, k);
    }

    /**
     * 怪物剩余血量为restHp，剩余攻击次数为restTimes，每次攻击伤害的范围是[0,damageRange]，求怪物被打死的情况数
     * @param restHp
     * @param damageRange
     * @param restTimes
     * @return
     */
    public static int process(int restHp, int damageRange, int restTimes) {
        // 边界条件
        // 如果剩余攻击次数=0，只有怪兽血量也<=0，才算一种杀死怪兽的情况，否则不算。
        if (restTimes == 0) {
            return restHp <= 0 ? 1 : 0;
        }
        // 如果剩余攻击次数不为0，但是怪兽血量已经<=0了，那么剩余几次攻击次数，每次攻击打出任意伤害都算一种杀死怪兽的情况，总共damageRange+1的restTimes次方种
        if (restHp <= 0) {
            return (int) Math.pow(damageRange + 1, restTimes);
        }

        // 罗列可能性，穷举所有可能的伤害值
        int ways = 0;
        for (int damage = 0; damage <= damageRange; damage++) {
            ways += process(restHp - damage, damageRange, restTimes - 1);
        }

        return ways;
    }


    /**
     * 解法第二阶段：动态规划（严格位置依赖）
     * @param hp
     * @param damageRange
     * @param attackTimes
     * @return
     */
    public static double dp(int hp, int damageRange, int attackTimes) {
        int[][] dp = new int[hp + 1][attackTimes + 1];

        // 初始状态
        // 攻击次数为0时，只有当怪兽血量也为0，才算一种杀死怪兽的情况数，否则为0。也就是第0列，只有第0行为1，其余行为0
        dp[0][0] = 1;
        // 怪兽血量为0时，剩余多少攻击次数，情况数就有Math.power(攻击范围+1, 剩余攻击次数)。也就是第0行，没列的值为Math.power(攻击范围+1, 列)
        for (int restTimes = 1; restTimes < attackTimes; restTimes++) {
            dp[0][restTimes] = (int) Math.pow(damageRange + 1, restTimes);
        }

        // 状态转移
        for (int restHp = 1; restHp <= hp; restHp++) {
            for (int restTimes = 1; restTimes <= attackTimes; restTimes++) {
                for (int damage = 0; damage <= damageRange; damage++) {
                    // 依赖前一列上边减去damage的格子
                    if (restHp - damage < 0) {
                        // 越界处理，
                        // 如果本次攻击已经打没血了，restTimes次攻击杀死怪兽的情况数将增加 damageRange的restTime-1次方，也就是后面几次攻击打出任意伤害均可以
                        // 为什么越界也要算？因为这道题怪物血量<0时也是有意义的，只不过用数组记录负值很麻烦，所以我们没有记录，转为用公式计算。
                        dp[restHp][restTimes] += (int) Math.pow(damageRange, restTimes - 1);
                    } else {
                        dp[restHp][restTimes] += dp[restHp - damage][restTimes - 1];
                    }
                }
            }
        }

        return (double) dp[hp][attackTimes] / Math.pow(damageRange + 1, attackTimes);
    }


    /**
     * 解法第三阶段：动态规划，斜率优化
     */
    public static double dpSlopeOpt(int hp, int damageRange, int attackTimes) {
        int[][] dp = new int[hp + 1][attackTimes + 1];
        dp[0][0] = 1;
        for (int restTimes = 0; restTimes <= attackTimes; restTimes++) {
            dp[0][restTimes] = (int) Math.pow(damageRange + 1, restTimes);
        }

        for (int restHp = 1; restHp <= hp; restHp++) {
            for (int restTimes = 1; restTimes <= attackTimes; restTimes++) {
                // 这里restHp从1开始遍历，restHp - 1肯定不越界，无需判断
                dp[restHp][restTimes] = dp[restHp][restTimes - 1] + dp[restHp - 1][restTimes - 1];
                if (restHp - damageRange - 1 >= 0) {
                    dp[restHp][restTimes] -= dp[restHp - damageRange - 1][restTimes - 1];
                } else {
                    /**
                     * 这里是关键的一步，忘记减就错了！！！！！
                     * 货币组成aim的斜率优化，多出来那个格子如果越界了就不用减了，是因为越界位置是无意义的，没有值，计算左边减掉一张的格子时也不会加上去。
                     * 但是这道题不同，即便是越界的位置也有意义也有值，意义是当下前怪兽已经被杀死了，后续不管打出多少伤害都算杀死怪兽的情况数，值为Math.power(damageRange+1, restTimes-1)。
                     * 结合优化前此处的越界处理一块看，优化前，没越界的话按照格子里的值加，越界的话按照公式加，那么优化后也一样，没越界的话按照格子里的减，越界的话按照公式减
                     */
                    dp[restHp][restTimes] -= (int) Math.pow(damageRange + 1, restTimes - 1);
                }
            }
        }

        return (double) dp[hp][attackTimes] / Math.pow(damageRange + 1, attackTimes);
    }


    public static void main(String[] args) {
        int n = 5;
        int m = 2;
        int k = 3;
        System.out.println(force(n, m, k));
        System.out.println(dp(n, m, k));
        System.out.println(dpSlopeOpt(n, m, k));
    }

}
