package tixiban.class1_13.stackandqueue;


import java.util.LinkedList;
import java.util.Queue;

public class StackMadeFromQueue {
    private Queue<Integer> queue;
    private Queue<Integer> help;

    public StackMadeFromQueue() {
        // java.util包中LinkedList就是Queue接口的实现类
        this.queue = new LinkedList<>();
        this.help = new LinkedList<>();
    }

    /**
     * 压栈
     *
     * @param value
     */
    public void push(Integer value) {
        // 压入队列
        queue.offer(value);
    }

    /**
     * 弹栈
     * @return
     */
    public Integer pop() {
        // 把队列中的数据压入辅助队列，只留下一个，就是最晚进队列的那个
        while (queue.size() > 1) {
            help.offer(queue.poll());
        }
        // 剩下的这个就是要弹出的，达到后进先出
        Integer result = queue.poll();

        // 现在数据都在辅助队列，所以交换引用
        Queue<Integer> temp = queue;
        queue = help;
        help = temp;

        return result;
    }
}
