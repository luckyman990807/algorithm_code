package shuatiban;

/**
 * 给定一个数组arr，长度为N，arr中的值不是0就是1。arr[i]表示第i栈灯的状态，0代表灭灯，1代表亮灯
 * 每一栈灯都有开关，但是按下i号灯的开关，会同时改变i-1、i、i+1栈灯的状态
 *
 * 问题一(无环)：如果N栈灯排成一条直线,请问最少按下多少次开关？
 * i为中间位置时，i号灯的开关能影响i-1、i和i+1
 * 0号灯的开关只能影响0和1位置的灯
 * N-1号灯的开关只能影响N-2和N-1位置的灯
 *
 * 问题二(有环)：如果N栈灯排成一个圈,请问最少按下多少次开关,能让灯都亮起来
 * i为中间位置时，i号灯的开关能影响i-1、i和i+1
 * 0号灯的开关能影响N-1、0和1位置的灯
 * N-1号灯的开关能影响N-2、N-1和0位置的灯
 */
public class Code049点灯有环无环 {
    /**
     * 无环递归试法
     * @param arr 原始数组
     * @param i 当前要决定哪个位置的按钮
     * @param cur 当前位置的状态
     * @param pre 上个位置的状态
     * @return i及其后面的都置为1, 最少需要按几次按钮
     *
     * 为什么当前位置和上个位置的状态需要传过来,自己去arr里查不行吗?
     * 不行,因为上层递归调用的时候可能会修改上层的cur和next,也就是本层的pre和cur,本层不知道上层修没修改,所以arr里的值已经不准了,必须上层传过来
     */
    public static int buttonProcess(int[] arr, int i, int cur, int pre) {
        // 递归出口,如果尝试到最后一个位置了,那么:
        // 如果最后一个位置跟前一个位置不相等,就永远无法调成全1;如果相等,是0则需要调1下,是1需要调0下,取反
        if (i == arr.length - 1) {
            return cur == pre ? cur ^ 1 : Integer.MAX_VALUE;
        }

        // 一般位置
        if (pre == 0) {
            // 如果前一个位置是0,那么当前位置必须按按钮,目的是把上个位置调成1,因为如果现在不调,以后再也没机会调了
            int next = buttonProcess(arr, i + 1, arr[i + 1] ^ 1, cur ^ 1);
            // 按下当前按钮1次+后面调成全1所需的次数,如果后面调成全1需要max次,那就保持max次,表示无论如何也调不成全1
            return next != Integer.MAX_VALUE ? 1 + next : Integer.MAX_VALUE;
        } else {
            // 如果前一个位置是1,那么当前位置必须不按按钮,因为一旦按了就把前一个位置调成0了,且以后再也没机会调回1
            return buttonProcess(arr, i + 1, arr[i + 1], cur);
        }
    }

    /**
     * 递归法求arr调成全1的最少按按钮次数
     */
    public static int button(int[] arr) {
        // 前置判断
        // 如果数组没有数据,需要调0次
        if (arr == null || arr.length == 0) {
            return 0;
        }
        // 如果数组只有一个数,那么=1就需要调0次,=0就需要调1次
        if (arr.length == 1) {
            return arr[0] ^ 1;
        }

        // 数组有2个以上的数,因为按下0位置按钮比较特殊,只会改变0位置和1位置,所以把0位置单独拿出来讨论.中间的位置都是按下i按钮会改变i-1、i、i+1
        // 可能性1:不按0位置,那么0位置和1位置保持原数组中的样子,从1位置开始递归
        int p1 = buttonProcess(arr, 1, arr[1], arr[0]);
        // 可能性2:按0位置,那么0位置和1位置取反,从1位置开始递归,累加0位置按的1次
        int p2 = buttonProcess(arr, 1, arr[1] ^ 1, arr[0] ^ 1);
        p2 = p2 == Integer.MAX_VALUE ? p2 : p2 + 1;

        return Math.min(p1, p2);
    }


    /**
     * 有环递归试法
     * @param arr 原始数组
     * @param i 当前要决定哪个位置的按钮
     * @param cur 当前位置的状态
     * @param pre 前一个位置的状态
     * @param end 最后一个位置的状态
     * @param start 第一个位置的状态
     * @return i及其后面全调成1, 最少需要按几次按钮
     *
     * 为什么要传最后一个和第一个位置的状态?
     * 因为0位置可能改变最后一个位置和第一个位置,1位置也可能改变第一个位置,所以原始数组里的值已经不准了,而递归出口要用到最后一个位置和第一个位置,所以要一直传下来
     */
    public static int buttonLoopProcess(int[] arr, int i, int cur, int pre, int end, int start) {
        // 递归出口,如果尝试到最后一个位置,那么如果倒数第二个、最后一个、第一个(环的缘故)位置都相等,才能调成全1,否则永远无法调成全1
        if (i == arr.length - 1) {
            return pre == end && end == start ? end ^ 1 : Integer.MAX_VALUE;
        }

        // 一般位置
        if (i == arr.length - 2) {
            // 一般位置里的特殊位置,倒数第二个,因为这个位置可能改变最后一个位置,也就是一直传下来的end值
            if (pre == 0) {
                // 如果前一个位置是0,那么当前位置必须按按钮,会让前一个位置、当前位置、下一个位置都取反,下一个位置就是最后一个位置.累加本次按按钮1次
                int next = buttonLoopProcess(arr, i + 1, end ^ 1, cur ^ 1, end ^ 1, start);
                return next != Integer.MAX_VALUE ? 1 + next : Integer.MAX_VALUE;
            } else {
                // 如果前一个位置是1,那么当前按钮必须不按按钮,大家都没改变
                return buttonLoopProcess(arr, i + 1, end, cur, end, start);
            }
        } else {
            // 一般位置里的一般位置,只改前一个、当前、后一个,不改最后一个
            if (pre == 0) {
                int next = buttonLoopProcess(arr, i + 1, arr[i + 1] ^ 1, cur ^ 1, end, start);
                return next != Integer.MAX_VALUE ? 1 + next : Integer.MAX_VALUE;
            } else {
                return buttonLoopProcess(arr, i + 1, arr[i + 1], cur, end, start);
            }
        }
    }

    /**
     * 递归法求arr调成全1的最少按按钮次数
     * @param arr
     * @return
     */
    public static int buttonLoop(int[] arr) {
        // 前置判断
        // 如果没有数,需要按0次
        if (arr == null || arr.length == 0) {
            return 0;
        }
        // 如果只有一个数,=0就要按1此次,=1就要按0次
        if (arr.length == 1) {
            return arr[0] ^ 1;
        }
        // 如果只有2个数或者只有3个数,那么必须所有数都一样才能调成全1,否则永远调不成全1.因为2个数或3个数有环的情况下按任意一个,所有位置都跟着取反.
        if (arr.length == 2) {
            return arr[0] == arr[1] ? arr[0] ^ 1 : Integer.MAX_VALUE;
        }
        if (arr.length == 3) {
            return arr[0] == arr[1] && arr[1] == arr[2] ? arr[0] ^ 1 : Integer.MAX_VALUE;
        }

        // 数组有3个以上的数,要把0位置和1位置分别单独拿出来讨论,因为0位置很特殊:影响的是0位置、1位置、最后一个位置,可能改变传下去的end值;同时1位置也很特殊,能影响0位置,可能改变传下去的start值
        // 可能性1:0不改,1不改
        int p1 = buttonLoopProcess(arr, 2, arr[2], arr[1], arr[arr.length - 1], arr[0]);
        // 可能性2:0不改,1改
        int p2 = buttonLoopProcess(arr, 2, arr[2] ^ 1, arr[1] ^ 1, arr[arr.length - 1], arr[0] ^ 1);
        p2 = p2 == Integer.MAX_VALUE ? p2 : p2 + 1;
        // 可能性3:0改,1不改
        int p3 = buttonLoopProcess(arr, 2, arr[2], arr[1] ^ 1, arr[arr.length - 1] ^ 1, arr[0] ^ 1);
        p3 = p3 == Integer.MAX_VALUE ? p3 : p3 + 1;
        // 可能性4:0改,1改(注意要累加2次按按钮次数)
        int p4 = buttonLoopProcess(arr, 2, arr[2] ^ 1, arr[1], arr[arr.length - 1] ^ 1, arr[0]);
        p4 = p4 == Integer.MAX_VALUE ? p4 : p4 + 2;

        return Math.min(Math.min(p1, p2), Math.min(p3, p4));
    }

    public static void main(String[] args) {
        int[] arr = {1, 0, 1, 1, 1};
        System.out.println(button(arr));
    }
}
