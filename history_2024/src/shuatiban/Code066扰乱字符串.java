package shuatiban;

/**
 * https://leetcode.com/problems/scramble-string/
 * 给定两个长度相等的字符串，判断二者是否互为扰乱字符串。
 * 扰乱字符串的定义是把一个字符串任意切成两段，然后选择是否两段交换位置，生成的两段又分别可以任意切成两段然后选择是否交换位置，最终的到的结果就是扰乱字符串。
 * 例如code和edco互为扰乱字符串，code切成co/de且交换位置的到deco，de切成d/e选择交换位置，co选择不交换，的到edco
 *
 * 思路：切成两段后的子串也要切成两段交换，这种行为显然是递归
 * 递归试法：
 * 例如str1=abcdefg，str2=hijklmn，str1枚举所有可能的切分位置例如ab/cdefg，
 * 那么str2可以选择hi/jklmn切分，然后递归f(ab,hi)&&f(cdefg,jklmn)，
 * str2也可以选择hijkl/mn切分，然后递归f(ab,mn)&&f(cdefg,hijkl)，
 * 只要有一种情况的到true，就可以直接返回
 */
public class Code066扰乱字符串 {

    /**
     * 递归试法，判断str1从left1开始长度为length、str2从left2开始长度为length，这两个子串是否互为扰乱字符串
     */
    public static boolean processViolent(char[] str1, int left1, char[] str2, int left2, int length) {
        // 如果对比的双方都只有一个字符，那么二者相等即为扰乱字符串，不等则不是
        if (length == 1) {
            return str1[left1] == str2[left2];
        }
        // firstLength是str1选择切分的第一段的长度，对应str2可以在同样的位置切分，也可以在对称的位置切分
        for (int firstLength = 1; firstLength < length; firstLength++) {
            if (
                    // str1和str2的切分位置相同，第一段的长度为firstLength，第二段的长度为length-firstLength
                    // 也就是str1=ab/cdefg，str2=hi/jklmn，然后判断ab和hi、cdefg和jklmn都互为扰乱字符串
                    processViolent(str1, left1, str2, left2, firstLength) && processViolent(str1, left1 + firstLength, str2, left2 + firstLength, length - firstLength)
                            // str1和str2的切分位置对称，也就是str1=ab/cdefg，str2=hijkl/mn，然后判断ab和mn、cdefg和hijkl都互为扰乱字符串
                            || processViolent(str1, left1, str2, left2 + length - firstLength, firstLength) && processViolent(str1, left1 + firstLength, str2, left2, length - firstLength)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isScrambleViolent(String s1, String s2) {
        return processViolent(s1.toCharArray(), 0, s2.toCharArray(), 0, s1.length());
    }

    /**
     * 缓存法
     * 改严格位置依赖的动态规划比较麻烦的话，直接缓存法即可
     */
    public static boolean process(char[] str1, int left1, char[] str2, int left2, int length, int[][][] dp) {
        if (dp[left1][left2][length] != 0) {
            return dp[left1][left2][length] == 1;
        }
        boolean ans;
        if (length == 1) {
            ans = str1[left1] == str2[left2];
        } else {
            for (int firstLength = 1; firstLength < length; firstLength++) {
                if (
                        process(str1, left1, str2, left2, firstLength, dp)
                                && process(str1, left1 + firstLength, str2, left2 + firstLength, length - firstLength, dp)
                                ||
                                process(str1, left1, str2, left2 + length - firstLength, firstLength, dp)
                                        && process(str1, left1 + firstLength, str2, left2, length - firstLength, dp)) {
                    dp[left1][left2][length] = 1;
                    return true;
                }
            }
            ans = false;
        }
        dp[left1][left2][length] = ans ? 1 : -1;
        return ans;
    }

    public static boolean isScramble(String s1, String s2) {
        return process(s1.toCharArray(), 0, s2.toCharArray(), 0, s1.length(), new int[s1.length()][s2.length()][s1.length() + 1]);
    }
}
