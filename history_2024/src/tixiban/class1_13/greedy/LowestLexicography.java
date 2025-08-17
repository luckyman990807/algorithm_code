package tixiban.class1_13.greedy;

import java.util.Arrays;
import java.util.TreeSet;

/**
 * 给定一个字符串数组，要求把这些字符串拼接成一个大字符串，可以改变拼接的顺序，使得大字符串的字典序最小
 * 字典序定义：可以把字符理解成k进制的数字，比较规则是，长度一样的情况下，从左到右比较每一位大小，长度不一样的情况下，补0补到一样长再比较（ASCII码的0）
 */
public class LowestLexicography {

    /**
     * 贪心策略：按照【如果ab字典序小于ba，那么a在前b在后】的顺序拼接，得到的大字符串字典序最小
     * 怎么证明这个贪心策略就是最优解？两种方法：
     * 1、理论推理
     * 2、实践证明
     * 做题的时候不可能理论推理，所以只能实践证明，也就是用暴力方法写一个对数器，验证贪心策略有没有贪对
     */
    public static String getLowestLexicography(String[] strings) {
        if (strings == null || strings.length == 0) {
            return "";
        }
        // 对数组排序，如果（a拼接b得到的）ab比ba的字典序小，那么a排在b前面
        Arrays.sort(strings, (o1, o2) -> (o1 + o2).compareTo(o2 + o1));

        // 按照上面的排序方式，拼接的结果就是字典序最小的
        String result = "";
        for (String str : strings) {
            result += str;
        }

        return result;
    }





    /**
     * 暴力方法，全排列找出拼接后字典序最小的结果
     */
    public static String getLowestLexicography2(String[] strings) {
        if (strings == null || strings.length == 0) {
            return "";
        }
        TreeSet<String> result = process(strings);
        return result.isEmpty() ? "" : result.first();
    }

    /**
     * 输入一个字符串数组，返回数组全排列拼接成的大字符串
     */
    public static TreeSet<String> process(String[] strings) {
        // 用TreeSet会自动根据字典序排序
        TreeSet<String> result = new TreeSet<>();
        if (strings.length == 0) {
            return result;
        }

        for (int i = 0; i < strings.length; i++) {
            // 数组元素轮流做第一个
            String first = strings[i];
            // 剩下的元素
            String[] others = removeByIndex(strings, i);
            // 递归，把剩下的元素全排列
            TreeSet<String> otherResult = process(others);
            // 当前数组的全排列=头+剩下的全排列
            if (otherResult.isEmpty()) {
                result.add(first);
            } else {
                for (String next : otherResult) {
                    result.add(first + next);
                }
            }
        }

        return result;
    }

    /**
     * 移除数组中index下标的元素，返回剩下的元素
     */
    public static String[] removeByIndex(String[] strings, int index) {
        String[] result = new String[strings.length - 1];
        int resultIndex = 0;
        for (int i = 0; i < strings.length; i++) {
            if (i != index) {
                result[resultIndex++] = strings[i];
            }
        }
        return result;
    }


    /**
     * 主函数验证
     */
    public static void main(String[] args) {
        for (int n = 0; n < 10000; n++) {
            String[] strings = generateStrings(5, 5);
            System.out.println("原字符串数组：" + Arrays.toString(strings));

            String[] stringsCopy = new String[strings.length];
            System.arraycopy(strings, 0, stringsCopy, 0, stringsCopy.length);
            String[] stringsCopy2 = new String[strings.length];
            System.arraycopy(strings, 0, stringsCopy2, 0, stringsCopy2.length);

            String lowestLexicography2 = getLowestLexicography2(stringsCopy);
            System.out.println("暴力法求最小字典序拼接：" + lowestLexicography2);
            String lowestLexicography = getLowestLexicography(stringsCopy2);
            System.out.println("贪心法求最小字典序拼接：" + lowestLexicography);
            if (!lowestLexicography.equals(lowestLexicography2)) {
                System.out.println("failed!");
                break;
            }
        }
        System.out.println("success!");
    }

    /**
     * 生成随机字符串数组
     */
    public static String[] generateStrings(int MaxStringLength, int MaxListLength) {
        String[] result = new String[(int) (Math.random() * MaxListLength) + 1];
        for (int i = 0; i < result.length; i++) {
            char[] chars = new char[(int) (Math.random() * MaxStringLength) + 1];
            for (int j = 0; j < chars.length; j++) {
                chars[j] = (char) ('a' + Math.random() * ('z' - 'a' + 1));
            }
            result[i] = String.valueOf(chars);
        }
        return result;
    }
}
