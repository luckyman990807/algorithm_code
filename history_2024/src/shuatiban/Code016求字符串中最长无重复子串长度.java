package shuatiban;

/**
 * https://leetcode.com/problems/longest-substring-without-repeating-characters/
 * 求一个字符串中，最长无重复字符子串长度
 *
 * 思路:动态规划
 * 一看到字串、子数组,就要想:必须以0位置结尾的满足XX条件的子串、必须以1位置结尾满足XX条件的子串...
 */
public class Code016求字符串中最长无重复子串长度 {
    /**
     * 暴力递归
     * 试法:返回str字符串必须以index结尾的最大无重复字串长度.
     * map中记录了index前面各个字母最后出现的位置.
     */
    public static int process(char[] str, int index, int[] map) {
        // 递归出口,以一个小于0的位置为结尾,没有长度,返回0
        if (index < 0) {
            return 0;
        }

        // 可能性1:以index结尾的无重复字串=以index-1结尾的无重复字串+index位置.
        // 如果index-1位置最远往左推到j,那么index最远也就推到j了,因为之所以index-1只能推到j,肯定是j-1在index-1到j之间出现了.
        int p1 = process(str, index - 1, map) + 1;
        // 可能性2:当前字符在左边最后一次出现的位置的下个位置,到当前位置,就是可能的最大无重复字串
        int p2 = index - map[str[index]];
        // 两种可能性取最小值
        int result = Math.min(p1, p2);

        // 返回之前更新当前字符最后一次出现的位置
        map[str[index]] = index;
        return result;
    }

    public static int violent(String s) {
        int[] map = new int[256];
        for (int i = 0; i < map.length; i++) {
            map[i] = -1;
        }

        char[] str = s.toCharArray();
        int max = Integer.MIN_VALUE;
        // 求必须以0位置结尾的最大无重复子串长度、必须以1位置结尾的最大无重复子串长度...所有结果取最大值
        for (int i = 0; i < str.length; i++) {
            // 求一次i位置结尾的答案,map就会被覆盖,求下一个位置之前要复原,否则影响计算结果
            for (int j = 0; j < map.length; j++) {
                map[j] = -1;
            }
            max = Math.max(max, process(str, i, map));
        }

        return max;
    }

    public static void main(String[] args) {
        String s = "abcbada";
        System.out.println(violent(s));
        System.out.println(dp(s));
    }

    /**
     * 改动态规划
     * 递归试法里只有一个可变参数,所以是一维动态规划.
     * 但是因为只依赖左边位置,不依赖其他位置,所以dp表不需要完整记录所有位置,只需要用一个变量记录左边位置即可.
     */
    public static int dp(String s) {
        char[] str = s.toCharArray();

        int[] map = new int[256];
        for (int i = 0; i < map.length; i++) {
            map[i] = -1;
        }

        int max = Integer.MIN_VALUE;

        int dp = 0;
        for (int i = 0; i < str.length; i++) {
            int p1 = dp + 1;
            int p2 = i - map[str[i]];
            dp = Math.min(p1, p2);
            map[str[i]] = i;
            max = Math.max(max, dp);
        }
        return max;
    }
}
