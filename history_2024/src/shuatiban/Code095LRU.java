package shuatiban;

import java.util.HashMap;
import java.util.Map;

/**
 * LRU内存/缓存替换算法（Least Recent Used，最近最少使用，选择最久未被使用的页面予以淘汰）
 * Leetcode题目：https://leetcode.com/problems/lru-cache/
 *
 * 思路：map+双链表。链表的头是最早使用的，链表的尾是最近使用的，淘汰的时候直接删掉链表的头，使用到某个节点的时候把节点挪到链表的尾。map的作用是，O(1)地get一个节点，或者put的时候O(1)地判断节点是否存在
 *
 * 问：map的key是节点的key，map的value是节点（包括key和value），重复保存了key，这样是否浪费？
 * 答：不浪费，少了任意一个都无法按照上述思路实现。
 * 例如，map的value只存节点的value，那么get的时候，从map中O(1)get到value了，但是moveToTail的时候需要遍历链表才能找到对应的节点。
 * 再例如，不用map，而是用Set<Node>，那么直接无法O(1)get了
 */
public class Code095LRU {
    /**
     * 链表节点
     */
    class Node {
        public int key;
        public int value;
        public Node next;
        public Node pre;

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
            next = null;
            pre = null;
        }
    }

    /**
     * 链表
     */
    class MyLinked {
        public Node head;
        public Node tail;

        public MyLinked() {
            head = null;
            tail = null;
        }

        /**
         * 往链表里新增节点，也就是在链表的尾部插入节点
         * @param node
         */
        public void add(Node node) {
            if (head == null) {
                head = node;
                tail = node;
            } else {
                tail.next = node;
                node.pre = tail;
                tail = node;
            }
        }

        /**
         * 删除链表的头节点，用于缓存淘汰。
         * 分两种情况：如果头指针==尾指针，说明链表中只有一个节点的时候，只需要清空头尾指针即可。否则说明链表中有多个节点，走通用的删除头节点逻辑
         * @return
         */
        public Node removeHead() {
            Node result = head;
            if (head == tail) {
                head = null;
                tail = null;
            } else {
                head = head.next;
                head.pre = null;
                result.next = null;
            }
            return result;
        }

        /**
         * 把链表中某个节点移动到尾部，用于某个节点被使用的时候，标记为最近使用
         * 分三种情况：
         * 如果当前节点就是尾节点，那么什么都不用做
         * 如果当前节点是头节点，头节点移动到尾部需要如下几步：头指针指向新的头（头的next），新头pre指向空，老头的next指向空，老头的pre指向尾，尾的next指向老头，尾指针指向新的尾（老头）
         * 如果当前节点是普通节点，有pre也有next，那么移动该节点需要如下几步：先把节点从pre和next中间摘出来，再把节点插到链表尾部
         * @param node
         */
        public void moveToTail(Node node) {
            if (node == tail) {
                return;
            }
            if (node == head) {
                head = head.next;
                head.pre = null;
            } else {
                node.next.pre = node.pre;
                node.pre.next = node.next;
            }
            node.next = null;
            node.pre = tail;
            tail.next = node;
            tail = node;
        }
    }


    class LRUCache {
        public Map<Integer, Node> map;
        public MyLinked myLinked;
        public int size;

        public LRUCache(int capacity) {
            this.size = capacity;
            this.map = new HashMap<>();
            this.myLinked = new MyLinked();
        }

        public int get(int key) {
            Node node = map.get(key);
            if (node != null) {
                myLinked.moveToTail(node);
                return node.value;
            }
            return -1;
        }

        public void put(int key, int value) {
            Node node = map.get(key);
            if (node != null) {
                node.value = value;
                myLinked.moveToTail(node);
            } else {
                Node newNode = new Node(key, value);
                myLinked.add(newNode);
                map.put(key, newNode);
                if (map.size() > size) {
                    Node head = myLinked.removeHead();
                    map.remove(head.key);
                }
            }
        }
    }
}
