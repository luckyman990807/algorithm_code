package shuatiban;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数组为{3, 2, 2, 3, 1}，查询为(0, 3, 2)，意思是在数组里下标0~3这个范围上，有几个2？答案返回2
 * 假设给你一个数组arr，对这个数组的查询非常频繁，且都给了查询组，请返回所有查询的结果
 *
 * 思路:
 * 查询非常频繁,言外之意就是要预处理一个辅助结构,能快速查结果
 * 预处理结构:用一个map记录每个数出现的所有下标,例如{2:[1,2]}表示2在下标1和下标2出现了
 * 查询(0,3,2)时,可以二分查找,即在2的下标数组[1,2]中二分找到大于等于0最左的位置,二分找到小于等于3最右的位置,返回两个位置的间隔
 *
 * 另一个思路:
 * 有几种数值就创建几个sum数组,sum[0]表示原数组0到0有几个这个数,sum[i]表示原数组0到i有几个这个数...
 * 适用于数据范围比较小的情况
 */
public class Code022从下标1到下标2上有几个M {
    public static class Counter {
        public Map<Integer, List<Integer>> map;

        public Counter(int[] arr) {
            map = new HashMap<>();
            for (int i = 0; i < arr.length; i++) {
                if (!map.containsKey(arr[i])) {
                    map.put(arr[i], new ArrayList<>());
                }
                map.get(arr[i]).add(i);
            }
        }

        public int count(int from, int to, int value) {
            if (!map.containsKey(value)) {
                return 0;
            }
            List<Integer> indexList = map.get(value);
            return lessLast(indexList, to + 1) - lessLast(indexList, from);
        }

        /**
         * 求有序数组list中最后一个小于value的下一个位置
         */
        public int lessLast(List<Integer> list, int value) {
            int left = 0;
            int right = list.size() - 1;
            int index = -1;
            while (left <= right) {
                int mid = left + ((right - left) >> 1);
                if (list.get(mid) < value) {
                    index = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            return index;
        }

        public static void main(String[] args) {
            int[] arr = {3, 2, 2, 3, 1};
            Counter counter = new Counter(arr);
            System.out.println(counter.count(0, 3, 2));
        }
    }
}
