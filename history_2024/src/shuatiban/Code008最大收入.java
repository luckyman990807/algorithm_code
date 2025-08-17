package shuatiban;

import java.util.Arrays;
import java.util.TreeMap;

/**
 * 给定数组hard和money,长度都为N,hard[i]表示i号工作的难度,money[i]表示i号工作的收入,
 * 给定数组ability,长度为M,ability[i]表示i号人的能力,
 * 每一号工作,都有无数个岗位,每一号人只有能力大于工作的难度,才能做这份工作,
 * 返回一个长度为M的数组ans,ans[i]表示i能获得的最大收入
 *
 * 贪心思路:
 * 1.先对工作排序,按照难度从小到大,难度一样的按照收入从大到小
 * 2.同样难度的,只保留最高收入的即可,剩下的可以删掉(既然难度一样,为什么不选择收入高的呢)
 * 3.如果b比a难度高,但是b的收入不比a高,那么b可以删掉(既然难度高的工作收入没有增加,那为什么不留下难度低的让更多人选择呢)
 * 4.经过上述处理,难度和收入已经建立单调性了,每来一个人,拿着他的能力在工作表的难度维度中,二分搜索找到小于等于他且离他最近的(可以用有序表TreeMap)
 */
public class Code008最大收入 {

    public static int[] maxMoney(int[] hard, int[] money, int[] ability) {
        // 封装成工作对象
        Job[] jobs = new Job[hard.length];
        for (int i = 0; i < hard.length; i++) {
            jobs[i] = new Job(hard[i], money[i]);
        }

        // 工作排序
        Arrays.sort(jobs, (o1, o2) -> {
            // 难度小的靠前,难度一样收入高的靠前
            return o1.hard - o2.hard != 0 ? o1.hard - o2.hard : o2.money - o1.money;
        });

        // 忽略掉没必要的工作,形成收入单调于难度的工作有序表
        TreeMap<Integer, Integer> map = new TreeMap<>();
        map.put(jobs[0].hard, jobs[0].money);
        Job pre = jobs[0];
        for (int i = 0; i < hard.length; i++) {
            if (jobs[i].hard > pre.hard && jobs[i].money > pre.money) {
                pre = jobs[i];
                map.put(jobs[i].hard, jobs[i].money);
            }
        }

        // 根据每个人的能力,在有序表中找到小于等于他并且离他最近的一个
        int[] ans = new int[hard.length];
        for (int i = 0; i < ability.length; i++) {
            ans[i] = map.floorKey(ability[i]) != null ? map.floorKey(ability[i]) : 0;
        }

        return ans;
    }

    public static class Job {
        public int hard;
        public int money;

        public Job(int hard, int money) {
            this.hard = hard;
            this.money = money;
        }
    }
}
