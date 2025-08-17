package tixiban.class1_13.linkedlist;

import tixiban.class1_13.recurrence.MergeSort;

import java.util.Arrays;
import java.util.Stack;

/**
 * 回文
 * 给定一个单链表，校验是否是回文（正着遍历和反着遍历一样）
 *
 * 链表技巧：
 * 1、借助容器，如列表、栈等，笔试用
 * 2、快慢指针法，面使用
 */
public class Palindrome {
    public static class Node {
        public int value;
        public Node next;

        public Node(int value) {
            this.value = value;
            this.next = null;
        }
    }

    /**
     * 判断一个单链表是不是回文结构
     * 面试用这个方法，因为保证了时间复杂度的同时还兼顾了额外空间复杂度，只申请有限几个变量。
     */
    public static boolean palindrome(Node head) {
        if (null == head) {
            return false;
        }
        // 找到链表的中点
        Node mid = getMidNode(head);
        // 从中点往右反转链表，返回新的头，也就是原来的尾
        Node tail = reverse(mid);

        Node left = head;
        Node right = tail;
        boolean result = true;
        // 每次比较第n个（left）和倒数第n个（right）是否相等，有一个不相等，就返回false
        // 直到有一个指针变为null（从中点反转后，中点的next是null，两指针都往中点移动，总有一个成为null）
        while (null != left && null != right) {
            if (left.value != right.value) {
                // 注意这里不要直接return false，必须要把前面反转的部分复原
                result = false;
            }
            left = left.next;
            right = right.next;
        }
        // 把反转的部分复原，从尾节点开始执行，到中点会停下，因为中点的next指向null
        reverse(tail);
        return result;
    }

    /**
     * 借助辅助栈判断单链表是不是回文结构
     * 笔试用这个方法，因为简单，不需要考虑边界条件，链表操作难的地方就只在边界条件
     */
    public static boolean palindromeWithStack(Node head) {
        // 申请一个栈
        Stack<Node> stack = new Stack<>();
        // 链表正序入栈
        Node cur = head;
        while (null != cur) {
            stack.push(cur);
            cur = cur.next;
        }
        // 入栈顺序和出栈顺序是反的，反序出栈和正序遍历比较，如果都一样就是回文
        cur = head;
        while (null != cur) {
            if (cur.value != stack.pop().value) {
                return false;
            }
            cur = cur.next;
        }
        return true;
    }

    /**
     * 快慢指针法求单链表中点
     */
    public static Node getMidNode(Node head) {
        if (null == head) {
            return null;
        }
        // 快指针，每次移动两步
        Node fast = head;
        // 慢指针，每次移动一步
        Node slow = head;
        // 快指针移动到最后一个节点（奇数个节点），或者倒数第二个节点（偶数个节点）时，慢指针恰好移动到中点
        while (null != fast.next && null != fast.next.next) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    /**
     * 单链表反转
     */
    public static Node reverse(Node head) {
        Node pre = null;
        Node next = null;

        // 循环对每个节点操作
        while (null != head) {
            // 先记录原next
            next = head.next;
            // 现next设为原pre
            head.next = pre;
            // 两个指针整体后移，准备下次循环
            pre = head;
            head = next;
        }
        return pre;
    }

    public static void print(Node head) {
        Node current = head;
        while (null != current) {
            System.out.print(current.value + " ");
            current = current.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        for(int n=0;n<10000;n++){
            int[] arr = MergeSort.generateArr(6, 1);

            if(arr.length<1){
                continue;
            }

            Node head = new Node(arr[0]);
            Node cur = head;
            for (int i = 1; i < arr.length; i++) {
                cur.next = new Node(arr[i]);
                cur = cur.next;
            }

            boolean palindrome = palindrome(head);
            boolean withStack = palindromeWithStack(head);

            if (palindrome != withStack){
                System.out.println(Arrays.toString(arr));
                System.out.println("palindrome: "+ palindrome +", withStack:"+ withStack);
                System.out.println("失败！");
                break;
            }
        }
    }
}
