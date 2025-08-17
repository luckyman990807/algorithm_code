package shuatiban;

/**
 * 给定一个有序数组arr，代表坐落在X轴上的点，给定一个正数K，代表绳子的长度，返回绳子最多压中几个点？
 * 即使绳子边缘处盖住点也算盖住
 *
 * 思路：
 * 1.最low的就是两重for循环,O(N^2)
 * 2.稍微优一点的是遍历+二分.遍历以i位置为结尾,二分法找出大于arr[i]-K的有多少个.O(N*logN)
 * 3.最优解是滑动窗口.最初左右边界在同一位置,窗口大小没超过绳子长度就让右边界右移,如果超过了就计算个数,然后左边界右移.O(N)
 */
public class Code001绳子最多压住几个点 {
    /**
     * 滑动窗口
     * @param arr 有序数组,表示数轴上的数
     * @param line 绳子长度
     * @return 最多能盖住几个数
     */
    public static int slideWindow(int[] arr, int line) {
        int max = 0;

        int left = 0;
        int right = 0;

        while (left <= right) {
            // 只要窗口大小<=绳子长度,右边界就++
            while (right < arr.length && arr[right] - arr[left] <= line) {
                right++;
            }
            // 计算个数
            max = Math.max(max, right - left++);
        }
        return max;
    }

}
