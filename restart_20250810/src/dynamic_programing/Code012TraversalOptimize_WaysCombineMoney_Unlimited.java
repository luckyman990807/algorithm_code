package dynamic_programing;

/**
 * 遍历优化案例：组成aim的方法数，每个面额的货币无限张
 * 给定一个正数数组表示货币面额，每个面额的货币有无限张，给定一个正数aim，求用货币组成aim的方法数
 *
 * 思路：由于每个面额的货币有无限张，因此尝试时每张货币需要穷举出所有可能的使用张数
 */
public class Code012TraversalOptimize_WaysCombineMoney_Unlimited {
    /**
     * 解法第一阶段：暴力递归
     * @param money
     * @param aim
     * @return
     */
    public static int force(int[] money, int aim) {
        return process(money, 0, aim);
    }

    /**
     * 求面额数组money使用从index往后的所有面额组成restAim的所有方法数
     * @param money
     * @param index
     * @param restAim
     * @return
     */
    public static int process(int[] money, int index, int restAim) {
        // 边界条件，所有钱都用完了，如果恰好aim已经凑齐了，就返回一种方法数，否则返回0
        if (index == money.length) {
            return restAim == 0 ? 1 : 0;
        }

        // 遍历index使用张数的所有可能性，分别递归累计
        int ways = 0;
        for (int num = 0; num * money[index] <= restAim; num++) {
            ways += process(money, index + 1, restAim - num * money[index]);
        }

        return ways;
    }

    public static void main(String[] args) {
        int[] money = new int[]{1, 2,3};
        int aim = 7;
        System.out.println(force(money, aim));
    }
}
