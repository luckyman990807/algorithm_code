package shuatiban;

import java.io.File;
import java.util.LinkedList;

/**
 * 给定一个文件目录的路径，写一个函数统计这个目录下所有的文件数量并返回，隐藏文件也算，但是文件夹不算
 *
 * 思路:
 * 多叉树的宽度优先遍历,或深度优先遍历都可以
 */
public class Code002同一个目录下文件的数量 {
    public static int bfs(String path) {
        File root = new File(path);
        if (!root.isDirectory() && !root.isFile()) {
            return 0;
        }
        if (root.isFile()) {
            return 1;
        }

        int count = 0;

        LinkedList<File> queue = new LinkedList<>();
        queue.addFirst(root);

        while (!queue.isEmpty()) {
            File cur = queue.pollLast();
            for (File child : cur.listFiles()) {
                if (child.isFile()) {
                    count++;
                } else if (child.isDirectory()) {
                    queue.addFirst(cur);
                }
            }
        }

        return count;
    }
}
