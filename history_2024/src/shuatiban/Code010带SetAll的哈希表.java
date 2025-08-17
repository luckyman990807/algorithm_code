package shuatiban;

import java.util.HashMap;

/**
 * 数据结构设计题
 *
 * 已知哈希表HashMap的get和put都是O(1),现在要求自己封装一个哈希表,新增setAll(value)方法,功能是把所有记录的值都改成value.
 * 要求get()、put()、setAll都是O(1)
 *
 * 思路:记录每个操作的时间戳,如果get时发现这条记录的时间戳早于setAll的时间戳,就返回getAll的的值,否则就返回HashMap中原本的值.
 */
public class Code010带SetAll的哈希表 {
    public static class MapValue {
        public int value;
        public int time;

        public MapValue(int value, int time) {
            this.value = value;
            this.time = time;
        }
    }

    public static class MyHashMap {
        public HashMap<Integer, MapValue> map;
        public int time;
        public int setAllTime;
        public int setAllValue;

        public MyHashMap() {
            map = new HashMap<>();
            time = 0;
            setAllTime = Integer.MAX_VALUE;
        }

        public void put(int key, int value) {
            // 插入或更新value,记录当前时间戳,时间戳++
            map.put(key, new MapValue(value, time++));
        }

        public void setAll(int value) {
            // 记录setAll的值,同时时间戳++
            setAllValue = value;
            setAllTime = time++;
        }

        public int get(int key) {
            // 取出hashMap中的值
            MapValue mapValue = map.get(key);
            if (mapValue.time < setAllTime) {
                // 如果这个值的更新时间早于setAll时间,说明逻辑上这个值已经被set成setAllValue了,返回setAllValue
                return setAllValue;
            } else {
                // 否则这个值得到更新时间晚于setAll的时间,当前的setAllValue管不着我,正常返回hashMap里的值
                return mapValue.value;
            }
        }
    }
}
