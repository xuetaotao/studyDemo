package com.example.algorithm;

/**
 * 查找算法
 */
@SuppressWarnings("unused")
public class SearchAlgorithm {

    /**
     * 二分查找
     * 时间复杂度：O(logN)
     * 优势：最省内存，对数时间复杂度
     * 局限性：依赖于顺序表，依赖于数据有序，不适用于数据量太大的场景
     * <a href="https://juejin.cn/post/6933241413341708296">...</a>
     * <a href="https://leetcode.cn/problems/binary-search/">...</a>
     */
    public int binarySearch(int[] arr, int target) {
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
}
