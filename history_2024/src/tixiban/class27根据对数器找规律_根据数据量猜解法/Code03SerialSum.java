package tixiban.class27根据对数器找规律_根据数据量猜解法;

/**
 * 定义一种数:可以表示成若干(数量>1)连续正数的和,例如
 * 5=2+3就是这样的数,
 * 12=3+4+5就是这样的数,
 * 1不是这样的数,因为连续的数量要大于1个,
 * 2=1+1不是这样的书,因为等号右边不是连续的数.
 * 给定一个正数N,返回是不是这样的数.
 */
public class Code03SerialSum {
    /**
     * 暴力法,思路:
     * 从1开始加,1+2+3+...,如果加到某一次,加之前小于n,加之后大于n,说明从1开始得不到n,换从2开始.
     * 如果加到某一次,恰好等于n,说明从1开始能得到n,直接返回true
     */
    public static boolean violent(int n) {
        for (int j = 1; j < n; j++) {
            int sum = 0;
            for (int i = j; i < n; i++) {
                sum += i;
                if (sum == n) {
                    return true;
                }
                if (sum > n) {
                    break;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
//        for (int i = 1; i < 100; i++) {
//            System.out.println(i + " " + violent(i));
//        }
        /**
         * 通过分析前100个结果,发现只有2的某次方才是false
         */
        for (int i = 1; i < 100; i++) {
            System.out.println(i + " " + violent(i) + " " + serialSum(i));
        }
    }

    /**
     * 分析规律后直接硬编码
     * 问:如果判断一个数是不是2的某次方?
     * 答:只需要判断这个数是不是只有1个1
     * 1.提取出这个数最右边的1,等于这个数,说明只有1个1(00001000提取出最右边的1还等于00001000,01001000提取出最右边的1是00001000,不等于01001000)
     *   问:如何提取出一个数最右边的1?
     *   1.1. n与上(n取反+1). 1.2. n与上(负n)
     * 2.只有1个1的话,这个数-1会把唯一的1拆散,所以自己与上(自己-1)如果等于0,说明只有1个1(00001000 - 1 = 00000111, 与上00001000 = 0)
     */
    public static boolean serialSum(int n) {
        return n != (n & ((~n) + 1));
    }
}
