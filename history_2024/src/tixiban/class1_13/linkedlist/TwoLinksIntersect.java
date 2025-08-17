package tixiban.class1_13.linkedlist;

import java.util.HashSet;
import java.util.Set;

/**
 * 给定两个单链表，可能有环也可能无环，判断二者是否相交，如果相交返回第一个交点，否则返回null
 * <p>
 * 1、首先是环的问题，如何判断有环？如何判断无环？
 * 2、其次是相交的问题，有环无环分别如何计算相交？
 */
public class TwoLinksIntersect {
    /**
     * 给定一个单链表，返回第一个入环节点，如果没有环，返回空
     * 借助容器实现，思路简单，笔试用
     */
    public Node getFirstLoopNodeUseSet(Node head) {
        Set<Node> set = new HashSet<>();
        Node cur = head;
        while (cur != null) {
            // 如果容器里已经有这个节点了，说明这个节点就是入环节点
            if (set.contains(cur)) {
                return cur;
            }
            // 把节点依次加入容器
            set.add(cur);
            cur = cur.next;
        }
        return null;
    }

    /**
     * 给定一个单链表，返回第一个入环节点，如果没有环，返回空
     * 快慢指针法，不借助容器，节省空间，有特殊技巧，面试用
     * 注意：如果一个单链表有环：那么一定是尾部有环，不可能头部有环尾部无环，因为单链表只有一个next，不可能叉出去
     */
    public Node getFirstLoopNode(Node head) {
        if (head == null || head.next == null || head.next.next == null) {
            return null;
        }
        // 快慢指针
        Node fast = head.next;
        Node slow = head.next.next;
        // 如果有环，快慢指针必相遇
        while (slow != fast) {
            // 如果遍历到null，说明无环，直接返回null
            if (slow.next == null || fast.next == null || fast.next.next == null) {
                return null;
            }
            // 慢指针一次走一步，快指针依次走两步
            slow = slow.next;
            fast = fast.next.next;
        }
        // 快慢指针相遇后，快指针回到头节点，慢指针还留在相遇节点
        // 现在快慢指针都一次走一步，必在入环节点处相遇
        fast = head;
        while (fast != slow) {
            fast = fast.next;
            slow = slow.next;
        }
        return fast;
    }


    /**
     * 给定两个无环单链表，返回第一个相交的节点，如果不相交返回空
     * 借助容器，思路简单，笔试用
     */
    public Node getIntersectNodeNoLoopUseSet(Node head1, Node head2) {
        if (head1 == null || head2 == null) {
            return null;
        }
        // head1全部录入set
        Set<Node> set = new HashSet<>();
        Node cur = head1;
        while (cur != null) {
            set.add(cur);
            cur = cur.next;
        }
        // 遍历head2，如果在set中出现了这个节点，那么该节点就是第一个相交节点
        cur = head2;
        while (cur != null) {
            if (set.contains(cur)) {
                return cur;
            }
            cur = cur.next;
        }
        // 如果能遍历完，那么不相交
        return null;
    }

    /**
     * 给定两个无环单链表，返回第一个相交的节点，如果不相交，返回空
     * 注意：如果两个单链表相交，那么相交之前的长度有可能不一样，但是相交之后的节点一定一摸一样，因为单链表只有一个next，不会叉出去
     */
    public Node getIntersectNodeNoLoop(Node head1, Node head2) {
        if (head1 == null || head2 == null) {
            return null;
        }
        int length = 1;
        Node cur1 = head1;
        Node cur2 = head2;
        // 遍历head1并记录长度
        while (cur1.next != null) {
            length++;
            cur1 = cur1.next;
        }
        // 遍历head2并减去长度
        while (cur2.next != null) {
            length--;
            cur2 = cur2.next;
        }
        // 如果两个链表的尾节点不是同一个，那么必然不相交
        if (cur1 != cur2) {
            return null;
        }
        // 如果length>0，说明head1更长，否则head2更长
        // cur1记录更长的链表，cur2记录更短的链表
        cur1 = length > 0 ? head1 : head2;
        cur2 = cur1 == head1 ? head2 : head1;
        // 更长的链表先走完二者的长度差
        length = Math.abs(length);
        while (length != 0) {
            cur1 = cur1.next;
            length--;
        }
        // 走完长度差后一起走，第一个相等的节点就是相交的第一个节点
        while ((cur1 != cur2)) {
            cur1 = cur1.next;
            cur2 = cur2.next;
        }
        return cur1;
    }

    /**
     * 给定两个有环单链表，返回第一个相交的节点，如果不相交，返回空
     * 只有三种情况：
     * 1）不相交
     * 2）相交，且入环节点是同一个（在入环之前相交）
     * 3）相交，但入环节点不是同一个（并入同一个环）
     */
    public Node getIntersectNodeLoop(Node head1, Node loop1, Node head2, Node loop2) {
        if (head1 == null || head2 == null) {
            return null;
        }
        if (loop1 == loop2) {
            // 如果两个链表入环节点是同一个，那么肯定相交了。他俩不一样的地方就是相交之前那段
            // 和两个无环单链表求第一个交点一样做
            int length = 1;
            Node cur1 = head1;
            Node cur2 = head2;
            while (cur1.next != loop1) {
                length++;
                cur1 = cur1.next;
            }
            while (cur2.next != loop2) {
                length--;
                cur2 = cur2.next;
            }
            // 如果length>0，说明head1更长，否则head2更长
            // cur1记录更长的链表，cur2记录更短的链表
            cur1 = length > 0 ? head1 : head2;
            cur2 = cur1 == head1 ? head2 : head1;
            // 长链表先走完长度查
            length = Math.abs(length);
            while (length != 0) {
                cur1 = cur1.next;
                length--;
            }
            // 再一起走，第一个相等的节点就是第一个交点
            while (cur1 != cur2) {
                cur1 = cur1.next;
                cur2 = cur2.next;
            }
            return cur1;
        } else {
            // 如果两个链表入环节点不相等，说有两种情况：1）两链表不相交，2）两链表相交但入环节点不同
            // 从loop1开始遍历，如果中途遇到loop2，说明在同一个环上，两链表相交，loop1和loop2都可以认为是第一个相交的节点
            // 如果从loop1转完一圈又回到loop1，都没有遇到loop2，说明二者不相交
            Node cur = loop1.next;
            while (cur != loop1) {
                if (cur == loop2) {
                    return loop1;
                }
                cur = cur.next;
            }
            return null;
        }
    }

    /**
     * 给定两个单链表，可能有环也可能无环，返回第一个相交的节点，如果不相交，返回空
     * 第一步，根据有环无环分情况
     * 第二步，每种情况计算相交节点
     */
    public Node getIntersectNode(Node head1, Node head2) {
        Node loop1 = getFirstLoopNode(head1);
        Node loop2 = getFirstLoopNode(head2);
        if (loop1 == null && loop2 == null) {
            // 两个无环链表计算相交
            return getIntersectNodeNoLoop(head1, head2);
        } else if (loop1 != null && loop2 != null) {
            // 两个有环链表计算相交
            return getIntersectNodeLoop(head1, loop1, head2, loop2);
        }
        // 一个有环一个无环，必不可能相交。因为环只能在尾部，相交也只能是尾部相交，相交的部分完全相同，所以相交的两个链表只要一个有环，另一个必然有环
        return null;
    }


    public static class Node {
        public int value;
        public Node next;

        public Node(int value) {
            this.value = value;
            this.next = null;
        }
    }
}
