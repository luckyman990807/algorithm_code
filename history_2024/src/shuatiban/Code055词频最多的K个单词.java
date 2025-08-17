package shuatiban;

import java.util.*;

/**
 * https://www.lintcode.com/problem/550/
 *
 * 在实时数据流中找到最常使用的k个单词.
 * 实现TopK类中的三个方法:
 * TopK(k), 构造方法
 * add(word), 增加一个新单词
 * topk(), 得到当前最常使用的k个单词.
 *
 * 思路：加强堆，小根堆
 * 为什么用小根堆，统计最常用的单词不应该用大根堆吗？
 * 小根堆维护的是词频最大的K个单词，堆顶是K个当中词频最小的。这样的好处是1、方便判断进堆门槛，直接跟堆顶元素比较即可。2、方便进堆，直接替代堆顶的位置即可
 */
public class Code055词频最多的K个单词 {
    public static class TopK {
        // 堆
        private Heap heap;
        // 记录某个单词有没有出现过，即单词-节点映射表
        private Map<String, Node> stringNodeMap;

        // 构造函数
        public TopK(int k) {
            // 小根堆，出现次最少的作为堆顶，出现次数一样则字典序大的作为堆顶（依据题目要求）
            heap = new Heap(k, ((o1, o2) -> o1.times != o2.times ? o1.times - o2.times : o2.str.compareTo(o1.str)));
            stringNodeMap = new HashMap<>();
        }

        // 添加单词
        public void add(String word) {
            // 题目要求，如果是top0，要返回空[]
            if (heap.heap.length == 0) {
                return;
            }
            // 首先获取到对应的节点和下标
            Node node = null;
            int index = -1;
            if (!stringNodeMap.containsKey(word)) {
                node = new Node(word, 1);
                stringNodeMap.put(word, node);
            } else {
                node = stringNodeMap.get(word);
                node.times++;
                if (heap.contains(node)) {
                    index = heap.nodeIndexMap.get(node);
                }
            }

            // 其次判断要不要入堆、调整堆
            if (index == -1) {
                // 没在堆上
                if (heap.full()) {
                    // 堆满了，只有达到门槛才能进，即比堆中最小的（堆顶）元素大，才能进
                    if (heap.comparator.compare(node, heap.peek()) > 0) {
                        heap.replaceTop(node);
                    }
                } else {
                    // 堆没满，直接add
                    heap.add(node);
                }
            } else {
                // 已经在堆上
                // 值变大了，在小根堆上应该往下沉
                heap.heapify(index);
            }
        }

        // 输出top k
        public List<String> topk() {
            // 输出结果要按照出现次数从大到小排序，如果次数一样就按字典序从小到大排序（依据题目要求）
            TreeSet<Node> treeSet = new TreeSet<>(((o1, o2) -> o1.times != o2.times ? o2.times - o1.times : o1.str.compareTo(o2.str)));
            for (int i = 0; i < heap.heapSize; i++) {
                treeSet.add(heap.heap[i]);
            }
            List<String> result = new ArrayList<>();
            for (Node node : treeSet) {
                result.add(node.str);
            }
            return result;
        }

        // 节点
        public class Node {
            public String str;
            public int times;

            public Node(String str, int times) {
                this.str = str;
                this.times = times;
            }
        }

        // 堆
        public class Heap {
            // 基本堆结构，数组
            private Node[] heap;
            // 堆大小
            private int heapSize;
            // 比较器
            private Comparator<Node> comparator;
            // 加强堆，node-数组下标的反向索引表
            private Map<Node, Integer> nodeIndexMap;

            // 构造方法
            public Heap(Integer maxSize, Comparator<Node> comparator) {
                this.heap = new Node[maxSize];
                this.comparator = comparator;
                this.heapSize = 0;
                this.nodeIndexMap = new HashMap<>();
            }

            // 返回堆的大小
            public int size() {
                return heapSize;
            }

            // 返回堆空了没
            public boolean empty() {
                return heapSize == 0;
            }

            // 返回堆满了没
            public boolean full() {
                return heapSize == heap.length;
            }

            // 判断一个元素是不是在堆上
            public boolean contains(Node node) {
                Integer index = nodeIndexMap.get(node);
                return index != null && index != -1;
            }

            // 返回堆顶元素
            public Node peek() {
                return heap[0];
            }

            // 往堆上添加元素
            public void add(Node node) {
                heap[heapSize] = node;
                nodeIndexMap.put(node, heapSize);
                heapInsert(heapSize++);
            }

            // 把堆顶节点换成一个新节点
            public void replaceTop(Node node) {
                Node pre = heap[0];
                heap[0] = node;
                nodeIndexMap.put(pre, -1);
                nodeIndexMap.put(node, 0);
                // 尝试往下移动
                heapify(0);
            }

            // 堆上一个元素往下沉，找到正确的位置
            private void heapify(int cur) {
                // 计算左孩子和右孩子的下标
                int left = (cur << 1) + 1;
                int right = (cur << 1) + 2;
                // 只要有左孩子就能比较
                while (left < heapSize) {
                    // 选出左右孩子中更小（或更大，取决于小根堆还是大根堆）的那个：如果有右孩子，并且右孩子比左孩子小，就选右孩子，否则就选左孩子
                    int better = right < heapSize && comparator.compare(heap[right], heap[left]) < 0 ? right : left;
                    if (comparator.compare(heap[cur], heap[better]) < 0) {
                        // 如果父亲已经比最小的孩子还小了，就不用继续调整了
                        return;
                    } else {
                        // 如果父亲确实不如最小的孩子小，就交换，父亲下沉
                        swap(cur, better);
                    }
                    cur = better;
                    left = (cur << 1) + 1;
                    right = (cur << 1) + 2;
                }
            }

            // 堆上一个元素往上浮，找到正确的位置
            private void heapInsert(int cur) {
                // 只要比自己的父亲小（或者大，取决于是小根堆还是大根堆）就跟父亲交换
                int father = (cur - 1) / 2;
                while (comparator.compare(heap[cur], heap[father]) < 0) {
                    swap(cur, father);
                    cur = father;
                    father = (cur - 1) / 2;
                }
            }

            // 交换堆上的两个元素
            private void swap(int cur, int father) {
                nodeIndexMap.put(heap[cur], father);
                nodeIndexMap.put(heap[father], cur);

                Node temp = heap[cur];
                heap[cur] = heap[father];
                heap[father] = temp;
            }
        }
    }

    public static void main(String[] args) {
        TopK topK = new TopK(3);
        topK.add("abc");
        topK.add("bcd");
        topK.add("cde");
        System.out.println(topK.topk());
        topK.add("def");

        topK.add("def");
        System.out.println(topK.topk());

    }
}
