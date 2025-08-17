package tixiban.class33动态规划猜法外部信息简化;

/**
 * 给定一个数组arr,代表一排有分数的气球,每打爆一只气球都会获得分数.
 * 计分规则为,打爆一只气球的得分=当前气球分数*左边最近的没被打爆的气球的分数*右边最近的没被打爆的气球的分数.
 * 如果左边已经没有剩余的气球,就*1,如果右边已经没有剩余的气球,也*1.
 * 求打爆所有气球后能获得的最大分值.
 *
 *
 * 一般思路:根据正常动态规划的思路,可能想到的试法是,在l到r上枚举先打爆哪个,所有可能性中求最大值.
 * 但是可变参数只有l和r的话没法计算,因为不知道当前左边最近的没爆的是哪个,也不知道当前右边最近的没爆的是哪个,如果再传一个表示当前全部气球状态的参数,就有点复杂了.
 * (可以用一个int的位信息表示气球状态?)
 *
 *
 * 正确思路:在l到r上枚举最后打爆哪个气球,同时潜台词必须保证l-1和r+1都没爆.
 * 假设arr=[3,5,2,7,4],那么开头和结尾各补一个1得arr=[1,3,5,2,7,4,1],主函数调f(1,5). 或者不补1,多写几个边界判断也行.
 *
 * 怎么能想到这么试?
 * 动态规划的基本原则,可变参数的复杂程度不要超过整型.
 * 在外部信息比较复杂的情况下,怎么保证可变参数复杂程度不超过整型? 2个思路
 * 1.设置潜台词代替外部信息.例如这道题,设置潜台词保证l-1和r+1都没爆,就不需要额外加气球状态参数了
 * 2.试法上,枚举第一个不行就枚举最后一个
 *
 *
 * 扩展:
 * 如果计分规则改成,当前气球*左边最近的爆了的*右边最近的爆了的, 那么就要枚举l到r上先打爆哪个气球了.
 *
 */
public class Code01打气球 {
    /**
     * 暴力递归
     * 满足潜台词:l-1和r+1一定不越界且一定没爆,才能调这个函数.
     * 换言之,只要执行这个函数,就一定满足l-1和r+1不越界且没爆
     */
    public static int process(int l, int r, int[] arr) {
        if (l == r) {
            // 递归出口,当前范围内只剩一个气球,只有打爆他
            // 由潜台词可知,左边最近的没爆的是l-1,右边最近的没爆的是r+1
            return arr[l] * arr[l - 1] * arr[r + 1];
        }

        int max = 0;

        // 先枚举边界位置
        // l位置最后打爆,也就是先把l右边的全打爆,再把l打爆
        int lScore = process(l + 1, r, arr) + arr[l] * arr[l - 1] * arr[r + 1];
        max = Math.max(max, lScore);
        // r位置最后打爆,也就是先把r左边的全部打爆,再把r打爆
        int rScore = process(l, r - 1, arr) + arr[r] * arr[l - 1] * arr[r + 1];
        max = Math.max(max, rScore);

        // 再枚举一般位置的所有可能性
        for (int i = l + 1; i <= r - 1; i++) {
            // i位置最后打爆,也就是先把i左边的全部打爆(除了最左边的1),再把i右边的全部打爆(除了最右边的1),最后打爆i(左右没爆的就剩左右两端的1)
            int score = process(l, i - 1, arr) + process(i + 1, r, arr) + arr[i] * arr[l - 1] * arr[r + 1];
            max = Math.max(max, score);
        }

        return max;
    }

    public static int violent(int[] arr) {
        int[] newArr = new int[arr.length + 2];
        for (int i = 0; i < arr.length; i++) {
            newArr[i + 1] = arr[i];
        }
        newArr[0] = 1;
        newArr[newArr.length - 1] = 1;

        return process(1, newArr.length - 2, newArr);
    }

    public static void main(String[] args) {
        int[] arr = {3, 2, 4};
        System.out.println(violent(arr));
    }

    /**
     * 改傻缓存法、改动态规划:略
     * 经验证,这道题不能用四边形不等式优化.
     */

}
