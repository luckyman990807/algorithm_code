package tixiban.class1_13.linkedlist;

import java.util.HashMap;
import java.util.Map;

/**
 * 链表节点有两个指针，next指向下个节点，random指针指向链表中的随机节点。给定一个这样的链表，复制一个新链表返回，要求时间复杂度O(N)
 * 假设C的random是R，那么C‘的random就是R‘，问题在于如何在新链表中找到C‘和R‘？
 */
public class CopyLinkWithRandomPoint {
    public static class Node {
        public int value;
        public Node next;
        public Node random;

        public Node(int value) {
            this.value = value;
            this.next = null;
            this.random = null;
        }
    }

    /**
     * 借助Map实现copy，思路简单，笔试用
     * 如何解决由老节点找新节点的问题：用Map
     *
     * @param head
     * @return
     */
    public static Node copyUseMap(Node head) {
        if (head == null) {
            return null;
        }
        // 用于存放老节点和新节点的映射
        Map<Node, Node> map = new HashMap<>();

        // 建立老节点和新节点的映射
        Node cur = head;
        while (cur != null) {
            map.put(cur, new Node(cur.value));
            cur = cur.next;
        }

        cur = head;
        while (cur != null) {
            // 假设C的random是R，那么C‘的random就是R‘
            // 如何找到C’？map.get(C)；如何找到R’？map.get(R)；
            map.get(cur).random = map.get(cur.random);
            cur = cur.next;
        }
        // 返回新头节点
        return map.get(head);
    }

    /**
     * 不借助Map实现copy，思路复杂，但是节省空间，面使用，加分
     * 如何解决由老节点找新节点的问题：利用链表结构，复制出新节点来直接插在老节点后面，这样老节点的next就是新节点
     *
     * @param head
     * @return
     */
    public static Node copy(Node head) {
        if (head == null) {
            return null;
        }

        // 第一次遍历，copy新节点，插在老节点后面
        Node cur = head;
        while (cur != null) {
            // 记录老next
            Node next = cur.next;
            // 老节点的next设为新节点
            cur.next = new Node(cur.value);
            // 新节点的next设为老next
            cur.next.next = next;
            // 跳到老next准备下一轮copy
            cur = next;
        }

        // 第二次遍历，设置新节点的random
        cur = head;
        while (cur != null) {
            // 新节点的random=老节点的random的新节点；如果老节点的random是null，那么新节点的random也是null
            cur.next.random = cur.random == null ? null : cur.random.next;
            // 跳到老next准备下一轮设置
            cur = cur.next.next;
        }

        // 第三次遍历，把新链表从老链表里剥离
        // 记录返回值（新链表的头）
        Node result = head.next;
        cur = head;
        while (cur != null) {
            // 记录copy的新节点
            Node copy = cur.next;
            // 老节点的next设置回老next，即新节点的next
            cur.next = copy.next;
            // 新节点的next=老next的新节点；如果老next是null，那么新节点的next也是null
            copy.next = cur.next == null ? null : cur.next.next;
            // 跳到老next准备下一轮设置
            cur = cur.next;
        }
        return result;
    }
}
