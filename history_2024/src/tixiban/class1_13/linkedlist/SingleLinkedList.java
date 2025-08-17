package tixiban.class1_13.linkedlist;

public class SingleLinkedList {
    public static class Node {
        public int value;
        public Node next;
    }

    /**
     * 单链表反转
     * @param head
     * @return
     */
    public static Node reverse(Node head) {
        // pre要作为返回值，每轮循环中pre记录head的上一个节点
        Node pre = null;
        // 每轮循环中next记录head的下一个节点
        Node next = null;
        // 循环的主线是head指针不断后移
        while (null != head) {
            // 先记录下个节点，要不然指针转向后找不着了
            next = head.next;
            // next指针指向上一个节点，完成当前节点的反转
            head.next = pre;

            // 整体往后移一位，准备下个节点的反转
            pre = head;
            head = next;
        }

        // 一直后移到head==null的时候，pre就是最后一个节点，也是反转后的头节点
        return pre;
    }

    /**
     * 单链表删除某值
     * @param head
     * @param value
     * @return
     */
    public static Node delete(Node head, int value) {
        // 如果开头就有目标值，head就往后跳，一直跳到第一个不需要删的位置
        while (null != head && value == head.value) {
            head = head.next;
        }
        // head已经是null了，说明所有节点全删了，返回null
        if(null == head){
            return null;
        }

        // cur永远指向不需要删的节点
        Node cur = head;
        // next永远指向cur的下一个节点，也是待判断是否删除的节点
        Node next = head.next;
        // 循环的主线是next指针不断后移
        while (null != next) {
            // 如果next是目标值，就让cur的next指针略过next，跳到next.next
            if (value == next.value) {
                cur.next = next.next;
            // 否则不是目标值，就把cur指向next，表示next不需要删除了
            } else {
                cur = next;
            }

            next = next.next;
        }
        return head;
    }

    public static void print(Node head) {
        Node current = head;
        while (null != current) {
            System.out.print(current.value + " ");
            current = current.next;
        }
        System.out.println();
    }

    public static Node generateLinkedList(int maxLength, int maxValue) {
        int length = (int) (Math.random() * (maxLength + 1));
        if (0 == length) {
            return null;
        }

        Node head = new Node();
        head.value = (int) (Math.random() * (maxValue + 1));
        head.next = null;
        length--;

        Node pre = head;

        for (int i = 0; i < length; i++) {
            Node cur = new Node();
            cur.value = (int) (Math.random() * (maxValue + 1));
            cur.next = null;

            pre.next = cur;
            pre = cur;
        }
        return head;
    }
}
