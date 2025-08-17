package tixiban.class1_13.greedy;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 一段长为x的金条切割一次会变成两段，不管切割成的长度分别是多少，都需要消耗x元
 * 输入一个列表表示一段金子要最终切割成的每一段的长度，输出最少花费多少元
 */
public class LessMoneySplitGold {
    /**
     * 贪心算法：哈夫曼编码
     * 把每段长度压入小根堆中，每次弹出两个，组成新数，把新数重新压入堆中。每次组成的新数就是每次切割花费的金额，累加每次的新数就是最小花费
     */
    public static int lessMoneyGreedy(int[] lengthList) {
        // new一个小根堆，把数组压入堆中
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        for (int length : lengthList) {
            heap.add(length);
        }
        // 总共需要花费的钱
        int result = 0;
        while (heap.size() >= 2) {
            // 只要堆的大小>=2，就弹出2个元素，组成新数
            int cur = heap.poll() + heap.poll();
            // 新数累计到结果中
            result += cur;
            // 新数压入堆
            heap.add(cur);
        }
        return result;
    }


    /**
     * 暴力算法：每种顺序都试一下
     * 切割的问题换个角度思考：可以理解成把一段一段的金条合成大金条，长x的和长y的合成长x+y的需要花费x+y元
     */
    public static int lessMoneyViolent(int[] lengthList) {
        if (lengthList == null || lengthList.length == 0) {
            return 0;
        }
        return process(lengthList, 0);
    }

    public static int process(int[] lengthList, int spent) {
        if (lengthList.length == 1) {
            return spent;
        }
        int spend = Integer.MAX_VALUE;
        for (int i = 0; i < lengthList.length; i++) {
            for (int j = i + 1; j < lengthList.length; j++) {
                spend = Math.min(spend, process(copyAndMerge(lengthList, i, j), spent + lengthList[i] + lengthList[j]));
            }
        }
        return spend;
    }


    public static int[] copyAndMerge(int[] arr, int i, int j) {
        int[] result = new int[arr.length - 1];
        int resultIndex = 0;
        for (int n = 0; n < arr.length; n++) {
            if (n != i && n != j) {
                result[resultIndex++] = arr[n];
            }
        }
        result[resultIndex] = arr[i] + arr[j];
        return result;
    }


    public static void main(String[] args) {
        for (int n = 0; n < 100000; n++) {
            int[] arr = generateArr(5, 20);
            System.out.println("原数组：" + Arrays.toString(arr));

            int[] arrGreedy = new int[arr.length];
            System.arraycopy(arr, 0, arrGreedy, 0, arr.length);
            int resultGreedy = lessMoneyGreedy(arrGreedy);
            System.out.println("贪心结果：" + resultGreedy);

            int[] arrViolent = new int[arr.length];
            System.arraycopy(arr, 0, arrViolent, 0, arr.length);
            int resultViolent = lessMoneyViolent(arrViolent);
            System.out.println("暴力结果：" + resultViolent);

            if (resultGreedy != resultViolent) {
                System.out.println("failed!");
                break;
            }
        }
        System.out.println("success!");
    }

    public static int[] generateArr(int maxLength, int maxValue) {
        int[] result = new int[(int) (Math.random() * maxLength) + 1];
        for (int i = 0; i < result.length; i++) {
            result[i] = (int) (Math.random() * maxValue) + 1;
        }
        return result;
    }
}
