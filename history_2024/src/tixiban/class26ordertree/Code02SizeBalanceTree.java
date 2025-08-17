package tixiban.class26ordertree;

public class Code02SizeBalanceTree {
    public static class Node<K extends Comparable<K>, V> {
        public K key;
        public V value;
        public Node<K, V> left;
        public Node<K, V> right;
        public int size;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.size = 1;
        }
    }

    public static class SizeBalanceTree<K extends Comparable<K>, V> {
        private Node<K, V> root;

        /**
         * 以head为头右旋,返回新的头节点
         */
        private Node<K, V> rightRotate(Node<K, V> head) {
            // 头节点的左孩子成为新的头,头节点接管左孩子的右孩子作为自己新的左孩子
            Node<K, V> left = head.left;
            head.left = left.right;
            left.right = head;

            // 左孩子成为头,继承旧头的size
            left.size = head.size;
            // 旧头的size重新算
            head.size = (head.left != null ? head.left.size : 0) + (head.right != null ? head.right.size : 0) + 1;
            // 返回新的头节点
            return left;
        }

        /**
         * 以head为头左旋,返回新的头节点
         */
        private Node<K, V> leftRotate(Node<K, V> head) {
            // 头节点的右孩子成为新的头,头节点接管右孩子的左孩子作为袭击新的右孩子
            Node<K, V> right = head.right;
            head.right = right.left;
            right.left = head;

            // 右孩子继承头节点的size
            right.size = head.size;
            // 头节点右旋后size重新算
            head.size = (head.left != null ? head.left.size : 0) + (head.right != null ? head.right.size : 0) + 1;
            // 返回新的头节点
            return right;
        }

        /**
         * 以head为头,调平衡,返回新的头节点
         */
        private Node<K, V> maintain(Node<K, V> head) {
            if (head == null) {
                return null;
            }
            int leftSize = head.left != null ? head.left.size : 0;
            int rightSize = head.right != null ? head.right.size : 0;
            int leftLeftSize = head.left != null && head.left.left != null ? head.left.left.size : 0;
            int leftRightSize = head.left != null && head.left.right != null ? head.left.right.size : 0;
            int rightRightSize = head.right != null && head.right.right != null ? head.right.right.size : 0;
            int rightLeftSize = head.right != null && head.right.left != null ? head.right.left.size : 0;

            if (leftLeftSize > rightSize) {
                // 头节点左孩子的左孩子,节点数大于头节点右孩子
                // 左旋
                head = leftRotate(head);
                // 孩子改变的节点递归调平衡.先调孩子再调头,因为调完头之后,孩子就是新孩子了,避免旧孩子被漏掉
                head.right = maintain(head.right);
                head = maintain(head);
            } else if (leftRightSize > rightSize) {
                // 头节点左孩子的右孩子,节点数大于头节点右孩子
                //先左旋再右旋
                head.left = leftRotate(head.left);
                head = rightRotate(head);
                // 孩子发生变化的节点,递归调平衡
                head.left = maintain(head.left);
                head.right = maintain(head.right);
                head = maintain(head);
            } else if (rightRightSize > leftSize) {
                // 头节点右孩子的右孩子,节点数大于头节点左孩子
                // 左旋
                head = leftRotate(head);
                // 孩子发生变化的节点,递归调平衡
                head.left = maintain(head.left);
                head = maintain(head);
            } else if (rightLeftSize > leftLeftSize) {
                // 头节点右孩子的左孩子,节点数大于头节点左孩子
                // 先右旋再左旋
                head.right = rightRotate(head.right);
                head = leftRotate(head);
                // 孩子发生变化的节点,递归调平衡
                head.left = maintain(head.left);
                head.right = maintain(head.right);
                head = maintain(head);
            }
            // 返回新的头节点
            return head;
        }

        /**
         * 从SB树上查询距离key最接近的一个节点
         */
        private Node<K, V> findMostClosed(K key) {
            if (key == null) {
                return null;
            }
            Node<K, V> pre = root;
            Node<K, V> cur = root;
            while (cur != null) {
                pre = cur;
                if (key.compareTo(cur.key) == 0) {
                    break;
                }
                if (key.compareTo(cur.key) < 0) {
                    cur = cur.left;
                } else {
                    cur = cur.right;
                }
            }
            return pre;
        }

        /**
         * 向SB树插入一个节点,返回新的头节点.
         */
        private Node<K, V> add(Node<K, V> head, K key, V value) {
            if (head == null) {
                return new Node<>(key, value);
            }
            head.size++;
            // 因为寻找插入位置时沿途经过的每个节点都要调平衡,所以只能用递归,不能像findMostClosed那样while循环找到位置之后插入
            if (key.compareTo(head.key) < 0) {
                head.left = add(head.left, key, value);
            } else {
                head.right = add(head.right, key, value);
            }
            return maintain(head);
        }

        /**
         * 从SB树上删除一个节点
         */
        private Node<K, V> delete(Node<K, V> head, K key) {
            if (head == null) {
                return null;
            }
            head.size--;
            if (key.compareTo(head.key) < 0) {
                // 注意一定要接收返回值,虽然delete不调平衡但是有可能子树的头节点被删了,也就是head的孩子被删了,因此要接收新的孩子
                head.left = delete(head.left, key);
            } else if (key.compareTo(head.key) > 0) {
                head.right = delete(head.right, key);
            } else {
                if (head.left == null && head.right == null) {
                    head = null;
                } else if (head.left != null && head.right == null) {
                    head = head.left;
                } else if (head.left == null && head.right != null) {
                    head = head.right;
                } else {
                    Node<K, V> rightLeft = head.right;
                    while (rightLeft.left != null) {
                        rightLeft = rightLeft.left;
                    }

                    delete(head.right, rightLeft.key);

                    rightLeft.left = head.left;
                    rightLeft.right = head.right;
                    head = rightLeft;
                }
            }
            return head;
        }

        // 提供给用户的方法

        /**
         * 插入或修改一个节点
         */
        public void put(K key, V value) {
            Node<K, V> mostClosed = findMostClosed(key);
            if (mostClosed != null && mostClosed.key.compareTo(key) == 0) {
                mostClosed.value = value;
            } else {
                root = add(root, key, value);
            }
        }

        /**
         * 删除一个节点
         */
        public void remove(K key) {
            if (contains(key)) {
                root = delete(root, key);
            }
        }

        /**
         * 判断是否包含这个节点
         */
        public boolean contains(K key) {
            Node<K, V> mostClosed = findMostClosed(key);
            return mostClosed != null && mostClosed.key.compareTo(key) == 0;
        }

        /**
         * 查询一个节点
         */
        public Node<K, V> get(K key) {
            Node<K, V> mostClosed = findMostClosed(key);
            return mostClosed != null && mostClosed.key.compareTo(key) == 0 ? mostClosed : null;
        }


        // 用于测试打印
        public void print() {
            process(root);
            System.out.println();
        }
        private void process(Node<K, V> head) {
            if (head == null) {
                return;
            }
            process(head.left);
            System.out.print(head.value + " ");
            process(head.right);
        }

    }

    public static void main(String[] args) {
        SizeBalanceTree<Integer, String> sbt = new SizeBalanceTree<>();

        sbt.put(6, "6");
        sbt.put(1, "1");
        sbt.put(4, "4");
        sbt.put(9, "9");
        sbt.put(3, "3");
        sbt.put(7, "7");
        sbt.put(1, "11");
        sbt.print();

        sbt.remove(1);
        sbt.print();

        System.out.println(sbt.get(6).value);

    }
}
