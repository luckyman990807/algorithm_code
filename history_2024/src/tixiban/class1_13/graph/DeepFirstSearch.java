package tixiban.class1_13.graph;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * 深度优先遍历
 */
public class DeepFirstSearch {
    /**
     * 深度优先遍历迭代版
     */
    public static void dfsIteration(Node start) {
        if (start == null) {
            return;
        }
        Stack<Node> stack = new Stack<>();
        Set<Node> set = new HashSet<>();
        stack.push(start);
        // 深度优先遍历，压入栈的时候打印
        System.out.println(start.value);
        set.add(start);

        while (!stack.empty()) {
            Node cur = stack.pop();
            for (Node next : cur.nexts) {
                if (!set.contains(next)) {
                    // 如果发现当前节点有一个没遍历过的next，就把当前节点也压栈，把这个next也压栈，退出next循环，栈里始终保存的是当前的路线
                    stack.push(cur);
                    stack.push(next);
                    System.out.println(next.value);
                    set.add(next);
                    break;
                }
            }
        }
    }
}
