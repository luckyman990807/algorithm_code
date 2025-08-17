package tixiban.class1_13.binarytree.treedp;

import tixiban.class1_13.binarytree.common.Node;

/**
 * 给定一棵二叉树，求树上的最大距离
 * 距离的定义：从一个节点到另一个节点，经过的节点个数。途径的每个节点只能出现一次
 * 二叉树上任意两个节点都有距离，而且是唯一的
 */
public class MaxDistance {
    public static class Info {
        // 当前子树上的最大距离
        public int maxDist;
        // 当前子树的高度
        public int height;

        public Info(int maxDist, int height) {
            this.maxDist = maxDist;
            this.height = height;
        }
    }

    /**
     * 思路：
     * 以head为头节点的树，最大距离可以分为两种情况：
     * 1、不经过head。那就是左子树上的最大距离，或者右子树上的最大距离
     * 2、经过head。那就是左子树的高度 + 右子树的高度 + 1
     */
    public Info process(Node head) {
        if (head == null) {
            // 如果一个子树为空，那么最大距离是0， 高度也是0
            return new Info(0, 0);
        }

        // 从左右子树获取信息
        Info leftInfo = process(head.left);
        Info rightInfo = process(head.right);

        // 当前树的最大距离，从以下3个里面取最大的：左子树上的最大距离，右子树上的最大距离，左子树高度+右子树高度+1
        int maxDist = Math.max(
                Math.max(leftInfo.maxDist, rightInfo.maxDist),
                leftInfo.height + rightInfo.height + 1);
        // 当前树的高度=左右子树更高的那个+1
        int height = Math.max(leftInfo.height, rightInfo.height) + 1;

        // 返回当前树（或者说当前节点）的信息
        return new Info(maxDist, height);
    }

    public int getMaxDistance(Node head) {
        return process(head).maxDist;
    }
}
