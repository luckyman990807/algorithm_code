package tixiban.class1_13.binarytree.treedp;

import tixiban.class1_13.binarytree.common.Node;

/**
 * 给定一棵二叉树，判断是否是搜索二叉树
 * 搜索二叉树定义：任意一个节点，左子树全都比他小，右子树全都比他大
 * 经典搜索二叉树上没有重复值，如果只有value，那么重复的value就对应一个节点，如果是key:value，那么通过节点的一个key和value列表实现
 */
public class BinarySearchTree {
    public static class Info {
        // 这棵树是否是搜索二叉树
        public boolean isBST;
        // 这棵树上的最大值
        public int max;
        // 这棵树上的最小值
        public int min;

        public Info(boolean isBST, int max, int min) {
            this.isBST = isBST;
            this.max = max;
            this.min = min;
        }
    }

    public boolean isBST(Node head) {
        // 因为process返回的信息可能是空，因此要提前判空
        if (head == null) {
            return true;
        }
        return process(head).isBST;
    }

    public Info process(Node head) {
        if (head == null) {
            // 如果节点为空，不返回任何信息
            // 因为节点为空无法定义最大最小值
            return null;
        }

        // 从左右孩子获取信息
        Info leftInfo = process(head.left);
        Info rightInfo = process(head.right);

        // 是否搜索二叉树，默认为是（如果当前节点没有左右孩子，那么这个节点是搜索二叉树）
        boolean isBST = true;
        // 最大值，默认为当前节点的value（如果当前节点没有左右孩子，那么当前节点的value就是当前节点为头的子树的最大值）
        int max = head.value;
        // 最小值，逻辑同最大值
        int min = head.value;

        // 如果有左子树
        if (leftInfo != null) {
            // 最大值要和左子树的最大值比一比
            max = Math.max(max, leftInfo.max);
            // 最小值也要和左子树的最小值比一比
            min = Math.min(min, leftInfo.min);
            // 左子树是搜索二叉树，并且左子树的最大值比当前节点的值小，当前节点才是搜索二叉树
            isBST = leftInfo.isBST && leftInfo.max < head.value;
        }
        // 如果有右子树
        if (rightInfo != null) {
            // 最大值再和右子树比一比
            max = Math.max(max, rightInfo.max);
            min = Math.min(min, rightInfo.min);
            // 右子树也是搜索二叉树，并且右子树的最小值比当前节点的值大，当前节点才是搜索二叉树
            isBST = rightInfo.isBST && rightInfo.min > head.value;
        }
        // 返回当前节点信息
        return new Info(isBST, max, min);
    }
}
