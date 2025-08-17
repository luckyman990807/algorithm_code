package shuatiban;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

/**
 * 给定两个有序数组arr1和arr2，再给定一个整数k，返回来自arr1和arr2的两个数相加和最大的前k个，两个数必须分别来自两个数组，按照降序输出
 * 时间复杂度为O(klogk)
 * 输入描述：
 * 第一行两个整数N, K分别表示数组arr1、arr2的大小，以及需要询问的数
 * 接下来一行N个整数，表示arr1内的元素
 * 再接下来一行N个整数，表示arr2内的元素
 * 输出描述：
 * 输出K个整数表示答案
 * 牛客网题目：https://www.nowcoder.com/practice/7201cacf73e7495aa5f88b223bbbf6d1
 *
 * 思路：大根堆
 * 两个数组的所有组合有M*N种，可以把两个数组的组合想像成一个矩阵，那么最大的一定是最右下角（arr1[M-1]和arr2[n-1]相加），第2大和第3大要么是它的上边位置，要么是他的左边位置，两个组合都压入大根堆
 */
public class Code094两个有序数组所有组合的topK {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int k = in.nextInt();
        int[] arr1 = new int[n];
        int[] arr2 = new int[n];
        for (int i = 0; i < n; i++) {
            arr1[i] = in.nextInt();
        }
        for (int i = 0; i < n; i++) {
            arr2[i] = in.nextInt();
        }
        int[] result = topK(arr1, arr2, k);
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i] + " ");
        }
        in.close();
    }

    public static class Node {
        public int index1;
        public int index2;
        public int sum;

        public Node(int index1, int index2, int sum) {
            this.index1 = index1;
            this.index2 = index2;
            this.sum = sum;
        }
    }

    public static int[] topK(int[] arr1, int[] arr2, int k) {
        // 两数组的长度
        int n = arr1.length;
        // 返回结果
        int[] result = new int[k];
        int resultIndex = 0;
        // 大根堆，初始状态把右下角位置压入
        PriorityQueue<Node> maxHeap = new PriorityQueue<>((o1, o2) -> o2.sum - o1.sum);
        maxHeap.add(new Node(n - 1, n - 1, arr1[n - 1] + arr2[n - 1]));
        // set记录已经压过堆的位置，避免重复压入重复输出（某个位置的上边和另一位置的左边可能是同一个位置）。初始状态把右下角位置压入
        Set<Long> set = new HashSet<>();
        set.add((long) (n - 1) * (long) (n) + (long) (n - 1));

        while (resultIndex < k) {
            // 计算结果
            Node node = maxHeap.poll();
            result[resultIndex++] = node.sum;
            // 接下来两个top分别出在左边(index1,index2 - 1)和下边(index1 - 1,index2)
            int index1 = node.index1;
            int index2 = node.index2 - 1;
            if (index1 >= 0 && index2 >= 0 && !set.contains((long) index1 * (long) n + (long) index2)) {
                // 如果左边没越界且没入过堆，就入堆
                maxHeap.add(new Node(index1, index2, arr1[index1] + arr2[index2]));
                // 记录入过堆
                set.add((long) index1 * (long) n + (long) index2);
            }
            index1 = node.index1 - 1;
            index2 = node.index2;
            if (index1 >= 0 && index2 >= 0 && !set.contains((long) index1 * (long) n + (long) index2)) {
                // 如果右边边没越界且没入过堆，就入堆
                maxHeap.add(new Node(index1, index2, arr1[index1] + arr2[index2]));
                // 记录入过堆
                set.add((long) index1 * (long) n + (long) index2);
            }
        }
        return result;
    }

}
