package tixiban.class1_13.stackandqueue;

import java.util.Stack;

public class MinStack {
    private Stack<Integer> dataStack;
    private Stack<Integer> minStack;

    public MinStack() {
        dataStack = new Stack<>();
        minStack = new Stack<>();
    }

    public void push(Integer value) {
        if (minStack.isEmpty()) {
            // 如果最小栈为空，直接压入
            minStack.push(value);
        } else {
            // 如果最小栈不为空，那么新元素和当前最小元素谁更小就压谁
            minStack.push(value < getMin() ? value : getMin());
        }
        dataStack.push(value);
    }

    public Integer pop(Integer value) {
        minStack.pop();
        return dataStack.pop();
    }

    public Integer getMin() {
        if (minStack.isEmpty()) {
            throw new RuntimeException("栈是空的");
        }
        // 返回最小栈栈顶，就是当前最小的元素
        return minStack.peek();
    }
}
