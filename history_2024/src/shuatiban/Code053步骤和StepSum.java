package shuatiban;

/**
 * 步骤和的定义：比如680，680 + 68 + 6 = 754，680的步骤和叫754。
 * 给定一个正数num，判断它是不是某个数的步骤和
 *
 * 思路：单调性、二分法
 *
 * 容易得到两个结论：
 * 1、步骤和是单调的，也就是如果x的步骤和是a，y的步骤和是b，且x>y，则a>b（可以简单推理一下，也可以不推理直接for循环打表检验）
 * 2、如果一个数的步骤和等于x，那么这个数一定在1到x之间（x的步骤和肯定大于x，1的4e步骤和肯定小于x，又因为步骤和是单调的，所以一定在1到x之间）
 * 基于这两个结论，可以用二分法求解：给定num，在1到num上二分，找步骤和=num的那个数，如果找到了就返回true，最终没找到就返回false
 */
public class Code053步骤和StepSum {

    public static int stepNum(int num) {
        int left = 1;
        int right = num;
        // 在1到num范围上用二分法查找步骤和=num的数
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            int stepNum = getStepNum(mid);
            if (stepNum > num) {
                right = mid - 1;
            } else if (stepNum < num) {
                left = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    /**
     * 求一个数的步骤和
     * @param num
     * @return
     */
    public static int getStepNum(int num) {
        int sum = 0;
        // 每次除以10，累加到结果上
        while (num > 0) {
            sum += num;
            num /= 10;
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(stepNum(754));
    }

}
