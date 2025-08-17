package shuatiban;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * https://leetcode.com/problems/longest-consecutive-sequence/
 * 给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
 * 请你设计并实现时间复杂度为 O(n) 的算法解决此问题。
 * 例如：
 * 输入：nums = [100,4,200,1,3,2]
 * 输出：4
 * 解释：最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4。
 *
 * 思路：
 * 跟打印消息流一样的解法，来一个数，记录到头表，记录到尾表，发现能连的就连起来
 * 最优解：
 * 一个表记录某个头/尾数字所在连续串的最大长度即可
 */
public class Code063最长连续序列 {
    /**
     * 最优解，一个map实现
     */
    public static int longestConsecutive(int[] nums) {
        // 头/尾表，key：一个链的头节点或尾节点，value：以key为头的链的长度或者以key为尾的链的长度
        Map<Integer, Integer> map = new HashMap<>();
        // 记录最大值结果
        int maxLength = 0;
        for (int num : nums) {
            if (!map.containsKey(num)) {
                // 先无脑塞进map，记录长度为1，起码占个位，证明已经处理过这个num了，后续会自动跳过重复的num
                map.put(num, 1);

                // 计算能接在num前面的链的长度、能接在num后面的链的长度、num加入后形成的新链的总长度
                int preLength = map.containsKey(num - 1) ? map.get(num - 1) : 0;
                int nextLength = map.containsKey(num + 1) ? map.get(num + 1) : 0;
                int allLength = preLength + 1 + nextLength;

                // 新链的头节点，修改长度
                map.put(num - preLength, allLength);
                // 新链的尾节点，修改长度
                map.put(num + nextLength, allLength);
                // 为什么不删除无用的节点？例如长度为1的num、长度为preLength的num-1、长度为nextLength的num+1？
                // 1、不会影响结果。形成新链之后，这些都是中间的节点，中间的节点长度再长，也不可能长的过头尾节点
                // 2、充当判重机制。把这些无用节点留在map里，能自动跳过重复节点

                // 更新最大值
                maxLength = Math.max(maxLength, allLength);
            }
        }
        return maxLength;
    }

    /**
     * 两个map实现
     * @param nums
     * @return
     */
    public static int longestConsecutiveTwoMaps(int[] nums) {
        // 头表，key：头节点的num值，value：以num为头的链的长度
        Map<Integer, Integer> head = new HashMap<>();
        // 尾表，key：尾节点的num值，value：以num为尾的链的长度
        Map<Integer, Integer> tail = new HashMap<>();
        // 用set限制每个数字只处理一次
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            if (set.contains(num)) {
                continue;
            }
            set.add(num);
            // 添加头节点，长度为1，添加尾节点，长度为1
            if (!head.containsKey(num)) {
                head.put(num, 1);
            }
            if (!tail.containsKey(num)) {
                tail.put(num, 1);
            }
            // 如果有以num-1结尾的，就把num加在后面作为新的结尾
            if (tail.containsKey(num - 1)) {
                int preLength = tail.get(num - 1);
                head.put(num - preLength, preLength + 1);
                tail.put(num, preLength + 1);
                head.remove(num);
                tail.remove(num - 1);
            }
            // 如果有以num+1开头的，就把num加在前面作为新的开头
            if (head.containsKey(num + 1)) {
                int nextLength = head.get(num + 1);
                tail.put(num + nextLength, nextLength + tail.get(num));
                head.put(num - tail.get(num) + 1, nextLength + tail.get(num));
                tail.remove(num);
                head.remove(num + 1);
            }
        }
        // 头表或尾表找出最大长度
        int max = 0;
        for (Integer length : head.values()) {
            max = Math.max(max, length);
        }
        return max;
    }

    public static void main(String[] args) {
        int[] nums = {100, 4, 200, 1, 3, 2};
        System.out.println(longestConsecutive(nums));
    }
}
