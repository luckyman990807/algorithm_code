package tixiban.class1_13.dynamicprogramming;

import java.util.HashMap;
import java.util.Map;

/**
 * 给定一个字符串数组，表示不同种类的贴纸，每种贴纸数量不限，每张贴纸可以任意剪成子序列
 * 给定一个字符串，表示目标，问最少使用几张贴纸能拼成目标字符串
 */
public class Code04StickersToSpellWord {
    /**
     * 暴力递归法
     */

    /**
     * 用stickers这些贴纸，拼出target所使用的最小张数
     */
    public static int processViolent(String[] stickers, String target) {
        // 最少用几张贴纸能拼出空字符串？0张
        if (target.length() == 0) {
            return 0;
        }

        // 最小张数，默认为最大整数
        int minCount = Integer.MAX_VALUE;

        // 几种贴纸轮流做第一张，比较哪种情况结果最小
        // sticker[i]做第一张去拼target要使用贴纸的最小张数 = 1 + 拼剩下的target要使用的最小张数
        // 这里minCount记录的是 拼剩下的target要使用的最小张数，返回的时候再+1
        for (String first : stickers) {
            String restTarget = cut(first, target);
            if (restTarget.length() < target.length()) {
                minCount = Math.min(minCount, processViolent(stickers, restTarget));
            }
        }
        // 如果minCount仍然是最大整数，说明stickers这些贴纸对拼接target都没有任何贡献，返回最大整数。否则返回1+minCount
        return minCount == Integer.MAX_VALUE ? Integer.MAX_VALUE : 1 + minCount;
    }

    /**
     * 用sticker这个贴纸去抵消target中的字符
     */
    public static String cut(String sticker, String target) {
        // 用一个26长度的数组记录target中每个字符出现的次数
        int[] targetMap = new int[26];
        for (char c : target.toCharArray()) {
            targetMap[c - 'a']++;
        }

        // 用sticker里的字符，去抵消target中的字符
        for (char c : sticker.toCharArray()) {
            targetMap[c - 'a']--;
        }

        // 抵消完，数组里剩下的>0的位置就是target中还剩下的字符的数量，拼接成剩余的target
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < targetMap.length; i++) {
            for (int j = 0; j < targetMap[i]; j++) {
                result.append((char) ('a' + i));
            }
        }

        return result.toString();
    }

    public static int minStickers(String[] stickers, String target) {
        if (stickers == null || target == null) {
            return -1;
        }
        int result = processViolent(stickers, target);
        // 如果返回最大整数，表示用这些贴纸无法拼成target
        return result == Integer.MAX_VALUE ? -1 : result;
    }

    /**
     * 优化：剪枝+傻缓存
     */

    /**
     * 给定一组贴纸，一个目标字符串，和历史计算结果缓存，要求计算用这些贴纸拼出目标最少需要多少张贴纸
     * 优化的地方是：
     * 1、只有包含目标字符串首字母的贴纸才会被尝试当第一个贴纸，不包含目标字符串首字符的贴纸直接跳过。至于为什么是首字符，没什么特殊含义，仅仅是根据自然智慧进行的剪枝，也可以是尾字母、中间字母。
     * 2、傻缓存。这道题不适合改成强依赖动态规划表，因为可变参数target是个字符串，列出它的所有可变范围成本太大，没必要
     */
    public static int processCache(int[][] stickers, String target, Map<String, Integer> cache) {
        if (target.length() == 0) {
            return 0;
        }

        if (cache.get(target) != null) {
            return cache.get(target);
        }

        // 目标字符串的字符数组、字母计数数组
        char[] targetChars = target.toCharArray();
        int[] targetLetters = new int[26];
        for (char c : targetChars) {
            targetLetters[c - 'a']++;
        }

        int min = Integer.MAX_VALUE;
        for (int[] sticker : stickers) {
            // targetChars[0]是目标字符串首字符，targetChars[0] - 'a'是这个字符的个数在计数数组中的下标，sticker[targetChars[0] - 'a'] > 0表示sticker包含这个字符
            if (sticker[targetChars[0] - 'a'] > 0) {
                // 用贴纸去抵消目标字符串的字符，生成剩余目标字符串
                StringBuilder restTarget = new StringBuilder();
                for (int i = 0; i < 26; i++) {
                    int letters = targetLetters[i] - sticker[i];
                    for (int j = 0; j < letters; j++) {
                        restTarget.append((char) ('a' + i));
                    }
                }
                min = Math.min(min, processCache(stickers, restTarget.toString(), cache));
            }
        }
        min = min == Integer.MAX_VALUE ? Integer.MAX_VALUE : 1 + min;

        cache.put(target, min);
        return min;
    }

    public static int minStickersCache(String[] stickers, String target) {
        int[][] stickerLetters = new int[stickers.length][26];
        for (int i = 0; i < stickers.length; i++) {
            for (char c : stickers[i].toCharArray()) {
                stickerLetters[i][c - 'a']++;
            }
        }

        Map<String, Integer> cache = new HashMap<>();

        return processCache(stickerLetters, target, cache);
    }

    /**
     * 主函数执行
     * @param args
     */
    public static void main(String[] args) {
        String[] stickers = {"abcd", "ba", "c"};
        String target = "babac";
        System.out.println(minStickers(stickers, target));
        System.out.println(minStickersCache(stickers, target));
    }
}
