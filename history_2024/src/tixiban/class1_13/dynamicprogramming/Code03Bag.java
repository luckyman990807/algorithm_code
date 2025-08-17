package tixiban.class1_13.dynamicprogramming;

/**
 * 背包问题
 * 有一排货物，数组w表示货物重量，数组v表示货物价值，默认w和v长度相等，重量和价值非负，
 * 有一个背包，给定背包最大重量，问如何选取获取使得背包装下的货物价值最大
 */
public class Code03Bag {
    /**
     * 暴力递归法
     */

    /**
     * 递归尝试策略，给定一组货物的价值数组、重量数组、当前遍历到第几个货物、当前背包剩余多少载重，返回抛弃或装下当前货物能获得的最大价值
     *
     * @param w        货物重量数组
     * @param v        货物价值数组
     * @param index    当前遍历到第几个货物
     * @param restLoad 当前背包剩余多少载重
     * @return
     */
    public static int processViolent(int[] w, int[] v, int index, int restLoad) {
        // 如果背包剩余载重小于0了，说明这次计算是无效计算，返回负数可以告知上游这是个无效结果（其实后边递归调用前做好校验的话，就不用在这里校验了）
        // 为什么不判断<=0？因为允许有重量为0的货物
        if (restLoad < 0) {
            return -1;
        }
        // 如果index越界了，说明现在已经没有货物可遍历了，返回价值是0
        if (index == w.length) {
            return 0;
        }

        // 抛弃当前货物，后面能获得的最大价值
        int abandon = processViolent(w, v, index + 1, restLoad);
        // 装下当前货物，后面能获得的最大价值
        // 如果剩余载重都小于当前货物重量了，就说明装不下当前货物，这个情况的最大价值是0；否则，价值加上当前价值，载重减去当前重量
        int select = restLoad < w[index] ? 0 : v[index] + processViolent(w, v, index + 1, restLoad - w[index]);
        // 两种情况选择最大值返回
        return Math.max(abandon, select);
    }

    public static int bagViolent(int[] w, int[] v, int bagLoad) {
        if (w == null || v == null || w.length != v.length || w.length == 0) {
            return 0;
        }
        return processViolent(w, v, 0, bagLoad);
    }


    /**
     * 动态规划法
     */

    /**
     * 由暴力递归尝试策略推状态转移方程，根据状态转移填充动态规划表
     * 1、当index = w.length时，f(index, restLoad) = 0
     * 2、否则 2.1 当抛弃index货物时，f(index, restLoad) = f(index+1, restLoad),
     * 2.2 当不抛弃index货物时 2.2.1当restLoad < w[index]时，f(index, restLoad) = 0,
     * 2.2.2否则 f(index, restLoad) = v[index] + f(index+1, restLoad-w[index])
     * 抛弃和不抛弃选择最大价值
     */
    public static int bag(int[] w, int[] v, int bagLoad) {
        if (w == null || v == null || w.length != v.length || w.length == 0) {
            return 0;
        }

        // 递归函数中的可变参数：index、restLoad，变化范围分别是：[0, N]、[0, bagLoad]，推出动态规划表长度：
        int[][] map = new int[w.length + 1][bagLoad + 1];

        // 根据条件1填充
        for (int i = 0; i <= bagLoad; i++) {
            map[w.length][i] = 0;
        }
        // 根据条件2填充
        for (int restLoad = 0; restLoad <= bagLoad; restLoad++) {
            for (int index = w.length - 1; index >= 0; index--) {
                int abandon = map[index + 1][restLoad];
                int select = restLoad < w[index] ? 0 : v[index] + map[index + 1][restLoad - w[index]];
                map[index][restLoad] = Math.max(abandon, select);
            }
        }

        return map[0][bagLoad];
    }

    public static void main(String[] args) {
        int[] weights = {3, 2, 4, 7, 3, 1, 7};
        int[] values = {5, 6, 3, 19, 12, 4, 2};
        int bag = 15;
        System.out.println(bagViolent(weights, values, bag));
        System.out.println(bag(weights, values, bag));
    }
}
