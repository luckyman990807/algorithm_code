package dynamic_programing.SimplifyOutDependency;

/**
 * https://leetcode.cn/problems/remove-boxes/
 * 移除盒子
 * 题意简单概括：给定一个正数数组arr，连续相同的数可以消掉，得分=消掉个数的平方。求消掉所有数的最大得分
 * 
 */
public class Class031RemoveBoxes {
    /**
     * 阶段一解法：暴力递归
     * @param boxes
     * @return
     */
    public static int force(int[] boxes) {
        return process(boxes, 0, boxes.length - 1, 0);
    }

    /**
     * 递归尝试策略：求arr从l到r范围上全部消掉的最大得分，有个潜台词是l前面紧跟着pre个arr[l]作为前缀
     * @param arr
     * @param l
     * @param r
     * @param pre
     * @return
     */
    private static int process(int[] arr, int l, int r, int pre) {
        if (l == r) {
            return (pre + 1) * (pre + 1);
        }
        if (l > r) {
            return 0;
        }

        // 可能性展开：前缀是否要继续累积？
        int max = Integer.MIN_VALUE;
        // 第一种可能性：前缀不再继续累积，直接和l一起消掉（没有必要自己消掉，起码要拉上l一块）
        max = Math.max(max, (pre + 1) * (pre + 1) + process(arr, l + 1, r, 0));
        // 其他可能性：前缀要继续累积
        for (int i = l + 1; i <= r; i++) {
            if (arr[i] != arr[l]) {
                continue;
            }
            // 可能性i：把i合并到前缀中（pre，l，i），整体作为前缀去处理i+1后面的。l+1到i-1没有前缀单独处理。
            max = Math.max(max, process(arr, l + 1, i - 1, 0) + process(arr, i, r, pre + 1));
        }

        return max;
    }


    /**
     * 阶段二解法：暴力递归剪枝
     * @param boxes
     * @return
     */
    public static int forceOpt(int[] boxes) {
        return processOpt(boxes, 0, boxes.length - 1, 0);
    }

    public static int processOpt(int[] arr, int l, int r, int pre) {
        if (l > r) {
            return 0;
        }
        if (l == r) {
            return (pre + 1) * (pre + 1);
        }

        // 剪枝，如果从l开始有连续的arr[l]，那么直接并入前缀中是最优的处理，没有必要递归处理。
        while (l + 1 <= r && arr[l + 1] == arr[l]) {
            l++;
            pre++;
        }

        int max = (pre + 1) * (pre + 1) + processOpt(arr, l + 1, r, 0);

        for (int i = l + 1; i <= r; i++) {
            if (arr[i] != arr[l]) {
                continue;
            }

            max = Math.max(max, processOpt(arr, i, r, pre + 1) + processOpt(arr, l + 1, i - 1, 0));

            // 剪枝，如果从i开始有连续的arr[i]，那么只处理第一个即可，处理第一个引出的分支会有一条分支把连续的arr[i]都并入前缀，这是最优处理。而处理第二个意味着把第一个单独消掉，然后把第二个并入前缀，没有必要白白浪费掉一个。
            while (i + 1 <= r && arr[i + 1] == arr[l]) {
                i++;
            }
        }

        return max;
    }


    /**
     * 阶段三解法：缓存法动态规划
     * @param boxes
     * @return
     */
    public static int cache(int[] boxes) {
        int[][][] cache = new int[boxes.length][boxes.length][boxes.length];
        return processCache(boxes, 0, boxes.length - 1, 0, cache);
    }

    public static int processCache(int[] arr, int l, int r, int pre, int[][][] cache) {
        if (l > r) {
            return 0;
        }
        if (cache[l][r][pre] != 0) {
            return cache[l][r][pre];
        }
        int result;
        if (l == r) {
            result = (pre + 1) * (pre + 1);
        } else {
            // 现在前缀有pre+1个arr[l]

            // 如果l后面有连续的arr[l]，那么不用每次都递归尝试，直接把连续的arr[l]累积到前缀中，得到新的l和pre
            int newL = l;
            int newPre = pre;
            while (newL + 1 <= r && arr[newL + 1] == arr[l]) {
                newL++;
                newPre++;
            }
            // 此时前缀有newPre+1个arr[newL]，newL位置并入到前缀中

            // 可能性1:前缀不再继续累积，直接消掉
            result = (newPre + 1) * (newPre + 1) + processCache(arr, newL + 1, r, 0, cache);
            // 其他可能性：前缀继续累积
            for (int i = newL + 1; i <= r; i++) {
                // 如果后面出现了连续的arr[l]，那么只处理第一个。为什么：剪枝，处理第二个意味着把第一个单独消掉，然后把第二个累积到前缀中，这样白白浪费了一个，没有必要。
                if (arr[i] != arr[l] || arr[i - 1] == arr[l]) {
                    continue;
                }
                // 可能性i:递归计算从i到r全部消掉的最大积分，i前面紧跟着newPre+1个arr[i]；从newL+1到i-1这段位置单独处理，前面没有前缀。
                result = Math.max(result, processCache(arr, i, r, newPre + 1, cache) + processCache(arr, newL + 1, i - 1, 0, cache));
            }
        }
        cache[l][r][pre] = result;
        return result;
    }
}
