package tixiban.class1_13.linkedlist;

public class DoubleLinkedList {
    public static class Node {
        public int value;
        public Node next;
        public Node pre;
    }

    /**
     * 双链表反转
     * @param head
     * @return
     */
    public static Node reverse(Node head) {
        // pre要作为返回值，每轮循环中，pre记录head的上一个节点
        Node pre = null;
        // 每轮循环中，next记录head的下一个节点
        Node next = null;
        // 循环的主线是head指针不断后移
        while (null != head) {
            // 先记录下一个节点的指针
            next = head.next;

            // 前后指针交换
            head.pre = next;
            head.next = pre;

            // 整体后移一个节点，准备下个节点的反转
            pre = head;
            head = next;
        }
        // 一直后移到head==null，这时pre就是最后一个节点，也是反转后的头节点
        return pre;
    }

    /**
     * 双链表删除某值
     * @param head
     * @param value
     * @return
     */
    public static Node delete(Node head, int value) {
        // 如果开头就有目标值，head就往后跳，一直跳到第一个不需要删的位置
        while (null != head && value == head.value) {
            head = head.next;
        }
        // 如果head已经是null了，说明全部节点都删了，返回null
        if(null == head){
            return null;
        }

        // cur永远指向不需要删的节点
        Node cur = head;
        // next永远指向cur下一个节点，也是待判断是否删除的节点
        Node next = head.next;
        // 循环的主线是next指针不断后移
        while (null != next) {
            // 如果next是目标值，就让cur的next指针掠过next，指向next的下一个节点
            if (value == next.value) {
                cur.next = next.next;
            // 否则不是目标值
            } else {
                // 万一上一轮循环有略过的，这次要把pre指针给接上
                next.pre = cur;
                //cur指向next，表示next不需要删除了
                cur = next;
            }
            next = next.next;
        }
        return head;
    }

    public static void print(Node head) {
        Node cur = head;
        while (null != cur) {
            System.out.print(cur.value + " ");
            cur = cur.next;
        }
        System.out.println();
    }

    public static Node generateLinkedList(int maxLength, int maxValue) {
        if (0 == maxLength) {
            return null;
        }
        int length = (int) (Math.random() * (maxLength + 1));

        Node head = new Node();
        head.value = (int) (Math.random() * (maxValue + 1));
        head.pre = null;
        head.next = null;
        length--;

        Node pre = head;
        for (int i = 0; i < length; i++) {
            Node cur = new Node();
            cur.value = (int) (Math.random() * (maxValue + 1));
            cur.pre = pre;
            cur.next = null;

            pre.next = cur;

            pre = cur;
        }
        return head;
    }
}
