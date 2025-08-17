package tixiban.class18manacher;

/**
 * 给定一个字符串,返回最大回文子串(要子串不要长度)
 * <p>
 * 思路:
 * max更新时除了记录最大半径,还要记录对应的回文起点,以便最后提取出最大回文子串
 *
 * 补充:
 * manacher字符串中的位置i,对应到原始串是(i-1)/2,猜+验证得到的
 */
public class Code03GetMaxPalindrome {
    public static String getMaxPalindromeLength(String string) {
        char[] str = getManacherStr(string);
        int[] radius = new int[str.length];
        int R = 0;
        int C = 0;
        int maxRadius = 0;
        int maxStart = 0;

        for (int i = 0; i < str.length; i++) {
            radius[i] = R > i ? Math.min(radius[2 * C - i], R - i) : 1;

            while (i + radius[i] < str.length && i - radius[i] >= 0) {
                if (str[i + radius[i]] == str[i - radius[i]]) {
                    radius[i]++;
                } else {
                    break;
                }
            }

            if (i + radius[i] > R) {
                R = i + radius[i];
                C = i;
            }

            // 更新最大半径和最大半径对应的回文起点
            if (radius[i] > maxRadius) {
                maxRadius = radius[i];
                maxStart = i - radius[i] + 1;
            }
        }

        // 根据最大半径和起点,提取出最大回文子串
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= (maxRadius - 1) * 2; i++) {
            if (str[maxStart + i] != '#') {
                sb.append(str[maxStart + i]);
            }
        }
        return sb.toString();
    }

    // abcba->#a#b#c#b#a#
    public static char[] getManacherStr(String string) {
        char[] str = string.toCharArray();
        char[] manacherStr = new char[string.length() * 2 + 1];
        manacherStr[0] = '#';
        int index = 1;
        for (int i = 0; i < str.length; i++) {
            manacherStr[index++] = str[i];
            manacherStr[index++] = '#';
        }
        return manacherStr;
    }

    public static void main(String[] args) {
        String str = "abc12321c";
        System.out.println(getMaxPalindromeLength(str));
    }
}
