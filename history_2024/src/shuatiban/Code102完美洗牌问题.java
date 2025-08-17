package shuatiban;

import java.util.Arrays;

/**
 * 完美洗牌问题
 * 给定一个长度为偶数的数组arr，假设长度为N*2
 * 左部分：arr[L1...Ln] 右部分： arr[R1...Rn]
 * 请把arr调整成arr[R1,L1,R2,L2,...,Rn,Ln]
 * 要求时间复杂度O(N)，额外空间复杂度O(1)
 * <p>
 * 前置知识：
 * 1、三反转
 * 给定一个数组，已知左半部分的长度，如何把数组左右两部分调换位置？要求空间复杂度O(1)
 * 例如，给定[l1,l2, r1,r2,r3]，返回[r1,r2,r3, l1,l2]
 * 解法：先把左半部分逆序[l2,l1, r1,r2,r3]，再把右半部分逆序[l2,l1, r3,r2,r1]，最后把整体逆序即可[r1,r2,r3, l1,l2]
 * 2、下标循环怼
 * 用于把一个数组排成特定的顺序，且每个元素排序后的位置可以用公式算出来的情况
 * 例如给定一个数组[L1...Ln, R1...Rn]，长度为2n，左右两部分长度都为n，如何排成[R1,L1,R2,L2,R3,L3,...,Rn,Ln]？要求空间复杂度O(1)
 * 具体例子[0,1,2,3]，n=2
 * 排序后是[2,0,3,1]
 * 解法：
 * 1）首先推导出新位置的计算公式
 * 左半部分计算新位置的公式是：i新=i旧*2+1，右半部分的公式是：i新=(i旧-n)*2。右边怎么来的：比方说右边第1个排序后就在左边第1个前一个，因此只要找到对应左边的位置，计算左边位置的新位置，再-1，就是右边排序后的位置。i旧-n是对应左边的位置，(i旧-n)*2+1是左边排序后的位置，(i旧-n)*2+1-1就是右边排序后的位置
 * 2）下标循环怼，怼出一个排一个
 * 首先从0（也就是l1）开始，0放到0*2+1=1位置，把原本1位置的1怼出来，1放到1*2+1=3位置，把原本3位置的3怼出来，3放到(3-2)*2=2位置，把原本2位置的2怼出来，2放到(2-2)*2=0位置，就排好了。
 * 问题：
 * 有时候一个循环并不会覆盖所有要排序的数，也就是说需要有多个循环。例如[0,1,2,3,4,5]，0怼出来放到1，1怼出来放到3，3怼出来放到0，然后循环结束了，但是剩下2、4、5还没排呢。
 * 解法：
 * 也就是本题的解法，下标循环怼+三反转。
 * <p>
 * 本题解法：
 * 规律（数论推理得到，它的证明不是我们该考虑的）：数组[L1...Ln, R1...Rn]长度为2n，如果 长度 = 3^k - 1，那么一共有k个循环，起点分别是0, 3^1-1, 3^2-1...3^(k-1)-1
 * 例如上面的例子长度为8，符合3^k - 1，k=2，那么一共有2个循环，起点分别是0，2
 * 根据这个结论，就可以把长度符合3^k-1的数组排好序。那么长度不符合的怎么办？
 * 解法就是，利用三反转，在数组左侧构造出长度符合3^k-1且最接近2n的子数组，先把左侧的子数组排好序，然后递归把右侧的排好序
 * 例如，数组[0,1,2,3,4,5,6,7,8,9]长度2n=10，最接近的3^k-1是8，要求左右两部分的长度是4，那么就把从4到8的部分做三反转（三反转的左部分长度为1）那么就得到
 * [0,1,2,3,5,6,7,8,4,9]，先把[0,1,2,3,5,6,7,8]用2次下标循环怼排好序，得到[5,0,6,1,7,2,8,3]，然后递归执行[4,9]
 */
public class Code102完美洗牌问题 {
    public static void main(String[] args) {
//        int[] arr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};
        int[] arr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        sortMain(arr);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 完美洗牌问题
     * 给定一个数组[L1...Ln, R1...Rn]，长度为2n，左右两部分长度都为n，将数组排成[R1,L1,R2,L2,R3,L3,...,Rn,Ln]
     * 要求时间复杂度O(N)，额外空间复杂度O(1)
     * <p>
     * 注：如果要排成[L1,R1,L2,R2,L3,R3,...,Ln,Rn]，只需要在最后遍历一遍数组，每2个位置两两交换即可
     */
    public static void sortMain(int[] arr) {
        if (arr != null && arr.length > 0 && arr.length % 2 == 0) {
            sort(arr, 0, arr.length - 1);
        }
    }

    private static void sort(int[] arr, int l, int r) {
        // 递归出口，如果待排序范围为空，直接退出
        if (l >= r) {
            return;
        }
        // 找出符合3^k-1且最接近length的长度
        int k = 1;
        int base = 3;
        while (base * 3 <= r - l + 1 + 1) {
            base *= 3;
            k++;
        }
        base--;
        // 三反转构造长度为base的子数组
        reverse3(arr, l + base / 2, (l + r) / 2 + base / 2, (l + r) / 2 - l - base / 2 + 1);
        // 在子数组上执行下标循环怼
        cycle(arr, l, l + base - 1, k);
        // 递归解决右侧剩下的
        sort(arr, l + base, r);
    }

    /**
     * 三反转
     */
    private static void reverse3(int[] arr, int l, int r, int leftLength) {
        reverse(arr, l, l + leftLength - 1);
        reverse(arr, l + leftLength, r);
        reverse(arr, l, r);
    }

    /**
     * 数组arr从l到r范围逆序
     *
     * @param arr
     * @param l
     * @param r
     */
    private static void reverse(int[] arr, int l, int r) {
        while (l < r) {
            swap(arr, l++, r--);
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * 下标循环怼
     * 数组arr从l到r的子数组要按要求排序，子数组长度为3^k-1
     */
    private static void cycle(int[] arr, int l, int r, int k) {
        // start是子数组中每次循环怼的开始位置，0,2,8,26...注意start是在子数组中的相对位置，而在arr中的真实位置是l+start
        for (int i = 0, start = 0; i < k; i++, start = (start + 1) * 3 - 1) {
            // 旧位置的值
            int oldValue = arr[l + start];
            // 要把旧值怼到哪个新位置
            int newIndex = l + getNewIndex(start, r - l + 1);
            // 只要没有回到本次循环怼的起点，就一直循环怼
            while (newIndex != l + start) {
                int temp = arr[newIndex];
                // 把旧值怼到新位置
                arr[newIndex] = oldValue;
                // 把新位置的值怼出来，准备继续怼到下一个位置
                oldValue = temp;
                // 计算下一个位置
                newIndex = l + getNewIndex(newIndex - l, r - l + 1);
            }
            // 因为newIndex=起点的时候就退出循环了，这时还没有给起点赋值，这里单独赋值
            arr[newIndex] = oldValue;
        }
    }

    /**
     * 根据旧下标计算新下标
     * 旧下标=i旧，待排序子数组长度=2n
     * 如果旧下标在左半部分，那么i新=i旧*2+1
     * 如果旧下标在右半部分，那么i新=(i旧-n)*2+1-1
     */
    private static int getNewIndex(int i, int length) {
        return i < length / 2 ? i * 2 + 1 : (i - length / 2) * 2;
    }
}
