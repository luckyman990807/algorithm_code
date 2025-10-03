package dynamic_programing;

/**
 * 规定1和A对应，2和B对应……，26和Z对应。那么一个数字字符串就可以转换成字母字符串，例如111可以转换为AAA、AK或KA。
 * 给定一个数组字符串，问有多少种转换结果
 */
public class Code004ParseNumToLetter {
    public static int force(String str) {
        return process(str.toCharArray(), 0);
    }

    public static int process(char[] str, int index) {
        // 越界了，走完了整个str，说明找到了一种有效的转换策略，返回方法数1。如果是无效的，那么中途就断了，根本到不了越界
        if (index == str.length) {
            return 1;
        }
        // 如果当前数字是0，那么这条策略到这就断了，走不下去了。因为0没有对应的字母，0和下一位组合也没有对应的字母
        if (str[index] == '0') {
            return 0;
        }

        // 当前数字只要不是0，那么至少有一种方案：当前数字单转成字母，然后继续往下走，看能不能走到底
        int ways = process(str, index + 1);
        // 如果和下一位的组合<=26，还可以有第二种方案：当前数字和下一位合并转成字母，然后继续往下走，看能不能走到底
        if (index + 1 < str.length && (str[index] - '0') * 10 + str[index + 1] - '0' <= 26) {
            ways += process(str, index + 2);
        }
        return ways;
    }

    public static int dp(String str) {
        char[] charArray = str.toCharArray();
        int[] dp = new int[str.length() + 1];
        dp[str.length()] = 1;
        for (int i = str.length() - 1; i >= 0; i--) {
            if (charArray[i] == '0') {
                dp[i] = 0;
                continue;
            }
            int ways = dp[i + 1];
            if (i + 1 < str.length() && (charArray[i] - '0') * 10 + charArray[i + 1] - '0' <= 26) {
                ways += dp[i + 2];
            }
            dp[i] = ways;
        }
        return dp[0];
    }

    public static void main(String[] args) {
        String str = "11178548391028712589";
        System.out.println(force(str));
        System.out.println(dp(str));
    }
}
