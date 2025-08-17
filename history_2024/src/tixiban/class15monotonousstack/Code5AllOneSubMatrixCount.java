package tixiban.class15monotonousstack;

import java.util.Stack;

/**
 * 给定一个二维数组，其中的值不是0就是1，求全部由1构成的子矩阵的数量
 * <p>
 * 思路：
 * 以第0行为底的子矩阵有多少个，以第1行为底的子矩阵有多少个...所有行算完加起来。
 * 以第i行为底的子矩阵个数怎么算？
 * 还是像上一道题一样，压缩成向上连通1的个数的数组，还是利用单调栈，只不过弹出结算的时候，计算以第i行为底，高度为height[stack.pop()]的子矩阵个数。
 * 从a列到b列一共有多少个个子矩阵怎么算？
 * 公式n*(n+1)/2
 * 相等的时候弹栈怎么结算？
 * 相等的时候弹栈不结算，等到最后一个遇到小于的时候弹栈结算，这样向右连通的最完整，如果前面也算的话会导致重复
 */
public class Code5AllOneSubMatrixCount {

    public static int countSubMatrix(int[][] matrix) {
        int count = 0;
        int[] height = new int[matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                height[j] = matrix[i][j] == 0 ? 0 : height[j] + matrix[i][j];
            }
            count += countFromBottom(height);
        }
        return count;
    }

    // 比如
    //              1
    //              1
    //              1         1
    //    1         1         1
    //    1         1         1
    //    1         1         1
    //
    //    2  ....   6   ....  9
    // 如上图，假设在6位置，1的高度为6
    // 在6位置的左边，离6位置最近、且小于高度6的位置是2，2位置的高度是3
    // 在6位置的右边，离6位置最近、且小于高度6的位置是9，9位置的高度是4
    // 此时我们求什么？
    // 1) 求在3~8范围上，必须以高度6作为高的矩形，有几个？
    // 2) 求在3~8范围上，必须以高度5作为高的矩形，有几个？
    // 也就是说，<=4的高度，一律不求
    // 那么，1) 求必须以位置6的高度6作为高的矩形，有几个？
    // 3..3  3..4  3..5  3..6  3..7  3..8
    // 4..4  4..5  4..6  4..7  4..8
    // 5..5  5..6  5..7  5..8
    // 6..6  6..7  6..8
    // 7..7  7..8
    // 8..8
    // 这么多！= 21 = (9 - 2 - 1) * (9 - 2) / 2
    // 这就是任何一个数字从栈里弹出的时候，计算矩形数量的方式
    public static int countFromBottom(int[] arr) {
        int count = 0;
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < arr.length; i++) {
            while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {
                Integer cur = stack.pop();
                // 如果栈顶和i相等，直接弹出不管，如果栈顶比i大，才结算
                if (arr[cur] > arr[i]) {
                    // 左右连通的长度
                    int n = stack.isEmpty() ? i : i - stack.peek() - 1;
                    // 左右边界的高度，左右边界以下的就不结算了，等左右边界弹出的时候会构成更完整的连通区结算
                    int down = Math.max(arr[i], stack.isEmpty() ? 0 : arr[stack.peek()]);
                    count += (arr[cur] - down) * (n * (n + 1)) >> 1;
                }
            }
            stack.push(i);
        }
        while (!stack.isEmpty()) {
            Integer cur = stack.pop();
            int n = stack.isEmpty() ? arr.length : arr.length - stack.peek() - 1;
            int down = stack.isEmpty() ? 0 : arr[stack.peek()];
            count += (arr[cur] - down) * (n * (n + 1)) >> 1;
        }
        return count;
    }

    public static void main(String[] args) {
        int[][] matrix={
                {1,1,1,0},
                {1,1,0,1},
                {1,1,1,0}
        };
        System.out.println(countSubMatrix(matrix));
    }
}
