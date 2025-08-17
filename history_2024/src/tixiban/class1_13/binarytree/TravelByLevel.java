package tixiban.class1_13.binarytree;

import tixiban.class1_13.binarytree.common.Node;

import java.util.LinkedList;
import java.util.Queue;

public class TravelByLevel {
    public void travelByLevel(Node head) {
        if (head == null) {
            return;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(head);
        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            if (cur != null) {
                System.out.println(cur.value);

                queue.add(cur.left);
                queue.add(cur.right);
            }
        }
    }
}
