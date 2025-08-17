package tixiban.class34最大网络流;

/**
 * 给定一个「有向」图,必须要指定一个「源点」和一个「目标点」,必须指定每条边的「承载量」,
 * 问 从源点灌水,最多有多大的流量到达目标点.
 *
 * 注意:无向图可以理解为有两条有向边的有向图.
 *
 * 例如下面的图,从A灌水,最多有130的流量到D.ABD最多100,ACD最多30.
 * A —— 100 —— B
 * |           |
 * 50         200
 * |           |
 * C —— 30 —— D
 *
 */

import java.util.*;

/**
 * Dinic算法所做的优化:
 *
 * 1.补反向边.
 * 例如一条边(1,3,20)从1到3承载量为20,给他补一条反向边(3,1,0)从3到1承载量为0. 如果用掉了5的承载量,这条边变成(1,3,15),反向边变成(3,1,5).
 * 补反向边是为了避免在有环的时候,没有走最优路线导致下一条路走不通了.具体例子看截图.
 *
 * 2.抽象出每个节点的高度.
 * 在宽度优先遍历时,源节点永远是0层,源节点的next节点是1层,next的next是2层...然后在深度优先遍历时只往下一层走,不往同一层走,可以避免转圈,优化常数时间.
 *
 * 3.记录当前可用的边
 * 用一个数组记录每个节点当前应该从那条边走, 前面的边的承载量已经用完了,就不用再去试了.
 *
 */
public class Code01最大网络流 {
    /**
     * 边
     */
    public static class Edge {
        // 从哪个节点
        public int from;
        // 到哪个节点
        public int to;
        // 当前可用的承载量
        public int available;

        public Edge(int from, int to, int available) {
            this.from = from;
            this.to = to;
            this.available = available;
        }
    }

    public static class Dinic {
        // 节点数
        public int n;
        // 图中所有的边的集合
        public List<Edge> edges;
        // 所有节点的可达边,nexts[0][1]=2 表示0号节点的下标为1的可达的边是2号边.再把2作为下边取edges里取到具体的边,边里记录着下一个节点
        public List<List<Integer>> nexts;
        // 高度数组,depth[0]=1 表示0号节点当前在第1层
        public int[] depth;
        // 当前可用边数组,cur[0]=2 表示0号节点当前直接从下标为2的边开始试
        public int[] cur;

        public Dinic(int nodes) {
            // 节点数+1,这样即使节点数从1开始算也可以支持
            n = nodes + 1;
            edges = new ArrayList<>();
            nexts = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                nexts.add(new ArrayList<>());
            }
            depth = new int[n];
            cur = new int[n];
        }

        public void addEdge(int from, int to, int available) {
            // 先加一条正常边
            edges.add(new Edge(from, to, available));
            nexts.get(from).add(edges.size() - 1);
            // 再加一条反向边
            edges.add(new Edge(to, from, 0));
            nexts.get(to).add(edges.size() - 1);
        }

        /**
         * 从源节点到目标节点的最大流量
         */
        public int maxStream(int source, int target) {
            int max = 0;
            // bfs宽度优先遍历用来检查是否有从source通往target的路,如果有,再dfs深度优先遍历走一条路并计算这条路的流量.
            // 为什么要循环,一次深搜不就能走遍所有路径吗?
            // 并不是,因为深搜的过程要减掉边的可用承载量,可能一次深搜还没把所有路径都走完,有的边就没有承载量了.
            // 但是得益于反向边,重新来一次深搜还能把剩余的路径走完.
            while (bfs(source, target)) {
                // 每次深度优先遍历都要重新记录当前每个节点该走条边了,因为得益于反向边下次可以后悔,不受上次减掉承载量的影响
                Arrays.fill(cur, 0);
                // bfs发现有通路,顺便还计算了depth数组,然后就bfs走几条路累加一下可达的流量
                max += dfs(source, target, Integer.MAX_VALUE);
                // 高度数组是每次宽度优先遍历生成的,用完清零
                Arrays.fill(depth, 0);
            }
            return max;
        }

        /**
         * 宽度优先遍历
         * 搜寻是否有从source节点到target节点的路,同时建立depth数组(想像成一棵树的宽度优先遍历,每个节点在第几层)
         */
        private boolean bfs(int source, int target) {
            // 宽度优先遍历标配队列
            LinkedList<Integer> nodeQueue = new LinkedList<>();
            nodeQueue.push(source);

            // 记录哪些节点遍历过了
            boolean[] visited = new boolean[n];
            visited[source] = true;

            // 记录节点的高度层数
            depth[source] = 0;

            while (!nodeQueue.isEmpty()) {
                Integer curNode = nodeQueue.pollFirst();
                for (int i = 0; i < nexts.get(curNode).size(); i++) {
                    // 从队列里弹出一个节点,遍历所有的边拿到next节点,依次加入队列
                    Edge curEdge = edges.get(nexts.get(curNode).get(i));
                    if (!visited[curEdge.to] && curEdge.available > 0) {
                        visited[curEdge.to] = true;
                        depth[curEdge.to] = depth[curNode] + 1;
                        // 如果找到目标点了,就退出.这里不管是退出for循环,还是直接return true,还是不做任何处理,都不影响结果
                        // 只会影响depth的补全程度,depth越完整,是不是一次深度遍历的路径会更多?但是提前退出一下会快一丢丢
                        if (curEdge.to == target) {
                            break;
                        }
                        nodeQueue.push(curEdge.to);
                    }
                }
            }
            return visited[target];
        }

        /**
         * 深度优先遍历
         * 从源节点source,带着上游upstream这么多的流量,到目标点target,返回最终能有多少流量到达target
         */
        private int dfs(int source, int target, int upstream) {
            // 递归出口,如果当前已经在目标点了,或者当前节点收到的流量已经是0了,直接返回当前收到的流量
            if (source == target || upstream == 0) {
                return upstream;
            }

            // 记录本次深度优先遍历的结果,即到达目标点的总流量
            int sum = 0;
            // 记录到达一次目标点所带过来的流量
            int targetStream;
            // 遍历源节点的每一个next节点,以next节点作为新的源节点,递归
            for (; cur[source] < nexts.get(source).size(); cur[source]++) {
                // 源节点当前遍历到的一条边的下标
                Integer curEdgeIndex = nexts.get(source).get(cur[source]);
                // 源节点当前遍历到的边
                Edge curEdge = edges.get(curEdgeIndex);
                // 对应的反向边.加边的时候永远是加一个正常边再加一个反向边,两条互相反向的边永远挨着,^1就能得到挨着的数,2^1=3,3^1=2
                Edge reverseEdge = edges.get(curEdgeIndex ^ 1);
                // 源节点当前遍历到的边的下一节点
                int nextNode = curEdge.to;
                // 如果下一节点在源节点的下一层,并且下一节点递归能打到目标点的流量>0,说明找到了一条路
                if (depth[nextNode] == depth[source] + 1 && (targetStream = dfs(nextNode, target, Math.min(upstream, curEdge.available))) > 0) {
                    // 这条路已经走过了,承载量要减去最终能到达目标点的流量
                    curEdge.available -= targetStream;
                    // 反向边要加
                    reverseEdge.available += targetStream;
                    // 算到总流量里
                    sum += targetStream;
                    // 上游带来的流量用掉这么多打到目标点了
                    upstream -= targetStream;
                    // 如果上游带来的流量已经用完了,就不用再试了
                    if (upstream <= 0) {
                        break;
                    }
                }
            }
            return sum;
        }
    }

    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        // 要测试几次
        int cases = cin.nextInt();
        for (int i = 1; i <= cases; i++) {
            // 节点数
            int n = cin.nextInt();
            // 源节点是几号
            int s = cin.nextInt();
            // 目标节点是几号
            int t = cin.nextInt();
            // 有几条边
            int m = cin.nextInt();
            Dinic dinic = new Dinic(n);
            // 每条边是什么
            for (int j = 0; j < m; j++) {
                int from = cin.nextInt();
                int to = cin.nextInt();
                int weight = cin.nextInt();
                dinic.addEdge(from, to, weight);
                dinic.addEdge(to, from, weight);
            }
            // 计算结果
            int ans = dinic.maxStream(s, t);
            System.out.println("Case " + i + ": " + ans);
        }
        cin.close();
    }
}
