package shuatiban;

/**
 * https://leetcode.com/problems/candy/
 * 老师想给孩子们分发糖果，有N个孩子站成了一条直线，老师会根据每个孩子的表现，预先给他们评分
 * 你需要按照以下要求，帮助老师给这些孩子分发糖果：
 * 每个孩子至少分配到 1 个糖果。
 * 评分更高的孩子必须比他两侧的邻位孩子获得更多的糖果。
 * 那么这样下来，返回老师至少需要准备多少颗糖果
 * 输入一个整数数组表示所有孩子的评分,返回一个整数表示最少需要多少糖果
 *
 * 思路:
 * 从左到右遍历评分数组,生成辅助数组1:第0个孩子给1颗糖,后面的只要评分比左边高,糖果就=左边+1,只要不比左边高,糖果就=1
 * 辅助数组1的含义是:保证「只要比左边分数高就一定比左边糖多」的情况下,每个孩子至少给多少颗糖.
 * 再从右到左生成辅助数组2:最后一个孩子给1颗糖,前面的只要评分比右边高,糖果就=右边+1,只要不比右边高,糖果就=1
 * 辅助数组2的含义是:保证「只要比右边分数高就一定比右边糖多」的情况下,每个孩子至少要给多少颗糖.
 * 每个位置从两个辅助数组取最大值就是每个孩子最少发几颗糖,求累加和就是答案.
 *
 * 不用辅助数组,空间优化到O(1)的思路:
 * 上坡怎么求?波谷糖数=1,后面每上一层糖数递增,直到波峰
 * 下坡怎么求?从波峰到波谷一股脑算出来(梯形面积公式)
 * 平坡怎么求?每次都累加1
 *
 * 进阶：在原来要求的基础上，增加一个要求，相邻的孩子间如果分数一样，分的糖果数必须一样，返回至少需要准备多少颗糖果
 */
public class Code026分糖果 {
    /**
     * 用两个辅助数组
     */
    public int candy1(int[] ratings) {
        int[] help1 = new int[ratings.length];
        int[] help2 = new int[ratings.length];

        help1[0] = 1;
        for (int i = 1; i < ratings.length; i++) {
            if (ratings[i] > ratings[i - 1]) {
                help1[i] = help1[i - 1] + 1;
            } else {
                help1[i] = 1;
            }
        }

        help2[ratings.length - 1] = 1;
        for (int i = ratings.length - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                help2[i] = help2[i + 1] + 1;
            } else {
                help2[i] = 1;
            }
        }

        int candy = 0;
        for (int i = 0; i < ratings.length; i++) {
            candy += Math.max(help1[i], help2[i]);
        }

        return candy;
    }

    /**
     * 空间优化,只用一个辅助数组(经leetcode验证,这个方法最省空间)
     */
    public int candy2(int[] ratings) {
        int[] help = new int[ratings.length];

        help[0] = 1;
        for (int i = 1; i < ratings.length; i++) {
            if (ratings[i] > ratings[i - 1]) {
                help[i] = help[i - 1] + 1;
            } else {
                help[i] = 1;
            }
        }

        int candy = help[ratings.length - 1];
        for (int i = ratings.length - 2; i >= 0; i--) {
            help[i] = ratings[i] > ratings[i + 1] ? Math.max(help[i], help[i + 1] + 1) : help[i];
            candy += help[i];
        }

        return candy;
    }

    /**
     * 进一步空间优化,不用辅助数组,只用有限几个变量
     */
    public static int candy(int[] ratings) {
        // 第0个位置先给1颗糖
        int candy = 1;

        // 当前遍历到的位置
        int index = 1;
        // 下个波谷位置
        int nextMin = 0;

        // 当前位置要想比左边糖多,最少要几颗糖
        int minForMoreThanLeft = 1;
        // 当前位置要想比右边糖多,最少要几颗糖
        int minForMoreThanRight = 0;

        while (index < ratings.length) {
            if (ratings[index] > ratings[index - 1]) {
                // 当前在上坡,当前位置要保证比左边多所需要的糖数++,然后累加到总数中
                candy += ++minForMoreThanLeft;
                index++;
            } else if (ratings[index] < ratings[index - 1]) {
                // 当前在下坡,一股脑求出一整个下坡区间的糖数
                // 找到波谷位置
                nextMin = nextMin(ratings, index);
                // 求从波峰到波谷整个下坡上的糖数,累加到总数上
                candy += incrementalSum(nextMin - index + 2);
                // 波峰位置加了两遍,上坡的时候也加了,现在要减去一遍,减去小的,留下大的才能保证波峰的糖比左右都多
                minForMoreThanRight = nextMin - index + 2;
                candy -= Math.min(minForMoreThanLeft, minForMoreThanRight);
                // index来到波谷的下一个位置
                index = nextMin + 1;
                minForMoreThanLeft = 1;
            } else {
                // 当前在平坡,当前位置的糖数为1
                candy += 1;
                minForMoreThanLeft = 1;
                index++;
            }
        }
        return candy;
    }

    /**
     * 找到start右边第一个波谷
     */
    public static int nextMin(int[] arr, int start) {
        for (; start < arr.length - 1; start++) {
            if (arr[start + 1] >= arr[start]) {
                return start;
            }
        }
        return arr.length - 1;
    }

    /**
     * 求size的递增和,也就是从1一直加到size
     */
    public static int incrementalSum(int size) {
        return ((1 + size) * size) >> 1;
    }


    public static void main(String[] args) {
        int[] ratings = {1, 5, 5, 4, 3, 2, 1};
        System.out.println(candy(ratings));
        System.out.println(candyPlus(ratings));
        System.out.println(candyPlus2(ratings));
    }

    /**
     * 进阶,相邻分数相等的孩子必须糖数一样
     * 用一个辅助数组
     */
    public static int candyPlus(int[] ratings) {
        int[] help = new int[ratings.length];
        help[0] = 1;
        for (int i = 1; i < ratings.length; i++) {
            if (ratings[i] > ratings[i - 1]) {
                help[i] = help[i - 1] + 1;
            } else if (ratings[i] == ratings[i - 1]) {
                help[i] = help[i - 1];
            } else {
                help[i] = 1;
            }
        }
        int candy = help[ratings.length - 1];
        for (int i = ratings.length - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                help[i] = Math.max(help[i + 1] + 1, help[i]);
            } else if (ratings[i] == ratings[i + 1]) {
                help[i] = help[i + 1];
            }
            candy += help[i];
        }

        return candy;
    }

    /**
     * 进阶,相邻分数相等的孩子必须糖数一样
     * 不用辅助数组
     */
    public static int candyPlus2(int[] ratings) {
        int candy = 1;
        int index = 1;
        int minForMoreThanLeft = 1;
        int same = 1;
        while (index < ratings.length) {
            if (ratings[index] > ratings[index - 1]) {
                candy += ++minForMoreThanLeft;
                same = 1;
                index++;
            } else if (ratings[index] < ratings[index - 1]) {
                int nextMin = nextMin(ratings, index);
                // 只累加纯下坡糖数,不累加波峰的糖数
                candy += incrementalSum(nextMin - index + 1);
                // 如果从右边看波峰的糖数要大于波峰当前的糖数,那么波峰有几个相等的,就补充几个差值
                int minForMoreThanRight = nextMin - index + 2;
                if (minForMoreThanRight > minForMoreThanLeft) {
                    candy += same * (minForMoreThanRight - minForMoreThanLeft);
                }
                index = nextMin + 1;
                minForMoreThanLeft = 1;
            } else {
                // 当前在平坡,糖数和左边相等
                candy += minForMoreThanLeft;
                same++;
                index++;
            }
        }
        return candy;
    }

}
