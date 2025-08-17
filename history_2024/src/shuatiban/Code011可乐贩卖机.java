package shuatiban;

/**
 * 携程笔试题
 *
 * 贩卖机只支持10 ，50，100三种面额,一次购买只能出一瓶可乐，且投钱和找零都遵循优先使用大钱的原则.
 * 需要购买的可乐数量是m，其中手头拥有的10、50、100的数量分别为a、b、c，可乐的价格是x(x是10的倍数)
 * 请计算出需要投入硬币次数
 */
public class Code011可乐贩卖机 {
    public static int getPuts(int a, int b, int c, int x, int m) {
        // 面额
        int[] yuan = {100, 50, 10};
        // 张数
        int[] zhang = {a, b, c};

        // 开始用i号面额的时候,前面的面额总共剩下几张
        int restZhang = 0;
        // 开始用i号面额的时候,前面的面额总共剩下多少钱
        int restMoney = 0;

        // 投币数
        int puts = 0;

        for (int i = 0; i < yuan.length && m > 0; i++) {
            // 当前面额购买第一瓶需要几张
            // 技巧:a除以b向上取整=(a+b-1)/b
            int curYuanFirstBuyZhang = (x - restMoney + yuan[i] - 1) / yuan[i];

            // 如果当前面额全部用上都不够买一瓶的,那直接把当前面额算入历史,启用下一面额
            if (curYuanFirstBuyZhang > zhang[i]) {
                restZhang += zhang[i];
                restMoney += zhang[i] * yuan[i];
                continue;
            }

            // 用当前面额买第一瓶
            // 买第一瓶,贩卖机应该找多少钱
            int buyOneRest = restMoney + yuan[i] * curYuanFirstBuyZhang - x;
            // 把应该找的钱换算成相应的面额
            getRest(yuan, zhang, i + 1, x, buyOneRest, 1);
            // 更新数据
            zhang[i] -= curYuanFirstBuyZhang;
            puts += restZhang + curYuanFirstBuyZhang;
            m--;

            // 用当前面额能买几瓶买几瓶
            // 当前面额能买几瓶
            int curYuanBuys = Math.min(m, yuan[i] * zhang[i] / x);
            // 每买一瓶需要几张
            int curYuanBuyOneZhang = (x + yuan[i] - 1) / yuan[i];
            // 每买一瓶,贩卖机应该找多少钱
            buyOneRest = yuan[i] * curYuanBuyOneZhang - x;
            // 把应该找的钱换算成相应的面额
            getRest(yuan, zhang, i + 1, x, buyOneRest, curYuanBuys);
            // 更新数据
            zhang[i] -= curYuanBuyOneZhang * curYuanBuys;
            puts += curYuanBuyOneZhang * curYuanBuys;
            m -= curYuanBuys;

            // 当前面额剩下的部分不够买一瓶,作为历史,启用下一面额
            restZhang = zhang[i];
            restMoney = zhang[i] * yuan[i];
        }
        return puts;
    }

    /**
     * 把应该找的钱变成相应的面额的张数++
     * @param yuan 面额数组
     * @param zhang 张数数组
     * @param i 找零可用面额从i开始
     * @param x 单价
     * @param buyOneRest 买一次要找的钱
     * @param buys 买了几次
     */
    public static void getRest(int[] yuan, int[] zhang, int i, int x, int buyOneRest, int buys) {
        for (; i < yuan.length; i++) {
            zhang[i] += (buyOneRest / yuan[i]) * buys;
            buyOneRest %= yuan[i];
        }
    }

    public static void main(String[] args) {
        int a = 4;
        int b = 4;
        int c = 30;
        int x = 250;
        int m = 3;
        System.out.println(getPuts(a, b, c, x, m));
    }
}

/**
 * 扩展:
 * 如何求100万的阶乘有几个0?
 *
 * 思路:
 * 100万的阶乘=1*2*3*4...*100万
 * 乘积中的0一定由2因子*5因子得到,由于5因子比2因子少,所以只需要算出1到100万之间有多少个5因子,就等于有几个0.
 */