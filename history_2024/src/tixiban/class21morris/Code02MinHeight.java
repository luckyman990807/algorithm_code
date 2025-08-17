package tixiban.class21morris;

/**
 * 给定一棵二叉树,求树上叶子节点的最小高度
 * <p>
 * 二叉树递归套路(树形dp):
 * 笔试用,问左右子树分别要高度,返回较小的高度+1
 *
 * Morris遍历:
 * 面试用,
 * cur的左子树最右节点的右指针为空,说明还没到过最右节点,继续遍历,当前curHeight++,
 * cur的左子树最右节点的右指针指向cur,说明cur是从左子树最右节点右指针来到的cur,说明左子树的右边界已经遍历完了,判断最右节点如果是叶子节点,就结算minHeight
 */
public class Code02MinHeight {
    public static class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int value) {
            this.value = value;
        }
    }

    /**
     * 递归法求最小高度
     */
    public static int minHeightRecurrence(Node head) {
        if (head == null) {
            return 0;
        }
        return process(head);
    }

    private static int process(Node head) {
        if (head == null) {
            return 0;
        }
        return 1 + Math.min(process(head.left), process(head.right));
    }

    /**
     * morris遍历求最小高度
     */
    public static int minHeightMorris(Node head) {
        // 记录当前节点cur的高度
        int curHeight = 0;
        // 记录最小高度
        int minHeight = Integer.MAX_VALUE;

        Node cur = head;
        while (cur != null) {
            if (cur.left != null) {
                // 记录左子树右边界高度
                int rightEdgeHeight = 1;
                Node mostRight = cur.left;
                while (mostRight.right != null && mostRight.right != cur) {
                    rightEdgeHeight++;
                    mostRight = mostRight.right;
                }
                if (mostRight.right == cur) {
                    // 如果左子树最右节点的右指针指向当前节点,说明已经遍历到最右节点然后通过右指针回到cur了
                    // 判断如果最右节点是叶子节点,就要结算最小高度
                    if (mostRight.left == null) {
                        minHeight = Math.min(minHeight, curHeight);
                    }
                    // 当前节点高度 = 左子树最右节点(上一节点)高度 - 左子树右边界高度
                    curHeight -= rightEdgeHeight;

                    mostRight.right = null;
                    cur = cur.right;
                } else {
                    // 如果最右节点==null,说明还没有遍历到可能是叶子节点的位置
                    // 当前节点高度 = 上一节点高度 + 1
                    curHeight++;

                    mostRight.right = cur;
                    cur = cur.left;
                }
            } else {
                // 还没有遍历到可能是叶子节点的位置
                // 当前节点高度 = 上一节点高度 + 1
                curHeight++;

                cur = cur.right;
            }
        }

        // 由于整棵树的右边界上的最右节点不会用右指针指向某个祖先,所以会被忽略作为叶子节点结算,这里专门结算一下
        int rightEdgeHeight = 1;
        Node mostRight = head;
        while (mostRight.right != null) {
            rightEdgeHeight++;
            mostRight = mostRight.right;
        }
        if (mostRight.left == null) {
            minHeight = Math.min(minHeight, rightEdgeHeight);
        }

        return minHeight;
    }


    public static void main(String[] args) {
        /**
         *     1
         *    / \
         *   2   3
         *  / \
         * 4   5
         */
        Node head = new Node(1);
        head.left = new Node(2);
        head.right = new Node(3);
//        head.left.left = new Node(4);
//        head.left.right = new Node(5);
        head.right.left = new Node(6);
        head.right.right = new Node(7);
        System.out.println(minHeightMorris(head));
        System.out.println(minHeightMorris(head));
        System.out.println(minHeightRecurrence(head));
    }
}
