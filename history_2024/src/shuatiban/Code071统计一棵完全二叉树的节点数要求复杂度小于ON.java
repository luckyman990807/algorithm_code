package shuatiban;

/**
 * https://www.nowcoder.com/practice/e13bceaca5b14860b83cbcc4912c5d4a
 * 给定一个棵完全二叉树，返回这棵树的节点个数，要求时间复杂度小于O(树的节点数)
 * <p>
 * 思路：
 * 来到cur节点，找到右子树的最左节点，
 * 可能性1、如果最左节点的高度=总高度，说明cur节点的左子树是满二叉树，直接公式求左子树的节点数，加上cur一个，加上递归求右子树节点数。
 * 可能性2、否则（最左节点的高度<总高度），说明cur节点的右子树是满二叉树，直接公式求右子树的节点数，加上cur一个，加上递归求左子树的节点数。
 */
public class Code071统计一棵完全二叉树的节点数要求复杂度小于ON {

    public static int getNodeNum(Node root) {
        return process(root, 1, getLeftestLevel(root, 1));
    }

    /**
     * 递归求以cur节点为头的子树的节点数
     *
     * @param cur      一棵子树的头节点
     * @param level    cur节点的高度
     * @param maxLevel 整棵树的高度
     * @return
     */
    public static int process(Node cur, int level, int maxLevel) {
        if (level == maxLevel) {
            return 1;
        }
        if (getLeftestLevel(cur.right, level + 1) == maxLevel) {
            // 如果当前节点右子树的高度=整棵树的高度，说明当前节点左子树是满二叉树，那么左子树的节点数可以用公示求，右子树的节点数递归求，再加上当前节点，就得到当前子树的节点个数
            return ((1 << maxLevel - level) - 1) + process(cur.right, level + 1, maxLevel) + 1;
        } else {
            // 如果当前节点右子树的高度不等于整棵树的高度，说明右子树是满二叉树，那么右子树的节点数可以用公示求，左子树的节点数递归求，再加上当前节点，就得到当前子树的节点数
            // 右子树为空的情况也可以涵盖
            return ((1 << maxLevel - level - 1) - 1) + process(cur.left, level + 1, maxLevel) + 1;
        }

    }

    /**
     * 给定一个节点，查询以该节点为头的子树的最左节点的高度，也就是当前子树的高度（完全二叉树的高度就是最左节点的高度）
     *
     * @param cur
     * @param level
     * @return
     */
    public static int getLeftestLevel(Node cur, int level) {
        // 只要左子树有路就往左子树走，同时level++
        while (cur != null) {
            level++;
            cur = cur.left;
        }
        // 因为while循环最后一次肯定会多加一次，所以结果要减掉一次
        return level - 1;
    }

    public static class Node {
        int value;
        Node left;
        Node right;

        public Node(int value) {
            this.value = value;
        }
    }

    public static void main(String[] args) {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.left = new Node(6);
        System.out.println(getNodeNum(root));
    }
}
