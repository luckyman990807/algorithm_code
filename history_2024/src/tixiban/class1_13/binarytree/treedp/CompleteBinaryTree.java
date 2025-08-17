package tixiban.class1_13.binarytree.treedp;

import tixiban.class1_13.binarytree.common.Node;

/**
 * 给定一棵二叉树，判断是否为完全二叉树。用二叉树递归套路解决
 * 难点是要列的可能性太多
 * 完全二叉树定义：要么是满二叉树，即便不满也是最后一层不满，而且是依次变满的状态
 */
public class CompleteBinaryTree {
    public static class Info {
        // 当前树是不是完全二叉树
        public boolean isCBT;
        // 当前树是不是满二叉树
        public boolean isFull;
        // 当前树的高度
        public int height;

        public Info(boolean isCBT, boolean isFull, int height) {
            this.isCBT = isCBT;
            this.isFull = isFull;
            this.height = height;
        }
    }

    public Info process(Node head) {
        if (head == null) {
            // 空树是完全二叉树，空树是满二叉树，空树高度是0
            return new Info(true, true, 0);
        }

        Info leftInfo = process(head.left);
        Info rightInfo = process(head.right);

        // 当前树的高度=左右子树中最高的那个+1
        int height = Math.max(leftInfo.height, rightInfo.height) + 1;
        // 左树是满的，并且右树是满的，并且左树高度==右树高度
        boolean isFull = leftInfo.isFull && rightInfo.isFull && leftInfo.height == rightInfo.height;
        // 如果是完全二叉树，有4种可能性，最后一层节点依次变满：
        boolean isCBT = false;
        if (isFull) {
            // 可能性1、当前树是满二叉树，那么一定是完全二叉树
            isCBT = true;
        } else if (leftInfo.isCBT && rightInfo.isFull && leftInfo.height == rightInfo.height + 1) {
            // 可能性2、左子树是完全二叉树，右子树是满二叉树，且左子树高度=右子树高度+1
            isCBT = true;
        } else if (leftInfo.isFull && rightInfo.isFull && leftInfo.height == rightInfo.height + 1) {
            // 可能性3、左子树是满二叉树，右子树也是满二叉树，且左子树高度=右子树高度+1
            isCBT = true;
        } else if (leftInfo.isFull && rightInfo.isCBT && leftInfo.height == rightInfo.height) {
            // 可能性4、左子树是满二叉树，右子树是完全二叉树，且左子树高度==右子树高度
            isCBT = true;
        }

        return new Info(isCBT, isFull, height);
    }
}
