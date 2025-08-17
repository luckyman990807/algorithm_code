package shuatiban;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 如果只给定一个二叉树前序遍历数组pre和中序遍历数组in，能否不重建树，而直接生成这个二叉树的后序数组并返回，已知二叉树中没有重复值
 * <p>
 * 思路：递归
 * 如果已知一棵子树的先序数组和中序数组，那么后序数组的最后一个一定等于先序数组的第一个（也就是头节点），
 * 然后在中序数组中找到头节点的位置，这个位置把中序数组划分左右两段，对应的是左右子树，对左右子树递归上述过程。
 * 递归出口：如果先序数组只有一个元素，那么直接赋值给后序数组即可，因为一个节点的子树，先序中序后序都一样。
 */
public class Code100根据先序数组和中序数组得到后序数组 {
    public static int[] getPos(int[] pre, int[] in) {
        // 把中序数组放到map中，方便通过节点值快速拿到在中序数组中的位置，进而划分出左右子树
        Map<Integer, Integer> inMap = new HashMap<>();
        for (int i = 0; i < in.length; i++) {
            inMap.put(in[i], i);
        }
        // 后序数组
        int[] pos = new int[pre.length];
        // 从整棵树开始递归
        process(pre, 0, pre.length - 1, in, 0, in.length - 1, pos, 0, pos.length - 1, inMap);
        return pos;
    }

    private static void process(int[] pre, int preL, int preR, int[] in, int inL, int inR, int[] pos, int posL, int posR, Map<Integer, Integer> inMap) {
        // 递归出口
        // 如果当前子树为空
        if (preL > preR) {
            return;
        }
        // 如果当前子树只有一个元素
        if (preL == preR) {
            pos[posL] = pre[preL];
            return;
        }

        // 后序数组的最后一个，等于先序数组的第一个
        pos[posR] = pre[preL];

        // 头节点在中序数组中的位置
        Integer headIn = inMap.get(pre[preL]);
        // 计算左子树长度
        int leftCount = headIn - inL;
        // 递归左子树，左子树在先序数组中的范围：preL+1，(preL+1)+leftCount-1，在中序数组中的范围：inL，headIn-1，在后序数组中的范围：posL，posL+leftCount-1
        process(pre, preL + 1, preL + leftCount, in, inL, headIn - 1, pos, posL, posL + leftCount - 1, inMap);
        // 递归右子树，右子树在先序数组中的范围：(preL+1+leftCount-1)+1，preR，在中序数组中的范围：headIn+1，inR，在后序数组中的范围：(posL+leftCount-1)+1，posR-1
        process(pre, preL + leftCount + 1, preR, in, headIn + 1, inR, pos, posL + leftCount, posR - 1, inMap);
    }

    public static void main(String[] args) {
        int[] pre = new int[]{1, 2, 3, 4, 5};
        int[] in = new int[]{3, 2, 4, 1, 5};
        System.out.println(Arrays.toString(getPos(pre, in)));
    }
}
