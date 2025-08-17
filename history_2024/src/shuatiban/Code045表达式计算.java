package shuatiban;

import java.util.LinkedList;

/**
 * 给定一个字符串str，str表示一个公式，公式里可能有整数、加减乘除符号和左右括号。返回公式的计算结果
 * 难点在于括号可能嵌套很多层，str="48*((70-65)-43)+8*1"，返回-1816。str="3+1*4"，返回7。str="3+(1*4)"，返回7。
 * 1，可以认为给定的字符串一定是正确的公式，即不需要对str做公式有效性检查
 * 2，如果是负数，就需要用括号括起来，比如"4*(-3)"但如果负数作为公式的开头或括号部分的开头，则可以没有括号，比如"-3*4"和"(-3*4)"都是合法的
 * 3，不用考虑计算过程中会发生溢出的情况。
 */
public class Code045表达式计算 {
    public static int calculate(String s) {
        return process(s.toCharArray(), 0)[0];
    }

    public static int[] process(char[] express, int i) {
        LinkedList<String> queue = new LinkedList<>();
        int cur = 0;
        while (i < express.length && express[i] != ')') {
            if (express[i] >= '0' && express[i] <= '9') {
                // 遇到数字,就把已经累加的数字*10再加上当前数字.例如最开始cur=0,遇到1,0*10+1=1.再例如cur=1,遇到5,1*10+5=15
                cur = cur * 10 + (express[i++] - '0');
            } else if (express[i] == '(') {
                // 遇到左括号,就递归求括号内的嵌套表达式,直接拿到结果作为一个数字
                int[] result = process(express, i + 1);
                cur = result[0];
                i = result[1] + 1;
            } else {
                // 遇到运算符号
                // 先把当前累加的数字入队列(入队列的时候如果栈顶运算符是*或/要先计算),当前累加的数字清零
                addNum(queue, cur);
                cur = 0;
                // 再把运算符入队列
                queue.addLast(String.valueOf(express[i++]));
            }
        }
        // 走到这里一定是表达式全部走完了,或者遇到)了,该返回了
        // 表达式最后一定是数字,先进队列
        addNum(queue, cur);
        // 队列里只剩下+-运算表达式,计算表达式结果
        int result = compute(queue);

        return new int[]{result, i};
    }

    /**
     * 把数字cur加入到队列queue
     */
    public static void addNum(LinkedList<String> queue, int cur) {
        if (!queue.isEmpty()) {
            // 如果此时队列不为空,那么top一定是运算符
            String operator = queue.peekLast();
            if (operator.equals("*") || operator.equals("/")) {
                // 如果运算符是*或/,不能直接把cur进队列,而是要把队列里上一个数字拿出来,跟cur运算,再把结果进队列.这是为了保证*/优先的运算顺序
                queue.pollLast();
                int firstNum = Integer.parseInt(queue.pollLast());
                cur = operator.equals("*") ? firstNum * cur : firstNum / cur;
            }
        }
        queue.addLast(String.valueOf(cur));
    }

    /**
     * 计算队列里的表达式的结果,队列里只有数字和+-,例如1+3-4+5+2
     */
    public static int compute(LinkedList<String> queue) {
        int firstNum = Integer.parseInt(queue.pollFirst());
        while (!queue.isEmpty()) {
            String operator = queue.pollFirst();
            int secondNum = Integer.parseInt(queue.pollFirst());
            firstNum = operator.equals("+") ? firstNum + secondNum : firstNum - secondNum;
        }
        return firstNum;
    }
}
