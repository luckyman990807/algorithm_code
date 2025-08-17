package tixiban.class22segmenttree;

public class Code00MySegmentTree {
    public static class SegmentTree {
        // 原始数组的大小
        private int size;
        // 累加和树
        private int[] sumTree;
        // 加任务树
        private int[] addTask;
        // 更新任务树
        private int[] updateTask;
        // 更新标识树，作用是标识某个节点是否有更新任务，因为updateTask[i]=0无法判断是要更新成0还是没有更新任务
        private boolean[] updateFlag;

        /**
         * 构造函数
         * 问：原始数组长度为n，树的大小为什么是4n？
         * 答：为了保证一定能装得下这棵线段树。线段树是用数组表示树，并且原始数组元素都在叶子节点，并且只有最后一层可能不满。
         * 用数组表示树的话需要把树补成满二叉树，补满时最浪费空间的情况是最后一层只有一个节点，这时如果叶子节点有n个，需要补2n-1个，最终的满二叉树的节点数是4n-1个，所以线段树数组长度申请4n。
         * 虽然实际不会出现最差情况，因为脑补一下递归构建树的过程，不可能出现最后一层只有一个节点的情况，但是线段树构建的最差情况不好判断，总之再差也不会超过4n，所以用4n。
         *
         * @param arr 原始数组
         */
        public SegmentTree(int[] arr) {
            size = arr.length;
            sumTree = new int[size * 4];
            addTask = new int[size * 4];
            updateTask = new int[size * 4];
            updateFlag = new boolean[size * 4];
            initSumTree(arr, 0, size - 1, 1);
        }

        /**
         * 递归，把arr从start到end范围的元素放到以head为头的子树上
         */
        private void initSumTree(int[] arr, int start, int end, int head) {
            // 递归出口，如果子树的范围只剩下一个元素，就把该元素放到头节点位置（或者说，线段树的叶子节点是数组元素本身）
            if (start == end) {
                sumTree[head] = arr[start];
                return;
            }
            // 把子树范围分成左右两半，分别放到左右子树上
            int mid = start + (end - start) / 2;
            int leftHead = head * 2;
            int rightHead = head * 2 + 1;
            initSumTree(arr, start, mid, leftHead);
            initSumTree(arr, mid + 1, end, rightHead);
            // 以head为头的子树的累加和=左子树累加和+右子树累加和
            sumTree[head] = sumTree[leftHead] + sumTree[rightHead];
        }

        /**
         * 加任务，数组从taskL到taskR范围上统一都加value
         * 递归，当前任务落到了head为头的子树上，head子树负责的范围是原始数组从rangeL到rangeR（数组下标从1开始）
         */
        private void add(int taskL, int taskR, int value, int head, int rangeL, int rangeR) {
            // 递归出口，如果当前子树的范围被任务范围完全覆盖，说明当前子树上要做的事已经明确了：就是全员+value
            // 既然任务已经明确了，就不需要实际执行了，只需要把任务记在当前子树上，等需要结果的时候再往下发或执行。正是这种懒更新手段才让线段树做到O(logN)的时间复杂度
            if (taskL <= rangeL && taskR >= rangeR) {
                // 注意，累加和和加任务都是累加，+=，不是=
                sumTree[head] += value * (rangeR - rangeL + 1);
                addTask[head] += value;
                return;
            }
            // 如果当前子树的范围没有被任务范围完全覆盖，说明当前子树上要做的事还不明确，需要把任务递归到左右子树处理

            // 递归左右子树之前，先把head目前为止积攒的任务下发到左右子树，让左右子树拥有最新数据
            pushDown(head, rangeL, rangeR);
            // 左右子树在各自的范围上递归处理这个任务
            int mid = rangeL + (rangeR - rangeL) / 2;
            int leftHead = head * 2;
            int rightHead = head * 2 + 1;
            if (mid >= taskL) {
                add(taskL, taskR, value, leftHead, rangeL, mid);
            }
            if (mid + 1 <= taskR) {
                add(taskL, taskR, value, rightHead, mid + 1, rangeR);
            }
            // 左右子树执行完任务后，更新head子树的累加和。这里不需要给head子树记加任务，因为head的任务已经下发到左右子树了
            sumTree[head] = sumTree[leftHead] + sumTree[rightHead];
        }

        /**
         * 更新任务，数组从taskL到taskR范围上统一都更新成value
         * 递归，当前任务落到了head为头的子树上，head子树负责的范围是原始数组从rangeL到rangeR（数组下标从1开始）
         */
        private void update(int taskL, int taskR, int value, int head, int rangeL, int rangeR) {
            // 递归出口，如果当前子树的范围被任务范围完全覆盖，说明当前子树上要做的事已经明确了：就是全员变为value
            // 既然任务已经明确了，就不需要实际执行了，只需要把任务记在当前子树上，等需哟结果的时候再往下发或者执行。正是这种懒更新手段才让线段树做到O(logN)的时间复杂度
            if (taskL <= rangeL && taskR >= rangeR) {
                sumTree[head] = value * (rangeR - rangeL + 1);
                updateTask[head] = value;
                updateFlag[head] = true;
                // 清空当前子树积攒的加任务，因为不管之前加了多少，只要一更新就全没了
                addTask[head] = 0;
                return;
            }
            // 如果当前子树的范围没有被任务范围完全覆盖，说明当前子树上要做的事还不明确，需要把任务递归到左右子树处理

            // 递归左右子树之前，先把head目前为止积攒的任务下发到左右子树，让左右子树拥有最新数据
            pushDown(head, rangeL, rangeR);

            // 左右子树在各自的范围上递归这个任务
            int mid = rangeL + (rangeR - rangeL) / 2;
            int leftHead = head * 2;
            int rightHead = head * 2 + 1;
            if (mid >= taskL) {
                update(taskL, taskR, value, leftHead, rangeL, mid);
            }
            if (mid + 1 <= taskR) {
                update(taskL, taskR, value, rightHead, mid + 1, rangeR);
            }
            // 左右子树执行完任务后，更新head子树的累加和。这里不需要给head子树记更新任务，因为head的任务已经下发到左右子树了
            sumTree[head] = sumTree[leftHead] + sumTree[rightHead];
        }

        private int querySum(int taskL, int taskR, int head, int rangeL, int rangeR) {
            // 递归出口，如果当前子树的范围被任务范围完全覆盖，说明当前子树上要做的事已经明确了：就是返回这棵子树的累加和，也就是head节点的值
            if (taskL <= rangeL && taskR >= rangeR) {
                return sumTree[head];
            }
            // 如果当前子树的范围没有被任务范围完全覆盖，说明当前子树上要做的事还不明确，需要把任务递归到左右子树处理

            // 递归左右子树之前，先把head目前为止积攒的任务下发到左右子树，让左右子树拥有最新数据
            pushDown(head, rangeL, rangeR);
            int sum = 0;
            int mid = rangeL + (rangeR - rangeL) / 2;
            if (mid >= taskL) {
                sum += querySum(taskL, taskR, head * 2, rangeL, mid);
            }
            if (mid + 1 <= taskR) {
                sum += querySum(taskL, taskR, head * 2 + 1, mid + 1, rangeR);
            }
            return sum;
        }

        /**
         * 把head节点积攒的任务下发到左右子树，head节点负责的范围是原始数组从rangeL到rangeR（数组下标从1开始）
         */
        private void pushDown(int head, int rangeL, int rangeR) {
            int mid = rangeL + (rangeR - rangeL) / 2;
            // 左右子树负责的范围的大小，也就是负责原始数组的多少个数
            int countL = mid - rangeL + 1;
            int countR = rangeR - mid;
            // 左右子树头节点
            int leftHead = head * 2;
            int rightHead = leftHead + 1;
            // 先下发更新任务，再下发加任务，因为update积累任务的时候，积累一个更新任务会清空所有加任务，所以如果此时有加任务，一定是比更新任务后到的，下发的时候也要按照积累时的顺序
            if (updateFlag[head]) {
                // 左子树，赋值更新任务、更新标识、加任务、累加和
                updateTask[leftHead] = updateTask[head];
                updateFlag[leftHead] = true;
                addTask[leftHead] = 0;
                sumTree[leftHead] = updateTask[leftHead] * countL;
                // 左子树，赋值更新任务、更新标识、加任务、累加和
                updateTask[rightHead] = updateTask[head];
                updateFlag[rightHead] = true;
                addTask[rightHead] = 0;
                sumTree[rightHead] = updateTask[rightHead] * countR;
                // 清空head节点的更新任务
                updateFlag[head] = false;
            }
            if (addTask[head] != 0) {
                // 左子树，赋值加任务、累加和
                addTask[leftHead] += addTask[head];
                sumTree[leftHead] += addTask[head] * countL;
                // 右子树，赋值加任务、累加和
                addTask[rightHead] += addTask[head];
                sumTree[rightHead] += addTask[head] * countR;
                // 清空head节点的加任务
                addTask[head] = 0;
            }
        }

        /**
         * 对外提供的add方法，数组从下标taskL到taskR范围内统一都加value，下标从0开始
         */
        public void add(int taskL, int taskR, int value) {
            add(taskL + 1, taskR + 1, value, 1, 1, size);
        }

        /**
         * 对外提供的update方法，数组从下标taskL到taskR范围内统一都改为value，下标从0开始
         */
        public void update(int taskL, int taskR, int value) {
            update(taskL + 1, taskR + 1, value, 1, 1, size);
        }

        /**
         * 对外提供的querySum方法，求数组从下标taskL到taskR范围的累加和，下标从0开始
         */
        public int querySum(int taskL, int taskR) {
            return querySum(taskL + 1, taskR + 1, 1, 1, size);
        }
    }

    public static void main(String[] args) {
        int[] arr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        SegmentTree segmentTree = new SegmentTree(arr);
        System.out.println(segmentTree.querySum(2, 4));
        segmentTree.update(3, 4, 0);
        System.out.println(segmentTree.querySum(2, 4));
        segmentTree.add(2, 3, 1);
        System.out.println(segmentTree.querySum(2, 4));

    }

}
