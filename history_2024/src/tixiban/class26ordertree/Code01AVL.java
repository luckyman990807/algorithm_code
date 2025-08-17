package tixiban.class26ordertree;

/**
 * AVL树实现
 */
public class Code01AVL {

    public static class Node<K extends Comparable<K>, V> {
        public K key;
        public V value;
        public Node<K, V> left;
        public Node<K, V> right;
        // 平衡因子,当前子树的高度.是当前节点到叶子节点的距离,不是根节点到当前节点的距离.
        public int height;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    public static class AVLTreeMap<K extends Comparable<K>, V> {
        private Node<K, V> root;
        private int size;


        /**
         * head节点左旋
         * head节点往左边倒,右孩子的左子树成为head的右子树,head成为右孩子的左子树.
         *
         * 左旋前:
         *   h
         *  / \
         * l   r
         *    / \
         *   a   b
         *
         * 左旋后:
         *     r
         *    / \
         *   h   b
         *  / \
         * l   a
         *
         */
        private Node<K, V> leftRotate(Node<K, V> head) {
            if (head == null) {
                return null;
            }
            Node<K, V> right = head.right;
            head.right = right.left;
            right.left = head;
            // 只有head和right的子树发生了变化,因此他俩更新高度
            head.height = Math.max(head.left != null ? head.left.height : 0, head.right != null ? head.right.height : 0) + 1;
            right.height = Math.max(right.left != null ? right.left.height : 0, right.right != null ? right.right.height : 0) + 1;
            return right;
        }

        /**
         * head节点右旋
         * head节点往右边倒,左孩子的右子树成为head的左孩子,head成为左孩子的右子树.
         *
         * 右旋前:
         *     h
         *    / \
         *   l   r
         *  / \
         * a   b
         *
         * 右旋后:
         *   l
         *  / \
         * a   h
         *    / \
         *   b   r
         *
         */
        private Node<K, V> rightRotate(Node<K, V> head) {
            if (head == null) {
                return null;
            }
            Node<K, V> left = head.left;
            head.left = left.right;
            left.right = head;
            // 只有head和left的子树发生了变化,因此他俩更新高度
            head.height = Math.max(head.left != null ? head.left.height : 0, head.right != null ? head.right.height : 0) + 1;
            left.height = Math.max(left.left != null ? left.left.height : 0, left.right != null ? left.right.height : 0) + 1;
            return left;
        }

        /**
         * AVL树维持平衡的补丁
         * 左子树和右子树的高度差的绝对值>1认为不平衡.
         * 高度差大于1又分为4种情况:
         * LL型违规:左子树的左子树过高导致不平衡,解法:一次右旋
         * LR型违规:左子树的右子树过高导致不平衡,解法:先左旋再右旋
         * RR型违规:右子树的右子树过高导致不平衡,解法:一次左旋
         * RL型违规:右子树的左子树过高导致不平衡,解法:先右旋再左旋
         */
        private Node<K, V> maintain(Node<K, V> head) {
            if (head == null) {
                return null;
            }
            int leftHeight = head.left != null ? head.left.height : 0;
            int rightHeight = head.right != null ? head.right.height : 0;
            if (Math.abs(leftHeight - rightHeight) > 1) {
                // 左子树和右子树的高度差大于1,需要调平衡
                // 先判断是左子树更高还是右子树更高
                if (leftHeight > rightHeight) {
                    // 左子树更高,LX型
                    int leftLeftHeight = head.left != null && head.left.left != null ? head.left.left.height : 0;
                    int leftRightHeight = head.left != null && head.left.right != null ? head.left.right.height : 0;
                    if (leftLeftHeight >= leftRightHeight) {
                        // 左子树的左子树比左子树的右子树更高,LL型违规,一样高既是LL型违规又是LR型违规,按LL型处理
                        // 爷爷节点右旋
                        head = rightRotate(head);
                    } else {
                        // 左子树的右子树比左子树的左子树更高,LR型违规
                        // 先父亲节点左旋,再爷爷节点右旋
                        head.left = leftRotate(head.left);
                        head = rightRotate(head);
                    }
                } else {
                    // 右子树更高,RX型
                    int rightRightHeight = head.right != null && head.right.right != null ? head.right.right.height : 0;
                    int rightLeftHeight = head.right != null && head.right.left != null ? head.right.left.height : 0;
                    if (rightRightHeight >= rightLeftHeight) {
                        // 右子树的右子树比右子树的左子树更高,RR型违规,一样高既是RR型违规又是RL型违规,按RR型处理
                        // 爷爷节点左旋
                        head = leftRotate(head);
                    } else {
                        // 右子树的左子树比右子树的右子树更高,RL型违规
                        // 先父亲节点右旋,再爷爷节点左旋
                        head.right = rightRotate(head.right);
                        head = leftRotate(head);
                    }
                }
            }
            return head;
        }

        /**
         * 向AVL树中插入一个节点,返回这棵树新的根节点(为什么会产生新根节点,因为要调平衡)
         * 递归函数,小于往左走,大于往右走.由上层函数保证一定不等于.
         */
        private Node<K, V> add(Node<K, V> head, K key, V value) {
            // 递归出口,如果走到null位置,就新建节点保存key,value
            if (head == null) {
                return new Node<>(key, value);
            }
            // 小于往左走,大于往右走
            if (key.compareTo(head.key) < 0) {
                head.left = add(head.left, key, value);
            } else {
                head.right = add(head.right, key, value);
            }
            // 所有递归经过的节点都要更新高度
            head.height = Math.max(head.left != null ? head.left.height : 0, head.right != null ? head.right.height : 0) + 1;
            // 所有递归经过的节点都要调平衡
            return maintain(head);
        }

        /**
         * 从AVL树中删除一个节点,返回这棵树新的根节点(为什么会产生新根,因为要调平衡)
         * 删除的节点分4种情况:
         * 1.无左无右:直接删除,删除节点=null
         * 2.有左无右:用左子树替代,删除节点=左子树
         * 3.有右无左:用右子树替代,删除节点=右子树
         * 4.有左有右:用右子树的最左节点替代删除节点,同时用右子树最左节点的左右子树替代右子树最左节点.
         */
        private Node<K, V> delete(Node<K, V> head, K key) {
            if (head == null) {
                return null;
            }
            if (key.compareTo(head.key) < 0) {
                // 如果删除节点比当前节点小,向左递归
                // 注意一定要接收返回值,因为delete之后会调平衡,调平衡可能会导致头节点变化
                head.left = delete(head.left, key);
            } else if (key.compareTo(head.key) > 0) {
                // 如果删除节点比当前节点大,向右递归
                head.right = delete(head.right, key);
            } else {
                // 如果删除节点就是当前节点
                if (head.left == null && head.right == null) {
                    // 无左无右
                    head = null;
                } else if (head.left != null && head.right == null) {
                    // 有左无右
                    head = head.left;
                } else if (head.left == null && head.right != null) {
                    // 有右无左
                    head = head.right;
                } else {
                    // 有左有右
                    // 找到右子树的最左节点
                    Node<K, V> rightLeft = head.right;
                    while (rightLeft.left != null) {
                        rightLeft = rightLeft.left;
                    }
                    // 在右子树上递归删除最左节点(最左节点找谁替代,已经在delete里做好了)
                    head.right = delete(head.right, rightLeft.key);
                    // 用右子树最左节点替代删除节点
                    rightLeft.left = head.left;
                    rightLeft.right = head.right;
                    head = rightLeft;
                }
            }
            // 递归经过的节点都要更新高度(前提是没被删除)
            if (head != null) {
                head.height = Math.max(head.left != null ? head.left.height : 0, head.right != null ? head.right.height : 0) + 1;
            }
            // 递归经过的节点都要调平衡
            return maintain(head);
        }

        /**
         * 在AVL树上找到离key最近的一个节点
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

        // 以下是提供给用户的方法

        /**
         * 判断是否存在
         */
        public boolean contains(K key) {
            Node<K, V> mostClosed = findMostClosed(key);
            return mostClosed != null && mostClosed.key.compareTo(key) == 0;
        }

        /**
         * 查询
         */
        public Node<K, V> get(K key) {
            Node<K, V> mostClosed = findMostClosed(key);
            return mostClosed != null && mostClosed.key.compareTo(key) == 0 ? mostClosed : null;
        }

        /**
         * 插入或修改
         */
        public void put(K key, V value) {
            if (key == null) {
                return;
            }
            Node<K, V> mostClosed = findMostClosed(key);
            if (mostClosed != null && mostClosed.key.compareTo(key) == 0) {
                mostClosed.value = value;
            } else {
                root = add(root, key, value);
                size++;
            }
        }

        /**
         * 删除
         */
        public void remove(K key) {
            if (key == null) {
                return;
            }
            if (contains(key)) {
                root = delete(root, key);
                size--;
            }
        }


        // 用于打印测试
        public void print(Node<K, V> head) {
            process(head);
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
        AVLTreeMap<Integer, String> avl = new AVLTreeMap<>();
        avl.put(4, "4");
        avl.put(6, "6");
        avl.put(5, "5");
        avl.put(1, "1");
        avl.put(7, "7");
        avl.put(9, "9");
        avl.print(avl.root);
        avl.remove(9);
        avl.print(avl.root);
        avl.put(4, "3");
        avl.print(avl.root);
        System.out.println(avl.get(4).value);
    }

}
