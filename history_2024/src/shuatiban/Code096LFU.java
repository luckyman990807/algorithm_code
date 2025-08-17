package shuatiban;

import java.util.HashMap;
import java.util.Map;

/**
 * LFU内存/缓存替换算法（Least Frequently Used，最不频繁使用，选择使用次数最少的页面予以淘汰，使用次数最少的如果有多个，选择其中最久未被使用的）
 * Leetcode题目：https://leetcode.com/problems/lfu-cache/
 *
 * 思路：还是map+双链表，但是双链表是桶的链表，一个桶内的节点访问次数相同，节点按照最近访问时间再排成双链表
 * 新增节点的时候，放在次数为1的桶的head节点，查询或修改节点的时候，节点使用次数+1，节点要从原桶删除，插入到次数+1的桶的head节点，同时原桶要判断删除节点后要判断是否空了，空桶要删除
 *
 * 难的地方在于边界条件，重新连接链表的时候，next/pre是否为null？头桶是否为null（即是不是一个桶都没有）？
 *
 * 想法：如果不删除空桶会怎么样？
 */
public class Code096LFU {
    /**
     * 缓存数据结构
     */
    static class LFUCache {
        // 缓存容量
        int capacity;
        // 缓存节点的key到缓存节点的映射
        Map<Integer, Node> keyToNode;
        // 缓存节点到节点所属桶的映射
        Map<Node, Bucket> nodeToBucket;
        // 桶链表的头桶
        Bucket headBucket;

        public LFUCache(int capacity) {
            this.capacity = capacity;
            keyToNode = new HashMap<>();
            nodeToBucket = new HashMap<>();
            headBucket = null;
        }

        /**
         * 向缓存中插入一条记录
         * @param key
         * @param value
         */
        public void put(int key, int value) {
            if (keyToNode.containsKey(key)) {
                // 如果已经存在这个key，就是更新。通过key查出node，修改node的值，然后增加node的访问次数，移动到合适的桶
                Node node = keyToNode.get(key);
                node.value = value;
                node.times++;
                move(node);
            } else {
                // 如果这个key不存在，就是插入
                if (keyToNode.size() == capacity) {
                    // 如果缓存已经满了，就要删除头桶的头节点（最久最少访问的节点）
                    Node headNode = headBucket.headNode;
                    headBucket.delete(headNode);
                    modify(headBucket);
                    keyToNode.remove(headNode.key);
                    nodeToBucket.remove(headNode);
                }
                // 创建新节点
                Node node = new Node(key, value);
                if (headBucket == null) {
                    // 如果头桶为空（可能是缓存容量为1，刚才已经把唯一的节点删掉了；也可能是第一次向缓存中插入），就新建一个桶，把头桶指针指向新头桶
                    headBucket = new Bucket(node);
                } else {
                    // 如果头桶不为空，就说明已经有头桶了
                    if (headBucket.headNode.times == node.times) {
                        // 如果头桶的访问次数和node匹配，就直接插入
                        headBucket.add(node);
                    } else {
                        // 否则头桶的访问次数和node不匹配，就新建一个桶，作为新的头桶，并和原有的桶链表连接
                        Bucket newHeadBucket = new Bucket(node);
                        newHeadBucket.next = headBucket;
                        headBucket.pre = newHeadBucket;
                        headBucket = newHeadBucket;
                    }
                }
                // 记录映射关系
                keyToNode.put(key, node);
                nodeToBucket.put(node, headBucket);
            }
        }

        /**
         * 从缓存中查询一条记录
         * @param key
         * @return
         */
        public int get(int key) {
            if (keyToNode.containsKey(key)) {
                Node node = keyToNode.get(key);
                node.times++;
                move(node);
                return node.value;
            }
            return -1;
        }

        /**
         * 删除空桶
         * @param bucket
         */
        public void modify(Bucket bucket) {
            // 只针对空桶
            if (!bucket.empty()) {
                return;
            }
            if (headBucket == bucket) {
                // 如果当前桶是头桶，就赋值新的头桶指针
                headBucket = headBucket.next;
                if (headBucket != null) {
                    headBucket.pre = null;
                }
            } else {
                // 否则，当前桶不是头桶，把pre和next相连
                bucket.pre.next = bucket.next;
                if (bucket.next != null) {
                    bucket.next.pre = bucket.pre;
                }
            }
            // 这些if(!=null)是在双向链表双指针重新连接的过程中判空
            // 这里没有把bucket的next和pre设为null，因为空桶删了就没用了，而节点从一个桶中删了还会去到另一个桶
        }

        /**
         * 将一个节点移动到合适的桶
         * @param node
         */
        public void move(Node node) {
            // 节点当前所在的桶
            Bucket curBucket = nodeToBucket.get(node);
            // 从当前桶中删除节点
            curBucket.delete(node);
            if (curBucket.next != null && curBucket.next.headNode.times == node.times) {
                // 如果当前桶有next，且next恰好是node该去的桶，就向next桶中插入节点，并记录节点和桶的映射关系
                curBucket.next.add(node);
                nodeToBucket.put(node, curBucket.next);
            } else {
                // 否则就意味着node该去的桶不存在，需要新建桶，并插入现有的桶链表中，并记录节点和桶的映射关系
                Bucket newBucket = new Bucket(node);
                newBucket.pre = curBucket;
                newBucket.next = curBucket.next;
                if (curBucket.next != null) {
                    curBucket.next.pre = newBucket;
                }
                curBucket.next = newBucket;
                nodeToBucket.put(node, newBucket);
            }
            // curBucket删除节点后，可能为空，所以检查并删除空桶
            modify(curBucket);
        }
    }

    /**
     * 桶数据结构
     */
    static class Bucket {
        Node headNode;
        Node tailNode;
        Bucket next;
        Bucket pre;

        public Bucket(Node node) {
            headNode = node;
            tailNode = node;
            next = null;
            pre = null;
        }

        public boolean empty() {
            return headNode == null;
        }

        /**
         * 向桶中插入指定的节点
         * @param node
         */
        public void add(Node node) {
            if (empty()) {
                headNode = node;
                tailNode = node;
            } else {
                tailNode.next = node;
                node.pre = tailNode;
                tailNode = node;
            }
        }

        /**
         * 从桶中删除指定的节点
         * @param node
         */
        public void delete(Node node) {
            if (headNode == tailNode) {
                // 如果桶中只用一个节点，那么直接让头尾节点都指向空
                headNode = null;
                tailNode = null;
            } else if (headNode == node) {
                // 如果要删的节点是头节点，那么就赋值新的头节点
                headNode = headNode.next;
                headNode.pre = null;
                node.next = null;
            } else if (tailNode == node) {
                // 如果要删的节点是尾节点，那么就赋值新的头节点
                tailNode = tailNode.pre;
                tailNode.next = null;
                node.pre = null;
            } else {
                // 否则，要删的节点既不是头节点也不是尾节点，既有pre又有next，正常删
                node.pre.next = node.next;
                node.next.pre = node.pre;
            }
        }
    }

    /**
     * 节点数据结构
     */
    static class Node {
        int key;
        int value;
        Node next;
        Node pre;
        // 记录反问次数
        int times;

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
            next = null;
            pre = null;
            times = 1;
        }
    }
}
