package tixiban.class14slidingwindow;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * 多个加油站围成一圈，给定两个数组，gas表示每个加油站有多少油，cost表示每个加油站到下个加油站要耗多少油。
 * 一辆汽车初始油量是0，返回从哪个加油站开始走，可以完整走完一圈。
 * <p>
 * 思路：
 * 用一个rest数组=gas-cost，在这个加油站能加的油-到下个加油站耗的油，也就是记录每个加油站，经过这个加油站之后汽车油量的变化。
 * 然后求前缀和，如果出现某个位置前缀和<0了，说明到不了这个加油站，也就无法走完一圈。
 * 因为可以从任意一个加油站出发走完一圈，所以rest用两倍数组长度，实现循环，让每个加油站开始都能计算一圈
 * <p>
 * 注意：
 * 用两倍数组之后，从i加油站出发走一圈就是i～i+N，判断从i能都走完一圈应该求i到i+N的累加和，
 * 但是求完前缀和后，rest[i+N]记录的是0到i+N的累加和，所以用的时候要减去rest[i-1]
 */
public class Code3GasStationCircle {
    public static boolean[] getCanCircleList(int[] gas, int[] cost) {
        // 加油站的个数
        int n = gas.length;
        // 记录每个加哟站，经过这个加油站后汽车油量的变化
        int[] rest = new int[n << 1];
        for (int i = 0; i < n; i++) {
            rest[i] = gas[i] - cost[i];
            rest[i + n] = gas[i] - cost[i];
        }

        // 求前缀和
        for (int i = 1; i < rest.length; i++) {
            rest[i] = rest[i - 1] + rest[i];
        }

        // 滑动窗口
        LinkedList<Integer> minQueue = new LinkedList<>();
        // 滑动窗口形成的过程
        for (int r = 0; r < n; r++) {
            while (!minQueue.isEmpty() && !(rest[minQueue.peekLast()] < rest[r])) {
                minQueue.pollLast();
            }
            minQueue.addLast(r);
        }

        // 记录返回结果，每一个加油站作为起点能否走完一圈
        boolean[] canCircle = new boolean[n];
        // offset是为求l到r的累加和而减去的偏移量，即sum(l,r)=sum(r)-sum(l-1)，l、r是滑动窗口左右边界
        for (int offset = 0, l = 0, r = n; r < n << 1; offset = rest[l], l++, r++) {
            // 如果窗口内的最小值都>=0，说明窗口内都>=0，到每个加油站的油量浮动累加和都>=0，就说明能完整走完一圈
            if (rest[minQueue.peekFirst()] - offset >= 0) {
                canCircle[l] = true;
            }

            // 右端滑入，滑入前删除无用的
            while (!minQueue.isEmpty() && !(rest[minQueue.peekLast()] < rest[r])) {
                minQueue.pollLast();
            }
            minQueue.addLast(r);

            // 左端滑出
            if (minQueue.peekFirst() == l) {
                minQueue.pollFirst();
            }
        }
        return canCircle;
    }

    public static int canCircle(int[] gas, int[] cost) {
        boolean[] resultList = getCanCircleList(gas, cost);
        // 找到一个可以走完的就返回当前下标
        for (int i = 0; i < gas.length; i++) {
            if (resultList[i]) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] gas = {1, 2, 3, 4};
        int[] cost = {3, 4, 1, 2};
        System.out.println(Arrays.toString(getCanCircleList(gas, cost)));
    }
}
