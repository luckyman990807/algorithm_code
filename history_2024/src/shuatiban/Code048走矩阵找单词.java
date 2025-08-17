package shuatiban;

/**
 * 给定一个char[][] matrix，也就是char类型的二维数组，再给定一个字符串word，
 * 可以从任何一个某个位置出发，可以走上、下、左、右，能不能找到word？
 *  char[][] m = {  { 'a', 'b', 'z' },
 *                  { 'c', 'd', 'o' },
 *                  { 'f', 'e', 'o' } }
 * 设定1：可以走重复路的情况下，返回能不能找到
 * 比如，word = "zoooz"，是可以找到的，z -> o -> o -> o -> z，因为允许走一条路径中已经走过的字符
 * 设定2：不可以走重复路的情况下，返回能不能找到
 * 比如，word = "zoooz"，是不可以找到的，因为允许走一条路径中已经走过的字符不能重复走
 *
 *
 * 思路:
 * 1、可以走重复路:动态规划,样本对应模型.
 * 递归试法是:f(i,j,k)等于从矩阵i,j位置开始走,能否找到单词从k开始的后缀串?
 * 对矩阵中每个位置都求一次f(i,j,0),只要有一个返回true,答案就是true
 * 2、不可以走重复路:只能递归深度优先遍历,无法改动态规划
 * 递归试法:还是从矩阵i,j出发,能否找到单词从k开始的后缀,但是每经过一个位置要把char值改为0,表示已经走过了,然后再递归尝试下个位置,尝试完再改回原来的值(深度优先遍历恢复现场)
 */
public class Code048走矩阵找单词 {

    /**
     * 可以走重复路,递归尝试,matrix字母矩阵从(row,col)位置出发,能否找到word字符串从start开始的后缀串?返回true or false
     */
    public static boolean canRepeatProcess(char[][] matrix, int row, int col, char[] word, int start) {
        // 递归出口,有两种理解:
        // 1、如果已经找完整个word字符串,说明发现了一种答案,返回true
        // 2、word从word.length开始的后缀串是空,空字符串不用走也能找到,返回true
        if (start == word.length) {
            return true;
        }
        // 递归出口,如果连当前位置都不匹配,更谈不上匹配后缀串了
        if (matrix[row][col] != word[start]) {
            return false;
        }

        // 上下左右4个方向分别递归,只要有一个是true,就返回true
        if (row > 0 && canRepeatProcess(matrix, row - 1, col, word, start + 1)) {
            return true;
        } else if (row < matrix.length - 1 && canRepeatProcess(matrix, row + 1, col, word, start + 1)) {
            return true;
        } else if (col > 0 && canRepeatProcess(matrix, row, col - 1, word, start + 1)) {
            return true;
        } else if (col < matrix[row].length - 1 && canRepeatProcess(matrix, row, col + 1, word, start + 1)) {
            return true;
        }

        return false;
    }

    /**
     * 矩阵每个位置都走一遍递归,只要有一个位置是true,就返回true
     */
    public static boolean canRepeat(char[][] matrix, String word) {
        char[] str = word.toCharArray();
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                if (canRepeatProcess(matrix, row, col, str, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 可以走重复路,改动态规划
     */
    public static boolean canRepeatDp(char[][] matrix, String str) {
        char[] word = str.toCharArray();
        boolean[][][] dp = new boolean[matrix.length][matrix[0].length][word.length];
        for (int r = 0; r < matrix.length; r++) {
            for (int c = 0; c < matrix[r].length; c++) {
                dp[r][c][word.length - 1] = matrix[r][c] == word[word.length - 1];
            }
        }
        // 三维动态规划表,最后一层已经按照递归出口填好了,而每个位置依赖下一层的上下左右,所以从最后一层开始往第一层填
        for (int start = word.length - 2; start >= 0; start--) {
            for (int row = 0; row < matrix.length; row++) {
                for (int col = 0; col < matrix[row].length; col++) {
                    // 矩阵当前字符==单词当前字符,并且矩阵中能找到单词下个字符开始的后缀串
                    dp[row][col][start] = matrix[row][col] == word[start] && (
                            (row > 0 && dp[row - 1][col][start + 1]) ||
                                    (row < matrix.length - 1 && dp[row + 1][col][start + 1]) ||
                                    (col > 0 && dp[row][col - 1][start + 1]) ||
                                    (col < matrix.length - 1 && dp[row][col + 1][start + 1]));
                    if (start == 0 && dp[row][col][start]) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        char[][] matrix = {
                {'a', 'b', 'c'},
                {'d', 'e', 'f'},
                {'g', 'h', 'f'}
        };
        String word = "effhe";
        System.out.println(canRepeat(matrix, word));
        System.out.println(canRepeatDp(matrix, word));
        System.out.println(unRepeat(matrix, word));
    }

    /**
     * 不可以走重复路,递归试法跟可以重复的一样,只不过每走过一个位置先把字符抹成0,再递归下个位置(深搜),返回之前恢复原本的字符
     * 没法改动态规划,因为matrix也成可变参数了,这么复杂的参数不适合用动态规划
     */
    public static boolean unRepeatProcess(char[][] matrix, int r, int c, char[] word, int i) {
        if (i == word.length) {
            return true;
        }
        if (matrix[r][c] != word[i]) {
            return false;
        }

        // 当前位置标记已走过
        matrix[r][c] = 0;

        // 上下左右4个方向分别递归,只要有一个是true,就返回true
        boolean answer = (r > 0 && canRepeatProcess(matrix, r - 1, c, word, i + 1)) ||
                (r < matrix.length - 1 && canRepeatProcess(matrix, r + 1, c, word, i + 1)) ||
                (c > 0 && canRepeatProcess(matrix, r, c - 1, word, i + 1)) ||
                (c < matrix[r].length - 1 && canRepeatProcess(matrix, r, c + 1, word, i + 1));

        // 深搜返回前恢复现场
        matrix[r][c] = word[i];

        return answer;
    }

    public static boolean unRepeat(char[][] matrix, String str) {
        char[] word = str.toCharArray();
        for (int r = 0; r < matrix.length; r++) {
            for (int c = 0; c < matrix[r].length; c++) {
                if (unRepeatProcess(matrix, r, c, word, 0)) {
                    return true;
                }
            }
        }
        return false;
    }
}
