package tixiban.class27根据对数器找规律_根据数据量猜解法;

/**
 * 小虎买苹果,商店提供两种塑料袋,每种数量无限,
 * 1.能装6个苹果的袋子
 * 2.能装8个苹果的袋子
 * 小虎可以自由使用两种袋子装苹果,但是小虎有强迫症,他要求使用的袋子必须最少,且每个袋子必须装满.
 * 给定要买的苹果数量N,返回至少使用多少个袋子.如果N无法让所有袋子装满,返回-1.
 */
public class Code01BuyApple {

    /**
     * 暴力法
     * 思路:
     * 既然要求袋子数量最少,那么就让8个的袋子尽量多.
     * 先看看全用8个的袋子能不能装满,装满就出来答案了,装不满就减少一个x8袋子,加入几个x6袋子看能不能装满,装满就出来答案了,装不满就再减少一个x8袋子...
     * 直到x8袋子减少到0个,全用x6袋子,如果还装不满,就返回-1.
     */
    public static int violent(int apples) {
        if (apples < 0) {
            return -1;
        }
        int bag8 = apples >> 3;
        int rest = apples - (bag8 << 3);
        while (bag8 >= 0) {
            if (rest % 6 == 0) {
                return bag8 + rest / 6;
            }
            bag8--;
            rest += 8;
        }
        return -1;
    }

    public static void main(String[] args) {
//        for (int i = 1; i <= 100; i++) {
//            System.out.println(i + " " + violent(i));
//        }
        /**
         * 通过分析前100的结果,发现:
         * 0~17的结果看不出明显规律,从18开始,每8个分成一组,每组为3和-1交替、4和-1交替、5和-1交替...
         * 因此,i<=17时,硬编码返回结果,i>=18时,偶数返回(i-18)/8+3,奇数返回-1
         */
        for (int i = 0; i < 100; i++) {
            System.out.println(i + " " + violent(i) + " " + minBags(i));
        }
    }

    /**
     * 分析规律后直接硬编码
     */
    public static int minBags(int apples) {
        // <18
        if (apples < 18) {
            if (apples == 6 || apples == 8) {
                return 1;
            }
            if (apples == 12 || apples == 14 || apples == 16) {
                return 2;
            }
            return -1;
        }
        // >18的偶数
        if (apples % 2 == 0) {
            return (apples - 18) / 8 + 3;
        }
        // >18的奇数
        return -1;
    }
}
