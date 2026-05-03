package dynamic_programing.bitmask;

public class Code028TODOVisitAllNodes {
    public int force(int[][] graph) {
        int[][] cache = new int[1 << graph.length][graph.length];
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < graph.length; i++) {
            int status = 1 << i;
            min = Math.min(min, process(graph, cache, status, i));
        }
        return min;
    }

    private int process(int[][] graph, int[][] cache, int status, int cur) {
        if (status == (1 << graph.length) - 1) {
            return 0;
        }

        if (cache[status][cur] != 0) {
            return cache[status][cur];
        }

        int min = Integer.MAX_VALUE;
        for (int i : graph[cur]) {
            /**
             * todo：由于可以重复走，这里会死循环。如何避免？？
             * 
             * 或者应该先计算出任意两点之间的最短距离，再使用状态压缩的动态规划？？
             */
            ;
            int curPath = 1 + process(graph, cache, status | (1 << i), i);
            min = Math.min(min, curPath);
        }

        cache[status][cur] = min;
        return min;
    }

}
