package shuatiban;

import java.util.Scanner;

/**
 * 返回二叉树上满足搜索二叉树性质的、最大子拓扑结构的节点数
 * 从二叉树的某个节点x开始，往下子节点都要的，叫子树；在二叉树上只要能连起来的任何结构，叫子拓扑结构；
 *
 * 思路：
 * 暴力法：遍历每个节点，计算以这个节点为头的最大搜索二叉拓扑（怎么计算？遍历整棵子树），最后取最大值。时间复杂度O(N^2)
 * 更优解：二叉树递归套路，每个节点都从左孩子拿到左子树上的最大搜索拓扑，从右孩子拿到右子树上的最大搜索拓扑，然后加工出以自己为头的最大搜索拓扑，返回Max{左子树上的，右子树上的，以自己为头的}
 * 时间复杂度O(N)，直白讲是O(3N)，因为整体递归遍历一遍，每个节点枚举两次左子树右边界和右子树左边界，而每个节点的左子树右边界都是不重合的，右子树左边界也是，所以总共遍历3遍
 */
public class Code074最大搜索二叉子拓扑 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // 输入总节点数
        int n = sc.nextInt();
        // 输入根节点
        int root = sc.nextInt();
        // 定义一个二维数组用来存储一棵树，用第一维下标来表示节点，第二维下标表示父/左/右（题目告诉我们，节点标号等价于节点值）
        // tree[i][0]表示i节点的父亲，tree[i][1]表示i节点的左孩子，tree[i][2]表示i节点的右孩子
        int[][] tree = new int[n + 1][3];

        for (int i = 0; i < n; i++) {
            int fa = sc.nextInt();
            int lc = sc.nextInt();
            int rc = sc.nextInt();
            // 给fa节点设置左右子树
            tree[fa][1] = lc;
            tree[fa][2] = rc;
            // 给lc、rc节点设置父亲，他们的左右子树会在后面的循环中设置
            tree[lc][0] = fa;
            tree[rc][0] = fa;
        }
        System.out.println(maxBSTTopology(root, tree, new int[n + 1]));
        sc.close();
    }

    /**
     * 求以head为头的子树上，最大的搜索二叉拓扑的大小。注意是以head为头的子树上的拓扑，不是以head为头的拓扑
     * @param head 当前递归的节点
     * @param tree 记录整棵树的结构
     * @param maxTop 记录所有子树上的最大搜索二叉拓扑大小
     * @return
     */
    public static int maxBSTTopology(int head, int[][] tree, int[] maxTop) {
        if (head == 0) {
            return 0;
        }

        int left = tree[head][1];
        // 求左子树上的最大搜索二叉拓扑大小
        int leftMaxTop = maxBSTTopology(left, tree, maxTop);
        // 求以左子树上以左孩子为头且能加入head为头的最大搜索拓扑，也就是在leftMaxTop中剔除掉比head大的
        // 例如head=10，左子树上有个12节点位于左孩子的右子树，对于左孩子来说是合法的，可能被算进最大搜索二叉拓扑中，但是这个12节点对head是不合法的，不能算到head的的最大拓扑中，需要从左子树的最大拓扑中减掉12为头的子树的最大拓扑
        // 顺着左子树的右边界往下找，找到第一个大于等于head的节点，也就是不能加入head的搜索二叉拓扑中的节点，从左子树的最大拓扑中减去这个节点的最大拓扑。
        // 如果还没等找到大于等于head的节点，就碰到最大拓扑为0了，可以提前结束，说明没有不合法的被算进来
        while (left < head && maxTop[left] > 0) {
            left = tree[left][2];
        }
        if (maxTop[left] > 0) {
            maxTop[tree[head][1]] -= maxTop[left];
        }

        // 右子树按照同样的方式求
        int right = tree[head][2];
        int rightMaxTop = maxBSTTopology(right, tree, maxTop);
        while (right > head && maxTop[right] > 0) {
            right = tree[right][1];
        }
        if (maxTop[right] > 0) {
            maxTop[tree[head][2]] -= maxTop[right];
        }

        // 加工出以head为头的最大拓扑，= 以左孩子为头且每个节点都小于head的最大搜索拓扑 + 以右孩子为头且每个节点都大于head的最大搜索拓扑
        maxTop[head] = maxTop[tree[head][1]] + maxTop[tree[head][2]] + 1;
        // 返回Max{左子树上的，右子树上的，以自己为头的}
        return Math.max(Math.max(leftMaxTop, rightMaxTop), maxTop[head]);
    }
}
