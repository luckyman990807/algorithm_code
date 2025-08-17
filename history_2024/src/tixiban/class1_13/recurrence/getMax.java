package tixiban.class1_13.recurrence;

public class getMax {
    public static int getMax(int[] arr, int left, int right) {
        // 递归的首要问题是边界，也就是递归的出口
        if (left == right) {
            return arr[left];
        }
        // 然后把问题拆成规模较小的子问题
        int mid = left + ((right - left) >> 1);
        int leftMax = getMax(arr, left, mid);
        int rightMax = getMax(arr, mid + 1, right);
        // 最后由子问题的解整合出原问题的解
        return Math.max(leftMax, rightMax);
    }


}
