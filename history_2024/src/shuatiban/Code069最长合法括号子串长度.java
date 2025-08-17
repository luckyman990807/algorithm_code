package shuatiban;

/**
 * 给定一个只由左括号和右括号的字符串，返回最长的合法括号子串的长度。
 * 合法指的是，子串中任意一个左括号，都有与之匹配的右括号，任意一个右括号都有与之匹配的左括号。
 *
 * 思路：
 * 看到子串就要想：以xx结尾的子串最多能往左延伸多长，最后求所有位置的最大值。
 */
public class Code069最长合法括号子串长度 {
    public static int validSubStr(String s) {
        int max = 0;
        char[] str = s.toCharArray();

        int[] dp = new int[str.length];
        // 一个括号必然不是合法子串
        dp[0] = 0;
        for (int i = 1; i < dp.length; i++) {
            // 如果i位置是左括号，那么以i结尾的子串都不合法
            if (str[i] == '(') {
                dp[i] = 0;
                continue;
            }
            // 当前位置是），应当跨过前一个位置结尾的合法子串去更前面找（，也就是pre位置
            int pre = i - 1 - dp[i - 1];
            // 如果pre位置越界了，或者是），那么i位置就没有匹配的（，答案为0。
            // 问：就算pre是）难道pre更前面没有与i位置配对的（吗？答：没有，因为如果有的话，首先会跟pre配对，然后以串联的形式算进i-1位置结尾的子串中。
            if (pre < 0 || str[pre] == ')') {
                dp[i] = 0;
                continue;
            }
            // 走到这里说明pre位置能跟i位置配对，那么以i结尾的合法子串最少是i-1位置的合法子串+i和pre一对
            dp[i] = dp[i - 1] + 2;
            // 如果可能的话，把pre-1位置结尾的合法子串以串联的形式合并到i位置结尾的子串上
            if (pre - 1 >= 0) {
                dp[i] += dp[pre - 1];
            }
            max = Math.max(max, dp[i]);
        }
        return max;
    }

    public static void main(String[] args) {
        String s = "(())((()())";
        System.out.println(validSubStr(s));
    }
}
