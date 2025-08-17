package tixiban.class33动态规划猜法外部信息简化;

/**
 * 有台奇怪的打印机有以下两个特殊要求：
 * 打印机每次只能打印由 同一个字符 组成的序列。
 * 每次可以在任意连续位置打印新字符，并且会覆盖掉原来已有的字符。
 * 给你一个字符串s，你的任务是计算这个打印机打印它需要的最少打印次数。
 *
 * 例如:
 * 输入：s = "aaabbb"
 * 输出：2
 * 解释：首先打印 "aaa" 然后打印 "bbb"
 * 输入：s = "aba"
 * 输出：2
 * 解释：首先打印 "aaa" 然后在第二个位置打印 "b" 覆盖掉原来的字符 'a'
 *
 * 思路:把s分成两部分递归,返回打印左部分的最少打印次数+右部分的最少打印次数.要注意的是如果左部分第一个和右部分第一个相等,那么加完次数-1,因为两部分的第一次可以由一次打印完成
 * 例如:
 * abbacc假设分成abb和acc,那么abb的打印次数为2(a, abb),acc的打印次数为2(a, acc),加起来次数为4,但是可以3次就完成(aaaa, abba,abbacc)
 */
public class Code05奇怪打印机 {
    /**
     * 暴力递归
     * 字符串str从l到r打印出来需要最少几次打印
     */
    public static int process(char[] str, int l, int r) {
        // 递归出口,如果只剩一个字符,只需要一次打印
        if (l == r) {
            return 1;
        }
        // 返回结果最少打印次数.最多也就每个字符打一次
        int min = r - l + 1;
        // m表示中间的分界线,l到m-1是左部分,m到r是右部分
        for (int m = l + 1; m <= r; m++) {
            // 左部分打印次数+右部分打印次数, 如果左右第一个字符相等就减1否则不减
            min = Math.min(min, process(str, l, m - 1) + process(str, m, r) - (str[l] == str[m] ? 1 : 0));
        }
        return min;
    }

    public static int violent(String str) {
        return process(str.toCharArray(), 0, str.length() - 1);
    }

    public static void main(String[] args) {
        String str = "aba";
        System.out.println(violent(str));
    }

    /**
     * 改傻缓存、动态规划:略
     */

    /**
     * 优化常数项方式:合并相同字符
     * 例如s = aaaabccb, 就不用先分成a和aaabccb、在分成aa和aabccb、..., 直接分成aaaa和bccb即可,也就是分界线m直接移到第一个不想等的地方
     */
}
