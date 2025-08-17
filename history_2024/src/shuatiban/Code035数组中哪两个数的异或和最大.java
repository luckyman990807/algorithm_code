package shuatiban;

/**
 * 给定一个数组arr，想知道arr中哪两个数的异或结果最大，返回最大的异或结果
 * Leetcode题目：https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/
 *
 * 上一道题求子数组最大异或和,需要在前缀数里存前缀异或和,因为一个前缀异或和异或上另一个前缀异或和,才能得到任意子数组异或和.
 * 这道题求两个数的最大异或和,直接在前缀数里存数组元素即可.
 */
public class Code035数组中哪两个数的异或和最大 {
    public static class Node {
        Node[] next = new Node[2];
    }

    public static class Trie {
        Node head = new Node();

        public void add(int value) {
            Node cur = head;
            for (int i = 31; i >= 0; i--) {
                int path = (value >> i) & 1;
                cur.next[path] = cur.next[path] == null ? new Node() : cur.next[path];
                cur = cur.next[path];
            }
        }

        public int maxXor(int value) {
            Node cur = head;
            int result = 0;
            for (int i = 31; i >= 0; i--) {
                int path = (value >> i) & 1;
                int expect = i == 31 ? path : (path ^ 1);
                int real = cur.next[expect] != null ? expect : (expect ^ 1);
                result |= (path ^ real) << i;
                cur = cur.next[real];
            }
            return result;
        }
    }

    public static int findMaximumXOR(int[] arr) {
        Trie trie = new Trie();
        int result = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            trie.add(arr[i]);
            result = Math.max(result, trie.maxXor(arr[i]));
        }
        return result;
    }
}
