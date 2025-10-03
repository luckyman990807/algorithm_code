package dynamic_programing;

/**
 * 最长回文子串
 * https://leetcode.cn/problems/longest-palindromic-subsequence/description/
 * 给定一个字符串，求其中最长回文子串的长度
 *
 * 思路一：样本对应模型
 * 一个字符串的最长回文子串 = 一个字符串和它倒序的最长公共子串
 * 因为str1的开头去匹配str2的开头，就等同于str1的开头去匹配str1的结尾
 *
 *
 * 思路二：范围尝试模型+优化
 * 尝试方法process(str,l,r)表示字符串str从l到r的子串，其中最长回文子串长度是多少
 *
 * 可能性1:最长回文子串以l开头以r结尾。
 * 如果str[l]=str[r]，证明这这种可能性成立，递归2+process(str,l+1,r-1)
 *
 * 可能性2:最长回文子串以l开头，不以r结尾
 * 递归process(str,l,r-1)
 *
 * 可能性3:最长回文子串不以l开头，以r结尾
 * 递归process(str,l+1,r)
 *
 * 可能性4:最长回文子串不以l开头，不以r结尾
 * 递归process(str,l+1,r-1)
 *
 * 返回4种可能性中的最大值。
 *
 * 优化点：剔除重复依赖的位置
 * 比如dp[l][r]同时依赖左，下，左下，那么它可以只依赖左，下。也就是把可能性4剔除，但是注意可能性1不能剔除，它不是完全依赖左下，它是左下+2，可能比左和下都大，要保留。
 * 为什么可以把左下剔除掉？
 * 因为左下在上一步求左那个位置的时候已经被左依赖了（作为左的下），同时也被下那个位置依赖了（作为下的左），因此求当前位置时不需要再次依赖。
 * 近一步解释：左那个位置是依赖左下的基础上得到的最大值，也就是左一定不小于左下；同理下也一定不小于左下，因此只需要从左和下里选最大值即可，不需要再考虑左下。
 *
 *
 * 思路三：范围尝试模型，以最长公共子序列的思路组织可能性，最终的可能性和优化后的思路二是一样的
 * 可能性1：以l开头以r结尾；
 * 可能性2:不以l开头；
 * 可能性3:不以r结尾。
 */
public class Code007LongestPalindromicSubsequence {
    /**
     * 思路一解法：转换成求一个字符串和它的倒序的最长公共子序列长度，样本对应模型，动态规划（严格位置依赖）
     * @param s
     * @return
     */
    public static int dpAsLCS(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str1 = s.toCharArray();
        char[] str2 = new char[str1.length];
        for (int i = 0; i < str1.length; i++) {
            str2[i] = str1[str1.length - 1 - i];
        }

        // 初始状态
        int[][] dp = new int[str1.length][str2.length];
        dp[0][0] = str1[0] == str2[0] ? 1 : 0;
        for (int j = 1; j < str2.length; j++) {
            dp[0][j] = str1[0] == str2[j] ? 1 : dp[0][j - 1];
        }
        for (int i = 1; i < str1.length; i++) {
            dp[i][0] = str1[i] == str2[0] ? 1 : dp[i - 1][0];
        }

        // 状态转移
        for (int i = 1; i < str1.length; i++) {
            for (int j = 1; j < str2.length; j++) {
                int p1 = str1[i] == str2[j] ? 1 + dp[i - 1][j - 1] : 0;
                int p2 = dp[i - 1][j];
                int p3 = dp[i][j - 1];
                dp[i][j] = Math.max(p1, Math.max(p2, p3));
            }
        }

        return dp[str1.length - 1][str2.length - 1];
    }

    /**
     * 思路二解法第一阶段：暴力递归
     */
    public static int force(String s) {
        if (s == null || s.isEmpty()) {
            return 1;
        }
        return process(s.toCharArray(), 0, s.length() - 1);
    }

    public static int process(char[] str, int l, int r) {
        // 回文长度最小是1，一个字符即可回文
        if (l == r) {
            return 1;
        }
        // 如果str[l] == str[r]相等，那么回文子序列至少有它们2个字符了，剩余部分继续递归（如果l+1和r-1后会交错，那么就不能递归了）。如果不相等，那么这个可能性就不成立，返回最小回文长度1；
        int p1 = str[l] == str[r] ? 2 + (r - l >= 2 ? process(str, l + 1, r - 1) : 0) : 1;
        int p2 = process(str, l + 1, r);
        int p3 = process(str, l, r - 1);
        return Math.max(p1, Math.max(p2, p3));
    }

    /**
     * 思路二解法第一阶段，另一种尝试策略：把判断从可能性展开挪到边界条件
     * @param str
     * @param l
     * @param r
     * @return
     */
    public static int process1(char[] str, int l, int r) {
        if (l == r) {
            return 1;
        }
        if (l == r - 1) {
            return str[l] == str[r] ? 2 : 1;
        }

        int p1 = str[l] == str[r] ? 2 + process(str, l + 1, r - 1) : 1;
        int p2 = process(str, l + 1, r);
        int p3 = process(str, l, r - 1);
        return Math.max(p1, Math.max(p2, p3));
    }


    /**
     * 思路二解法二阶段：动态规划（严格位置依赖），范围尝试模型
     * @param s
     * @return
     */
    public static int dp(String s) {
        if (s == null || s.isEmpty()) {
            return 1;
        }
        char[] str = s.toCharArray();

        // 初始状态
        int[][] dp = new int[str.length][str.length];
        for (int i = 0; i < str.length; i++) {
            dp[i][i] = 1;
        }

        // 状态转移
        for (int l = str.length - 2; l >= 0; l--) {
            for (int r = l + 1; r < str.length; r++) {
                int p1 = str[l] == str[r] ? 2 + (r - l >= 2 ? dp[l + 1][r - 1] : 0) : 1;
                int p2 = dp[l + 1][r];
                int p3 = dp[l][r - 1];
                dp[l][r] = Math.max(p1, Math.max(p2, p3));
            }
        }
        return dp[0][str.length - 1];
    }



    public static void main(String[] args) {
        String s = "qawebrtycvbbna";
        System.out.println(force(s));
        System.out.println(dp(s));
    }
}
