package shuatiban;

/**
 * 给定一个字符串str，当然可以生成很多子序列，返回有多少个子序列是回文子序列，空序列不算回文
 * 比如，str = “aba”，回文子序列：{a}、{a}、 {a,a}、 {b}、{a,b,a}，返回5
 *
 *
 * 思路：看到子序列，就想到从左到右or范围上的尝试，看到回文子序列，就想到范围上的尝试，因为回文涉及到左右两端是否相等，所以是范围上的尝试。
 *
 * dp[l][r]表示下标从l到r范围上，有多少个子序列是回文子序列，那么dp[0][n-1]就是所求。
 * 那么dp[l][r]怎么算呢？根据范围上尝试，也就是分析左右两端分别要不要，得到4种可能性：
 * 可能性a：l要，r弃
 * 可能性b：l弃，r要
 * 可能性c：l弃，r弃
 * 可能性d：l要，r要
 * 而dp[l][r] = 可能性a+b+c+d。
 *
 * 然后分析dp[l][r]的3个相邻位置 下dp[l+1][r],左dp[l][r-1],左下dp[l+1][r-1]跟4种可能性的关系，进而推出状态转移方程（这里如果分析不出来3个位置和4种可能性的关系，可以举一个简单的具体例子具体分析，例如"abab"）
 * dp[l+1][r]表示从l+1到r范围上有几个回文子序列，也就是l一定不要，r可能要可能不要，那不就是可能性b+c
 * dp[l][r-1]表示从l到r-1范围上有几个回文子序列，也就是l可能要可能不要，r一定不要，那不就是可能性a+c
 * dp[l+1][r-1]表示从l+1到r-1范围上有几个回文子序列，也就是l和r一定都不要，那不就是可能性c
 * 而可能性d，如果str[l]!=str[r]，那么可能性d=0；如果str[l]==str[r]，那么lr之间有多少回文子序列，外面套一层str[l]，str[r]还是回文子序列，另外如果lr之间如果为空，单纯str[l]，str[r]也是一个回文子序列，即：
 * 可能性d = str[l]!=str[r] ? 0 : dp[l+1][r-1] + 1
 *
 * 最后的到结论，dp[l][r] = dp[l+1][r] + dp[l][r-1] - dp[l+1][r-1] + (str[l]!=str[r] ? 0 : dp[l+1][r-1] + 1)
 */
public class Code103求回文子序列的个数 {
    public static int dp(String s) {
        if (s == null || s.length() == 0) return 0;
        char[] str = s.toCharArray();

        // dp[l][r]表示下标从l到r范围上，有多少个子序列是回文子序列
        int[][] dp = new int[s.length()][s.length()];

        // l==r时，dp[l][r]=1，因为只有一个字符的时候，只有一个回文子序列，就是这个字符本身
        for (int i = 0; i < s.length(); i++) {
            dp[i][i] = 1;
        }

        // 因为依赖左、下、左下位置，所以从下往上，从左往右填充dp数组
        for (int l = str.length - 2; l >= 0; l--) {
            for (int r = l + 1; r < s.length(); r++) {
                dp[l][r] = dp[l + 1][r] + dp[l][r - 1] - dp[l + 1][r - 1] + (str[l] != str[r] ? 0 : dp[l + 1][r - 1] + 1);
            }
        }

        return dp[0][str.length - 1];
    }

    public static void main(String[] args) {
        String s = "acca";
        System.out.println(dp(s));
    }
}
