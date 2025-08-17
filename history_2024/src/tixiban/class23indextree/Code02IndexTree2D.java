package tixiban.class23indextree;

/**
 * IndexTree二维扩展
 * 解决的问题:求二维数组1,1位置和i,j位置围成的矩形内的累加和,在支持单点更新的基础上,每次求累加和复杂度O(logN).
 *
 * 思路:
 * 在行数组上玩一维indexTree,在列数组上玩一维IndexTree,二者两层for循环交叉得到的数据就是要更新的数据
 *
 * 三维怎么扩展?
 * 再套一层for循环,在新维度上依然使用一维indexTree的规律
 */
public class Code02IndexTree2D {
    public static class IndexTree2D {
        private int row;
        private int col;
        private int[][] tree;

        public IndexTree2D(int[][] arr) {
            row = arr.length;
            col = arr[0].length;
            tree = new int[row + 1][col + 1];
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    add(i + 1, j + 1, arr[i][j]);
                }
            }
        }

        /**
         * 二维单点更新,把第taskR行taskC列加value
         */
        public void add(int taskR, int taskC, int value) {
            // 影响的位置是行和列分别indexTree规律的全部组合
            for (int i = taskR; i <= row; i += i & -i) {
                for (int j = taskC; j <= col; j += j & -j) {
                    tree[i][j] += value;
                }
            }
        }

        /**
         * 求taskR行taskC列到1行1列围成的矩形的累加和
         *
         * 如果求a,b到i,j的累加和怎么求?
         * 按照求矩形面积的方法
         * sum(a,b,i,j) = sum(1,1,i,j) - sum(1,1,a-1,j) - sum(1,1,i,b-1) + sum(1,1,a-1,b-1)
         * sum(1,1,a-1,b-1)多减了一遍所以加回来
         */
        public int sum(int taskR, int taskC) {
            int sum = 0;
            // 累加的位置是行和列分别按照indexTree累加和规律的全部组合
            for (int i = taskR; i > 0; i -= i & -i) {
                for (int j = taskC; j > 0; j -= j & -j) {
                    sum += tree[i][j];
                }
            }
            return sum;
        }
    }

    public static void main(String[] args) {
        int[][] arr = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 0, 1, 2}
        };
        IndexTree2D indexTree = new IndexTree2D(arr);
        System.out.println(indexTree.sum(2, 3));
        indexTree.add(3, 2, 10);
        System.out.println(indexTree.sum(3, 2));
    }
}
