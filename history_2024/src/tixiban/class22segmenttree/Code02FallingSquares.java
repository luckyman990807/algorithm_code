package tixiban.class22segmenttree;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * 落方块问题.(俄罗斯方块但每个图形都是正方形)
 * 给定一个二维数组arr,arr[i][0]=left表示方块i左边对应的横坐标为left,arr[i][1]=width表示方块i宽为width.
 * 如果一个方块右边坐标和另一个方块左边坐标相等,那么二者能严丝合缝地挨在一起.
 * 返回一个一维数组,表示每落一个方块后当前最大高度是是多少.
 *
 * 思路:
 * 查询和更新的线段树,查询是查最大值,不是查累加和(一样,都是从左右子树要信息得出结果)
 * 每落下一个方块,就查询一下当前方块宽度width对应的范围上的最大高度max,然后把这个范围上的最大高度都更新成max+width
 */
public class Code02FallingSquares {
    public static class SegmentTree {
        // 实际的范围
        int n;
        // 记录一个范围上的最大值懒信息的二叉树
        int[] maxLazy;
        // 记录一个范围上的更新懒信息的二叉树
        int[] updateLazy;
        // 记录更新信息是否生效(因为更新信息为0的话无法区分是没有更新信息还是更新为0)
        boolean[] updateFlag;

        public SegmentTree(int size) {
            n = size;
            // 线段树约定俗成不用0下标,从1下标开始.n个节点转换成范围二叉树最多不超过n*4个节点
            maxLazy = new int[(n + 1) << 2];
            updateLazy = new int[(n + 1) << 2];
            updateFlag = new boolean[(n + 1) << 2];
        }

        /**
         * 对外暴露,把taskL到taskR范围全部更新成value
         */
        public void update(int taskL, int taskR, int value) {
            updateProcess(taskL, taskR, value, 1, 1, n);
        }

        /**
         * 递归,任务是从taskL到taskR全部更新成value,当前递归到head节点,head代表的范围是rangeL到rangeR
         */
        private void updateProcess(int taskL, int taskR, int value, int head, int rangeL, int rangeR) {
            // base case,如果任务范围把head范围全包了,那么要做的事很明确:head范围全更新成value,不需要再拆分
            if (taskL <= rangeL && taskR >= rangeR) {
                // 既然要做的事明确了,那么就没必要真正执行,只需要记录一下head范围上改成value即可,等真正需要执行的时候再执行,这就是懒更新.
                updateLazy[head] = value;
                updateFlag[head] = true;
                // 落方块的题目保证了每次更新必然是更新成更大的(因为肯定越落越高),所以最大值懒信息也直接更新成value
                maxLazy[head] = value;
                return;
            }

            // 如果任务范围没有把head范围全包,那么还要在head范围上拆分下发
            // 先把当前head节点积攒的懒信息下发一层
            pushDown(head);
            int mid = (rangeL + rangeR) >> 1;
            // 如果任务范围涉及左子树,就递归更新左子树
            if (taskL <= mid) {
                updateProcess(taskL, taskR, value, head << 1, rangeL, mid);
            }
            // 如果任务范围涉及右子树,就递归更新右子树
            if (taskR > mid) {
                updateProcess(taskL, taskR, value, head << 1 | 1, mid + 1, rangeR);
            }
            // 下发之后head范围上必然有节点被更新成value,落方块题目保证了更新的一定是更大的,所以最大值懒信息直接记录head范围上最大值为value
            maxLazy[head] = value;
        }

        /**
         * 对外暴露,查询从taskL到taskR上的最大值.思路和更新一样
         */
        public int query(int taskL, int taskR) {
            return queryProcess(taskL, taskR, 1, 1, n);
        }

        private int queryProcess(int taskL, int taskR, int head, int rangeL, int rangeR) {
            if (taskL <= rangeL && taskR >= rangeR) {
                return maxLazy[head];
            }

            pushDown(head);

            int mid = (rangeL + rangeR) >> 1;
            int max = 0;
            if (taskL <= mid) {
                max = Math.max(max, queryProcess(taskL, taskR, head << 1, rangeL, mid));
            }
            if (taskR > mid) {
                max = Math.max(max, queryProcess(taskL, taskR, head << 1 | 1, mid + 1, rangeR));
            }

            return max;
        }

        /**
         * 把head范围上积攒的懒信息下发到下一层
         */
        private void pushDown(int head) {
            if (updateFlag[head]) {
                // 懒更新下发左子树
                updateLazy[head << 1] = updateLazy[head];
                updateFlag[head << 1] = true;
                maxLazy[head << 1] = updateLazy[head];
                // 懒更新下发右子树
                updateLazy[head << 1 | 1] = updateLazy[head];
                updateFlag[head << 1 | 1] = true;
                maxLazy[head << 1 | 1] = updateLazy[head];
                // 自己懒信息清空
                updateFlag[head] = false;
            }
        }
    }


    /**
     * 方块数据离散化,作用是节省空间
     *
     * 举个例子,如果有3个方块[0,5],[100,50],[5000,1000],对应的范围分别是0到4、100到149、5000到5999,
     * 那么线段树的长度就至少申请6000*4才能支持范围更新和范围查询.
     * 而如果把连续的方块数据离散化,令0=1,4=2,100=3,149=4,5000=5,5999=6,
     * 那么三个方块对应的范围就是1到2、3到4、5到6,线段树的长度只需要申请6*4即可
     *
     * 为什么方块[0,5]对应的范围是0到4而不是0到5?
     * 为了防止贴边的两个方块高度计算错误.
     * 举个例子,两个方块[0,5],[5,3]依次落下,如果方块范围分别是0到5,5到8,那么5位置的最大高度就会加两遍=5+3=8,
     * 实际最大高度=5,两个方块贴边并排落下.
     */
    public static Map<Integer, Integer> index(int[][] squares) {
        TreeSet<Integer> set = new TreeSet<>();
        for (int[] square : squares) {
            set.add(square[0]);
            set.add(square[0] + square[1] - 1);
        }

        HashMap<Integer, Integer> map = new HashMap<>();
        int index = 1;
        for (int point : set) {
            map.put(point, index++);
        }

        return map;
    }

    public static void main(String[] args) {
        int[][] squares = {{0, 2}, {1, 3}, {3, 3}, {8, 4},{8,6}};
        // key:方块范围端点,value:对应的离散点
        Map<Integer, Integer> map = index(squares);
        SegmentTree segmentTree = new SegmentTree(map.size());
        // 落几个方块记录几次结果
        int[] result = new int[squares.length];

        int max = 0;
        for (int i = 0; i < squares.length; i++) {
            // 先查询范围上现有的最大高度
            int height = segmentTree.query(map.get(squares[i][0]), map.get(squares[i][0] + squares[i][1] - 1));
            // 记录最大值
            max = Math.max(max, height + squares[i][1]);
            result[i] = max;
            // 范围上全部的最大高度=现有高度+当前方块高度
            segmentTree.update(map.get(squares[i][0]), map.get(squares[i][0] + squares[i][1] - 1),height+squares[i][1]);
        }
        System.out.println(Arrays.toString(result));
    }

}
