package tixiban.class1_13.graph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * 宽度优先遍历
 */
public class BreadthFirstSearch {
    public static void bfs(Node start) {
        if (start == null) {
            return;
        }
        Queue<Node> queue = new LinkedList<>();
        // set用于记录当前节点是否遍历过，因为当前节点可能是多个节点的next，如果不记录，当前节点可能被多次遍历
        Set<Node> set = new HashSet<>();
        queue.add(start);
        set.add(start);
        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            // 宽度优先遍历，从队列里弹出的时候打印
            System.out.println(cur.value);
            for (Node next : cur.nexts) {
                if (!set.contains(cur)) {
                    // 如果set里没有当前节点，说明之前每遍历过，就加入队列，并记录set
                    queue.add(cur);
                    set.add(cur);
                }
            }
        }
    }
}
