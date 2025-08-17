package shuatiban;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode.com/problems/recover-binary-search-tree/
 * 给定一个棵搜索二叉树的头节点head，其中有两个节点错了，交换过来就能让整棵树重新变成搜索二叉树，怎么找到并调整正确？
 *
 * 思路：中序遍历出现逆序的节点就是错误节点。但是中序遍历中有可能出现一个逆序对也可能出现两个逆序对，针对两种情况通用的判断标准是：
 * 第一个逆序对的第一个节点，是第一个错误节点，最后一个逆序对的第二个节点，是第二个错误节点
 *
 * leetcode进阶：空间复杂度O(1)的解法？——Morris遍历代替递归遍历即可
 */
public class Code073搜索二叉树把两个错位的节点调整正确 {
    public void recoverTree(TreeNode root) {
        List<TreeNode> list = new ArrayList<>();
        process(root, list);
        TreeNode err1 = null;
        TreeNode err2 = null;
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).val > list.get(i + 1).val) {
                if (err1 == null) {
                    err1 = list.get(i);
                }
                err2 = list.get(i + 1);
            }
        }
        int temp = err1.val;
        err1.val = err2.val;
        err2.val = temp;
    }

    public static void process(TreeNode cur, List<TreeNode> list) {
        if (cur == null) {
            return;
        }
        process(cur.left, list);
        list.add(cur);
        process(cur.right, list);
    }

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
}
