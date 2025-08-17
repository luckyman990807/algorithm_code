package tixiban.class26ordertree;

/**
 * 给定一个数组,再给定一个范围,返回累加和落在这个范围上的子数组的个数(子数组,连续)
 *
 * 前情回顾:
 * arr所有子数组中累加和落在[a,b]的个数 = 以arr[0]结尾的子数组中累加和落在[a,b]的个数 + 以arr[1]结尾的子数组中累加和落在[a,b]的个数 + ... + 以arr[n-1]结尾的子数组中累加和落在[a,b]的个数
 * 如果arr[i]的前缀和=sum,那么 以arr[i]结尾的子数组中累加和落在[a,b]的个数 = arr[i]前面有几个数的前缀和落在[sum-b,sum-a]
 *
 * 思路:
 * 改写有序表,用于记录前缀和,具有以下几个功能:
 * 1.可以插入重复值(因为前缀和有可能重复)
 * 2.可以求小于key的个数(落在[a,b]的个数 = 小于b+1的个数 - 小于a的个数)
 */
public class Code04CountOfRangeSum {
    public static class Node {
        public long key;
        public Node left;
        public Node right;
        // sb树的平衡因子,只记录不同key的个数.只参与平衡,不参与业务.
        public int size;
        // 记录所有插入时经过本节点的节点个数,包括重复key.只参与业务,不参与平衡.
        public int all;

        public Node(long key) {
            this.key = key;
            this.size = 1;
            this.all = 1;
        }
    }

    public static class SizeBalanceTree {
        private Node root;

        /**
         * 右旋
         * 同时调整size和all
         */
        private Node rightRotate(Node head) {
            if (head == null) {
                return null;
            }

            // 先求出head节点出现的次数 = 所有经过head的节点数 - 小于head的节点数 - 大于head的节点数
            int headNum = head.all - (head.left != null ? head.left.all : 0) - (head.right != null ? head.right.all : 0);

            // 右旋
            Node left = head.left;
            head.left = left.right;
            left.right = head;

            // 孩子发生变化的两个节点更新size
            left.size = head.size;
            head.size = (head.left != null ? head.left.size : 0) + (head.right != null ? head.right.size : 0) + 1;

            // 孩子发生变化的两个节点更新all
            left.all = head.all;
            head.all = (head.left != null ? head.left.all : 0) + (head.right != null ? head.right.all : 0) + headNum;

            return left;
        }

        /**
         * 左旋
         * 同时调整size和all
         */
        private Node leftRotate(Node head) {
            if (head == null) {
                return null;
            }

            int headNum = head.all - (head.left != null ? head.left.all : 0) - (head.right != null ? head.right.all : 0);

            Node right = head.right;
            head.right = right.left;
            right.left = head;

            right.size = head.size;
            head.size = (head.left != null ? head.left.size : 0) + (head.right != null ? head.right.size : 0) + 1;

            right.all = head.all;
            head.all = (head.left != null ? head.left.all : 0) + (head.right != null ? head.right.size : 0) + headNum;

            return right;
        }

        /**
         * 调平衡,返回新的头节点
         */
        private Node maintain(Node head) {
            if (head == null) {
                return null;
            }
            int left = head.left != null ? head.left.size : 0;
            int right = head.right != null ? head.right.size : 0;
            int leftLeft = head.left != null && head.left.left != null ? head.left.left.size : 0;
            int leftRight = head.left != null && head.left.right != null ? head.left.right.size : 0;
            int rightRight = head.right != null && head.right.right != null ? head.right.right.size : 0;
            int rightLeft = head.right != null && head.right.left != null ? head.right.left.size : 0;

            if (leftLeft > right) {
                head = rightRotate(head);
                head.right = maintain(head.right);
                head = maintain(head);
            } else if (leftRight > right) {
                head.left = leftRotate(head.left);
                head = rightRotate(head);
                head.right = maintain(head.right);
                head.left = maintain(head.left);
                head = maintain(head);
            } else if (rightRight > left) {
                head = leftRotate(head);
                head.left = maintain(head.left);
                head = maintain(head);
            } else if (rightLeft > left) {
                head.right = rightRotate(head.right);
                head = leftRotate(head);
                head.left = maintain(head.left);
                head.right = maintain(head.right);
                head = maintain(head);
            }
            return head;
        }

        /**
         * 判断sb树上是否存在该节点
         */
        public boolean contains(long key) {
            Node cur = root;
            while (cur != null) {
                if (key < cur.key) {
                    cur = cur.left;
                } else if (key > cur.key) {
                    cur = cur.right;
                } else {
                    break;
                }
            }
            return cur != null;
        }

        /**
         * 向sb树上添加节点
         */
        private Node add(Node head, long key, boolean contains) {
            if (head == null) {
                return new Node(key);
            }

            // 只要有节点经过了head,all就++,但是只有非重复节点经过时,size才++
            head.all++;
            if (!contains) {
                head.size++;
            }

            // 如果是当前节点的重复值,直接all++后返回
            if (key == head.key) {
                return head;
            }
            // 否则就递归左右子树,并接收左右子树的新节点
            if (key < head.key) {
                head.left = add(head.left, key, contains);
            } else {
                head.right = add(head.right, key, contains);
            }
            // 调平衡,返回新的头节点
            return maintain(head);
        }

        public void add(long key) {
            root = add(root, key, contains(key));
        }

        /**
         * 计算sb树上小于key的数有几个
         */
        public int lessKeyNum(long key) {
            int num = 0;
            Node cur = root;
            while (cur != null) {
                if (key < cur.key) {
                    // 如果key小于当前节点,那么没法计算当前有多少小于key的数,直接往右滑
                    cur = cur.left;
                } else if (key > cur.key) {
                    // 如果key大于当前节点,那么当前节点和左子树都是小于key的
                    // 当前节点的个数+左子树的个数=所有经过当前节点的个数-所有经过右子树的个数
                    num += cur.all - (cur.right != null ? cur.right.all : 0);
                    cur = cur.right;
                } else {
                    // 如果key等于当前节点,那么左子树的个数都是小于key的
                    // 滑到相等就是到底了,返回
                    num += cur.left != null ? cur.left.all : 0;
                    break;
                }
            }
            return num;
        }
    }

    /**
     * 计算数组arr中,累加和落在[lower,upper]内的子数组的个数
     * 根据前面的推导,转换成求 sum0前面有几个前缀和落在[sum0-upper,sum0-lower] + sum1前面有几个前缀和落在[sum1-upper,sum1-lower] + ...
     */
    public static int countOfRangeSum(int[] arr, int lower, int upper) {
        // sb树,用来记录所有前缀和,初始应该有个前缀和=0
        SizeBalanceTree sbt = new SizeBalanceTree();
        sbt.add(0);

        // 记录当前数组元素的前缀和
        int sum = 0;
        // 返回值
        int count = 0;

        for (int i = 0; i < arr.length; i++) {
            // 计算当前数组元素的前缀和
            sum += arr[i];
            // 现在sb树里的前缀和肯定是当前sum之前的前缀和,求sum前面有几个前缀和落在[sum-upper,sum-lower]
            count += sbt.lessKeyNum(sum - lower + 1) - sbt.lessKeyNum(sum - upper);
            // 把当前前缀和也加到sb树
            sbt.add(sum);
        }
        return count;
    }

    public static void main(String[] args) {
        int[] arr = {5, 4, 5, 2, 5, 6, 3};
        System.out.println(countOfRangeSum(arr, 10, 20));
    }
}
