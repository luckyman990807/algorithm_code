package tixiban.class1_13.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 迪杰斯特拉算法求单源最短路径
 */
public class ShortestPathDijkstra {
    /**
     * 迪杰斯特拉简易版
     * 简易在从没被访问过的节点中选出路径最小的一个，也就是getUnvisitedClosestNode方法，每次都要遍历全部节点，每次都要log(N)
     */
    public static Map<Node, Integer> dijkstra(Node from) {
        // 由from节点到图中所有节点的距离表，没在表里表示距离为无穷
        Map<Node, Integer> distance = new HashMap<>();
        // from到自己的距离是0
        distance.put(from, 0);

        // 记录哪些节点被访问过
        Set<Node> visited = new HashSet<>();

        // 从没被访问过的节点中选出距离最小的一个
        Node midNode = getUnvisitedClosestNode(distance, visited);

        // 循环访问中继节点，以中继节点为跳板刷新其他节点的距离
        while (midNode != null) {
            // 当前访问的中继节点作为出点，引出哪些边以及入点
            for (Edge edge : midNode.edges) {
                if (!distance.containsKey(edge.to)) {
                    // 如果当前入点没在距离表中，说明此前距离是无穷，现在记录距离=源节点到中继的距离+中继到当前点的距离（也就是当前边的权重）
                    distance.put(edge.to, distance.get(midNode) + edge.weight);
                } else {
                    // 如果当前入点已经在距离表中，就用源节点到中继的距离+当前边的权重刷新距离
                    distance.put(edge.to, Math.min(distance.get(edge.to), distance.get(midNode) + edge.weight));
                }
            }
            // 中继节点加入被访问的集合
            visited.add(midNode);
            // 选出新的中继节点
            midNode = getUnvisitedClosestNode(distance, visited);
        }

        return distance;
    }

    /**
     * 从未被访问过的点中选择距离最短的一个
     */
    public static Node getUnvisitedClosestNode(Map<Node, Integer> distance, Set<Node> visited) {
        int minDistance = Integer.MAX_VALUE;
        Node result = null;
        for (Node node : distance.keySet()) {
            if (!visited.contains(node) && distance.get(node) < minDistance) {
                minDistance = distance.get(node);
                result = node;
            }
        }
        return result;
    }


    /**
     *
     */


    /**
     * 迪杰斯特拉加强堆版
     * 优化在每次从没访问过的节点中选出距离最小的一个用加强堆实现，每次log(N)
     */
    public static Map<Node, Integer> dijkstraWithHead(Node from, int size) {
        // 记录结果，离源节点最小距离表
        Map<Node, Integer> distanceMap = new HashMap<>();
        // 加强堆，用来快速得到没被访问过的节点中距离最小的一个，作为中继节点
        NodeHeap heap = new NodeHeap(size);
        // 源节点自身作为第一个中继节点
        heap.addOrUpdateOrIgnore(from, 0);
        while (!heap.isEmpty()) {
            // 弹出没被访问过的节点中距离最小的一个，作为中继节点
            Record record = heap.pop();
            Node midNode = record.node;
            int midDistance = record.distance;

            for (Edge edge : midNode.edges) {
                // 以中继节点作为跳板，得到下一个节点的距离是中继节点距离+当前边权重，刷新下个节点的距离
                heap.addOrUpdateOrIgnore(edge.to, midDistance + edge.weight);
            }
            // 当前到中继节点的距离已经是最短距离了，加入结果距离表
            // 为什么中继节点的距离已经是最短路了？因为中继节点是从源节点当前可达的所有节点中选出的最近的节点，这就意味着无论再以谁为跳板，都不可能离中继节点更近了，也就是中继节点的距离不会再刷新了
            distanceMap.put(midNode, midDistance);
        }
        return distanceMap;
    }

    /**
     * 辅助类，用于封装堆返回的结果
     */
    public static class Record {
        public Node node;
        public int distance;

        public Record(Node node, int distance) {
            this.node = node;
            this.distance = distance;
        }
    }

    /**
     * 加强堆
     */
    public static class NodeHeap {
        // 堆本体
        private Node[] nodes;
        // 堆大小
        private int size;
        // 加强堆特有的反向索引表，节点没在表里说明没进过堆，节点在表里且index=-1表示进过堆但已经弹出了
        private Map<Node, Integer> indexMap;
        // 用于迪杰斯特拉算法的距离表
        private Map<Node, Integer> distanceMap;

        public NodeHeap(int size) {
            nodes = new Node[size];
            size = 0;
            indexMap = new HashMap<>();
            distanceMap = new HashMap<>();
        }

        public boolean isEmpty() {
            return size == 0;
        }

        /**
         * 新节点加入堆，或者更新堆节点，或者忽略
         */
        public void addOrUpdateOrIgnore(Node node, int distance) {
            if (!indexMap.containsKey(node)) {
                // 如果反向索引表没记录过这个节点，说明该节点从来没加入过堆，现在要加入
                nodes[size] = node;
                indexMap.put(node, size);
                distanceMap.put(node, distance);
                // 堆底加入新节点后，尝试上浮，重建堆结构
                heapUp(size++);
            } else if (indexMap.get(node) != -1) {
                // 如果反向索引表中的索引是-1，说明之前加入过，但是已经弹出了，忽略
                // 如果索引不是-1，说明该节点正在堆中，现在要更新
                if (distance < distanceMap.get(node)) {
                    // 如果该节点的新距离比原有距离小，才更新
                    distanceMap.put(node, distance);
                    // 更新后尝试上浮，重建堆结构。因为只会比原来小，所以只需要上浮，不需要下沉
                    heapUp(indexMap.get(node));
                }
            }
        }

        /**
         * 弹出堆顶节点
         */
        public Record pop() {
            // 封装返回结果
            Record result = new Record(nodes[0], distanceMap.get(nodes[0]));

            // 堆顶节点和堆底节点交换，要弹出的堆顶节点现在来到了size-1
            swap(0, size - 1);

            // 从堆本体中删除该节点
            nodes[size - 1] = null;
            // 从距离表中删除该节点
            distanceMap.remove(nodes[size - 1]);
            // 索引表中不删除，索引改为-1，表示入过堆，但是已经弹出了
            indexMap.put(nodes[size - 1], -1);
            // 堆大小-1
            size--;

            // 堆底节点来到堆顶后，尝试下沉，重建堆结构
            heapDown(0);

            return result;
        }

        /**
         * 堆中两个节点交换位置
         */
        private void swap(int a, int b) {
            // 交换反向索引表
            indexMap.put(nodes[a], b);
            indexMap.put(nodes[b], a);

            // 交换堆数组
            Node temp = nodes[a];
            nodes[a] = nodes[b];
            nodes[b] = temp;

        }

        /**
         * 堆节点上浮，重建堆结构
         */
        private void heapUp(int cur) {
            // 堆中i位置节点的父亲是(i-1)/2位置的节点
            while (distanceMap.get(nodes[cur]) < distanceMap.get(nodes[(cur - 1) / 2])) {
                // 只要当前节点比父节点小，就和父节点交换
                swap(cur, (cur - 1) / 2);
                cur = (cur - 1) / 2;
            }
        }

        /**
         * 堆节点下沉，重建堆结构
         */
        private void heapDown(int cur) {
            // 堆中i位置的左孩子是i*2+1位置，右孩子是i*2+2位置
            int left = cur * 2 + 1;
            int right = cur * 2 + 2;

            // 只要左孩子没越界
            while (left < size) {
                // 如果右孩子没越界并且右孩子小于左孩子，就选中右孩子，否则选中左孩子
                int better = right < size && distanceMap.get(nodes[right]) < distanceMap.get(nodes[left]) ? right : left;
                // 如果左右孩子中被选中的那个并不比父亲小，父亲就不用下沉了
                if (distanceMap.get(nodes[cur]) < distanceMap.get(nodes[better])) {
                    break;
                }
                // 父亲和孩子中被选中的那个交换
                swap(cur, better);
                // 被选中的孩子作为父亲继续下沉
                cur = better;
                // 算出新的孩子节点的位置
                left = cur * 2 + 1;
                right = cur * 2 + 2;
            }
        }
    }
}
