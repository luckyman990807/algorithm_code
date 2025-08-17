package tixiban.class1_13.binarytree;

import tixiban.class1_13.binarytree.common.Node;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 给定一棵二叉树，判断是不是完全二叉树
 * 完全二叉树定义：每一层都是满的，即便有不满的也只能是最后一层，最后一层即便不满也是从左往右依次变满，没有空位
 */
public class CompleteBinaryTree {
    /**
     * 思路：中序遍历的同时，
     * 一方面，如果遍历到某个节点只有右孩子没有左孩子，那么不是完全二叉树
     * 另一方面，当第一次遍历到左右不双全的节点时，后面遍历的必须是叶子节点
     */
    public boolean isComplete(Node head) {
        if (head == null) {
            // 一般来讲，空树算完全二叉树
            return true;
        }

        // 记录是否遇到过左右不双全的节点，即是否接下来都必须是叶子节点
        boolean leaf = false;

        Queue<Node> queue = new LinkedList<>();
        queue.add(head);
        while (!queue.isEmpty()) {
            Node cur = queue.poll();

            // 如果已经遇到过左右不双全的节点了，即接下来只能是叶子了，那么如果当前节点不是叶子，直接返回false
            if (leaf && (cur.left != null || cur.right != null)) {
                return false;
            }
            // 如果只有右孩子没有左孩子，直接返回false
            if (cur.left == null && cur.right != null) {
                return false;
            }

            if (cur.left != null) {
                queue.add(cur.left);
            }
            if (cur.right != null) {
                queue.add(cur.right);
            }

            // 如果遇到了左右不双全的节点，把标识置为true。重复多次置true无影响
            if (cur.left == null || cur.right == null) {
                leaf = true;
            }
        }
        return true;
    }
}
