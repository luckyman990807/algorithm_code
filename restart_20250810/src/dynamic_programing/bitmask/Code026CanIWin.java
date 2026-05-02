package dynamic_programing.bitmask;

/**
 * https://leetcode.cn/problems/can-i-win/description/
 * 给定两个整数 maxChoosableInteger （整数池中可选择的最大数）和 desiredTotal（累计和），
 * 两个玩家可以轮流从公共整数池中抽取从 1 到 maxChoosableInteger 的整数（不放回），直到累计整数和 >= desiredTotal，
 * 先使得累计整数和 达到或超过 desiredTotal 的玩家，即为胜者，
 * 若先出手的玩家能赢，返回true，否则返回false
 * 
 * 取值范围：
 * 1 <= maxChoosableInteger <= 20
 * 0 <= desiredTotal <= 300
 * 
 * 状态压缩的动态规划
 * bitmask：二进制位掩码，用一个整数的二进制的每一位，表示一个状态是否有效/是否被使用过
 */
public class Code026CanIWin {
    /**
     * 阶段一解法：暴力递归
     * 提交到leetcode上会超时
     * 
     * @param maxChoosableInteger
     * @param desiredTotal
     * @return
     */
    public static boolean force(int maxChoosableInteger, int desiredTotal) {
        // 题目规定，如果最开始total就是0，那么先手赢
        if (desiredTotal <= 0) {
            return true;
        }
        // 如果所有数加在一起都达不到total，那么先手无论如何也不可能赢。排除掉这个场景后，就不存在双输的场景了，要么先手赢，要么后手赢，就看谁先达到total
        if ((1 + maxChoosableInteger) * maxChoosableInteger / 2 < desiredTotal) {
            return false;
        }

        // 用一个数组来存放所有可抽取的数
        int[] arr = new int[maxChoosableInteger];
        for (int i = 0; i < maxChoosableInteger; i++) {
            arr[i] = i + 1;
        }
        // 递归
        return process(arr, desiredTotal);
    }

    /**
     * 递归函数
     * 可抽取的数存在arr，目标累计和为total，返回当前玩家是否能赢
     * 
     * @param arr
     * @param total
     * @return
     */
    private static boolean process(int[] arr, int total) {
        // 如果目标累计和已经<=0了，说明上一个玩家已经凑齐累计和了，那么当前玩家输了
        if (total <= 0) {
            return false;
        }

        // 当前玩家遍历尝试每一个可抽取的数（值为-1表示不可抽取，否则表示可抽取），把剩下的数以及目标累计和交给下一个玩家，如果下一个玩家会输，那么当前玩家就赢
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == -1) {
                continue;
            }
            int tmp = arr[i];
            arr[i] = -1;
            boolean result = !process(arr, total - tmp);
            // 深度优先遍历恢复现场
            arr[i] = tmp;
            // 只要发现一种能赢的取法，就返回
            if (result) {
                return result;
            }
        }
        // 没有发现任何能赢的取法，那么当前玩家输
        return false;
    }

    /**
     * 阶段二解法：傻缓存法
     * 
     * 想缓存就得解决一个问题：其中一个可变参数是数组，数组怎么作为缓存的key？
     * 答案是用一个整数的位信息代替数组的效果，题目规定取值范围1 <= maxChoosableInteger <= 20，而int有32位，完全可以cover
     * 
     * 提交到leetcode上会内存超限制，二维数组缓存超限制了。
     */
    public static boolean forceDP(int maxChoosableInteger, int desiredTotal) {
        // 题目规定，如果最开始total就是0，那么先手赢
        if (desiredTotal <= 0) {
            return true;
        }
        // 如果所有数加在一起都达不到total，那么先手无论如何也不可能赢。排除掉这个场景后，就不存在双输的场景了，要么先手赢，要么后手赢，就看谁先达到total
        if ((1 + maxChoosableInteger) * maxChoosableInteger / 2 < desiredTotal) {
            return false;
        }

        // 用一个整数的位信息来存储每个数是否已被抽取，第n位为1表示n已经被抽取过了
        int status = 0;
        // 用二维数组来做缓存，key是两个可变参数的组合。status的取值范围是2的maxChoosableInteger次方，因为有几个供抽取的数，status就要用到几位
        int[][] cache = new int[1 << maxChoosableInteger][desiredTotal + 1];
        // 递归
        return processDp(maxChoosableInteger, status, desiredTotal, cache);
    }

    private static boolean processDp(int num, int status, int total, int[][] cache) {
        // 如果目标累计和已经<=0了，说明上一个玩家已经凑齐累计和了，那么当前玩家输了
        if (total <= 0) {
            return false;
        }

        // 如果有缓存，直接走缓存。0:没有缓存，1:true，2:false
        if (cache[status][total] != 0) {
            return cache[status][total] == 1;
        }

        for (int i = 0; i < num; i++) {
            // 如果第i个数已经被抽取过了，那么直接跳过
            if ((status & (1 << i)) != 0) {
                continue;
            }
            // 深度优先遍历保存现场
            int tmp = status;
            // 抽取第i个数，把剩下的数以及剩下的total交给另一个玩家，如果另一个玩家输，那么当前玩家赢。
            status |= (1 << i);
            boolean win = !processDp(num, status, total - i - 1, cache);
            // 深度优先遍历恢复现场
            status = tmp;
            // 只要发现一种能赢的取法，就返回
            if (win) {
                cache[status][total] = 1;
                return win;
            }
        }
        // 没有发现任何能赢的取法，那么当前玩家输
        cache[status][total] = 2;
        return false;
    }

    /**
     * 解法第三阶段：一维傻缓存法
     * 观察发现对于processDp这个函数来说，影响函数结果的可变参数就只有status，因为total是可以根据status计算出来的，用desiredTotal减去status为1的位对应的数即可。
     * 所以缓存可以只以status为key，一维数组即可。
     * 
     * 提交leetcode直接过
     * 
     * @param maxChoosableInteger
     * @param desiredTotal
     * @return
     */
    public static boolean forceDP1(int maxChoosableInteger, int desiredTotal) {
        // 题目规定，如果最开始total就是0，那么先手赢
        if (desiredTotal <= 0) {
            return true;
        }
        // 如果所有数加在一起都达不到total，那么谁也赢不了。排除掉这个场景后，就不存在双输的场景了，要么先手赢，要么后手赢，就看谁先达到total
        if ((((1 + maxChoosableInteger) * maxChoosableInteger) >> 1) < desiredTotal) {
            return false;
        }

        // 用一个整数的0～maxChoosableInteger-1位来表示1～maxChoosableInteger是否已被抽取，第i位为1表示i+1这个数已经被抽取过了
        int status = 0;
        // 用一维数组来缓存某个status的计算结果
        int[] cache = new int[1 << maxChoosableInteger];
        // 递归
        return processDp1(maxChoosableInteger, status, desiredTotal, cache);
    }

    private static boolean processDp1(int num, int status, int total, int[] cache) {
        // 如果有缓存，直接走缓存。0:没有缓存，1:true，2:false
        if (cache[status] != 0) {
            return cache[status] == 1;
        }
        // 如果目标累计和已经<=0了，说明上一个玩家已经凑齐累计和了，那么当前玩家输了
        if (total <= 0) {
            cache[status] = 2;
            return false;
        }

        for (int i = 0; i < num; i++) {
            // 当前玩家尝试抽取i+1这个数
            // 如果i+1这个数已经被抽取过了，那么直接跳过
            if ((status & (1 << i)) != 0) {
                continue;
            }
            // 深度优先遍历保存现场
            int tmp = status;
            // 当前玩家抽取i+1这个数，把剩下的数和剩下的total留给另一个玩家，如果另一个玩家赢输，那当前玩家赢
            status |= (1 << i);
            boolean win = !processDp1(num, status, total - i - 1, cache);
            // 深度优先遍历恢复现场
            status = tmp;
            // 只要发现一种能赢的取法，就返回能赢
            if (win) {
                cache[status] = 1;
                return win;
            }
        }
        // 找不到任何能赢的取法，返回当前玩家输
        cache[status] = 2;
        return false;
    }
}
