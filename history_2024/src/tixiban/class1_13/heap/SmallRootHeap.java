package tixiban.class1_13.heap;

public class SmallRootHeap {

    /**
     * 小根堆
     */
    public static class MyHeap {
        private int[] arr;
        private int heapSize;
        private int maxSize;

        public MyHeap(int size) {
            this.arr = new int[size];
            this.maxSize = size;
        }

        public void add(int value) {
            if (this.heapSize >= this.maxSize) {
                throw new RuntimeException("堆已满");
            }

            this.arr[heapSize++] = value;
            heapInsert(this.arr, heapSize);
        }

        public int popMin() {
            if (this.heapSize <= 0) {
                throw new RuntimeException("堆已空");
            }
            int result = this.arr[0];
            swap(this.arr, 0, --heapSize);
            heapify(this.arr, 0, heapSize);
            return result;
        }

        private void heapInsert(int[] arr, int cur) {
            // 只要比父节点小，就交换
            while (arr[cur] < arr[(cur - 1) / 2]) {
                swap(arr, cur, (cur - 1) / 2);
                cur = (cur - 1) / 2;
            }
        }

        private void heapify(int[] arr, int cur, int heapSize) {
            int left = cur * 2 + 1;
            while (left < heapSize) {
                int smaller = left + 1 < heapSize && arr[left + 1] < arr[left] ? left + 1 : left;
                if (arr[cur] <= smaller) {
                    break;
                }
                swap(arr, cur, smaller);
                cur = smaller;
                left = cur * 2 + 1;
            }
        }

        public static void swap(int[] arr, int a, int b) {
            int temp = arr[a];
            arr[a] = arr[b];
            arr[b] = temp;
        }
    }


    /**
     * 把给定的数组排序，要求时间复杂度O(N)，已知排序前后所有元素移动距离不超过step
     * 思路：
     * 排序后0位置的数只会分布在原始数组0~step位置，所以这几个数建堆，弹出最小值就是0位置该放的数。
     * 同理1位置的数只会分布在原数组0~step+1位置，所以把这个范围上除了已排好的1个剩下的建堆（在上一步的基础上新插入step+1位置），弹出最小的就是1位置该放的数
     * 以此类推，插一个弹一个
     */
    public static void heapSortWithStep(int[] arr, int step) {
        if (null == arr || arr.length < 2) {
            return;
        }

        MyHeap myHeap = new MyHeap(step);

        // 如果数组长度不超过step，直接全部插入小根堆，再依次弹出最小值
        if (arr.length <= step) {
            for (int i = 0; i < arr.length; i++) {
                myHeap.add(arr[i]);
            }
            for (int i = 0; i < arr.length; i++) {
                arr[i] = myHeap.popMin();
            }
        } else {
            // 先把step个元组插入小根堆
            for (int i = 0; i < step; i++) {
                myHeap.add(arr[i]);
            }
            // 每插入一个元素，就弹出一个最小值
            for (int i = step; i < arr.length; i++) {
                myHeap.add(arr[i]);
                arr[i - step] = myHeap.popMin();
            }
            // 最后剩下step个元素依次弹出最小值
            for (int i = step; i < arr.length; i++) {
                arr[i] = myHeap.popMin();
            }
        }
    }
}
