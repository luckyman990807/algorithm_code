package tixiban.class1_13.binarytree.treedp;

import tixiban.class1_13.binarytree.common.Node;

/**
 * 给定一棵二叉树，返回最大二叉搜索子树的头节点
 */
public class MaxBSTHead {
    public class Info {
        public Node maxBSTHead;
        public int maxBSTSize;
        public int max;
        public int min;

        public Info(Node maxBSTHead, int maxBSTSize, int max, int min) {
            this.maxBSTHead = maxBSTHead;
            this.maxBSTSize = maxBSTSize;
            this.max = max;
            this.min = min;
        }
    }

    public Info process(Node cur) {
        if (cur == null) {
            return null;
        }

        Info leftInfo = process(cur.left);
        Info rightInfo = process(cur.right);

        // 最大最小值默认都是当前节点的值，如果左右子树不为空，就可以刷新一波
        int max = cur.value;
        int min = cur.value;
        if (leftInfo != null) {
            max = Math.max(max, leftInfo.max);
            min = Math.min(min, leftInfo.min);
        }
        if (rightInfo != null) {
            max = Math.max(max, rightInfo.max);
            min = Math.min(min, rightInfo.min);
        }

        Node maxBSTHead = null;
        int maxBSTSize = 0;
        // 情况1、当前节点就是答案，当前树点就是二叉搜索树
        // 怎么判断左子树是BST：左子树为空，或左子树上的最大二叉搜索子树头节点就是左孩子本身
        boolean leftBST = leftInfo == null || leftInfo.maxBSTHead == cur.left;
        boolean rightBST = rightInfo == null || rightInfo.maxBSTHead == cur.right;
        if (leftBST && rightBST) {
            // 如果左右子树都是BST
            boolean moreLeftMax = leftInfo == null || cur.value > leftInfo.max;
            boolean lessRightMin = rightInfo == null || cur.value < rightInfo.min;
            if (moreLeftMax && lessRightMin) {
                // 如果当前节点比左子树最大值还大、比右子树最小值还小
                // 怎么获取左子树的size：既然走到这了，左子树要么是空，要么是不空的搜索二叉树，后者左子树的size=左子树的maxBSTSize
                int leftSize = leftInfo == null ? 0 : leftInfo.maxBSTSize;
                int rightSize = rightInfo == null ? 0 : rightInfo.maxBSTSize;
                // 当前节点就是答案，当前树的大小就是答案大小
                maxBSTSize = leftSize + rightSize + 1;
                maxBSTHead = cur;
            }
        }

        if (maxBSTHead == null) {
            // 情况2、当前节点不是答案，左子树已经有答案了
            if (leftInfo != null && leftInfo.maxBSTHead != null) {
                maxBSTHead = leftInfo.maxBSTHead;
                maxBSTSize = leftInfo.maxBSTSize;
            }
            // 情况3、当前节点不是答案，右子树已经有答案了
            if (rightInfo != null && rightInfo.maxBSTHead != null) {
                maxBSTHead = rightInfo.maxBSTHead;
                maxBSTSize = rightInfo.maxBSTSize;
            }
        }

        return new Info(maxBSTHead, maxBSTSize, max, min);
    }

    public Node getMaxBSTHead(Node head) {
        if (head == null) {
            return null;
        }
        return process(head).maxBSTHead;
    }
}
