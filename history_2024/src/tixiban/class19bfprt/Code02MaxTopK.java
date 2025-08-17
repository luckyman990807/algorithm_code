package tixiban.class19bfprt;

/**
 * 给定一个无需数组arr,长度为N,给定一个正整数k,返回top k个最大的数,要求返回有序
 * 不同时间复杂度的3个方法:
 * 1、O(N*logN)
 * 2、O(N + k*logN)
 * 3、O(N + k*logk)
 *
 * 思路:
 * O(N*logN)先排序,再从大到小取k个数
 * O(N + k*logN)先把无需数组从底往上建大根堆O(N),然后k次取走堆顶自顶向下建大根堆O(logN)
 * O(N + k*logk)先bfprt求第k大的数O(N),然后遍历数组取出所有比第k大的数大的数O(N),最后把取出的k个数排序O(k*logk)
 */
public class Code02MaxTopK {
}
