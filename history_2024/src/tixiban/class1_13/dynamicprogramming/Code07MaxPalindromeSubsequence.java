package tixiban.class1_13.dynamicprogramming;

/**
 * 给定一个字符串，返回最大回文子序列长度
 * 例如，给定"1ft23f432jio1"，返回7（最大回文子序列是1234321或123f321）
 */
public class Code07MaxPalindromeSubsequence {
    /**
     * 方法一：最大公共子序列法
     * 针对两个样本
     */

    /**
     * str的最大回文子序列 = str和str的逆序字符串的最大公共子序列
     *
     * @param str
     * @return
     */
    public static int byMaxPublicSubsequence(String str) {
        StringBuilder convert = new StringBuilder();
        char[] chars = str.toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            convert.append(chars[i]);
        }
        return Code06MaxPublicSubsequence.maxPublicSubsequence(str, convert.toString());
    }

    /**
     * 方法二：针对一个样本的范围，暴力递归
     */
    public static int processViolent(char[] str, int left, int right) {
        // base case 1、范围只剩一个字符，那么回文子序列长度为1
        if (left == right) {
            return 1;
        }
        // base case 2、范围剩下两个字符，如果相等，回文长度=2，否则=1（单个字符作为回文子序列）
        if (left == right - 1) {
            return str[left] == str[right] ? 2 : 1;
        }

        // 可能性1、要求的回文子序列既不以l开头，又不以r结尾
        int p1 = processViolent(str, left + 1, right - 1);
        // 可能性2、可能以l开头，但不以r结尾
        int p2 = processViolent(str, left, right - 1);
        // 可能性3、不以l开头，但可能以r结尾
        int p3 = processViolent(str, left + 1, right);
        // 可能性4、既以l开头，又以r结尾，那么l和r就占了回文子序列的两位
        int p4 = str[left] == str[right] ? 2 + processViolent(str, left + 1, right - 1) : 0;

        // 返回4种可能性的最大值
        return Math.max(Math.max(p1, p2), Math.max(p3, p4));
    }

    public static int maxPalindromeSubsequenceViolent(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        return processViolent(str.toCharArray(), 0, str.length() - 1);
    }

    /**
     * 方法三：针对一个样本的范围，动态规划表
     */
    public static int maxPalindromeSubsequence(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        char[] chars = str.toCharArray();
        int N = str.length();

        // 可变参数left、right，变化范围都是0~N-1
        int[][] dpMap = new int[N][N];

        for (int i = 0; i < N - 1; i++) {
            // base case 1，对角线=1
            dpMap[i][i] = 1;
            // base case 2，对角线的上一条对角线
            dpMap[i][i + 1] = chars[i] == chars[i + 1] ? 2 : 1;
        }
        // 上面for循环要算base case 2，必须要有i+1，所以i必须<N-1，N-1单独赋值
        dpMap[N - 1][N - 1] = 1;

        // 因为上半三角，N-1行和N-2行都已经赋值了，从N-3开始
        for (int left = N - 3; left >= 0; left--) {
            // 对角线位置是列号=行号，往右一个位置是列号=行号+1已经在base case 2算过了，这里从列号=行号+2开始算
            // 按照这个顺序，能保证每次遍历到的位置，它所依赖的位置都已经算过了
            for (int right = left + 2; right < N; right++) {
                // 当前位置依赖左下、左、下三个位置
                int p1 = dpMap[left + 1][right - 1];
                int p2 = dpMap[left][right - 1];
                int p3 = dpMap[left + 1][right];
                int p4 = chars[left] == chars[right] ? 2 + dpMap[left + 1][right - 1] : 0;
                dpMap[left][right] = Math.max(Math.max(p1, p2), Math.max(p3, p4));
            }
        }
        return dpMap[0][N - 1];
    }

    /**
     * 方法四：针对一个样本的范围，基于动态规划表做优化，不过这个题优化只能减少常数时间
     */
    public static int maxPalindromeSubsequenceSuper(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        char[] chars = str.toCharArray();
        int N = str.length();

        int[][] dpMap = new int[N][N];

        for (int i = 0; i < N - 1; i++) {
            dpMap[i][i] = 1;
            dpMap[i][i + 1] = chars[i] == chars[i + 1] ? 2 : 1;
        }
        dpMap[N - 1][N - 1] = 1;

        for (int left = N - 3; left >= 0; left--) {
            for (int right = left + 2; right < N; right++) {
                // 优化只在依赖位置这里
                // 一个位置依赖左下、左、下三个位置，且最后取Max，那么当前位置一定不比三者任何一个小
                // 由于当前位置的左，依赖当前位置的左下（当前位置的左的下），所以当前位置的左一定不比当前位置的左下小，也就是当前位置的左下在Max()中没啥用处，可以不求
                dpMap[left][right] = Math.max(dpMap[left][right - 1], dpMap[left + 1][right]);
                if (chars[left] == chars[right]) {
                    dpMap[left][right] = Math.max(dpMap[left][right], 2 + dpMap[left + 1][right - 1]);
                }
            }
        }
        return dpMap[0][N - 1];
    }

    /**
     * 主函数执行
     *
     * @param args
     */
    public static void main(String[] args) {
        String str = "1ft23f432jio1";
        System.out.println(byMaxPublicSubsequence(str));
        System.out.println(maxPalindromeSubsequenceViolent(str));
        System.out.println(maxPalindromeSubsequence(str));
        System.out.println(maxPalindromeSubsequenceSuper(str));
    }
}
