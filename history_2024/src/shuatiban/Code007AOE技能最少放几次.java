package shuatiban;

/**
 * 给定两个正数数组x和hp，长度都是N。
 * x数组一定是有序的，x[i]表示i号怪兽在x轴上的位置；hp数组不要求有序，hp[i]表示i号怪兽的血量
 * 再给定一个正数range，表示法师释放AOE技能的范围长度, 范围内的每只怪兽损失1点血量。
 * 返回要把所有怪兽血量清空，至少需要释放多少次AOE技能？
 *
 * 三个参数：int[] x, int[] hp, int range
 * 返回：int 次数
 *
 * 思路:
 * 1.贪心
 * 每次把范围右移到恰好左边界够到最左边的活着的怪,这个怪有多少血,就放几次技能,然后寻找下一个活着的怪,范围的左边界再恰好够到这个活着的怪.
 * 复杂度O(N^2),因为既要遍历放放技能,又要遍历给属于当前范围内的怪减血
 * 2.贪心+线段树
 * 策略还是上面的贪心策略,只不过用线段树把范围上加减的复杂度降到O(logN).
 *
 *
 * range是半径?
 * 只能站在怪的位置上放技能?
 */
public class Code007AOE技能最少放几次 {
    public static int lestAOE(int[] position, int[] hp, int range) {
        int count = 0;
        for (int i = 0; i < position.length; i++) {
            if (hp[i] > 0) {
                // 如果i号怪兽还活着,就以i号的位置作为左边界
                int curHp = hp[i];
                int right = i;
                // 找到以i号作为左边界能影响到的最右的怪兽,把这个范围内的怪兽都减去i号的血量
                while (right < position.length && position[right] - position[i] <= range) {
                    hp[right++] -= curHp;
                }
                // i号有多少血,本次就要放多少次AOE技能
                count += curHp;
            }
        }
        return count;
    }


    public static void main(String[] args) {
        int[] position = {1, 3, 7, 9};
        int[] hp = {1, 1, 4, 4};
        int[] hpCp = {1, 1, 4, 4};
        int[] hpCpp = {1, 1, 4, 4};
        int range = 4;
        System.out.println(lestAOE(position, hp, range));
        System.out.println(minAoe2(position, hpCp, range));
        System.out.println(lestAOE2(position, hpCpp, range));
    }

    public static int lestAOE2(int[] position, int[] hp, int range) {
        // 构建辅助数组,rangeForm[i]=j表示以i号怪兽为左边界,最右能影响到j号怪兽
        int[] rangeFrom = new int[position.length];
        int right = 0;
        for (int i = 0; i < position.length; i++) {
            while (right + 1 < position.length && position[right + 1] - position[i] < range) {
                right++;
            }
            rangeFrom[i] = right;
        }

        int count = 0;

        SegmentTree segmentTree = new SegmentTree(hp);
        for (int i = 0; i < rangeFrom.length; i++) {
            // 只要发现一个怪兽还有血,就以他为左边界,放AOE技能直到当前怪兽没血,再找下一个有血的怪兽
            if (segmentTree.sum(i, i) > 0) {
                segmentTree.add(i, rangeFrom[i], -hp[i]);
                count += hp[i];
            }
        }
        return count;
    }


    /**
     * 线段树
     */
    public static class SegmentTree {
        // 记录原始数据的数组
        int[] arr;
        // 懒累加和树
        int[] lazySumTree;
        // 懒增加树
        int[] lazyAddTree;
        // 懒更新树
        int[] lazyUpdateTree;
        // 更新标志,因为懒更新树的值无法表示「没有更新任务」的情况,=0可能任务就是更新成0.所以需要额外的标志
        boolean[] updateFlag;

        // 构造函数
        public SegmentTree(int[] originArr) {
            int n = originArr.length;
            arr = new int[n];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = originArr[i];
            }
            // 懒累加和树、懒加树、懒更新树,都是把原始数据放在叶子节点,只不过懒累加和树的非叶子节点表示左右孩子之和,懒加树的非叶子节点表示左右孩子都要加上多上,懒更新树的非叶子节点表示左右孩子都要更新成什么
            lazySumTree = new int[n << 2];
            lazyAddTree = new int[n << 2];
            lazyUpdateTree = new int[n << 2];
            updateFlag = new boolean[n << 2];

            // 把原始数据从0到n-1下标(全部值)挂到累加树的0下标节点上
            initLazySumTree(0, n - 1, 0);
        }

        /**
         * 初始化懒累加和树
         * 把原数据从l下标到r下标的数,挂到懒累加和树的head节点下
         */
        private void initLazySumTree(int l, int r, int head) {
            // 如果l到r只有一个数,那么直接挂到head上
            if (l == r) {
                lazySumTree[head] = arr[l];
                return;
            }

            // 把l到r拆成两半挂到左右子树上
            int mid = (l + r) >> 1;
            initLazySumTree(l, mid, (head << 1) + 1);
            initLazySumTree(mid + 1, r, (head << 1) + 2);

            // 由左右孩子相加得到head节点的值
            sumUp(head);
        }

        /**
         * 封装加方法
         */
        public void add(int taskL, int taskR, int value) {
            lazyAdd(taskL, taskR, value, 0, 0, arr.length - 1);
        }

        /**
         * 封装更新方法
         */
        public void update(int taskL, int taskR, int value) {
            lazyUpdate(taskL, taskR, value, 0, 0, arr.length - 1);
        }

        /**
         * 封装查询累加和方法
         */
        public int sum(int taskL, int taskR) {
            return querySum(taskL, taskR, 0, 0, arr.length - 1);
        }

        /**
         * 懒加,把原数据下标taskL到taskR范围都加value,
         * 当前懒加任务落到了head节点头上,head管辖的范围是原数据下标l到r
         */
        private void lazyAdd(int taskL, int taskR, int value, int head, int l, int r) {
            // 如果任务范围把当前head的管辖范围覆盖了,那么只需要把懒任务记到head头上即可
            if (taskL <= l && taskR >= r) {
                lazySumTree[head] += value * (r - l + 1);
                lazyAddTree[head] += value;
                return;
            }

            // 如果head辖下有的数据不在任务范围内,那么就要先把之前记在head头上的懒任务下发给head的孩子,再递归尝试给孩子追加本次的懒任务

            // 下发任务
            taskDown(head, l, r);

            int mid = (l + r) >> 1;
            // 如果任务范围涉及到左孩子的范围,就递归对左孩子懒加
            if (mid >= taskL) {
                lazyAdd(taskL, taskR, value, (head << 1) + 1, l, mid);
            }
            // 如果任务范围涉及到右孩子的范围,就递归对右孩子懒加
            if (mid + 1 <= taskR) {
                lazyAdd(taskL, taskR, value, (head << 1) + 2, mid + 1, r);
            }

            // 左右孩子懒加之后,更新head的累加和
            sumUp(head);
        }

        /**
         * 懒更新,把原数据taskL到taskR范围更新成value,
         * 当前懒更新任务落到了head节点头上,head管辖的范围是原数据下标l到r
         */
        private void lazyUpdate(int taskL, int taskR, int value, int head, int l, int r) {
            if (taskL <= l && taskR >= r) {
                lazyUpdateTree[head] = value;
                updateFlag[head] = true;
                lazySumTree[head] = value * (r - l + 1);
                // 一旦更新,之前积累的懒加任务都要清空了
                lazyAddTree[head] = 0;
                return;
            }

            int mid = (l + r) >> 1;
            if (mid >= taskL) {
                lazyUpdate(taskL, taskR, value, (head << 1) + 1, l, mid);
            }
            if (mid <= taskR) {
                lazyUpdate(taskL, taskR, value, (head << 1) + 2, mid + 1, r);
            }
            sumUp(head);
        }

        /**
         * 查询原数据从下标taskL到taskR的累加和,
         * 当前查询任务落到了head头上,head管辖的范围是原数组下标l到r范围
         */
        private int querySum(int taskL, int taskR, int head, int l, int r) {
            if (taskL <= l && taskR >= r) {
                return lazySumTree[head];
            }

            taskDown(head, l, r);
            int sum = 0;
            int mid = (l + r) >> 1;
            if (mid >= taskL) {
                sum += querySum(taskL, taskR, (head << 1) + 1, l, mid);
            }
            if (mid + 1 <= taskR) {
                sum += querySum(taskL, taskR, (head << 1) + 2, mid + 1, r);
            }
            return sum;
        }

        /**
         * 把head节点头上的任务下发给他的子节点,也就是清空head头上的任务.
         * head所管辖的原始数据的范围是下标l到r
         * 注意,如果head管辖l到r,那么head的左孩子一定管辖l到mid,右孩子一定管辖mid+1到r,因为当时把原始数据挂到累加和树上的时候就是这么分的.
         */
        private void taskDown(int head, int l, int r) {
            // 左孩子的位置
            int lChild = (head << 1) + 1;
            // 右孩子的位置
            int rChild = (head << 1) + 2;

            // head管辖范围的中点
            int mid = (l + r) >> 1;
            // 左半范围,也就是左孩子管辖的范围
            int lCount = mid - l + 1;
            // 右半范围,也就是右孩子管辖的范围
            int rCount = r - mid;

            // 先下发更新任务,再下发加任务.因为更新时要清空加,如果此时有加,那么加必然是在更新之后来的,也就是要先更新成某个值,在在这个值基础上加.如果先下发加,那么加就会被更新覆盖,加就丢失了.

            // 下发更新任务
            if (updateFlag[head]) {
                // 左孩子的懒更新值等于head的懒更新值
                lazyUpdateTree[lChild] = lazyUpdateTree[head];
                // 左孩子更新标志置为有效
                updateFlag[lChild] = true;
                // 左孩子的懒累加和值 = 每个节点要更新成什么 * 管辖的节点数
                lazySumTree[lChild] = lazyUpdateTree[head] * lCount;
                // 一旦更新,那么之前累计的的懒加值都失效了
                lazyAddTree[lChild] = 0;

                lazyUpdateTree[rChild] = lazyUpdateTree[head];
                updateFlag[rChild] = true;
                lazySumTree[rChild] = lazyUpdateTree[head] * rCount;
                lazyAddTree[rChild] = 0;

                // 最后清空head自己的更新标志
                updateFlag[head] = false;
            }

            // 下发加任务
            /** 注意这里一定是!=0,不是>0,因为懒加的值可能是负数 */
            if (lazyAddTree[head] != 0) {
                // 左孩子的懒加值在原有基础上添加head的懒加值
                lazyAddTree[lChild] += lazyAddTree[head];
                // 左孩子的懒累加和值在原有基础上添加累计新增的值
                lazySumTree[lChild] += lazyAddTree[lChild] * lCount;

                lazyAddTree[rChild] += lazyAddTree[head];
                lazySumTree[rChild] += lazyAddTree[rChild] * rCount;

                // 最后清空head自己的懒加值
                lazyAddTree[head] = 0;
            }
        }

        /**
         * 在累加树上通过左右孩子的值相加得到头节点的值
         */
        private void sumUp(int head) {
            // 用数组表示的树,根节点在0下标,i下标的左孩子是i*2+1,右孩子是i*2+2
            lazySumTree[head] = lazySumTree[(head << 1) + 1] + lazySumTree[(head << 1) + 2];
        }

        /**
         * 打印维护的数据
         */
        private void print() {
            for (int i = 0; i < arr.length; i++) {
                System.out.print(sum(i, i)+" ");
            }
            System.out.println();
        }
    }


    // 贪心策略：永远让最左边缘以最优的方式(AOE尽可能往右扩，最让最左边缘盖住目前怪的最左)变成0，也就是选择：
    // 一定能覆盖到最左边缘, 但是尽量靠右的中心点
    // 等到最左边缘变成0之后，再去找下一个最左边缘...
    public static int minAoe2(int[] x, int[] hp, int range) {
        int N = x.length;
        int ans = 0;
        for (int i = 0; i < N; i++) {
            if (hp[i] > 0) {
                int triggerPost = i;
                while (triggerPost < N && x[triggerPost] - x[i] <= range) {
                    triggerPost++;
                }
                ans += hp[i];
                aoe(x, hp, i, triggerPost - 1, range);
            }
        }
        return ans;
    }

    public static void aoe(int[] x, int[] hp, int L, int trigger, int range) {
        int N = x.length;
        int RPost = trigger;
        while (RPost < N && x[RPost] - x[trigger] <= range) {
            RPost++;
        }
        int minus = hp[L];
        for (int i = L; i < RPost; i++) {
            hp[i] = Math.max(0, hp[i] - minus);
        }
    }
}
