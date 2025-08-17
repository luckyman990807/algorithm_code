package tixiban.class22segmenttree;

/**
 * 线段树
 * 解决的问题:区间上的统一增加、区间上的统一修改、区间上的累加和查询,复杂度O(logN)
 * 暴力解法:遍历区间,复杂度O(N)
 * 例如,数组arr下标为0~1000,要求
 * 1.从1到200每个数加20,
 * 2.从5到50每个数都改为10,
 * 3.查询3到100区间内上的累加和.
 * <p>
 * 累加和树:
 * 叶子节点是原数组中的数,每个父节点=左孩子+右孩子
 * 例如:
 *      28
 *    /   \
 *   11    17
 *  / \   / \
 * 6   5 2  15
 *         / \
 *        8   7
 * 原数组长度为奇数时,左边多一个和右边多一个都无所谓
 *
 *
 * 线段树的适用范围:
 * 如果知道左半边的答案、知道右半边的答案,能通过o(1)的简单加工得出整体的答案,才能用线段树.
 * 什么时候不能用线段树?举个例子:查询l到r范围上的众数.因为由左半边的众数和右半边的众数无法加工出整体的众数,整体的众数可能在左半边不是众数,在右半边也不是众数.
 */
public class Code01SegmentTree {
    public static class SegmentTree {
        // 线段树中的数组下标都从1开始，是约定俗成的，大概率是因为用数组描述二叉树时，如果下标从1开始，计算左孩子、右孩子、父亲的公式更简单
        // 左孩子：2*i，右孩子：2*1+1，父亲：i/2

        // 原数组，因为这道题需要求累加和，所以要用到原数组
        int[] arr;

        // 累加和树,用来脑补某一区间上的累加和信息.是用数组表示的一棵二叉树，叶子节点是原数组中的元素，非节点=左孩子+右孩子。懒更新
        int[] lazySumTree;

        // 加树,用来脑补某一区间上的加任务（区间范围内每个元素都加某值）,是用数组表示的一棵二叉树。懒更新
        int[] lazyAddTree;

        // 更新树,用来脑补某一区间上的更新任务（区间范围内每个元素都更新成某值），是用数组表示的一棵二叉树。懒更新
        int[] lazyUpdateTree;
        // 更新标识,因为update[i]=0无法区分是没有更新值还是更新值=0,所以只能另外找个数组标识一下
        boolean[] updateFlag;

        /**
         * 构造函数,初始化线段树
         * 为什么懒数组的长度要=4*n?
         * 因为把树转成数组要:1.把树补空节点补成满二叉树 2.宽度优先遍历. 这种结构最费空间的情况是最后一层只有最后一个节点不空,其他节点都空.因为这样要补最多的空节点.(实际对于累加树的话,最差是最后一层倒数两个节点不为空)
         * 假设原始数据个数为n,那么形成的累加树的叶子节点个数就是n,按照最差情况,要补2*(n-1)+1个空节点,形成完全二叉树,这棵树上有4n-1个节点,所以为了能装下申请4n长度的数组.
         * 问:4n差不多正好装下,那最后一个节点获取孩子节点要乘2,不会越界吗?
         * 答:不会,因为不管是查询还是修改,都是基于原数组的l到r范围操作的,也就是基于某一范围的叶子节点操作,那么head永远是范围内叶子节点的祖先,不可能遍历到没有孩子的head.
         *   1
         *  / \
         * 1   1
         *      \
         *       1
         */
        public SegmentTree(int[] originArr) {
            int n = originArr.length + 1;

            arr = new int[n];
            for (int i = 1; i < n; i++) {
                arr[i] = originArr[i - 1];
            }
            lazySumTree = new int[n << 2];
            lazyAddTree = new int[n << 2];
            lazyUpdateTree = new int[n << 2];
            updateFlag = new boolean[n << 2];

            // 初始化累加和树
            initSum(1, n - 1, 1);
        }

        /**
         * 初始化sum树,累加和树
         * 把arr从l到r范围上的数落到sum上以head为头的子树上
         */
        public void initSum(int l, int r, int head) {
            // 如果当前l到r范围就一个数,那么累加和就等于这个数
            if (l == r) {
                lazySumTree[head] = arr[l];
                return;
            }

            // 中点
            int mid = (l + r) >> 1;
            // 把左半个范围落到左子树,右半个范围落到右子树
            initSum(l, mid, head << 1);
            initSum(mid + 1, r, head << 1 | 1);
            // 头节点的累加和=左节点累加和+右节点累加和
            lazySumTree[head] = lazySumTree[head << 1] + lazySumTree[head << 1 | 1];
        }

        /**
         * add任务,arr数组从l到r范围每个数都加value
         * 当前任务下发到sum树的head节点,head节点表示rangeL到rangeR范围的累加和
         * 递归
         */
        public void add(int taskL, int taskR, int value, int head, int rangeL, int rangeR) {
            // base case:如果任务范围把head对应累加和的范围全包了,就说明自己范围上全部元素都加value即可,那么直接把这个任务记录懒信息,不需要往下拆分
            if (taskL <= rangeL && taskR >= rangeR) {
                // 懒加,只记录head范围上的累加和,不记录更小范围的累加和.正是因为不往下发,才做到O(logN)
                lazySumTree[head] += value * (rangeR - rangeL + 1);
                // 懒加树上记录head对应的范围上都加value,不落到具体的元素
                lazyAddTree[head] += value;
                return;
            }

            // 如果当前sum范围不全在任务范围下,那么先把之前懒下的任务下发下去
            int mid = (rangeL + rangeR) >> 1;
            pushDown(head, mid - rangeL + 1, rangeR - mid);
            // 再执行新任务
            // 如果左子树在任务范围内,就让左子树add
            if (taskL <= mid) {
                add(taskL, taskR, value, head << 1, rangeL, mid);
            }
            // 如果右子树在任务范围内,就让右子树add
            if (taskR > mid) {
                add(taskL, taskR, value, head << 1 | 1, mid + 1, rangeR);
            }
            // 左右子树都add好了,更新自己位置的累加和=左子树累加和+右子树累加和
            lazySumTree[head] = lazySumTree[head << 1] + lazySumTree[head << 1 | 1];
        }

        public void update(int taskL, int taskR, int value, int head, int rangeL, int rangeR) {
            System.out.println("进来,taskL:" + taskL + " taskR:" + taskR + " value:" + value + " head:" + head + " rangeL:" + rangeL + " rangeR:" + rangeR);
            // base case:如果任务范围把head对应累加和的范围全包了,说明对head的任务明确了:head范围上所有元素都变成value.不需要拆分下发
            if (taskL <= rangeL && taskR >= rangeR) {
                // 懒更新,记录head范围全都变为value,更新标识记为有效
                lazyUpdateTree[head] = value;
                updateFlag[head] = true;
                // head范围内全变成value的话,累加和就是value*范围内节点个数
                lazySumTree[head] = value * (rangeR - rangeL + 1);
                // head范围内全变成value的话,不管之前积攒了多少懒加信息都无效了
                lazyAddTree[head] = 0;
                System.out.println("返回");
                return;
            }

            // 如果任务范围没有把head范围全包,说明不是简单的把head范围都变成value,需要拆分下发.
            // 下发新任务之前先把积攒的旧任务下发
            int mid = (rangeL + rangeR) >> 1;
            pushDown(head, mid - rangeL + 1, rangeR - mid);

            // 如果任务范围涉及到左子树,就下发左子树
            if (taskL <= mid) {
                System.out.println("head:" + head + "左边");
                update(taskL, taskR, value, head << 1, rangeL, mid);
            }
            // 如果任务范围涉及到右子树,就下发右子树
            if (taskR > mid) {
                System.out.println("head:" + head + "右边");
                update(taskL, taskR, value, head << 1 | 1, mid + 1, rangeR);
            }
            // 左右子树执行完任务后,调整自己的累加和
            lazySumTree[head] = lazySumTree[head << 1] + lazySumTree[head << 1 | 1];
        }

        public int querySum(int taskL, int taskR, int head, int rangeL, int rangeR) {
            // base case:如果任务范围把当前head对应累加和的范围全包了,说明对head的任务明确了:返回head范围累加和.不需要拆分下发
            if (taskL <= rangeL && taskR >= rangeR) {
                return lazySumTree[head];
            }

            // 如果任务范围没有把head范围全包,说明不是简单的返回head范围累加和,需要拆分下发
            // 下发新任务之前先把积攒的旧任务下发
            int mid = (rangeL + rangeR) >> 1;
            pushDown(head, mid - rangeL + 1, rangeR - mid);

            int sum = 0;
            // 如果任务范围涉及到左子树,就下发左子树
            if (taskL <= mid) {
                sum += querySum(taskL, taskR, head << 1, rangeL, mid);
            }
            // 如果任务范围涉及到右子树,就下发右子树
            if (taskR > mid) {
                sum += querySum(taskL, taskR, head << 1 | 1, mid + 1, rangeR);
            }
            return sum;
        }

        private void pushDown(int head, int lNum, int rNum) {
            // 先下发懒更新，再下发懒加.为什么?因为update的时候会清空懒加信息,如果下发的时候依然有懒加,说明先执行了update，又执行了add，
            // 而下发更新的时候也会清空懒加信息，所以要先下发更新再下发加,如果先下发加再下发更新，加就被更新覆盖了

            // 如果有懒更新信息,就下发左右节点,最后清空自己的懒更新信息
            if (updateFlag[head]) {
                // 左子树
                lazyUpdateTree[head << 1] = lazyUpdateTree[head];
                updateFlag[head << 1] = true;
                lazySumTree[head << 1] = lazyUpdateTree[head] * lNum;
                lazyAddTree[head << 1] = 0;
                // 右子树
                lazyUpdateTree[head << 1 | 1] = lazyUpdateTree[head];
                updateFlag[head << 1 | 1] = true;
                lazySumTree[head << 1 | 1] = lazyUpdateTree[head] * rNum;
                lazyAddTree[head << 1 | 1] = 0;
                // 清空head的懒更新信息
                updateFlag[head] = false;
            }
            // 如果有懒加信息,就下发左右节点,最后清空自己的懒加信息
            if (lazyAddTree[head] != 0) {
                // 左子树
                lazySumTree[head << 1] += lazyAddTree[head] * lNum;
                lazyAddTree[head << 1] += lazyAddTree[head];
                // 右子树
                lazySumTree[head << 1 | 1] += lazyAddTree[head] * rNum;
                lazyAddTree[head << 1 | 1] += lazyAddTree[head];
                // 清空head的懒加信息
                lazyAddTree[head] = 0;
            }
        }

    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        SegmentTree segmentTree = new SegmentTree(arr);
        System.out.println(segmentTree.querySum(1, 10, 1, 1, 10));
        System.out.println(segmentTree.querySum(4, 9, 1, 1, 10));
        System.out.println(segmentTree.querySum(2, 8, 1, 1, 10));
        System.out.println("===============");
        segmentTree.add(5, 9, 1, 1, 1, 10);
        System.out.println(segmentTree.querySum(1, 10, 1, 1, 10));
        System.out.println(segmentTree.querySum(4, 9, 1, 1, 10));
        System.out.println(segmentTree.querySum(2, 8, 1, 1, 10));
        System.out.println("=============");
        segmentTree.update(1, 5, 1, 1, 1, 10);
        System.out.println(segmentTree.querySum(1, 10, 1, 1, 10));
        System.out.println(segmentTree.querySum(4, 9, 1, 1, 10));
        System.out.println(segmentTree.querySum(2, 8, 1, 1, 10));


    }

}
