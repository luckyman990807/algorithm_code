package shuatiban;

/**
 * // 谷歌面试题
 * 	// 面值为1~10的牌组成一组，
 * 	// 每次你从组里等概率的抽出1~10中的一张
 * 	// 下次抽会换一个新的组，有无限组
 * 	// 当累加和<17时，你将一直抽牌
 * 	// 当累加和>=17且<21时，你将获胜
 * 	// 当累加和>=21时，你将失败
 * 	// 返回获胜的概率
 */
public class Code065抽纸牌求获胜的概率 {
    /**
     * 太简单都没必要改动态规划，直接递归即可
     * @param cur
     * @return
     */
    public static double win(int cur) {
        // 递归出口，如果当前累加和到达赢区间，返回赢的概率=1
        if (cur >= 17 && cur < 21) {
            return 1D;
        }
        // 递归出口，如果当前累加和到达输区间，返回赢的概率=0
        if (cur >= 21) {
            return 0D;
        }
        // 一般情况，1～10的牌抽到每张牌的概率是1/10，抽到每张牌后的累加和的赢的概率用递归计算
        double result = 0;
        for (int i = 1; i <= 10; i++) {
            result += win(cur + i);
        }
        // 抽到每张牌的概率是1/10，在最后整体乘1/10，就不用上面枚举递归时都乘了
        return result / 10;
    }

    public static void main(String[] args) {
        System.out.println(win(0));
    }

    /**
     * 进阶版
     * 每次都从1～N的牌中等概率抽一张，当累加和< a就一直抽，当累加和>=a且< b就获胜，当累加和>=b就失败。
     * 求获胜的概率
     */
    public static double winPlus(int n, int a, int b) {
        if (a >= b) {
            return 0D;
        }
        if (n <= a - b) {
            return 1D;
        }
        return process(0, n, a, b);
    }

    public static double process(int cur, int n, int a, int b) {
        if (cur >= a && cur < b) {
            return 1D;
        }
        if (cur >= b) {
            return 0D;
        }
        double result = 0;
        for (int i = 1; i <= n; i++) {
            result += process(cur + i, n, a, b);
        }
        return result / n;
    }

    /**
     * 进阶版的斜率优化（观察位置优化枚举）
     * 例如n=4，a=7，b=10，
     * 当前累加和来到2，枚举所有可能性的f(2)=[f(3)+f(4)+f(5)+f(6)]/n
     * 当前累加和来到3，枚举所有可能性得f(3)=[f(4)+f(5)+f(6)+f(7)]/n
     * 把2式带入1式得f(2) = {f(3)+[f(3)*n-f(7)]}/n = [f(3)*(n+1)-f(7)]/n
     * 也就是f(i)=[f(i+1)*(n+1)-f(i+1+n)]/n
     * 这就把枚举行为优化成了两个位置的依赖。
     *
     * 但是有一些特殊情况：
     * 1、累加和来到a-1时，枚举的n种可能性中，有b-a个=1，其他的=0，所以赢的概率可以简化成(b-a)/n
     * 2、累加和小于a-1，且大于b-n-1这部分，枚举的n种可能性中，由于累加和超过b赢的概率=0，所以可以省去不算，也就是枚举可能性都只枚举到f(b-1)即可，每种情况可能性的结尾都一样，计算的时候就不需要减去f(i+1+n)了，就可以简化成
     *    f(i)=f(i+1)*(n+1)/n
     */
    public static double processSuper(int cur, int n, int a, int b) {
        if (cur >= a && cur < b) {
            return 1D;
        }
        if (cur >= b) {
            return 0D;
        }
        // 如果当前累加和恰好来到a-1，那么抽牌的n种可能性中，有b-a种能赢，剩下的都会超过b会输
        if (cur == a - 1) {
            return (double) (b - a) / n;
        }
        // 如果当前累加和小于a-1但大于b-n-1，
        if (cur >= b - n - 1) {
            return processSuper(cur + 1, n, a, b) * (n + 1) / n;
        }
        return (processSuper(cur + 1, n, a, b) * (n + 1) - processSuper(cur + 1 + n, n, a, b)) / n;
    }

}
