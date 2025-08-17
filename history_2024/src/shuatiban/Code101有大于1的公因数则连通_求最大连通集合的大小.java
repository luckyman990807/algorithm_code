package shuatiban;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 给定一个由不同正整数的组成的非空数组 A，考虑下面的图：有 A.length 个节点，按从 A[0] 到 A[A.length - 1] 标记；
 * 只有当 A[i] 和 A[j] 共用一个大于 1 的公因数时，A[i] 和 A[j] 之间才有一条边。返回图中最大连通集合的大小
 * Leetcode题目：https://leetcode.com/problems/largest-component-size-by-common-factor/
 * <p>
 * 思路：一看连通性就知道用并查集
 * 方法一（超时）：两重for循环，判断任意两个元素的最大公因数不等于1，就把他俩合并。
 *      求最大公因数用辗转相除法，时间复杂度可以认为O(1)。整体时间复杂度O(N^2)
 * 方法二：遍历每一个元素，找出元素的每一个因子，用一个map维护所有出现的因子及其所属的元素，如果这个因子同时也是其他元素的因子，那么就把这俩元素合并，否则就在map中记录这个因子。
 *      找一个数a的所有因子，时间复杂度O(根号a)，只需遍历1到根号a即可。整体时间复杂度O(N*根号maxValue)
 * <p>
 * <p>
 * 并查集基础知识：
 * 核心成员变量就是parents，记录每个节点的父亲，顺着父亲一直往上找，会找到当前集合的根，如果两个节点的根相同，说明他俩在一个集合中（也就是他俩连通/他俩有一条边）
 * 如果需要用到集合大小，就增加一个size数组，记录每个根节点所在集合的大小
 * 如果要用到按秩合并（高度小的树合并到高度大的树上），就增加一个rank数组，记录每个根节点的秩（树的高度）。否则，按大小合并也可以。
 * 如果路径压缩的时候不想每次都新申请数组，就增加一个path数组，暂存查找过程中经过的节点路径
 * 核心方法有2个，一个并，一个查。
 * 并是合并两个集合，这里讲究按秩合并，也就是把高度小的合并到高度大的上，避免树的高度增加过多而拖慢查询速度。如果两个集合的秩相等，就随便把一个合并到另一个上，然后最终的集合秩+1
 * 查是查找某个节点的根节点，如果两个节点的根相同，就可以判断这俩节点在同一个集合（即他俩连通/他俩有边）。这里讲究路径压缩，也就是顺着父亲往上找的过程中，把经过的所有节点都记录下来，最后统一挂到根节点上，这样能减小树的高度，加快查找速度
 */
public class Code101有大于1的公因数则连通_求最大连通集合的大小 {
    public int largestComponentSize(int[] nums) {
        // 并查集，把有相同因子的元素放到同一个集合
        UnionFind unionFind = new UnionFind(nums.length);
        // key：因子，value：哪个元素有这个因子。只记录一个元素即可，因为都加入了并查集，有一个就能找到集合
        Map<Integer, Integer> factMap = new HashMap<>();

        // 遍历每一个元素，找出元素的每一个因子，如果这个因子同时也是其他元素的因子，那么就把这俩元素合并，否则就在map中记录这个因子和所属的元素
        for (int i = 0; i < nums.length; i++) {
            // 找出n的每一个因子，只需要遍历从1到根号n即可，因为对于n的一对因子a和b，a*b=n，那么a和b中必然有一个小于等于根号n（否则a和b都大于根号n的话，a*b就必然大于n）。
            // 而找到了小于等于根号n的a，n/a就得到了b。所以只需要遍历1到根号n，就可以找出n的所有因子
            int sqrt = (int) Math.sqrt(nums[i]);
            for (int fact = 1; fact <= sqrt; fact++) {
                if (nums[i] % fact == 0) {
                    // fact是num的一个因子，且不等于1（因为题目要求不考虑1）
                    if (fact != 1) {
                        // 如果这个因子同时也是其他元素的因子，那么就把这俩元素合并，否则就在map中记录
                        if (factMap.containsKey(fact)) {
                            unionFind.union(i, factMap.get(fact));
                        } else {
                            factMap.put(fact, i);
                        }
                    }

                    // otherFact是num的另一个因子
                    int otherFact = nums[i] / fact;
                    if (otherFact != 1) {
                        if (factMap.containsKey(otherFact)) {
                            unionFind.union(i, factMap.get(otherFact));
                        } else {
                            factMap.put(otherFact, i);
                        }
                    }
                }
            }
        }
        return unionFind.maxSize;
    }


    /**
     * 并查集
     */
    public static class UnionFind {
        // 记录节点的父亲
        private int[] parents;

        // 暂存查找路径，用于路径压缩
        private int[] path;
        // 记录集合的秩
        private int[] rank;
        // 记录集合大小
        private int[] size;
        // 记录最大集合的大小
        public int maxSize;

        // 构造方法，这道题
        public UnionFind(int n) {
            parents = new int[n];
            path = new int[n];
            rank = new int[n];
            size = new int[n];
            maxSize = 1;
            for (int i = 0; i < n; i++) {
                parents[i] = i;
                size[i] = 1;
            }
        }

        /**
         * 查找某个节点所在集合的根节点
         *
         * @param a
         * @return
         */
        public int find(int a) {
            int i = 0;
            // 只要a的父亲不是自己（即，不是根节点），那么就让a等于它的父亲，直到a等于根节点，记录下沿途经过的所有节点
            while (parents[a] != a) {
                path[i++] = a;
                a = parents[a];
            }
            // 路径压缩，沿途经过的所有节点，直接挂到根节点上。最后一个经过的节点不需要处理，因为他已经挂在根节点上了
            for (int j = 0; j < i - 1; j++) {
                parents[path[j]] = a;
            }
            return a;
        }

        /**
         * 合并两个节点所在的集合
         *
         * @param a
         * @param b
         */
        public void union(int a, int b) {
            // 分别查出两个节点所在集合的根节点
            int rootA = find(a);
            int rootB = find(b);
            // 如果两个根节点不相同，就合并两个集合
            if (rootA != rootB) {
                // 两个集合，判断谁挂到谁上
                int parent;
                int child;
                if (rank[rootA] != rank[rootB]) {
                    // 如果两个集合的秩不同，就把秩小的挂到秩大的上
                    parent = rank[rootA] > rank[rootB] ? rootA : rootB;
                    child = rootA == parent ? rootB : rootA;
                } else {
                    // 如果两个集合的秩相同，挂谁上都行，最终的集合的秩+1
                    parent = rootA;
                    child = rootB;
                    rank[rootA]++;
                }
                // 合并后更新parents和size
                parents[child] = parent;
                size[parent] += size[child];
                maxSize = Math.max(maxSize, size[parent]);
            }
        }
    }

    public static void main(String[] args) {
        UnionFind unionFind = new UnionFind(5);
        unionFind.union(1, 2);
        unionFind.union(2, 3);

        System.out.println(Arrays.toString(unionFind.parents));
        System.out.println(unionFind.maxSize);
    }
}
