package tixiban.class18manacher;

/**
 * manacher算法用于求字符串的最长回文子串，时间复杂度O(N)。
 * <p>
 * 先看下求最长回文子串的暴力解法:遍历每一个位置,往左右两边扩,直到左右两边不相等.时间复杂度O(N2).
 * <p>
 * manacher算法前置的几个概念:
 * 1、回文半径和回文直径:abcba回文半径=3,回文直径=5
 * 2、回文半径数组:把每个位置可扩展到的最大回文半径记录到一个数组
 * 3、最大回文右边界R:目前为止半径最大的回文串的下一个位置,也就是目前为止往右最远的没扩到的位置
 * 4、最大回文中心C:目前为止半径最大的回文的的中心位置.最大中心和最大右边界一定是同步更新的
 * 5、最大回文左边界L:R关于C的对称点
 * <p>
 * <p>
 * manacher算法流程
 * 遍历到一个位置i,会分2种情况:
 * 1、i在最大回文右边界R外(i>=R,R是到不了的那个位置),这时没有优化空间,按照暴力解往左右两边扩,记录最大回文半径数组并更新R的位置
 * 2、i在R内,那么i对于最大回文中心C必然在左边有一个对称点i',并且i'的最大回文半径已经求过了,针对i'的最大回文半径又分3种情况:
 * a.以i'为中心的回文整个在(L+1, R-1)范围内,那么根据对称,i的回文半径=i'的回文半径
 * b.以i‘为中心的回文的左边界在L之外,也就是<=L,那么i的最大回文半径就是R-i+1,也就是i‘在L内的那部分长度
 * 为什么?首先根据对称,i‘在L之内的部分是回文,那么i在R之内的部分也是回文.那i能不能扩到R以外呢?不能,因为能扩的话C早扩了
 * c.i‘的回文左边界正好==L+1,也就是正好和目前最大回文的左边界重合,这时候i的回文半径在R的位置上继续往右扩,按照暴力解
 * 因为根据i’的对称,i在R内一定是回文的,但是在R外还能不能扩不确定,所以在R的位置上尝试继续扩
 * <p>
 * <p>
 * manacher时间复杂度分析
 * R从0开始,最大到N,i从0开始,最大到N,按照上面几种情况分析变量i+R:
 * 1、i增,R增,i+R增
 * 2、a、i增,R不变,i+R增
 * 2、b、i增,R不变,i+R增
 * 2、c、i增,R增,i+R增
 * 可以看到i+R不管哪个分支都单调增,最小0,最大2*N,最多增2N次,所以复杂度O(N)
 */
public class Code01Manacher {
    public static int getMaxPalindromeLength(String string) {
        // 把原始字符串转成manacher字符串,abcba->#a#b#c#b#a#,这样不管原始字符串是奇数还是偶数,都可以按照同样的方式求解
        char[] str = getManacherStr(string);
        // 每个位置为中心的最大回文半径
        int[] radius = new int[str.length];
        // 目前为止的最大回文的右边界(到不了的位置)
        int R = 0;
        // 目前为止最大回文的中心位置
        int C = 0;
        // 记录结果
        int max = 0;
        // 遍历每个位置,求最大回文半径
        for (int i = 0; i < str.length; i++) {
            // 当前位置的最大回文半径赋初始值,有的情况这个初始值就是最终结果,有的情况要基于这个初始值继续扩
            // 当i在R外时,从i自己开始扩,i的半径初始值=1
            // 当i在R内时,返回radius[2 * C - i]和R - i中较小的一个即可,因为:
            // 如果i‘左边界在L+1内,i的半径就直接=i‘的半径也就是radius[2 * C - i]
            // 如果i’左边界在L+1外,i的半径就直接=R-i
            // 如果i’左边界在L+1上,i从R位置开始往右扩,i的半径初始值=R - i
            radius[i] = R > i ? Math.min(radius[2 * C - i], R - i) : 1;

            // 不需要扩的,第一轮就不会命中if,直接break,需要扩的会循环扩
            while (i + radius[i] < str.length && i - radius[i] >= 0) {
                if (str[i + radius[i]] == str[i - radius[i]]) {
                    radius[i]++;
                } else {
                    break;
                }
            }

            // 如果当前i的右边界扩到了比R更远的位置,那么更新R=那个右边界,C=当前i
            if (i + radius[i] > R) {
                R = i + radius[i];
                C = i;
            }

            max = Math.max(max, radius[i]);
        }
        // max是manacher字符串的最大回文「半」径,-1恰好等于原始字符串的最大回文「直」径
        return max - 1;
    }

    // abcba->#a#b#c#b#a#
    public static char[] getManacherStr(String string) {
        char[] str = string.toCharArray();
        char[] manacherStr = new char[string.length() * 2 + 1];
        manacherStr[0] = '#';
        int index = 1;
        for (int i = 0; i < str.length; i++) {
            manacherStr[index++] = str[i];
            manacherStr[index++] = '#';
        }
        return manacherStr;
    }

    public static void main(String[] args) {
        String str = "qabceddwcbaq";
        System.out.println(getMaxPalindromeLength(str));
    }
}
