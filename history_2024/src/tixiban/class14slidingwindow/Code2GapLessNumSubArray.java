package tixiban.class14slidingwindow;

import java.util.LinkedList;

/**
 * 给定一个数组arr，和一个整数gap，arr中某个子数组sub，如果满足sub中max-sub中min<=gap，那么认为sub达标。
 * 返回arr中达标的子数组的个数
 */
public class Code2GapLessNumSubArray {
    public static int getSubs(int[] arr, int gap){
        if(arr==null||arr.length==0||gap<0){
            return 0;
        }

        // 记录返回结果，达标的个数
        int count=0;
        // 记录每次滑入/滑出的最大值和最小值
        LinkedList<Integer> maxQueue=new LinkedList<>();
        LinkedList<Integer> minQueue = new LinkedList<>();

        // 滑动窗口的右边界，这道题我们让滑动窗口的范围是[l, r)，不包括r位置
        int r=0;
        // 左右边界都从0开始，右边界先往右滑，直到再滑max-min就超过gap了，就停下，然后以l开头的达标子数组个数就是r-l
        // 然后l往右缩一个，右边界再往右扩，扩到扩不动，再计算l开头的子数组个数……

        // 滑动窗口的左边界l逐位往右缩
        for(int l=0;l<arr.length;l++){
            // r往右扩，探索滑动窗口在左边界=l的前提下的最大右边界
            while (r<arr.length){
                // 滑动窗口每滑入一个，就要往双端队列里塞一个，塞之前把没用的删掉
                while (!maxQueue.isEmpty()&&!(arr[maxQueue.peekLast()]>arr[r])){
                    maxQueue.pollLast();
                }
                maxQueue.addLast(r);

                while (!minQueue.isEmpty() &&!(arr[minQueue.peekLast()]<arr[r])){
                    minQueue.pollLast();
                }
                minQueue.addLast(r);

                // 直到再滑就不达标了，break
                if(arr[maxQueue.peekFirst()]-arr[minQueue.peekFirst()]>gap){
                    break;
                }

                // 滑动窗口右边界++
                r++;
            }

            // 左边界为l的前提下，滑动窗口的右边界最大到r，窗口内的个数是r-l，也就是达标的个数
            count+=r-l;

            // 马上要l++了，l++意味着左端滑出，判断双端队列里的最大值或最小值是不是左边界，如果是，那么删除
            if(maxQueue.peekFirst()==l){
                maxQueue.pollFirst();
            }

            if(minQueue.peekFirst()==l){
                minQueue.pollFirst();
            }
        }

        return count;
    }

    public static void main(String[] args) {
        int[] arr={0,1,2,3,4};
        int gap=3;
        System.out.println(getSubs(arr, gap));
    }
}
