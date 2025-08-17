package tixiban.class33动态规划猜法外部信息简化;

/**
 * 给定一个整数数组arr,消掉数字可以获得分数,规则是每次只能消掉连在一起的相同数字,个数可以1个也可以多个,得分等于本次消掉的个数的平方.
 * 求所有数字都消完后的最大得分.
 *
 * 思路:
 * 设置潜台词:调用递归函数f(l,r,k)消掉从l到r的数字时,保证在l左边有k个和arr[l]相等的数字.k可以是0
 */
public class Code02消数字 {
    /**
     * 暴力递归,从l到r消掉
     * 潜台词:调这个函数则一定保证l前面有k个跟l相等的数字
     */
    public static int process(int l, int r, int k, int[] arr) {
        // 递归出口
        if (r < l) {
            return 0;
        }

        // 合并前缀,这一步是把 [1,1,1,2,3]前面有5个1, 变成[1,2,3]前面有7个1, 得到新的l:newL和新的k:pre
        int newL = l;
        while (newL < arr.length - 1 && arr[newL + 1] == arr[l]) {
            newL++;
        }
        int pre = k + newL - l;

        // 可能性1:新l和前面pre个消掉,剩下的继续消
        int result = (pre + 1) * (pre + 1) + process(newL + 1, r, 0, arr);

        // 其他可能性:新l和前面pre合为整体,找到后面等于新l的连成一片的第一个,作为下一个l,把新l和下一个l中间消掉,这样下一个l前面就有pre+1个相等的,递归
        for (int nextL = newL + 1; nextL <= r; nextL++) {
            // 找到等于新l的连成一片的第一个,即当前位置相等而前一个位置不相等
            if (arr[nextL - 1] != arr[newL] && arr[nextL] == arr[newL]) {
                result = Math.max(
                        result,
                        process(newL + 1, nextL - 1, 0, arr) + process(nextL, r, pre + 1, arr));
            }
        }

        return result;
    }

    public static int violent(int[] arr) {
        return process(0, arr.length - 1, 0, arr);
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 1, 1, 2, 2, 1};
        System.out.println(violent(arr));
    }
}
