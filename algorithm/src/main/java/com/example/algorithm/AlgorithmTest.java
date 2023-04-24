package com.example.algorithm;

import org.junit.Test;

/**
 * 字符串，链表（单和双），二叉树，堆，栈，队列，哈希，图
 * 排序算法，查找算法，递归，动态规划，贪心算法
 * 线性表：数组和链表
 */
public class AlgorithmTest {

    @Test
    public void hj01() {
        int[] arr = new int[]{1, 5, 2, 4, 3};
//        DynamicProgramming.longestIncreaseSub3(arr);
//        quickSort(arr);
//        List<Integer> list = new ArrayList<>();
//        list.add(1);
//        list.add(2);
//        list.add(3);
//        list.add(0, 8);
//        for (Integer integer : list) {
//            System.out.println(integer);
//        }
        String aa = "12 days / 13 hours";
        String bb = aa.replace("/", "");
        System.out.println(bb);
        String[] split = bb.split(" ");
        System.out.println(split.length);
        for (String s : split) {
            System.out.print(s + "\t");
        }
    }

    public void quickSort(int[] array) {
        if (array == null || array.length < 2) {
            return;
        }
        quickSort(array, 0, array.length - 1);
        for (int j : array) {
            System.out.println(j);
        }
    }

    public void quickSort(int[] arr, int left, int right) {
        if (left < right) {
            int randomIndex = left + (int) (Math.random() * (right - left + 1));
            swap(arr, randomIndex, right);
            int[] p = partition(arr, left, right);
            quickSort(arr, left, p[0] - 1);
            quickSort(arr, p[1] + 1, right);
        }
    }

    public int[] partition(int[] arr, int left, int right) {
        int less = left - 1;
        int more = right;
        while (left < more) {
            if (arr[left] < arr[right]) {
                swap(arr, ++less, left++);
            } else if (arr[left] > arr[right]) {
                swap(arr, --more, left);
            } else {
                left++;
            }
        }
        swap(arr, more, right);
        return new int[]{less + 1, more};
    }

    public void swap(int[] arr, int left, int right) {
        int temp = arr[left];
        arr[left] = arr[right];
        arr[right] = temp;
    }
}