package shuatiban;

import java.util.*;

/**
 * 给定三个参数，二叉树的头节点head，树上某个节点target，正数K。
 * 从target开始，可以向上走或者向下走，返回与target的距离是K的所有节点
 *
 * 图的宽度优先遍历,每个节点的可达节点有:左孩子、右孩子、父节点
 */
public class Code014距离target为k的所有节点 {
    public static class Node {
        int value;
        Node left;
        Node right;

        public Node(int value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(value);
        }
    }

    /**
     * 由于经典二叉树没有指向父节点的指针,所以要建立父节点表,才能实现向上走
     */
    public static void createParentMap(Node cur, HashMap<Node, Node> parentMap) {
        if (cur == null) {
            return;
        }
        if (cur.left != null) {
            parentMap.put(cur.left, cur);
            createParentMap(cur.left, parentMap);
        }
        if (cur.right != null) {
            parentMap.put(cur.right, cur);
            createParentMap(cur.right, parentMap);
        }
    }

    public static List<Node> getDistanceKNodes(Node head, Node target, int k) {
        // 父节点表,记录每个节点的父节点(如果有)
        HashMap<Node, Node> parentMap = new HashMap<>();
        createParentMap(head, parentMap);

        // 宽度优先遍历用的队列
        LinkedList<Node> queue = new LinkedList<>();
        queue.addFirst(target);

        // 用于记录一个节点是否被遍历过
        HashSet<Node> visited = new HashSet<>();
        visited.add(target);

        // 当前层,要遍历k层
        int level = 0;
        // 记录返回值
        List<Node> result = new ArrayList<>();

        while (!queue.isEmpty()) {
            int size = queue.size();
            if (level == k) {
                // 如果当前层就是k层,那么队列里的节点就是和target距离为k的节点,全部返回
                while (size-- > 0) {
                    result.add(queue.pollLast());
                }
                break;
            } else {
                // 如果还没到k层,就把队列里面的节点(都是同一层的)全部弹出并压入他们的孩子/父亲作为下一层
                while (size-- > 0) {
                    Node cur = queue.pollLast();
                    if (cur.left != null && !visited.contains(cur.left)) {
                        queue.addFirst(cur.left);
                        visited.add(cur.left);
                    }
                    if (cur.right != null && !visited.contains(cur.right)) {
                        queue.addFirst(cur.right);
                        visited.add(cur.right);
                    }
                    if (parentMap.containsKey(cur) && !visited.contains(parentMap.get(cur))) {
                        queue.addFirst(parentMap.get(cur));
                        visited.add(parentMap.get(cur));
                    }
                }
            }

            level++;
        }
        return result;
    }

    public static void main(String[] args) {
        /*
             1
           /   \
          2     3
         / \   /
        4   5 6
           / \
          7   8
        求跟2节点的距离为2的所有节点
         */
        Node head = new Node(1);
        head.left = new Node(2);
        head.left.left = new Node(4);
        head.left.right = new Node(5);
        head.left.right.left = new Node(7);
        head.left.right.right = new Node(8);
        head.right = new Node(3);
        head.right.left = new Node(6);

        System.out.println(getDistanceKNodes(head, head.left, 2));
    }

}
