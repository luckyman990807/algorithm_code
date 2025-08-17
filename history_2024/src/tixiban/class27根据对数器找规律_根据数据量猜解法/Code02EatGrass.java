package tixiban.class27根据对数器找规律_根据数据量猜解法;

/**
 * 给定一个整数N,表示有N份草料,有一只牛和一只羊轮流吃草,牛先吃,羊后吃,不管是牛还是羊,每次吃草的份数必须是4的某次方,例如1,4,16...
 * 谁吃完最后一口,谁获胜.
 * 假设牛和羊都绝顶聪明,都想赢,都会做出最理性的决定.
 * 根据唯一的参数N,返回谁会赢.
 */
public class Code02EatGrass {
    /**
     * 暴力法
     */
    public static String violent(int n) {
        // base case的范围随便写,可以把5以内的作为baseCase,也可以把2以内的作为baseCase,区别只是递归的深度不同,一个依据前5个数计算,另一个依据前2个数计算.
        if (n < 5) {
            if (n == 0 || n == 2) {
                return "后手";
            }
            return "先手";
        }
        // 从1开始试,如果减去eat后是后手赢了,那么就是当前先手赢了(下一局的后手就是当前的先手)
        int eat = 1;
        while (eat <= n) {
            if (violent(n - eat).equals("后手")) {
                return "先手";
            }
            // eat每次*4,同时避免溢出(如果n差点就溢出了,而eat又差点赶上n,那么eat*4就溢出了)
            if (eat <= n / 4) {
                eat *= 4;
            } else {
                break;
            }
        }
        return "后手";
    }

    public static void main(String[] args) {
//        for (int i = 0; i < 50; i++) {
//            System.out.println(i + " " + violent(i));
//        }
        /**
         * 通过分析前100个结果,发现每五个一组,每组固定为 后先后先先
         */
        for (int i = 0; i < 50; i++) {
            System.out.println(i + " " + violent(i) + " " + eatGrass(i));
        }
    }

    /**
     * 分析规律后直接硬编码
     */
    public static String eatGrass(int n) {
        if (n % 5 == 0 || n % 5 == 2) {
            return "后手";
        }
        return "先手";
    }

}
