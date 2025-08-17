package tixiban.class1_13.heap;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class MaxCoverLine {
    /**
     * 最大重合线段问题
     * 题目：
     * 给定很多线段，线段的端点用数组[start, end]表示，端点都是整数，区间都是闭区间。求线段重合最多的区域有几条线段（重合区域长度>=1）
     * 思路：
     * 一段重合区域的左端点，一定是某个线段的左端点，所以遍历每条线段的左端点就相当于遍历每个以该端点开始的重合区域。
     * 那么以某线段左端点为开始的重合区域，包含几条线段？就等于该线段之间落了几条线段的右端点
     * 解法：
     * 所有线段按左端点排序，依次遍历线段，每条线段把右端点入堆，并弹出堆中小于自己左端点的数据，计算当前堆的size，size最大的就是重合线段最多的
     * 为什么要排序？
     * 因为对于一个线段，左端点在我之前的那些线段，才有可能成为以我左端点开始的重合区域的一部分，左端点在我之后的线段，属于后面的重合区域，不属于我左端点开始的重合区域
     * 为什么要弹出小于自己左端点的数据？
     * 因为堆中存放的是经过以‘当前线段左端点为开始的重合区域‘的所有线段的右端点，如果前面线段的右端点小于当前线段的左端点，说明二者没有重合。
     */
    public static int maxCover(int[][] arr) {
        Line[] lines = new Line[arr.length];
        // 转换成Line对象的数组
        for (int i = 0; i < arr.length; i++) {
            lines[i] = new Line(arr[i][0], arr[i][1]);
        }
        // 按照开始端点（右端点）从小到大排序
        Arrays.sort(lines, Comparator.comparingInt((Line l) -> l.start));
        // 优先级队列作为堆，默认就是小根堆
        PriorityQueue<Integer> heap = new PriorityQueue<>();

        int max = 0;
        for (int i = 0; i < lines.length; i++) {
            // 弹出比自己左端点小的
            while (!heap.isEmpty() && heap.peek() <= lines[i].start) {
                heap.poll();
            }
            // 插入自己右端点
            heap.add(lines[i].end);
            // 堆size就是以当前左端点为开始的重合区域包含线段的条数
            max = Math.max(max, heap.size());
        }
        return max;
    }

    public static class Line {
        public int start;
        public int end;

        public Line(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }
}
