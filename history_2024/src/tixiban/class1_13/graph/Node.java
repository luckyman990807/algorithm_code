package tixiban.class1_13.graph;

import java.util.ArrayList;
import java.util.List;

public class Node {
    // 数值
    public int value;
    // 入度
    public int in;
    // 出度
    public int out;
    // 相邻节点，由本节点可达的节点
    public List<Node> nexts;
    // 由本结点出发的边
    public List<Edge> edges;

    public Node(int value) {
        this.value = value;
        in = 0;
        out = 0;
        nexts = new ArrayList<>();
        edges = new ArrayList<>();
    }
}
