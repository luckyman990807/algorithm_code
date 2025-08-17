package tixiban.class1_13.unionfind;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * 并查集（用来做集合的合并和查询的数据结构），功能有以下几个：
 * 1、初始状态，每个节点都自己一个集合
 * 2、给定两个节点，判断是否属于同一个集合
 * 3、给定两个节点，把二者所属的集合合并为一个大集合
 *
 * 可以用表实现，也可以用数组实现，能用数组下标寻址代替表，就用数组，因为下标寻址很快
 */
public class UnionFind<V> {
    // 把value包装一下
    public static class Node<V> {
        public V value;

        public Node(V value) {
            this.value = value;
        }
    }

    // 节点：父亲节点
    public Map<Node<V>, Node<V>> parentMap = new HashMap<>();
    // 值：包装成的节点
    public Map<V, Node<V>> valueMap = new HashMap<>();
    // 集合代表节点：集合大小
    public Map<Node<V>, Integer> sizeMap = new HashMap<>();

    public UnionFind(List<V> values) {
        this.parentMap = new HashMap<>();
        this.valueMap = new HashMap<>();
        this.sizeMap = new HashMap<>();
        for (V value : values) {
            Node<V> node = new Node<>(value);
            parentMap.put(node, node);
            valueMap.put(value, node);
            sizeMap.put(node, 1);
        }
    }

    /**
     * 给定一个节点，找到这个节点所在集合的头，也就是集合的代表节点
     */
    public Node<V> findHead(Node<V> cur) {
        // 记录从已知节点到头节点所经过的全部节点
        Stack<Node<V>> path = new Stack<>();
        // 找头的主体逻辑
        while (parentMap.get(cur) != cur) {
            path.add(cur);
            cur = parentMap.get(cur);
        }
        // 把路径上的节点都挂在头节点上，也就是把并查集扁平化，下次找头就可以一步到位
        while (!path.isEmpty()) {
            parentMap.put(path.pop(), cur);
        }

        return cur;
    }

    /**
     * 给定两个节点，判断二者是否在同一个集合
     */
    public boolean isSameSet(V a, V b) {
        // 二者找到同一个代表节点，说明在同一个集合
        return findHead(valueMap.get(a)) == findHead(valueMap.get(b));
    }

    /**
     * 给定两个节点，把二者所属的两个集合合并成一个大集合
     */
    public void union(V a, V b) {
        // a节点所在集合的代表节点
        Node<V> headA = findHead(valueMap.get(a));
        // b节点所在集合的代表节点
        Node<V> headB = findHead(valueMap.get(b));
        // 如果代表节点是同一个，说明本来就在同一个集合，直接返回
        if (headA == headB) {
            return;
        }

        // 比较出哪个是大集合，哪个是小集合
        Node<V> bigHead = sizeMap.get(headA) >= sizeMap.get(headB) ? headA : headB;
        Node<V> smallHead = bigHead == headA ? headB : headA;

        // 小集合挂到大集合底下
        parentMap.put(smallHead, bigHead);
        // 小集合的size归到大集合下
        sizeMap.put(bigHead, sizeMap.get(bigHead) + sizeMap.get(smallHead));
        // 小集合的代表节点不再是代表节点了，从sizeMap中删除
        sizeMap.remove(smallHead);
    }
}
