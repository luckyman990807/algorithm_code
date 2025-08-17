package shuatiban;

/**
 * 给定一个数组arr，长度为N，表示N层汉诺塔的当前状态。arr中的值只有1，2，3三种，
 * arr[i] == 1，代表汉诺塔问题中，从上往下第i个圆盘目前在左
 * arr[i] == 2，代表汉诺塔问题中，从上往下第i个圆盘目前在中
 * arr[i] == 3，代表汉诺塔问题中，从上往下第i个圆盘目前在右
 * 那么arr整体就代表汉诺塔游戏过程中的一个状况，如果这个状况不是汉诺塔最优解运动过程中的状况，返回-1
 * 如果这个状况是汉诺塔最优解运动过程中的状态，返回它是第几个状态
 */
public class Code091汉诺塔问题的第几步 {
    /**
     * 递归，当前状态arr，是不是index+1层汉诺塔问题（第0～index层整体从from挪到to）的最优解的其中一步？
     * 是，则返回第几步，不是，则返回-1
     * @param arr 所有汉诺塔圆盘的当前状态
     * @param index 当前递归的是arr是不是0到index层从from挪到to的其中一步
     * @param from 汉诺塔问题一堆圆盘的初始位置
     * @param to 汉诺塔问题一堆圆盘要挪到的位置
     * @param another 另一个位置
     * @return
     */
    public static int process(int[] arr, int index, int from, int to, int another) {
        // 如果越界了，0层汉诺塔问题，处在第0步
        if (index == -1) {
            return 0;
        }
        // index+1层汉诺塔问题，0到index号圆盘从from挪到to
        // 如果index号也就是最大圆盘在another位置，那么这个状态绝对不可能作为最优运动过程的一步，因为index+1层汉诺塔问题就3个大步：1、0到index-1从from挪到another 2、index从from挪到to 3、0到index-1从another挪到to，index绝对不会出现在another位置，出现了就绝对不是最优运动过程
        if (arr[index] == another) {
            return -1;
        }
        // 如果index号还处在from位置，说明0到index-1还没完全从from挪到another，0到index-1的汉诺塔问题（从from到another）处在第几步，当前0到index汉诺塔就处在第几步
        if (arr[index] == from) {
            return process(arr, index - 1, from, another, to);
        }
        // 如果index号已经处在to位置，说明0到index-1已经从from挪到another，并且index已经从from挪到to，并且0到index-1已经开始从another挪到to了
        // 那么当前0到index汉诺塔就处在第几步 = 0到index-1从from到another的汉诺塔问题总步数 + index挪的1步 + 0到index-1从another到to的汉诺塔问题处在第几步
        // 前提是当前状态是0到index-1从another到to的其中一步，如果不是，那么整体的答案就不存在了，返回-1
        // n层汉诺塔问题最优运动步数公式：斐波那契数列，2^n - 1
        if (arr[index] == to) {
            int step = process(arr, index - 1, another, to, from);
            // 展开：(1 << index) - 1 + 1 + step    0到index-1是index层汉诺塔问题
            return step == -1 ? -1 : (1 << index) + step;
        }
        return -1;
    }

    public static int hanoiStep(int[] arr) {
        return process(arr, arr.length - 1, 1, 3, 2);
    }

    public static void main(String[] args) {
        int[] arr = {2, 2, 3};
        System.out.println(hanoiStep(arr));
    }
}
