package tixiban.class18manacher;

/**
 * 给定一个字符串,只能从后面追加,求至少追加几个字符能使整体变成回文
 * <p>
 * 思路:
 * 求包含最后一个字符的最长回文子串,前面剩下的逆序追加到后面就是回文.前面剩下的长度就是最小追加的长度
 */
public class Code02AddMinToBePalindrome {
    public static int getMinAddLength(String string) {
        char[] str = getManacherStr(string);
        int[] radius = new int[str.length];
        int R = 0;
        int C = 0;

        for (int i = 0; i < str.length; i++) {
            radius[i] = R > i ? Math.min(radius[2 * C - i], R - i) : 1;

            while (i + radius[i] < str.length && i - radius[i] >= 0) {
                if (str[i + radius[i]] == str[i - radius[i]]) {
                    radius[i]++;
                } else {
                    break;
                }
            }

            // 如果当前i的右边界扩到了比R更远的位置,那么更新R=那个右边界,C=当前i
            if (i + radius[i] > R) {
                R = i + radius[i];
                C = i;

                // 只要第一次有人把R扩到字符串末尾,这个就是包含最后一个字符的最大回文子串
                if (R == str.length) {
                    // R-1位置关于i位置的对称点,除以2就是原始串的位置,也就是除去最大回文子串剩下的部分的长度
                    return (2 * i - (R - 1)) / 2;
                }
            }
        }
        return Integer.MAX_VALUE;
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
        String str = "ab123";
        System.out.println(getMinAddLength(str));
    }
}
