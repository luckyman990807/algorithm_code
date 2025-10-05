package dynamic_programing;

import java.util.HashMap;
import java.util.Map;

/**
 * https://leetcode.cn/problems/stickers-to-spell-word/description/
 * 给定n种不同的贴纸，每个贴纸上是一个小写英文单词。
 * 给定一个目标单词target。
 * 问最少需要多少张贴纸可以拼出目标单词。
 * 每种贴纸的数量是无限的，每张贴纸可以剪成单个单词使用。如果无论如何也拼不出目标单词，则返回-1
 */
public class Code005TickersToSpellWord {
    /**
     * 解法一阶段：暴力递归
     * 思路：面对目标单词，第一种可能性：上来先用第一种贴纸去抵消掉一部分目标字符，剩下的部分递归交给下一轮去抵消，拿到剩余部分抵消所需要的最少贴纸数；
     * 第二种可能性：上来先用第二种贴纸去抵消……
     * 第n种可能性：上来先用第n种贴纸去抵消……
     * 最后选取使用贴纸最少的可能性
     *
     * 怎么抵消：
     * 记录下贴纸的每个字母的词频，记录下目标单词的每个字母的词频，从目标单词的词频中减去贴纸的词频
     *
     * @param stickers
     * @param target
     * @return
     */
    public static int force(String[] stickers, String target) {
        int min = process(stickers, target);
        return min == Integer.MAX_VALUE ? -1 : min;
    }

    public static int process(String[] stickers, String target) {
        if (target.length() == 0) {
            return 0;
        }

        int min = Integer.MAX_VALUE;
        for (String sticker : stickers) {
            // 统计当前贴纸的词频
            int[] stickerWords = new int[26];
            char[] stickerCharArr = sticker.toCharArray();
            for (char c : stickerCharArr) {
                stickerWords[c - 'a']++;
            }

            // 统计目标单词的词频
            int[] targetWords = new int[26];
            char[] targetCharArr = target.toCharArray();
            for (char c : targetCharArr) {
                targetWords[c - 'a']++;
            }

            // 用贴纸的词频抵消掉目标单词的词频
            for (int i = 0; i < stickerWords.length; i++) {
                targetWords[i] -= stickerWords[i];
            }

            // 拼接出当前贴纸抵消后的剩余目标单词
            StringBuilder rest = new StringBuilder();
            for (int i = 0; i < targetWords.length; i++) {
                if (targetWords[i] > 0) {
                    for (int j = 0; j < targetWords[i]; j++) {
                        rest.append((char) ('a' + i));
                    }
                }
            }

            if (rest.length() == target.length()) {
                continue;
            }
            int restMin = process(stickers, rest.toString());
            min = Math.min(min, restMin == Integer.MAX_VALUE ? Integer.MAX_VALUE : 1 + restMin);
        }
        return min;
    }

    /**
     * 解法第二阶段：预处理贴纸字符数组+剪枝
     * @param stickers
     * @param target
     * @return
     */
    public static int forcePreCharArrAndBranchPruning(String[] stickers, String target) {
        // 提前转换好每张贴纸的字符数组，避免重复转换
        char[][] stickersCharArr = new char[stickers.length][];
        for (int i = 0; i < stickers.length; i++) {
            stickersCharArr[i] = stickers[i].toCharArray();
        }

        int min = processBranchPruning(stickersCharArr, target.toCharArray());
        return min == Integer.MAX_VALUE ? -1 : min;
    }

    /**
     * 剪枝版递归process
     * @param stickers
     * @param target
     * @return
     */
    public static int processBranchPruning(char[][] stickers, char[] target) {
        if (target.length == 0) {
            return 0;
        }
        int min = Integer.MAX_VALUE;
        for (char[] sticker : stickers) {
            // 统计当前贴纸词频
            int[] stickerWords = new int[26];
            for (char c : sticker) {
                stickerWords[c - 'a']++;
            }
            // 如果当前贴纸不包含目标单词的首字母，那么直接砍掉这一枝的尝试
            // 为什么？首字母总会在某次尝试中被消除掉的，那我就指定为这次，某个字母先消还是后消不影响结果。
            // 这里剪枝的好处是能够限制本次使用的贴纸一定是能抵消的，不会混进来那种没用的没法做任何抵消的贴纸
            if (stickerWords[target[0] - 'a'] <= 0) {
                continue;
            }

            // 统计目标单词词频
            int[] targetWords = new int[26];
            for (char c : target) {
                targetWords[c - 'a']++;
            }

            // 用贴纸的词频抵消掉目标的词频
            for (int i = 0; i < stickerWords.length; i++) {
                targetWords[i] -= stickerWords[i];
            }

            // 拼接剩余的目标字符串
            StringBuilder rest = new StringBuilder();
            for (int i = 0; i < targetWords.length; i++) {
                if (targetWords[i] > 0) {
                    for (int j = 0; j < targetWords[i]; j++) {
                        rest.append((char) ('a' + i));
                    }
                }
            }

            if (rest.length() == target.length) {
                continue;
            }
            int restMin = processBranchPruning(stickers, rest.toString().toCharArray());
            min = Math.min(min, restMin == Integer.MAX_VALUE ? Integer.MAX_VALUE : 1 + restMin);
        }
        return min;
    }


    public static int dp(String[] stickers, String target) {
        Map<String, Integer> dp = new HashMap<>();
        int min = processDp(stickers, target, dp);
        return min == Integer.MAX_VALUE ? -1 : min;
    }

    public static int processDp(String[] stickers, String target, Map<String, Integer> dp) {
        if (target.length() == 0) {
            return 0;
        }

        if (dp.containsKey(target)) {
            return dp.get(target);
        }

        int min = Integer.MAX_VALUE;
        for (String sticker : stickers) {
            int[] stickerWords = new int[26];
            char[] stickerCharArr = sticker.toCharArray();
            for (char c : stickerCharArr) {
                stickerWords[c - 'a']++;
            }

            int[] targetWords = new int[26];
            char[] targetCharArr = target.toCharArray();
            if (stickerWords[targetCharArr[0] - 'a'] <= 0) {
                continue;
            }
            for (char c : targetCharArr) {
                targetWords[c - 'a']++;
            }

            for (int i = 0; i < stickerWords.length; i++) {
                targetWords[i] -= stickerWords[i];
            }

            StringBuilder rest = new StringBuilder();
            for (int i = 0; i < targetWords.length; i++) {
                if (targetWords[i] > 0) {
                    for (int j = 0; j < targetWords[i]; j++) {
                        rest.append((char) ('a' + i));
                    }
                }
            }
            if (rest.length() == target.length()) {
                continue;
            }
            int restMin = processDp(stickers, rest.toString(), dp);
            min = Math.min(min, restMin == Integer.MAX_VALUE ? Integer.MAX_VALUE : 1 + restMin);
        }
        dp.put(target, min);
        return min;
    }

    public static int dpPreCharArrAndBranchPruning(String[] stickers, String target) {
        char[][] stickersCharArr = new char[stickers.length][];
        int[][] stickersWords = new int[stickers.length][26];
        for (int i = 0; i < stickers.length; i++) {
            stickersCharArr[i] = stickers[i].toCharArray();
            for (char c : stickersCharArr[i]) {
                stickersWords[i][c - 'a']++;
            }
        }

        int min = processDpPreCharArrAndBranchPruning(stickersCharArr, stickersWords, target, new HashMap<>());
        return min == Integer.MAX_VALUE ? -1 : min;
    }

    public static int processDpPreCharArrAndBranchPruning(char[][] stickers, int[][] stickersWords, String target, Map<String, Integer> cache) {
        if (target.length() == 0) {
            return 0;
        }
        if (cache.containsKey(new String(target))) {
            return cache.get(new String(target));
        }

        char[] targetCharArr = target.toCharArray();
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < stickers.length; i++) {
            if (stickersWords[i][targetCharArr[0] - 'a'] <= 0) {
                continue;
            }

            int[] targetWords = new int[26];
            for (char c : targetCharArr) {
                targetWords[c - 'a']++;
            }
            for (int j = 0; j < stickersWords[i].length; j++) {
                targetWords[j] -= stickersWords[i][j];
            }

            StringBuilder rest = new StringBuilder();
            for (int j = 0; j < targetWords.length; j++) {
                if (targetWords[j] > 0) {
                    for (int k = 0; k < targetWords[j]; k++) {
                        rest.append((char) ('a' + j));
                    }
                }
            }
            if (rest.length() == target.length()) {
                continue;
            }
            int restMin = processDpPreCharArrAndBranchPruning(stickers, stickersWords, rest.toString(), cache);
            min = Math.min(min, restMin == Integer.MAX_VALUE ? Integer.MAX_VALUE : 1 + restMin);
        }
        cache.put(new String(target), min);
        return min;
    }


    public static void main(String[] args) {
        String[] stickers = new String[]{"ab", "abd", "bcd"};
        String target = "abbdcc";
        System.out.println(force(stickers, target));
        System.out.println(forcePreCharArrAndBranchPruning(stickers, target));
        System.out.println(dp(stickers, target));
        System.out.println(dpPreCharArrAndBranchPruning(stickers, target));
    }
}
