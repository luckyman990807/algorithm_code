package dynamic_programing.QuadrangleInequalityOptimize;

/**
 * https://leetcode.cn/problems/split-array-largest-sum/description/
 *
 * 给定一个数组arr,数组中每个值都为正数,表示完成一幅画所需要的时间,再给定一个整数num,表示画家的数量,每个画家只能画连在一起的画.
 * 所有画家并行工作,返回完成所有的画所需要的最少时间.
 * 例如,arr=[3,1,4],num=2,返回最小时间=4.方法是让第一个画家画3和1,用时4,同时让第二个画家画4,用时4,最终所需时间为4.
 * 如果让第一个画家画3,用时3,同时第二个画家画1和4,用时5,最终所需时间为5.
 */
public class Code023Painter {
    public static int force(int[] arr, int num) {
        int[] sum = new int[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            sum[i + 1] = sum[i] + arr[i];
        }

        return process(arr.length - 1, num, sum);
    }

    /**
     * 从0到index范围的画，交给num个画家来画，返回所需要的最少时间
     * @param index
     * @param num
     * @param sum
     * @return
     */
    public static int process(int index, int num, int[] sum) {
        if (index == 0) {
            return sum[1];
        }
        if (num == 1) {
            return sum[index + 1];
        }

        int min = Integer.MAX_VALUE;
        // 定义一个划分点split，从0到split递归交给剩余num-1个画家来画，从split+1到index交给其中一个画家来画
        // 枚举split的所有可能性
        for (int split = index - 1; split >= 0; split--) {
            int restPainMin = process(split, num - 1, sum);
            int curPain = sum[index + 1] - sum[split + 1];
            int cost = Math.max(restPainMin, curPain);
            min = Math.min(min, cost);
        }

        return min;
    }

    public static void main(String[] args) {
        int[] arr = new int[]{3, 1, 4, 5, 2};
        System.out.println(force(arr, 3));
    }
}
