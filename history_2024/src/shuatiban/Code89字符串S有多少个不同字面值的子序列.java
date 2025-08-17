package shuatiban;

/**
 * https://leetcode.com/problems/distinct-subsequences-ii/
 * 思路：以i位置结尾的前缀串，有多少个不同字面值的子序列；把i+1也算进来，以i+1位置结尾的前缀串，有多少个不同字面值的子序列……
 * 不过这个状态转移比较简单，不需要动态规划了
 *
 * 算到i的时候，以i位置的字符c结尾的前缀串，有多少不同字面值的子序列，怎么算呢？
 * 首先，i-1的结果preAll直接继承过来，对应着不取i位置的可能性
 * 其次，preAll的末尾直接加上i位置，产生新的子序列newAdd，数量newAdd=preAll
 * 最后，减去newAdd中的重复值，因为计算上一个c的时候，也会拿上一个c的preAll'末尾追加c得到newAdd'，newAdd'和newAdd有重复的。
 * 那么重复的是哪些呢？就是newAdd'，因为得到newAdd'的preAll'会被代代继承下来，直到计算当前c的时候，也会用这些newAdd'追加c产生新子序列算到newAdd中
 * 所有减去的时上一个c字符对应的newAdd'，只要迭代过程中，用map记录每一个字符对应的newAdd即可。
 */
public class Code89字符串S有多少个不同字面值的子序列 {
    public static int distinctSubseqII(String s) {
        int all = 1;
        int[] map = new int[26];
        char[] str = s.toCharArray();
        int m = 1000000007;
        for (char c : str) {
            // 首先继承i-1位置的结果
            int newAll = all;
            // 然后在上一步结果基础上，末尾都追加c得到一批新结果。每一步都可能越界，所以每一步都要取余
            newAll = (newAll + all) % m;
            // 减去重复的，也就是上一个c字符的newAdd。+m是为了防止前面两数相减得到负数，+m再取余就能保证结果是正数
            newAll = (newAll - map[c - 'a'] + m) % m;
            // 记录最新的map
            map[c - 'a'] = all;
            // 更新最新的结果
            all = newAll;
        }
        return (all - 1 + m) % m;
    }

    public static void main(String[] args) {
        String s = "yezruvnatuipjeohsymapyxgfeczkevoxipckunlqjauvllfpwezhlzpbkfqazhexabomnlxkmoufneninbxxguuktvupmpfspwxiouwlfalexmluwcsbeqrzkivrphtpcoxqsueuxsalopbsgkzaibkpfmsztkwommkvgjjdvvggnvtlwrllcafhfocprnrzfoyehqhrvhpbbpxpsvomdpmksojckgkgkycoynbldkbnrlujegxotgmeyknpmpgajbgwmfftuphfzrywarqkpkfnwtzgdkdcyvwkqawwyjuskpvqomfchnlojmeltlwvqomucipcwxkgsktjxpwhujaexhejeflpctmjpuguslmzvpykbldcbxqnwgycpfccgeychkxfopixijeypzyryglutxweffyrqtkfrqlhtjweodttchnugybsmacpgperznunffrdavyqgilqlplebbkdopyyxcoamfxhpmdyrtutfxsejkwiyvdwggyhgsdpfxpznrccwdupfzlubkhppmasdbqfzttbhfismeamenyukzqoupbzxashwuvfkmkosgevcjnlpfgxgzumktsexvwhylhiupwfwyxotwnxodttsrifgzkkedurayjgxlhxjzlxikcgerptpufocymfrkyayvklsalgmtifpiczwnozmgowzchjiop";
        System.out.println(distinctSubseqII(s));
    }
}
