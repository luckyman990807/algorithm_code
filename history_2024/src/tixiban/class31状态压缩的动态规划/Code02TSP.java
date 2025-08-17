package tixiban.class31状态压缩的动态规划;


import java.util.ArrayList;
import java.util.List;

/**
 * TSP问题
 * 有N个城市,任意两个城市之间都有距离,记录在一个N*N的矩阵里,任何一座城市到自己的距离都是0.也就是用邻接矩阵表示一张图.
 * 一个旅行商从k点出发必须经过每个城市并且只经过一次,最终回到k点,求最短距离.
 * 给定邻接矩阵,给定k,返回最短距离
 */
public class Code02TSP {

    /**
     * 暴力递归
     * 尝试思路:从start位置出发,走完set集合里剩余的点,最后再回到0点,返回最小距离.任意两点之间的距离记录在matrix里
     */
    public static int process(int[][] matrix, int start, List<Integer> list) {
        // 递归出口:如果集合里没有其他点作为下一个了,说明当前start下一步只能回到0了
        int nodes = 0;
        for (Integer integer : list) {
            if (integer != null) {
                nodes++;
            }
        }
        if (nodes == 0) {
            return matrix[start][0];
        }

        // 还有其他节点作为start的下一个
        int min = Integer.MAX_VALUE;
        for (int next = 0; next < list.size(); next++) {
            if (list.get(next) != null) {
                list.set(next, null);
                min = Math.min(min, matrix[start][next] + process(matrix, next, list));
                list.set(next, 1);
            }
        }

        return min;
    }

    public static int violent(int[][] matrix) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            list.add(i, 1);
        }
        list.set(0, null);
        return process(matrix, 0, list);
    }

    /**
     * 状态压缩+傻缓存
     * status第1位=1表示1号节点已经被走过了,第2位=0表示2号节点没被走过
     * dp表是二维的,因为start和status共同决定一个结果
     * start表示当前子过程从哪个节点出发,status表示当前哪些节点被走过了哪些节点没被走过,dp记录缓存,matrix任意两点的距离
     */
    public static int dpProcess(int[][] matrix, int start, int status, int[][] dp) {
        // 先读缓存
        if (dp[start][status] != -1) {
            return dp[start][status];
        }

        if (status == (1 << matrix.length) - 1) {
            // 如果status变成1111111这种格式,说明所有节点都被走过了,那么下一步只能回到0
            dp[start][status] = matrix[start][0];
        } else {
            // 如果第next位=0,说明next号节点没被走过,那就把next号标为已走过,递归从next出发的过程
            int min = Integer.MAX_VALUE;
            for (int next = 0; next < matrix.length; next++) {
                if (((1 << next) & status) == 0) {
                    min = Math.min(min, matrix[start][next] + dpProcess(matrix, next, status | (1 << next), dp));
                }
            }
            // return之前记录缓存
            dp[start][status] = min;
        }

        return dp[start][status];
    }

    public static int dp(int[][] matrix) {
        int[][] dp = new int[matrix.length][1 << matrix.length];
        for (int i = 0; i < dp.length; i++) {
            for (int j = 0; j < dp[i].length; j++) {
                dp[i][j] = -1;
            }
        }
        return dpProcess(matrix, 0, 1, dp);
    }


    public static void main(String[] args) {
        int[][] matrix = {
                {0, 1, 2},
                {1, 0, 3},
                {2, 3, 0}
        };
        System.out.println(violent(matrix));
        System.out.println(dp(matrix));
    }
}
