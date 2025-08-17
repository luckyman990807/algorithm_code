package shuatiban;

/**
 * 数组中所有数都异或起来的结果，叫做异或和。给定一个数组arr，返回arr的最大子数组异或和
 *
 * 思路:
 * 如果是求子数组最大累加和,那么可以按照以i结尾的子数组最大累加和的思路,先求出所有数的前缀和,再遍历前缀和数组,每个前缀和减去前面一个最小的前缀和(用一个min记录),结果就是以当前i结尾的子数组的最大累加和.
 *
 * 求最大异或和也可以用类似的思路,遍历求每个位置结尾的最大子数组异或和,求的过程中比较出目前最大的答案.
 * 怎么求i位置结尾的最大子数组异或和? 求i位置的异或前缀和 异或上 i前面哪个位置的异或前缀和 的结果最大, 这个结果就是i结尾的最大子数组异或和.(i的前缀异或和异或上j的前缀异或和,等于j+1到i的子数组异或和)
 * 所以思路:遍历数组,每个位置先求出异或前缀和,再从前缀树中求出当前位置的最大子数组异或和,跟整体最大值比较记录结果,然后把当前异或前缀和也加入到前缀树中.
 *
 * 前缀树记录一个一个的异或前缀和,每个数据用节点的next的索引表示路径.
 */
public class Code034子数组最大异或和 {
    public static class Trie {
        Node trie = new Node();

        /**
         * 把value这个数的二进制展开加入到前缀树中,形成一条路径
         */
        public void add(int value) {
            Node cur = trie;
            for (int i = 31; i >= 0; i--) {
                // 提取出value每一位比特位
                int path = (value >> i) & 1;
                // 如果没有这条路就新建,有的话就复用
                cur.next[path] = cur.next[path] == null ? new Node() : cur.next[path];
                cur = cur.next[path];
            }
        }

        /**
         * 前缀树中已经存储了一堆数的二进制展开路径,现在求value跟前缀树中哪个树的异或结果最大,返回最大结果
         */
        public int getMaxXor(int value) {
            Node cur = trie;
            int result = 0;
            for (int i = 31; i >= 0; i--) {
                // 提取出value每一位比特位
                int path = (value >> i) & 1;
                // 当前比特位期待的路径(1期待0,0期待1,这样以获得结果最大.有个例外是符号位0期待0,1期待1,这样异或的结果起码是正数)
                int expect = i == 31 ? path : (path ^ 1);
                // 在前缀树中实际能遇到的路经(有expect这条路的话就走这条路,没有的话就只能退而求其次取反了)
                int real = cur.next[expect] != null ? expect : (expect ^ 1);
                // 把当前位的异或结果加到结果变量上(通过或的方式记录结果)
                result |= (path ^ real) << i;
                cur = cur.next[real];
            }
            return result;
        }
    }

    public static class Node {
        Node[] next = new Node[2];
    }

    public static int maxXor(int[] arr) {
        // 记录最终答案
        int result = Integer.MIN_VALUE;
        // 记录当前位置的异或前缀和
        int xor = 0;

        Trie trie = new Trie();
        // 先把0加进去,用于求0到i的子数组异或和
        trie.add(0);
        for (int i = 0; i < arr.length; i++) {
            // 当前位置的异或前缀和
            xor ^= arr[i];
            // 当前位置结尾的最大子数组异或和
            int subArrXor = trie.getMaxXor(xor);
            // 跟整体最大值比较,记录最终结果
            result = Math.max(result, subArrXor);
            // 当前位置异或前缀和也加入前缀树
            trie.add(xor);
        }
        return result;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 5};
        System.out.println(maxXor(arr));
    }
}
