package tixiban.class1_13.binarytree.treedp;

import tixiban.class1_13.binarytree.common.Node;

/**
 * 给定一棵二叉树，判断是不是满二叉树
 * 满二叉树定义：如果高度是h，节点数一定是2^h-1个
 */
public class FullBinaryTree {
    public static class Info {
        // 当前树的高度
        public int height;
        // 当前树的节点数
        public int nodes;

        public Info(int height, int nodes) {
            this.height = height;
            this.nodes = nodes;
        }
    }

    /**
     * 递归计算一棵树的高度和节点数，最后只要验证节点数是否等于2^h-1即可证明
     */
    public Info process(Node head) {
        if (head == null) {
            // 如果当前树是空树，那么高度是0，节点数也是0
            return new Info(0, 0);
        }

        // 从左右节点拿信息
        Info leftInfo = process(head.left);
        Info rightInfo = process(head.right);

        // 当前树的高度=左右子树更高的那个+1
        int height = Math.max(leftInfo.height, rightInfo.height) + 1;
        // 当前树的节点数=左子树节点数+右子树节点数+1
        int nodes = leftInfo.nodes + rightInfo.nodes + 1;

        // 返回当前树的信息
        return new Info(height, nodes);
    }

    public boolean isFullBinaryTree(Node head) {
        Info info = process(head);
        // 2^h = 1*2^h = 1<<h
        return 1 << info.height - 1 == info.nodes;
    }
}
