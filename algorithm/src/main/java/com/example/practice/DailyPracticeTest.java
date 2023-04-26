package com.example.practice;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DailyPracticeTest {

    @Test
    public void test() {
//        System.out.printf(captureName2("texts"));
        System.out.println(containsTest());
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
