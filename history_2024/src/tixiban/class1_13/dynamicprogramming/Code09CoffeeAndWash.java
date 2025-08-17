package tixiban.class1_13.dynamicprogramming;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 咖啡机问题
 * 给定一个数组arr，arr[i]表示第i号咖啡机泡一杯咖啡的时间
 * 给定一个正数N，表示将有N个人同时冲向咖啡机
 * 一台咖啡机只能串行用，不同咖啡机之间可以并行用
 * 只有一台洗杯机，一次只能洗一个杯子，洗干净杯子花费时间a
 * 每个杯子也可以自己挥发干净，花费时间b，杯子可以并行挥发
 * 假设所有人拿到咖啡后立刻喝完，然后就去洗/挥发杯子，每个人有一个杯子
 * 求N个人从开始冲向咖啡机到所有杯子变干净的最短时间
 * <p>
 * 思路：
 * 可以分为两个过程：泡咖啡过程求每个人最早泡完的时间（小根堆）+喝完咖啡后所有杯子变干净的最短时间（暴力递归改动态规划）
 */
public class Code09CoffeeAndWash {
    /**
     * 咖啡机入堆辅助类
     */
    public static class Machine {
        // 咖啡机什么时间点可用
        public int freeTimePoint;
        // 咖啡机泡一杯咖啡花费的时间
        public int workTime;

        public Machine(int workTime, int freeTimePoint) {
            this.workTime = workTime;
            this.freeTimePoint = freeTimePoint;
        }
    }

    /**
     * 给定一个数组workTime表示每台咖啡机泡一杯咖啡的耗时，给定一个整数number表示同时有这么多人冲向这些咖啡机
     * 求每个人泡好咖啡的最早时间点
     *
     * @param workTime workTime[i]表示第i号咖啡机泡一杯咖啡的耗时
     * @param number   表示有number个人同时排队
     * @return 每个人泡好咖啡的最早时间
     */
    public static int[] getDrinkOverTimePoint(int[] workTime, int number) {
        // 定义一个小根堆，比较器是，可用时间+工作耗时越小，越排在堆顶
        PriorityQueue<Machine> heap = new PriorityQueue<>(Comparator.comparingInt(o -> o.freeTimePoint + o.workTime));
        // 把工作耗封装成Machine对象入堆
        for (int i = 0; i < workTime.length; i++) {
            heap.add(new Machine(workTime[i], 0));
        }
        // 记录结果
        int[] drinkOverTimePoint = new int[number];

        // 每个人循环一次
        for (int i = 0; i < number; i++) {
            // 弹出可用时间点+工作耗时最小的一个，也就是能最快泡好咖啡的那个咖啡机
            Machine machine = heap.poll();
            // i号人得到咖啡的时间点=咖啡机可用时间点+泡一杯咖啡的耗时
            drinkOverTimePoint[i] = machine.freeTimePoint + machine.workTime;
            // 咖啡机可用时间点要后移到泡完这杯咖啡之后
            machine.freeTimePoint += machine.workTime;
            // 当前咖啡机重新入堆
            heap.add(machine);
        }

        return drinkOverTimePoint;
    }

    /**
     * 洗杯子部分方法一：暴力递归
     */

    /**
     * 给定一个数组startTimePoint表示每个杯子开始clean（喝完咖啡，准备弄干净）的时间点，给定洗杯机洗干净一个杯子的耗时，给定杯子自己挥发干净的耗时
     * 求从index杯子开始往后都弄干净的时间点
     *
     * @param startTimePoint 杯子喝完等着clean的时间点
     * @param washTime       洗杯机洗干净一个杯子的时间
     * @param selfCleanTime  杯子自己挥发干净的时间
     * @param index          当前正在考虑哪个杯子，index之前的杯子不考虑
     * @param freeTimePoint  洗杯机可用的时间点
     * @return
     */
    public static int processViolent(int[] startTimePoint, int washTime, int selfCleanTime, int index, int freeTimePoint) {
        // startTimePoint.length就是人数就是杯子的个数
        if (index == startTimePoint.length) {
            return 0;
        }

        // 可能性1、当前杯子选择用洗杯机洗
        // 当前杯子洗干净的时间点=当前杯子能洗的时间点（当前杯子得喝完等着clean了，并且洗杯机也可用了）+洗干净一个杯子的耗时
        int curWashOverTimePoint = Math.max(startTimePoint[index], freeTimePoint) + washTime;
        // 其他杯子弄干净的时间点，传给下一层的参数 咖啡机可用的时间点=当前杯子洗干净的时间点
        int othersOverTimePoint1 = processViolent(startTimePoint, washTime, selfCleanTime, index + 1, curWashOverTimePoint);
        // 可能当前杯子先洗干净，也可能其他杯子先弄干净（例如洗一个耗时100，自己挥发耗时1），那么从当前往后整体弄干净的时间点=二者取最大值
        int p1 = Math.max(curWashOverTimePoint, othersOverTimePoint1);

        // 可能性2、当前杯子选择自己挥发
        // 当前杯子挥发干净的时间点=被子开始clean的时间+挥发一个杯子的耗时
        int curSelfOverTimePoint = startTimePoint[index] + selfCleanTime;
        // 其他杯子弄干净的时间点，传给下一层的参数 咖啡机可用的时间点不变
        int othersOverTimePoint2 = processViolent(startTimePoint, washTime, selfCleanTime, index + 1, freeTimePoint);
        // 可能当前杯子先挥发干净，也可能其他杯子先弄干净（例如挥发一个耗时100，洗一个耗时1），那么从当前往后整体弄干净的时间点=二者取最大值
        int p2 = Math.max(curSelfOverTimePoint, othersOverTimePoint2);

        // 两种可能性取最小值
        return Math.min(p1, p2);
    }

    public static int getCleanOverTimePointViolent(int[] workTime, int number, int washTime, int selfCleanTime) {
        int[] drinkOverTimePoint = getDrinkOverTimePoint(workTime, number);
        return processViolent(drinkOverTimePoint, washTime, selfCleanTime, 0, 0);
    }

    /**
     * 洗杯子部分方法二：动态规划表
     */

    public static int getCleanOverTimePointDp(int[] startTimePoint, int washTime, int selfCleanTime) {
        // startTimePoint记录每个杯子是什么时间点喝完开始clean，startTimePoint.length就是杯子的数量，也就是人数
        int number = startTimePoint.length;

        // 递归函数中的可变参数freeTimePoint洗杯机可用时间点，如何确定变化范围？
        // 用业务限制模型，最坏情况，所有杯子都不挥发，都排队来洗，可用时间能被拖到多晚，就是变化范围
        int maxFreeTimePoint = 0;
        for (int i = 0; i < startTimePoint.length; i++) {
            maxFreeTimePoint = Math.max(maxFreeTimePoint, startTimePoint[i]) + washTime;
        }

        // 递归函数中的可变参数：index当前考虑到第几个杯子、freeTimePoint洗杯机可用时间点，变化范围0~number、0~maxFreeTimePoint
        int[][] dp = new int[number + 1][maxFreeTimePoint + 1];
        // 根据base case，dp[number][*]=0，但是数组初始化就是0，不需要特意赋值了

        // 因为index依赖index-1，所以先按index遍历，保证当前计算的位置的依赖位置都已经计算了
        for (int index = number - 1; index >= 0; index--) {
            for (int freeTimePoint = 0; freeTimePoint <= maxFreeTimePoint; freeTimePoint++) {
                // 当前杯子决定要用洗杯机洗
                int curWashOverTimePoint = Math.max(startTimePoint[index], freeTimePoint) + washTime;
                // 前面根据业务限制出了洗杯机可用时间能拖到的最大值，如果遍历过程中算出的可用时间大于这个值，直接跳过，否则数组会越界
                // 因为这个值是不可能在递归中出现的，就像在范围上动态规划，左边界不可能大于右边界一个道理
                if(curWashOverTimePoint>maxFreeTimePoint){
                    continue;
                }
                int othersOverTimePoint1 = dp[index + 1][curWashOverTimePoint];
                int p1 = Math.max(curWashOverTimePoint, othersOverTimePoint1);

                // 当前杯子决定要自己挥发
                int curSelfOverTimePoint = startTimePoint[index] + selfCleanTime;
                int othersOverTimePoint2 = dp[index + 1][freeTimePoint];
                int p2 = Math.max(curSelfOverTimePoint, othersOverTimePoint2);

                dp[index][freeTimePoint] = Math.min(p1, p2);
            }
        }

        return dp[0][0];
    }
}
