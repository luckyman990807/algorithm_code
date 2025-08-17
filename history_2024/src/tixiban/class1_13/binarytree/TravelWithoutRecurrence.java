package tixiban.class1_13.binarytree;

import tixiban.class1_13.binarytree.common.Node;

import java.util.Stack;

/**
 * 非递归方式，遍历二叉树，先序、后序、中序
 */
public class TravelWithoutRecurrence {
    /**
     * 先序遍历，最简单。
     * 自己定义栈，先把头节点压栈，然后遍历：
     * 弹出栈顶节点，执行业务逻辑，压入右节点，压入左节点（如果想 头 左 右 的话，就先压右再压左）
     */
    public void preOrder(Node head) {
        if (head == null) {
            return;
        }
        Stack<Node> stack = new Stack<>();
        stack.push(head);
        while (!stack.empty()) {
            Node cur = stack.pop();
            if (cur != null) {
                // 业务逻辑
                System.out.println(cur.value + " ");

                stack.push(cur.right);
                stack.push(cur.left);
            }
        }
        System.out.println();
    }

    /**
     * 后序遍历，在先序的基础上稍作改动。
     * 总的来说，后序遍历相当于先序遍历的逆序，怎么输出先序遍历的逆序呢？把先序的输出压栈，压完再依次弹栈
     * 细节方面，先序先压右再压左，输出就是头左右；而先压左再压右，输出就是头右左，基于这个顺序再逆序，得到就是左右头。
     */
    public void postOrder(Node head) {
        if (head == null) {
            return;
        }
        Stack<Node> preOrderStack = new Stack<>();
        Stack<Node> postOrderStack = new Stack<>();
        preOrderStack.push(head);
        while (!preOrderStack.empty()) {
            Node cur = preOrderStack.pop();
            if (cur != null) {
                // 先序的输出压栈，到时候弹栈就是后序
                postOrderStack.push(cur);

                // 先压左再压右，先序顺序就是头右左，到时候逆序就是左右头
                preOrderStack.push(cur.left);
                preOrderStack.push(cur.right);
            }
        }
        // 相当于输出先序的逆序
        while (!postOrderStack.empty()) {
            System.out.println(postOrderStack.pop() + " ");
        }
        System.out.println();
    }

    /**
     * 中序遍历，最难理解。
     * 思路：
     * 一棵二叉树可以只被子树左边界分解掉的。
     * 把一棵树的左边界依次压栈，压完之后，栈中相邻的靠顶元素是靠底元素的左节点，且栈顶就是这棵树未被遍历的最左节点。
     * 从站顶元素开始输出，然后把他右子树按照同样的套路处理，就实现了先左再头再右（栈顶要么是最左的没有左节点，要么就是已经被输出了；再输出自己；再处理右子树）
     * 步骤：
     * 1、遍历直到栈为空并且cur也为null。栈为空表示没有头节点了，cur为null表示左边界到头了。
     * 2、揪着cur直到把左边界全部压栈。
     * 3、左边界压完之后，打印cur，并把cur的右节点作为新的cur，重复2。
     */
    public void midOrder(Node head) {
        if (head == null) {
            return;
        }
        Stack<Node> stack = new Stack<>();
        Node cur = head;
        while (!stack.empty() || cur != null) {
            if (cur != null) {
                // 把左边界全部压栈
                stack.push(cur);
                cur = cur.left;
            } else {
                // 走到这cur==null说明左边界到底了，当前栈顶没有左子树了，可以输出当前栈顶了。
                cur = stack.pop();
                System.out.println(cur.value + " ");
                // 没有左；输出当前；再处理右。这就是中序遍历。
                stack.push(cur.right);
            }
        }
        System.out.println();
    }
}
