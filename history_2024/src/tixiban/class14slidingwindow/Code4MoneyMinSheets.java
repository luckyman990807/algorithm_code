package tixiban.class14slidingwindow;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 找钱最少张数问题
 * arr是货币数组，每个值都是正数，允许重复。再给定一个正数aim。
 * arr每个值都认为是一张独一无二的货币，返回组成aim的最小张数。
 */
public class Code4MoneyMinSheets {
    /**
     * 动态规划1、每张货币看成独一无二的一张，复杂度O(n*aim)
     *
     * @param arr
     * @param aim
     * @return
     */
    public static int dpSingleSheet(int[] arr, int aim) {
        int n = arr.length;
        int[][] dp = new int[n + 1][aim + 1];

        // base case，当index==n，也就是没有货币可取的时候，只有目标金额aim=0，才表示需要0张就能凑齐，否则目标!=0又没钱了，永远都凑不齐
        dp[n][0] = 0;
        for (int rest = 1; rest <= aim; rest++) {
            dp[n][rest] = Integer.MAX_VALUE;
        }

        for (int index = n - 1; index >= 0; index--) {
            for (int rest = 0; rest <= aim; rest++) {
                // 可能性1、不要当前货币，index跳到下一个，目标金额不变
                int p1 = dp[index + 1][rest];
                // 可能性2、要当前货币，张数+1，index跳到下一个，目标金额减去当前面额
                int next = rest - arr[index] >= 0 ? dp[index + 1][rest - arr[index]] : Integer.MAX_VALUE;
                int p2 = next == Integer.MAX_VALUE ? Integer.MAX_VALUE : next + 1;
                // 选最小的那个
                dp[index][rest] = Math.min(p1, p2);
            }
        }

        return dp[0][aim];
    }

    /**
     * 动态规划2、记录每种面额的货币的张数，复杂度 O(n) + O(货币种数 * aim * 每种平均张数)
     *
     * @param arr
     * @param aim
     * @return
     */
    public static int dpAmountAndCount(int[] arr, int aim) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int value : arr) {
            if (map.containsKey(value)) {
                map.put(value, map.get(value) + 1);
            } else {
                map.put(value, 1);
            }
        }
        int n = map.size();
        int[] amount = new int[n];
        int[] count = new int[n];
        int i = 0;
        for (int key : map.keySet()) {
            amount[i] = key;
            count[i++] = map.get(key);
        }

        int[][] dp = new int[n + 1][aim + 1];
        dp[n][0] = 0;
        for (int rest = 1; rest <= aim; rest++) {
            dp[n][rest] = Integer.MAX_VALUE;
        }


        for (int index = n - 1; index >= 0; index--) {
            for (int rest = 0; rest <= aim; rest++) {
                int min = Integer.MAX_VALUE;
                // index是当前货币下标，rest是当前还剩多少目标金额，c是当前货币张数
                // 动态规划，当前货币使用0张、1张、2张...分别求搞定剩下的金额需要几张，取最小值
                // 忽略无效数据，例如Integer.MAX_VALUE不能再加当前张数了
                for (int c = 0; c <= count[index] && c * amount[index] <= rest; c++) {
                    if (rest - c * amount[index] >= 0 && dp[index + 1][rest - c * amount[index]] != Integer.MAX_VALUE) {
                        min = Math.min(min, c + dp[index + 1][rest - c * amount[index]]);
                    }
                }
                dp[index][rest] = min;
            }
        }
        return dp[0][aim];
    }

    public static int dpWindow(int[] arr, int aim) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int value : arr) {
            if (map.containsKey(value)) {
                map.put(value, map.get(value) + 1);
            } else {
                map.put(value, 1);
            }
        }
        int n = map.size();
        int[] amount = new int[n];
        int[] count = new int[n];
        int i = 0;
        for (int key : map.keySet()) {
            amount[i] = key;
            count[i++] = map.get(key);
        }

        int[][] dp = new int[n + 1][aim + 1];
        dp[n][0] = 0;
        for (int rest = 1; rest <= aim; rest++) {
            dp[n][rest] = Integer.MAX_VALUE;
        }

        for (int index = n - 1; index >= 0; index--) {
            // 当前货币面额amount[index]=a，那么可以把dp表aim维度分成几组，分别是[0,0+a,0+2*a...]，[1,1+a,1+2*a...]...mod是当前组号，也就是每组开头的元素
            // mod <= aim是因为，当前面额为100，剩余目标金额为30，那么mod没必要一直++
            // mod < amount[index]是因为，mod只要取=0、1...a-1，那么[0,0+a,0+2*a...]，[1,1+a,1+2*a...]...就能把所有aim维度数据覆盖
            for (int mod = 0; mod <= aim && mod < amount[index]; mod++) {
                LinkedList<Integer> minQueue = new LinkedList<>();

                for (int rest = mod; rest <= aim; rest += amount[index]) {
                    // 根据之前学的动态规划for枚举可能性改位置依赖，假设当前面额为a，a有2张，那么
                    // dp[i][r] = min(dp[i+1][r]+0, dp[i+1][r-a]+1, dp[i+1][r-2*a]+2)
                    // dp[i][r-a] = min(dp[i+1][r-a]+0, dp[i+1][r-2*a]+1, dp[i+1][r-3*a]+2);
                    // 这里是min，不是之前的累加和，min无法直接直接通过dp[i][r-a]求dp[i][r]

                    // 解法：滑动窗口记录最小值
                    // 先把dp[i+1][r-2*a]滑入，当滑入dp[i+1][r-a]之前，先比较dp[i+1][r-a]和dp[i+1][r-2*a]+1的大小，如果后者不比前者小就删除。
                    // 为什么dp[i+1][r-2*a]要+1？因为dp[i+1][r-a]比dp[i+1][r-2*a]多用了1张a面额，后者+1才是实际的张数。
                    // 窗口里的last和当前要滑入的相差几张，last就+几

                    // 右端滑入，删除无用数据
                    while (!minQueue.isEmpty() &&

                            !(dp[index + 1][minQueue.peekLast()] != Integer.MAX_VALUE
                                    && dp[index + 1][minQueue.peekLast()] + (rest - minQueue.peekLast()) / amount[index] < dp[index + 1][rest])){

                        minQueue.pollLast();
                    }
                    minQueue.addLast(rest);

                    // dp[i+1][r]滑入后，早滑入的dp[i+1][r-3*a]滑出，因为当前a面额只有2张
                    if(minQueue.peekFirst()==rest-(count[index]+1)*amount[index]){
                        minQueue.pollFirst();
                    }

                    // 记录dp表从index货币开始，搞定rest金额的最小张数，同理也要加上相差的当前a面额张数
                    dp[index][rest]=dp[index+1][minQueue.peekFirst()]+(rest-minQueue.peekFirst())/amount[index];
                }
            }
        }
        return dp[0][aim];
    }

    public static void main(String[] args) {
        int[] arr={1,1,1,1,1,2,2,2,2,2,5,5,5,5,5,10,10,10,10,10,20,20,20,20,20};
        int aim=90;
        System.out.println(dpSingleSheet(arr, aim));
        System.out.println(dpAmountAndCount(arr, aim));
        System.out.println(dpWindow(arr, aim));

    }
}
