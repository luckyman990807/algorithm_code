package tixiban.class1_13.heap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * 加强堆
 * 为什么要加强堆？系统提供的堆有什么缺陷？
 * 系统提供的堆只能把输入的数据整理成堆结构，然后能弹出堆顶元素，能以数组的形式根据下标找到某个元素（有正向索引，就是数组本身）
 * 但是不能快速地根据某个元素找到他所在的位置（没有反向索引）。比如说堆上某个对象更新了，需要重新成堆，就只能遍历找到这个对象
 * 如果有反向索引，找到某个对象是O(1)，而遍历找到某个对象是O(N)
 */
public class SuperHeap<T> {
    // 基础堆
    private ArrayList<T> heap;
    // 堆大小
    private int heapSize;
    // 反向索引
    private HashMap<T, Integer> indexMap;
    // 比较器
    private Comparator<T> comparator;

    public SuperHeap(Comparator<T> comparator) {
        this.heap = new ArrayList<>();
        this.heapSize = 0;
        this.indexMap = new HashMap<>();
        this.comparator = comparator;
    }

    // 判断堆上有没有元素
    public boolean isEmpty() {
        return heapSize == 0;
    }

    // 返回堆元素的个数
    public int size() {
        return heapSize;
    }

    // 判断堆上是否含有某对象
    public boolean contains(T object) {
        return indexMap.containsKey(object);
    }

    // 返回堆中所有元素列表。只有这个方法是O(N)，其他都是O(logN)
    public List<T> getAllElements(){
        List<T> result = new ArrayList<>();
        for (T object : this.heap){
            result.add(object);
        }
        return result;
    }

    // 读取堆顶元素，不弹出
    public T peek() {
        return heap.get(0);
    }

    // 新元素加入堆
    public void push(T object) {
        heap.add(object);
        indexMap.put(object, heapSize);
        heapInsert(heapSize++);
    }

    // 弹出堆顶元素
    public T pop() {
        T result = heap.get(0);
        swap(0, heapSize - 1);
        indexMap.remove(result);
        heap.remove(--heapSize);
        heapify(0);
        return result;
    }

    // 堆中某元素发生变化，重新调整堆结构
    public void resign(T object) {
        Integer cur = indexMap.get(object);
        heapInsert(cur);
        heapify(cur);
    }

    // 从堆中移除某元素
    public void remove(T object) {
        Integer cur = indexMap.get(object);
        swap(cur, heapSize - 1);
        indexMap.remove(object);
        heap.remove(heapSize - 1);
        if (cur != --heapSize) {
            heapInsert(cur);
            heapify(cur);
        }
    }

    public void swap(int a, int b) {
        T tempA = heap.get(a);
        T tempB = heap.get(b);
        heap.set(a, tempB);
        heap.set(b, tempA);
        indexMap.put(tempA, b);
        indexMap.put(tempB, a);
    }

    public void heapInsert(int cur) {
        while (comparator.compare(heap.get(cur), heap.get((cur - 1) / 2)) < 0) {
            swap(cur, (cur - 1) / 2);
            cur = (cur - 1) / 2;
        }
    }

    public void heapify(int cur) {
        int left = cur * 2 + 1;
        int right = cur * 2 + 2;
        while (left < heapSize) {
            int better = right < heapSize && comparator.compare(heap.get(right), heap.get(left)) < 0 ? right : left;
            if (comparator.compare(heap.get(cur), heap.get(better)) <= 0) {
                break;
            }
            swap(cur, better);
            cur = better;
            left = cur * 2 + 1;
            right = cur * 2 + 2;
        }
    }
}
