package tixiban.class1_13.binarytree.treedp;

import tixiban.class1_13.binarytree.common.Node;

/**
 * 给定一棵二叉树，判断是否为平衡二叉树
 * 平衡二叉树定义：任何一个节点，左子树和右子树高度差不超过1
 */
public class BalancedBinaryTree {
    /**
     * 二叉树递归过程中返回的信息体
     * 每次递归都从左孩子要信息，从右孩子要信息，再根据左右孩子的信息组装自己的信息返回
     */
    public static class Info {
        // 这棵树是否平衡
        public boolean isBalanced;
        // 这棵树高度是多少
        public int height;

        public Info(boolean isBalanced, int height) {
            this.isBalanced = isBalanced;
            this.height = height;
        }
    }

    public boolean isBalanced(Node head) {
        // 虽然最后只用到是否平衡，但是递归过程中判断平衡需要用到高度
        return process(head).isBalanced;
    }

    public Info process(Node head) {
        if (head == null) {
            // 空树认为是平衡二叉树，高度为0
            return new Info(true, 0);
        }

        // 从左右孩子要信息
        Info leftInfo = process(head.left);
        Info rightInfo = process(head.right);

        // 根据左右孩子的信息，组装自己的信息
        // 左树是平衡的，右数也是平衡的，并且左右子树高度差不超过1，当前节点为头的树才是平衡的
        boolean isBalanced = leftInfo.isBalanced && rightInfo.isBalanced && Math.abs(leftInfo.height - rightInfo.height) <= 1;
        // 左右子树中的最大高度+1，是当前节点为头的树的高度
        int height = Math.max(leftInfo.height, rightInfo.height) + 1;
        return new Info(isBalanced, height);
    }
}
