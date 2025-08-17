package tixiban.class1_13.graph;

import java.util.*;

/**
 * 克鲁斯卡尔算法计算最小生成树
 * 1、从权值最小的边开始考察，当前的边要么进入最小生成树的边集合，要么丢弃
 * 2、如果当前边进入最小生成树中会形成环，就丢弃，如果不会形成环，就加入最小生成树
 * 3、考察完所有边（或者考察过的边已经覆盖了所有点），最小生成树也得到了
 * <p>
 * 怎么判断会不会形成环：并查集。如果这条边的出点和入点在同一个集合中，那么这条边一定会带来环。
 */
public class MSTKruskal {
    /**
     * Kruskal算法，求最小生成树
     */
    public static Set<Edge> mst(Graph graph) {
        // 并查集用于检查环
        UnionFind unionFind = new UnionFind(graph.nodes.values());
        // 图的边的小根堆，权值越小的边越靠前
        PriorityQueue<Edge> heap = new PriorityQueue<>(Comparator.comparingInt(o -> o.weight));
        for (Edge edge : graph.edges) {
            heap.add(edge);
        }

        // 结果
        Set<Edge> mst = new HashSet<>();

        while (!heap.isEmpty()) {
            // 权值从小到大的顺序弹出边
            Edge edge = heap.poll();
            // 如果这条边的出点和入点属于同一个集合，那么加上这条边必然产生环
            if (!unionFind.isSameSet(edge.from, edge.to)) {
                // 如果出点和入点不属于同一个集合，就把这条边加入最小生成树
                mst.add(edge);
                // 同时两个集合要合并成一个
                unionFind.union(edge.from, edge.to);
            }
        }

        return mst;
    }

    /**
     * 并查集
     */
    public static class UnionFind {
        public Map<Node, Node> parentMap;
        public Map<Node, Integer> sizeMap;

        public UnionFind(Collection<Node> nodes) {
            parentMap = new HashMap<>();
            sizeMap = new HashMap<>();
            for (Node node : nodes) {
                parentMap.put(node, node);
                sizeMap.put(node, 1);
            }
        }

        public Node findHead(Node cur) {
            Stack<Node> path = new Stack<>();
            while (parentMap.get(cur) != cur) {
                path.add(cur);
                cur = parentMap.get(cur);
            }
            while (!path.isEmpty()) {
                parentMap.put(path.pop(), cur);
            }
            return cur;
        }

        public void union(Node a, Node b) {
            Node aHead = findHead(a);
            Node bHead = findHead(b);
            if (aHead == bHead) {
                return;
            }

            Node bigSet = sizeMap.get(aHead) >= sizeMap.get(bHead) ? aHead : bHead;
            Node smallSet = bigSet == aHead ? bHead : aHead;
            parentMap.put(smallSet, bigSet);
            sizeMap.put(bigSet, sizeMap.get(bigSet) + sizeMap.get(smallSet));
            sizeMap.remove(smallSet);
        }

        public boolean isSameSet(Node a, Node b) {
            return findHead(a) == findHead(b);
        }
    }
}
