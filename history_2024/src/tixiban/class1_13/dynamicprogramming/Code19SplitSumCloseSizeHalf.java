package tixiban.class1_13.dynamicprogramming;

/**
 * 给定一个正整数数组arr，请把arr中所有的数分成两个集合，要求
 * 如果arr长度是偶数，两个集合中包含的数必须一样多
 * 如果arr长度是奇数，两个集合中包含的数必须只差一个
 * 在此基础上尽量让两个集合的累加和接近
 * 返回较小的累加和
 */
public class Code19SplitSumCloseSizeHalf {
    /**
     * 暴力递归法
     * index之前的不管，从index开始，必须拿够picks个，返回最趋近restSum的累加和
     */
    public static int process(int[] arr, int index, int picks, int restSum) {
        // 当没有数可用的时候，只有要求的数的个数也=0，才是有效解，返回累加和0。否则现在没数了，但要求的个数没凑够，属于无效解，返回-1，表示无法达成
        if (index == arr.length) {
            return picks == 0 ? 0 : -1;
        }
        // 可能性1、不要当前数
        int p1 = process(arr, index + 1, picks, restSum);
        // 可能性2、要当前数
        int p2 = -1;
        int nextSum = -1;
        // 递归前保证了restSum和picks不会=0。其实picks就算<0也无所谓，递归到index==arr.length一样返回-1，可以算，但是限制一下picks>=0可以提前剪枝
        if (restSum >= arr[index] && picks - 1 >= 0) {
            nextSum = process(arr, index + 1, picks - 1, restSum - arr[index]);
        }
        if (nextSum != -1) {
            p2 = arr[index] + nextSum;
        }
        return Math.max(p1, p2);
    }

    public static int splitSumViolent(int[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }

        if ((arr.length & 1) == 0) {
            return process(arr, 0, arr.length / 2, sum / 2);
        } else {
            // 当arr长度为奇数时，假设长度为5，累加和为100，那么有两种可能性，1、必须拿够2个数，返回最趋近50的累加和，2、必须拿够3个数，返回最趋近50的累加和。
            // 两种情况都要求出来，取最接近50的那个。最接近的其实就是最大的，因为两个都是<=50，谁大谁就是最接近的。
            // 举个例子，arr=[100, 1, 1, 1, 1]，有5个数，累加和=104，一般累加和=52
            // 可能性1、递归函数必须挑2个数，那么返回两个集合的累加和分别为1+1=2、100+1+1=102，相差100，较小的累加和=2
            // 可能性2、递归函数必须挑3个数，那么返回两个集合的累加和分别为1+1+1=3、100+1=101，相差98，较小的累加和=3，这个更接近52
            return Math.max(
                    process(arr, 0, arr.length / 2, sum / 2),
                    process(arr, 0, arr.length / 2 + 1, sum / 2)
            );
        }
    }

    /**
     * 严格表依赖的动态规划，三个可变参数，三维表
     * @param arr
     * @return
     */
    public static int splitSumDp(int[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }

        // 可变参数index范围0~arr.length，
        // 可变参数picks范围，最小值可以取到0，最大值，arr长度6的时候可以取到2，长度5的时候既可能取到2也可能取到3，因此是向上取整
        // 可变参数restSum范围0~sum/2
        int[][][] dp = new int[arr.length + 1][(arr.length + 1) / 2 + 1][sum / 2 + 1];

        // 因为有无效场景，所以初始化全是-1，有效的才会改成正的
        for (int i = 0; i < dp.length; i++) {
            for (int j = 0; j < dp[0].length; j++) {
                for (int k = 0; k < dp[0][0].length; k++) {
                    dp[i][j][k] = -1;
                }
            }
        }

        // base case
        for (int restSum = 0; restSum <= sum / 2; restSum++) {
            dp[arr.length][0][restSum] = 0;
        }

        for (int index = arr.length - 1; index >= 0; index--) {
            for (int picks = 0; picks <= (arr.length + 1) / 2; picks++) {
                for (int restSum = 0; restSum <= sum / 2; restSum++) {
                    // 从暴力递归抄过来的
                    int p1 = dp[index + 1][picks][restSum];
                    int p2 = -1;
                    int nextSum = -1;
                    if (restSum >= arr[index] && picks - 1 >= 0) {
                        nextSum = dp[index + 1][picks - 1][restSum - arr[index]];
                    }
                    if (nextSum != -1) {
                        p2 = arr[index] + nextSum;
                    }
                    dp[index][picks][restSum] = Math.max(p1, p2);
                }
            }
        }

        if ((arr.length & 1) == 0) {
            return dp[0][arr.length / 2][sum / 2];
        } else {
            return Math.max(dp[0][arr.length / 2][sum / 2], dp[0][arr.length / 2 + 1][sum / 2]);
        }
    }

    public static void main(String[] args) {
        int[] arr = {100, 1, 1, 1, 1};
        System.out.println(splitSumViolent(arr));
        System.out.println(splitSumDp(arr));
    }
}
