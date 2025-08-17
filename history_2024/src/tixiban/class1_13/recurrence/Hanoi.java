package tixiban.class1_13.recurrence;

/**
 * 汉诺塔问题
 * 复杂度O(2^n - 1)因为要求就是把每一步的操作打印，一共就有2^n - 1步，所以没法优化了
 */
public class Hanoi {
    public static void main(String[] args) {
        hanoi(3);
    }

    public static void hanoi(int size) {
        if (size <= 0) {
            return;
        }
        process(size, "left", "right", "middle");
    }

    /**
     * 汉诺塔递归函数
     * @param size 要移动几个塔片
     * @param from 从哪个位置
     * @param to 移动到哪个位置
     * @param other 剩下一个位置是哪个
     */
    public static void process(int size, String from, String to, String other) {
        // 如果要移动的塔片数量为1，那么直接从from移动到to即可
        if (size == 1) {
            System.out.println("1 from " + from + " to " + to);
            return;
        }

        // 先把前size-1个塔片从from位置移到另一个位置
        process(size - 1, from, other, to);
        // 再把最后一个塔片从from位置移到to位置
        System.out.println(size + " from " + from + " to " + to);
        // 最后把前size-1个塔片从另一个位置移到to位置
        process(size - 1, other, to, from);
    }
}
