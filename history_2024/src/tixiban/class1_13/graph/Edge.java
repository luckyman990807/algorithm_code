package tixiban.class1_13.graph;

public class Edge {
    // 权重
    public int weight;
    // 出点
    public Node from;
    // 入点
    public Node to;

    public Edge(int weight, Node from, Node to) {
        this.weight = weight;
        this.from = from;
        this.to = to;
    }
}
