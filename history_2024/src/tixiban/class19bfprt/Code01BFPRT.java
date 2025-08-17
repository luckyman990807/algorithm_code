package tixiban.class19bfprt;

/**
 * 给定一个数组arr,和一个整数k,返回arr中第k小的数
 * k从1开始
 * <p>
 * 思路:
 * 暴力解法:
 * 先排好序,再取下标k-1的数,即便用最快的排序方法也是O(N*logN)
 * 改写快排法:
 * 每次荷兰国旗分成三部分后,如果等于区的范围涵盖了k-1,那么直接返回等于的数,否则根据范围选择对小于区或者大于区继续操作.
 * 由于走一个分支,不是项快排那样全都走完,所以复杂度优于快排,根据概率计算为O(N)
 * bfprt算法:
 * 大流程和改进快排一摸一样,只不过第一步选标准的时候,快排是随机选,bfprt是很讲究地选
 */
public class Code01BFPRT {
    public static void main(String[] args) {
        int[] arr = {1, 3, 2, 6, 4, 5, 9, 0};
        int k = 3;
        System.out.println(minKthByQuickSortRecurrence(arr, k));
        System.out.println(minKthByQuickSortIteration(arr, k));
        System.out.println(minKthByBfprt(arr, k));
    }

    /**
     * 方法一:改写快排递归版
     */
    public static int minKthByQuickSortRecurrence(int[] arr, int k) {
        int[] srrCopy = new int[arr.length];
        System.arraycopy(arr, 0, srrCopy, 0, arr.length);
        // 参数k是从1开始的,但是我们取第几个数,是从0开始的
        return process(srrCopy, 0, arr.length - 1, k - 1);
    }

    /**
     * 快排改进的取第k小的数
     *
     * @param arr
     * @param left  处理的左边界(包含)
     * @param right 处理的右边界(包含)
     * @param index 取排好序后的下标为index的数
     * @return
     */
    public static int process(int[] arr, int left, int right, int index) {
        if (left == right) {
            return arr[left];
        }
        // 随机选一个数做划分标准
        // [0,1)*(right-left+1)=[0,right-left+1)
        // +left=[left,right+1)
        // int=[left,right]
        /** 注意一定要选一个数值,做标准,而不是选一个下标,因为下标上的数组在partition的过程中会变化 */
        int flag = arr[(int) (Math.random() * (right - left + 1) + left)];
        int[] range = partition(arr, left, right, flag);

        if (index >= range[0] && index <= range[1]) {
            return flag;
        } else if (index < range[0]) {
            return process(arr, left, range[0] - 1, index);
        } else {
            return process(arr, range[1] + 1, right, index);
        }
    }

    /**
     * 荷兰国旗,给定一个数组arr和一个标准位置flag,在指定区域内把数组划分成三部分:比arr[flag]小的区域、和arr[flag]相等的区域、比arr[flag]大的区域
     * 返回等于区域的第一个位置和最后一个位置
     */
    public static int[] partition(int[] arr, int left, int right, int flag) {
        int less = left - 1;
        int more = right + 1;
        int cur = left;

        while (cur < more) {
            if (arr[cur] == flag) {
                cur++;
            } else if (arr[cur] < flag) {
                swap(arr, ++less, cur++);
            } else {
                swap(arr, --more, cur);
            }
        }
        /** 注意一定要返回等于区域的起始位置,不要返回小于区的最后一个位置和大于区的第一个位置,因为当整个数组全是等于区时会返回left-1和right+1就错了 */
        return new int[]{less + 1, more - 1};
    }

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * 方法二:改写快排迭代版
     */
    public static int minKthByQuickSortIteration(int[] arr, int k) {
        int index = k - 1;
        int[] srrCopy = new int[arr.length];
        System.arraycopy(arr, 0, srrCopy, 0, arr.length);

        int left = 0;
        int right = srrCopy.length - 1;

        while (left < right) {
            int flag = arr[(int) (Math.random() * (right - left + 1) + left)];
            int[] range = partition(srrCopy, left, right, flag);
            if (index >= range[0] && index <= range[1]) {
                return flag;
            } else if (index < range[0]) {
                right = range[0] - 1;
            } else {
                left = range[1] + 1;
            }
        }
        return srrCopy[left];
    }


    /**
     * 方法三:bfprt算法
     * 这个方法实际用不到,因为改写快排已经很优秀了,时间复杂度已经O(N)了,bfprt时间复杂度跟他一样
     * 但是改写快排是依靠概率计算得到的O(N),bfprt是实打实的直接推导的O(N),并且bfprt上了算法导论第九章第三节
     * 所以这个方法单纯用来面试的时候装逼
     */
    public static int minKthByBfprt(int[] arr, int k) {
        int[] srrCopy = new int[arr.length];
        System.arraycopy(arr, 0, srrCopy, 0, arr.length);
        return bfprt(srrCopy, 0, arr.length - 1, k - 1);
    }

    public static int bfprt(int[] arr, int left, int right, int index) {
        if (left == right) {
            return arr[left];
        }
        // 这一句做了以下几件事:
        // 1、把arr分成每5个一组,O(1)
        // 2、每组排序,一组5个排序O(1),所有组一共O(N)
        // 3、每组拿到中位数,O(1)
        // 4、所有中位数中拿到中位数返回,T(N/5)
        int flag = medianOfMedians(arr, left, right);

        int[] range = partition(arr, left, right, flag);
        if (index >= range[0] && index <= range[1]) {
            return flag;
        } else if (index < range[0]) {
            // 至多T(7N/10)
            return bfprt(arr, left, range[0] - 1, index);
        } else {
            // 至多T(7N/10)
            return bfprt(arr, range[1] + 1, right, index);
        }
        /**
         * 所以分析bfprt时间复杂度
         * T(N)=O(N)+T(N/5)+T(7N/10)=O(N)
         */
    }

    /**
     * bfprt算法的精髓,找出中位数的中位数作为partition的标准
     * 1、把数组分为每5个一组,一共有N/5组(或N/5+1,且计为N/5)
     * 2、每组排序,取中位数.由于每组只有5个,所以每组的复杂度O(1),一共N/5组,所有组排完序的复杂度O(N)
     * 3、每组的中位数组成一个数组,长度为N/5
     * 4、递归调用bfprt,求中位数数组的中位数,即求中位数数组中第length/2小的数,复杂度T(N/5)
     *
     * 5、对于中位数数组的中位数,至少有N/5/2=N/10个中位数>=他,而对于每个中位数,他本组内至少有3个数>=他,
     * 因此对于中位数数组的中位数,数组中至少有3N/10个数>=他,取反,至多有7N/10小于他
     *
     * 6、同理,反过来也可以证明至多有7N/10大于他
     */
    private static int medianOfMedians(int[] arr, int left, int right) {
        int size = right - left + 1;
        int[] median = new int[size % 5 == 0 ? size / 5 : size / 5 + 1];
        for (int group = 0; group < median.length; group++) {
            int start = left + group * 5;
            int end = Math.min(start + 4, right);
            median[group] = getMedian(arr, start, end);
        }
        return bfprt(median, 0, median.length - 1, median.length / 2);
    }

    // 从数组中找到中位数
    private static int getMedian(int[] arr, int left, int right) {
        // 插入排序
        // 当前要把i插入到前面合适的位置
        for (int i = left + 1; i <= right; i++) {
            // j一开始就是i,只要j比前面的小,就交换,直到j和前面的相等了,或者j比前面的大了,就停
            for (int j = i; j > left && arr[j] > arr[j - 1]; j--) {
                swap(arr, j, j - 1);
            }
        }
        // 返回中位数
        return arr[(right + left) / 2];
    }
}
