package shuatiban;

/**
 * 阿里面试题
 * 如果一个节点X，它左树结构和右树结构完全一样，那么我们说以X为头的树是相等树，给定一棵二叉树的头节点head，返回head整棵树上有多少棵相等子树
 *
 * 思路:看到常规二叉树问题,就要想: 二叉树的递归套路: 从左孩子获取信息,从右孩子获取信息,加工出自己的信息,返回给父节点.
 * 当前节点上有几棵相等子树 = 左子树上有几棵相等子树 + 右子树有几棵相等子树 + (自己左右子树相等 ? 1 : 0)
 */
public class Code031返回有几棵相等子树 {
    public static class Node {
        int value;
        Node left;
        Node right;

        public Node(int value) {
            this.value = value;
        }
    }

    /**
     * 递归计算一棵树上有几棵相等子树
     * 复杂度:O(N*logN),根据Master公式可以算.
     * Master公式:
     * 如果递归函数的每个子问题规模相等,则有T(N) = a*(N/b) + O(N^c), a,b,c是常数
     * 那么时间复杂度
     * 1.若以b为底a的对数log(a,b) < c,则T(N) = O(N^c)
     * 2.若log(a,b) > c,则T(N) = O(N^log(a,b))
     * 3.若log(a,b) = c,则T(N) = O(N^c * logN)
     * 这个算法T(N) = 2*T(N/2) + O(N),由Master公式得T(N) = O(N * logN)
     */
    public static int howManySame(Node head) {
        if (head == null) {
            return 0;
        }
        // 左子树上有几棵相等子树 + 右子树上有几棵相等子树 + 自己是不是一棵相等子树(自己的左子树跟右子树是不是相等)
        return howManySame(head.left) + howManySame(head.right) + (isSame(head.left, head.right) ? 1 : 0);
    }

    /**
     * 递归判断两棵树是否相等
     * 复杂度:O(N),判断两棵树是否相等实际上就要把两棵树的所有节点比较一遍
     */
    public static boolean isSame(Node head1, Node head2) {
        // 两棵树都是空,相等
        if (head1 == null && head2 == null) {
            return true;
        }
        // 一棵树是空,另一棵不是空,不相等
        if (head1 == null ^ head2 == null) {
            return false;
        }
        // 两棵树都不是空的情况下,判断两棵树的头节点、左子树、右子树都是相等的
        return head1.value == head2.value && isSame(head1.left, head2.left) && isSame(head1.right, head2.right);
    }


    /**
     * 哈希优化
     * 思路:isSame函数判断两棵树是否相等是O(N),如果判断两树相等能优化到O(1),那么整体的复杂度就能优化到O(N),即遍历所有节点就能算出由多少个相等子树.
     * 怎么把判断两树相等优化到O(1)?
     * 把两树分别序列化,再把序列化的结果用哈希计算,得到的就是固定长度的字符串,比较两个固定长度的字符串是O(1),另外哈希函数也是O(1),虽然常数时间有点大.
     */
    public static class Info {
        // 当前子树上有几棵相等子树(当前子过程的答案)
        int sames;
        // 当前子树的序列化字符串
        String str;

        public Info(int sames, String str) {
            this.sames = sames;
            this.str = str;
        }
    }

    public static Info process(Node head) {
        if (head == null) {
            return new Info(0, "#");
        }
        Info leftInfo = process(head.left);
        Info rigthInfo = process(head.right);
        String str = head.value + leftInfo.str + rigthInfo.str;
        int sames = leftInfo.sames + rigthInfo.sames + (leftInfo.str.hashCode() == rigthInfo.str.hashCode() ? 1 : 0);
        return new Info(sames, str);
    }

    public static int howManySameTree(Node head) {
        return process(head).sames;
    }

}
