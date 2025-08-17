package tixiban.class1_13.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通过计算以每个节点开始的子图的节点数，给所有节点排出拓扑序。数越大的节点，拓扑序越小，也就是排序越往前。
 * <p>
 * 如果a为起点的子图的节点数>b为起点的子图的节点数，那么拓扑序一定a<=b，也就是说a放在b前面肯定没错，
 * 因为如果b是a的子图，那么拓扑序a一定要在b的前面，如果a和b没有父子关系，说明a事件和b事件不互相依赖，谁在前都行。所以a数>b数那么a放b前面肯定没错。
 * <p>
 * 计算节点数的时候，可以算每个相邻节点为起点的子图分别有几个节点，加一起就是当前节点为起点的子图的节点数。
 * 有节点被重复计算也无所谓，因为按照这个标准，只要b是a的子图，那么求出来的数a一定比b大，起码大1
 */
public class TopologyByNodeCount {
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
        // 该节点为起点的子图的点次
        int nodes;

        public Record(Node node, int nodes) {
            this.node = node;
            this.nodes = nodes;
        }
    }

    /**
     * 给定一个节点，计算这个节点为起点的子图的节点数，或者说走遍所有路径经过的点次（反正就那意思，递归累加相邻节点个数）
     *
     * @param cur   当前节点
     * @param cache 缓存，记录图中已经计算过的节点和它对应的点次，每次递归都用这一个缓存
     * @return {节点，点次} 递归的时候没用的返回值里的节点，为什么还要返回节点？为了在主方法里方便给节点按照点次排序
     */
    public static Record process(Node cur, Map<Node, Record> cache) {
        // 如果缓存里面有这个节点，说明之前已经计算过了，直接获取返回
        if (cache.containsKey(cur)) {
            return cache.get(cur);
        }

        // 之前没有计算过，开始计算
        // nodes初始值只算cur自身，1个节点
        int nodes = 1;
        // 遍历相邻节点，把相邻节点计算得到的点次累加上
        for (Node neighbour : cur.neighbours) {
            nodes += process(neighbour, cache).nodes;
        }
        Record record = new Record(cur, nodes);
        // 把计算结果塞到缓存里
        cache.put(cur, record);
        return record;
    }

    public static List<Node> getTopology(List<Node> graph) {
        // 初始化缓存
        Map<Node, Record> cache = new HashMap<>();
        // 遍历图中的节点，对每一个节点求点次，并塞进缓存
        for (Node node : graph) {
            process(node, cache);
        }

        // 提取出Record列表
        List<Record> records = new ArrayList<>();
        for (Record record : cache.values()) {
            records.add(record);
        }

        // 按照点次排序，点次大的排在前面
        records.sort((o1, o2) -> o2.nodes - o1.nodes);

        // 提取出排好序的Node列表返回
        List<Node> topology = new ArrayList<>();
        for (Record record : records) {
            topology.add(record.node);
        }
        return topology;
    }
}
