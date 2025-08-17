package tixiban.class31状态压缩的动态规划;

/**
 * 给定num表示有1到num这些数可以拿,给定rest表示还可以拿不超过rest的数,rest<=0表示无数可拿,每个数拿走后就没有了,
 * 一个先手和一个后手轮流拿,谁拿完后让对方无数可拿谁就赢,返回先手是否能赢.
 *
 * ps:num的范围不超过20,rest不超过300
 */
public class Code01CanIWin {
    /**
     * 暴力递归
     * 当前是先手拿,返回先手能不能赢
     * 复杂度O(N!),因为第一局有N种选择*第二局有N-1种选择*第三局有N-2种选择...
     */
    public static boolean process(int[] arr, int rest) {
        // 如果rest已经没了,当前角色肯定输了
        if (rest <= 0) {
            return false;
        }
        // ==-1表示已经被拿过了,遍历所有没拿过的尝试
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != -1) {
                int temp = arr[i];
                arr[i] = -1;
                // 当前的后手在下一局是先手,如果当前后手在下一局输了,其实就是当前先手赢了
                if (!process(arr, rest - temp)) {
                    return true;
                }
                // 恢复现场
                arr[i] = temp;
            }
        }
        return false;
    }

    public static boolean violent(int num, int rest) {
        // 题目规定,最初rest==0时先手赢
        if (rest == 0) {
            return true;
        }
        // 如果所有数的累加和小于rest,谁都赢不了
        // 从1到num的累加和=梯形面积公式
        if ((1 + num) * num / 2 < rest) {
            return false;
        }

        int[] arr = new int[num];
        for (int i = 0; i < num; i++) {
            arr[i] = i + 1;
        }

        return process(arr, rest);
    }

    /**
     * 为什么没法直接改动态规划?
     * 因为自变量arr是个数组类型,动态规划要求自变量的复杂度不能超出整型,因为整型的状态形成dp表,数组的状态没法形成dp表.
     *
     * 是否有重复计算?
     * 有,例如arr[1,2,3,4],rest=5,第一种情况先手拿1后手拿2,arr=[-1,-1,3,4],rest=2,第二种情况先手拿2后手拿1,arr=[-1,-1,3,4],rest=2,是重复过程.
     *
     * 有重复过程就可以加缓存
     * 但是对数组不好做缓存,所以把数组压缩成整型,用一个int的比特位表示第几个数拿了没拿,比如第2位=0表示2没拿,第5位=1表示5已经拿了.
     * rest需要一起缓存吗?
     * 不需要,因为在整个过程中,数组的状态就可以唯一对应一个状态,数组一样则rest一定一样,rest一样则数组不一定一样,问题的状态和rest无关,只和数组有关.
     */

    /**
     * 傻缓存
     * 复杂度O(2^N * N)
     * 最多要填2^N个格子,填每个格子都要经历N的for循环
     */
    public static boolean processDp(int num, int rest, int status, int[] dp) {
        // 先查缓存
        if (dp[status] != 0) {
            return dp[status] == 1 ? true : false;
        }
        // 怎么试?就在给定的num范围里试
        boolean result = false;
        if (rest > 0) {
            for (int i = 1; i <= num; i++) {
                if (((1 << i) & status) == 0) {
                    if (!processDp(num, rest - num, status | (1 << num), dp)) {
                        result = true;
                        break;
                    }
                }
            }
        }
        // return之前记录缓存
        dp[status] = result ? 1 : -1;
        return result;
    }

    public static boolean dp(int num, int rest) {
        // 题目规定,最初rest==0时先手赢
        if (rest == 0) {
            return true;
        }
        // 如果所有数的累加和小于rest,谁都赢不了
        // 从1到num的累加和=梯形面积公式
        if ((1 + num) * num / 2 < rest) {
            return false;
        }

        return processDp(num, rest, 0, new int[1 << (num + 1)]);
    }

    public static void main(String[] args) {
        System.out.println(violent(5, 6));
        System.out.println(dp(5, 6));
    }
}
