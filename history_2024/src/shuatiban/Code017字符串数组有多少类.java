package shuatiban;

import java.util.HashSet;
import java.util.Set;

/**
 * 只由小写字母（a~z）组成的一批字符串，都放在字符类型的数组String[] arr中，如果其中某两个字符串所含有的字符种类完全一样
 * 就将两个字符串算作一类，比如baacbba和bac就算作一类，返回arr中有多少类
 *
 * 思路:
 * 只由小写字母组成说明只有26种字符,用一个int就能表示所有字符是否出现过,
 * 遍历字符串,如果是a把int的第0位置为1,如果是b就把int的第2位置为1...最后得到的int就是这个字符串的类别.
 * 把所有字符串的类别都加到hash表里,返回表的size就是有多少类
 */
public class Code017字符串数组有多少类 {

    public static int getStringGroup(String[] arr) {
        Set<Integer> set = new HashSet<>();
        for (String s : arr) {
            char[] str = s.toCharArray();
            int group = 0;
            for (char c : str) {
                group |= 1 << (c - 'a');
            }
            set.add(group);
        }
        return set.size();
    }

    public static void main(String[] args) {
        String[] arr = {"abc", "abcbcabab"};
        System.out.println(getStringGroup(arr));
    }
}
