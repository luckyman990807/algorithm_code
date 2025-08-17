package shuatiban;

/**
 * 一个数组中只有两种字符'G'和'B'，可以让所有的G都放在左侧，所有的B都放在右侧
 * 或者可以让所有的G都放在右侧，所有的B都放在左侧，但是只能在相邻字符之间进行交换操作，返回至少需要交换几次
 *
 * 思路:
 * 如果让G在左边B在右边,那么数组中从左往右出现的第0个G,一定只需要移动到arr[0],第1个G只需要移动到arr[1]...第i个G只需要放到arr[i],不需要移动到更往左的位置.
 * 所以对于每一个G,他要移动到的位置是确定的,他要移动的次数也是可以直接算出来的,不需要真实地移动.
 * 只要G都移到左边了,B自然就到右边了.
 * 让G在右边B在左边也一样.
 */
public class Code004WrapG_B {
    public static int wrapSteps(String s) {
        char[] str = s.toCharArray();

        int gLeftIndex = 0;
        int gLeftSteps = 0;
        for (int i = 0; i < str.length; i++) {
            if (str[i] == 'G') {
                gLeftSteps += i - (gLeftIndex++);
            }
        }

        int bLeftIndex = 0;
        int bLeftSteps = 0;
        for (int i = 0; i < str.length; i++) {
            if (str[i] == 'B') {
                bLeftSteps += i - (bLeftIndex++);
            }
        }

        return Math.min(gLeftSteps, bLeftSteps);
    }

}
