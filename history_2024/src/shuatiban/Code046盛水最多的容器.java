package shuatiban;

/**
 * 给定n个非负整数a1，a2，...an，每个数代表坐标中的一个点 (i, ai)。在坐标内画n条垂直线
 * 垂直线i的两个端点分别为(i, ai)和(i, 0)，找出其中的两条线，使得它们与x轴共同构成的容器可以容纳最多的水
 * Leetcode题目：https://leetcode.com/problems/container-with-most-water/
 *
 * 思路:左右指针,小的那边计算结果并向对方移动,记录所有结果的最大值.
 * 例如[6,3,8,4],一开始左指针在6,右指针在4,
 * 右指针小,计算结果4*3=12,右指针左移到8,
 * 此时左指针小,计算结果6*2=12,左指针右移到3,
 * 此时左指针小,计算结果3*1=3,左指针右移到8,两指针相遇,结束.
 *
 * 问:左右指针为什么不需要回退?左指针指在3的时候,明明和最右边的4结合的结果最大,但实际是跟8结合的,这样不会错过最优答案吗?
 * 答:不会,因为3和4结合的结果完全包含在上一步4和6结合的结果里了,就没必要再具体算一遍,我们只关注有没有更优的结果产生,不关心不优的结果算得对不对.
 * 3如果比4小,那么3的结果就会被包含在4的结果内,3如果比4大,那么3就不会跟4结合了,只能跟比自己大的结合.所以不后悔错过最优答案.
 */
public class Code046盛水最多的容器 {
    public static int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int max = Integer.MIN_VALUE;
        while (left < right) {
            if (height[left] < height[right]) {
                max = Math.max(max, height[left] * (right - left));
                left++;
            } else {
                max = Math.max(max, height[right] * (right - left));
                right--;
            }
        }
        return max;
    }
}
