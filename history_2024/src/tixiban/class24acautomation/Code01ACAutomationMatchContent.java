package tixiban.class24acautomation;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * AC自动机
 * 解决的问题:大文章中匹配敏感词
 * AC自动机的思路:在前缀树上玩KMP.
 * 前缀树:记录一个字符串出现了几次,或者一个字符串作为前缀出现了几次的树.
 * 在AC自动机中用前缀树判断大文章的某个子串在所有敏感词中有没有出现过.
 * KMP的思想:我匹配了一串字符发现匹配不下去了,那我就看看这串字符的后缀是哪串字符的前缀,并且使重合的这部分前后缀最大.这样就为下次节省了匹配这段相同前后缀的时间.
 * 在AC自动机中,匹配一个敏感词A到一半匹配不下去了,就看看A已匹配部分的某个后缀是哪个敏感词B的前缀,并且使相同前后缀最大,就跳到这个敏感词B相同前后缀的最后一个位置继续匹配,这样B的前缀就已经在匹配A的时候匹配了,避免重复匹配,避免浪配匹配.
 *
 * fail指针:指示一个敏感词匹配失败后要跳到哪里继续匹配,用来落地kmp思想.
 * 如果敏感词A的某个节点a及之前的子串 的某个后缀,跟敏感词B的前缀是最大相同前后缀,那么A路径上的a节点的fail指针指向B路径上的a节点.
 *
 * 根据「A路径上a节点指向B路径a节点,则Aa及之前子串的某个后缀与Ba之前的前缀是最大相同前后缀」,
 * 如果成功匹配到一个敏感词cba,那么顺着a的fail指针就可以检查是否有ba、a敏感词,如果有的话,他们也全部命中.
 */
public class Code01ACAutomationMatchContent {
    public static class Node {
        // 前缀树上的下个节点
        public Node[] next;

        // 如果这个节点是某个敏感词的结尾,那么该节点的end=这个敏感词.否则=空
        public String end;

        // fail指针,指示匹配一个敏感词失败后应该跳到哪继续匹配
        public Node fail;

        // 标识这个敏感词是否已经被命中过
        public boolean endUsed;

        public Node() {
            // 26个字母
            next = new Node[26];
            fail = null;
            end = null;
            endUsed = false;
        }
    }

    public static class ACAutomation {
        private Node root;

        public ACAutomation() {
            root = new Node();
        }

        /**
         * 插入敏感词,构建前缀树
         * @param string 一个敏感词
         */
        public void insert(String string) {
            char[] str = string.toCharArray();
            Node cur = root;
            for (char c : str) {
                // 如果没有通向这个字符的路,就创建,有就跳过(换成标准前缀树的话,有就计数++,这里不计数)
                // 字符放在路上,不放在节点上
                int index = c - 'a';
                if (cur.next[index] == null) {
                    cur.next[index] = new Node();
                }
                cur = cur.next[index];
            }
            // 表示这个节点是一个字符串(敏感词)的结尾
            cur.end = string;
        }

        /**
         * 初始化前缀树节点的fail指针
         *
         * fail指针指向规则:
         * 1、前缀树根节点的fail指向null
         * 2、根节点的直接孩子的fail指向根节点
         * 3、其他节点一定是某个非根节点的i孩子,从父亲开始顺着fail指针往下转,如果沿途有节点有通往i的路,就指向该节点的i孩子.如果转完一圈都没有,就指向根节点
         */
        public void initFail() {
            // 宽度优先遍历
            Queue<Node> queue = new LinkedList<>();
            queue.add(root);
            while (!queue.isEmpty()) {
                // 弹出cur节点时,设置cur的孩子的fail指针
                Node cur = queue.poll();
                for (int i = 0; i < cur.next.length; i++) {
                    // 如果有通往'a'+i字符的路,即有i号孩子,就为i号孩子设置fail指针
                    if (cur.next[i] != null) {
                        // cur的i号儿子的fail先默认设为root,如果找到合适的再改
                        cur.next[i].fail = root;
                        // 如果cur的fail有i方向的路,cur的i儿子就指向cur的fail的i儿子
                        // 否则,再看cur的fail的fail,一直顺着cur的fail往上找,直到fail=null
                        Node curFail = cur.fail;
                        while (curFail != null) {
                            if (curFail.next[i] != null) {
                                cur.next[i].fail = curFail.next[i];
                                break;
                            }
                            curFail = curFail.fail;
                        }
                        // 新设置fail指针的i儿子也加入队列,等待为i的儿子设置fail
                        queue.add(cur.next[i]);
                    }
                }
            }
        }

        /**
         * 给定一个文章content,返回命中了哪些敏感词
         * @param content 待匹配的大文章
         * @return 命中的敏感词
         */
        public List<String> match(String content) {
            // 存放匹配到的敏感词
            List<String> answers = new LinkedList<>();
            char[] str = content.toCharArray();
            Node cur = root;
            for (char c : str) {
                int index = c - 'a';
                // 如果cur没有通往c的路,就跳到fail,直到找到通往c的路,或者fail为null了
                while (cur.next[index] == null && cur.fail != null) {
                    cur = cur.fail;
                }
                // 经历了前面的跳跃,如果cur有通往c的路
                if (cur.next[index] != null) {
                    // 就往c方向走一步
                    cur = cur.next[index];

                    // 顺着fail指针转一圈,看看有没有敏感词的结尾,如果有,就加入答案,直到fail指针到了null.
                    Node follow = cur;
                    while (follow.fail != null) {
                        // 如果这个节点已经被命中过敏感词了,那么顺着fail指针沿途都已经命中过了,就没有再匹配的必要了,直接退出.(如果敏感词需要计数,可以在这里计数)
                        if (follow.endUsed) {
                            break;
                        }
                        // 如果沿途节点是敏感词结尾,就把敏感词加入答案
                        if (follow.end != null) {
                            answers.add(follow.end);
                            follow.endUsed = true;
                        }
                        // 顺着fail往下转
                        follow = follow.fail;
                    }
                }
                // 经历了前面的跳跃,如果依然没用通往c的路,说明匹配不下去了.
                // 并且fail为null了,而只有root的fail为null,正好回到root可以从头匹配了.
            }
            return answers;
        }
    }

    public static void main(String[] args) {
        ACAutomation acAutomation = new ACAutomation();
        acAutomation.insert("abc");
        acAutomation.insert("abcd");
        acAutomation.insert("abe");
        acAutomation.insert("def");
        acAutomation.insert("cde");
        acAutomation.initFail();

        String content = "abcdek";

        System.out.println(acAutomation.match(content));
    }
}
