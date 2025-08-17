package tixiban.class1_13.unionfind;

/**
 * 岛问题。给定一个二维数组，值只有1和0，规定相邻的1（上下左右相邻）可以看作一个岛，问二维数组里有几个岛。
 *
 *  * 扩展：
 *  * 1、如果二维数组很大，但是1很少，那么初始化一个大二维数组就很浪费空间，该怎么节约空间？
 *  * 解法：用map实现并查集，用行号和列号拼接成的字符串表示一个节点（例如parentMap中key="5_3"，value="2_0"，表示5行3列节点的父亲是2行0列节点），用map的contains判断这个位置的节点是不是第一次添加。
 *  * 2、如果数据量很大，设计并行计算的方案
 *  * 解法：把大二维数组分成多个小数组，并行计算岛的个数，最后计算分割的边界，如果边界两边是相邻的1，并且属于不同的集合（递归法的话可以在一个节点中记录这个节点是被谁感染的，相当于并查集的代表节点），就合并。
 */
public class Islands {

    /**
     * 递归方法计算岛的个数，最优解
     */
    public static int countIslandsRecurrence(int[][] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int islands = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                // 从上到下，从左到右依次遍历二维数组
                if (arr[i][j] == 1) {
                    // 如果值是1，说明发现了新岛，岛数+1
                    islands++;
                    // 开始感染
                    infect(arr, i, j);
                }
            }
        }
        return islands;
    }

    /**
     * 感染，把相连的全部1都变成2
     */
    public static void infect(int[][] arr, int i, int j) {
        // 如果i或j超出二维数组边界，直接返回
        if (i < 0 || i >= arr.length || j < 0 || j >= arr[i].length) {
            return;
        }
        // 如果当前位置不是1，不需要感染，直接返回
        if (arr[i][j] != 1) {
            return;
        }
        // 把当前位置变成2。变2是为了标记这个位置已经感染过了，下次作为别人的上/下/左/右被遍历时，!=1直接返回，要不然相邻位置会死循环遍历
        arr[i][j] = 2;
        // 分别感染上下左右相邻的位置
        infect(arr, i - 1, j);
        infect(arr, i + 1, j);
        infect(arr, i, j - 1);
        infect(arr, i, j + 1);
    }


    /**
     * 并查集方法，复杂度也是最优解，但是常数时间不如递归
     * 遍历二维数组，只看左边和上边，如果左边或上边是1，自己也是1，就合并。因为每个元素都遍历，只看左上就能保证每个相邻的都能合并一次
     */
    public static int countIslandsUnionFind(int[][] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int rows = arr.length;
        int cols = arr[0].length;
        UnionFind unionFind = new UnionFind(arr);

        // arr[0][0]不需要处理，因为他会作为别人的左或上被合并
        // 二维数组第一行，只合并左，因为没有上
        for (int i = 1; i < cols; i++) {
            // 如果当前位置是1，并且左边也是1，就合并
            if (arr[0][i] == 1) {
                if (arr[0][i - 1] == 1) {
                    unionFind.union(0, i, 0, i - 1);
                }
            }
        }

        // 二维数组第一列，只合并上，因为没有左
        for (int i = 1; i < rows; i++) {
            if (arr[i][0] == 1) {
                if (arr[i - 1][0] == 1) {
                    unionFind.union(i, 0, i - 1, 0);
                }
            }
        }

        // 其他位置，合并左和上
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                // 如果当前位置是1，并且左边也是1，就合并，上边也是1，也合并
                if (arr[i][j] == 1) {
                    if (arr[i - 1][j] == 1) {
                        unionFind.union(i, j, i - 1, j);
                    }
                    if (arr[i][j - 1] == 1) {
                        unionFind.union(i, j, i, j - 1);
                    }
                }
            }
        }

        return unionFind.sets();
    }

    public static class UnionFind {
        private int[] parent;
        private int[] size;
        private int[] path;
        private int sets;
        private int cols;

        public UnionFind(int[][] arr) {
            // 初始化并查集，把二位数组转成一维数组表示
            cols = arr[0].length;
            parent = new int[arr.length * cols];
            size = new int[arr.length * cols];
            path = new int[arr.length * cols];
            sets = 0;
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < cols; j++) {
                    if (arr[i][j] == 1) {
                        // 只把1初始化
                        int index = index(i, j);
                        parent[index] = index;
                        size[index] = 1;
                        sets++;
                    }
                }
            }
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
        public void union(int x1, int y1, int x2, int y2) {
            // 先转成一维下标
            int index1 = index(x1, y1);
            int index2 = index(x2, y2);

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
    }

}
