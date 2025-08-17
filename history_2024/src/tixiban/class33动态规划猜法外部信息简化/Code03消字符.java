package tixiban.class33动态规划猜法外部信息简化;

// 如果一个字符相邻的位置没有相同字符，那么这个位置的字符出现不能被消掉
// 比如:"ab"，其中a和b都不能被消掉
// 如果一个字符相邻的位置有相同字符，就可以一起消掉
// 比如:"abbbc"，中间一串的b是可以被消掉的，消除之后剩下"ac"
// 某些字符如果消掉了，剩下的字符认为重新靠在一起
// 给定一个字符串，你可以决定每一步消除的顺序，目标是请尽可能多的消掉字符，返回最少的剩余字符数量
// 比如："aacca", 如果先消掉最左侧的"aa"，那么将剩下"cca"，然后把"cc"消掉，剩下的"a"将无法再消除，返回1
// 但是如果先消掉中间的"cc"，那么将剩下"aaa"，最后都消掉就一个字符也不剩了，返回0，这才是最优解。

// 跟上一道题很像,把前面有几个跟l相等的(int),换成前面有没有跟l相等的(bool)
public class Code03消字符 {
    /**
     * 暴力递归
     * str字符串从l到r尽可能地消,返回最少剩余字符数量.
     * hasPre表示str的前面一个字符是否等于str[l]
     */
    public static int process(char[] str, int l, int r, boolean hasPre) {
        if (l > r) {
            return 0;
        }
        // 递归出口,如果当前过程处理的只有一个字符,那么前面有相同的就能消掉,没有相同的就只能留着
        if (l == r) {
            return hasPre ? 0 : 1;
        }
        int min = Integer.MAX_VALUE;
        // l后面第一个不等于l的位置
        int firstNotEqualL = l;
        // 等于l的前缀长度,算上l和l前一个.所以最小为1,表示只有l自己
        int allPreEqualL = hasPre ? 1 : 0;
        while (firstNotEqualL <= r && str[firstNotEqualL] == str[l]) {
            firstNotEqualL++;
            allPreEqualL++;
        }
        // 第一种可能性:str由l开头的相等前缀就是要单独消
        min = Math.min(min, (allPreEqualL == 1 ? 1 : 0) + process(str, firstNotEqualL, r, false));
        // 其他可能性:由l开头的相等前缀要跟后面的等于l的一块消,那么前提是二者中间的能完全消掉剩余0字符
        // nextL表示继由l开头的相等前缀之后,后面的第一个和l相等的位置
        for (int nextL = firstNotEqualL; nextL <= r; nextL++) {
            if (str[nextL] == str[l] && str[nextL - 1] != str[l]) {
                // 如果二者中间能完全消掉,他俩才能一起消
                if (process(str, firstNotEqualL, nextL - 1, false) == 0) {
                    min = Math.min(min, process(str, nextL + 1, r, true));
                }
            }
        }
        return min;
    }

    public static int violent(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        return process(str.toCharArray(), 0, str.length() - 1, false);
    }

    public static void main(String[] args) {
        String str = "baaccabb";
        System.out.println(violent(str));
    }
}
