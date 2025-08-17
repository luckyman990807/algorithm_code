package tixiban.class1_13.heap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 得奖问题用加强堆实现
 */
public class WhoWinsThePrizeSuperHeap {

    public static List<List<Integer>> getWinnerList(int[] arr, boolean[] op, int k) {
        PrizeSystem prizeSystem = new PrizeSystem(k);
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            prizeSystem.operate(i, arr[i], op[i]);
            result.add(prizeSystem.getCurWinners());
        }
        return result;
    }

    /**
     * 得奖系统
     */
    public static class PrizeSystem {
        // 用户id和用户对象的映射
        private HashMap<Integer, Customer> customerIdMap;
        // 候选区，用加强堆实现
        private SuperHeap<Customer> candidateHeap;
        // 得奖区，用加强堆实现
        private SuperHeap<Customer> winnerHeap;
        // 得奖区大小
        private final int winnerLimit;

        public PrizeSystem(int winnerLimit) {
            this.winnerLimit = winnerLimit;
            this.customerIdMap = new HashMap<>();
            // 候选区比较规则：购买数大的靠前，如果购买数一样则进入时间早的靠前
            this.candidateHeap = new SuperHeap<>((Customer o1, Customer o2) ->
                    o1.butNumber != o2.butNumber ? o2.butNumber - o1.butNumber : o1.enterTime - o2.enterTime);
            // 得奖区比较规则：购买数小的靠前，如果购买数一样则进入时间早的靠前
            this.winnerHeap = new SuperHeap<>((Customer o1, Customer o2) ->
                    o1.butNumber != o2.butNumber ? o1.butNumber - o2.butNumber : o1.enterTime - o2.enterTime);
        }

        /**
         * 对于当前时间，某用户的某个事件，进行处理
         */
        public void operate(int time, int id, boolean buyOrRefund) {
            // 购买数为0，且发生退货，直接忽略
            if (!buyOrRefund && !this.customerIdMap.containsKey(id)) {
                return;
            }
            // 此时要么购买数为0且发生购买，要么购买数不为0发生购买或退货
            if (!this.customerIdMap.containsKey(id)) {
                // 购买数为0，那么肯定是发生购买，记录用户
                this.customerIdMap.put(id, new Customer(id, 1, time));
                // 并且进入某个区
                if (this.winnerHeap.size() < this.winnerLimit) {
                    // 得奖区没满，就直接进得奖区
                    this.winnerHeap.push(customerIdMap.get(id));
                } else {
                    // 否则进候选区
                    this.candidateHeap.push(customerIdMap.get(id));
                }
            } else {
                Customer customer = customerIdMap.get(id);
                // 购买数不为0
                if (!buyOrRefund) {
                    // 发生退货
                    if (--customer.butNumber <= 0) {
                        // 退货后购买数为0了，无论是得奖区、候选区还是用户列表，都要删除
                        if (this.winnerHeap.contains(customer)) {
                            this.winnerHeap.remove(customer);
                        } else {
                            this.candidateHeap.remove(customer);
                        }
                        this.customerIdMap.remove(id);
                    }
                } else {
                    // 发生购买
                    customer.butNumber++;
                    // 重新成堆
                    if (this.winnerHeap.contains(customer)) {
                        this.winnerHeap.resign(customer);
                    } else {
                        this.candidateHeap.resign(customer);
                    }
                }
            }
            // 检查两个区域是否有用户需要移动
            move(time);
        }

        /**
         * 检查是否有用户可以从候选区进入得奖区
         */
        private void move(int time) {
            if (this.candidateHeap.isEmpty()) {
                // 候选区为空，不需要移动
                return;
            }
            // 候选区不为空
            if (this.winnerHeap.size() < this.winnerLimit) {
                // 得奖区没满，弹出候选区堆顶元素移动到得奖区，同时进入区域时间置为当前时间
                Customer customer = this.candidateHeap.pop();
                customer.enterTime = time;
                this.winnerHeap.push(customer);
            } else {
                // 得奖区满了，需要比较候选区最合适的那个是否具备进入得奖区的条件
                if (this.candidateHeap.peek().butNumber > this.winnerHeap.peek().butNumber) {
                    // 候选区堆顶用户的购买数大于得奖区堆顶用户的购买数，移动
                    Customer oldWinner = this.winnerHeap.pop();
                    oldWinner.enterTime = time;
                    Customer newWinner = this.candidateHeap.pop();
                    newWinner.enterTime = time;
                    this.winnerHeap.push(newWinner);
                    this.candidateHeap.push(oldWinner);
                }
            }
        }

        /**
         * 查询当前所有得奖用户的id
         */
        public List<Integer> getCurWinners() {
            List<Customer> customers = this.winnerHeap.getAllElements();
            List<Integer> result = new ArrayList<>();
            for (Customer customer : customers) {
                result.add(customer.id);
            }
            return result;
        }
    }

    /**
     * 用户类
     */
    public static class Customer {
        // 用户id
        private int id;
        // 购买数
        private int butNumber;
        // 进入区域的时间
        private int enterTime;

        public Customer(int id, int butNumber, int enterTime) {
            this.id = id;
            this.butNumber = butNumber;
            this.enterTime = enterTime;
        }
    }
}
