package tixiban.class16MatrixFastPower;

/**
 * 第一年农场有1只成熟的母牛，之后的每年：
 * 1、每一只成熟的母牛都会生1只小母牛
 * 2、每只新出生的小母牛都在3年后成熟
 * 3、每只母牛都不会死
 * 求n年后母牛的数量
 *
 * 思路：
 * 递推式f(n)=f(n-1)+f(n-3)
 * 前一年就有然后活到今年的牛 + 今年新出生的牛
 * 前一年的牛就是f(n-1)会活到今年，今年新出生的牛都是3年前存在的牛生的，就是f(n-3)。f(n-2)比f(n-3)多出来的部分不会生小牛，会活到f(n-1)进而活到f(n)
 *
 * |f(n),f(n-1),f(n-2)| = |f(n-1),f(n-2),f(n-3)| * |A|
 *       |a b c|
 *       |d e f|
 * |A| = |g h i|，带入，乘开，得
 * |f(n),f(n-1),f(n-2)| = |af(n-1)+df(n-2)+gf(n-3), bf(n-1)+ef(n-2)+hf(n-3), cf(n-1)+ff(n-2)+if(n-3)|
 * 行列式第1个元素相等，f(n) = af(n-1)+df(n-2)+gf(n-3)，带入递推公式得a=1,d=0,g=1
 * 行列式第2个元素相等，f(n-1) = bf(n-1)+ef(n-2)+hf(n-3)，带入递推公式得f(n) = bf(n-1)+ef(n-2)+(h+1)f(n-3)，得b=1,e=0,h=0
 * 行列式第3个元素相等，f(n-2) = cf(n-1)+ff(n-2)+if(n-3)，得c=0,f=1,i=0
 *          |1 1 0|
 *          |0 0 1|
 * 所以|A| = |1 0 0|
 *
 * |f(n),f(n-1),f(n-2)| = |f(3),f(2),f(1)| * |A|^n-3
 * 先求A的矩阵快速幂，再带入前三项，f(n)可求
 */
public class Code02CountOfCowNYearsAgo {
    public static int countCow(int n){
        if (n==1) {
            return 1;
        }
        if (n==2) {
            return 2;
        }
        if (n==3) {
            return 3;
        }
        int[][] matrix = {{1,1,0}, {0,0,1}, {1,0,0}};
        matrix = Code0快速幂和笔记.matrixFastPower(matrix, n - 3);
        return 3 * matrix[0][0] + 2 * matrix[1][0] + matrix[2][0];
    }

    public static void main(String[] args) {
        System.out.println(countCow(19));
    }
}
