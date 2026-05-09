package dynamic_programing.SimplifyOutDependency;

/**
 * https://leetcode.cn/problems/remove-boxes/
 * 移除盒子
 * 题意简单概括：给定一个正数数组arr，连续相同的数可以消掉，得分=消掉个数的平方。求消掉所有数的最大得分
 * 
 */
public class Class031RemoveBoxes {
    public static int force(int[] boxes) {
        return process(boxes, 0, boxes.length - 1, 0);
    }

    private static int process(int[] arr, int l, int r, int pre) {
        if (l == r) {
            return (pre + 1) * (pre + 1);
        }
        if (l > r) {
            return 0;
        }

        // 可能性展开：前面pre和谁放在一起消掉？
        int max = Integer.MIN_VALUE;
        // 第一种可能性：前面pre个和l直接消掉
        max = Math.max(max, (pre + 1) * (pre + 1) + process(arr, l + 1, r, 0));
        // 其他可能性：前面pre和l先不消掉，合在一起，和后面的消掉
        for (int i = l + 1; i <= r; i++) {
            if (arr[i] != arr[l]) {
                continue;
            }
            // 第i种可能性：前面pre和l作为前缀，去消i到r范围的数
            max = Math.max(max, process(arr, l + 1, i - 1, 0) + process(arr, i, r, pre + 1));
        }

        return max;
    }

}
