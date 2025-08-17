package shuatiban;

/**
 * 阿里多年面试题
 * https://leetcode.com/problems/super-washing-machines/
 * 有n台洗衣机，每一轮，没台洗衣机都可以选择向左边丢一件衣服，或者向右边丢一件衣服，或者不丢。问最少几轮可以让每台洗衣机中的衣服一样多
 *
 * 思路：奇技淫巧
 *
 * 假设计算平均数的到最终每台洗衣机里应当有v件衣服，
 * 当前来到i位置的洗衣机，左边的所有洗衣机的衣服总数 跟左边所有洗衣机应当有的衣服总数的差值为a，右边所有洗衣机的所有衣服总数 跟右边所有洗衣机应当有的衣服总数的差值为b，那么
 * 如果a<0并且b<0，那么最少a+b轮能让i位置再也不用动了，因为i要向左边丢(v*左边洗衣机数-a)件，还要向右边丢(v*右边洗衣机数-b)件
 * 如果a>0并且b>0，那么最少max(a,b)轮能让i位置再也不用动了，因为左边要向i丢(a-v*左边洗衣机数)件，同时并行着右边要向i丢(b-v*右边洗衣机数)件
 * 如果a>0并且b<0，那么最少max(a,b)轮能让i位置再也不用动了，因为左边要向i丢(a-v*左边洗衣机数)件，同时并行着i要向右边丢(v*右边洗衣机数-b)件
 * 如果a<0并且b>0，同理，最少max(a,b)轮能让i位置再也不用动了
 *
 * 计算每个位置最少需要几轮可以不用再动了，取最大值就是答案
 */
public class Code067超级洗衣机 {
    public static int findMinMoves(int[] machines) {
        // 前缀和数组
        int[] preSum = new int[machines.length];
        preSum[0] = machines[0];
        for (int i = 1; i < machines.length; i++) {
            preSum[i] = preSum[i - 1] + machines[i];
        }
        // 累加和
        int sum = preSum[machines.length - 1];
        // 如果累加和除以洗衣机数量除不尽，那么不可能搞成一样多
        if (sum % machines.length != 0) {
            return -1;
        }
        // 每台洗衣机应当最终拥有的衣服数，也就是平均数
        int avg = sum / machines.length;

        int ans = 0;
        for (int i = 0; i < machines.length; i++) {
            // 左边总数差多少
            int leftGap = i == 0 ? 0 : preSum[i] - machines[i] - avg * i;
            // 右边总数差多少
            int rightGap = i == machines.length - 1 ? 0 : sum - preSum[i] - avg * (machines.length - 1 - i);
            int curAns;
            if (leftGap < 0 && rightGap < 0) {
                // 如果两边差值都小于0，也就是当前位置既要向左边丢衣服，又要向右边丢衣服，那么总共差几件衣服就最少多少轮能让当前位置不需要动了
                curAns = -leftGap - rightGap;
            } else {
                // 否则，需要两边差值的max轮
                curAns = Math.max(Math.abs(leftGap), Math.abs(rightGap));
            }
            ans = Math.max(ans, curAns);
        }
        return ans;
    }
}
