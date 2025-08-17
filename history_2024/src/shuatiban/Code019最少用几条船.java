package shuatiban;

import java.util.Arrays;

/**
 * 给定一个正数数组arr，代表若干人的体重，再给定一个正数limit，表示所有船共同拥有的载重量，每艘船最多坐两人，且不能超过载重
 * 想让所有的人同时过河，并且用最好的分配方法让船尽量少，返回最少的船数
 *
 * 思路:
 * 贪心,如果有2个轻的和2个重的,最省的方案是尽量让重的和轻的两两配对,一共2条船;而不是2个轻的配对,2个重的各自一条船,一共3条船.
 * 并且配对时尽量让轻的中的重的和重的中的轻的配对,这样最能保证最重的也能配对
 */
public class Code019最少用几条船 {
    public static int minBoats(int[] arr, int limit) {
        Arrays.sort(arr);
        // 如果最重的人超过船的载重量,那么永远无法让所有人过河
        if (arr[arr.length - 1] > limit) {
            return -1;
        }

        // 把体重数组分为两部分:小于等于limit/2,和大于limit/2. 找到小于等于区的右边界lessRight
        int lessRight = -1;
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] <= limit / 2) {
                lessRight = i;
                break;
            }
        }
        // 如果小于等于区的右边界是-1,也就是没有小于等于区,所有人的体重都大于limit/2,那么谁都没法两两配对坐船,只能一人一条船
        if (lessRight == -1) {
            return arr.length;
        }

        // 左指针,遍历小于等于区
        int left = lessRight;
        // 右指针,遍历大于区
        int right = lessRight + 1;

        // 思路:用左边尝试为每个右边的配对,或者用右边尝试为左边的配对,配对成功的两两一条船,左边没配对的两两一条船,右边没配对成功的一人一条船
        // 这里用左边尝试为每个右边的配对.
        // 记录左边没有配对的个数
        int leftNoUsed = 0;
        while (left >= 0) {
            // 计算当前的左能为多少个右配对,然后往左顺延相同的数量进行配对(如果当前的左能配对,再往左更小更能配对了)
            int paired = 0;
            while (right < arr.length && arr[right] + arr[left] <= limit) {
                right++;
                // 记录配对的对数
                paired++;
            }

            if (paired == 0) {
                // 如果当前的左不能为当前的右配对,左指针左移,记录左边没配对的个数
                left--;
                leftNoUsed++;
            } else {
                // 如果有配对的,左指针左移到下一个没配对的地方继续遍历
                left -= paired;
            }
        }
        // 左边配对的个数
        int leftPaired = lessRight + 1 - leftNoUsed;
        // 右边没有配对的个数
        int rightNoUsed = arr.length - leftNoUsed - leftPaired * 2;

        // 最少船数 = 配对的对数 + 右边没有配对的个数 + 左边没有配对的个数/2向上取整
        return leftPaired + rightNoUsed + (leftNoUsed + 1) / 2;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 3, 4, 5, 5, 6, 6, 7, 8};
        int limit = 10;
        System.out.println(minBoats(arr, limit));
    }
}
