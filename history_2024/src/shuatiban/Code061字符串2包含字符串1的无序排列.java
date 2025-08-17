package shuatiban;

/**
 * https://leetcode.com/problems/permutation-in-string/
 * 给你两个字符串 s1 和 s2 ，写一个函数来判断 s2 是否包含 s1 的排列。如果是，返回 true ；否则，返回 false 。
 * 换句话说，s1 的排列之一是 s2 的 子串 。
 *
 * 也有面试题是这种说法：
 * 给定长度为m的字符串aim，以及一个长度为n的字符串str，问能否在str中找到一个长度为m的连续子串，
 * 使得这个子串刚好由aim的m个字符组成，顺序无所谓，返回任意满足条件的一个子串的起始位置，未找到返回-1
 *
 * 思路：滑动窗口+hash表（简单的映射可以用数组）
 * 一个不关注顺序的字符排列，就等同于一张表：a：几个，b：几个，c：几个...，相当于一个欠账表，
 * 如果滑动窗口内的字符符合这张表，也就相当于把欠账都抵消掉了，就找到了答案
 */
public class Code061字符串2包含字符串1的无序排列 {
    public boolean checkInclusion(String s1, String s2) {
        // 如果s2都没s1长，那肯定不包含s1的排列
        if(s2.length() < s1.length()){
            return false;
        }

        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();

        // 记录欠账表
        int[] map = new int[256];
        for (char c : str1) {
            map[c]++;
        }
        // 欠账表总和
        int all = str1.length;

        // 窗口右边界
        int right = 0;
        // 窗口宽度
        int width = str1.length;

        // 初步形成窗口
        for (; right < width; right++) {
            // 把右边界位置的字符从欠账表上减1，然后右边界右扩。
            // 如果减之前当前字符的欠账大于0，说明是有效还账，总欠账数也跟着减1，否则当前字符的欠账减到负数就是无效的还账，总数不减
            if (map[str2[right]]-- > 0) {
                all--;
            }
        }
        // 初步形成窗口后判断当前窗口是不是就是答案
        if (all == 0) {
            return true;
        }

        // 窗口右滑阶段
        for (; right < s2.length(); right++) {
            // 右边界位置是本次滑进来的字符，还账，如果还之前的欠账数大于0说明是有效还账，总数也减
            if (map[str2[right]]-- > 0) {
                all--;
            }
            // 做边界位置是本次要滑出去的字符，继续欠账，如果欠之前的欠账数小于0说明是还前面的无效账，总数不变；否则是有效欠账，欠账总数加1
            if (map[str2[right - width]]++ >= 0) {
                all++;
            }
            // 每滑动一次就检查当前窗口是不是找到了答案
            if (all == 0) {
                return true;
            }
        }
        return false;
    }
}
