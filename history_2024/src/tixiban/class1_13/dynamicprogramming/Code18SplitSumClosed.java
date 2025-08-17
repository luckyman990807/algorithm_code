package tixiban.class1_13.dynamicprogramming;

/**
 * 给定一个整数数组，把数组中的所有数分成两个集合，让两个集合的累加和尽量接近。返回较小的累加和。
 */
public class Code18SplitSumClosed {
    /**
     * 暴力递归法
     * index之前的数不管，从index开始，凑成<=restSum且最接近restSum的累加和，返回这个累加和
     * @param arr 数组
     * @param index 当前遍历到的数的下标
     * @param restSum 剩余累加和目标
     * @return
     */
    public static int process(int[] arr, int index, int restSum) {
        // 如果没数可取了，就没有累加和了，返回0
        if (index == arr.length) {
            return 0;
        }
        // 可能性1、不要当前数，求后面数<=restSum且最接近restSum的累加和
        int p1 = process(arr, index + 1, restSum);
        // 可能性2、要当前数，求当前数 + 后面数<=restSum - arr[index]且最接近restSum - arr[index]的累加和
        int p2 = 0;
        if (arr[index] <= restSum) {
            p2 = arr[index] + process(arr, index + 1, restSum - arr[index]);
        }
        // 较大的那个才是最接近restSum的累加和
        return Math.max(p1, p2);
    }

    /**
     * 让分成两个集合，但是不需要去考虑两个集合，只需要求出一个集合即可，让这个集合的累加和去趋近整体累加和的一半
     * @param arr
     * @return
     */
    public static int splitViolent(int[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        // 从0开始遍历，趋近的累加和是整体累加和的一半
        return process(arr, 0, sum / 2);
    }

    public static int splitDp(int[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }

        int[][] dp = new int[arr.length + 1][sum / 2 + 1];

        for (int index = arr.length - 1; index >= 0; index--) {
            for (int restSum = 0; restSum <= sum / 2; restSum++) {
                int p1 = dp[index + 1][restSum];

                int p2 = 0;
                if (arr[index] <= restSum) {
                    p2 = arr[index] + dp[index + 1][restSum - arr[index]];
                }
                dp[index][restSum] = Math.max(p1, p2);
            }
        }

        return dp[0][sum / 2];
    }

    public static void main(String[] args) {
        int[] arr = {2, 4, 1, 6};
        System.out.println(splitViolent(arr));
        System.out.println(splitDp(arr));
    }
}
