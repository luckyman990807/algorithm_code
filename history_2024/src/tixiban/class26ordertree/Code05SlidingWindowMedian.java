package tixiban.class26ordertree;

import java.util.Arrays;

/**
 * 有一个滑动窗口,窗口大小为k,L是滑动窗口左边界,R是右边界,一开始LR都在数组左侧,
 * 一开始先生成k大小的窗口,然后窗口每次整体向右滑动一格.
 * 求从生成窗口开始到窗口滑到最右端,每一个窗口状态的中位数.
 */
public class Code05SlidingWindowMedian {
    public static class Node {
        public int key;
        public Node left;
        public Node right;
        public int size;
        public int all;

        public Node(int key) {
            this.key = key;
            this.size = 1;
            this.all = 1;
        }
    }

    public static class SizeBalanceTree {
        private Node root;

        public Node rightRotate(Node head) {
            if (head == null) {
                return null;
            }
            int headAll = head.all - (head.left != null ? head.left.all : 0) - (head.right != null ? head.right.all : 0);
            Node left = head.left;
            head.left = left.right;
            left.right = head;
            left.size = head.size;
            head.size = (head.left != null ? head.left.size : 0) + (head.right != null ? head.right.size : 0) + 1;
            left.all = head.all;
            head.all = (head.left != null ? head.left.all : 0) + (head.right != null ? head.right.all : 0) + headAll;
            return left;
        }

        public Node leftRotate(Node head) {
            if (head == null) {
                return null;
            }
            int headAll = head.all - (head.left != null ? head.left.all : 0) - (head.right != null ? head.right.all : 0);
            Node right = head.right;
            head.right = right.left;
            right.left = head;
            right.size = head.size;
            head.size = (head.left != null ? head.left.size : 0) + (head.right != null ? head.right.size : 0) + 1;
            right.all = head.all;
            head.all = (head.left != null ? head.left.all : 0) + (head.right != null ? head.right.all : 0) + headAll;
            return right;
        }

        public Node maintain(Node head) {
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
                head.left = maintain(head.left);
                head.right = maintain(head.right);
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

        private boolean contains(Node head, int key) {
            Node cur = head;
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

        private Node add(Node head, int key) {
            if (head == null) {
                return new Node(key);
            }
            head.all++;
            if (!contains(head, key)) {
                head.size++;
            }
            if (key == head.key) {
                return head;
            }
            if (key < head.key) {
                head.left = add(head.left, key);
            } else if (key > head.key) {
                head.right = add(head.right, key);
            }
            return maintain(head);
        }

        private Node delete(Node head, int key) {
            if (!contains(head, key)) {
                return head;
            }
            head.all--;
            if (key < head.key) {
                head.left = delete(head.left, key);
            } else if (key > head.key) {
                head.right = delete(head.right, key);
            } else {
                if (head.all - (head.left != null ? head.left.all : 0) - (head.right != null ? head.right.all : 0) == 0) {
                    head.size--;
                    if (head.left == null && head.right == null) {
                        head = null;
                    } else if (head.left != null && head.right == null) {
                        head = head.left;
                    } else if (head.left == null && head.right != null) {
                        head = head.right;
                    } else {
                        Node rightLeft = head.right;
                        while (rightLeft.left != null) {
                            rightLeft = rightLeft.left;
                        }
                        head.right = delete(head.right, rightLeft.key);
                        rightLeft.left = head.left;
                        rightLeft.right = head.right;
                        rightLeft.all = head.all;
                        rightLeft.size = head.size;
                        head = rightLeft;
                    }
                }
            }
            return head;
        }

        private Node getIndex(Node head, int index) {
            if (head.left != null && index < head.left.all) {
                return getIndex(head.left, index);
            }
            int headAndLeftAll = head.all - (head.right != null ? head.right.all : 0);
            if (index < headAndLeftAll) {
                return head;
            }
            return getIndex(head.right, index - headAndLeftAll);
        }

        public void add(int key) {
            root = add(root, key);
            System.out.print("add ");
            print();
        }

        public void delete(int key) {
            root = delete(root, key);
            System.out.print("delete ");
            print();
        }

        public int getIndex(int index) {
            if (root == null || index >= root.all) {
                throw new RuntimeException("out index!");
            }
            return getIndex(root, index).key;
        }

        public void print() {
            process(root);
            System.out.println();
        }

        private void process(Node head) {
            if (head == null) {
                return;
            }
            process(head.left);
            System.out.print(head.key + " ");
            process(head.right);
        }
    }

    public static double[] slidingWindowMedian(int[] arr, int k) {
        if (arr == null || arr.length < k) {
            return null;
        }
        double[] result = new double[arr.length - k + 1];
        SizeBalanceTree sbt = new SizeBalanceTree();
        for (int i = 0; i < k - 1; i++) {
            sbt.add(arr[i]);
        }
        int resultI = 0;
        for (int i = k - 1; i < arr.length; i++) {
            sbt.add(arr[i]);
            if (k % 2 == 1) {
                result[resultI++] = sbt.getIndex(k / 2);
                System.out.println("中位数 " + sbt.getIndex(k / 2));
            } else {
                result[resultI++] = ((double) (sbt.getIndex(k / 2 - 1) + sbt.getIndex(k / 2))) / 2;
                System.out.println("中位数 " + ((double) (sbt.getIndex(k / 2 - 1) + sbt.getIndex(k / 2))) / 2);
            }
            sbt.delete(arr[i - k + 1]);
        }
        return result;
    }

    public static void main(String[] args) {
        int[] arr = {1, 5, 4, 1, 2, 9, 8, 9, 7, 1};
        int k = 4;
        System.out.println(Arrays.toString(slidingWindowMedian(arr, k)));
    }


}
