package tixiban.class21morris;

/**
 * Morris遍历
 * 一种二叉树遍历方式,时间复杂度O(N),空间复杂度O(1)
 * 原理:利用二叉树中大量空闲指针,达到节省空间的目的
 * 解决的问题:用于在内存空间较小的环境遍历二叉树
 * 递归遍历(用系统栈)和迭代遍历(用自己申请的栈)的空间复杂度都是O(logN)
 * <p>
 * Morris遍历细节
 * 当前节点cur,cur初始值为二叉树根节点
 * 1、如果cur没有左孩子,cur向右移动cur=cur.right
 * 2、如果cur有左孩子,找到左子树上的最右节点mostRight,
 * a.如果最右节点mostRight的右指针指向空,让其指向cur,然后cur向左移动cur=cur.left
 * b.如果最右节点mostRight的右指针指向cur,让其指向空,然后cur向右移动cur=cur.right
 * 3、cur为空时停止遍历
 * <p>
 * <p>
 * 问:Morris遍历每个节点都要走一遍左子树右边界,是否影响时间复杂度?
 * 不影响,画图走一遍就知道,每个节点的左子树右边界都是不重叠的,也就是说所有节点的左子树右边界加起来也不会超过N,时间复杂度依然O(N)
 * <p>
 * <p>
 * 可以看出morris的大致流程也是来到一个节点后,先尝试走左边,再走右边.
 * <p>
 * 本质就是利用cur左子树的最右节点标记这时第几次来到cur,最右节点指向空,说明这是第一次来到cur,最右节点指向cur,说明这是遍历完左子树后第二次来到cur.
 * 跟递归遍历是一样的效果,作者在利用二叉树中空闲的指针来致敬递归序:
 * // 第一次到达该节点
 * // 遍历左子树
 * process(cur.left);
 * // 第二次到达该节点
 * // 遍历右子树
 * process(cur.right);
 * // 第三次到达该节点(Morris中没有这个时刻)
 *
 *
 * 注意:
 * Morris在面试场上的用处就是装逼,面试问到二叉树遍历相关的题目可以用Morris优化空间,
 * 笔试的时候不需要用Morris,因为逻辑比较复杂,直接用最直白的递归遍历
 */
public class Code01Morris {
    public static class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int value) {
            this.value = value;
        }
    }

    /**
     * morris遍历
     * 生成的Morris序的特点是:头节点在先序和中序的位置各出现一次
     * 即:总流程是先左再右,头节点在左之前和左右之间各出现一次
     * 例如二叉树
     *     1
     *    / \
     *   2   3
     *  / \
     * 4   5
     * morris序:1 2 4 2 5 1 3
     * 先序:    1 2 4 5 3
     * 中序:    4 2 5 1 3
     * 后序:    4 5 2 3 1
     */
    public static void morris(Node head) {
        // 当前节点从头节点开始
        Node cur = head;
        while (cur != null) {
            // morris序打印
            System.out.print(cur.value + " ");
            if (cur.left != null) {
                // 左子树不为空就尝试往左走
                // 先找出左子树的最右节点
                Node mostRight = cur.left;
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                // 走到这只有两种情况,1.最右节点右指针指向空;2.最右指针右孩子指向cur
                if (mostRight.right == null) {
                    // 指向空说明这是第一次到达cur,标记以下已经来过了,然后遍历左子树
                    mostRight.right = cur;
                    cur = cur.left;
                } else {
                    // 指向cur说明这是第二次来到cur,把标记改回去,第一次已经遍历左子树了,这一次要遍历右子树
                    mostRight.right = null;
                    cur = cur.right;
                }
            } else {
                // 左子树为空就直接往右走
                cur = cur.right;
            }
        }
        System.out.println();
    }

    /**
     * morris先序遍历
     * 思路:
     * 如果没有左树,那么cur只会来到自己一次,那么来到的时候就打印
     * 如果有左树,那么cur会来到自己两次,那么第一次来到时打印就是先序
     */
    public static void morrisPre(Node head) {
        Node cur = head;
        while (cur != null) {
            if (cur.left != null) {
                // 如果有左树
                Node mostRight = cur.left;
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    // 第一次来到cur,这时打印就是先序
//                    System.out.print(cur.value+" ");
                    mostRight.right = cur;
                    cur = cur.left;
                } else {
                    // 第二次来到cur,这时打印就是中序
                    System.out.print(cur.value + " ");
                    mostRight.right = null;
                    cur = cur.right;
                }
            } else {
                // 如果没有左树
                // 来到就打印,就是先序
                System.out.print(cur.value + " ");
                cur = cur.right;
            }
        }
        System.out.println();
    }

    /**
     * morris后序遍历
     * 思路:
     * 每当一个节点来到自己第二遍的时候,逆序打印左子树的右边界.最后逆序打印整棵树的右边界
     * 怎么逆序打印?
     * 单链表反转,把右指针指向自己的父节点.
     * 当然不能用栈,因为morris的意义就是空间复杂度O(1),用栈就失去Morris的意义了
     *
     * @param head
     */
    public static void morrisPost(Node head) {
        Node cur = head;
        while (cur != null) {
            if (cur.left != null) {
                Node mostRight = cur.left;
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                if (mostRight.right == null) {
                    mostRight.right = cur;
                    cur = cur.left;
                } else {
                    mostRight.right = null;
                    // 来到自己第二遍的时候,逆序打印左子树右边界
                    rightEdgeDesc(cur.left);
                    cur = cur.right;
                }
            } else {
                cur = cur.right;
            }
        }
        // 最后逆序打印整棵树右边界
        rightEdgeDesc(head);
        System.out.println();
    }

    /**
     * 逆序打印一棵树的右边界
     */
    public static void rightEdgeDesc(Node cur) {
        if (cur == null) {
            return;
        }
        // 先反转
        Node tail = reverse(cur);
        // 再打印
        Node newCur = tail;
        while (newCur != null) {
            System.out.print(newCur.value+" ");
            newCur=newCur.right;
        }
        // 最后反转回去
        reverse(tail);
    }

    /**
     * 单链表反转
     */
    public static Node reverse(Node cur) {
        Node pre = null;
        Node next = null;
        while (cur != null) {
            next = cur.right;
            cur.right = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }

    public static void main(String[] args) {
        Node head = new Node(1);
        head.left = new Node(2);
        head.right = new Node(3);
        head.left.left = new Node(4);
        head.left.right = new Node(5);
        morris(head);
        morrisPre(head);
        morrisPost(head);
    }
}
