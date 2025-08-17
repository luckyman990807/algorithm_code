package shuatiban;

/**
 * https://leetcode.com/problems/median-of-two-sorted-arrays/
 * 给定两个大小分别为 m 和 n 的正序（从小到大）数组 nums1 和 nums2。请你找出并返回这两个正序数组的 中位数 。
 * 算法的时间复杂度应该为 O(log (m+n)) 。
 */
public class Code062在两个等长有序数组中找到上中位数 {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int total = nums1.length + nums2.length;
        // 如果两个数组都为空，返回0
        if (total == 0) {
            return 0;
        }
        // 如果有一个数组为空，返回另一个数组的中位数
        if (nums1.length == 0) {
            return (total & 1) == 1 ? nums2[total >> 1] : (double) (nums2[(total >> 1) - 1] + nums2[total >> 1]) / 2;
        }
        if (nums2.length == 0) {
            return (total & 1) == 1 ? nums1[total >> 1] : (double) (nums1[(total >> 1) - 1] + nums1[total >> 1]) / 2;
        }
        // 两个数组都不为空，求中位数
        if ((total & 1) == 1) {
            return getKth(nums1, nums2, (total >> 1) + 1);
        } else {
            return (double) (getKth(nums1, nums2, total >> 1) + getKth(nums1, nums2, (total >> 1) + 1)) / 2;
        }
    }

    /**
     * 求有序数组arr1和arr2中整体第k小的数
     */
    public int getKth(int[] arr1, int[] arr2, int k) {
        // 把arr1和arr2区分为长的和短的
        int[] shorter = arr1.length < arr2.length ? arr1 : arr2;
        int[] longer = arr1 == shorter ? arr2 : arr1;

        // 可能性1、k <= short
        if (k <= shorter.length) {
            // 直接短的里面取前k个，长的里面取前k个，求整体的上中位数
            return getUpMid(arr1, 0, k - 1, arr2, 0, k - 1);
        }
        // 可能性2、short < k <= long
        if (k <= longer.length) {
            // 长的掐头去尾，多淘汰一个，剩下的求整体的上中位数
            // 例如长的[1,2,3,4,5,6,7,8]，短的[1,2,3,4]（数值仅代表第几小，不代表真实值），k=6
            // 首先把长[1]淘汰掉，因为长[1]最靠后的情况就是长[1]大于短[4]时,长[1]排第5，永远不可能排第6
            // 其次把长[7,8]淘汰掉，因为他俩最靠前的情况就是长[8]<短[1]时，长[7,8]分别排第7、8，永远不可能排第6
            // 于是剩下长[2,3,4,5,6]，短[1,2,3,4]，发现长的多一个，没法求整体的上中位数
            // 这时就手动检查一下长[2]，如果长[2]>=短[4]，那么长[2]就是排第6，直接返回；否则淘汰长[2]，长[3,4,5,6]短[1,2,3,4]整体求中位数返回
            // 为什么要淘汰长[2]？
            // 1、能让两数组剩下的一样长，套用求整体上中位数。
            // 2、淘汰长[2]之后，前面就淘汰了2个，剩下整体的中位数排第4，加起来正好是第6。如果淘汰别的位置就达不到这个效果了。
            if (longer[k - shorter.length - 1] >= shorter[shorter.length - 1]) {
                return longer[k - shorter.length - 1];
            }
            return getUpMid(shorter, 0, shorter.length - 1, longer, k - shorter.length, k - 1);
        }
        // 可能性3、k > long
        // 长的掐头，短的掐头，分别多淘汰一个，剩下的求整体的上中位数
        // 例如长的[1,2,3,4,5,6,7,8]，短的[1,2,3,4]（数值仅代表第几小，不代表真实值），k=10
        // 首先把长[1,2,3,4,5]淘汰，因为他们最靠后的情况就是长[1]>短[4]时，长[1]排第5，长[5]排第9，永远不可能排第10
        // 其次把短[1]淘汰，因为他最靠后的情况就是短[1]>长[8]时，短[1]排第9，永远到不了第10
        // 于是剩下长[6,7,8]，短[2,3,4]，直接求整体上中位数吗？不行，因为前面只淘汰了6个，剩下整体的中位数是排第3，加起来排第9，没到第10
        // 这时手动检查一下长[6]和短[2]：
        // 如果长[6]>短[4]，说明长[6]就是排第10，直接返回长[6]。（长[6]本身大于长的5个，现在又大于短的4个，总共大于9个，排第10）
        // 如果短[2]>长[8]，说明短[2]就是排第10，直接返回短[2]。（短[2]本身大于短的1个，现在又大于长的8个，总共大于9个，排第10）
        // 否则淘汰长[6]和短[2]，剩下长[7,8]短[3,4]求整体上中位数返回。前面淘汰了总共8个，剩下的上中位数排第2，加起来正好第10
        if (longer[k - shorter.length - 1] >= shorter[shorter.length - 1]) {
            return longer[k - shorter.length - 1];
        }
        if (shorter[k - longer.length - 1] >= longer[longer.length - 1]) {
            return shorter[k - longer.length - 1];
        }
        return getUpMid(shorter, k - longer.length, shorter.length - 1, longer, k - shorter.length, longer.length - 1);
    }

    /**
     * 从有序数组arr1的从start1到end1，和有序数组arr2的从start2到end2中，找到整体的上中位数
     * start1到end1和start2到end2等长
     */
    public int getUpMid(int[] arr1, int start1, int end1, int[] arr2, int start2, int end2) {
        // 只要每个数组的长度都大于等于2，就不停二分
        while (start1 < end1) {
            // 二分中点，也就是每个数组的上中点
            int mid1 = start1 + ((end1 - start1) >> 1);
            int mid2 = start2 + ((end2 - start2) >> 1);
            if (arr1[mid1] == arr2[mid2]) {
                // 如果两个数组的上中位数相同，那么这就是整体的上中位数，直接返回这个数
                return arr1[mid1];
            }
            if (arr1[mid1] > arr2[mid2]) {
                // 如果arr1的上中位数较大

                // 可能性1、数组长度为偶数，那么arr1去尾，arr2掐头，剩下的递归求整体上中位数
                // 例如arr1[1,2,3,4]，arr2[1',2',3',4']，求上中位数也就是整体第4个
                // 因为arr1[2]>arr2[2']，所以arr1[3]最靠前的情况就是arr1[3]<arr2[3']，整体排第5（大于arr1中的2个，大于arr2中的2个），永远到不了第4，所以淘汰。arr1[3后面的]更大，更淘汰。
                // 另外arr2[2']最靠后的情况就是>arr1[1]，整体排第3（大于arr2中的1个，大于arr1中的1个），永远到不了第4，所以淘汰。arr2[2'前面的]更小，更淘汰。
                // 于是剩下arr1[1,2]arr2[3',4']，求整体中位数也就是第2个，就是原数组整体的第4个，因为掐头掐了2个，剩下的第2个加起来正好是第4个

                // 可能性2、数组长度为奇数，那么arr1去尾，arr2掐头，arr2多淘汰一个，剩下的递归求整体上中位数
                // 例如arr1[1,2,3,4,5]，arr2[1',2',3',4',5']，求上中位数也就是整体第5个
                // 因为arr1[3]>arr2[3']，所以arr1[3]最靠前的情况是arr1[3]<arr2[4']，整体排第5（arr1里大于2个，arr2里又大于3个），永远到不了第5，所以淘汰。而arr1[3后面的]更大，更淘汰。
                // 另外arr2[2']最靠后的情况就是>arr1[2]，整体排第4（大于arr2中的1个，大于arr1中的2个），永远到不了第5，所以淘汰。而arr2[2'前面的]更小，更淘汰。
                // 于是剩下arr1[1,2]arr2[3',4',5']，发现长度不一样，没法递归求整体的上中位数
                // 这时手动检查一下arr2[3']，
                // 如果arr2[3']>arr1[2]，那么arr2[3']就是整体第5个（大于arr2中的2个，又大于arr1中的2个），也就是上中位数，直接返回
                // 否则arr2[3']就不是上中位数，淘汰，剩下arr1[1,2]arr2[3',4']递归求整体上中位数
                if (((end1 - start1 + 1) & 1) == 1) {
                    // 奇数
                    if (arr2[mid2] >= arr1[mid1 - 1]) {
                        return arr2[mid2];
                    } else {
                        end1 = mid1 - 1;
                        start2 = mid2 + 1;
                    }
                } else {
                    // 偶数
                    end1 = mid1;
                    start2 = mid2 + 1;
                }
            } else {
                // 如果arr1的上中位数较小，完全就跟前一种情况镜像过来即可
                if (((end1 - start1 + 1) & 1) == 1) {
                    // 奇数
                    if (arr1[mid1] >= arr2[mid2 - 1]) {
                        return arr1[mid1];
                    } else {
                        end2 = mid2 - 1;
                        start1 = mid1 + 1;
                    }
                } else {
                    // 偶数
                    end2 = mid2;
                    start1 = mid1 + 1;
                }
            }
        }
        // 循环结束，说明每个数组有效长度都只剩1，那么上中位数就是两个元素中较小的那个
        return Math.min(arr1[start1], arr2[start2]);
    }
}
