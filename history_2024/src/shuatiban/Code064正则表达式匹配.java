package shuatiban;

/**
 * https://leetcode.com/problems/regular-expression-matching/
 * 给你一个字符串 s 和一个字符规律 p，请你来实现一个支持 '.' 和 '*' 的正则表达式匹配。
 * '.' 匹配任意单个字符
 * '*' 匹配零个或多个前面的那一个元素
 * 所谓匹配，是要涵盖 整个 字符串 s的，而不是部分字符串。
 */
public class Code064正则表达式匹配 {
    /**
     * 方法三、也是最优解，记忆化搜索+斜率优化
     */
    public static boolean isMatch(String s, String p) {
        return process(p.toCharArray(), 0, s.toCharArray(), 0, new int[p.length() + 1][s.length() + 1]);
    }
    public static boolean process(char[] exp, int ei, char[] str, int si, int[][] dp) {
        if (dp[ei][si] != 0) {
            return dp[ei][si] == 1;
        }
        boolean ans;
        if (ei == exp.length) {
            ans = si == str.length;
        } else if (ei + 1 == exp.length || exp[ei + 1] != '*') {
            ans = si < str.length && (str[si] == exp[ei] || exp[ei] == '.') && process(exp, ei + 1, str, si + 1, dp);
        } else if (si == str.length || !(exp[ei] == str[si] || exp[ei] == '.')) {
            ans = process(exp, ei + 2, str, si, dp);
        } else {
            // 走到这个分支，说明ei+1是*，并且ei位置和si位置匹配，于是要计算ei和*能跟si及其后面多少个位置匹配
            // 看见枚举行为就想能不能优化成有限几个位置依赖
            // 举个例子，当前递归的是f(10,5)走到这个分支，exp从10开始是a*,str从5开始是aaaab，那么f(10,5)=f(12,5)||f(12,6)||...||f(12,9)，只要有一个true就可以返回了
            // 而f(10,6)对应exp从10开始是a*,str从6开始是aaab也肯定走到这个分支，f(10,6)=f(12,6)||f(12,7)||...||f(12,9)，只要有一个true就可以返回
            // 由这两个式子可知f(10,5)=f(12,5)||f(10,6)
            // 这就把枚举行为优化成仅仅两个位置的依赖
            ans = process(exp, ei + 2, str, si, dp) || process(exp, ei, str, si + 1, dp);
        }
        dp[ei][si] = ans ? 1 : -1;
        return ans;
    }

    /**
     * 方法二、记忆化搜索，dp[i][j]=0表示没有缓存过，=1表示答案为true，=-1表示答案为false
     */
    public static boolean isMatchCache(String s, String p) {
        return processCache(p.toCharArray(), 0, s.toCharArray(), 0, new int[p.length() + 1][s.length() + 1]);
    }
    public static boolean processCache(char[] exp, int ei, char[] str, int si, int[][] dp) {
        if (dp[ei][si] != 0) {
            return dp[ei][si] == 1;
        }
        boolean ans;
        if (ei == exp.length) {
            ans = si == str.length;
        } else if (ei + 1 == exp.length || exp[ei + 1] != '*') {
            ans = si < str.length && (str[si] == exp[ei] || exp[ei] == '.') && processCache(exp, ei + 1, str, si + 1, dp);
        } else if (si == str.length || !(exp[ei] == str[si] || exp[ei] == '.')) {
            ans = processCache(exp, ei + 2, str, si, dp);
        } else {
            while (si < str.length && (str[si] == exp[ei] || exp[ei] == '.')) {
                if (processCache(exp, ei + 2, str, si, dp)) {
                    dp[ei][si] = 1;
                    return true;
                }
                si++;
            }
            ans = processCache(exp, ei, str, si, dp);
        }
        dp[ei][si] = ans ? 1 : -1;
        return ans;
    }

    /**
     * 方法一，纯递归
     */

    /**
     * 纯递归
     */
    public static boolean isMatchViolent(String s, String p) {
        return processViolent(p.toCharArray(), 0, s.toCharArray(), 0);
    }

    /**
     * 递归尝试，用表达式exp从ei开始到最后，能否拼出字符串str从si开始到最后
     */
    public static boolean processViolent(char[] exp, int ei, char[] str, int si) {
        // 递归出口，如果表达式到头了，那么字符串也必须到头，否则不可能拼出来
        // 注意，如果字符串到头了，那么表达式不需要到头，因为有可能是*可以消掉
        if (ei == exp.length) {
            return si == str.length;
        }

        // 可能性1、ei下一个字符不是*（包括ei没有下一个了，以及ei有下一个但是不是*）
        if (ei + 1 == exp.length || exp[ei + 1] != '*') {
            // 没有*来消除，那么表达式ei位置和字符串si位置必须匹配（匹配包括si没到头，并且ei位置等于si位置或者ei是.），并且后续字符串也必须匹配
            return si < str.length && (exp[ei] == str[si] || exp[ei] == '.') && processViolent(exp, ei + 1, str, si + 1);
        }
        // 可能性2、ei下一个字符是*，但ei和si不匹配（不匹配包括si到头了，或者ei位置不等于si位置也不等于.）
        if (si == str.length || !(exp[ei] == str[si] || exp[ei] == '.')) {
            // 只能靠*消除ei位置，表达式跳到ei+2位置继续匹配
            return processViolent(exp, ei + 2, str, si);
        }
        // 可能性3、ei下一个字符是*，并且ei和si匹配，那么就要考虑*用来拼几个
        // 例如字符串从si开始是aaaab...，表达式从ei开始是a*...，那么a*可以用来拼0个a，ei+2继续和si匹配，也可以a*用来拼1个a，ei+2继续和si+1匹配...
        while (si < str.length && (str[si] == exp[ei] || exp[ei] == '.')) {
            if (processViolent(exp, ei + 2, str, si++)) {
                return true;
            }
        }
        // while遍历的是*用来拼0个、1个、2个、3个a，遍历完si指向b，单独算剩下拼4个a的情况
        return processViolent(exp, ei + 2, str, si);
    }
}
