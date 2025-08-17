package tixiban.class1_13.recurrence;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定一个字符串，求它的全部子序列
 * 例如，给定"abc"，返回""，"a"，"b"，"c"，"ab"，"ac"，"bc"，"abc"
 */
public class SubSequence {
    /**
     * index前面的位置不变了，分别求index位置保留产生的子序列、index位置丢弃产生的子序列
     *
     * @param str          原字符串（字符列表）
     * @param index        当前在决定哪个位置的去留
     * @param path         index前面已经决定了每一个位置的去留产生的路径
     * @param subSequences 存放最终结果集合
     */
    public static void process(char[] str, int index, String path, List<String> subSequences) {
        // 如果index越界了，说明整个字符串每个位置都已经决定去留了，把产生的结果加入结果集合，结束递归
        if (index == str.length) {
            subSequences.add(path);
            return;
        }
        // index+1了，但是path没变，表示求index位置丢弃产生的子序列
        process(str, index + 1, path, subSequences);
        // path加上index位置的字符
        path += str[index];
        // index+1了，path也加上index了，表示求index位置保留产生的子序列
        process(str, index + 1, path, subSequences);
    }

    public static void getAllSubSequence(String str) {
        char[] chars = str.toCharArray();
        List<String> subSequences = new ArrayList<>();
        process(chars, 0, "", subSequences);
        System.out.println(subSequences);
    }

    public static void main(String[] args) {
        getAllSubSequence("abc");
    }
}
