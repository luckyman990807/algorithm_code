package tixiban.class26ordertree;

import java.util.ArrayList;
import java.util.List;

/**
 * 跳表实现
 */
public class Code03SkipListMap {
    public static class Node<K extends Comparable<K>, V> {
        public K key;
        public V value;
        // 因为有多层,所以有多个后继节点
        public List<Node<K, V>> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = new ArrayList<>();
            this.next.add(null);
        }

        /**
         * 判断当前节点的key是否比传入的key小
         * 认为null是最小的
         */
        public boolean keyLessThan(K key) {
            return key != null && (this.key == null || this.key.compareTo(key) < 0);
        }

        /**
         * 判断当前节点的key是否和传入的key相等
         */
        public boolean keyEquals(K key) {
            return (this.key == null && key == null) || (this.key != null && key != null & this.key.compareTo(key) == 0);
        }
    }

    public static class SkipListMap<K extends Comparable<K>, V> {
        private Node<K, V> head;
        private static final double PROBABILITY = 0.5;
        private int size;
        private int maxLevel;

        public SkipListMap() {
            head = new Node<>(null, null);
            size = 0;
            maxLevel = 0;
        }

        /**
         * 在跳表上找到小于key的最右边那个节点
         * 思路:在最高层上找到小于key的最右节点,然后在这个节点上跳到下一层,再在这一层上找到小于key的最右节点,再跳到下一层...
         */
        public Node<K, V> mostRightLessNode(K key) {
            if (key == null) {
                return null;
            }
            int level = maxLevel;
            Node<K, V> cur = head;
            while (level >= 0) {
                cur = mostRightLessNodeInLevel(key, cur, level--);
            }
            return cur;
        }

        /**
         * 在跳表的第level层上从cur开始往右找,找到小于key的最靠右的节点
         * 思路:从cur开始一直next(level)往右出溜
         */
        public Node<K, V> mostRightLessNodeInLevel(K key, Node<K, V> cur, int level) {
            Node<K, V> next = cur.next.get(level);
            while (next != null && next.key.compareTo(key) < 0) {
                cur = next;
                next = next.next.get(level);
            }
            return cur;
        }

        /**
         * 判断跳表上是否包含这个key
         * 思路:找到小于key最右的节点,看看下一个节点是不是key,是就存在,不是就不存在.
         */
        public boolean contains(K key) {
            if (key == null) {
                return false;
            }
            Node<K, V> next = mostRightLessNode(key).next.get(0);
            return next != null && next.keyEquals(key);
        }

        /**
         * 在跳表上插入或更新节点
         */
        public void put(K key, V value) {
            if (key == null) {
                return;
            }
            Node<K, V> next = mostRightLessNode(key).next.get(0);
            if (next != null && next.keyEquals(key)) {
                // 已存在当前key,就更新
                next.value = value;
            } else {
                // 否则就插入

                // new一个节点,roll骰子roll一个层数,每次有0.5的概率增加层数,另外0.5的概率停下.只要不停下就一直增加层数.
                Node<K, V> newNode = new Node<>(key, value);
                int newLevel = 0;
                while (Math.random() > PROBABILITY) {
                    newLevel++;
                    newNode.next.add(null);
                }

                // 更新跳表大小、最大层数,添加head节点对应层数的next指针.
                size++;
                while (newLevel > maxLevel) {
                    maxLevel++;
                    head.next.add(null);
                }

                // 把newNode插入cur和cur.next之间
                Node<K, V> pre = head;
                int level = maxLevel;
                while (level >= 0) {
                    pre = mostRightLessNodeInLevel(key, pre, level);
                    if (level <= newLevel) {
                        newNode.next.set(level, pre.next.get(level));
                        pre.next.set(level, newNode);
                    }
                    level--;
                }
            }
        }

        /**
         * 从跳表中删除节点
         */
        public void remove(K key) {
            if (contains(key)) {
                Node<K, V> pre = head;
                int level = maxLevel;
                while (level >= 0) {
                    pre = mostRightLessNodeInLevel(key, pre, level);
                    Node<K, V> cur = pre.next.get(level);

                    if (cur != null && cur.keyEquals(key)) {
                        pre.next.set(level, cur.next.get(level));
                    }

                    if (level != 0 && pre == head && pre.next.get(level) == null) {
                        head.next.remove(level);
                        maxLevel--;
                    }
                    level--;
                }
            }
        }

        /**
         * 从跳表上查询一个节点
         */
        public Node<K, V> get(K key) {
            Node<K, V> mostRightLess = mostRightLessNode(key);
            return mostRightLess != null && mostRightLess.next.get(0).keyEquals(key) ? mostRightLess.next.get(0) : null;
        }


        // 用于测试打印
        public void print() {
            Node<K, V> cur = head;
            while (cur.next.get(0) != null) {
                cur = cur.next.get(0);
                System.out.print(cur.value + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        SkipListMap<Integer, String> skipListMap = new SkipListMap<>();
        skipListMap.put(1, "1");
        skipListMap.put(5, "5");
        skipListMap.put(3, "3");
        skipListMap.put(2, "2");
        skipListMap.put(8, "8");
        skipListMap.put(7, "7");
        skipListMap.print();
        skipListMap.put(1, "11");
        skipListMap.print();
        skipListMap.remove(3);
        skipListMap.print();
        System.out.println(skipListMap.get(1).value);
    }


}
