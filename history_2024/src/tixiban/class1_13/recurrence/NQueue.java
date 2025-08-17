package tixiban.class1_13.recurrence;

/**
 * N皇后问题（无法改成动态规划）
 * 在N*N的棋盘上摆N个皇后，要求任意两个皇后不同行、不同列，也不再同一条斜线上。
 * 给定一个整数n，返回n皇后的摆法有多少种
 * n=1，返回1
 * n=2或n=3，返回0，因为无论怎么摆都不行
 * n=8，返回92
 */
public class NQueue {
    /**
     * index之前的皇后不管，index之前的皇后的摆放位置记录在record中，一共有n个皇后，返回从index往后的皇后一共有几种摆法
     *
     * @param index  当前是第几个皇后
     * @param record record[x]=y，表示第x号皇后放在x行y列。因为一行只能放一个皇后，所以第x个皇后就放在第x行，不用另外的变量记录行号
     * @param n      一共要求n皇后问题
     * @return
     */
    public static int process(int index, int[] record, int n) {
        // 如果所有皇后都遍历完了，说明找到了一种摆法，返回1
        if (index == n) {
            return 1;
        }

        // 求第index号皇后放在index行第几列
        int ways = 0;
        for (int col = 0; col < n; col++) {
            if (valid(index, col, record)) {
                // 如果允许放在col列，就把index号皇后放在index行col列记录到record表中，继续摆放下一个皇后的位置
                record[index] = col;
                ways += process(index + 1, record, n);
            }
        }
        return ways;
    }

    /**
     * 判断第index号皇后（或者说第index行的皇后）能不能放在第col列，index之前的皇后的摆放位置记录在record中
     */
    public static boolean valid(int index, int col, int[] record) {
        // 遍历index之前的皇后，也就是index之前的行
        for (int row = 0; row < index; row++) {
            // 如果共列或者共斜线，不允许摆放
            if (record[row] == col || Math.abs(index - row) == Math.abs(col - record[row])) {
                return false;
            }
        }
        return true;
    }

    public static int nQueue(int n) {
        int[] record = new int[n];
        return process(0, record, n);
    }


    /**
     * 位运算优化版本，时间复杂度不变，但大幅优化常数时间
     * @param nLimit n皇后的最大范围，4皇后就是最右边4个1，左边全是0
     * @param colUsed 当前行跟前面皇后同列的位置，不可用
     * @param leftBiaUsed 当前行跟前面皇后同左斜线的位置，不可用
     * @param rightBiaUsed 当前行跟前面皇后同右斜线的位置，不可用
     * @return
     */
    public static int processSuper(int nLimit, int colUsed, int leftBiaUsed, int rightBiaUsed) {
        // 如果整个n的范围全都不可用，说明所有皇后都摆完了，返回返回方法数1
        if (nLimit == colUsed) {
            return 1;
        }
        // 3处位置或到一起就是当前行被前面的皇后占据的所有位置，取反，和n范围相与，结果就是当前行所有可以摆皇后的位置
        int validPosition = nLimit & (~(colUsed | leftBiaUsed | rightBiaUsed));
        // 为什么用异或不行？
        // 答：因为放皇后的位置有可能超过nLimit的范围（例如计算左斜线已占用位置），需要用nLimit来砍掉
//        int validPosition = nLimit ^ (colUsed | leftBiaUsed | rightBiaUsed);

        int ways=0;
        // 遍历所有可以摆皇后的位置
        while (validPosition != 0) {
            // 怎么遍历？每次提取出最右侧的1，然后减掉，直到validPosition==0
            int mostRight = validPosition & (~validPosition + 1);
            validPosition -= mostRight;

            // validPosition -= mostRight表示最右边的1位置当前摆了一个皇后，递归下一行的皇后
            // 列占用：或上当前列。左斜线占用：或上当前位置左移一位。右斜线占用：或上当前位置右移一位
            ways+=processSuper(nLimit, colUsed|mostRight, (leftBiaUsed|mostRight)<<1, (rightBiaUsed|mostRight)>>>1);
        }
        return ways;
    }

    public static int nQueueSuper(int n){
        // (1<<n)-1的结果就是最右边n个1，左边全是0
        return processSuper((1<<n)-1, 0, 0, 0);
    }

    public static void main(String[] args) {
        System.out.println(nQueue(8));
        System.out.println(nQueueSuper(8));
    }
}
