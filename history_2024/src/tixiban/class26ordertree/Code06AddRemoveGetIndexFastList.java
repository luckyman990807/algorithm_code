package tixiban.class26ordertree;

/**
 * ArrayList是用数组实现的,所以getIndex很快,直接通过索引寻址,
 * 但是addIndex和removeIndex很慢,因为每次操作都要移动index后面的所有元素.
 * 现在要求实现一个List,要求getIndex、addIndex、removeIndex都快.
 *
 * 思路:改写有序表,用虚拟的数组下标当作key来比较,左孩子的下标比父亲小,右孩子的下标比父亲大.最终getIndex、addIndex、removeIndex都是O(logN).
 */
public class Code06AddRemoveGetIndexFastList {
    public static class Node {
        public int value;
        public Node left;
        public Node right;
        public int size;

        public Node(int value) {
            this.value = value;
            this.size = 1;
        }
    }

    public static class SizeBalanceTree {
        private Node root;

        private Node rightRotate(Node head) {
            if (head == null) {
                return null;
            }
            Node left = head.left;
            head.left = left.right;
            left.right = head;
            left.size = head.size;
            head.size = (head.left != null ? head.left.size : 0) + (head.right != null ? head.right.size : 0) + 1;
            return left;
        }

        private Node leftRotate(Node head) {
            if (head == null) {
                return null;
            }
            Node right = head.right;
            head.right = right.left;
            right.left = head;
            right.size = head.size;
            head.size = (head.left != null ? head.left.size : 0) + (head.right != null ? head.right.size : 0) + 1;
            return right;
        }

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

        /**
         * 向sb树中插入节点
         */
        private Node add(Node head, int index, int value) {
            // 递归出口,如果一路滑倒了null,这就是新节点要待的地方
            if (head == null) {
                return new Node(value);
            }
            head.size++;
            // 当前节点的下标.如果左子树大小为2,那么当前节点就是第3个,下标为2
            int headIndex = head.left != null ? head.left.size : 0;
            if (index <= headIndex) {
                // 如果插入下标比当前节点下标小,那么往左滑;如果相等,那说明插入节点把当前节点往后挤了一位,插入节点在前面,还是往左滑.
                head.left = add(head.left, index, value);
            } else {
                // 如果插入下标比当前节点大,那么往右滑,对于右子树来说,插入下标=index-headIndex-1,因为前面有headIndex+1个,要减掉
                head.right = add(head.right, index - headIndex - 1, value);
            }
            // 调平衡
            return maintain(head);
        }

        /**
         * 从sb树上删除节点
         */
        private Node delete(Node head, int index) {
            head.size--;
            int headIndex = head.left != null ? head.left.size : 0;

            // 如果要删除的下标小于当前下标,左滑
            if (index < headIndex) {
                /** 注意,delete left返回的是新的左子树头节点,一定要赋值给head的左指针,不能直接返回,直接返回就让head指向head的左子树了 */
                head.left = delete(head.left, index);
                return head;
            }
            // 如果要删除的下标大于当前下标,右滑
            if (index > headIndex) {
                head.right = delete(head.right, index - headIndex - 1);
                return head;
            }
            // 要删除的就是当前下标
            // 无左无右
            if (head.left == null && head.right == null) {
                return null;
            }
            // 有左无右
            if (head.left != null && head.right == null) {
                return head.left;
            }
            // 有右无左
            if (head.left == null && head.right != null) {
                return head.right;
            }
            // 有左有右
            // 找出右子树的最左节点
            Node rightLeft = head.right;
            while (rightLeft.left != null) {
                rightLeft = rightLeft.left;
            }
            // 从右子树上删除最左节点,相当于删除右子树下标为0的节点
            head.right = delete(head.right, 0);
            // 用右子树最左节点代替头节点
            rightLeft.left = head.left;
            rightLeft.right = head.right;
            rightLeft.size = head.size;
            return rightLeft;
        }

        /**
         * 从sb树上查询节点
         */
        private Node get(Node head, int index) {
            int headIndex = head.left != null ? head.left.size : 0;
            if (index < headIndex) {
                return get(head.left, index);
            }
            if (index > headIndex) {
                return get(head.right, index - headIndex - 1);
            }
            return head;
        }


        public void add(int index, int value) {
            if (root == null) {
                root = new Node(value);
                return;
            }
            if (index <= root.size) {
                root = add(root, index, value);
            }
        }

        public void delete(int index) {
            if (root == null || index >= root.size) {
                return;
            }
            root = delete(root, index);
        }

        public int get(int index) {
            if (root == null || index >= root.size) {
                throw new RuntimeException("out index!");
            }
            return get(root, index).value;
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
            System.out.print(head.value + " ");
            process(head.right);
        }
    }

    public static void main(String[] args) {
        SizeBalanceTree sbt = new SizeBalanceTree();
        sbt.add(0, 3);
        sbt.add(1, 1);
        sbt.add(2, 9);
        sbt.add(3, 5);
        sbt.add(4, 4);
        sbt.print();
        sbt.delete(2);
        sbt.print();
        sbt.delete(3);
        sbt.print();
        System.out.println(sbt.get(2));

    }
}
