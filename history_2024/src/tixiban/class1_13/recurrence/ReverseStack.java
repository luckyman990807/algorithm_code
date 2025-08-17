package tixiban.class1_13.recurrence;

import java.util.Stack;

/**
 * 逆序一个栈，要求不能申请额外的数据结构
 */
public class ReverseStack {
    /**
     * 弹出栈底元素，上面的元素盖下来，返回弹出的栈底元素
     * <p>
     * 要弹出栈底元素，但是上面盖了其他元素怎么办：通过递归不断地揭开栈顶，露出下面的元素试图找栈底，下面的元素继续通过递归揭开栈顶找栈底……
     * 直到剩下一个元素，揭开的栈顶就是栈底，返回给上一层，上一层记录下栈底，把之前揭开的栈顶压进去，把栈底返回给再上一层……
     * 直到随着递归的不断返回，揭开的栈顶最终都依次压回去，栈底最终会返回给主函数
     */
    public static int pullBottom(Stack<Integer> stack) {
        Integer result = stack.pop();
        if (stack.isEmpty()) {
            return result;
        }
        int bottom = pullBottom(stack);
        stack.push(result);
        return bottom;
    }

    /**
     * 反转一个栈
     * 怎么反转：先抽出栈底元素，把上面的反转，再把栈底元素压到栈顶
     */
    public static void reverse(Stack<Integer> stack) {
        if (stack.isEmpty()) {
            return;
        }
        int bottom = pullBottom(stack);
        reverse(stack);
        stack.push(bottom);
    }

    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);

        reverse(stack);
        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }
    }
}
