package tixiban.class1_13.binarytree.treedp;

import java.util.List;

/**
 * 多叉树表示公司层级关系，每个节点表示一个员工，节点的属性有一个int happy表示员工快乐值，一个List<Node>表示下属。
 * 现在公司要举办一个聚会，规则是直接上下级不能一起邀请。求这个聚会的最大快乐值。
 */
public class MaxHappy {
    public static class Node {
        public int value;
        public List<Node> next;
    }

    public static class Info {
        // 当前树邀请头节点产生的最大快乐值
        public int yes;
        // 当前树不邀请头节点产生的最大快乐值
        public int no;

        public Info(int yes, int no) {
            this.yes = yes;
            this.no = no;
        }
    }

    public Info process(Node cur) {
        if (cur == null) {
            // 如果当前节点是空，那么邀请也是0，不邀请也是0
            return new Info(0, 0);
        }

        // 邀请当前节点，默认值是当前节点的value
        int yes = cur.value;
        // 不邀请当前节点，默认值是0
        int no = 0;

        // 从子树拿信息
        for (Node next : cur.next) {
            Info nextInfo = process(next);
            // 当前节点参加，他的直接孩子只能不参加。当前节点参加的快乐值加上孩子不参加的快乐值
            yes += nextInfo.no;
            // 当前节点不参加，直接孩子可以参加也可以不参加，当前节点参加的快乐值加上Max(孩子参加的快乐值，孩子不参加的快乐值)
            no += Math.max(nextInfo.yes, nextInfo.no);
        }

        return new Info(yes, no);
    }

    public int getMaxHappy(Node head) {
        Info info = process(head);
        // 头节点可以参加也可以不参加，取二者最大的快乐值
        return Math.max(info.yes, info.no);
    }
}
