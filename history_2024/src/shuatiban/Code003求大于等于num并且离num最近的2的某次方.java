package shuatiban;

/**
 * 给定一个非负整数num，如何不用循环语句，返回>=num，并且离num最近的，2的某次方
 *
 * 思路:
 * 先把num-1,再把num从最高位的1开始后面全填满1,最后+1,就是答案. 如果num恰好是n的某次方,也能返回num自己.
 * hashmap扩容的源码就是这么实现的.
 */
public class Code003求大于等于num并且离num最近的2的某次方 {
    public static int getNear2Power(int num) {
        num--;
        num |= num >>> 1;
        num |= num >>> 2;
        num |= num >>> 4;
        num |= num >>> 8;
        // int就32位,最差情况右移16就能填满
        num |= num >>> 16;

        num++;

        // 考虑了负数的情况,负数第一位是1,填满后是32个1,+1是0.而负数离他最近的2的某次方是1.
        return num == 0 ? 1 : num;
    }

    public static void main(String[] args) {
        System.out.println(getNear2Power(8));
    }
}
