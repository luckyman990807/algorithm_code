package tixiban.class1_13.trie;

/**
 * trie树，又叫前缀树、prefix tree，用于求字符串出现过的次数、字符串作为前缀出现过的次数
 * 前缀树用节点记录次数，两个节点之间的路径记录字符
 */
public class Trie {

    private Node root;

    public Trie() {
        this.root = new Node();
    }

    /**
     * 向前缀树中加入一个字符串
     */
    public void insert(String word) {
        if (null == word) {
            return;
        }
        Node cur = root;
        // 只要新加入一个字符串，必然要经过根节点，因为路径才表示字符，根节点下面才有路径
        cur.pass++;

        char[] str = word.toCharArray();
        for (int i = 0; i < str.length; i++) {
            // 通向str[i]字符的路在next数组中是第几条？第str[i]-'a'条
            int path = str[i] - 'a';
            // 判断当前通向str[i]字符的路是否存在
            if (null == cur.next[path]) {
                // 如果此前没有这条路，现在要创建这条路
                cur.next[path] = new Node();
            }
            cur = cur.next[path];
            // 这条路对应的节点，被经过的次数+1
            cur.pass++;
        }
        // 遍历完整个字符串后，cur就是通向最后一个字符的路对应的节点，也就是说本字符串以本节点结尾
        cur.end++;
    }

    /**
     * 从前缀树中查找某个字符串完整出现的次数
     */
    public int search(String word) {
        if (null == word) {
            return 0;
        }
        Node cur = root;
        char[] str = word.toCharArray();
        for (int i = 0; i < str.length; i++) {
            int path = str[i] - 'a';
            if (null == cur.next[path]) {
                // 如果查询过程中发现，在某个位置上通向下一个字符的路径没有，说明这个字符串没有加入过
                return 0;
            }
            cur = cur.next[path];
        }
        // 遍历字符串找到的最后一个节点的end，记录的就是这个字符串加入的次数
        return cur.end;
    }

    /**
     * 从前缀树中查找某个字符串作为前缀出现的次数
     */
    public int searchPrefix(String word) {
        Node cur = root;
        char[] str = word.toCharArray();
        for (int i = 0; i < str.length; i++) {
            int path = str[i] - 'a';
            if (null == cur.next[path]) {
                // 如果查询过程中发现，在某个位置上通向下一个字符的路径没有，说明这个字符串没有出现过，也没有任何字符串以它为前缀
                return 0;
            }
            cur = cur.next[path];
        }
        // 遍历字符串找到的最后一个节点的pass，记录的就是有几个字符串经过他，也就是作为前缀出现的次数
        return cur.pass;
    }

    /**
     * 从前缀树中删除某个字符串
     * 沿途pass--，结尾还要end--
     */
    public void remove(String word) {
        if (search(word) <= 0) {
            return;
        }
        Node cur = root;
        // 根节点pass先--，因为for循环里减的是下一个节点的pass
        root.pass--;
        char[] str = word.toCharArray();
        for (int i = 0; i < str.length; i++) {
            int path = str[i] - 'a';
            // 沿途pass--
            if (--cur.next[path].pass <= 0) {
                // 如果通向某个字符的路径，pass==0了，说明没有字符串经过这里了，那么再往后更没人经过了，直接置为null返回
                cur.next[path] = null;
                return;
            }
            cur = cur.next[path];
        }
        // 字符串最后一个节点除了pass--还要end--
        cur.end--;
    }


    /**
     * 前缀树的节点
     */
    public static class Node {
        // 记录本节点下一步的路径，路径实际不是一个实体，而是用节点数组表示能到哪些节点，也就是表示到下一个节点的路径
        public Node[] next;
        // 记录有几个字符串经过这个节点，或者说这个节点被经过的次数
        public int pass;
        // 记录有几个字符串以这个节点结束
        public int end;

        public Node() {
            /**
             * 为什么下一个节点的数组要初始化26长度？
             * 假设题目要求的字符串都只涉及小写字母，那么一个字符的下一个字符，一定在'a'到'z'范围内，最多有26个分支
             * 即一个节点下一步的路径最多有26条
             * next[0]表示通向a的路径
             * next[1]表示通向b的路径
             * next[i] == null表示通往i方向的路不存在
             * next[i] != null表示通往i方向的路存在
             */
            this.next = new Node[26];
            this.pass = 0;
            this.end = 0;

        }
    }
}