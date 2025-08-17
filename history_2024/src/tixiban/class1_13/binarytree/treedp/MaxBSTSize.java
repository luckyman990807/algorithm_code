package tixiban.class1_13.binarytree.treedp;

import tixiban.class1_13.binarytree.common.Node;

/**
 * 给定一棵二叉树，求树上最大二叉搜索子树的节点数
 * <p>
 * 以根节点作为头的子树，就是当前树自己
 * 以x节点作为头的子树，必须包含x以及x下面的所有分支，不能只要左树不要右树了，那不叫子树那叫拓扑结构
 * <p>
 * 思路：
 * 确认从子树要哪些信息，然后递归。要哪些信息要列出所有可能性，所有可能性涉及到的所有信息就是要准备的信息。
 * 1、自己就是BST，返回当前树的节点数：
 * 判断是BST，要用到isBST，max，min，返回节点数，要用到treeSize
 * 2、自己不是BST，返回左右子树中最大二叉搜索子树节点数最大的那个
 * 用到maxBSTSize
 */
public class MaxBSTSize {
    /**
     * Info为什么不用isBST信息？
     * 因为当maxBSTSize=treeSize时，也能证明是BST
     */
    public static class Info {
        // 当前树上的最大二叉搜索子树的节点数
        public int maxBSTSize;
        // 当前树的节点数
        public int treeSize;
        // 当前树上的最大值
        public int max;
        // 当前树上的最小值
        public int min;

        public Info(int maxBSTSize, int treeSize, int max, int min) {
            this.maxBSTSize = maxBSTSize;
            this.treeSize = treeSize;
            this.max = max;
            this.min = min;
        }
    }

    public Info process(Node head) {
        if (head == null) {
            // 空树无法定义max和min，因此返回空
            return null;
        }

        Info leftInfo = process(head.left);
        Info rightInfo = process(head.right);

        // 当前树的信息，默认值
        int max = 0;
        int min = 0;
        // 默认只有当前节点
        int treeSize = 1;

        if (leftInfo != null) {
            // 如果左子树不为空，信息就可以刷新一波
            max = leftInfo.max;
            min = leftInfo.min;
            treeSize += leftInfo.treeSize;
        }
        if (rightInfo != null) {
            // 如果右子树不为空，信息又可以刷新一波
            max = rightInfo.max;
            min = rightInfo.min;
            treeSize += rightInfo.treeSize;
        }

        // 最大二叉搜索子树大小，这个信息，默认为1，因为最差就是只有叶子是BST，大小为1
        // 列出所有的情况，取最大值
        // 情况1、当前子树不是BST，返回左子树的最大二叉搜索树大小
        int possible1 = 1;
        if (leftInfo != null) {
            possible1 = leftInfo.maxBSTSize;
        }
        // 情况2、当前子树不是BST，返回右子树的最大二叉搜索树大小
        int possible2 = 1;
        if (rightInfo != null) {
            possible2 = rightInfo.maxBSTSize;
        }
        // 情况3、当前子树是BST（左子树是BST，右子树是BST，比左子树最大值大，比右子树最小值小），返回当前子树大小
        int possible3 = 1;
        boolean leftIsBST = leftInfo == null || (leftInfo.maxBSTSize == leftInfo.treeSize);
        boolean rightIsBST = rightInfo == null || (rightInfo.maxBSTSize == rightInfo.treeSize);
        if (leftIsBST && rightIsBST) {
            // 左右子树都是BST
            boolean moreLeftMax = leftInfo == null || head.value > leftInfo.max;
            boolean lessRightMin = rightInfo == null || head.value < rightInfo.min;
            if (moreLeftMax && lessRightMin) {
                // 当前子树是BST
                int leftSize = leftInfo == null ? 0 : leftInfo.treeSize;
                int rightSize = rightInfo == null ? 0 : rightInfo.treeSize;
                // baseCase就是左右子树都为空，这里返回1，也就是说maxBSTSize最小值为1
                possible3 = leftSize + rightSize + 1;
            }
        }

        int maxBSTSize = Math.max(Math.max(possible1, possible2), possible3);

        return new Info(maxBSTSize, treeSize, max, min);
    }


    public int getMaxBSTSize(Node head) {
        if (head == null) {
            return 0;
        }
        return process(head).maxBSTSize;
    }
}
