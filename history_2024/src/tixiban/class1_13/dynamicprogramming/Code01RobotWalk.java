package tixiban.class1_13.dynamicprogramming;

/**
 * 一个机器人在1~N范围上走，每次只能走一个位置，在1处只能往后走，在N处只能往前走，其他位置可以往前走也可以往后走。
 * 给定N的大小、机器人的初始位置、目标位置、允许移动的步数，要求从初始位置到达目标位置的路径数量
 * 例如N=4，初始位置2，目标位置4，允许移动4，返回路径数量3（2-1-2-3-4， 2-3-2-3-4， 2-3-4-3-4）
 */
public class Code01RobotWalk {
    /**
     * 方法一
     */

    /**
     * 暴力递归
     * 给定当前位置、当前剩余步数、目标位置、活动空间的范围，返回到达目标的路径数
     *
     * @param curPosition 当前位置
     * @param restStep    当前剩余步数
     * @param aim         目标位置
     * @param size        活动范围
     * @return
     */
    public static int processViolent(int curPosition, int restStep, int aim, int size) {
        // 剩余步数=0时，如果当前位置恰好是目标位置，说明找到了一条路径，返回1，否则就是没找到路径，返回0
        if (restStep == 0) {
            return curPosition == aim ? 1 : 0;
        }

        // 当前在1位置，下一步只能走2位置，所以从1走restStep步到aim的路径数 = 从2走restStep-1步到aim的路径数
        if (curPosition == 1) {
            return processViolent(2, restStep - 1, aim, size);
        }
        // 当前在最后一个位置，下一步只能走size-1位置，所以从size走restStep步到aim的路径数 = 从size-1走restStep-1步到aim的路径数
        if (curPosition == size) {
            return processViolent(size - 1, restStep - 1, aim, size);
        }
        // 当前在中间位置，下一步既可以走cur-1也可以走cur+1，所以同理从cur走restStep步到aim的路径数 = 两种走法的路径数加起来
        return processViolent(curPosition - 1, restStep - 1, aim, size)
                + processViolent(curPosition + 1, restStep - 1, aim, size);
    }

    public static int robotWalkViolent(int curPosition, int totalStep, int aim, int size) {
        // 参数校验
        if (size < 2 || aim < 1 || aim > size || curPosition < 1 || curPosition > size || totalStep < 0) {
            return -1;
        }
        return processViolent(curPosition, totalStep, aim, size);
    }

    /**
     * 方法二
     */

    /**
     * 使用缓存的动态规划
     * 从上面暴力递归可以看出，每次递归的参数只有当前位置curPos、剩余步数restStep是可变参数，而目标位置aim、活动范围size是恒定参数，
     * 可以看作一个二元函数，curPos、restStep是两个自变量，aim、size是两个常数，递归就是求f(cur, rest)的值。
     * 在递归f(cur, rest)时，有可能f(cur, rest)的值在之前的递归中已经算过了，为了避免重复计算，用一个二维数组记录之前算过的f(cur, rest)的值。
     * 和暴力递归的区别仅仅是用缓存记录历史计算结果，避免重复计算
     *
     * @param curPos   当前位置
     * @param restStep 剩余步数
     * @param aim      目标位置
     * @param size     活动范围
     * @param ways     缓存
     * @return
     */
    public static int processCache(int curPos, int restStep, int aim, int size, int[][] ways) {
        // 如果缓存里已经有这个cur,rest对应的结果了，直接返回
        if (ways[curPos][restStep] != -1) {
            return ways[curPos][restStep];
        }

        // 如果缓存里没有，就先计算，再加入缓存，再返回结果。计算过程和暴力递归一样
        int result;
        if (restStep == 0) {
            result = curPos == aim ? 1 : 0;
        } else if (curPos == 1) {
            result = processCache(2, restStep - 1, aim, size, ways);
        } else if (curPos == size) {
            result = processCache(size - 1, restStep - 1, aim, size, ways);
        } else {
            result = processCache(curPos - 1, restStep - 1, aim, size, ways)
                    + processCache(curPos + 1, restStep - 1, aim, size, ways);
        }
        ways[curPos][restStep] = result;
        return result;
    }

    public static int robotWalkCache(int curPos, int totalStep, int aim, int size) {
        // 参数校验
        if (size < 2 || aim < 1 || aim > size || curPos < 1 || curPos > size || totalStep < 0) {
            return -1;
        }

        // 缓存，ways[cur][rest]=k表示f(cur,rest)=k，也就是robotWalkCache(cur, rest, aim, size)=k
        // f(cur,rest)自变量cur的取值范围[1,size]，自变量rest的取值范围[0, totalStep]
        int[][] ways = new int[size + 1][totalStep + 1];

        // 缓存ways[i][j]=-1表示f(i,j)这个计算没缓存过，这是第一次计算
        for (int i = 0; i < ways.length; i++) {
            for (int j = 0; j < ways[i].length; j++) {
                ways[i][j] = -1;
            }
        }

        return processCache(curPos, totalStep, aim, size, ways);
    }

    /**
     * 方法三
     */

    /**
     * 与其用二维数组缓存记录历史计算过的结果，不如直接填充二维数组，返回curPos,totalStep位置的数值。
     * 怎么填充二维数组：按照递归里的判断条件作为规律。f(cur, rest) = process(cur, rest, AIM, SIZE)
     * 1、当rest=0时，只有cur=AIM时，f(AIM, 0)=1，cur=其他时，f(cur, 0)=0；
     * 2、当cur=1时，f(1, rest) = f(2, rest-1);
     * 3、当cur=size时，f(size, rest) = f(size-1, rest-1);
     * 4、当cur∈(1,size)时，f(cur, rest) = f(cur-1, rest-1) + f(cur+1, rest+1)
     */
    public static int robotWalkCacheSuper(int curPos, int totalStep, int aim, int size) {
        // 参数校验
        if (size < 2 || aim < 1 || aim > size || curPos < 1 || curPos > size || totalStep < 0) {
            return -1;
        }

        // 不需要初始化为-1，因为不需要标注有没有缓存过，二维数组不再是缓存的角色，而是计算的主体
        int[][] ways = new int[size + 1][totalStep + 1];

        // 第1个规律，当rest=0时，只有cur=AIM时，f(AIM, 0)=1
        // 因为Java int数组默认初始化都是0，所以只需要把ways[aim][0]置为1即可
        ways[aim][0] = 1;

        // 按照每一列从小行数到大行数的顺序填充二维数组
        // 不用担心rest-1列没有计算过，因为当rest=1时，rest-1=0，第0列上面已经赋过值了，rest=2时，上一次循环第1列也赋过值了
        for (int rest = 1; rest <= totalStep; rest++) {
            // 第2个规律，当cur=1时，f(1, rest) = f(2, rest-1)
            ways[1][rest] = ways[2][rest - 1];
            // 第三个规律，当cur=size时，f(size, rest) = f(size-1, rest-1)
            ways[size][rest] = ways[size - 1][rest - 1];
            // 第4个规律，当cur∈(1,size)时，f(cur, rest) = f(cur-1, rest-1) + f(cur+1, rest+1)
            for (int cur = 2; cur < size; cur++) {
                ways[cur][rest] = ways[cur - 1][rest - 1] + ways[cur + 1][rest - 1];
            }
        }

        return ways[curPos][totalStep];
    }


    /**
     * 主函数验证
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(robotWalkViolent(1, 1, 2, 4));
        System.out.println(robotWalkCache(1, 1, 2, 4));
        System.out.println(robotWalkCacheSuper(1, 1, 2, 4));

    }
}
