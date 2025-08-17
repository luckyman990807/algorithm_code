package shuatiban;

/**
 * 这道题属于面试中比较少见的一种动态规划「数位dp」
 *
 * 给定一个正数N，比如N = 13，在纸上把所有数都列出来如下：
 * 1 2 3 4 5 6 7 8 9 10 11 12 13
 * 可以数出1这个字符出现了6次，给定一个正数N，如果把1~N都列出来，返回1这个字符出现的多少次
 *
 * 思路：这么拆分：从1到abc中出现了几个1 = 从1到bc中出现了几个1 + 从bc+1到abc出现了几个1
 */
public class Code097返回1字符出现的个数 {
    /**
     * 1~n上的所有数字，一共出现了几个1
     *
     * @param abc
     * @return
     */
    public static int count1(int abc) {
        if (abc < 1) {
            return 0;
        }
        // 数字n的位数，比如199，位数是3
        int length = getLength(abc);
        if (length == 1) {
            // 如果只有1位，那1到某个个位数肯定只会出现一个1
            return 1;
        }
        // 计算从bc+1到abc一共出现几个1

        // 发现好几个地方都要用到10的length-1次方，因此把它暂存出来
        int temp = (int) Math.pow(10, length - 1);
        // 从bc+1到abc中，最高位是1的数有几个：
        // 如果abc的最高位是1，那么除去最高位剩下的数值+1，也就是ab+1就是答案。例如n=123，那么从1到123，最高位是1的数的个数=23+1=24个（100到123）
        // 如果abc的最高位不是1，那么答案是10的length-1次方。例如n=345，那么从1到345，最高位是1的数的个数=100（从100到199都覆盖了）
        int highestBitCount1 = abc / temp == 1 ? abc % temp + 1 : temp;
        // 从bc+1到abc，个位是1的有几个+十位是1的有几个+……+第二高位是1的有几个
        // 假设abc=4567，从568到4567，个位是1的有几个？这时候个位确定了，十位和百位（除去最高位和当前为1的位）从0到9随便填，最高位从1到4随便填
        // 也就是 abc的最高位数值 * 10的length-2次方 * 有几位数需要填
        int lowBitsCount1 = abc / temp * (length - 1) * (int) Math.pow(10, length - 2);

        // 最终答案=从bc+1到abc出现几个1+从1到bc出现几个1，后者是递归
        return highestBitCount1 + lowBitsCount1 + count1(abc % temp);
    }

    public static int getLength(int n) {
        int length = 0;
        while (n > 0) {
            n /= 10;
            length++;
        }
        return length;
    }

    public static int violent(int n) {
        int count = 0;
        for (int i = 1; i <= n; i++) {
            int num = i;
            while (num > 0) {
                if (num % 10 == 1) {
                    count++;
                }
                num /= 10;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        int n = 2245;
        // 12345678910111213141516171819202122
        System.out.println(count1(n));
        System.out.println(violent(n));
    }

}
