package tixiban.class1_13.graph;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Prim算法求最小生成树
 * 1、可以从任意节点出发来寻找最小生成树
 * 2、某个节点被选中后，解锁以这个节点为入点的所有边
 * 3、从所有解锁的边中选权值最小的边，判断是否会形成环
 * 4、如果会形成环，丢弃这个边，再从剩下的解锁的边中重复3
 * 5、如果不会形成环，就把这个边的出点加入到被选中的节点中，重复2
 * 6、当所有节点都被选中，最小生成树就得到了
 */
public class MSTPrim {
    public static Set<Edge> prim(Graph graph) {
        // 解锁的边进入小根堆
        PriorityQueue<Edge> edgeHeap = new PriorityQueue<>();
        // 选中的节点进入set
        Set<Node> nodeSet = new HashSet<>();
        // 选中的边进入最小生成树
        Set<Edge> mst = new HashSet<>();

        // for循环用来防森林，如果图是森林，必须遍历每一个节点才能保证得到最小生成森林。如果没有森林，那么执行完一次后，后面的循环所有节点都contain了，会直接跳过
        for (Node node : graph.nodes.values()) {
            if (!nodeSet.contains(node)) {
                // 如果这个点没有被选中过，就加入选中集合
                nodeSet.add(node);
                // 并且把这个点为出点的所有边解锁
                for (Edge edge : node.edges) {
                    edgeHeap.add(edge);
                }

                while (!edgeHeap.isEmpty()) {
                    Edge curEdge = edgeHeap.poll();
                    Node toNode = curEdge.to;
                    // 如果这条边对应的入点已经被选中过了，又因为这条边是由被选中的出点解锁来的，又因为两个被选中的点都在最小生成树中已经是连通的了，那么如果这条边再被选中，再把两个点连通一下，就出现环了
                    // 所以如果这条边的入点被选中过，就直接跳过
                    if (!nodeSet.contains(toNode)) {
                        // 如果入点没被选中过，就加入选中集合
                        nodeSet.add(toNode);
                        // 这条边加入最小生成树
                        mst.add(curEdge);
                        // 入点又能解锁以它为出点的一批边
                        for (Edge nextEdge : toNode.edges) {
                            edgeHeap.add(nextEdge);
                        }
                    }
                }
            }
        }
        return mst;
    }
}
