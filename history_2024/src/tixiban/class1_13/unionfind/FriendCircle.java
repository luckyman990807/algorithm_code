package tixiban.class1_13.unionfind;

/**
 * 给定一个二维数组表示朋友圈，二维数组长和宽一定相等，arr[i][j]=1表示i和j认识，=0表示i和j不认识，规定每个人都认识自己，i认识j则j一定认识i。
 * 求这个二维数组里有几个朋友圈
 */
public class FriendCircle {

    public static int countFriendCircle(int[][] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        // 有几个人就初始化多大的并查集
        UnionFind unionFind = new UnionFind(arr.length);
        for (int i = 0; i < arr.length; i++) {
            // j=i+1，只遍历对角线以上的部分
            for (int j = i + 1; j < arr[i].length; j++) {
                if (arr[i][j] == 1) {
                    // 如果i和j认识，就合并
                    unionFind.union(i, j);
                }
            }
        }
        // 直接返回并查集的集合个数
        return unionFind.sets;
    }

    /**
     * 并查集
     */
    public static class UnionFind {
        // parent[i]=j，表示i的父亲是j。用下标寻址代替map
        private int[] parent;
        // size[i]=n，当i是集合的代表节点时才有意义，表示以i为代表的集合的大小为n
        private int[] size;
        // 辅助数组，找代表节点的时候用来记录路径
        private int[] path;
        // 记录一共有几个集合
        private int sets;

        public UnionFind(int n) {
            // 初始化，每个节点都自己是一个集合，每个节点的父亲都是自己
            parent = new int[n];
            size = new int[n];
            path = new int[n];
            sets = n;
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        /**
         * 给定一个节点，返回这个节点所在集合的代表节点，并顺便把集合压缩（扁平化，途径的节点都记录下来挂在代表节点上）
         */
        public int findHead(int i) {
            int pathI = 0;
            // 主体就是i=parent[i]循环
            while (parent[i] != i) {
                path[pathI++] = i;
                i = parent[i];
            }
            // 第一个pathI--是for结构初始化，pathI=pathI-1
            for (pathI--; pathI >= 0; pathI--) {
                // 压缩，途径的节点都挂到代表节点上
                parent[pathI] = i;
            }
            return i;
        }

        /**
         * 给定两个节点，把两节点所在的两个集合合并成一个大集合
         */
        public void union(int i, int j) {
            int iHead = findHead(i);
            int jHead = findHead(j);
            if (iHead == jHead) {
                // 如果二者属于同一个集合（即所在集合的代表节点是同一个），不需要合并，直接返回
                return;
            }
            // 两个代表节点中区分出较大集合和较小集合
            int bigSet = size[iHead] >= size[jHead] ? iHead : jHead;
            int smallSet = bigSet == iHead ? jHead : iHead;
            // 小集合挂到大集合上
            parent[smallSet] = bigSet;
            // 小集合的size归到大集合上
            size[bigSet] += size[smallSet];
            // 小集合没了，集合数-1
            sets--;
        }
    }
}
