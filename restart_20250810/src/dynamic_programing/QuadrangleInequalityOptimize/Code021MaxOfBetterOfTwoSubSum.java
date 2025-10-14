package dynamic_programing.QuadrangleInequalityOptimize;


import java.util.Arrays;

/**
 * 给定一个长度为n的数组，为每个从0开始的子数组都找到一个最佳划分点（什么是最佳划分点：把数组分成左右两部分，使其中累加和较小的部分尽量大，也就是让两部分的累加和尽量接近）。
 * 返回一个长度为n的结果数组，记录每个子数组最佳划分后的较小部分的累加和
 */
public class Code021MaxOfBetterOfTwoSubSum {
    public static int[] force(int[] arr) {
        int[] preSum = new int[arr.length + 1];
        preSum[0] = arr[0];
        for (int i = 1; i < arr.length; i++) {
            preSum[i] = preSum[i - 1] + arr[i];
        }

        int[] result = new int[arr.length];
        int split = 0;
        for (int r = 1; r < arr.length; r++) {
            while (split + 1 < arr.length) {
                // 左半部分：0到split，右半部分：split+1到r
                int beforeSmaller = Math.min(preSum[r] - preSum[split], preSum[split]);
                // 计算假装划分点右移一格到split+1后，累加和较小的那部分的累加和
                int afterSmaller = Math.min(preSum[r] - preSum[split + 1], preSum[split + 1]);
                // 如果划分点右移后，较小部分的累加和真的变大了，就真的把划分点右移，并且继续往右试探。否则就停止试探，这个划分点就是0到r上的最优划分点
                if (afterSmaller >= beforeSmaller) {
                    split++;
                } else {
                    break;
                }
            }
            result[r] = Math.min(preSum[r] - preSum[split], preSum[split]);
        }

        return result;
    }

    public static void main(String[] args) {
        int[] arr = new int[]{0, 1, 2, 3, 4, 5};
        System.out.println(Arrays.toString(force(arr)));
    }
}
