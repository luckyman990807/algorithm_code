package shuatiban;

/**
 * https://leetcode.com/problems/convert-binary-search-tree-to-sorted-doubly-linked-list/
 *
 * 给定一棵搜索二叉树头节点，转化成首尾相接的有序双向链表（节点都有两个方向的指针）
 * 思路：二叉树递归套路
 */
public class Code057搜索二叉树转成双向链表 {
    /**
     * 存放一个递归过程生成的双向链表的头节点和尾节点
     */
    class Info {
        Node start;
        Node end;

        public Info(Node start, Node end) {
            this.start = start;
            this.end = end;
        }
    }

    /**
     * 二叉树递归套路，左子树转换成双向链表，右子树转换成双向链表，最后整合成一个大的双向链表
     * @param head
     * @return
     */
    public Info process(Node head) {
        if (head == null) {
            return new Info(null, null);
        }
        Info leftInfo = process(head.left);
        Info rightInfo = process(head.right);

        if (leftInfo.end != null) {
            leftInfo.end.right = head;
        }
        head.left = leftInfo.end;
        head.right = rightInfo.start;
        if (rightInfo.start != null) {
            rightInfo.start.left = head;
        }
        return new Info(leftInfo.start != null ? leftInfo.start : head, rightInfo.end != null ? rightInfo.end : head);
    }

    public Node treeToDoublyList(Node root) {
        if (root == null) {
            return null;
        }
        Info info = process(root);
        info.start.left = info.end;
        info.end.right = info.start;
        return info.start;
    }


    class Node {
        public int val;
        public Node left;
        public Node right;

        public Node() {
        }

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, Node _left, Node _right) {
            val = _val;
            left = _left;
            right = _right;
        }
    }
}
