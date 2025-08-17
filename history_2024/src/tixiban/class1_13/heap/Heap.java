package tixiban.class1_13.heap;

import tixiban.class1_13.recurrence.MergeSort;

import java.util.Arrays;

/**
 * 堆
 * 说到堆一定要要说大根堆还是小根堆，除了这俩没别的。
 * 什么是小根堆：完全二叉树，每个子树都是根最小
 * 什么是大根堆：完全二叉树，每个子树都是根最大
 * <p>
 * 扩展
 * 1、求堆中最大值：返回根节点（数组第一个元素），时间复杂度O(1)
 * 2、求堆中最小值：返回最后一个子节点（数组最后一个元素），时间复杂度O(1)
 * 3、弹出堆中最大值：返回根节点；最后一个节点和根节点互换，把最后一个位置移出堆，heapify形成新堆
 * 4、堆中任意一个节点变化，恢复堆结构：变化的节点作为当前节点，分别来一次heapInsert和heapify。顺序随意，只会满足其中一个的条件，上浮或下沉
 */
public class Heap {

    /**
     * 堆排序
     * 第一步整体heapInsert，第二步整体heapify
     * 时间复杂度O(NlogN)。heapInsert时间复杂度O(logN)，执行N次，heapify时间复杂度O(logN)，执行N次
     * 额外空间复杂度O(1)，没有基于数组长度申请的空间
     * <p>
     * 思考：
     * 整体heapInsert时间复杂度O(NlogN)存疑，因为只有堆大小为N的时候heapInsert才是O(logN)，在此之前都小于O(logN)，执行N次就小于O(NlogN)
     * 证明：数据量扩倍法。因为时间复杂度忽略常数项，所以如果N个数收敛于T(N)，那么2N个数也收敛于T(N)
     * 数据量为N时，heapInsert第一个数O(log1)，第二个数O(log2)……，第N个数O(logN)，所以N个数整体的上限不会高于O(N*logN)
     * 数据量为2N时，后N个数heapInsert，第N+1个数O(log(N+1))，第N+2个数O(log(N+2))……，第2N个数O(log(2N))，所以后2N个数整体的下限不会低于O(N*logN)
     * 根据夹逼定理，T(N)上限是O(N*logN)，下限也是O(N*logN)，那么T(N)就收敛于O(N*logN)
     */
    public static void heapSort(int[] arr) {
        if (null == arr || arr.length < 2) {
            return;
        }
        // 建堆，把数组元素依次加入堆，依次形成大根堆，最后整个数组形成大根堆
        for (int i = 0; i < arr.length; i++) {
            heapInsert(arr, i);
        }
        // 堆的范围=数组长度
        int heapSize = arr.length;
        // 每次循环把当前堆的根节点（最大值）排好
        while (heapSize > 0) {
            // 把第一个数（根节点）和最后一个数（最后的叶子节点）交换，并把最后位置移出堆
            // 怎么移出：heapSize--，最后一个节点就不在堆的范围了
            // 交换后数组中的最大值已经排好了
            swap(arr, 0, --heapSize);
            // 此时堆的根节点变化，执行heapify恢复堆结构
            heapify(arr, 0, heapSize);
        }
    }

    /**
     * 在堆的末尾插入新节点，建立新的堆结构
     * 即最后一个叶子节点上浮的过程
     * 时间复杂度O(logN)，因为每次上浮所做的操作是有限的，最多上浮logN次（树的高度）
     */
    public static void heapInsert(int[] arr, int cur) {
        // 完全二叉树用数组表示，当前节点cur的父节点是(cur-1)/2
        // 只要当前节点比父节点大，就让当前节点和父节点互换
        // cur=0时，(cur-1)/2也=0，不大于，会跳出
        while (arr[cur] > arr[(cur - 1) / 2]) {
            swap(arr, cur, (cur - 1) / 2);
            // 当前节点来到新的位置（上浮），准备下一轮的比较
            cur = (cur - 1) / 2;
        }
    }

    /**
     * 把堆的根节点变成新的节点，重新建立新的堆结构
     * 即根节点下沉的过程
     * 时间复杂度O(logN)，因为每次下沉所做的操作是有限的，最多下沉logN层（树的高度）
     */
    public static void heapify(int[] arr, int cur, int heapSize) {
        // 完全二叉树用数组表示，当前节点cur的左子节点是cur*2+1，右子节点是cur*2+2
        int left = cur * 2 + 1;
        // heapSize表示堆的范围，即堆的节点个数。
        // 左子节点超出堆范围，跳出循环。左子节点超了右子节点也必超。
        while (left < heapSize) {
            // 左右子节点哪个更大
            int bigger = left + 1 < heapSize && arr[left + 1] > arr[left] ? left + 1 : left;
            // 如果当前节点比子节点中更大的那个还大，说明当前节点来到了正确的位置，已经符合堆的要求了
            if (arr[cur] >= arr[bigger]) {
                break;
            }
            // 如果当前节点不比子节点大，就把更大的子节点和当前节点互换
            swap(arr, cur, bigger);
            // 当前节点来到新位置（下沉）
            cur = bigger;
            // 新的左子节点
            left = cur * 2 + 1;
        }
    }

    /**
     * 只用到heapify的堆排序
     * 第一步整体建堆采用从下往上遍历下沉的方式，的时间复杂度减小到O(N)
     * 把给定的数组看作一个完全二叉树，由完全二叉树的特性知：
     * 叶子节点的个数为N/2，下沉只需比较1次；高度为2的节点个数为N/4，下沉只需比较2次……，所以：
     * T(N) = N/2*1 + N/4*2 + N/8*3 + …… + N/(2^logN)*logN，高中数学，数列求和，乘2错位相减：
     * 2*T(N) = N*1 + N/2*2 + N/4*3 + …… + N/(2^(logN-1))*logN
     * T(N) = N + N/2 + N/4 + …… + f(N) + f(logN)
     * 前面等比数列求和公式收敛于O(N),后面一个O(N)一个O(logN)，整体收敛于O(N)
     * <p>
     * 分析为什么从下往上遍历下沉，时间复杂度更小？
     * 从下往上遍历上浮，数量最多的节点（叶子）当初上浮时比较的次数最多
     * 从下往上遍历下沉，数量最多的节点（叶子）下沉时比较的次数最少
     */
    public static void heapifySort(int[] arr) {
        if (null == arr || arr.length < 2) {
            return;
        }
        // 和正常堆排序唯一不同就是建堆的过程是从下往上遍历下沉
        for (int i = arr.length; i >= 0; i--) {
            heapify(arr, i, arr.length);
        }

        int heapSize = arr.length;
        while (heapSize > 0) {
            swap(arr, 0, --heapSize);
            heapify(arr, 0, heapSize);
        }
    }


    public static void swap(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }


    public static void main(String[] args) {
        for (int n = 0; n < 1000000; n++) {
            int[] arr = MergeSort.generateArr(20, 100);
            int[] arr1 = new int[arr.length];
            System.arraycopy(arr, 0, arr1, 0, arr.length);
            int[] arr2 = new int[arr.length];
            System.arraycopy(arr, 0, arr2, 0, arr.length);

            Arrays.sort(arr1);
            heapSort(arr2);

            for (int i = 0; i < arr1.length; i++) {
                if (arr1[i] != arr2[i]) {
                    System.out.println("原数组：" + Arrays.toString(arr));
                    System.out.println("系统排序：" + Arrays.toString(arr1));
                    System.out.println("堆排序" + Arrays.toString(arr2));
                    break;
                }
            }
        }
    }
}
