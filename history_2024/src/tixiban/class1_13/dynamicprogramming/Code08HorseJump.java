package tixiban.class1_13.dynamicprogramming;

/**
 * 象棋跳马
 * 给定一个10*9的象棋棋盘，一个马放在(0,0)位置，求马跳k步恰好跳到(a,b)位置的方法数
 */
public class Code08HorseJump {
    /**
     * 暴力递归
     *
     * @param startX 当前位置x
     * @param startY 当前位置y
     * @param restSteps 剩余步数
     * @param aimX 目标位置x
     * @param aimY 目标位置y
     * @return
     */
    public static int processViolent(int startX, int startY, int restSteps, int aimX, int aimY) {
        // base case 1、如果走着走着起点越界了，返回0，说明这条路的尝试是无效的
        if (startX < 0 || startX > 9 || startY < 0 || startY > 8) {
            return 0;
        }
        // base case 2、剩余步数为0时，只有当前起点恰好是目标点的时候，才返回1，表示找到了一条路，否则返回0
        if (restSteps == 0) {
            return startX == aimX && startY == aimY ? 1 : 0;
        }

        // 马走日，8个方向8中可能性的方法数累加
        int ways = 0;
        ways += processViolent(startX + 2, startY + 1, restSteps - 1, aimX, aimY);
        ways += processViolent(startX + 1, startY + 2, restSteps - 1, aimX, aimY);
        ways += processViolent(startX - 1, startY + 2, restSteps - 1, aimX, aimY);
        ways += processViolent(startX - 2, startY + 1, restSteps - 1, aimX, aimY);
        ways += processViolent(startX - 2, startY - 1, restSteps - 1, aimX, aimY);
        ways += processViolent(startX - 1, startY - 2, restSteps - 1, aimX, aimY);
        ways += processViolent(startX + 1, startY - 2, restSteps - 1, aimX, aimY);
        ways += processViolent(startX + 2, startY - 1, restSteps - 1, aimX, aimY);

        return ways;
    }

    public static int horseJumpViolent(int aimX, int aimY, int steps) {
        if (aimX < 0 || aimX > 9 || aimY < 0 || aimY > 8 || steps <= 0) {
            return 0;
        }
        return processViolent(0, 0, steps, aimX, aimY);
    }

    /**
     * 动态规划表
     */
    public static int horseJumpDp(int aimX, int aimY, int steps) {
        if (aimX < 0 || aimX > 9 || aimY < 0 || aimY > 8 || steps < 0) {
            return 0;
        }

        // 可变参数startX、startY、restSteps，变化范围分别是0-10、0-9、0-steps
        int[][][] dp = new int[10][9][steps + 1];

        // 根据base case 2 ，赋值第0层
        dp[aimX][aimY][0] = 1;
        // 根据8种可能性，赋值第1~steps层
        /** 注意，填表的时候一定先遍历steps，也就是按层遍历，因为由递归尝试可知当前层依赖上一层，必须确保上一层的全部x、y都计算完毕，才能计算当前层 */
        for (int z = 1; z <= steps; z++) {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 9; y++) {
                    dp[x][y][z] += peek(dp, x + 2, y + 1, z - 1);
                    dp[x][y][z] += peek(dp, x + 1, y + 2, z - 1);
                    dp[x][y][z] += peek(dp, x - 1, y + 2, z - 1);
                    dp[x][y][z] += peek(dp, x - 2, y + 1, z - 1);
                    dp[x][y][z] += peek(dp, x - 2, y - 1, z - 1);
                    dp[x][y][z] += peek(dp, x - 1, y - 2, z - 1);
                    dp[x][y][z] += peek(dp, x + 1, y - 2, z - 1);
                    dp[x][y][z] += peek(dp, x + 2, y - 1, z - 1);
                }
            }
        }
        /** 返回值的xyz怎么取？就看递归尝试的返回值，可变参数怎么取 */
        return dp[0][0][steps];
    }

    // 根据坐标取值，如果越界返回0
    public static int peek(int[][][] arr, int x, int y, int z) {
        // 根据base case 1，越界的话返回0
        if (x < 0 || x > 9 || y < 0 || y > 8) {
            return 0;
        }
        return arr[x][y][z];
    }

    /**
     * 主函数
     */
    public static void main(String[] args) {
        int aimX = 1;
        int aimY = 2;
        int steps = 3;
        System.out.println(horseJumpViolent(aimX, aimY, steps));
        System.out.println(horseJumpDp(aimX, aimY, steps));
    }
}
