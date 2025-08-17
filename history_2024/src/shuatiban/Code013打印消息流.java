package shuatiban;

import java.util.HashMap;

/**
 * 已知一个消息流会不断地吐出整数1~N，但不一定按照顺序依次吐出，
 * 如果上次打印的最后一个序号为i， 那么这次当i+1出现时打印i+1及其之后接收过的所有连续数，直到1~N全部接收并打印完，请设计这种接收并打印的结构
 *
 * 思路:
 * 一边接收节点一边串成链表,会形成一个一个的链表,也就是接收到的一个一个的连续区间.
 * 定义一个头表,记录所有链表的头节点,和一个尾表,记录所有链表的尾节点,
 */
public class Code013打印消息流 {
    public static class Node {
        int value;
        Node next;

        public Node(int value) {
            this.value = value;
        }
    }

    public static class StreamReceiver {
        // 头表,记录所有连续区间的开头
        HashMap<Integer, Node> headMap;
        // 尾表,记录所有连续区间的结尾
        HashMap<Integer, Node> tailMap;
        // 当前在等哪个数打印
        int waitFor;

        public StreamReceiver() {
            headMap = new HashMap<>();
            tailMap = new HashMap<>();
            waitFor = 0;
        }

        /**
         * 接收消息
         * @param value
         */
        public void receive(int value) {
            Node cur = new Node(value);

            // 如果已经有value+1作为开头的连续区间,就让value接到这个连续区间,作为新的头.否则就单纯让value作为孤零零的头.
            if (headMap.containsKey(value + 1)) {
                cur.next = headMap.get(value + 1);
                headMap.remove(value + 1);
            }
            headMap.put(value, cur);

            // 如果已经有value-1作为结尾的连续区间,就让value接到这个连续区间,作为新的尾.否则就单纯让value作为孤零零的尾.
            if (tailMap.containsKey(value - 1)) {
                tailMap.get(value - 1).next = cur;
                tailMap.remove(value - 1);
            }
            tailMap.put(value, cur);

            // 如果value恰好是要等的数,就打印
            if (value == waitFor) {
                print();
            }
        }

        public void print() {
            Node cur = headMap.get(waitFor);
            headMap.remove(waitFor);

            while (cur.next != null) {
                System.out.print(cur.value + " ");
                cur = cur.next;
            }
            System.out.println(cur.value);
            waitFor = cur.value + 1;
        }
    }

    public static void main(String[] args) {
        StreamReceiver receiver = new StreamReceiver();
        System.out.println("接收0");
        receiver.receive(0);
        System.out.println("接收3");
        receiver.receive(3);
        System.out.println("接收2");
        receiver.receive(2);
        System.out.println("接收1");
        receiver.receive(1);
        System.out.println("接收6");
        receiver.receive(6);
        System.out.println("接收4");
        receiver.receive(4);
        System.out.println("接收7");
        receiver.receive(7);
        System.out.println("接收5");
        receiver.receive(5);
    }

}
