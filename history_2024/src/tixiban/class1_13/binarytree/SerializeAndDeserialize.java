package tixiban.class1_13.binarytree;

import tixiban.class1_13.binarytree.common.Node;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 二叉树序列化和反序列化
 * 二叉树序列化注意：null节点要占位，不要放过
 */
public class SerializeAndDeserialize {
    /**
     * 序列化二叉树
     * 可以返回字符串，这里返回队列
     */
    public Queue<String> serialize(Node head) {
        Queue<String> queue = new LinkedList<>();
        preOrderSerialize(head, queue);
        return queue;
    }

    /**
     * 递归函数，先序遍历序列化
     * 如果想要后序遍历，只需要改一下顺序，先递归左，再递归右，最后add当前节点
     * 注意：中序遍历不做序列化，因为中序遍历得到的结果会歧义，不同的树可能中序遍历的结果一样，例如：
     * 1
     * /  \
     * #   2
     * / \
     * #  #
     * <p>
     * 和
     * 2
     * /  \
     * 1   #
     * / \
     * #  #
     * <p>
     * 中序遍历都是[#,1,#,2,#]
     */
    public void preOrderSerialize(Node head, Queue<String> result) {
        if (head == null) {
            result.add(null);
        } else {
            result.add(String.valueOf(head.value));
            preOrderSerialize(head.left, result);
            preOrderSerialize(head.right, result);
        }
    }

    /**
     * 反序列化二叉树
     */
    public Node deserialize(Queue<String> queue) {
        if (queue == null || queue.isEmpty()) {
            return null;
        }
        return preOrderDeserialize(queue);
    }

    /**
     * 递归函数，按照先序反序列化二叉树
     */
    public Node preOrderDeserialize(Queue<String> queue) {
        String value = queue.poll();
        if (value == null) {
            // 如果队列里弹不出东西了，或者就是个null元素，就返回null节点
            return null;
        }
        Node head = new Node(Integer.valueOf(value));
        head.left = preOrderDeserialize(queue);
        head.right = preOrderDeserialize(queue);
        return head;
    }

    /**
     * 按层序列化
     */
    public Queue<String> levelSerialize(Node head) {
        // 这个队列用于存放序列化结果
        Queue<String> result = new LinkedList<>();
        if (head == null) {
            result.add(null);
        } else {
            result.add(String.valueOf(head.value));
            // 这个队列用于按层遍历
            Queue<Node> levelQueue = new LinkedList<>();
            levelQueue.add(head);

            while (!levelQueue.isEmpty()) {
                Node cur = levelQueue.poll();

                if (cur.left != null) {
                    // 如果左孩子不为空，就既序列化，又按层遍历
                    result.add(String.valueOf(cur.left.value));
                    levelQueue.add(cur.left);
                } else {
                    // 如果左孩子为空，就只序列化
                    result.add(null);
                }

                if (cur.right != null) {
                    result.add(String.valueOf(cur.right.value));
                    levelQueue.add(cur.right);
                } else {
                    result.add(null);
                }
            }
        }
        return result;
    }

    /**
     * 按层反序列化
     */
    public Node levelDeserialize(Queue<String> serialQueue) {
        if (serialQueue == null || serialQueue.isEmpty()) {
            return null;
        }
        Node head = buidNode(serialQueue.poll());
        if (head == null) {
            // 按层序列化队列的第一个节点是null，说明整棵树就是个null
            return null;
        }
        Queue<Node> levelQueue = new LinkedList<>();
        levelQueue.add(head);

        while (!levelQueue.isEmpty()) {
            Node cur = levelQueue.poll();

            // 画图演示一下可知，序列化队列里前两个，一定是按层遍历队列第一个的左右孩子
            cur.left = buidNode(serialQueue.poll());
            if (cur.left != null) {
                // 如果左孩子不为空，就加入按层遍历队列
                levelQueue.add(cur.left);
            }

            cur.right = buidNode(serialQueue.poll());
            if (cur.right != null) {
                levelQueue.add(cur.right);
            }
        }
        return head;
    }

    /**
     * 根据一个字符串创建一个节点
     */
    public Node buidNode(String value) {
        if (value == null) {
            return null;
        }
        return new Node(Integer.valueOf(value));
    }
}
