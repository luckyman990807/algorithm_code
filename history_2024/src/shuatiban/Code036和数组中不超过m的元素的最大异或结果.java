package shuatiban;

/**
 * https://leetcode.cn/problems/maximum-xor-with-an-element-from-array/description/
 * 给定一个数组nums,和一个查询数组queries,queries[i]=[xi,mi],表示要查询xi跟nums中不超过mi的元素的最大异或结果,
 * 返回一个answer数组作为查询结果,answer[i]表示queries[i]的查询结果.
 *
 * 思路:
 * 先把nums依次加入前缀树,再遍历queries用xi从前缀树里计算最大异或和.
 * 卡点:怎么控制跟xi计算异或和的元素是不超过mi的?
 * 解法:前缀数的节点加一个字段min表示经过这套路径的所有数中最小的一个是多少.求最大异或和选择路径时对min做强限制,如果min>mi就说明没有路,查询结果是-1.
 */
public class Code036和数组中不超过m的元素的最大异或结果 {
    public static class Node {
        Node[] next;
        int min;

        public Node() {
            this.min = Integer.MAX_VALUE;
            next = new Node[2];
        }
    }

    public static class Trie {
        Node head = new Node();

        public void add(int value) {
            Node cur = head;
            head.min = Math.min(head.min, value);
            for (int i = 30; i >= 0; i--) {
                int path = (value >> i) & 1;
                cur.next[path] = cur.next[path] == null ? new Node() : cur.next[path];
                cur.next[path].min = Math.min(cur.next[path].min, value);
                cur = cur.next[path];
            }
        }

        public int maxXor(int value, int m) {
            if (head.min > m) {
                return -1;
            }
            Node cur = head;
            int result = 0;
            for (int i = 30; i >= 0; i--) {
                int path = (value >> i) & 1;
                int expect = path ^ 1;
                int real = cur.next[expect] != null && cur.next[expect].min <= m ? expect : (expect ^ 1);
                result |= (path ^ real) << i;
                cur = cur.next[real];
            }
            return result;
        }
    }

    public static int[] maximizeXor(int[] nums, int[][] queries) {
        Trie trie = new Trie();
        int[] result = new int[queries.length];
        for (int i = 0; i < nums.length; i++) {
            trie.add(nums[i]);
        }
        for (int i = 0; i < queries.length; i++) {
            result[i] = trie.maxXor(queries[i][0], queries[i][1]);
        }
        return result;
    }
}
