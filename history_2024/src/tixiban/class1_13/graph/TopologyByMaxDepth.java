package tixiban.class1_13.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据每个节点能走出的最大深度，给所有节点排拓扑序，最大深度越大的，拓扑序越小，也就是拓扑序中越靠前。
 * 如果最大深度a>b，那么拓扑序a<=b。
 * 因为如果a是b的父，那么a的最大深度起码比b大1，拓扑序a也理所当然在b前面。
 * 如果a和b没有父子关系，那么a和b拓扑序相等，即两个事件没有依赖关系，谁先都行。
 */
public class TopologyByMaxDepth {
    /**
     * 邻接表法表示图
     */
    public static class Node {
        // 节点的值
        int label;
        // 节点的邻居
        List<Node> neighbours;

        public Node(int label) {
            this.label = label;
            neighbours = new ArrayList<>();
        }
    }

    /**
     * 辅助类，用于递归结果传递
     */
    public static class Record {
        // 节点
        Node node;
        // 从该节点出发能走出的最大深度
        int depth;

        public Record(Node node, int depth) {
            this.node = node;
            this.depth = depth;
        }
    }

    /**
     * 给定一个节点，求这个节点出发能走出的最大深度
     *
     * @param cur   当前节点
     * @param cache 缓存
     * @return {当前节点, 最大深度} 递归过程中用不到返回值里的节点，返回节点是为了在主方法里方便给节点排序
     */
    public static Record process(Node cur, Map<Node, Record> cache) {
        // 如果缓存里已经有这个节点，说明之前已经计算过最大深度，直接返回
        if (cache.containsKey(cur)) {
            return cache.get(cur);
        }

        // 记录当前节点邻居的最大深度
        int nextDepth = 0;
        for (Node neighbour : cur.neighbours) {
            // 取所有邻居能走出的最大深度的最大值
            nextDepth = Math.max(nextDepth, process(neighbour, cache).depth);
        }
        // 当前节点能走出的最大深度 = 所有邻居的最大深度的最大值 + 1
        Record record = new Record(cur, nextDepth + 1);
        // 塞进缓存里
        cache.put(cur, record);
        return record;
    }

    public static List<Node> getTopology(List<Node> graph) {
        // 缓存
        Map<Node, Record> cache = new HashMap<>();
        // 计算所有节点的最大深度，塞进缓存
        for (Node node : graph) {
            process(node, cache);
        }

        // 提取出缓存里的Record列表
        List<Record> records = new ArrayList<>();
        for (Record record : cache.values()) {
            records.add(record);
        }

        // 谁的最大深度大，谁就排在前面
        records.sort((o1, o2) -> o2.depth - o1.depth);

        // 提取出排序后的节点，就是拓扑排序
        List<Node> topology = new ArrayList<>();
        for (Record record : records) {
            topology.add(record.node);
        }
        return topology;
    }
}
