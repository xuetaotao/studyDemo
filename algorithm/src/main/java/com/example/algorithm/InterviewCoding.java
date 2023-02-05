package com.example.algorithm;

/**
 * 面试 Coding 代码（非算法）
 */
public class InterviewCoding {

    /**
     * 单例模式
     * 腾讯面过，小鱼易连面过
     */
    public static volatile InterviewCoding instance;

    public InterviewCoding getInstance() {
        if (instance == null) {
            synchronized (InterviewCoding.class) {
                if (instance == null) {
                    instance = new InterviewCoding();
                }
            }
        }
        return instance;
    }
}
