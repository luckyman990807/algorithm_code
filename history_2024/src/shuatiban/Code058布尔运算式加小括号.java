package shuatiban;

/**
 * https://leetcode-cn.com/problems/boolean-evaluation-lcci/
 *
 * 给定一个布尔表达式和一个期望的布尔结果 result，布尔表达式由 0 (false)、1 (true)、& (AND)、 | (OR) 和 ^ (XOR) 符号组成。
 * 实现一个函数，算出有几种可使该表达式得出 result 值的括号方法。
 */
public class Code058布尔运算式加小括号 {
    class Info {
        int trueWays;
        int falseWays;

        public Info(int trueWays, int falseWays) {
            this.trueWays = trueWays;
            this.falseWays = falseWays;
        }
    }

    /**
     * 表达式str从第left位到第right位任意加括号，
     * @param str
     * @param left
     * @param right
     * @return
     */
    public Info process(char[] str, int left, int right) {
        if (left == right) {
            return new Info(str[left] == '1' ? 1 : 0, str[left] == '0' ? 1 : 0);
        }
        int trueWays = 0;
        int falseWays = 0;
        for (int split = left + 1; split < right; split++) {
            Info leftInfo = process(str, left, split - 1);
            Info rightInfo = process(str, split + 1, right);
            switch (str[split]) {
                case '&':
                    trueWays += leftInfo.trueWays * rightInfo.trueWays;
                    falseWays += leftInfo.trueWays * rightInfo.falseWays + leftInfo.falseWays * rightInfo.trueWays + leftInfo.falseWays * rightInfo.falseWays;
                    break;
                case '|':
                    trueWays += leftInfo.trueWays * rightInfo.falseWays + leftInfo.falseWays * rightInfo.trueWays + leftInfo.trueWays * rightInfo.trueWays;
                    falseWays += leftInfo.falseWays * rightInfo.falseWays;
                    break;
                case '^':
                    trueWays += leftInfo.trueWays * rightInfo.falseWays + leftInfo.falseWays * rightInfo.trueWays;
                    falseWays += leftInfo.falseWays * rightInfo.falseWays + leftInfo.trueWays * rightInfo.trueWays;
                    break;
                default:
                    break;
            }
        }
        return new Info(trueWays, falseWays);
    }

    public int countEval(String s, int result) {
        Info info = process(s.toCharArray(), 0, s.length() - 1, new Info[s.length()][s.length()]);
        return result == 1 ? info.trueWays : info.falseWays;
    }

    public Info process(char[] str, int left, int right, Info[][] dp) {
        if (dp[left][right] != null) {
            return dp[left][right];
        }
        Info result;
        if (left == right) {
            result = new Info(str[left] == '1' ? 1 : 0, str[left] == '0' ? 1 : 0);
        } else {
            int trueWays = 0;
            int falseWays = 0;
            for (int split = left + 1; split < right; split++) {
                Info leftInfo = process(str, left, split - 1, dp);
                Info rightInfo = process(str, split + 1, right, dp);
                switch (str[split]) {
                    case '&':
                        trueWays += leftInfo.trueWays * rightInfo.trueWays;
                        falseWays += leftInfo.trueWays * rightInfo.falseWays + leftInfo.falseWays * rightInfo.trueWays + leftInfo.falseWays * rightInfo.falseWays;
                        break;
                    case '|':
                        trueWays += leftInfo.trueWays * rightInfo.falseWays + leftInfo.falseWays * rightInfo.trueWays + leftInfo.trueWays * rightInfo.trueWays;
                        falseWays += leftInfo.falseWays * rightInfo.falseWays;
                        break;
                    case '^':
                        trueWays += leftInfo.trueWays * rightInfo.falseWays + leftInfo.falseWays * rightInfo.trueWays;
                        falseWays += leftInfo.falseWays * rightInfo.falseWays + leftInfo.trueWays * rightInfo.trueWays;
                        break;
                    default:
                        break;
                }
            }
            result = new Info(trueWays, falseWays);
        }
        dp[left][right] = result;
        return result;
    }


}
