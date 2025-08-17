package shuatiban;

/**
 * https://leetcode.com/problems/bricks-falling-when-hit/
 * 给定一个矩阵，1表示有砖块，0表示空白。第一行的砖块认为是连在天花板上的，跟第一行的砖块联通（上下左右四个方向）的所有砖块都认为是连在天花板上的。
 * 给定一个二维数组，表示炮弹每次打击的位置，打到砖块的话砖块会直接碎掉，后面连通的砖块会掉落。
 * 问每次打击会有几块砖头掉落，返回一个结果数组。
 *
 * 思路：逆向思维+并查集
 * 炮弹把连通的大集合分裂成小集合，如果把打击的过程倒过来考虑，就是炮弹逆序地把小集合合并成大集合，每次合并完，天花板集合增加的元素个数-1，实际上就是这个炮弹打击的时候掉落的个数。
 * 集合，合并，统计个数————并查集。并查集就是用来快速对集合进行合并和查询的数据结构。
 */
public class Code068打砖块 {
    /**
     * 并查集
     */
    public class UnionFind {
        // parent表，下标表示某个节点的id，值表示这个节点的父亲节点的id。父亲节点就是当前节点所在集合中的头节点
        int[] parent;
        // size表，下标是某个头节点的id，值表示以这个节点为头的集合的大小。当一个节点以前是头后来合并到其他头上，那么不需要删除这个节点的无用数据，因为不影响查询
        int[] size;

        // 构造函数，n是节点总个数
        public UnionFind(int n) {
            parent = new int[n];
            size = new int[n];

            for (int i = 0; i < n; i++) {
                // 初始时默认每个节点都自己一个集合，头节点是自己，集合大小为1
                parent[i] = i;
                size[i] = 1;
            }
        }

        // 查，查某个节点属于哪个集合，也就是查某个节点所在集合的头节点。顺便压缩路径，有的节点像链表一样挂在头节点上，为了查询快，在find的时候把链表扁平化，即每个节点都直接挂在头节点上
        public int find(int id) {
            // 如果当前节点的父亲不是自己，说明当前节点不是头节点，那么递归找父亲的父亲，一直找到头节点，返回作为当前节点的父亲。也可以用栈代替递归记录往上找头节点的路径，会更好理解
            if (parent[id] != id) {
                parent[id] = find(parent[id]);
            }
            return parent[id];
        }

        // 并，把两个节点所在的集合合并成一个大集合。大集合的头由哪边做无所谓，反正通过任何一方都能找到大集合的头
        public void union(int id1, int id2) {
            int head1 = find(id1);
            int head2 = find(id2);
            // 如果两个节点的父亲不同，才有合并的必要
            if (head1 != head2) {
                parent[head1] = head2;
                size[head2] += size[head1];
            }
        }

        // 给定一个节点id，计算这个节点所在集合的大小
        public int getSize(int id) {
            // 找到这个节点的父亲，用父亲查size表
            return size[find(id)];
        }
    }

    /**
     * 打砖块功能函数
     */
    public int[] hitBricks(int[][] grid, int[][] hits) {
        // 先把该打的砖块都打掉，把连通的集合分裂
        for (int i = 0; i < hits.length; i++) {
            int r = hits[i][0];
            int c = hits[i][1];
            // 只有当前位置是砖块，才有打的必要
            if (grid[r][c] == 1) {
                grid[r][c] = -1;
            }
        }

        int rows = grid.length;
        int cols = grid[0].length;
        int size = rows * cols;
        // 并查集用id=size的冗余节点充当“天花板”，某个砖块跟size节点在一个集合，就表示这个砖块是和天花板相连的
        UnionFind unionFind = new UnionFind(size + 1);

        // 遍历砖块数组，录入并查集
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // 只有当前位置是砖块，才有录入的必要
                if (grid[r][c] == 1) {
                    int id = r * cols + c;
                    // 初始化节点
                    unionFind.parent[id] = id;
                    unionFind.size[id] = 1;
                    // 跟周围的节点合并
                    if (r == 0) {
                        // 如果当前节点在第0行，直接去跟天花板合并
                        unionFind.union(id, size);
                    } else {
                        // 如果不是第0行，就尝试跟上、左位置合并。合并的前提是上左位置不越界且是砖块
                        if (grid[r - 1][c] == 1) {
                            unionFind.union(id, (r - 1) * cols + c);
                        }
                        if (c - 1 >= 0 && grid[r][c - 1] == 1) {
                            unionFind.union(id, r * cols + c - 1);
                        }
                    }
                }
            }
        }

        // 逆向打砖块，也就是倒序恢复砖块，恢复砖块后如果跟天花板连通的砖块数发生变化，那么差值-1就是打中砖块应该掉落的砖块数。为什么-1？因为恢复的那块砖块也被算进增量里了，但实际被打中之后会碎掉不会掉落，所以要排除
        int[] ans = new int[hits.length];
        for (int i = hits.length - 1; i >= 0; i--) {
            int r = hits[i][0];
            int c = hits[i][1];
            // 如果这个位置标记被打中了砖块，才有恢复的必要
            if (grid[r][c] == -1) {
                grid[r][c] = 1;
                int id = r * cols + c;
                // 记录恢复当前砖块前 跟天花板连通的砖块数
                int preSize = unionFind.getSize(size);
                // 如果当前位置是第0行，就要和天花板合并一下
                if (r == 0) {
                    unionFind.union(id, size);
                }
                // 然后跟上下左右尝试合并，不会重复合并，因为union方法有校验，只有两个节点的父不同才合并
                if (r - 1 >= 0 && grid[r - 1][c] == 1) {
                    unionFind.union(id, (r - 1) * cols + c);
                }
                if (r + 1 < rows && grid[r + 1][c] == 1) {
                    unionFind.union(id, (r + 1) * cols + c);
                }
                if (c - 1 >= 0 && grid[r][c - 1] == 1) {
                    unionFind.union(id, r * cols + c - 1);
                }
                if (c + 1 < cols && grid[r][c + 1] == 1) {
                    unionFind.union(id, r * cols + c + 1);
                }
                // 记录恢复当前砖块后 跟天花板连通的砖块数
                int newSize = unionFind.getSize(size);
                // 如果数量没变，就说明不会有原本连通天花板的砖块掉落，答案是0。否则答案是增量-1
                ans[i] = newSize == preSize ? 0 : newSize - preSize - 1;
            }
        }
        return ans;
    }
}
