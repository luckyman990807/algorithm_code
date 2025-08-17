package shuatiban;

import java.util.ArrayList;
import java.util.List;

/**
 * 给你一个由若干括号和字母组成的字符串 s ，删除最小数量的无效括号，使得输入的字符串有效(左右括号都能成对)。
 * 返回所有可能的结果。答案可以按 任意顺序 返回。
 *
 * 例如
 * 输入：s = "(a)())()"
 * 输出：["(a())()","(a)()()"]
 *
 * 思路:
 * 1、怎么判断有效字符串?
 * 首先判断有没有多的右括号: 定义一个变量count,正向每遍历一个字符,如果是(,count++,如果是),count--,如果count<0了,说明碰到多的)了,就改删)了
 * 然后判断有没有多的左括号: 反向遍历,遇到),count++,遇到(,count--
 * 2、正向遍历一遍只能判断有没有多出来的)
 * 因为只要碰见多出来的),count就会减到<0,就说明这个)的左边没有(跟他匹配.而碰见多出来的(,无法确定后面会不会有)跟他匹配.
 * 3、反向遍历一遍只能判断有没有多出来的(
 * 同上
 * 所以正向遍历一遍+反向遍历一遍就能找出所有多出来的(和)
 */
public class Code050删掉最少的括号得到有效括号串 {
    public static List<String> removeInvalidParentheses(String str) {
        List<String> answer = new ArrayList<>();
        process(str, 0, 0, answer, '(');
        return answer;
    }

    /**
     * 递归试法
     * @param str 上层递归已经删除的基础上的新字符串,本次要检查新的多余然后删除
     * @param checkIndex 从哪个位置开始检查(剪枝,前面的已经在上层递归检查过了,不需要重复检查)
     * @param deleteIndex 从哪个位置开始寻找删除的字符(剪枝,前面的该删除的已经在上层递归删除过了,不需要重新遍历)
     * @param answer 保存所有答案
     * @param start 一对符号的开始符号.如果开始符号是(,那么结束符号是),说明是正向遍历;如果开始符号是),那么结束符号是(,说明是反向遍历
     */
    public static void process(String str, int checkIndex, int deleteIndex, List<String> answer, char start) {
        // 和开始符号配对的结束符号,即(和)或者)和(
        char end = start == '(' ? ')' : '(';
        int count = 0;
        for (int i = checkIndex; i < str.length(); i++) {
            if (str.charAt(i) == start) {
                count++;
            } else if (str.charAt(i) == end) {
                count--;
            }
            // count小于0,说明第一次遇到没有start符号匹配的end符号,一定是非法的,一定要删除多余的end符号来达到平衡
            if (count < 0) {
                // 找出前面所有的可以删除的end符号,也就是所有分支,删除对应的end符号后顺着分支往下执行(深度优先遍历),下层递归会记录结果
                for (int j = deleteIndex; j <= i; j++) {
                    // 遍历当前检查位置前面所有的end符号(她上一个字符不是end符号才考虑,否则删除连续的end符号得到的答案是相同的.也是一种剪枝)
                    if (str.charAt(j) == end && (j == deleteIndex || str.charAt(j - 1) != end)) {
                        // 删掉找到的end符号
                        String newStr = str.substring(0, j) + str.substring(j + 1);
                        // 因为已经删除了,后面的字符补过来了,所以还是从i位置开始检查,从j位置开始删除
                        process(newStr, i, j, answer, start);
                    }
                }
                // 只要碰到一次不合法的,就递归后返回,不记录结果,在最深层的递归出口才记录结果(因为那时候才把所有非法字符全删掉了)
                return;
            }
        }
        // 反转字符串
        String reverse = new StringBuilder(str).reverse().toString();
        if (start == '(') {
            // 如果当前是正向遍历,要反向、把)作为开始符号、再遍历检查一遍
            process(reverse, 0, 0, answer, end);
        } else {
            // 如果当前已经是反向了,说明已经经历了正向、反向遍历,已经删掉所有不匹配的(和)了,可以记录答案了
            answer.add(reverse);
        }
    }
}
