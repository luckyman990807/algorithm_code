package dynamic_programing.Bitmask;

/**
 * TSP问题
 * 有N个城市，任意两个城市之间都有距离，任何城市到自身的距离都是0，所有城市之间的距离都存在N*N的二维数组matrix里，也就是整张图由邻接矩阵表示。
 * 现在要求一旅行商从k城市出发，必须经过每一个城市并且每个城市只经过一次，最后返回出发的k城市。
 * 给定matrix，k，求最短路径长度。
 */
public class Code027TSP {
    /**
     * 解法第一阶段：暴力递归法
     */
    public static int force(int[][] matrix, int k) {
        // 用一个数组表示每个城市的状态，1:已经过，0:未经过，目前已经在k城市，k已经过
        int[] status = new int[matrix.length];
        status[k] = 1;

        // 递归，matrix是邻接矩阵，status是每个城市经过与否的状态，从k出发，把status中剩余没经过的城市都经过一遍，最终回到k，返回最短路径长度
        return process(matrix, status, k, k);
    }

    /**
     * 递归函数
     * 求从cur城市出发，遍历剩余所有城市，最终回到end的最短路径。
     * matrix是每两个城市的距离，status是每个城市是否已经过的标识
     * 
     * @param matrix
     * @param status
     * @param cur
     * @param end
     * @return
     */
    private static int process(int[][] matrix, int[] status, int cur, int end) {
        // 如果全部城市都已经过，那么可以直接返程，从当前城市到end城市
        int count = 0;
        for (int i = 0; i < status.length; i++) {
            if (status[i] == 1) {
                count++;
            }
        }
        if (count == status.length) {
            return matrix[cur][end];
        }

        // 还有未经过的城市，需要枚举所有可能性（从当前城市可以去往的所有城市）
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < status.length; i++) {
            if (status[i] == 1) {
                continue;
            }
            // 深度优先遍历记录现场
            status[i] = 1;
            // 从当前城市首先去往i城市，并且遍历剩余所有城市，最终回到end，的最短路径
            int curToEnd = matrix[cur][i] + process(matrix, status, i, end);
            // 所有可能性取最小值
            min = Math.min(min, curToEnd);
            // 深度优先遍历恢复现场
            status[i] = 0;
        }

        return min;
    }

    /**
     * 解法第二阶段：状态压缩+傻缓存法
     * 用一个整数的位信息表示每个城市的访问状态，同时增加缓存表
     * 
     * @param matrix
     * @param k
     * @return
     */
    public static int forceDp(int[][] matrix, int k) {
        // 用一个整数的位信息表示每个城市的访问状态，1:已经过，0:未经过，目前已经在k城市，k已经过
        int status = 0;
        status |= 1 << k;

        // 用二维数组做缓存。为什么是二维，因为status和cur两个可变参数共同决定结果
        int[][] cache = new int[1 << matrix.length][matrix.length];

        // 递归，matrix是邻接矩阵，status是每个城市经过与否的状态，从k出发，把status中剩余没经过的城市都经过一遍，最终回到k，返回最短路径长度
        return processDP(matrix, status, cache, k, k);
    }

    private static int processDP(int[][] matrix, int status, int[][] cache, int cur, int end) {
        // 如果有缓存，直接走缓存
        if (cache[status][cur] != 0) {
            return cache[status][cur];
        }

        // 如果所有城市都已经遍历过，那么直接去往end城市
        if (status == (1 << matrix.length) - 1) {
            cache[status][cur] = matrix[cur][end];
            return cache[status][cur];
        }

        // 尚有未遍历的城市，枚举所有可能性（当前城市可去往的所有城市）
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            // 如果i城市已经遍历过了，直接跳过
            int visited = status & (1 << i);
            if (visited != 0) {
                continue;
            }

            // 可能性枚举：从当前城市首先去往i城市，计算遍历剩余所有城市最终回到end的最短路径
            int statusTmp = status | (1 << i);
            int curToEnd = matrix[cur][i] + processDP(matrix, statusTmp, cache, i, end);

            min = Math.min(min, curToEnd);
        }

        return min;
    }

    /**
     * 解法第三阶段：完全位置依赖的动态规划
     * 
     * @param matrix
     * @param k
     * @return
     */
    public static int dp(int[][] matrix, int k) {
        // dp表
        int[][] dp = new int[1 << matrix.length][matrix.length];
        // 初始化：如果所有城市都已经被遍历，那么从cur到end的最短路径就是他俩的距离
        int allVisitedStatus = (1 << matrix.length) - 1;
        for (int cur = 0; cur < matrix.length; cur++) {
            dp[allVisitedStatus][cur] = matrix[cur][k];
        }

        // 填dp表
        // 为什么status要从大往小填？
        // 简单分析一下依赖关系：第status行的每个格子依赖比status多一个1的所有行，那么肯定都是比status大的行，粗略估计出是上依赖下。
        // 然后简单举例验证一下：假设有4个城市，status取值范围0～15，第15行已初始化，第14行依赖谁呢？
        // 14=1110，比他多一个1的只有1111，也就是14行依赖15行
        // 13=1101，比他多一个1的也只有1111，也就是13行依赖15行
        // 12=1100，比他多一个1的有1110，1101，也就是12行依赖14行和13行......
        // 最后发现所有行都依赖比他大的行，所以从大往小填，当前行所依赖的所有行都已经被算过了
        for (int status = allVisitedStatus - 1; status >= 0; status--) {
            // cur从大到小还是从小到大无所谓
            for (int cur = 0; cur < dp[0].length; cur++) {
                int min = Integer.MAX_VALUE;
                for (int i = 0; i < matrix.length; i++) {
                    // 跳过已经遍历过的城市
                    int visited = status & (1 << i);
                    if (visited != 0) {
                        continue;
                    }

                    // 可能性枚举：从当前城市首先去往i城市，计算遍历剩余所有城市最终回到end的最短路径
                    int statusTmp = status | (1 << i);
                    int curToEnd = matrix[cur][i] + dp[statusTmp][i];

                    min = Math.min(min, curToEnd);
                }
                dp[status][cur] = min;
            }
        }

        // 上面的遍历填表可以优化，status遍历到1<<k即可，cur遍历时判断如果cur=k并且status=1<<k，那么直接返回当前格子
        return dp[1 << k][k];
    }


    
    public static void main(String[] args) {
        int[][] matrix = { { 0, 3, 1, 2 }, { 3, 0, 4, 2 }, { 1, 4, 0, 5 }, { 2, 2, 5, 0 } };
        System.out.println(force(matrix, 0));
        System.out.println(forceDp(matrix, 0));
        System.out.println(dp(matrix, 0));
    }

}
