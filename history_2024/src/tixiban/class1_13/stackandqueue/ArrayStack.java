package tixiban.class1_13.stackandqueue;

public class ArrayStack {
    private int limit;
    private int[] arr;
    // head永远等于下次压栈应该放的位置
    private int head;

    public ArrayStack(int limit) {
        this.limit = limit;
        this.arr = new int[limit];
        this.head = 0;
    }

    public void push(int value) {
        if (head >= limit) {
            throw new RuntimeException("栈满了，不能压栈");
        }
        arr[head++] = value;
    }

    public int pop() {
        if (head <= 0) {
            throw new RuntimeException("栈空了，不能弹栈");
        }
        return arr[--head];
    }
}
