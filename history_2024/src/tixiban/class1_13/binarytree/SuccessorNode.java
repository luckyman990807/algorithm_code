package tixiban.class1_13.binarytree;

/**
 * 二叉树节点有三个指针：左孩子，右孩子，父亲
 * 给定二叉树上任意一个节点，找出这个节点的后继节点
 *
 * 后继节点定义：在中序遍历中某节点的下一个节点
 */
public class SuccessorNode {
    public class Node{
        int value;
        Node left;
        Node right;
        Node parent;

        public Node(int value) {
            this.value = value;
        }
    }

    /**
     * 思路：
     * O(N)的方法是利用parent节点一直往上找到根节点，对整棵树中序遍历，找到X的下一个。
     * 我们看的是O(K) （K是给定节点X到他后继节点的真实距离）的方法：
     * 中序遍历X节点的下一个节点只有两种情况：1、X右子树的最左节点，2、X是谁的左子树上的最右节点
     * 只要找到这两个节点就可以了
     */
    public Node getSuccessorNode(Node node){
        if(node==null){
            return null;
        }

        if(node.right!=null){
            // 如果有右子树，就找循环找右子树上最左的节点
            Node cur = node.right;
            while (cur.left!=null){
                cur=cur.left;
            }
            return cur;
        }

        // 没有右子树，就找谁的左子树的最右节点是这个node
        Node cur = node;
        Node parent = node.parent;
        // 只要cur是父亲的右孩子，就往上找，直到发现cur是父亲的左孩子了，就停，这个父亲就是后继
        // 或者一直找到根节点（parent==null）了还不是左孩子，就返回null
        while (parent!=null &&cur==parent.right){
            cur=parent;
            parent=cur.parent;
        }
        return parent;
    }
}
