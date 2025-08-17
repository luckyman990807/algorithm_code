package tixiban.class1_13.binarytree;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定一棵多叉树，要求转成一棵二叉树，并且二叉树还能转回多叉树
 * 思路：多叉树节点的所有孩子，挂到二叉树节点的左子树右边界上。（或者右子树左边界也行，只要转二和转多的套路一样就行）
 */
public class NAryTreeToBinaryTree {
    /**
     * 二叉树节点
     */
    public class BinaryNode{
        public int value;
        public BinaryNode left;
        public BinaryNode right;

        public BinaryNode(int value) {
            this.value = value;
        }
    }

    /**
     * 多叉树节点
     */
    public class NAryNode{
        public int value;
        public List<NAryNode> children;

        public NAryNode(int value, List<NAryNode> children) {
            this.value = value;
            this.children = children;
        }
    }

    /**
     * 多叉树转成二叉树
     * @param nAryRoot
     * @return
     */
    public BinaryNode toBinaryTree(NAryNode nAryRoot){
        if(nAryRoot == null){
            return null;
        }
        // 根节点
        BinaryNode binaryRoot = new BinaryNode(nAryRoot.value);
        // 递归把孩子列表转成左子树的右边界
        binaryRoot.left= encode(nAryRoot.children);
        return binaryRoot;
    }

    /**
     * 递归，把多叉树的孩子列表，转成二叉树的一条右边界
     * @param children
     * @return
     */
    public BinaryNode encode(List<NAryNode> children){
        // 记录返回右边界的头节点
        BinaryNode head = null;
        BinaryNode cur = null;
        for (NAryNode child : children){
            BinaryNode node = new BinaryNode(child.value);
            if(head==null){
                // 如果没记录过头节点，说明是第一次循环，记录一下头节点
                head=node;
            }else {
                // 否则就不是第一次循环，既然不是第一次循环，那么cur就不为null了
                // 把新节点做为cur的右孩子，连接成右边界
                cur.right = node;
            }

            // cur指针指向新节点
            cur = node;
            // 当前孩子节点的孩子节点，递归转成cur的左孩子的右边界
            cur.left= encode(child.children);
        }
        return head;
    }

    /**
     * 二叉树转回多叉树
     */
    public NAryNode toNAryTree(BinaryNode binaryRoot){
        if(binaryRoot==null){
            return null;
        }
        // 赋值根节点，递归把左子树的右边界转成孩子列表
        return new NAryNode(binaryRoot.value, decode(binaryRoot.left));
    }

    /**
     * 递归，把二叉树的一条右边界，转成多叉树的孩子列表
     */
    public List<NAryNode> decode(BinaryNode binaryHead){
        List<NAryNode> children = new ArrayList<>();
        BinaryNode cur = binaryHead;
        while (cur!=null){
            // 当前多叉树节点的孩子就是当前二叉树节点的左子树右边界
            NAryNode child = new NAryNode(cur.value, decode(cur.left));
            children.add(child);
            cur=cur.right;
        }
        return children;
    }
}
