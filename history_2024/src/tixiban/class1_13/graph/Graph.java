package tixiban.class1_13.graph;

import java.util.Map;
import java.util.Set;

public class Graph {
    // 点的集合。为什么用map？因为题目或用户给的是数值，我们要封装成Node，二者需要有一个映射
    public Map<Integer, Node> nodes;
    // 边的集合
    public Set<Edge> edges;

    public Graph(Map<Integer, Node> nodes, Set<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public Graph() {
    }

    public Graph build(int[][] matrix) {
        Graph graph = new Graph();
        for (int i = 1; i < matrix.length; i++) {
            int weight = matrix[i][0];
            int from = matrix[i][1];
            int to = matrix[i][2];

            if (!graph.nodes.containsKey(from)) {
                graph.nodes.put(from, new Node(from));
            }
            if (!graph.nodes.containsKey(to)) {
                graph.nodes.put(to, new Node(to));
            }

            Node fromNode = graph.nodes.get(from);
            Node toNode = graph.nodes.get(to);

            Edge edge = new Edge(weight, fromNode, toNode);
            graph.edges.add(edge);

            fromNode.out++;
            fromNode.nexts.add(toNode);
            fromNode.edges.add(edge);

            toNode.in++;
        }
        return graph;
    }
}
