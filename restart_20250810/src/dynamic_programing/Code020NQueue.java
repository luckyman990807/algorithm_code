package dynamic_programing;

/**
 * N皇后问题。给定整数n，在nxn的棋盘上摆n个皇后，要求任意两个皇后不同行，不同列，不同斜线，返回所有摆放的方法数
 * https://leetcode.cn/problems/n-queens-ii/submissions/668165170/
 *
 * 这道题没法改动态规划，只能暴力递归，因为每一个递归过程都是不重复的。
 * 为什么不重复？因为尝试某个皇后的时候，上个皇后的位置已经固定了，不可能在尝试某个皇后的时候去更改上个皇后的位置
 *
 * 思路：
 * 递归策略process(record, curRow, n)表示一共要摆n个皇后，当前要摆的是第curRow个（或者说当前要摆的是第curRow行，第几个皇后就是第几行，因为一行只能摆一个），此前摆的所有皇后的位置记录在record里，求第curRow及其后续的皇后一共有几种有效的摆法
 * record是一位数组，record[row]=col表示第row行的皇后摆在第col列，让下标作为行数，能节省一维空间
 *
 * 优化思路：用 整数位运算 代替 一维数组遍历 实现 皇后摆位限制
 * n的范围如何表示：scope = (1 << n) - 1，得到右边n个1
 * 之前皇后占用的列如何表示：colDisable，已经摆皇后的位是1
 * 之前皇后通过左斜线辐射到本行的位置如何表示：leftDiaDisable，已辐射的位是1
 * 之前皇后通过右斜线辐射到本行的位置如何表示：rightDiaDisable，已辐射的位是1
 * 通过以上限制，可推导出：
 * 如何判断已全部摆完：colDisable = scope
 * 解释：n列全部被占用，说明全部摆完了
 * 本行可摆的位置：available = (~(colDisable | leftDiaDisable | rightDiaDisable)) & scope
 * 解释：三个不可用位置或，得到本行全部不可用位置，取反后和scope相与，得到可用位置并限制在scope范围内，即本行可用位为1
 * 获取第一个可摆位置：first = ((~available)+1) & available
 * 解释：取反+1和自己与，得到得到最右边的1（最右边的1保持1，其他位被置为0）
 * 第一个可摆位置置为已摆：available -= first
 * 解释：减去最右边的1（最右边的1被置为0）
 * 下一行的列占用用：colDisable | first
 * 解释：first代表的列已被本次占用，或上
 * 下一行的左斜线占用：(leftDiaDisable | first) << 1
 * 解释：或上本次位置后还要左移一位，才是左斜线辐射到下一行的位置
 * 下一行的右斜线占用：(rightDiaDisable | first) >> 1
 *
 */
public class Code020NQueue {
    /**
     * 解法一，纯暴力递归，用一维数组记录已经摆好的皇后的位置
     * @param n
     * @return
     */
    public static int nQueue(int n) {
        return process(new int[n], 0, n);
    }

    /**
     * 当前要摆的是第curRow行，总共有n皇后，curRow之前的行所有摆过的皇后的位置记录在record里，求从curRow往后有几种摆法
     * @param record
     * @param curRow
     * @param n
     * @return
     */
    public static int process(int[] record, int curRow, int n) {
        // 如果所有行都摆完了，说明找到了一种有效的解法，返回1
        if (curRow == n) {
            return 1;
        }

        // 尝试当前行可以把皇后摆在哪一列，每行有n种可能性
        int ways = 0;
        for (int curCol = 0; curCol < n; curCol++) {
            // 如果摆在这一列是合法的，那么记录当前皇后的位置，递归下一个皇后
            if (valid(record, curRow, curCol)) {
                record[curRow] = curCol;
                ways += process(record, curRow + 1, n);
            }
        }
        return ways;
    }

    /**
     * curRow行之前的皇后的位置记录在record，当前准备把第curRow行的皇后摆在curCol列，判断是否允许摆
     * @param record
     * @param curRow
     * @param curCol
     * @return
     */
    public static boolean valid(int[] record, int curRow, int curCol) {
        for (int row = 0; row < curRow; row++) {
            int col = record[row];
            // 如果和之前的某个皇后同列，或者同斜线（行1-行2的绝对值=列1-列2的绝对值），那么这俩皇后就冲突了，不允许摆
            if (curCol == col || Math.abs(curRow - row) == Math.abs(curCol - col)) {
                return false;
            }
        }
        return true;
    }


    public static int nQueueByteOptimize(int n) {
        return processByteOptimize((1 << n) - 1, 0, 0, 0);
    }

    public static int processByteOptimize(int scope, int colDisable, int leftDiaDisable, int rightDiaDisable) {
        if (colDisable == scope) {
            return 1;
        }

        // 本行可摆的位置
        int available = (~(colDisable | leftDiaDisable | rightDiaDisable)) & scope;

        int ways = 0;
        while (available != 0) {
            // 最右边的可摆位置
            int first = ((~available) + 1) & available;
            // 从可摆位置中去掉（在这个位置摆了），是这个减操作让available逐渐减小直到=0的
            available -= first;
            // 递归
            ways += processByteOptimize(scope,
                    colDisable | first,
                    (leftDiaDisable | first) << 1,
                    (rightDiaDisable | first) >> 1);
        }

        return ways;
    }

    public static void main(String[] args) {
        int n = 1;
        System.out.println(nQueue(n));
        System.out.println(nQueueByteOptimize(n));
    }
}
