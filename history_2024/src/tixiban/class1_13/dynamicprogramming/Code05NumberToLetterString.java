package tixiban.class1_13.dynamicprogramming;

/**
 * 给定一串数字，规定1可转成a，26可转成z，问一共有多少种转法
 * 例如给定111，返回3（aaa，ak，ka）
 */
public class Code05NumberToLetterString {
    /**
     * 暴力递归法
     */

    /**
     * index前面的东西不用管了，从index开始转，一共有多少种转法
     *
     * @param str   数字字符串
     * @param index 当前遍历到哪一个字符
     * @return
     */
    public static int processViolent(char[] str, int index) {
        // 如果已经走到越界了，那么恭喜你找到了1种方法
        if (index == str.length) {
            return 1;
        }
        // 如果当前要面对的数字是0，连最小的a都转不了，那么从index开始直接没法转，返回0
        if (str[index] == '0') {
            return 0;
        }
        // 只要不是0，那么总能把当前字符单个转成字母，index来到下个字符继续执行
        int ways = processViolent(str, index + 1);
        // 如果还有下一个字符，并且当前字符和下一个字符拼起来<=26，在z的范围内，就能两个组团转，index来到下下个字符继续执行
        if (index + 1 < str.length && (str[index] - '0') * 10 + str[index + 1] - '0' <= 26) {
            ways += processViolent(str, index + 2);
        }
        return ways;
    }

    public static int getConvertWays(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        return processViolent(str.toCharArray(), 0);
    }

    /**
     * 动态规划法
     */

    /**
     * 递归函数只有一个可变参数index，变化范围是0~N，因此建一个N+1大小的动态规划表，根据递归尝试策略填充动态规划表
     */
    public static int convert(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        char[] chars = str.toCharArray();
        // 动态规划表
        int[] map = new int[chars.length + 1];
        // 递归策略1分支
        map[chars.length] = 1;
        // 递归策略2分支
        for (int index = str.length() - 1; index >= 0; index--) {
            if (chars[index] != '0') {
                int ways = map[index + 1];
                if (index + 1 < str.length() && (chars[index] - '0') * 10 + chars[index + 1] - '0' <= 26) {
                    ways += map[index + 2];
                }
                map[index] = ways;
            }
        }

        return map[0];
    }

    public static void main(String[] args) {

        String str = "111";
        System.out.println(getConvertWays(str));
        System.out.println(convert(str));
    }
}
