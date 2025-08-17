package tixiban.class17kmp;

/**
 * 给定两个字符串,判断是否互为旋转字符串
 * 什么是旋转字符串:把str1的开头子串挪到结尾,能得到str2,就说str1和str2互为旋转字符串.
 * 例如abcde和cdeab互为旋转字符串
 * <p>
 * 思路:
 * 把str1自身拼接到自身的结尾,得到str1+str1,如果str2是str1+str1的子串,那么str1和str2互为旋转字符串
 * 为什么?
 * 因为str1+str1已经把str1的所有旋转字符串都囊括在内了
 */
public class Code02IfRotateString {

    public static boolean isRotate(String str1, String str2) {
        return startIndex(str1 + str1, str2) != -1;
    }

    /**
     * kmp算法
     *
     * @param string
     * @param subString
     * @return
     */
    public static int startIndex(String string, String subString) {
        if (string == null || subString == null || subString.length() == 0 || string.length() < subString.length()) {
            return -1;
        }

        char[] str = string.toCharArray();
        char[] subStr = subString.toCharArray();

        int cur = 0;
        int subCur = 0;

        int[] next = getNextArray(subStr);

        while (cur < str.length && subCur < subStr.length) {
            if (str[cur] == subStr[subCur]) {
                cur++;
                subCur++;
            } else if (subCur == 0) {
                cur++;
            } else {
                subCur = next[subCur];
            }
        }
        return subCur == subStr.length ? cur - subCur : -1;
    }


    public static int[] getNextArray(char[] str) {
        int[] next = new int[str.length];
        next[0] = -1;
        next[1] = 0;

        int i = 2;
        int cur = 0;
        while (i < next.length) {
            if (str[i - 1] == str[cur]) {
                next[i++] = ++cur;
            } else if (cur > 0) {
                cur = next[cur];
            } else {
                next[i++] = 0;
            }
        }
        return next;
    }

    public static void main(String[] args) {
        String str1 = "asdf";
        String str2 = "asdf";
        System.out.println(isRotate(str1, str2));
    }

}
