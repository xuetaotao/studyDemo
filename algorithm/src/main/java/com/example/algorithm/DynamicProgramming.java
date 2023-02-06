package com.example.algorithm;

/**
 * 动态规划（Dynamic Programming, DP）
 * <a href="https://www.bilibili.com/video/BV1AB4y1w7eT/?spm_id_from=333.337.search-card.all.click&vd_source=8e9997b417b4b997fd66ceb00b62d13b">...</a>
 * <a href="https://oi-wiki.org/dp/">...</a>
 * 动态规划是由下而上（先解决小问题最后到大问题）,它会储存每个小问题的结果，从而它的计算速度会比递归要快。
 * （代价是动态规划的空间复杂度更高，即用空间换取的时间）。
 * <p>
 * 经典问题：斐波那契数列；最长递增子序列；
 */
public class DynamicProgramming {
    /**
     * [1,5,2,4,3]，找出最长的最长递增子序列（的长度）
     * 解法1：暴力枚举法 / 暴力搜索
     * 时间复杂度：O(n*2^n)
     */
    public static void longestIncreaseSub1(int[] arr) {
        int maxSubLen = 1;
        for (int i = 0; i < arr.length; i++) {
            maxSubLen = Math.max(maxSubLen, help(arr, i));
        }
        System.out.println(maxSubLen);
    }

    //返回从数组第i个数字开始的最长子序列长度
    public static int help(int[] arr, int i) {
        //最后一个数字后面没有子序列了
        if (i == arr.length - 1) {
            return 1;
        }
        int maxLen = 1;
        //检查i后面的所有数字，将索引标记为j，遍历所有的j，选出最长的子序列长度返回
        for (int j = i + 1; j < arr.length; j++) {
            if (arr[j] > arr[i]) {
                //递归去计算从j开始的最长子序列长度 ，+1得到目前这个序列的总长度
                int x = help(arr, j) + 1;
                maxLen = Math.max(maxLen, x);
            }
        }
        return maxLen;
    }
}
