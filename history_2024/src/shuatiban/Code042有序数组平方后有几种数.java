package shuatiban;

/**
 * 1、给定一个有序数组arr，其中值可能为正、负、0。返回arr中每个数都平方之后,有多少种不同的数字？
 * 例如[-3,-2,-2,-1,0,2,3],平方之后有4种数字:0,1,2,3
 *
 * 思路:
 * 首先,平方后有几种数相当于取绝对值后有几种数
 * 方法1、遍历数组,平方后加入哈希表,返回哈希表大小.时间复杂度O(N),额外空间复杂度O(N).
 * 方法2、左右指针,左边绝对值大就左边滑,右边绝对值大就右边滑,两边绝对值一样大就同时滑.每一轮滑动不是只滑一个数,如果有连续相等的数要一次滑完.
 * 每滑一轮,计数+1.
 * 时间复杂度O(N),额外空间复杂度O(1).
 * 这里用方法2
 *
 *
 * 2、给定一个数组arr，先递减然后递增，返回arr中有多少个不同的数字？
 * 一样的思路,第1道题取绝对值后不就是先递减然后递增么.
 */
public class Code042有序数组平方后有几种数 {
    public static int howManyKindsAfterSquare(int[] arr) {
        // 左指针
        int left = 0;
        // 右指针
        int right = arr.length - 1;
        // 答案
        int count = 0;

        while (left <= right) {
            int leftAbs = Math.abs(arr[left]);
            int rightAbs = Math.abs(arr[right]);
            if (leftAbs > rightAbs) {
                // 左指针绝对值大,左指针滑,有相等的就一直滑
                while (left < arr.length && Math.abs(arr[left]) == leftAbs) {
                    left++;
                }
            } else if (leftAbs < rightAbs) {
                // 右指针绝对值大,右指针滑,有相等的就一直滑
                while (right >= 0 && Math.abs(arr[right]) == rightAbs) {
                    right--;
                }
            } else {
                // 左右指针绝对值一样大,都滑,有相等的就一直滑
                while (left < arr.length && Math.abs(arr[left]) == leftAbs) {
                    left++;
                }
                while (right >= 0 && Math.abs(arr[right]) == rightAbs) {
                    right--;
                }
            }
            // 每滑一轮,计数+1
            count++;
        }
        return count;
    }

    public static void main(String[] args) {
    }
}
