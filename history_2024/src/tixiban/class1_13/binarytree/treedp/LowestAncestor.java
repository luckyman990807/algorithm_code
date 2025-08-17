package tixiban.class1_13.binarytree.treedp;

import tixiban.class1_13.binarytree.common.Node;

/**
 * 给定一棵二叉树，和两个节点，返回两个节点在二叉树上的最低公共祖先
 * 如果其中一个是另一个的祖先，就返回这个祖先
 * 如果在这棵树上找不到全部的这两个节点，返回null
 */
public class LowestAncestor {
    public static class Info {
        // 这棵树上有没有找到a
        public boolean findA;
        // 这棵树上有没有找到b
        public boolean findB;
        // 这棵树上有没有找到答案（即ab的最低公共祖先），找到，result就是答案，没找到，result是null
        public Node result;

        public Info(boolean findA, boolean findB, Node result) {
            this.findA = findA;
            this.findB = findB;
            this.result = result;
        }
    }

    public Info process(Node cur, Node a, Node b) {
        if (cur == null) {
            // 如果是空树，那么没有找到a，没有找到b，没有找到答案
            return new Info(false, false, null);
        }

        Info leftInfo = process(cur.left, a, b);
        Info rightInfo = process(cur.right, a, b);

        boolean findA = cur == a || leftInfo.findA || rightInfo.findA;
        boolean findB = cur == b || leftInfo.findB || rightInfo.findB;

        Node result = null;
        if (leftInfo.result != null) {
            // 可能性1、当前节点不是答案，答案在左子树上
            result = leftInfo.result;
        } else if (rightInfo.result != null) {
            // 可能性2、当前节点不是答案，答案在右子树上
            result = rightInfo.result;
        } else if (findA && findB) {
            // 可能性3、当前节点是答案，当前节点就是a，左右子树上找到了b
            // 可能性4、当前节点是答案，当前节点就是b，左右子树上找到了a
            // 可能性5、当前节点是答案，左右子树分别有a、b其中一个
            result = cur;
        }

        return new Info(findA, findB, result);
    }

    public Node getLowestAncestor(Node head, Node a, Node b) {
        return process(head, a, b).result;
    }
}
