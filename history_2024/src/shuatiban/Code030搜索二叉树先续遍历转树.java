package shuatiban;

import java.util.Stack;

/**
 * 本题测试链接 : https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/
 * 已知一棵搜索二叉树上没有重复值的节点，现在有一个数组arr，是这棵搜索二叉树先序遍历的结果，请根据arr生成整棵树并返回头节点
 */
public class Code030搜索二叉树先续遍历转树 {
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode() {
        }

        public TreeNode(int val) {
            this.val = val;
        }

        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    public static TreeNode bstFromPreorder1(int[] preorder) {
        return process1(preorder, 0, preorder.length - 1);
    }

    public static TreeNode process1(int[] preorder, int left, int right) {
        if (left > right) {
            return null;
        }
        TreeNode head = new TreeNode(preorder[left]);
        int firstBig = left + 1;
        for (; firstBig <= right; firstBig++) {
            if (preorder[firstBig] > preorder[left]) {
                break;
            }
        }
        head.left = process1(preorder, left + 1, firstBig - 1);
        head.right = process1(preorder, firstBig, right);
        return head;
    }

    /**
     * 单调栈优化
     * 每次都要遍历找到第一个比头节点大的节点,可以用单调栈优化.
     * 单调栈就是用来求: 每个数左边小于他离他最近的数、每个数右边小于他离他最近的数、每个数左边大于他离他最近的数、每个数右边大于他离他最近的数...
     */
    public static TreeNode bstFromPreorder(int[] preorder) {
        int[] rightFirstBig = new int[preorder.length];
        for (int i = 0; i < rightFirstBig.length; i++) {
            rightFirstBig[i] = -1;
        }

        // 单调栈求每个数右边第一个大于他的数
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < preorder.length; i++) {
            // 只要新来的数比栈顶元素大,那么栈顶元素右边第一个大于他的数就是新来的数.下一个栈顶元素也同样判断.
            while (!stack.empty() && preorder[stack.peek()] < preorder[i]) {
                rightFirstBig[stack.pop()] = i;
            }
            stack.push(i);
        }

        return process(preorder, 0, preorder.length - 1, rightFirstBig);
    }

    public static TreeNode process(int[] preorder, int left, int right, int[] rightFirstBig) {
        if (left > right) {
            return null;
        }
        TreeNode head = new TreeNode(preorder[left]);
        // 如果右边第一个大于他的数不存在,或者这个数超过了当前的右边界,就统一设为right+1,下次递归肯定会走到出口.
        int nearBig = rightFirstBig[left] == -1 || rightFirstBig[left] > right ? right + 1 : rightFirstBig[left];
        head.left = process(preorder, left + 1, nearBig - 1, rightFirstBig);
        head.right = process(preorder, nearBig, right, rightFirstBig);
        return head;
    }
}
