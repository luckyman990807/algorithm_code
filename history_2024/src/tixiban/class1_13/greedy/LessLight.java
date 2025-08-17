package tixiban.class1_13.greedy;

/**
 * 一个字符串只由.和x组成，.代表街道，需要照明，x代表墙，不需要照明。i位置放一盏灯可以照亮i-1、i、i+1位置。
 * 给定一个字符串，计算最少需要放几盏灯
 */
public class LessLight {
    /**
     * 贪心算法：如果i-1、i、i+1位置都是街道，那么把灯放在i位置。
     * 考虑所有情况：
     * 1、如果i位置是x，直接i+1
     * 2、如果i位置是.，但是i+1位置是x，那么在i位置放灯，i+2（因为后面的灯不可能照到我了。为什么不考虑前面有没有能照到我的？因为后面i放灯的时候，至少会跳到i+2去，可以理解为只要到了i，就是前面不会有灯照到了）
     * 3、如果i位置是.，i+1位置也是.，但是i+2位置是x，那么i或i+1位置放灯，i+3
     * 4、如果i位置是.，i+1位置也是.，i+2位置也是.，那么i+1位置放灯，i+3
     */
    public static int getLessLight(String str) {
        char[] chars = str.toCharArray();
        int i = 0;
        int lights = 0;
        while (i < chars.length) {
            if (chars[i] == 'x') {
                i = i + 1;
            } else {
                lights++;
                if (i + 1 >= chars.length) {
                    break;
                }
                if (chars[i + 1] == 'x') {
                    i = i + 2;
                } else {
                    // 情况3和情况4一样，都是灯+1，i+3
                    i = i + 3;
                }
            }
        }
        return lights;
    }
}
