package tixiban.class1_13.recurrence;

import java.util.ArrayList;
import java.util.List;

/**
 * 全排列
 * 给定一个字符串（字符列表），输出这个字符列表的全排列
 * 例如，给定abc，输出abc,acd,bac,bca,cab,cba，给定acc，输出acc,cac,cca
 */
public class Permutation {
    public static void main(String[] args) {
        System.out.println(getPermutations("abc"));
    }

    public static List<String> getPermutations(String str){
        List<String> permutations = new ArrayList<>();
        if(str==null || str.length()==0){
            return permutations;
        }
        process(str.toCharArray(), 0, permutations);
        return permutations;
    }

    /**
     * index之前的位置已经确定了，后面的元素轮流做index并拼接index后面的全排列
     * 例如，str=abc，index=0，表示输出0位置为a时的全排列、0位置为b时的全排列、0位置为c时的全排列
     * @param str 当前的排列，index之前的位置在本次调用中不会变动
     * @param index 当前要轮流做哪个位置
     * @param permutations 全排列结果
     */
    public static void process(char[] str, int index, List<String> permutations) {
        // 如果当前要轮流的位置越界了，说明全排列结束了，加入结果集合中
        if (index == str.length) {
            permutations.add(String.valueOf(str));
            return;
        }
        // 定义一个表记录index位置有没有被某个char占据过，ASCII码有256个
        boolean[] visited = new boolean[256];
        // index来到index位置，后面全排列；index+1来到index位置，后面全排列……
        for (int i = index; i < str.length; i++) {
            // 如果index位置已经被当前字符占据过了，直接跳过
            if (!visited[str[i]]) {
                visited[str[i]] = true;
                // i来到index位置
                swap(str, i, index);
                // index后面的位置全排列
                process(str, index + 1, permutations);
                // 恢复现场，要不然后面递归的时候会乱套
                // 例如abc，index=0，经历了abc、acb、bac、bca，如果不恢复现场，下一次是2位置来到0，得到acb，是之前出现过的，而c开头的则永远不会出现
                swap(str, i, index);
            }
        }
    }

    public static void swap(char[] arr, int i, int j) {
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
