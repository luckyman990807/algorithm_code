package shuatiban;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Leetcode题目：https://leetcode.com/problems/freedom-trail/
 * 电子游戏“辐射4”中，任务“通向自由”要求玩家到达名为“Freedom Trail Ring”的金属表盘，并使用表盘拼写特定关键词才能开门
 * 给定一个字符串 ring，表示刻在外环上的编码；给定另一个字符串 key，表示需要拼写的关键词。您需要算出能够拼写关键词中所有字符的最少步数
 * 最初，ring 的第一个字符与12:00方向对齐。您需要顺时针或逆时针旋转 ring 以使 key 的一个字符在 12:00 方向对齐，然后按下中心按钮，以此逐个拼写完 key 中的所有字符
 * 旋转 ring 拼出 key 字符 key[i] 的阶段中：
 * 您可以将 ring 顺时针或逆时针旋转一个位置，计为1步。旋转的最终目的是将字符串 ring 的一个字符与 12:00 方向对齐，并且这个字符必须等于字符 key[i] 。
 * 如果字符 key[i] 已经对齐到12:00方向，您需要按下中心按钮进行拼写，这也将算作 1 步。按完之后，您可以开始拼写 key 的下一个字符（下一阶段）, 直至完成所有拼写。
 *
 * 思路:
 * 表盘转相当于指针转,相当于一个指针在一个逻辑环上游走来拼写字符串.
 * 从表盘的一个位置到另一个位置的最短距离是固定的(要么顺时针走要么逆时针走)
 * 可以用map记录表盘上所有字符出现的所有位置
 * 递归指定当前位置,指定下个字符,枚举下个字符的所有位置,选出最省的一种可能性
 */
public class Code021旋转表盘拼写字符串所需的最小步数 {
    /**
     * 暴力递归
     * @param ring
     * @param key
     * @return
     */
    public static int findRotateStepsViolent(String ring, String key) {
        // 构建一个map,记录每个字符在表盘上的所有位置
        Map<Character, List<Integer>> map = new HashMap<>();
        char[] ringChars = ring.toCharArray();
        for (int i = 0; i < ringChars.length; i++) {
            if (!map.containsKey(ringChars[i])) {
                map.put(ringChars[i], new ArrayList<>());
            }
            map.get(ringChars[i]).add(i);
        }

        // 表盘当前位置在0(也就是12:00位置),下一个要拼的字符是key[0],返回拼出从key[0]到整个key的最少步数
        return process(0, 0, key.toCharArray(), map, ringChars.length);
    }

    /**
     * 暴力递归
     */
    public static int process(int index, int next, char[] key, Map<Character, List<Integer>> map, int ringLength) {
        // 如果key已经拼完了,返回0,不需要任何步数
        if (next == key.length) {
            return 0;
        }
        // 遍历要拼的字母在表盘上的所有位置,可能性1:转到位置1,然后拼下一个字符;可能性2:转到位置2,然后拼下一个字符...取步数最少的可能性
        int min = Integer.MAX_VALUE;
        for (Integer nextIndex : map.get(key[next])) {
            min = Math.min(min, rollStep(index, nextIndex, ringLength) + process(nextIndex, next + 1, key, map, ringLength));
        }
        return min;
    }

    /**
     * 计算从表盘的i1位置转到i2位置最少需要几步(就两种可能,要么顺时针转要么逆时针转)
     */
    public static int rollStep(int i1, int i2, int length) {
        if (i1 == i2) {
            return 1;
        }
        return 1 + Math.min(Math.abs(i1 - i2), length - Math.abs(i1 - i2));
    }


    /**
     * 改记忆化搜索
     * 改不了动态规划,因为两个自变量中当前表盘的位置index的依赖关系不固定,不一定map中记录的是哪些index,没法推动态转移方程
     */
    public static int findRotateSteps(String ring, String key) {
        Map<Character, List<Integer>> map = new HashMap<>();
        char[] ringChars = ring.toCharArray();
        for (int i = 0; i < ringChars.length; i++) {
            if (!map.containsKey(ringChars[i])) {
                map.put(ringChars[i], new ArrayList<>());
            }
            map.get(ringChars[i]).add(i);
        }

        Map<Integer, Map<Integer, Integer>> dp = new HashMap<>();
        return processDp(0, 0, key.toCharArray(), map, ringChars.length, dp);

    }

    /**
     * 记忆化搜索的递归
     */
    public static int processDp(int index, int next, char[] key, Map<Character, List<Integer>> map, int ringLength, Map<Integer, Map<Integer, Integer>> dp) {
        // 如果key已经拼完了,返回0,不需要任何步数
        if (next == key.length) {
            return 0;
        }
        // 先查缓存
        if (dp.containsKey(next) && dp.get(next).containsKey(index)) {
            return dp.get(next).get(index);
        }
        // 遍历要拼的字母在表盘上的所有位置,可能性1:转到位置1,然后拼下一个字符;可能性2:转到位置2,然后拼下一个字符...取步数最少的可能性
        int min = Integer.MAX_VALUE;
        for (Integer nextIndex : map.get(key[next])) {
            min = Math.min(min, rollStep(index, nextIndex, ringLength) + processDp(nextIndex, next + 1, key, map, ringLength, dp));
        }
        // 返回前记录缓存
        if (!dp.containsKey(next)) {
            dp.put(next, new HashMap<>());
        }
        dp.get(next).put(index, min);

        return min;
    }

}
