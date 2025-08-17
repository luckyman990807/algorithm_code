package tixiban.class1_13.greedy;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 有一个会场，同一时刻只能安排一场会议。
 * 给定一组会议的开始和结束时间，要求合理安排，使得能同时容纳的会议数最多，并求容纳会议数的最大值
 */
public class BestArrange {
    public static class Program {
        public int start;
        public int end;

        public Program(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return "{" +
                    "start=" + start +
                    ", end=" + end +
                    '}';
        }
    }

    /**
     * 贪心算法：结束时间早的会议先安排
     */
    public static int getBestArrange(Program[] programs) {
        if (programs == null || programs.length == 0) {
            return 0;
        }
        // 按照会议的结束时间，从小到大排序
        Arrays.sort(programs, Comparator.comparingInt(o -> o.end));
        // 已经安排到什么时间了
        int timeLine = 0;
        // 一共安排了几场会议
        int result = 0;

        for (Program program : programs) {
            if (program.start >= timeLine) {
                // 如果当前会议的开始时间晚于当前时间线，说明这个会议可以安排
                timeLine = program.end;
                result++;
            }
        }
        return result;
    }


    /**
     * 暴力算法：每一个会议安排与否都尝试一遍
     */
    public static int getBestArrangeViolent(Program[] programs) {
        if (programs == null || programs.length == 0) {
            return 0;
        }
        return process(programs, 0, 0);
    }

    /**
     * @param programs 当前还剩哪些会议没安排
     * @param arranged 已经安排好的会议的数量
     * @param timeLine 当前时间线
     * @return 这些会议的最大安排数量
     */
    public static int process(Program[] programs, int arranged, int timeLine) {
        if (programs.length == 0) {
            return arranged;
        }
        // 返回值，默认等于当前已经安排的数量，也就是不安排当前会议的结果
        int result = arranged;
        for (int i = 0; i < programs.length; i++) {
            if (programs[i].start >= timeLine) {
                // 如果这个会议的开始时间晚于当前时间线，说明这个会议是可安排的
                // 除去这个会议，剩下的会议
                Program[] others = removeByIndex(programs, i);
                // 不安排当前会议的结果，和安排当前会议的结果，取最大值
                // 安排当前会议的结果，就是递归调用，还剩那些会议就是除去当前会议剩下的，安排数是当前安排数+1， 时间线是当前会议的结束时间
                result = Math.max(result, process(others, arranged + 1, programs[i].end));
            }
        }
        return result;
    }

    /**
     * 将数组index下标的元素删除，返回剩下的
     */
    public static Program[] removeByIndex(Program[] programs, int index) {
        Program[] result = new Program[programs.length - 1];
        int resultIndex = 0;
        for (int i = 0; i < programs.length; i++) {
            if (i != index) {
                result[resultIndex++] = programs[i];
            }
        }
        return result;
    }


    /**
     * 主函数校验
     */
    public static void main(String[] args) {
        for (int n = 0; n < 100000; n++) {
            Program[] programs = generatePrograms(10, 24);

            Program[] programsGreedy = new Program[programs.length];
            System.arraycopy(programs, 0, programsGreedy, 0, programs.length);

            Program[] programsViolent = new Program[programs.length];
            System.arraycopy(programs, 0, programsViolent, 0, programs.length);

            int resultGreedy = getBestArrange(programsGreedy);
            int resultViolent = getBestArrangeViolent(programsViolent);

            System.out.println("会议列表：" + Arrays.toString(programs));
            System.out.println("贪心结果：" + resultGreedy);
            System.out.println("暴力结果：" + resultViolent);

            if (resultGreedy != resultViolent) {
                System.out.println("failed!");
                break;
            }
        }
        System.out.println("success!");
    }

    /**
     * 生成随机会议列表
     */
    public static Program[] generatePrograms(int maxCount, int maxTime) {
        Program[] programs = new Program[(int) (Math.random() * maxCount) + 1];
        for (int i = 0; i < programs.length; i++) {
            int start = (int) (Math.random() * maxTime) + 1;
            int end = start + (int) (Math.random() * (maxTime - start)) + 1;
            programs[i] = new Program(start, end);
        }
        return programs;
    }
}
