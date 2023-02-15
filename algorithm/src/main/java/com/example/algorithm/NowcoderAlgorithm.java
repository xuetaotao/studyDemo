package com.example.algorithm;

/**
 * 牛客网算法篇：
 * <a href="https://www.nowcoder.com/exam/oj?page=1&tab=%E7%AE%97%E6%B3%95%E7%AF%87&topicId=295">...</a>
 */
@SuppressWarnings("unused")
public class NowcoderAlgorithm {

    /**
     * 二分查找/排序
     * <p>
     * BM17 二分查找-I
     * 请实现无重复数字的升序数组的二分查找
     * 时间复杂度O(logN),空间复杂度O(1)
     */
    public int bm17(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    /**
     * BM18 二维数组中的查找
     * 时间复杂度O(n+m): 遍历矩阵的时候，最多经过矩阵的一行一列
     * 空间复杂度O(1):常数级变量，无额外辅助空间
     * 思路：首先以左下角为起点，若是它小于目标元素，则往右移动去找大的，若是他大于目标元素，则往上移动去找小的。
     */
    public boolean bm18(int target, int[][] array) {
        if (array.length == 0) {
            return false;
        }
        //获取行数
        int n = array.length;
        //列数为0
        if (array[0].length == 0) {
            return false;
        }
        int m = array[0].length;
        //从最左下角的元素开始往左或往上
        for (int i = n - 1, j = 0; i >= 0 && j <= m - 1; ) {
            //元素较大，往上走
            if (array[i][j] > target) {
                i--;
            } else if (array[i][j] < target) {
                //元素较小，往右走
                j++;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * BM19 寻找峰值
     * 时间复杂度O(logN),二分法最坏情况对整个数组连续二分，最多能分logN次
     * 空间复杂度O(1),常数级变量，无额外辅助空间
     */
    public int bm19(int[] array) {
        int left = 0;
        int right = array.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            //右边是往下，不一定有坡峰
            if (array[mid] > array[mid + 1]) {
                right = mid;
            } else {
                //右边是往上，一定能找到波峰
                left = mid + 1;
            }
        }
        //其中一个波峰
        return right;
    }
}
