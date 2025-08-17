package dynamic_programing;

public class Code003Bag {
    public static int force(int[] weight, int[] value, int volume) {
        return process(0, volume, weight, value);
    }

    public static int process(int l, int rest, int[] weight, int[] value) {
        if (l == weight.length - 1) {
            return weight[l] <= rest ? value[l] : 0;
        }

        int p1 = process(l + 1, rest, weight, value);
        int p2 = weight[l] <= rest ? value[l] + process(l + 1, rest - weight[l], weight, value) : 0;
        return Math.max(p1, p2);
    }

    public static int dp(int[] weight, int[] value, int volume) {
        int[][] dp = new int[weight.length][volume + 1];
        for (int rest = 0; rest <= volume; rest++) {
            dp[weight.length - 1][rest] = weight[weight.length - 1] <= rest ? value[weight.length - 1] : 0;
        }

        for (int l = weight.length - 2; l >= 0; l++) {
            for (int rest = 0; rest <= volume; rest++) {
                int p1 = dp[l + 1][rest];
                int p2 = weight[l] <= rest ? value[l] + dp[l + 1][rest - weight[l]] : 0;
                dp[l][rest] = Math.max(p1, p2);
            }
        }

        return dp[0][volume];
    }
}
