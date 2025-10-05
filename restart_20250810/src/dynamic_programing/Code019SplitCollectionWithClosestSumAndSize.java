package dynamic_programing;


/**
 * 给定一个集合，拆分成两个子集，要求两个子集的长度和累加和都最接近，返回其中较小的累加和
 *
 * 思路：转换成背包问题，增加一个参数限制物品的个数。
 * 尝试策略：process(arr, index, restVolume, restNum)，表示arr集合中从index往后挑，一共挑restNum个，容量不超过restVolume，求最大累加和
 *
 * 关键的点在于，原集合长度是奇数和偶数时要分别讨论，取较大的，为什么？
 * 因为挑n/2个最大容量不超过100，和挑n/2+1个最大容量不超过100，是两种情况
 * 举个挑n/2+1个最优的例子，arr=[1,1,1,1,100]，最优解是挑n/2+1=3个最大容量不超过52，得到较小的子集[1,1,1]累加和3。而不是挑n/2=2个，累加和=2。
 * 再举个挑n/2个最优的例子，arr=[100, 100, 100, 100, 1]，最优解是挑n/2=2个最大容量不超过200，得到较小子集[100, 100]，累加和200。而不是挑n/2+1=3个，累加和=201。
 */
public class Code019SplitCollectionWithClosestSumAndSize {
    public static int force(int[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }

        if (arr.length % 2 == 0) {
            return process(arr, 0, sum / 2, arr.length / 2);
        } else {
            return Math.max(process(arr, 0, sum / 2, arr.length / 2), process(arr, 0, sum / 2, arr.length / 2 + 1));
        }
    }

    public static int process(int[] arr, int index, int restVolume, int restNum) {
        if (index == arr.length) {
            return restNum == 0 ? 0 : Integer.MIN_VALUE;
        }
        if (restNum == 0) {
            return 0;
        }

        int p1 = process(arr, index + 1, restVolume, restNum);
        int p2 = Integer.MIN_VALUE;
        if (arr[index] <= restVolume) {
            int next = process(arr, index + 1, restVolume - arr[index], restNum - 1);
            if (next != Integer.MIN_VALUE) {
                p2 = arr[index] + next;
            }
        }

        return Math.max(p1, p2);
    }

    public static int dp(int[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }

        int[][][] dp = new int[arr.length + 1][sum / 2 + 1][arr.length / 2 + 2];

        // 初始状态
        // index=arr.length时，只有restNum也=0，结果才有效，此时最大累加和为0。否则index遍历完了，但是压根没有挑够restNum个，那么这枝尝试是失败的，记为无效
        for (int restVolume = 0; restVolume < dp[0].length; restVolume++) {
            for (int restNum = 1; restNum < dp[0][0].length; restNum++) {
                dp[arr.length][restVolume][restNum] = Integer.MIN_VALUE;
            }
        }

        for (int index = arr.length - 1; index >= 0; index--) {
            for (int restVolume = 0; restVolume < dp[0].length; restVolume++) {
                for (int restNum = 0; restNum < dp[0][0].length; restNum++) {
                    int p1 = dp[index + 1][restVolume][restNum];
                    int p2 = Integer.MIN_VALUE;
                    if (arr[index] <= restVolume && restNum > 0) {
                        int next = dp[index + 1][restVolume - arr[index]][restNum - 1];
                        if (next != Integer.MIN_VALUE) {
                            p2 = arr[index] + next;
                        }
                    }
                    dp[index][restVolume][restNum] = Math.max(p1, p2);
                }
            }
        }

        if (arr.length % 2 == 0) {
            return dp[0][sum / 2][arr.length / 2];
        } else {
            return Math.max(dp[0][sum / 2][arr.length / 2], dp[0][sum / 2][arr.length / 2 + 1]);
        }
    }

    public static void main(String[] args) {
        int[] arr = new int[]{1, 1, 1, 1, 100};
//        int[] arr = new int[]{100, 100, 100, 100, 1};

        System.out.println(force(arr));
        System.out.println(dp(arr));
    }
}
