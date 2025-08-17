package shuatiban;

/**
 * 腾讯原题
 * 给定整数power，给定一个数组arr，给定一个数组reverse。含义如下：
 * arr的长度一定是2的power次方，reverse中的每个值一定都在0~power范围。
 * 例如power = 2, arr = {3, 1, 4, 2}，reverse = {0, 1, 0, 2}
 * 任何一个在前的数字可以和任何一个在后的数组，构成一对数。可能是升序关系、相等关系或者降序关系。
 * 比如arr开始时有如下的降序对：(3,1)、(3,2)、(4,2)，一共3个。
 * 接下来根据reverse对arr进行调整：
 * reverse[0] = 0, 表示在arr中，划分每1(2的0次方)个数一组，然后每个小组内部逆序，那么arr变成
 * [3,1,4,2]，此时有3个逆序对。
 * reverse[1] = 1, 表示在arr中，划分每2(2的1次方)个数一组，然后每个小组内部逆序，那么arr变成
 * [1,3,2,4]，此时有1个逆序对
 * reverse[2] = 0, 表示在arr中，划分每1(2的0次方)个数一组，然后每个小组内部逆序，那么arr变成
 * [1,3,2,4]，此时有1个逆序对。
 * reverse[3] = 2, 表示在arr中，划分每4(2的2次方)个数一组，然后每个小组内部逆序，那么arr变成
 * [4,2,3,1]，此时有4个逆序对。
 * 所以返回[3,1,1,4]，表示每次调整之后的逆序对数量。
 * 输入数据状况：
 * power的范围[0,20]
 * arr长度范围[1,10的7次方]
 * reverse长度范围[1,10的6次方]
 *
 *
 * 思路：
 * 根据归并排序的merge思想，求出arr每2个一组有几个逆序对几个正序对、每4个一组有几个逆序对几个正序对（前面算过的每2个一组的这次不计入）、...、每2^power个一组有几个逆序对几个正序对，分别记录到正序对、逆序对两个辅助数组中，
 * 遍历reverse数组，每2的reverse[i]次方个数翻转，对正序/逆序对的影响是：
 * reverse[i]及起以下的分组的逆序对个数和正序对个数互换，reverse[i]以上的分组不变。例如reverse[i]=3，那么辅助数组中1、2、3个数一组的逆序对个数和正序对个数互换，4、5个数一组的不变。
 * 遍历完reverse数组后累加所有逆序对个数返回。
 */
public class Code084求每次翻转后的逆序对数量 {
    public static int reversePairs(int power, int[] arr, int[] reverse) {
        // ascPair[i]=j表示每i个一组共有j个正序对
        int[] ascPair = new int[power + 1];
        // descPair[i]=j表示每i个一组共有j个逆序对
        int[] descPair = new int[power + 1];
        // 计算每2个一组、每4个一组...每2的power次方个一组分别有几个正序/逆序对
        process(arr, 0, power, ascPair, descPair);

        // 执行翻转操作，例如rev=2，就是把每4个一组翻转，那么每2、4个一组的逆序对个数变成正序对个数，正序对个数变成逆序对个数，而每8、16...个一组的正序/逆序对个数不变
        for (int rev : reverse) {
            for (int p = 1; p <= rev; p++) {
                int temp = ascPair[p];
                ascPair[p] = descPair[p];
                descPair[p] = temp;
            }
        }

        // 计算翻转完毕之后的所有逆序对个数，返回
        int sum = 0;
        for (int i = 0; i < descPair.length; i++) {
            sum += descPair[i];
        }
        return sum;
    }

    public static void process(int[] arr, int left, int power, int[] ascPair, int[] descPair) {
        if (power == 0) {
            return;
        }
        int mid = left + (1 << power - 1) - 1;
        // 把left到mid也就是左半边弄有序，并且统计每2的power-1次方个一组的正序/逆序对个数到ascPair和descPair中
        process(arr, left, power - 1, ascPair, descPair);
        // 把mid+1到right也就是右半边弄有序，并且统计每2的power-1次方个一组的正序/逆序对个数到ascPair和descPair中
        process(arr, mid + 1, power - 1, ascPair, descPair);
        // 两边都有序了，就把两边整体弄有序，并且统计每2的power次方个一组的正序/逆序对个数到ascPair和descPair中
        merge(arr, left, mid, mid + (1 << power - 1), power, ascPair, descPair);
    }

    public static void merge(int[] arr, int left, int mid, int right, int power, int[] ascPair, int[] descPair) {
        int curLeft = left;
        int curRight = mid + 1;
        int[] merged = new int[right - left + 1];
        int mergedI = 0;
        while (curLeft <= mid && curRight <= right) {
            // 正序对
            if (arr[curLeft] < arr[curRight]) {
                // 因为left到mid升序，mid+1到right也升序，如果curLeft<curRight，那么curLeft就<curRight到right的所有，于是就有right-curRight+1个升序对
                ascPair[power] += right - curRight + 1;
                // 较小的那个数放入归并数组
                merged[mergedI++] = arr[curLeft++];
            }
            // 逆序对
            if (arr[curLeft] > arr[curRight]) {
                // 因为left到mid升序，mid+1到right也升序，如果curLeft>curRight，那么curLeft到mid的所有就都>curRight，于是就有了mid-curLeft+1个逆序对
                descPair[power] += mid - curLeft + 1;
                // 较小的那个数放入归并数组
                merged[mergedI++] = arr[curRight++];
            }
        }
        while (curLeft <= mid) {
            merged[mergedI++] = arr[curLeft++];
        }
        while (curRight <= right) {
            merged[mergedI++] = arr[curRight++];
        }
        // 写入归并后的数组
        for (int i = 0; i < mergedI; i++) {
            arr[left + i] = merged[i];
        }
    }

    public static void main(String[] args) {
        int power = 3;
        int[] arr = {4, 2, 3, 5, 0, 7, 1, 6};
        int[] reverse = {2};
        System.out.println(reversePairs(power, arr, reverse));
    }

}
