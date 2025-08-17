package shuatiban;

import java.util.List;
import java.util.TreeSet;

/**
 * 你有k个非递减排列的整数列表。找到一个最小区间，使得k个列表中的每个列表至少有一个数包含在其中
 * 我们定义如果 b-a < d-c 或者在 b-a == d-c 时 a < c，则区间 [a,b] 比 [c,d] 小。
 * Leetcode题目：https://leetcode.com/problems/smallest-range-covering-elements-from-k-lists/
 * <p>
 * 思路：有序表（TreeSet）
 * 先获得一个符合条件的初始区间（可能不是最小区间）：把每个列表的第一个元素放进有序表，获取他们之中的min和max，那么区间[min, max]就满足每个列表至少有一个数包含在其中。
 * 再一步步从左边界缩小区间范围：怎么缩小？有序表弹出min，放入min所在列表的下一个元素，看看区间有没有缩小。
 * 这就是缩小范围最经济的做法了，因为每次都是选的最左边的元素去往右推，只有推最左边元素才可能使区间缩小，推其他的都不可能使区间缩小。
 */
public class Code098找到一个最小区间使得每个列表至少有一个数在区间内 {
    public int[] smallestRange(List<List<Integer>> nums) {
        // 创建一个有序表，比较器：value小的节点排在前面，如果value相等，那么arrId小的节点排在前面（不可能出现value相等且arrId也相等的情况，因为同一个列表内的相等元素（假设为a，b）不可能同时存在于有序表中，因为如果a进了有序表，说明他是当前所有头节点中最小的，那么接下来一定是弹出a，压入b）
        TreeSet<Node> treeSet = new TreeSet<>((o1, o2) -> o1.value != o2.value ? o1.value - o2.value : o1.arrId - o2.arrId);
        // 初始化有序表，先把每个列表的第一个元素压入有序表中
        for (int i = 0; i < nums.size(); i++) {
            treeSet.add(new Node(nums.get(i).get(0), i, 0));
        }
        // 记录最小区间的边界
        int left = 0;
        int right = Integer.MAX_VALUE;
        // 用有序表中的min和max更新最小区间的边界，然后弹出min，压入新的min，直到其中一个列表空了
        while (treeSet.size() == nums.size()) {
            Node min = treeSet.first();
            Node max = treeSet.last();
            if (max.value - min.value < right - left) {
                left = min.value;
                right = max.value;
            }
            min = treeSet.pollFirst();
            List<Integer> list = nums.get(min.arrId);
            if (min.index + 1 < list.size()) {
                treeSet.add(new Node(list.get(min.index + 1), min.arrId, min.index + 1));
            }
        }
        return new int[]{left, right};
    }

    public class Node {
        public int value;
        public int arrId;
        public int index;

        public Node(int value, int arrId, int index) {
            this.value = value;
            this.arrId = arrId;
            this.index = index;
        }
    }
}
