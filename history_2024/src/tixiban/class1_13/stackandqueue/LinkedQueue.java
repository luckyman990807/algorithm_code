package tixiban.class1_13.stackandqueue;

public class LinkedQueue {
    private Node head;
    private Node tail;

    /**
     * 从头部进
     *
     * @param value
     */
    public void addToHead(Integer value) {
        Node cur = new Node(value);
        // 注意边界，当没有节点时
        if (null == head) {
            head = cur;
            tail = cur;
        } else {
            cur.next = head;
            head.pre = cur;
            head = cur;
        }
    }

    /**
     * 从尾部进
     *
     * @param value
     */
    public void addToTail(Integer value) {
        Node cur = new Node(value);
        // 注意边界，当没有节点时
        if (null == head) {
            head = cur;
            tail = cur;
        } else {
            tail.next = cur;
            cur.pre = tail;
            tail = cur;
        }
    }

    /**
     * 从头部出
     *
     * @return
     */
    public Integer removeFromHead() {
        // 注意边界，当没有节点时
        if (null == head) {
            return null;
        }

        Node cur = head;

        // 注意边界，当只有一个节点时
        if (head == tail) {
            head = null;
            tail = null;
        } else {
            head = head.next;
            head.pre = null;
            cur.next = null;
        }
        return cur.value;
    }

    /**
     * 从尾部出
     *
     * @return
     */
    public Integer removeFromTail() {
        // 注意边界，当没有节点时
        if (null == head) {
            return null;
        }

        Node cur = tail;

        // 注意边界，当只有一个节点时
        if (head == tail) {
            head = null;
            tail = null;
        } else {
            tail = tail.pre;
            tail.next = null;
            cur.pre = null;
        }
        return cur.value;
    }

    public static class Node {
        public Integer value;
        public Node pre;
        public Node next;

        public Node(Integer value) {
            this.value = value;
            this.pre = null;
            this.next = null;
        }
    }
}
