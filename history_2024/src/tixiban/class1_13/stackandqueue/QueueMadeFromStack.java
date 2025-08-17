package tixiban.class1_13.stackandqueue;

import java.util.Stack;

public class QueueMadeFromStack {
    private Stack<Integer> pushStack;
    private Stack<Integer> popStack;

    public QueueMadeFromStack() {
        this.pushStack = new Stack<>();
        this.popStack = new Stack<>();
    }

    public void push(Integer value) {
        pushStack.push(value);
    }

    public Integer pop() {
        // 如果两个栈都没数据，那确实是没数据
        if (pushStack.isEmpty() && popStack.isEmpty()) {
            throw new RuntimeException("队列是空的");
        }

        // 如果pop栈是空的，就把push栈的数据倒进pop栈
        if (popStack.isEmpty()) {
            while (!pushStack.isEmpty()) {
                // "倒"指的是，1，2，3进push栈，则3，2，1进pop栈
                popStack.push(pushStack.pop());
            }
        }
        // pop栈不空直接弹pop栈，不能往pop栈倒数据，否则会打乱弹栈的顺序
        return popStack.pop();
    }
}
