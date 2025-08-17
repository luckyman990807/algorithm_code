package tixiban.class1_13.binarytree;

/**
 * 一张纸条，对折一次打开，产生一个凹折痕；连续对折两次打开，折痕依次是凹凹凸；连续对折3次打开，折痕依次是凹凹凸凹凹凸凸……
 * 问对折n次打开，折痕的规则
 */
public class PrintAllCrease {
    /**
     * 思路：
     * 对折一次，只有一个凹折痕；
     * 对折两次，在原有折痕的两边出现一凹一凸两个新折痕
     * 对折三次，又在两次的折痕的基础上，每条折痕的两边出现一凹一凸两个新折痕
     *
     * 抽象成二叉树：
     * 头节点是凹，每个节点的左孩子都是凹，每个节点的右孩子都是凸
     * 每折一次就是让二叉树长出一层
     * 打印折痕的规则就是中序遍历
     *
     * 甚至不需要真实地构建这棵二叉树，只需要模拟中序遍历，打印想象中的二叉树即可
     */
    public static void printAllCrease(int n) {
        process(1, n, true);
    }

    /**
     * @param cur 当前是第几次折
     * @param n 一共要折多少次
     * @param crease 折痕，true表示凹，false表示凸
     */
    public static void process(int cur, int n, boolean crease) {
        if (cur > n) {
            // 如果当前次数大于要求折的次数，直接返回
            return;
        }
        // 模拟打印左孩子
        process(cur + 1, n, true);
        // 打印当前节点
        System.out.println(crease ? "凹" : "凸");
        // 模拟打印右孩子
        process(cur + 1, n, false);
    }

    public static void main(String[] args) {
        printAllCrease(3);
    }
}
