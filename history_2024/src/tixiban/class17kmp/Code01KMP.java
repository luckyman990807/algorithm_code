package tixiban.class17kmp;


public class Code01KMP {

    /**
     * kmp算法用于匹配一个字符串是不是另一个字符串的子串，并返回子串在父串中的开始位置。如果父串长度N，子串长度M，那么复杂度O(N)。
     *
     * 匹配子串的暴力方法：遍历父串的每一个字符，判断以这个位置的这个字符开头能否匹配出子串。如果父串长度N，子串长度M，那么复杂度O(N*M)。
     *
     * 前置概念：最长相等前后缀
     * 相等前后缀就是这个字符串的前缀和后缀相等，最长就是让这个前缀和后缀在保持相等的前提下尽量长，但不能等于字符串长度。
     * 例如 abcdabcda 最长相等前后缀为abcda，长度为5，abaccaba 最长相等前后缀为aba，长度为3
     * 使用kmp算法需要先求出一个next数组，next[i]=l表示字符串第i位之前（0～i-1）的子串，最长相等前后缀长度为l，也表示第i位之前的子串最长相等前缀的下一个位置是l下标。
     *
     * kmp算法流程
     * 假设父串当前比对位置为cur，子串当前比对位置为subCur，二者逐位比对，
     * 如果cur和subCur位置的字符相等，那么二者都右移
     * 如果不相等，
     *      如果subCur是子串的开头，说明连开头都没匹配上，只cur右移，继续匹配子串开头
     *      如果subCur不是开头，那么取出next[subCur]，即subCur前面的子串的最长相等前后缀长度，subCur左移next[subCur]位，继续和sub比对
     * 直到subCur越界，说明匹配成功，或者cur越界，说明没匹配到
     *
     * 可以看出，暴力方法前面匹配的过程丝毫不会对后面的匹配起到帮助，而kmp算法前面的匹配过程会加速后面的匹配，所以他快
     *
     */


    /**
     * 问：
     * 为什么可以从子串subCur左移next[subCur]位置开始匹配？子串开头到subCur-next[subCur]-1这段为什么不用匹配了？
     * 答：
     * 举个例子，父串下标0123456789，子串下标012345，假设cur=6、subCur=4的时候对不上了，此前的4个位置都匹配，即：
     * 0 1 2 3 4 5 6 7 8 9 0    父串下标
     *     t t t t f            t表示匹配，f表示不匹配
     *     0 1 2 3 4            子串下标
     * 假设next[subCur]=next[4]=2，说明子串01和子串23相同，而子串23又和父串45相同，所以子串01和父串45相同，
     * 所以前两位就无需再比，直接把子串01和父串45对齐，继续匹配，即：
     * 0 1 2 3 4 5 6 7 8 9 0    父串下标
     *         t t ？           t表示匹配，f表示不匹配
     *         0 1 2 3 4        子串下标
     * 如果用暴力方法则是回退到父串3位置继续和子串0比对：
     * 0 1 2 3 4 5 6 7 8 9 0    父串下标
     *       ？                 t表示匹配，f表示不匹配
     *       0 1 2 3 4          子串下标
     *
     *
     *
     * 问：
     * cur=6、subCur=4对不上的时候，比对的是父串2开头能否匹配子串，子串回退到subCur-next[subCur]的时候，比对的是父串4开头能否匹配子串，
     * 那以2和4之间的位置开头为什么不需要不对？为什么一定匹配不上？
     * 答：
     * 反证法
     * 假设父串2和4之间的位置（例如3）开头能匹配上子串，那么起码有父串345=子串012
     * 又因为cur=6、subCur=4之前的都匹配上了，所以父串345=子串123
     * 所以推出子串012=子串123，说明子串4之前的最长相等前后缀长度=3？和你自己计算的next[4]=3矛盾了，所以父串3开头不可能匹配上子串。
     *
     */
    public static int startIndex(String string, String subString){
        if (string == null || subString == null || subString.length() == 0 || string.length() < subString.length()) {
            return -1;
        }

        char[] str = string.toCharArray();
        char[] subStr = subString.toCharArray();

        // 父串当前比对的位置
        int cur = 0;
        // 子串当前比对的位置
        int subCur = 0;

        // next[i] = l 表示i位置之前的子串，最大相等前后缀长度为l
        int[] next = getNext(subStr);

        // 子串或父串下标越界，就退出循环
        while (cur < str.length && subCur < subStr.length){
            if (str[cur] == subStr[subCur]) {
                // 如果当前位置匹配上了，二者都继续比对下一个
                cur++;
                subCur++;
            } else if (subCur == 0) {
                // 如果每匹配上，并且是子串的开头就没匹配上，父串比对下一个
                cur++;
            } else {
                // 每匹配上，并且不是子串开头就没匹配上而是匹配了一部分之后断掉了
                // 子串回退到最大相等前缀的下一个位置
                subCur = next[subCur];
            }
        }
        // 子串位置越界说明都匹配上了，返回两个位置的差值就是父串中子串开始的位置
        // 否则就是父串位置越界，匹配到最后也没匹配上，返回-1
        return subCur == subStr.length ? cur - subCur : -1;
    }
    /**
     * 分析startIndex方法中while的复杂度
     *
     * while中3个分支每次只会走一个，3个分支走的次数加起来等于while的复杂度
     * 对于变量cur和变量cur-subCur，二者初始值都是0，每次循环要么cur增要么cur-subCur增，要么同时增，不会回退，二者最大都能到N，所以最大循环2N遍，复杂度O(N)。
     * 为什么不回退？
     * 第一个分支cur增，subCur也增，cur-subCur不变
     * 第二个分支cur增，subCur不变，cur-subCur增
     * 第三个分支cur不变，subCur减，cur-subCur增
     * 所以cur和subCur单调增不回退
     *
     * 为什么要用cur-subCur分析？
     * 因为用cur的话，第三个分支体subCur减的影响体现不出来，用subCur的话第三个分支subCur会回退，没法分析复杂度。
     * 所以要构造一个不回退的变量来证明，数学证明也会用到这种方式。
     */





    /**
     * 怎么求next数组？
     *
     * next数组的含义：next[i]=l表示 字符串i位之前的子串，最大相等前后缀长度=l；也表示i位之前的子串，最大相等前缀的下一位是l位。
     * next数组前两项固定为-1、0，因为0位置前面没有子串，next[0]=-1标记无效；因为1位置前面子串长度=1，前缀又不能跟子串长度相等，所以=0。
     *
     * i-1之前的最大前后缀，分别右扩一位，有可能就是i之前的最大前后缀，例如：
     * a b c e a b c a      字符串
     * 0 1 2 3 4 5 6 7      下标
     *-1 0 0 0 0 1 2 ?      next数组值
     * 6之前的最大前后缀ab，前缀和后缀分别右扩一位abc，就是7之前的最大前后缀，所以7之前前缀的下一位next[7] = 6之前前缀的下一位next[6] + 1
     * 因此求next[i]要拿i-1之前的 最大前缀的下一位、最大后缀的下一位比对，如果相等就能右扩得到next[i] = 比对位置之前前缀的下一位 + 1
     * 所以大思路就是遍历i，每一轮都找个位置和i-1位置比对，这个位置的初始值是next[i-1]
     *
     * 如果比对不成功，例如：
     * a b c e a b c a f        字符串
     * 0 1 2 3 4 5 6 7 8        下标
     *-1 0 0 0 0 1 2 3 ?        next数组值
     * 求8之前的最大前后缀时，前一个位置7，next[7]=3表示7之前的最大前缀的下一位是3，比对7之前的最大后缀的下一位7，发现不相等，7之前的最大前后缀无法右扩，
     * 接下来再拿3之前的最大前缀（空串）的下一位next[3]=0，比对7之前的最大后缀的下一位7，发现相等，这个前后缀（空串）可以右扩作为8之前的最大前后缀，
     * next[8]=8之前的最大前缀的下一位=0+1=1
     *
     * 为什么能比对3之前的最大前缀和7之前的最大后缀？他俩有什么关系？
     * 因为7之前的最大前后缀是012和456，这俩串是相等的，如果说3之前（也就是012中）也有一对相等的前后缀，那么7之前（456中）一定也有一对相等的前后缀，且012中的前缀一定等于456中的后缀
     * （实际上012中前缀=012中后缀=456中前缀=456中后缀）
     * 所以可以基于3之前的前缀和7之前的后缀判断能否右扩作为8之前的前后缀。
     *
     * 如果当前i-1位置比对到了next[0]依然没有匹配上，那就没得再比了，只能让next[i]=0了
     *
     *
     * 问：
     * next[i]可能>next[i-1]+1吗？
     * 答：
     * 不可能，反证法。
     * next[6] = 2，假设next[7] = 4，说明7之前的最大前后缀是0123和3456，都左缩一位得到6之前的最大前后缀是012和345，得到next[6]=3，前后矛盾。
     *
     * 问：
     * next[i]可能==next[i-2]吗？
     * 答：
     * 不可能，反证法。
     * next[6]=2，假设next[8]=next[6]2，则
     * 01=67，得0=6
     * 又因为next[7]=3，得012=456，得2=6
     * 所以0=2，得next[3]至少为1，与next[3]=0前后矛盾。
     *
     */
    public static int[] getNext(char[] str) {
        int[] next = new int[str.length];
        next[0] = -1;
        next[1] = 0;

        // 当前是哪个位置在求next数组的值
        int i = 2;
        // 当前是哪个位置在和i-1比较，初始位置是i-1之前的最大前缀的下一个位置，也就是next[i - 1]
        // 初始值cur=0
        int cur = next[i - 1];
        while (i < next.length) {
            if (str[i - 1] == str[cur]) {
                // 匹配上了，可以右扩得到next[i]
                // i-1之前的前缀的下一位是cur，右扩之后，i之前的前缀的下一位自然是cur+1
                next[i] = cur + 1;
                // 求下一个位置的next数组值
                i++;
                // cur也来到下一个比对位置，实际上就是cur+=1，因为右扩之后i之前的前缀的下一位=i-1之前的前缀的下一位+1
                cur = next[i - 1];
            } else if (cur > 0) {
                // 没匹配上，但是cur没回退到头，继续回退到前一个前缀的下一位
                cur = next[cur];
            } else {
                // 没匹配上，cur也回退到头了，没得比了，赋值0
                next[i++] = 0;
            }
        }
        return next;
    }

    /**
     * getNext方法的代码可以简化
     */
    public static int[] getNextArray(char[] str) {
        int[] next = new int[str.length];
        next[0] = -1;
        next[1] = 0;

        // 当前是哪个位置在求next数组的值
        int i = 2;
        // 当前是哪个位置在和i-1比较，初始位置是i-1之前的最大前缀的下一个位置，也就是next[i - 1]
        // 初始值cur=0
        int cur = 0;
        while (i < next.length) {
            if (str[i - 1] == str[cur]) {
                // 匹配上了，可以右扩得到next[i]
                // i-1之前的前缀的下一位是cur，右扩之后，i之前的前缀的下一位自然是cur+1
                // i来到下一个位置求next数组值
                // cur也来到下一个比对位置，实际上就是cur+=1，因为右扩之后i之前的前缀的下一位=i-1之前的前缀的下一位+1
                /**简化*/
                next[i++] = ++cur;
            } else if (cur > 0) {
                // 没匹配上，但是cur没回退到头，继续回退到前一个前缀的下一位
                cur = next[cur];
            } else {
                // 没匹配上，cur也回退到头了，没得比了，赋值0
                next[i++] = 0;
            }
        }
        return next;
    }

    // 不用for循环了，不好分析复杂度
//        // 遍历每一个位置求next[i]
//        for (int i = 2; i < next.length; i++){
//            // cur初始=i-1
//            int cur = i - 1;
//
//            // 如果str[next[cur]] == str[cur]，就能算next[i]了，否则cur一直左移到next[cur]，直到cur==0
//            while (cur > 0 && str[next[cur]] != str[i - 1]){
//                cur = next[cur];
//            }
//
//            if (cur == 0) {
//                // 如果是因为cur==0退出循环，说明都找完了也没找到最大前后缀，赋值0
//                next[i] = 0;
//            } else {
//                // 否则就是因为i-1之前的最大前缀的后一个 == 最大后缀的后一个，说明在i位置，最大前后缀长度+1
//                next[i] = next[cur] + 1;
//            }
//        }

    /**
     * 分析getNext方法的时间复杂度
     *
     * while循环中3个分支每次只会走1个，所有分支走的次数加起来就是整体次数
     * 对于变量i和cur，分析方式和分析startIndex方法的cur、subCur一摸一样，也是最大循环2N遍，复杂度O(N)
     */

    public static void main(String[] args) {
        String str = "asdfgasd";
        String subStr = "dfa";
        System.out.println(startIndex(str, subStr));
    }
}
