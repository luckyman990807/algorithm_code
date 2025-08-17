package shuatiban;

/**
 * https://leetcode.com/problems/shortest-bridge/
 * 给你一个大小为 n x n 的二元矩阵 grid ，其中 1 表示陆地，0 表示水域。
 * 岛 是由四面（上下左右）相连的 1 形成的一个最大组，即不会与非组内的任何其他 1 相连。grid 中 恰好存在两座岛 。
 * 你可以将任意数量的 0 变为 1 ，以使两座岛连接起来，变成 一座岛 。
 * 返回必须翻转的 0 的最小数目。
 *
 * 思路：等价于求两个岛之间的最短距离。如何求最短距离？从那些0入手，计算每个0到2个岛的距离和，找出最小的距离和，距离和-1就是需要翻转的最小数目。
 * 例如，1 0 a 0 0 1，a位置（值是0）离左边岛的距离是2，离右边岛的距离是3，假设这是最小的距离和，那么需要翻转的最小数目为2+3-1=4
 * 那么如何求每个0到两个到的距离和？用两个辅助数组，一个数组记录每个位置到第一个岛的距离，另一个数组记录每个位置到第二个岛的距离。
 * 如何用辅助数组记录每个位置到岛的距离？宽度优先遍历，从某个岛的每个1出发，往上下左右都走一步，如果新到的位置是0，就改为2，然后从每个2（本次新扩展的位置）出发，往上下左右都走一步，如果新到的位置是0，就改成3……
 * 这样得到的辅助数组，每个位置的值-1就是该位置到岛的距离
 * 为什么要从1开始计数，用的时候再-1？为了好实现，题目中的岛就是用1表示的，那么往外扩一圈就是2，再扩一圈就是3……也可以从0开始计数，这样就不用-1了，但是要先把水域的0改成-1，要不然就分不出水和岛了
 *
 *
 *
 * PS：用宽度优先遍历求到岛的距离的时候，要用到队列，为了减少常数时间，可以用数组代替LinkedList+自定义Class
 */
public class Code92连接两座岛的最短的桥 {
    public int shortestBridge(int[][] grid) {
        int all = grid.length * grid[0].length;
        // 用数组实现队列，做宽度优先遍历，从岛开始一层一层往外扩，记录每一层离岛的距离。queue记录一层的位置，distance记录每个位置到岛的距离
        // queues[0]是当前层的队列，queues[1]是从当前层往外扩的下一层的队列，从下一层再外扩的下一层再回到queues[0]，两个队列交替使用实现一层一层扩，节省空间
        int[][] queues = new int[2][all];
        // distances[0]是每个位置到第一个岛的距离，distances[1]是每个位置到第二个岛的距离。距离的初始值为-1，岛到自己的距离是0
        int[][] distances = new int[2][all];
        for (int i = 0; i < distances.length; i++) {
            for (int j = 0; j < distances[i].length; j++) {
                distances[i][j] = -1;
            }
        }

        // 第几个岛
        int island = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                // 遍历网格，如果碰到岛，就调用inject感染函数把整个岛都捞出来。因此，有两个岛的话这个if只会进来两次
                if (grid[i][j] == 1) {
                    // 完整地捞出一个岛，把岛自身作为外扩的起点（第0层）记录在queues[0]里，把岛到自身的距离0记录在distance里，返回第0层的大小（岛的大小）
                    int queueSize = inject(grid, i, j, queues[0], 0, distances[island]);
                    // 下一层的层数
                    int level = 1;
                    // while+队列，宽度优先遍历
                    while (queueSize > 0) {
                        // 从当前层出发，外扩出下一层，记录在queues[1]中，返回下一层的大小，记录在queueSize
                        queueSize = bfs(queues[0], queueSize, distances[island], queues[1], level++, grid.length, grid[0].length);
                        // 两个队列交替使用即可，不需要每一层都建一个队列
                        int[] temp = queues[0];
                        queues[0] = queues[1];
                        queues[1] = temp;
                    }
                    island++;
                }
            }
        }
        // 找出距离两个岛的距离和的最小值-1就是两岛之间的最短距离
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < all; i++) {
            min = Math.min(min, distances[0][i] + distances[1][i]);
        }
        return min - 1;
    }

    /**
     * 感染函数，递归捞出整片岛
     * @param grid 记录岛和水的网格
     * @param row 当前行号
     * @param col 当前列号
     * @param queue 记录一片岛的位置
     * @param queueSize 岛的大小，每次递归都不断更新
     * @param distance 记录每个位置到岛的距离，这里只更新岛到自身的距离
     * @return 返回岛的大小
     */
    public int inject(int[][] grid, int row, int col, int[] queue, int queueSize, int[] distance) {
        // 递归出口，如果越界了，或者!=1，说明走到水里了，直接返回，不再继续递归
        if (row >= grid.length || row < 0 || col >= grid[0].length || col < 0 || grid[row][col] != 1) {
            return queueSize;
        }
        // 把当前遍历过的位置标记为2，避免重复遍历（因为每个位置都往上下左右遍历，可能涉及重复遍历）
        grid[row][col] = 2;
        // 二维网格下标转成一维数组的下标
        int index = row * grid[0].length + col;
        // 记录岛到自身的距离
        distance[index] = 0;
        // 记录岛的位置
        queue[queueSize++] = index;
        // 往上下左右分别递归，遍历这片岛的每个位置
        queueSize = inject(grid, row - 1, col, queue, queueSize, distance);
        queueSize = inject(grid, row + 1, col, queue, queueSize, distance);
        queueSize = inject(grid, row, col - 1, queue, queueSize, distance);
        queueSize = inject(grid, row, col + 1, queue, queueSize, distance);
        // 返回遍历完整座岛之后得到的岛的大小
        return queueSize;
    }

    /**
     * 宽度优先遍历的一层操作
     * @param queue 当前层
     * @param queueSize 当前层的大小
     * @param distance 每个位置到岛的距离
     * @param nextQueue 下一层
     * @param level 下一层的层数
     * @param rows 网格的行数
     * @param cols 网格的列数
     * @return 返回下一层的大小
     */
    public int bfs(int[] queue, int queueSize, int[] distance, int[] nextQueue, int level, int rows, int cols) {
        int nextQueueSize = 0;
        // 从队列中拿出当前层的每个位置，往上下左右各走一步，如果下一步是未涉足过的（distance=-1），那么就属于下一层，更新下一层的队列和距离
        for (int i = 0; i < queueSize; i++) {
            int up = queue[i] - cols >= 0 ? queue[i] - cols : -1;
            if (up != -1 && distance[up] == -1) {
                distance[up] = level;
                nextQueue[nextQueueSize++] = up;
            }
            int down = queue[i] + cols < rows * cols ? queue[i] + cols : -1;
            if (down != -1 && distance[down] == -1) {
                distance[down] = level;
                nextQueue[nextQueueSize++] = down;
            }
            int left = queue[i] % cols != 0 ? queue[i] - 1 : -1;
            if (left != -1 && distance[left] == -1) {
                distance[left] = level;
                nextQueue[nextQueueSize++] = left;
            }
            int right = queue[i] % cols != cols - 1 ? queue[i] + 1 : -1;
            if (right != -1 && distance[right] == -1) {
                distance[right] = level;
                nextQueue[nextQueueSize++] = right;
            }
        }
        return nextQueueSize;
    }
}
