package shuatiban;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 给定一个字符串数组arr，里面都是互不相同的单词，找出所有不同的索引对(i, j)，使得列表中的两个单词，words[i] + words[j]，可拼接成回文串。
 * Leetcode题目：https://leetcode.com/problems/palindrome-pairs/
 *
 * 是假复杂度要求O(N*K)，K是单词平均长度
 * 首先O(N)把字符串数组整理成map
 * 遍历每个字符串O(N)，找可能的配对字符串O(K)
 * 如何找可能配对的字符串？
 * 遍历该字符串的每个位置，
 * 前i个字符的前缀是不是回文？是的话，剩下的后缀的倒序，是否存在于map中？如果存在，把它拼在该字符串的左边，就是一个回文串，也就找到了一个回文对
 * 后i个字符的后缀是不是回文？是的话，剩下的前缀的倒序，是否存在于map中？如果存在，把它拼在该字符串的右边，就是一个回文串，也就找到了一个回文对
 * 按照复杂度要求，判断回文以及从map中找字符串的复杂度要做到O(1)？好像不太可能，暂时达不到，是O(K)
 */
public class Code90找出字符串数组中的所有回文对 {
    /**
     * 主函数
     */
    public static List<List<Integer>> palindromePairs(String[] words) {
        // 字符串：下标。用于快速判断原字符串数组中是否含有某个字符串，且下标是多少
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            map.put(words[i], i);
        }

        List<List<Integer>> result = new ArrayList<>();

        // 针对每一个字符串，找出和他配对的组合，记录所有组合结果
        for (int i = 0; i < words.length; i++) {
            result.addAll(getPalindromeManacher(words[i], i, map));
        }

        return result;
    }

    /**
     * 针对一个字符串word，从剩余其他字符串中找出所有能和word配对成回文的组合，记录在result里
     * @param word
     * @param index word在原字符串数组中的下标
     * @param map 字符串：下标
     * @return
     */
    private static List<List<Integer>> getPalindromeManacher(String word, int index, Map<String, Integer> map) {
        List<List<Integer>> result = new ArrayList<>();
        // manacher算法，先把原字符串变成manacher字符串（abc->#a#b#c#），再求出manacher字符串每个字符的回文半径
        int[] radius = manacher(word);

        // 先求出word的逆序串，后面每次直接用逆序串subString，比每次subString后再逆序快2倍
        String reverse = reverse(word);

        if (radius[word.length()] == word.length() + 1) {
            // 1、word自身是回文，那么word和""可以产生2个回文组合
            Integer rest = map.get("");
            if (rest != null && rest != index) {
                record(result, index, rest);
                record(result, rest, index);
            }
        } else {
            // 2、word自身不是回文，word的逆序串在map中，那么word和逆序串可以产生两个回文组合，这里只记录一个，避免重复，因为遍历到word的逆序串的时候还要记录
            Integer rest = map.get(reverse);
            if (rest != null) {
                record(result, index, rest);
            }
        }

        for (int i = 1; i < word.length(); i++) {
            // 3、word的前缀是回文，那么把后缀的逆序串拼在前面，就是一个回文组合
            if (radius[i] == i + 1) {
                Integer rest = map.get(reverse.substring(0, word.length() - i));
                if (rest != null) {
                    record(result, rest, index);
                }
            }
            // 4、word的后缀是回文，那么把前缀的逆序对拼在后面，就是一个回文组合
            if (radius[word.length() * 2 - i] == i + 1) {
                Integer rest = map.get(reverse.substring(i));
                if (rest != null) {
                    record(result, index, rest);
                }
            }
        }
        return result;
    }


    /**
     * manacher算法，先把字符串转成manacher字符串（abc->#a#b#c#），再求manacher字符串每个字符的回文半径
     * 更简洁的写法见「Code01Manacher」这个类
     * @param s
     * @return
     */
    private static int[] manacher(String s) {
        // 加#后的manacher字符串
        char[] str = getManacherStr(s.toCharArray());
        // 记录每个字符的回文半径
        int[] radius = new int[str.length];
        // 记录当前所有已知的回文子串能触及到的最右的位置
        int R = 0;
        // 记录R位置所属回文子串的中点位置
        int C = 0;

        for (int i = 0; i < str.length; i++) {
            if (i > R) {
                // 如果当前字符处在R的右边，那么这个位置是之前所有回文字串都无法触及到的，也无法根据之前的回文字串帮助判断当前位置的回文半径，所以暴力往两边扩展
                radius[i] = 1;
                while (i + radius[i] < str.length && i - radius[i] >= 0) {
                    if (str[i + radius[i]] == str[i - radius[i]]) {
                        radius[i]++;
                    } else {
                        break;
                    }
                }
                // 扩展后i位置的回文串可能变成最右回文子串
                if (i + radius[i] > R) {
                    R = i + radius[i] - 1;
                    C = i;
                }
            } else {
                // 当前字符处在R位置或者R的左边，那么可以根据之前的回文串帮助计算当前位置的回文半径
                // 当前i位置关于最右回文串的中点，必有一个对称点iLeft
                int iLeft = 2 * C - i;
                // 最右回文串的左边界位置
                int L = 2 * C - R;

                if (iLeft - radius[iLeft] + 1 < L) {
                    // 如果i的对称点的回文半径已经超出L了，说明i的回文半径就是对称点到L这段距离。为什么不会继续往左扩展？能扩展的话最右回文串早扩展了
                    radius[i] = iLeft - L + 1;
                } else {
                    // 如果i的对称点的回文半径没有到达L，那么对称过来i的回文半径也就是跟iLeft一样了，能扩展的话iLeft早扩展了
                    if (iLeft - radius[iLeft] + 1 > L) {
                        radius[i] = radius[iLeft];
                    }
                    // 如果i的对称点的回文半径恰好到L，i的回文半径能否在iLeft的基础上继续扩展，是不能确定的，只能尝试去暴力扩展。列个不等式关系即可得到这个结论
                    while (i + radius[i] < str.length && i - radius[i] >= 0) {
                        if (str[i + radius[i]] == str[i - radius[i]]) {
                            radius[i]++;
                        } else {
                            break;
                        }
                    }
                    // 扩展后i位置的回文串可能变成最右回文子串
                    if (i + radius[i] > R) {
                        R = i + radius[i] - 1;
                        C = i;
                    }
                }
            }
        }
        return radius;
    }


    private static char[] getManacherStr(char[] str) {
        char[] manancherStr = new char[str.length * 2 + 1];
        manancherStr[0] = '#';
        for (int i = 0; i < str.length; i++) {
            manancherStr[2 * i + 1] = str[i];
            manancherStr[2 * i + 2] = '#';
        }
        return manancherStr;
    }

    private static void record(List<List<Integer>> result, int left, int right) {
        ArrayList<Integer> pair = new ArrayList<>();
        pair.add(left);
        pair.add(right);
        result.add(pair);
    }

    private static String reverse(String s) {
        char[] str = s.toCharArray();
        for (int i = 0; i < str.length >> 1; i++) {
            char temp = str[i];
            str[i] = str[str.length - 1 - i];
            str[str.length - 1 - i] = temp;
        }
        return new String(str);
    }


    public static void main(String[] args) {
        String[] words = {"a", "aa", "aaa"};
        System.out.println(palindromePairsViolent(words));
        System.out.println(palindromePairs(words));

    }

    public static List<List<Integer>> palindromePairsViolent(String[] words) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            map.put(words[i], i);
        }

        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            result.addAll(getPalindrome(words[i], i, map));
        }

        return result;
    }

    /**
     * 判断回文使用暴力法，也就是如果word.equals(word的翻转)，那么word就是回文
     * 答案对，但是会超时
     */
    private static List<List<Integer>> getPalindrome(String word, int index, Map<String, Integer> map) {
        List<List<Integer>> result = new ArrayList<>();
        String reverse = reverse(word);
        Integer rest;

        if (word.equals(reverse)) {
            // 1、先处理word自身是回文的情况
            rest = map.get("");
            // rest != index 是为了防止把“”自己和自己的配对加进去，而且是加两遍
            if (rest != null && rest != index) {
                record(result, index, rest);
                record(result, rest, index);
            }
        } else {
            // 2、再找word自身的翻转
            // 如果自身是回文了，那么自身的翻转就是自身，所以这两种情况不会同时存在
            rest = map.get(reverse);
            if (rest != null) {
                record(result, index, rest);
            }
        }

        for (int i = 1; i < word.length(); i++) {
            // 3、再处理word前缀是回文，找后缀的翻转
            String pre = word.substring(0, i);
            if (isPalindrome(pre)) {
                rest = map.get(reverse.substring(0, reverse.length() - i));
                if (rest != null) {
                    record(result, rest, index);
                }
            }
            // 4、再处理word后缀缀是回文，找前缀的翻转
            String post = word.substring(word.length() - i);
            if (isPalindrome(post)) {
                rest = map.get(reverse.substring(i));
                if (rest != null) {
                    record(result, index, rest);
                }
            }
        }

        return result;
    }

    /**
     * 判断回文，暴力判断，即s和s的逆序对相等则s是回文
     * @param s
     * @return
     */
    private static boolean isPalindrome(String s) {
        String reverse = reverse(s);
        return s.equals(reverse);
    }
}
