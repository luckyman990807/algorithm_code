package tixiban.class33动态规划猜法外部信息简化;

// 整型数组arr长度为n(3 <= n <= 10^4)，最初每个数字是<=200的正数且满足如下条件：
// 1. 0位置的要求：arr[0]<=arr[1]
// 2. n-1位置的要求：arr[n-1]<=arr[n-2]
// 3. 中间i位置的要求：arr[i]<=max(arr[i-1],arr[i+1])
// 但是在arr有些数字丢失了，比如k位置的数字之前是正数，丢失之后k位置的数字为0
// 给定一个含有0的数组, 请你根据上述条件，计算可能有多少种不同的arr可以满足以上条件
// 比如 [6,0,9] 只有还原成 [6,9,9]满足全部三个条件，所以返回1种，即[6,9,9]达标

/**
 * 思路:加2个潜台词简化外部信息:递归函数在0到i上还原数组,同时满足i位置必须要改成v,并且用一个参数表示i的下一位比v大,还是等于v,还是比v小
 */
public class Code要求每个数小于等于左边和右边的最大值_计算可能有多少种arr满足以上条件 {
    /**
     * 暴力递归
     * 对arr数组从0到index范围上还原,
     * 已知index位置必须改成value,已知status=0表示arr[index] < arr[index+1], status=1表示arr[index]==arr[index+1], status=2表示arr[index] > arr[index+1],
     * 返回有几种不同的还原方法
     */
    public static int process(int[] arr, int index, int value, int status) {
        // 递归出口,只剩一个数要还原
        if (index == 0) {
            // 只有满足1.右边的数大于等于当前位置 2.当前位置确实是丢失了或者没丢失但正好是你要改成的数 才允许修改,
            // 修改的方法就是把i位置改成v,只有这一种方法,能修改就返回方法数1,不能修改就返回0
            return (status == 0 || status == 1) && (arr[index] == 0 || value == arr[index]) ? 1 : 0;
        }

        // i>0,还剩多个数要还原

        // 如果i位置的数没丢,并且不等于v,那么不能要求i位置非得改成v,返回0种方法
        if (arr[index] != 0 && arr[index] != value) {
            return 0;
        }

        // i位置丢了,或者i位置正好就是v,允许i位置改为v

        // 枚举可能性,可能性围绕i-1位置上的取值展开
        int ways = 0;
        // 如果a[i]已经<=arr[i+1]了,那么i位置已经满足条件了,所以i-1位置可以任意取值,不会影响i
        if (status == 0 || status == 1) {
            for (int preV = 1; preV <= 200; preV++) {
                // 当前函数是把i改成value, 下次递归是把i-1改成preV,根据value和preV的关系确定下次递归的status取值
                ways += process(arr, index - 1, preV, value > preV ? 0 : value == preV ? 1 : 2);
            }
        } else {
            // a[i]已经>arr[i+1],i位置的右边没满足条件,那么只能完全考左边来满足条件了,也就是arr[i-1]必须大于等于arr[i]
            for (int preV = value; preV <= 200; preV++) {
                // i的左边必须大于等于i,也就是i-1的右边必须小于等于i-1
                ways += process(arr, index - 1, preV, value == preV ? 1 : 2);
            }
        }

        return ways;
    }

    public static int violent(int[] arr) {
        int ways = 0;
        if (arr[arr.length - 1] == 0) {
            // 如果最右边位置丢了,那么把所有可能的数字都试一遍,但是因为是n-1位置,所以右边一定要小于他,他才能完全依赖n-2让arr[n-1]<arr[n-2]
            for (int v = 1; v <= 200; v++) {
                ways += process(arr, arr.length - 1, v, 2);
            }
        } else {
            // 如果最右边位置没丢,那么只能尝试把n-1位置设成他本来的值,右边同样也一定小于他
            ways += process(arr, arr.length - 1, arr[arr.length - 1], 2);
        }
        return ways;
    }

    public static void main(String[] args) {
        int[] arr = {6, 0, 0, 9};
        System.out.println(violent(arr));
        System.out.println(ways1(arr));
        System.out.println(dp(arr));
        System.out.println(dpPreSum(arr));
    }

    /**
     * 改动态规划
     * 最终是N*200*3的三维表,复杂度为O(N)
     * 具体点是600N * 200 ,因为一共有600N个格子,每个格子要枚举200个可能性.
     */
    public static int dp(int[] arr) {
        int[][][] dp = new int[arr.length][201][3];

        // 根据递归出口赋初值
        for (int v = 1; v <= 200; v++) {
            for (int s = 0; s < 3; s++) {
                dp[0][v][s] = (s == 0 || s == 1) && (arr[0] == 0 || v == arr[0]) ? 1 : 0;
            }
        }

        for (int i = 1; i < arr.length; i++) {
            for (int v = 1; v <= 200; v++) {
                for (int s = 0; s < 3; s++) {
                    // 根据递归试法直接改状态转移方程
                    if (arr[i] != 0 && arr[i] != v) {
                        dp[i][v][s] = 0;
                        continue;
                    }

                    if (s == 0 || s == 1) {
                        for (int preV = 1; preV <= 200; preV++) {
                            dp[i][v][s] += dp[i - 1][preV][v > preV ? 0 : v == preV ? 1 : 2];
                        }
                    } else {
                        for (int preV = v; preV <= 200; preV++) {
                            dp[i][v][s] += dp[i - 1][preV][v == preV ? 1 : 2];
                        }
                    }

                }
            }
        }

        int ways = 0;
        if (arr[arr.length - 1] == 0) {
            for (int v = 1; v <= 200; v++) {
                ways += dp[arr.length - 1][v][2];
            }
        } else {
            ways += dp[arr.length - 1][arr[arr.length - 1]][2];
        }
        return ways;
    }


    /**
     * 优化的动态规划
     * 思路:每个格子都要遍历求累加和,可以用前缀和数组代替遍历求累加和
     */
    public static int dpPreSum(int[] arr) {
        int[][][] dp = new int[arr.length][201][3];

        for (int v = 1; v <= 200; v++) {
            for (int s = 0; s < 3; s++) {
                dp[0][v][s] = (s == 0 || s == 1) && (arr[0] == 0 || v == arr[0]) ? 1 : 0;
            }
        }

        int[][] sum = new int[201][3];

        for (int i = 0; i < arr.length; i++) {

            // 提前算i-1的前缀和数组
            if (i > 0) {
                for (int v = 1; v <= 200; v++) {
                    for (int s = 0; s < 3; s++) {
                        sum[v][s] = sum[v - 1][s] + dp[i - 1][v][s];
                    }
                }
            }

            for (int v = 1; v <= 200; v++) {
                for (int s = 0; s < 3; s++) {

                    if (arr[i] != 0 && v != arr[i]) {
                        continue;
                    }

                    // 这里枚举i-1的可能性,用前缀和数组代替遍历求累加和
                    if (s == 0 || s == 1) {
                        dp[i][v][s] += sum[v - 1][0] - sum[0][0];
                        dp[i][v][s] += sum[v][1] - sum[v - 1][1];
                        dp[i][v][s] += sum[200][2] - sum[v][2];

                    } else {
                        dp[i][v][s] += sum[v][1] - sum[v - 1][1];
                        dp[i][v][s] += sum[200][2] - sum[v][2];
                    }
                }
            }
        }

        int ways = 0;
        if (arr[arr.length - 1] == 0) {
            for (int v = 1; v <= 200; v++) {
                ways += dp[arr.length - 1][v][2];
            }
        } else {
            ways += dp[arr.length - 1][arr[arr.length - 1]][2];
        }

        return ways;
    }


    /**
     * 抄的代码,用于测试
     * @param arr
     * @return
     */

    public static int ways1(int[] arr) {
        int N = arr.length;
        if (arr[N - 1] != 0) {
            return process1(arr, N - 1, arr[N - 1], 2);
        } else {
            int ways = 0;
            for (int v = 1; v < 201; v++) {
                ways += process1(arr, N - 1, v, 2);
            }
            return ways;
        }
    }

    // 如果i位置的数字变成了v,
    // 并且arr[i]和arr[i+1]的关系为s，
    // s==0，代表arr[i] < arr[i+1] 右大
    // s==1，代表arr[i] == arr[i+1] 右=当前
    // s==2，代表arr[i] > arr[i+1] 右小
    // 返回0...i范围上有多少种有效的转化方式？
    public static int process1(int[] arr, int i, int v, int s) {
        if (i == 0) { // 0...i 只剩一个数了，0...0
            return ((s == 0 || s == 1) && (arr[0] == 0 || v == arr[0])) ? 1 : 0;
        }
        // i > 0
        if (arr[i] != 0 && v != arr[i]) {
            return 0;
        }
        // i>0 ，并且， i位置的数真的可以变成V，
        int ways = 0;
        if (s == 0 || s == 1) { // [i] -> V <= [i+1]
            for (int pre = 1; pre < 201; pre++) {
                ways += process1(arr, i - 1, pre, pre < v ? 0 : (pre == v ? 1 : 2));
            }
        } else { // ? 当前 > 右 当前 <= max{左，右}
            for (int pre = v; pre < 201; pre++) {
                ways += process1(arr, i - 1, pre, pre == v ? 1 : 2);
            }
        }
        return ways;
    }
}
