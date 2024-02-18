package com.example.algorithm

import org.junit.Test

/**
 * 青岛大学-王卓老师，数据结构与算法基础
 * https://www.bilibili.com/video/BV1nJ411V7bd?p=20&vd_source=8e9997b417b4b997fd66ceb00b62d13b
 * 2024.02.14
 *
 */
class QingDaoCourse {

    @Test
    fun main() {
        println("*****Qingdao******")
    }

    //构造一个空链表
    fun initLinkedList(): ElemType {
        val list = ElemType()
        list.next = null
        return list
    }

    class ElemType() {
        var score: Int? = 0
        var next: ElemType? = null

        constructor(score: Int?) : this() {
            this.score = score
        }

        constructor(score: Int, next: ElemType) : this() {
            this.score = score
            this.next = next
        }
    }
}