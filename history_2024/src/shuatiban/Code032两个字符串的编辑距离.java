package shuatiban;

/**
 * https://leetcode.cn/problems/edit-distance/description/
 * 给你两个单词 word1 和 word2， 请返回将 word1 转换成 word2 所使用的最少操作数。
 * 你可以对一个单词进行如下三种操作：
 * 插入一个字符
 * 删除一个字符
 * 替换一个字符
 *
 * 上面是leetcode原题,插入、删除、替换所需要的操作数相当于都是1.
 * 下面我们加强一下,插入、删除、替换所需要的操作数可以任意指定.
 *
 * 思路:看到两个字符串,有点匹配的意思,就要想到动态规划,样本对应模型.
 * 动态规划样本对应模型,往往是根据最后一个位置来展开可能性划分的.
 */
public class Code032两个字符串的编辑距离 {
    /**
     * 递归试法,字符串1用前index1个字符,最少用多少编辑距离能转换成字符串2前index2个字符
     */
    public static int process(char[] str1, int index1, char[] str2, int index2, int insert, int delete, int update) {
        // 递归出口1.如果str1和str2长度都是0,空字符串转换成空字符串的最小编辑距离为0
        if (index1 == 0 && index2 == 0) {
            return 0;
        }
        // 递归出口2.如果str1长度为0,str2长度不为0,那么空字符串转换成str2的最小编辑距离是插入str2的所有字符所需要的距离
        if (index1 == 0) {
            return insert * index2;
        }
        // 递归出口3.如果str1长度不为0,str2长度为0,那么str1转换成空字符串的最小编辑距离是删除str2所有字符所需要的距离
        if (index2 == 0) {
            return delete * index1;
        }
        // 可能性1.先花一个update距离把str1的最后一个字符变成str2的最后一个字符(如果二者最后一个字符本就相等,就省去一个update距离),
        // 然后递归用str1剩下的前index1-1个字符,转换成str2剩下的前index2-1个字符.
        int result = (str1[index1 - 1] == str2[index2 - 1] ? 0 : update) + process(str1, index1 - 1, str2, index2 - 1, insert, delete, update);
        // 可能性2.先花一个delete距离删掉str1最后一个字符,然后递归用str1剩下的前index1-1个字符转换成str2前index2个字符.
        result = Math.min(result, delete + process(str1, index1 - 1, str2, index2, insert, delete, update));
        // 可能性3.先花一个insert距离在str1结尾插入str2的结尾字符,然后递归用str1前index1个字符转换成str2剩下的前index2-1个字符.
        result = Math.min(result, insert + process(str1, index1, str2, index2 - 1, insert, delete, update));

        // 返回编辑距离最小的可能性
        return result;
    }

    public static int violent(String s1, String s2) {
        return process(s1.toCharArray(), s1.length(), s2.toCharArray(), s2.length(), 1, 1, 1);
    }

    /**
     * 改动态规划
     */
    public static int minDistance1(String s1, String s2) {
        int insert = 1;
        int delete = 1;
        int update = 1;

        // str1转换成str2,编辑距离最小是多少
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();

        int[][] dp = new int[str1.length + 1][str2.length + 1];
        // str1是空串
        for (int i = 1; i <= str2.length; i++) {
            dp[0][i] = insert * i;
        }
        // str2是空串
        for (int i = 1; i <= str1.length; i++) {
            dp[i][0] = delete * i;
        }

        for (int i1 = 1; i1 <= str1.length; i1++) {
            for (int i2 = 1; i2 <= str2.length; i2++) {
                dp[i1][i2] = (str1[i1 - 1] == str2[i2 - 1] ? 0 : update) + dp[i1 - 1][i2 - 1];
                dp[i1][i2] = Math.min(dp[i1][i2], delete + dp[i1 - 1][i2]);
                dp[i1][i2] = Math.min(dp[i1][i2], insert + dp[i1][i2 - 1]);
            }
        }

        return dp[str1.length][str2.length];
    }

    /**
     * 空间优化
     * 由于每个格子只依赖自己左边、上边、左上,因此可以简化成用一个一维数组滚动
     */
    public static int minDistance(String s1, String s2) {
        int insert = 1;
        int delete = 1;
        int update = 1;

        // str1转换成str2,编辑距离最小是多少
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();

        int[] dp = new int[str2.length + 1];
        // str1长度为0的时候
        for (int i2 = 1; i2 <= str2.length; i2++) {
            dp[i2] = insert * i2;
        }

        for (int i1 = 1; i1 <= str1.length; i1++) {
            // 记录左上角格子
            int leftUp = dp[0];
            dp[0] = i1 * delete;
            for (int i2 = 1; i2 <= str2.length; i2++) {
                // 左边格子就是现在覆盖之前的dp[i2],上边格子就是单纯的上边dp[i2-1],左上角是覆盖之前的dp[i2]的上边,需要提前记录,因为必然会被覆盖.
                int min = (str1[i1 - 1] == str2[i2 - 1] ? 0 : update) + leftUp;
                min = Math.min(min, delete + dp[i2]);
                min = Math.min(min, insert + dp[i2 - 1]);
                // 现在的左边,就是下一轮循环的左上.
                leftUp = dp[i2];
                dp[i2] = min;
            }
        }
        return dp[str2.length];
    }

}
