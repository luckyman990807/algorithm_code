package tixiban.class1_13.stackandqueue;

public class ArrayQueue {
    private final int limit;
    private int[] arr;
    // 弹出的时候从哪个位置弹
    private int popIndex;
    // 压入的时候往哪个位置压
    private int pushIndex;
    private int size;

    public ArrayQueue(int limit) {
        this.limit = limit;
        arr = new int[limit];
        popIndex = 0;
        pushIndex = 0;
        size = 0;
    }

    public void push(int value) {
        if (size >= limit) {
            throw new RuntimeException("队列满了，不能压入");
        }
        arr[pushIndex] = value;
        // 循环数组，index先+1，如果超过limit就取余绕回去
        pushIndex = (++pushIndex) % limit;
    }

    public int pop() {
        if (size <= 0) {
            throw new RuntimeException("队列空了，不能弹出");
        }
        int result = arr[popIndex];
        // 循环数组，index先+1，如果超过limit就取余绕回去
        popIndex = (++popIndex) % limit;
        return result;
    }
}
