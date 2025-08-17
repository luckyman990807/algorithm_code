package shuatiban;

/**
 * 给定两个字符串S和T，返回S的所有子序列中有多少个子序列的字面值等于T
 *
 * 思路：样本对应模型动态规划。样本对应模型的可能性通常按照结尾位置划分，所以：
 * dp表dp[i][j]=k表示字符串S从0到i前缀中，有k个子序列的字面值等于字符串T从0到j前缀。那么dp[S.length-1][T.length-1]就是答案。
 * 分析可能性：
 * 可能性1:前提条件是S[i]==T[j]，那么S[i]就用来匹配T[j]，进而dp[i][j]=dp[i-1][j-1]
 * 可能性2:S[i]直接舍弃不用，那么就要用S的0到i-1来匹配T的0到j，进而dp[i][j]=dp[i-1][j]
 */
public class Code088字符串S有多少个子序列的字面值等于字符串T {
    public static int dp(String s, String t) {
        char[] str = s.toCharArray();
        char[] tmp = t.toCharArray();

        int[][] dp = new int[str.length][tmp.length];
        dp[0][0] = str[0] == tmp[0] ? 1 : 0;
        for (int i = 1; i < str.length; i++) {
            dp[i][0] = dp[i - 1][0] + (str[i] == tmp[0] ? 1 : 0);
        }
        for (int i = 1; i < str.length; i++) {
            for (int j = 1; j < tmp.length && j <= i; j++) {
                dp[i][j] = dp[i - 1][j];
                if (str[i] == tmp[j]) {
                    dp[i][j] += dp[i - 1][j - 1];
                }
            }
        }
        return dp[str.length - 1][tmp.length - 1];
    }

    /**
     * dp[i][j]=k的含义是，字符串s取前i个字符，有k个子序列的字面值等于字符串t取前j个字符
     * 且认为空字符串也是子序列
     */
    public static int dp2(String s, String t) {
        char[] str = s.toCharArray();
        char[] tmp = t.toCharArray();

        int[][] dp = new int[str.length + 1][tmp.length + 1];
        // 初始值：s取前0个（空串），除了能拼成t取前0个（空串）之外，拼不成t取前任意个；s取前任意个，都能拼成t取前0个（空串）
        for (int i = 0; i <= str.length; i++) {
            dp[i][0] = 1;
        }
        for (int i = 1; i <= str.length; i++) {
            // 字符串s取前i个字符，有几个子序列的字面值等于字符串t取前j个字符，所以j既<=t.length又<=i
            // 举个例子，假如s长度100万，t长度只有5，那么j<=t.length即可；假如i现在到5，t长度100万，那么j<=i即可
            for (int j = 1; j <= Math.min(tmp.length, i); j++) {
                dp[i][j] = dp[i - 1][j];
                if (str[i - 1] == tmp[j - 1]) {
                    dp[i][j] += dp[i - 1][j - 1];
                }
            }
        }
        return dp[str.length][tmp.length];
    }

    public static void main(String[] args) {
        String s = "ajusdfsfatsd";
        String t = "asd";
        System.out.println(dp(s, t));
        System.out.println(dp2(s, t));
    }

}
