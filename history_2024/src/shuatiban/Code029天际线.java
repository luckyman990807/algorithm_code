package shuatiban;

import java.util.*;

/**
 * 天际线问题
 * Leetcode题目：https://leetcode.com/problems/the-skyline-problem/
 *
 * 给定一个二维数组表示一组大楼,[[3,5,2],[1,4,5]]表示有两个建筑,第一个坐标从3到5,高度为2,第二个坐标从1到4,高度为5
 * 描绘出这一组大楼的轮廓,返回一个二维数组表示大楼轮廓的关键点,关键的是轮廓上每一条水平线的左端点.两座不重合的相邻大楼之间的地平线也算轮廓线的一部分.
 *
 * 思路:分析题目和leetcode上的图可知,轮廓关键点是每次最大高度发生变化的点.而最大高度发生变化只会在每座大楼的起点和终点.
 * 因此只要统计出出每个起终点坐标上的最大高度,就能的出轮廓关键点.
 * 那怎么统计出每个点的最大高度?
 * 把每个大楼转化成两个节点:一个让高度+H,一个让高度-H.所有节点按照坐标位置排序,遍历所有节点,记录所有高度出现的次数,
 * 如果+H就记录高度H出现的次数++,如果是-H就记录高度出现的次数--,-到0就删掉记录.
 * 遍历到每个节点,找出现存的所有高度记录中最大的一个,就是当前坐标的最大高度.
 */
public class Code029天际线 {
    public static class Node {
        // 节点在地平线坐标上的位置
        int position;
        // 是+height还是-height,true是+,false是-
        boolean add;
        // +或-的高度
        int height;

        public Node(int position, boolean add, int height) {
            this.position = position;
            this.add = add;
            this.height = height;
        }
    }

    public static List<List<Integer>> getSkyline(int[][] buildings) {
        // 把输入的建筑信息转换成节点列表
        List<Node> nodes = new ArrayList<>();
        for (int[] building : buildings) {
            nodes.add(new Node(building[0], true, building[2]));
            nodes.add(new Node(building[1], false, building[2]));
        }

        // 所有节点按照坐标位置排序
        nodes.sort(Comparator.comparingInt(n -> n.position));

        // 记录每个高度出现了几次
        TreeMap<Integer, Integer> heightTimes = new TreeMap<>();
        // 记录每个出现过的坐标位置上的最大高度
        TreeMap<Integer, Integer> maxHeight = new TreeMap<>();
        // 记录返回结果
        List<List<Integer>> result = new ArrayList<>();

        // 遍历所有节点(可能存在两个节点坐标一样)
        for (Node node : nodes) {
            if (node.add) {
                // 如果是+高度,就记录当前高度出现次数+1
                if (!heightTimes.containsKey(node.height)) {
                    heightTimes.put(node.height, 1);
                } else {
                    heightTimes.put(node.height, heightTimes.get(node.height) + 1);
                }
            } else {
                // 如果是-高度,就记录当前高度出现次数-1
                if (heightTimes.get(node.height) == 1) {
                    heightTimes.remove(node.height);
                } else {
                    heightTimes.put(node.height, heightTimes.get(node.height) - 1);
                }
            }
            // 目前依然存在的高度记录中的最后一个高度(treeMap有序表最后一个就是最大的)就是当前坐标的最大高度
            maxHeight.put(node.position, heightTimes.isEmpty() ? 0 : heightTimes.lastKey());
        }

        // 遍历所有坐标的最大高度,根据轮廓关键点的定义,每当最大高度变化时,记录(坐标,高度)作为一个关键点
        result.add(Arrays.asList(maxHeight.firstKey(), maxHeight.get(maxHeight.firstKey())));
        for (Integer pos : maxHeight.keySet()) {
            if (result.isEmpty() || !result.get(result.size() - 1).get(1).equals(maxHeight.get(pos))) {
                result.add(Arrays.asList(pos, maxHeight.get(pos)));
            }
        }

        return result;
    }
}
