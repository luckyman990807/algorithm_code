package shuatiban;

/**
 * https://leetcode.com/problems/jump-game-ii/
 *
 * 给定一个长度为 n 的 0 索引整数数组 nums。初始位置为 nums[0]。
 * 每个元素 nums[i] 表示从索引 i 向前跳转的最大长度。换句话说，如果你在 nums[i] 处，你可以跳转到任意 nums[i + j] 处:
 * 0 <= j <= nums[i]
 * i + j < n
 * 返回到达 nums[n - 1] 的最小跳跃次数。生成的测试用例可以到达 nums[n - 1]。
 */
public class Code054跳跃游戏_从开头最少跳几次到达末尾 {
    public int jump(int[] nums) {
        // 按照最优策略，当前跳了几步
        int step = 0;
        // 跳step步最远能到哪
        int curStepFarthest = 0;
        // 跳step+1步最远能到哪
        int nextStepFarthest = nums[0];

        for (int i = 1; i < nums.length; i++) {
            // 如果跳step步最远也到不了当前位置i
            if (curStepFarthest < i) {
                // 那就再跳一步之后，step步最远能到的地方肯定比i大了，因为i是一个数一个数地超过cur的
                step++;
                // 此时跳step步最远能到哪=上一步的nextStepFarthest
                curStepFarthest = nextStepFarthest;
            }
            // 跳step+1步最远能到哪，需要实时更新，因为从当前i到step步最远能到的curStepFarthest之间所有点，都可能是跳的最远的点
            // 既然step步最远不小于i，那么step+1步最远也不小于i + nums[i]
            nextStepFarthest = Math.max(nextStepFarthest, i + nums[i]);
        }
        return step;
    }
}
