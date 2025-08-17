package shuatiban;

import java.util.Arrays;

/**
 * 生成长度为size的达标数组，什么叫达标？对于任意的i<k<j，满足[i]+[j]!=[k]*2。给定一个正数size，返回长度为size的达标数组
 *
 * 思路：
 * 易知，如果数组值a、b、c达标，那么2a、2b、2c也一定达标，并且2a+1、2b+1、2c+1也一定达标.
 * 进而,二者组合[2a、2b、2c、2a+1、2b+1、2c+1]也一定达标，
 * 因为如果全从左边取或者全从右边取，已知是达标的，如果跨两边取，左边全是偶数右边全是奇数，一个偶数+一个奇数不可能等于某个数*2
 * 所以要想生成size大小的达标数组，可以由一个大小为size/2（向上取整）的种子，经由*2和*2+1分别得到两个数组，然后拼成一个，删去多余的即可。
 * 而要想生成size/2大小的达标数组，又可以递归由大小为size/4的种子生成。。。
 * 递归的出口是，生成大小为1的达标数组，返回任意数字即可。
 * 分治+递归。
 */
public class Code027生成达标数组两边相加不等于中间乘二 {
    public static int[] process(int size) {
        if (size == 1) {
            return new int[]{1};
        }

        int[] seed = process((size + 1) >> 1);
        int[] result = new int[size];

        for (int i = 0; i < seed.length; i++) {
            result[i] = seed[i] << 1;
        }
        for (int i = seed.length; i < result.length; i++) {
            result[i] = (seed[i- seed.length] << 1) + 1;
        }
        return result;
    }

    public static void main(String[] args) {
        int size=3;
        System.out.println(Arrays.toString(process(size)));
    }
}
