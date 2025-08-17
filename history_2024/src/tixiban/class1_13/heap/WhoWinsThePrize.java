package tixiban.class1_13.heap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * 得奖系统问题
 * 题目：
 * 给定一个整型数组int[]arr，和一个布尔型数组boolean[]op，两数组等长，一对arr[i]和op[i]代表一个事件，op[i]表示id为arr[i]的用户所做的操作，举例：
 * arr = [3, 1, 1, 3, 5 ……
 * op  = [T, T, F, T, F ……
 * 依次表示：3用户购买一件商品，1用户购买一件商品，1用户退货一件商品，3用户购买一件商品，5用户退货一件商品……
 * 你是电商平台负责人，你想在每个事件到来时，都给购买次数最多的前K名用户颁奖，所以每个事件发生后，你都需要一个得奖名单。
 * 得奖规则：
 * 1、如果某用户购买商品数量为0，又发生了退货事件，则认为该事件无效，得奖名单和上一个事件一致，比如例子中的5用户。
 * 2、用户发生购买事件时，购买商品数+1， 发生退货事件时，购买商品数-1.
 * 3、每次最多K个用户得奖，K作为参数传入。如果得奖人数确实不够K个，就以不够的情况输出结果。
 * 4、得奖系统分为得奖区和候选区，任何用户只要购买数>0，一定在两个区域中的一个
 * 5、购买数最大的前K名用户进入得奖区，在最初得奖区没到达K个用户的时候，新来的用户直接进入得奖区。
 * 6、如果购买数不足以进入得奖区，则进入候选区。
 * 7、候选区购买数最多的用户，如果购买数大于得奖区购买数最少的用户，可以取而代之。
 * 如果得奖区购买数最少的用户有多个，就替换最早进入得奖区的用户。
 * 如果候选区购买数最多的用户有多个，机会留给最早进入候选区的用户。
 * 8、候选区和得奖区是两套时间，而用户只会在其中一个区域，所以只会有一个时间。
 * 进入得奖区的时间就是当前事件的时间（可以理解为arr[i]和op[i]中的i），进入候选区的时间就是当前事件的时间。
 * 从候选区出来的用户，候选区时间删除。从得奖区出来的用户，得奖区时间删除。
 * 9、如果用户购买数==0，不管在哪个区域，都要删掉，在哪个区域也不会找到该用户，区域时间删除。
 * 如果该用户下次又发生购买行为，产生>0的购买数，会再根据之前的规则回到某个区域，重新记录区域时间。
 */
public class WhoWinsThePrize {

    public static List<List<Integer>> getWinnerList(int[] arr, boolean[] op, int k) {
        // 用户id和对象的映射
        HashMap<Integer, Customer> customerIdMap = new HashMap<>();
        // 得奖区
        ArrayList<Customer> winners = new ArrayList<>();
        // 候选区
        ArrayList<Customer> candidates = new ArrayList<>();
        // 要返回结果，所有时刻的得奖列表
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < arr.length; i++) {
            // 用户id
            int id = arr[i];
            // 用户事件，true购买，false退货
            boolean buyOrRefund = op[i];

            // 购买数为0，且发生了退货
            if (!customerIdMap.containsKey(id) && !buyOrRefund) {
                result.add(getCurWinners(winners));
                continue;
            }
            // 走到这有3种情况：1、购买数为0，发生购买；2、购买数不为0，发生退货；3、购买数不为0，发生购买

            // 购买数为0，发生购买，需要记录用户，并且进入某个区
            if (!customerIdMap.containsKey(id)) {
                Customer customer = new Customer(id, 1, i);
                customerIdMap.put(id, customer);
                if (winners.size() < k) {
                    // 如果获奖区没满，进入获奖区
                    winners.add(customer);
                } else {
                    // 否则进入候选区
                    candidates.add(customer);
                }
            } else if (buyOrRefund) {
                // 购买数不为0，发生购买
                customerIdMap.get(id).buyNumber++;
            } else {
                // 购买数不为0，发生退货
                customerIdMap.get(id).buyNumber--;
                // 退货后如果购买数==0，就清理掉
                if (customerIdMap.get(id).buyNumber <= 0) {
                    winners.remove(customerIdMap.get(id));
                    candidates.remove(customerIdMap.get(id));
                    customerIdMap.remove(id);
                }
            }

            winners.sort(new WinnerComparator());
            candidates.sort(new CandidateComparator());
            move(winners, candidates, k, i);
            result.add(getCurWinners(winners));
        }
        return result;
    }


    public static class Customer {
        // 用户id
        public int id;
        // 购买数量
        public int buyNumber;
        // 进入某区域的时间
        public int enterTime;

        public Customer(int id, int buyNumber, int enterTime) {
            this.id = id;
            this.buyNumber = buyNumber;
            this.enterTime = enterTime;
        }
    }

    public static class WinnerComparator implements Comparator<Customer> {
        @Override
        public int compare(Customer o1, Customer o2) {
            return o1.buyNumber != o2.buyNumber ? o1.buyNumber - o2.buyNumber : o1.enterTime - o2.enterTime;
        }
    }

    public static class CandidateComparator implements Comparator<Customer> {
        @Override
        public int compare(Customer o1, Customer o2) {
            return o1.buyNumber != o2.buyNumber ? o2.buyNumber - o1.buyNumber : o1.enterTime - o2.enterTime;
        }
    }

    public static List<Integer> getCurWinners(List<Customer> winners) {
        ArrayList<Integer> result = new ArrayList<>();
        for (Customer winner : winners) {
            result.add(winner.id);
        }
        return result;
    }

    public static void move(ArrayList<Customer> winners, ArrayList<Customer> candidates, int k, int time){
        if(candidates.isEmpty()){
            // 候选区为空，都在得奖区，不需要移动
            return;
        }

        if(winners.size()<k){
            // 得奖区没满，把候选区第一名移动到得奖区
            winners.add(candidates.get(0));
            candidates.remove(0);
        }else {
            // 得奖区满了
            // 得奖区倒数第一名移动到候选区，候选区第一名移动到得奖区
            Customer lastWinner = winners.get(0);
            winners.remove(0);
            Customer firstCandidate = candidates.get(0);
            candidates.remove(0);

            candidates.add(lastWinner);
            winners.add(firstCandidate);
        }
    }

}
