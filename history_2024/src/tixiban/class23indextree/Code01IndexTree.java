package tixiban.class23indextree;

/**
 * IndexTree解决的问题:单点更新、求范围累加和,都是O(logN)复杂度
 * 建立IndexTree花费O(N*logN),每次求累加和都是O(logN),原数组每变一次只需花费O(logN)调整IndexTree.
 *
 * 为什么前缀和数组法求累加和有缺陷?
 * 前缀和数组法无法适用于原数组变化的场景.
 * 建立前缀和数组O(N),后面算累加和都是O(1),平均起来复杂度确实很优秀.但是一旦原数组会变,那就没这么快了,每变一次就要花O(N)重新求前缀和数组.
 *
 * 跟线段树的作用有什么区别?
 * 对于一维数组,IndexTree能做的,线段树都能做,复杂度一样,甚至线段树还能范围更新,IndexTree只能单点更新,如果范围更新复杂度较差.
 * 但对于二维数组,线段树就很难支持,IndexTree很容易支持
 *
 *
 * IndexTree的运算规律:
 *
 * 1、IndexTree怎么赋值?
 * 前提:下标0弃而不用,从下标1开始用
 * 1位置存1自己,2位置存1到2的累加和,3位置存3自己,4位置存1到4的累加和,5位置存5自己,6位置存5到6的累加和,7位置存7自己,8位置存1到8的累加和,
 * 9位置存9自己,10位置存9到10的累加和,11位置存11自己,12位置存9到12的累加和,13位置存13自己,14位置存13到14的累加和,15位置存15自己,16位置存1到16的累加和...
 * 规律:下标i存几到几的累加和?从i二进制去掉最后一个1再加1,到i自己.
 * 例如12下标=00001100,去掉最后一个1=00001000,加1=00001001=9,得到12下标存9到12的累加和.
 * 为什么?前人怎么得出的结论已经不得而知了,记住就行.
 *
 * 2、怎么求前缀和?
 * 规律: 下标i的前缀和 = IndexTree i位置 + i去掉最后一个1位置 + 再去掉最后一个1位置 + ...直到=0就不加了
 * 例如 下标13=1101的前缀和 = 13位置 + 去掉最后一个1=1100=12位置 + 在去掉最后一个1=1000=8位置.
 * 由规律1可知,13=1101位置存的是13自己,12位置存的是9到12的累加和,8位置存的是1到8的累加和,加起来正好是1到13的累加和,也就是13的前缀和.
 *
 * 3、单点更新后,IndexTree哪些位置受影响?
 * 规律:如果下标i的数(第1个数,原数组下标i-1)要加v,那么IndexTree i位置、i加上最后一个1位置、i再加上最后一个1位置...都要加v,直到i>n就不加了.
 * 怎么提取最后一个1? i&-i就等于i最后i个1.而-i=~i+1取反加一.例如13=1101,取反=0010,取反加一=0011,与上13=0001,也就是最后一个1=0001.
 * 例如 下标13=1101位置加v,那么13位置加v,加上最后一个1=1110=14位置加v,再加最后一个1=10000=16位置加v,再加最后一个1=100000=32位置加v...
 */
public class Code01IndexTree {
    public static class IndexTree {
        // 实际支持的数据量大小
        private int n;
        // 用于求累加和的数组
        private int[] tree;

        public IndexTree(int[] arr) {
            this.n = arr.length;
            this.tree = new int[n + 1];
            // 初始化IndexTree,tree数组初始值都是0,初始化就相当于在对应的位置加原数组的值,复杂度O(N*logN)
            for (int i = 0; i < n; i++) {
                add(i + 1, arr[i]);
            }
        }

        /**
         * 单点更新,index位置加上value
         */
        public void add(int index, int value) {
            while (index <= n) {
                tree[index] += value;
                index += index & -index;
            }
        }

        /**
         * 求index位置的前缀和.
         * l到r位置的累加和 = sum(r) - sum(l-1)
         */
        public int sum(int index) {
            int sum = 0;
            while (index > 0) {
                sum += tree[index];
                index -= index & -index;
            }
            return sum;
        }
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        IndexTree indexTree = new IndexTree(arr);
        System.out.println(indexTree.sum(10));
        indexTree.add(7,10);
        System.out.println(indexTree.sum(10));
    }

}
