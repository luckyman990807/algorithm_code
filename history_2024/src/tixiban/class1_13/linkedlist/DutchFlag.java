package tixiban.class1_13.linkedlist;

import java.util.ArrayList;

/**
 * 荷兰国旗
 * 链表实现荷兰国旗问题，给定一个单链表和一个基准，返回结果小于基准的放左边，等于基准的放中间，大于基准的放右边，要求时间复杂度O(N)
 * 如果借助集合，直接把链表转换成数组，来一次数组版荷兰国旗就解决了，但是这样需要额外空间复杂度O(N)，有没有额外空间复杂度O(1)的？
 */
public class DutchFlag {
    public static class Node {
        public int value;
        public Node next;

        public Node() {
        }

        public Node(int value) {
            this.value = value;
            this.next = null;
        }
    }

    /**
     * 不借助集合实现链表荷兰国旗问题，思路复杂，但是节省空间，面试用，加分
     * @param head
     * @param base
     * @return
     */
    public static Node dutchFlag(Node head, int base) {
        // 小于区的头和尾指针
        Node smallHead = null;
        Node smallTail = null;
        // 等于区的头和尾指针
        Node equalHead = null;
        Node equalTail = null;
        // 大于区的头和尾指针
        Node bigHead = null;
        Node bigTail = null;

        while (head != null) {
            Node next = head.next;
            // 为什么当前节点的next一定要置空
            // 举例：2-3-4，基准2。小于区指向2，如果不置空，2后面还跟着-3-4，即小于区是2-3-4，等于区同理3-4，大于区是4，返回结果变成2-3-4-3-4-4
            head.next = null;

            if (head.value < base) {
                if (smallHead == null) {
                    // 如果小于区头节点为空，说明当前节点是小于区第一个，那么头尾指针都指向它
                    smallHead = head;
                    smallTail = head;
                } else {
                    // 否则说明小于区已经有节点了，当前节点追加到尾部
                    smallTail.next = head;
                    smallTail = head;
                }
            } else if (head.value > base) {
                if (bigHead == null) {
                    bigHead = head;
                    bigTail = head;
                } else {
                    bigTail.next = head;
                    bigTail = head;
                }
            } else {
                if (equalHead == null) {
                    equalHead = head;
                    equalTail = head;
                } else {
                    equalTail.next = head;
                    equalTail = head;
                }
            }
            head = next;
        }

        // 把小于区、等于区、大于区连到一起，小于区的尾连等于区的头，等于区的尾连大于区的头
        // 先把两处能连的连起来
        // 小于区的尾连等于区的头，并确定等于区的尾，用于下一步连大于区的头
        if (smallTail != null) {
            smallTail.next = equalHead;
            equalTail = equalTail == null ? smallTail : equalTail;
        }
        // 等于区的尾连大于区的头
        if (equalTail != null) {
            equalTail.next = bigHead;
        }
        // 不管实际有没有，反正该连的连好了，最后只要确定实际的头就行了
        return smallHead != null ? smallHead : (equalHead != null ? equalHead : bigHead);
    }

    /**
     * 借助列表实现荷兰国旗问题，思路简单，笔试用
     */
    public static Node dutchFlagWithList(Node head, int base) {
        // 把链表装进列表
        ArrayList<Node> list = new ArrayList<>();
        Node node = head;
        while (null != node) {
            list.add(node);
            node = node.next;
        }

        // 荷兰国旗问题
        int smallArea = -1;
        int bigArea = list.size();
        int cur = 0;
        while (cur < bigArea) {
            if (list.get(cur).value < base) {
                swap(list, cur++, ++smallArea);
            } else if (list.get(cur).value > base) {
                swap(list, cur, --bigArea);
            } else {
                cur++;
            }
        }

        // 列表再变回链表
        node = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            node.next = list.get(i);
            node = node.next;
        }
        node.next = null;

        // 列表的头就是链表的头
        return list.get(0);
    }

    public static void swap(ArrayList<Node> list, int a, int b) {
        if (a == b || null == list) {
            return;
        }
        Node temp = list.get(a);
        list.set(a, list.get(b));
        list.set(b, temp);
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
        int length = (int) (Math.random() * maxLength) + 1;

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

    public static Node copyLinkedList(Node head){
        Node result = new Node(head.value);
        Node resultCur = result;

        Node sourceCur = head.next;
        while (sourceCur!=null){
            Node next = new Node(sourceCur.value);
            resultCur.next= next;
            resultCur = next;
            sourceCur=sourceCur.next;
        }
        return result;
    }

    public static void main(String[] args) {

//        int maxValue = 20;
//        Node head = generateLinkedList(10, maxValue);
//        int base = (int) (Math.random() * (maxValue + 1));
//        dutchFlag(head, base);

        for (int n = 0; n < 10000; n++) {
            int maxValue = 20;
            Node head = generateLinkedList(10, maxValue);
            print(head);
            Node head1 = copyLinkedList(head);
            print(head1);
            Node head2 = copyLinkedList(head);
            print(head2);
            int base = (int) (Math.random() * (maxValue + 1));
            System.out.println(base);

            Node result1 = dutchFlagWithList(head1, base);
            print(result1);

            Node result2 = dutchFlag(head2, base);
            print(result2);

            Node cur1 = result1;
            Node cur2 = result2;

            boolean flag = false;
            while (cur1 != null) {
                if (cur1.value != cur2.value) {
                    flag = true;
                    break;
                }
                cur1 = cur1.next;
                cur2 = cur2.next;
            }

            if (flag) {

                System.out.print("原链表：");
                print(head);
                System.out.println("基准：" + base);
                System.out.print("辅助数组：");
                print(result1);
                System.out.print("不用数组：");
                print(result2);
                break;
            }
        }
    }
}
