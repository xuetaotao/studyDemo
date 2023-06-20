package com.example.practice;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DailyPracticeTest {

    @Test
    public void test() {
//        System.out.printf(captureName2("texts"));
//        System.out.println(containsTest());
//        mapToString();
        getFilePrefix();
    }

    public void getFilePrefix() {
        File f = new File("TileTest.java");
        String fileName = f.getName();
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        System.out.println(prefix);
    }


    public void iteratorUseCorrect() {
        List<String> list = new ArrayList<>();
        list.add("aa");
        list.add("bb");
        list.add("cc");
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (next.equals("bb")) {
                iterator.remove();
            }
        }
        for (String s : list) {
            System.out.println(s);
        }
        //aa cc
    }


    public void testIteratorUseError() {
        List<String> list = new ArrayList<>();
        list.add("aa");
        list.add("bb");
        list.add("cc");
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (next.equals("bb")) {
                list.remove(next);
            }
        }
        for (String s : list) {
            System.out.println(s);
        }
        //这里没有出问题是因为没有涉及到并发场景和共享变量
        //aa cc
    }

    public static boolean containsTest() {
        List<String> list = new ArrayList<>();
        list.add("aa");
        list.add("bb");
        list.add("cc");
        return list.contains(null);//false
//        return list.contains("");//false
    }

    /**
     * Map.toString()后字符串转换回Map
     */
    public static void mapToString() {
        Map<String, Integer> map = new HashMap<>();
        map.put("aa", 1);
        map.put("bb", 2);
        map.put("cc", 3);
        System.out.println("map:" + map);
        String str = map.toString();
        System.out.println("map.toString():" + str);
        String newStr = str.substring(1, str.length() - 1);
        String[] split = newStr.split(",");
        Map<String, Integer> newMap = new HashMap<>();
        for (String s : split) {
            String[] singleStr = s.split("=");
            newMap.put(singleStr[0], Integer.valueOf(singleStr[1]));
        }
        System.out.println("newMap:" + newMap);
        System.out.println("newMap.toString():" + newMap.toString());
    }

    /**
     * 单词首字母大写
     */
    public static String captureName(String name) {
        return name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
    }

    //进行字母的ascii编码前移
    public static String captureName2(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }
}
