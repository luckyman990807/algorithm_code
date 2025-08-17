package tixiban.class1_13.dynamicprogramming;

/**
 * 给定一个整型数组，代表不同点数的纸牌排成一行，
 * 玩家a和玩家b每次从最左边或最右边拿一张纸牌，规定玩家a先拿，玩家b后拿，牌都拿完后点数多的获胜，
 * 玩家a和b都绝顶聪明，求最后获胜者的点数。
 */
public class Code02CardsFirstSecond {
    /**
     * 方法一：纯暴力递归尝试
     */

    /**
     * 先手的尝试策略，暴力递归
     * 给定纸牌点数数组和当前剩余纸牌的范围，返回作为先手能拿到的最大点数
     *
     * @param arr   纸牌点数数组
     * @param left  当前剩余纸牌的左边界
     * @param right 当前剩余纸牌的右边界
     * @return
     */
    public static int firstViolent(int[] arr, int left, int right) {
        // 如果只剩一张牌，那么肯定被先手取到，直接返回这个点数
        if (left == right) {
            return arr[left];
        }
        // 选择最左边能获得的最大点数 = 最左边点数 + 剩余纸牌作为后手能获得的最大点数（因为剩余纸牌会先让对方拿一张，自己再拿，所以在剩余纸牌中自己是后手了）
        int selectLeft = arr[left] + secondViolent(arr, left + 1, right);
        // 选择最右边能获得的最大点数 = 最右边点数 + 剩余纸牌作为后手能获得的最大点数
        int selectRight = arr[right] + secondViolent(arr, left, right - 1);
        // 返回两种情况中最大的点数
        return Math.max(selectLeft, selectRight);
    }

    /**
     * 后手的尝试策略，暴力递归
     * 给定指派点数数组和当前剩余纸牌的范围，返回作为后手能拿到的最大点数
     */
    public static int secondViolent(int[] arr, int left, int right) {
        // 自己是后手，如果只剩一张牌，那么肯定被对方拿走，自己啥也拿不到，返回0
        if (left == right) {
            return 0;
        }
        // 当前剩余纸牌自己作为后手，要么先被对方拿走最左边，要么先被对方拿走最右边
        // 最左边被对方拿走后自己能获得的最大点数 = 剩余纸牌自己作为先手能获得的最大点数
        int leftSelected = firstViolent(arr, left + 1, right);
        // 最右边被对方拿走后自己能获得的最大点数 = 剩余纸牌自己作为先手能获得的最大点数
        int rightSelected = firstViolent(arr, left, right - 1);
        // 两种情况取最小的，因为当前自己是后手，对方是先手，最大的肯定被对方拿走，留给后手的一定是最小的
        return Math.min(leftSelected, rightSelected);
    }

    public static int getMarksViolent(int[] arr) {
        // 当前数组自己作为先手能获得的最大点数，当前数组自己作为后手能获得的最大点数，取最大值返回
        return Math.max(firstViolent(arr, 0, arr.length - 1), secondViolent(arr, 0, arr.length - 1));
    }

    /**
     * 方法二：递归+傻缓存法避免重复计算
     */

    /**
     * 先手尝试策略，递归+傻缓存
     * 由暴力递归函数可知，可变参数为left、right，所以要以他俩为key去缓存计算结果
     */
    public static int firstCache(int[] arr, int left, int right, int[][] firstCache, int[][] secondCache) {
        // 如果缓存里有，直接返回缓存数据
        if (firstCache[left][right] != -1) {
            return firstCache[left][right];
        }
        // 否则就计算，把结果加到缓存中，再返回
        int marks = 0;
        if (left == right) {
            marks = arr[left];
        } else {
            int selectLeft = arr[left] + secondCache(arr, left + 1, right, firstCache, secondCache);
            int selectRight = arr[right] + secondCache(arr, left, right - 1, firstCache, secondCache);
            marks = Math.max(selectLeft, selectRight);
        }
        firstCache[left][right] = marks;
        return marks;
    }

    /**
     * 后手尝试策略，递归+傻缓存
     */
    public static int secondCache(int[] arr, int left, int right, int[][] firstCache, int[][] secondCache) {
        if (secondCache[left][right] != -1) {
            return secondCache[left][right];
        }
        int marks = 0;
        if (left == right) {
            marks = 0;
        } else {
            int leftSelected = firstCache(arr, left + 1, right, firstCache, secondCache);
            int rightSelected = firstCache(arr, left, right - 1, firstCache, secondCache);
            marks = Math.min(leftSelected, rightSelected);
        }
        return marks;
    }

    public static int getMarksCache(int[] arr) {
        // 可变参数left、right，取值范围都是[0,arr.length-1]，所以缓存大小为arr.length * arr.length
        int[][] firstCache = new int[arr.length][arr.length];
        int[][] secondCache = new int[arr.length][arr.length];
        // 初始化为-1，表示f(i,j)没缓存过
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                firstCache[i][j] = -1;
                secondCache[i][j] = -1;
            }
        }
        return Math.max(firstCache(arr, 0, arr.length - 1, firstCache, secondCache), secondCache(arr, 0, arr.length - 1, firstCache, secondCache));
    }

    /**
     * 方法三：直接基于缓存表计算
     */

    /**
     * 由先手和后手的递归函数，可抽象成先手函数f(left, right)，后手函数g(left, right)，有以下限定条件：
     * 1、当left=right时，f(left, right) = arr[left]，g(left, right) = 0
     * 2、当left!=right时，f(left, right) = MAX{arr[left] + g(left+1, right), arr[right] + g(left, right-1)}， g(left, right) = MIN{f(left+1, right), f(left, right-1)}
     * 3、left<=right
     *
     * @param arr
     * @return
     */
    public static int getMarksMap(int[] arr) {
        int[][] firstMap = new int[arr.length][arr.length];
        int[][] secondMap = new int[arr.length][arr.length];

        // 根据限定条件1，把先手二维数组的对角线设为arr[i]，后手二维数组的对角线设为0（Java数组初始化默认就是0）
        for (int i = 0; i < arr.length; i++) {
            firstMap[i][i] = arr[i];
        }

        // 把对角线以上的半个矩阵填充，填充的顺序：从1列到N-1列，每一列从对角线往上一行到0行
        // 为什么这个顺序：因为根据限定条件2，f(i,j)的值依赖g(i+1, j)和g(i, j-1)的值，也就是左边和下边。按照这个顺序能保证每次遍历到的位置其依赖位置已经遍历过
        // 也可以设计一个沿对角线方向斜着推的顺序，但没有这个简单
        for (int right = 1; right < arr.length; right++) {
            // 根据限定条件3，left<=right，=的情况就是对角线，已经算了，现在只算<的情况，也就是对角线上半个矩阵
            for (int left = right - 1; left >= 0; left--) {
                // 根据限定条件2填充二维数组的值
                firstMap[left][right] = Math.max(arr[left] + secondMap[left + 1][right], arr[right] + secondMap[left][right - 1]);
                secondMap[left][right] = Math.min(firstMap[left + 1][right], firstMap[left][right - 1]);
            }
        }

        return Math.max(firstMap[0][arr.length - 1], secondMap[0][arr.length - 1]);

    }

    public static void main(String[] args) {
        int[] arr = {10, 7, 56, 100, 34, 78};
        System.out.println(getMarksViolent(arr));
        System.out.println(getMarksCache(arr));
        System.out.println(getMarksMap(arr));
    }
}
