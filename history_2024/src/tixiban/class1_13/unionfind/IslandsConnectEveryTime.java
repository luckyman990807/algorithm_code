package tixiban.class1_13.unionfind;

/**
 * 岛问题。
 * 二维数组中，值只有1和0，规定相邻的1（上下左右相邻）可以看作一个岛。
 * 给定一个长和宽，表示一个没有任何岛的全0二维数组，再给定一个二维数组，表示每次添加1的位置，要求返回一个一维数组表示每次添加后岛的数目。
 * 例如给定4,5,[[0,0],[2,1],[2,2]]，表示一个4*5的全0数组，第一次在0,0位置添加1，第二次在2,1位置添加1，第三次在2,2位置添加1，返回[1,2,2]表示第一次添加完有1个岛，第二次添加完有2个岛，第三次添加完有2个岛。
 * <p>
 * 思路：和原始岛问题的区别：动态初始化*
 */
public class IslandsConnectEveryTime {
    public static int[] countIslands(int r, int c, int[][] positions) {
        UnionFind unionFind = new UnionFind(r, c);
        int[] islands = new int[positions.length];
        int index = 0;
        for (int[] position : positions) {
            islands[index++] = unionFind.connect(position[0], position[1]);
        }
        return islands;
    }


    public static class UnionFind {
        private int[] parent;
        private int[] size;
        private int[] path;
        private int sets;
        private int rows;
        private int cols;

        public UnionFind(int r, int c) {
            // 初始化并查集，把二位数组转成一维数组表示。只初始化成员变量，不做集合的增加
            rows = r;
            cols = c;
            parent = new int[r * c];
            size = new int[r * c];
            path = new int[r * c];
            sets = 0;
        }

        private int sets() {
            return sets;
        }

        private int index(int x, int y) {
            return x * cols + y;
        }

        // 给定一个节点，找到节点所在集合的代表节点
        private int findHead(int i) {
            int pathI = 0;
            while (parent[i] != i) {
                path[pathI++] = i;
                i = parent[i];
            }
            for (pathI--; pathI >= 0; pathI--) {
                parent[pathI] = i;
            }
            return i;
        }

        // 给定两个二维坐标，把两个位置合并
        public void union(int r1, int c1, int r2, int c2) {
            // 判断是否超过二维数组边界
            if (r1 < 0 || r1 > rows || c1 < 0 || c1 > cols || r2 < 0 || r2 > rows || c2 < 0 || c2 > cols) {
                return;
            }
            // 先转成一维下标
            int index1 = index(r1, c1);
            int index2 = index(r2, c2);

            int head1 = findHead(index1);
            int head2 = findHead(index2);
            if (head1 == head2) {
                return;
            }
            int bigSet = size[index1] >= size[index2] ? index1 : index2;
            int smallSet = bigSet == index1 ? index2 : index1;
            parent[smallSet] = bigSet;
            size[bigSet] += size[smallSet];
            sets--;
        }

        /**
         * 动态初始化，每次加一个1就在并查集里加一个集合，并且尝试合并这个1的上下左右
         */
        public int connect(int r, int c) {
            int index = index(r, c);
            if (size[index] == 0) {
                // 只有size==1才添加，size>0说明这个位置之前已经加过了
                parent[index] = index;
                size[index] = 1;
                sets++;

                union(r, c, r - 1, c);
                union(r, c, r + 1, c);
                union(r, c, r, c - 1);
                union(r, c, r, c + 1);
            }
            return sets;
        }
    }

}
