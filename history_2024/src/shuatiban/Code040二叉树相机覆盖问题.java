package shuatiban;

/**
 * 腾讯面试题
 * 给定一个二叉树，我们在树的节点上安装摄像头，节点上的每个摄影头都可以监视其父对象、自身及其直接子对象，计算监控树的所有节点所需的最小摄像头数量
 *
 * 思路:二叉树递归套路干一切
 *
 */
public class Code040二叉树相机覆盖问题 {
    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * 二叉树递归,当前节点返回给父节点的信息,记录了当前节点的3种可能性
     */
    public class Info {
        // 当前节点没被覆盖,子树全被覆盖,所需要的最少相机
        long uncovered;
        // 当前节点被覆盖但没有相机,子树全被覆盖,所需要的最少相机
        long coveredNoCamera;
        // 当前节点被覆盖且有相机,子树全被覆盖,所需要的最少相机
        long coveredHasCamera;

        // 为什么3种可能性都必须子树全被覆盖?
        // 因为如果当前节点没被覆盖,还能被父节点补救,但如果当前节点的子树没被覆盖,那么返回给父节点也不可能补救了

        // 为什么要用long类型?因为有可能等于Integer.Max,并且设计两数相加,可能溢出.

        public Info(long uncovered, long coveredNoCamera, long coveredHasCamera) {
            this.uncovered = uncovered;
            this.coveredNoCamera = coveredNoCamera;
            this.coveredHasCamera = coveredHasCamera;
        }
    }

    /**
     * 二叉树递归套路
     */
    public Info process(TreeNode head) {
        if (head == null) {
            // 如果当前节点是空节点,那么只有一种可能性:已被覆盖(或者说不需要覆盖),无相机.你让一个空节点没被覆盖,不可能,返回系统最大值.你让一个空节点有相机,不可能,返回系统最大值.
            return new Info(Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
        }

        // 递归向左右孩子要信息
        Info leftInfo = process(head.left);
        Info rightInfo = process(head.right);

        // 当前节点没被覆盖且当前节点的子树都被覆盖所需要的最少相机 = 左孩子被覆盖且没有相机且左孩子的子树都被覆盖 + 右孩子被覆盖且没有相机且右孩子的子树都被覆盖
        long uncovered = leftInfo.coveredNoCamera + rightInfo.coveredNoCamera;
        // 当前节点被覆盖但没有相机所需要的最少相机 = max(左右孩子都有相机, 被左孩子的相机覆盖, 被右孩子的相机覆盖)
        long coveredNoCamera = Math.min(
                leftInfo.coveredHasCamera + rightInfo.coveredHasCamera,
                Math.min(
                        leftInfo.coveredHasCamera + rightInfo.coveredNoCamera,
                        rightInfo.coveredHasCamera + leftInfo.coveredNoCamera));
        // 当前节点被覆盖且有相机 = 给当前节点的相机1 + 左孩子随意状况反正当前节点有相机能cover住 + 右孩子随意状况反正当前节点有相机能cover住
        long coveredHasCamera = 1
                + Math.min(leftInfo.uncovered, Math.min(leftInfo.coveredNoCamera, leftInfo.coveredHasCamera))
                + Math.min(rightInfo.uncovered, Math.min(rightInfo.coveredNoCamera, rightInfo.coveredHasCamera));

        // 把当前节点的信息返回给父节点
        return new Info(uncovered, coveredNoCamera, coveredHasCamera);
    }

    public int minCameraCover1(TreeNode root) {
        Info info = process(root);
        // 三种可能性取最小值:
        // 1、根结节点没被覆盖且子树都被覆盖所需的最少相机 + 给根结点1个相机
        // 2、根结点被覆盖但没有相机且子树都被覆盖
        // 3、根结点被覆盖且有相机且子树都被覆盖
        return (int) Math.min(1 + info.uncovered, Math.min(info.coveredNoCamera, info.coveredHasCamera));
    }


    /**
     * 贪心优化
     * 思路:每个节点其实有唯一的最优解,不需要考虑3种可能性
     */

    /**
     * 二叉树递归,返回给父节点的信息,只包含当前节点的唯一一种状态和唯一一种相机数
     */
    public class Data {
        // 当前节点的状态,0=没覆盖,1=覆盖了但没相机,2=有相机
        int status;
        // 当前子树的相机数
        int cameras;

        public Data(int status, int cameras) {
            this.status = status;
            this.cameras = cameras;
        }
    }

    /**
     * 二叉树递归套路+贪心优化,每个节点只需要考虑唯一的一种可能性
     */
    public Data processGreed(TreeNode head) {
        // 如果当前节点是空,那么他不需要考虑是否覆盖,不能放相机.所以直接设为已覆盖但没相机
        if (head == null) {
            return new Data(1, 0);
        }

        // 分别向左右子树要信息
        Data left = processGreed(head.left);
        Data right = processGreed(head.right);

        // 但凡左右孩子有一个没被覆盖,那当前节点就要放相机补救.但凡左右孩子有一个有相机,那当前节点就不需要放像机就能被覆盖.其他情况就是没被覆盖.
        int status = left.status == 0 || right.status == 0 ? 2 : left.status == 2 || right.status == 2 ? 1 : 0;
        // 如果当前节点要放相机,那么相机数+1,否则依然等于左右子树相机数
        int cameras = (status == 2 ? 1 : 0) + left.cameras + right.cameras;

        return new Data(status, cameras);
    }

    public int minCameraCover(TreeNode root) {
        Data data = processGreed(root);
        // 如果根结点的状态是没被覆盖,那么要在根结点+1个相机,如果根结点已经被覆盖了就直接返回当前树的相机数
        return (data.status == 0 ? 1 : 0) + data.cameras;
    }
}
