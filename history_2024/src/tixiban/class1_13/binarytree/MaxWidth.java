package tixiban.class1_13.binarytree;

import tixiban.class1_13.binarytree.common.Node;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 计算二叉树节点数最多的层有多少个节点，即最大宽度
 */
public class MaxWidth {
    /**
     * 思路：
     * 按层遍历，遍历到第n层时，会把第n+1层的节点纳入队列
     * 循环遍历，在不断逼近当前层最后一个节点的同时，不断试探下一层最后节点的位置
     * 这样遍历完一层，到达当前层最后节点时，既能算出当前层宽度，又能试探出下一层最后节点
     * 第一层的最后节点是一开始手动赋值的，就是根节点
     */
    public int getMaxWidth(Node head){
        if(head==null){
            return 0;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(head);

        // 当前层的最后一个节点
        Node curLevelEnd = head;
        // 下一层的最后一个节点
        Node nextLevelEnd = null;
        // 当前层的宽度
        int curWidth = 0;
        // 最大宽度
        int maxWidth = 0;

        while (!queue.isEmpty()){
            Node cur = queue.poll();
            // 每弹出一个节点，当前层宽度+1
            curWidth++;

            if(cur.left!=null){
                // 如果左孩子不为空，一边加入按层遍历队列，一边设为下一层最后节点
                queue.add(cur.left);
                nextLevelEnd = cur.left;
            }
            if(cur.right!=null){
                queue.add(cur.right);
                // 每个孩子节点都设为最后节点，那么遍历完一层总能试探出下一层的最后节点
                nextLevelEnd=cur.right;
            }

            if(cur==curLevelEnd){
                // 如果当前节点就是本层最后节点
                // 刷新最大宽度
                maxWidth = Math.max(curWidth, maxWidth);
                // 因为马上要进入下一层了，所以清空当前宽度，当前层最后节点也赋值为下一层最后节点
                curWidth=0;
                curLevelEnd=nextLevelEnd;
            }
        }
        return maxWidth;
    }
    /**
     * 13000
     * 9500
     * 5500 = 3000+2500
     */
}
