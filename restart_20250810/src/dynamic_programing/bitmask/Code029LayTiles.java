package dynamic_programing.Bitmask;

import tixiban.class31状态压缩的动态规划.NMFiledWith12Test;

/**
 * 你有无限的1*2的瓷砖，要铺满M*N的区域，不同的铺法有多少种？
 * 
 * 每个一般位置的可能性：
 * 看上去有5种：1、当前格子+上格子铺一块瓷砖，2、当前格子+右格子铺一块瓷砖，3、当前格子+下格子铺一块瓷砖，4、当前格子+左格子铺一块瓷砖，5、不铺
 * 实际上可以优化成3种：1、当前格子+上格子铺一块瓷砖，2、当前格子+右格子铺一块瓷砖，3、不铺
 * 为什么：因为当前格子+左格子，实际上就是左边格子的「当前格子+右格子」，当前格子+下格子，实际上就是下边格子的「当前格子+上格子」
 * 
 * 试法：
 * 给定一个行号和上一行的状态（每个格子铺了or没铺），返回从该行开始把剩余区域全铺完，有几种铺法
 * 每个格子要么上面没铺只能向上铺把上面填满，要么向右铺，要么不铺等着被下一行填满
 */
public class Code029LayTiles {
    /**
     * 阶段一解法：暴力递归法
     * 
     * @param m
     * @param n
     * @return
     */
    public static int force(int m, int n) {
        // 如果m和n有一个<1，或者区域的面积是奇数，那么无论如何也无法用1*2的瓷砖铺满
        if (n < 1 || m < 1 || ((n * m) & 1) != 0) {
            return 0;
        }
        // 如果区域的面积是偶数，并且m和n有一个是1，那么只有顺着铺一溜这一种铺法
        if (n == 1 || m == 1) {
            return 1;
        }
        // 用一个数组表示上一行的状态，1表示已铺瓷砖，0表示没铺
        int[] preStatus = new int[n];
        // 初始化：对第0行来说，上一行全部都铺了
        for (int i = 0; i < preStatus.length; i++) {
            preStatus[i] = 1;
        }

        // 递归，上一行的状态是preStatus，当前行是第0行，总共有m行
        return process(preStatus, 0, m);
    }

    /**
     * 表维度的递归函数
     * 上一行的状态是preStatus，当前行是第curRow行，总共有rows行，
     * 返回从curRow开始铺完剩下的区域总共有多少种铺法
     * 
     * @param preStatus
     * @param curRow
     * @param rows
     * @return
     */
    private static int process(int[] preStatus, int curRow, int rows) {
        // 如果所有行都已经遍历完了，那么检查最后一行有没有空位置
        // 如果有，说明没贴满，不是一种有效的铺法，返回0
        // 如果没有，说明都贴满了，成功得到了一种有效的铺法，返回1
        if (curRow == rows) {
            for (int i = 0; i < preStatus.length; i++) {
                if (preStatus[i] == 0) {
                    return 0;
                }
            }
            return 1;
        }

        // 当前行的初始状态：
        // 如果上面格子=0，说明上面格子没铺，那么当前格子必须向上铺，也就是当前格子=1，上面格子=1（上面格子要怎样已经不需要记录了）
        // 如果上面格子=1，说明上面格子已经铺了，那么当前格子可以选择不铺（当前格子=0），也可以选择向右铺（当前格子和右边格子=1），具体怎么铺，就是要遍历的各种可能性
        int[] status = new int[preStatus.length];
        for (int i = 0; i < status.length; i++) {
            status[i] = preStatus[i] == 1 ? 0 : 1;
        }

        // 在一行上玩深度优先遍历，并且支持一直往下一行遍历，返回所有的铺法
        return processRow(status, 0, curRow, rows);
    }

    /**
     * 行维度的递归函数
     * 当前行目前的状态是status，当前是第curCol列。
     * curRow、rows是开启下一行遍历所需要的参数
     * 
     * @param status
     * @param curCol
     * @param curRow
     * @param rows
     * @return
     */
    public static int processRow(int[] status, int curCol, int curRow, int rows) {
        // 如果这一行所有列都已经遍历完了，那么开始遍历下一行
        if (curCol == status.length) {
            return process(status, curRow + 1, rows);
        }
        // 如果当前列已经铺了，直接跳过
        if (status[curCol] == 1) {
            return processRow(status, curCol + 1, curRow, rows);
        }

        // 空闲的格子，选择不铺或者向右铺
        // 可能性一：不铺
        int noTile = processRow(status, curCol + 1, curRow, rows);
        // 可能性二：如果右边格子也没铺，那么可以选择向右铺
        int rightTile = 0;
        if (curCol < status.length - 1 && status[curCol + 1] == 0) {
            // 深度优先遍历保存现场
            status[curCol] = 1;
            status[curCol + 1] = 1;
            // 递归铺+2列
            rightTile = processRow(status, curCol + 2, curRow, rows);
            // 深度优先遍历恢复现场
            status[curCol] = 0;
            status[curCol + 1] = 0;
        }

        return noTile + rightTile;
    }



    /**
     * 阶段二解法：暴力递归（一个函数，不嵌套）
     * 
     * @param n
     * @param m
     * @return
     */
    public static int force1(int n, int m) {
        if (n < 1 || m < 1 || ((n * m) & 1) != 0) {
            return 0;
        }
        if (n == 1 || m == 1) {
            return 1;
        }

        // 用一个数组表示上一行的状态，1表示已铺瓷砖，0表示没铺
        int[] preStatus = new int[n];
        // 初始化：对第0行来说，上一行全部都铺了
        for (int i = 0; i < preStatus.length; i++) {
            preStatus[i] = 1;
        }

        // 用一个数组表示当前行的状态，初始化：对第0行来说，当前行没有任何列已铺，也没有任何列必须要铺，可以自由发挥
        int[] curStatus = new int[preStatus.length];

        // 递归，上一行的状态是preStatus，当前行的状态是curStatus，从第0行开始铺，总共有m行，返回一共有多少种铺法
        return process1(preStatus, curStatus, 0, 0, m);
    }

    /**
     * 递归函数
     * 单个函数，不涉及嵌套
     * 
     * @param preStatus
     * @param curStatus
     * @param curRow
     * @param curCol
     * @param rows
     * @return
     */
    private static int process1(int[] preStatus, int[] curStatus, int curRow, int curCol, int rows) {
        // 如果所有行都已经遍历完，那么检查最后一行的状态，如果铺满了那么恭喜得到一种铺法，如果没铺满那么不是有效的铺法。
        if (curRow == rows) {
            for (int i = 0; i < preStatus.length; i++) {
                if (preStatus[i] == 0) {
                    return 0;
                }
            }
            return 1;
        }

        // 如果当前行的所有列都已经遍历完，那么进入下一行
        if (curCol == preStatus.length) {
            // 下一行的状态有限制：
            // 如果当前行第i列没铺，那么下一行第i列必须向上铺，也就是下一行i列=1；
            // 如果当前行i列铺了，那么下一行可以不铺或者向右铺，留给下一行自由发挥
            int[] nextStatus = new int[curStatus.length];
            for (int i = 0; i < nextStatus.length; i++) {
                nextStatus[i] = curStatus[i] == 0 ? 1 : 0;
            }
            return process1(curStatus, nextStatus, curRow + 1, 0, rows);
        }

        // 如果当前行当前列已经铺了（上一行的限制），那么直接跳过，进入下一列
        if (curStatus[curCol] == 1) {
            return process1(preStatus, curStatus, curRow, curCol + 1, rows);
        }

        // 当前行当前列，选择不铺，或者向右铺
        int ways = process1(preStatus, curStatus, curRow, curCol + 1, rows);
        if (curCol < curStatus.length - 1 && curStatus[curCol + 1] == 0) {
            curStatus[curCol] = 1;
            curStatus[curCol + 1] = 1;
            ways += process1(preStatus, curStatus, curRow, curCol + 2, rows);
            curStatus[curCol] = 0;
            curStatus[curCol + 1] = 0;
        }
        return ways;
    }



    /**
     * 阶段三解法：状态压缩
     * 用一个整数的位信息表示上一行的状态
     * 
     * @param m
     * @param n
     * @return
     */
    public static int forceBitmask(int m, int n) {
        if (m < 1 || n < 1 || (m * n & 1) != 0) {
            return 0;
        }
        if (m == 1 || n == 1) {
            return 1;
        }

        // 找出m，n中较小的那个当列，因为列要用位信息表示，列数越大，占用的位数越大，status的取值范围就越大
        int rows = Math.max(m, n);
        int cols = Math.min(m, n);

        // 上一行的状态，以及当前行的状态
        int preStatus = (1 << cols) - 1;
        int curStatus = 0;

        return processBitmask(preStatus, curStatus, rows, cols, 0, 0);
    }

    private static int processBitmask(int preStatus, int curStatus, int rows, int cols, int curRow, int curCol) {
        // 如果所有行都遍历完了，就检查最后一行的状态，如果铺满了，那么恭喜你发现了一种有效的铺法
        if (curRow == rows) {
            // 如果最后一行的状态是1111...，说明都铺满了
            return preStatus == (1 << cols) - 1 ? 1 : 0;
        }

        // 如果当前行所有列都遍历完了，就开始下一行
        if (curCol == cols) {
            // 下一行的初始状态等于当前行的状态取反（截取有效位）
            int nextStatus = (~curStatus) & ((1 << cols) - 1);
            return processBitmask(curStatus, nextStatus, rows, cols, curRow + 1, 0);
        }

        // 如果当前格子已经铺过了，那么直接跳过
        if ((curStatus & (1 << curCol)) != 0) {
            return processBitmask(preStatus, curStatus, rows, cols, curRow, curCol + 1);
        }

        // 统计当前格子的所有可能性：
        // 不铺
        int ways = processBitmask(preStatus, curStatus, rows, cols, curRow, curCol + 1);
        // 如果右边还有格子，并且右边格子还没铺，那么可以选择向右铺
        if (curCol < cols - 1 && (curStatus & (1 << (curCol + 1))) == 0) {
            ways += processBitmask(preStatus, curStatus | (3 << curCol), rows, cols, curRow, curCol + 2);
        }

        return ways;
    }



    /**
     * 阶段四解法：状态压缩+傻缓存
     * 
     * @param m
     * @param n
     * @return
     */
    public static int forceBitmaskCache(int m, int n) {
        if (m < 1 || n < 1 || (m * n & 1) != 0) {
            return 0;
        }
        if (m == 1 || n == 1) {
            return 1;
        }

        // 找出m，n中较小的那个当列，因为列要用位信息表示，列数越大，占用的位数越大，status的取值范围就越大
        int rows = Math.max(m, n);
        int cols = Math.min(m, n);

        // 上一行的状态和当前行的状态
        int preStatus = (1 << cols) - 1;
        int curStatus = 0;

        // 有4个可变参数共同决定递归函数的结果，所以缓存4个可变参数
        int[][][][] cache = new int[1 << cols][1 << cols][rows + 1][cols + 1];
        for (int i = 0; i < cache.length; i++) {
            for (int j = 0; j < cache[0].length; j++) {
                for (int k = 0; k < cache[0][0].length; k++) {
                    for (int l = 0; l < cache[0][0][0].length; l++) {
                        cache[i][j][k][l] = -1;
                    }
                }
            }
        }

        return processBitmaskCache(preStatus, curStatus, rows, cols, 0, 0, cache);
    }

    private static int processBitmaskCache(int preStatus, int curStatus, int rows, int cols, int curRow, int curCol, int[][][][] cache) {
        if (curRow == rows) {
            return preStatus == (1 << cols) - 1 ? 1 : 0;
        }

        // 如果有缓存，就直接走缓存
        if (cache[preStatus][curStatus][curRow][curCol] != -1) {
            return cache[preStatus][curStatus][curRow][curCol];
        }

        if (curCol == cols) {
            int nextStatus = (~curStatus) & ((1 << cols) - 1);
            return processBitmaskCache(curStatus, nextStatus, rows, cols, curRow + 1, 0, cache);
        }

        if ((curStatus & (1 << curCol)) != 0) {
            return processBitmaskCache(preStatus, curStatus, rows, cols, curRow, curCol + 1, cache);
        }

        int ways = processBitmaskCache(preStatus, curStatus, rows, cols, curRow, curCol + 1, cache);
        if (curCol < cols - 1 && (curStatus & (1 << (curCol + 1))) == 0) {
            ways += processBitmaskCache(preStatus, curStatus | (3 << curCol), rows, cols, curRow, curCol + 2, cache);
        }

        // 计算答案保存到缓存
        cache[preStatus][curStatus][curRow][curCol] = ways;
        return ways;
    }

    /**
     * 阶段五解法：严格位置依赖的动态规划
     * 完全根据上一阶段傻缓存改过来的
     * 面试的时候直接用傻缓存就行，时间复杂度差不多，还好写
     * 
     * @param m
     * @param n
     * @return
     */
    public static int dp(int m, int n) {
        if (m < 1 || n < 1 || (m * n & 1) != 0) {
            return 0;
        }
        if (m == 1 || n == 1) {
            return 1;
        }

        int rows = Math.max(m, n);
        int cols = Math.min(m, n);

        int[][][][] dp = new int[1 << cols][1 << cols][rows + 1][cols + 1];

        for (int j = 0; j < dp[0].length; j++) {
            for (int l = 0; l < dp[0][0][0].length; l++) {
                dp[(1 << cols) - 1][j][rows][l] = 1;
            }
        }

        // 只有curRow=rows的那一层的所有数据都确定了，所以一定要把curRow放在最外层，按照curRow一层一层地填表，每一层依赖自己的+1层
        for (int curRow = rows - 1; curRow >= 0; curRow--) {
            for (int preStatus = 0; preStatus < dp.length; preStatus++) {
                // curStatus要先填大的再填小的，因为从向右铺的依赖关系可以看出，curStatus依赖比自己大的
                for (int curStatus = dp[0].length - 1; curStatus >= 0; curStatus--) {
                    // curCol也依赖自己的+1或者+2，所以也是先填大的再填小的
                    for (int curCol = cols; curCol >= 0; curCol--) {
                        int ways = 0;
                        if (curCol == cols) {
                            ways = dp[curStatus][(~curStatus) & ((1 << cols) - 1)][curRow + 1][0];
                        } else if ((curStatus & (1 << curCol)) != 0) {
                            ways = dp[preStatus][curStatus][curRow][curCol + 1];
                        } else {
                            ways = dp[preStatus][curStatus][curRow][curCol + 1];
                            if (curCol < cols - 1 && (curStatus & (1 << (curCol + 1))) == 0) {
                                ways += dp[preStatus][curStatus | (3 << curCol)][curRow][curCol + 2];
                            }
                        }
                        dp[preStatus][curStatus][curRow][curCol] = ways;
                    }
                }
            }
        }
        return dp[(1 << cols) - 1][0][0][0];
    }

    public static void main(String[] args) {
        // int n = 3;
        // int m = 4;
        // System.out.println(force(m, n));
        // System.out.println(force1(m, n));
        // System.out.println(NMFiledWith12Test.ways1(n, m));

        for (int times = 0; times < 100; times++) {
            int n = (int) (Math.random() * 10);
            int m = (int) (Math.random() * 10);
            int dp = dp(m, n);
            int forceBitmask = forceBitmask(m, n);
            int ways1 = NMFiledWith12Test.ways1(n, m);
            if (dp != forceBitmask || forceBitmask != ways1) {
                System.out.println("执行出错,m=" + m + ",n=" + n + ",dp=" + dp + ",forceBitmask=" + forceBitmask + ",ways1=" + ways1);
            }
        }
        System.out.println("完美通过");
    }
}
