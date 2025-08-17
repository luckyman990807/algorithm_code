package shuatiban;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 一张扑克有3个属性，每种属性有3种值（A、B、C）
 * 比如"AAA"，第一个属性值A，第二个属性值A，第三个属性值A
 * 比如"BCA"，第一个属性值B，第二个属性值C，第三个属性值A
 * 给定一个字符串类型的数组cards[]，每一个字符串代表一张扑克
 * 从中挑选三张扑克，一个属性达标的条件是：这个属性在三张扑克中全一样，或全不一样
 * 挑选的三张扑克达标的要求是：每种属性都满足上面的条件
 * 比如："ABC"、"CBC"、"BBC"
 * 第一张第一个属性为"A"、第二张第一个属性为"C"、第三张第一个属性为"B"，全不一样
 * 第一张第二个属性为"B"、第二张第二个属性为"B"、第三张第二个属性为"B"，全一样
 * 第一张第三个属性为"C"、第二张第三个属性为"C"、第三张第三个属性为"C"，全一样
 * 每种属性都满足在三张扑克中全一样，或全不一样，所以这三张扑克达标
 * 返回在cards[]中任意挑选三张扑克，达标的方法数
 * 备注：牌的总数在100万张左右
 * <p>
 * 思路：根据数据量猜解法，从牌入手的话，100万张太多，而牌面只有27种（3个属性，每个属性有3个可选值，3的3次方），因此选择从牌面入手
 * 那么什么样的牌面是达标的呢？一种情况是3张牌的牌面完全一样，肯定达标；另一种情况是3张牌牌面完全不一样，并且要额外判断每一位都不一样。不可能出现有两张牌一样还能达标的情况。
 * 那么达标的方法数怎么算呢？3张牌完全一样的情况，就统计出每种牌面的数量Ni，每种牌面计算C(Ni,3)累加到总数中。3张牌每一位都不一样的情况，递归出所有达标的牌面组合，计算每种组合的数量Ni*Nj*Nk累加到总数中。
 * <p>
 * 递归：27种牌面，每一种，要，或者不要。
 * 深度优先遍历：先把一种牌面加入要的集合，然后往下递归，递归回来后删掉刚加入集合的牌面（清除现场）
 * 剪枝：如果已经要了3种牌面，就直接提前结束
 */
public class Code099三张扑克牌达标数 {
    public static int count(String[] cards) {
        // 计算每种牌面的数量
        Map<String, Integer> map = new HashMap<>();
        for (String card : cards) {
            if (map.containsKey(card)) {
                map.put(card, map.get(card) + 1);
            } else {
                map.put(card, 1);
            }
        }

        int count = 0;

        // 3张牌完全一样的情况，有几种组合
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            Integer n = entry.getValue();
            if (n >= 3) {
                int add = n * (n - 1) * (n - 2) / 6;
                System.out.println(entry.getKey() + " " + n + "张，增加" + add + "种组合");
                count += add;
            }
        }

        // 三张牌完全不一样且符合条件的情况，有几种组合，靠深度优先遍历+递归
        // 深度优先遍历用盏保存路径
        Stack<String> path = new Stack<>();
        count += process(map, 0, path);

        return count;
    }

    /**
     * 所有牌面及其数量放在map中，从下标index开始遍历，每一位可以选择取与不取，递归直到取满3张。取的路径放在path里。
     */
    private static int process(Map<String, Integer> map, int index, Stack<String> path) {
        // 递归出口，已经取满了3张，结算
        if (path.size() == 3) {
            for (int i = 0; i < 3; i++) {
                // 检查这3张的每一位是否符合条件？条件是：3张的这一位要么都相等，要么都不等。符合条件就返回3种牌面的数量相乘，不符合就返回0
                /** 注意这里的都相等和递归前计算的三位完全相等的情况不冲突，之前那是3张牌每一位都完全相等，现在是针对某一位完全相等 */
                if (path.get(0).charAt(i) == path.get(1).charAt(i) && path.get(0).charAt(i) == path.get(2).charAt(i)
                        || path.get(0).charAt(i) != path.get(1).charAt(i) && path.get(1).charAt(i) != path.get(2).charAt(i) && path.get(2).charAt(i) != path.get(0).charAt(i)) {
                    continue;
                }
                return 0;
            }
            return map.get(path.get(0)) * map.get(path.get(1)) * map.get(path.get(2));
        }

        int count = 0;

        // index之前的牌面，取与不取已经确定了，现在从index开始，每一位决定取与不取
        ArrayList<String> valueList = new ArrayList<>(map.keySet());
        for (int i = index; i < valueList.size(); i++) {
            path.push(valueList.get(i));
            /** 这里是i+1，不是index+1 */
            count += process(map, i + 1, path);
            path.pop();
        }
        return count;
    }

    /**
     * 暴力枚举法
     * @param cards
     * @return
     */
    public static int violent(String[] cards) {
        int count = 0;
        for (int i = 0; i < cards.length; i++) {
            for (int j = i + 1; j < cards.length; j++) {
                for (int k = j + 1; k < cards.length; k++) {
                    boolean flag = true;
                    for (int l = 0; l < 3; l++) {
                        if (!(cards[i].charAt(l) == cards[j].charAt(l) && cards[k].charAt(l) == cards[j].charAt(l) || cards[i].charAt(l) != cards[j].charAt(l) && cards[j].charAt(l) != cards[k].charAt(l) && cards[k].charAt(l) != cards[i].charAt(l))) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        String[] cards = {"aaa", "bbb", "abc", "cab", "ccc", "bca", "aaa", "bbb", "ccc", "aaa", "bbb", "ccc", "abb", "aba", "abc"};
        System.out.println(violent(cards));
        System.out.println(count(cards));

    }
}
