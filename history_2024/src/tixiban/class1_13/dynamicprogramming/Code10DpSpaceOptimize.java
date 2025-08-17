package tixiban.class1_13.dynamicprogramming;

/**
 * 动态规划表空间优化，用一个一维数组代替二维数组，用一维数组的自我更新代替二维数组的逐行/逐列更新
 * 用经典的动态规划问题举例：
 * 一个二维数组每个位置都有权重，每一步只能往右或往下，每到一个位置都累加当前权重，求从数组左上角0,0位置出发，走到右下角m,m位置的权重最小累加和
 *
 * 动态规划空间压缩推广：（只看按行自我更新，按列是一个道理）
 * 1、如果动态规划表中一个格子依赖自己左、上
 * 假设原二维表第一行是a b c。下一行a’没有左，只依赖上，上就是a，可求；b‘的左就是a’，b‘的上就是b，可求；c’同b‘。按照这个在abc一维数组上完成自我更新。
 * 2、如果动态规划表中一个格子依赖自己上、左上
 * 假设原二维表第一行是a b c。下一行从右往左求，c’的上就是c，左上就是b，可求；b‘同c’；a‘没有左上，只会依赖上，上是a，可求。按照这个套路在abc一维数组上完成自我更新。
 * 3、如果动态规划表中一个格子依赖自己左、上、左上
 * 假设原二维表第一行是a b c。下一行a’只会依赖上是a，可求；用临时变量t记录a；b‘的左就是a’，上就是b，左上就是a，a现在是a‘了，但是记录在t，可求。c’同b‘。按照这个套路在abc一维数组上完成自我更新。
 * 为什么不能按照从右往左求了？因为这种场景依赖自己的左，必须要从左往右。场景2因为不依赖左，所以可以从右往左求，其实从左网友也能求，只不过要像场景3一样用临时变量记录左上。
 */
public class Code10DpSpaceOptimize {
    /**
     * 暴力递归法
     */
    public static int processViolent(int[][] weight, int curX, int curY) {
        if (curX == 0 && curY == 0) {
            return weight[0][0];
        }
        if (curX == 0) {
            return processViolent(weight, curX, curY - 1) + weight[curX][curY];
        }
        if (curY == 0) {
            return processViolent(weight, curX - 1, curY) + weight[curX][curY];
        }

        int p1 = processViolent(weight, curX - 1, curY) + weight[curX][curY];
        int p2 = processViolent(weight, curX, curY - 1) + weight[curX][curY];
        int min = Math.min(p1, p2);
        return min;
    }

    public static int getMinValueViolent(int[][] weight) {
        return processViolent(weight, weight.length - 1, weight[0].length - 1);
    }

    /**
     * 严格表依赖的动态规划
     */
    public static int getMinValueDp(int[][] values) {
        int[][] dp = new int[values.length][values[0].length];
        // 根据base case赋值第一行和第一列
        dp[0][0] = values[0][0];
        for (int x = 1; x < values.length; x++) {
            dp[x][0] = dp[x - 1][0] + values[x][0];
        }
        for (int y = 1; y < values[0].length; y++) {
            dp[0][y] = dp[0][y - 1] + values[0][y];
        }
        // 其余每个位置都依赖左和上
        for (int x = 1; x < values.length; x++) {
            for (int y = 1; y < values[0].length; y++) {
                int p1 = dp[x - 1][y] + values[x][y];
                int p2 = dp[x][y - 1] + values[x][y];
                dp[x][y] = Math.min(p1, p2);
            }
        }
        // 返回最右下角的位置
        return dp[values.length - 1][values[0].length - 1];
    }

    /**
     * 空间优化的动态优化，二维表变一维表
     * 思路：用一个一维数组表示二维数组的一列（表示一行也行。列数少就用行，行数少就用列，这样最省空间。这里默认先用列），每次按列更新dp表
     */
    public static int getMinValueDpSuper(int[][] values) {
        int[] dp = new int[values.length];
        dp[0] = values[0][0];
        // 先计算原二维表的第一列，原来依赖上边位置，现在还是依赖上面位置
        for (int i = 1; i < dp.length; i++) {
            dp[i] = dp[i - 1] + values[i][0];
        }
        for (int y = 1; y < values[0].length; y++) {
            // 原二维表的第一行，也就是每列的第一个，原来依赖左边位置，现在左边就是自身
            dp[0] = dp[0] + values[0][y];
            for (int x = 1; x < dp.length; x++) {
                // 原来依赖上面位置，现在还是依赖上面
                int p1 = dp[x - 1] + values[x][y];
                // 原来依赖左边位置，现在左边就是自身
                int p2 = dp[x] + values[x][y];

                dp[x] = Math.min(p1, p2);
            }
        }
        // 原来最右下角的位置，现在就是最末尾的位置
        return dp[dp.length - 1];
    }


    /**
     * 主函数测试
     */
    public static void main(String[] args) {
        int rowSize = 4;
        int colSize = 4;
        int[][] m = generateRandomMatrix(rowSize, colSize);
        printMatrix(m);
        System.out.println(getMinValueViolent(m));
        System.out.println(getMinValueDp(m));
        System.out.println(getMinValueDpSuper(m));
    }

    // for test
    public static int[][] generateRandomMatrix(int rowSize, int colSize) {
        if (rowSize < 0 || colSize < 0) {
            return null;
        }
        int[][] result = new int[rowSize][colSize];
        for (int i = 0; i != result.length; i++) {
            for (int j = 0; j != result[0].length; j++) {
                result[i][j] = (int) (Math.random() * 10);
            }
        }
        return result;
    }

    // for test
    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i != matrix.length; i++) {
            for (int j = 0; j != matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}
