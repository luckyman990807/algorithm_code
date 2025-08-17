package shuatiban;

/**
 * 携程笔试题
 * 现有司机N*2人，调度中心会将所有司机平分给A、B两区域，i号司机去A可得收入为income[i][0]，去B可得收入为income[i][1]
 * 返回能使所有司机总收入最高的方案是多少钱?
 */
public class Code012司机调度 {
    /**
     *
     * @param income 收入价格表
     * @param index 当前该决策index号司机
     * @param rest 当前去A区域的名额还剩下rest个
     * @return 从index往后的司机全调度完能获得的最大收入
     */
    public static int process(int[][] income, int index, int rest) {
        // 如果没有司机了,返回0
        if (index == income.length) {
            return 0;
        }
        // 如果没有A区域的名额了,剩下的司机都只能去B.让当前司机去B,后面的司机交给递归,也会去B.
        if (rest == 0) {
            return income[index][1] + process(income, index + 1, rest);
        }
        // 如果A区域的名额刚好等于剩下的司机数,那么只能让剩下的司机都去A.让当前司机去A,后面的司机交给递归,也会去A.
        if (rest == income.length - index) {
            return income[index][0] + process(income, index + 1, rest - 1);
        }

        // 一般情况,有两种可能性,选择收益最大的一种.
        int p1 = income[index][0] + process(income, index + 1, rest - 1);
        int p2 = income[index][1] + process(income, index + 1, rest);
        return Math.max(p1, p2);
    }

    public static int violent(int[][] income) {
        return process(income, 0, income.length >> 1);
    }

}
