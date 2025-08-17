package tixiban.class1_13.graph;

import java.util.*;

/**
 * 拓扑排序
 * 拓扑排序是有向无环图所有节点的一种线性序列，满足以下条件：
 * 1、每个节点只出现一次
 * 2、如果图中存在由a到b的路径，那么序列中a出现在b前面
 * <p>
 * 注意：只有有向无环图才有拓扑排序
 *
 * 用途：事件安排、编译顺序等
 */
public class TopologySort {
    /**
     * 图的拓扑排序算法
     * 1、在图中找到所有入度为0的节点输出
     * 2、把所有入度为0的节点从图中删掉，继续找入度为0的节点输出，周而复始
     * 3、图的所有节点都被删除后，依次输出的顺序就是拓扑排序
     */
    public static List<Node> getTopologySort(Graph graph) {
        // key是节点，value是这个节点的入度
        Map<Node, Integer> inMap = new HashMap<>();
        // 入度为0的节点才会进这个队列
        Queue<Node> zeroInQueue = new LinkedList<>();

        // 先遍历一遍图中所有节点，用inMap记录每个节点的入度。如果碰到入度为0的节点，压入队列
        for (Node node : graph.nodes.values()) {
            inMap.put(node, node.in);
            if (node.in == 0) {
                zeroInQueue.add(node);
            }
        }

        List<Node> topology = new ArrayList<>();

        while (!zeroInQueue.isEmpty()) {
            // 从队列中弹出一个入度为0的节点，它就相当于是剩下的图的起始节点，加入拓扑序列
            Node cur = zeroInQueue.poll();
            topology.add(cur);

            for (Node next : cur.nexts) {
                // 减去这个节点在它相邻节点上产生的入度，但不是真的改图的信息，只是改记录节点入度的inMap
                inMap.put(next, inMap.get(next) - 1);
                // 如果某个相邻节点减去这个入度后，入度为0了，也加入队列
                if (inMap.get(next) == 0) {
                    zeroInQueue.add(next);
                }
            }
        }

        return topology;
    }
}
