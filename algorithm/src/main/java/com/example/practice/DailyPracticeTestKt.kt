package com.example.practice

import org.junit.Test

class DailyPracticeTestKt {

    @Test
    fun test() {
        testTakeWhile()
    }

    /**
     * 关于 kotlin 的 forEach 如何实现 break/continue 的思考:
     * https://blog.csdn.net/knight1996/article/details/112271828
     *
     * return@forEach 表示是是否终止这次 lambda 的进行执行，for 循环还会继续，这种写法和 continue 的效果是一致的
     * forEach 源码很简单，就是循环执行 action 这个函数，这个 action 就是我们传入的 lambda，所有我们 return@forEach 只会影响一次，整体的 for 循环不会被终止的。
     */
    private fun testForEach() {
        listOf(1, 2, 3, 4, 5).forEach { one ->
            if (one == 3) {
                return@forEach
            }
            println(one)//1 2 4 5
        }
    }

    /**
     * takeWhile 表示从头开始取，一直取到不满足条件的，所以从头开始2之前的元素都被取出来了。使用集合框架api的就可以很轻松的搞定。
     */
    private fun testTakeWhile() {
        listOf(1, 2, 3, 4, 5).takeWhile { it != 3 }.forEach { println(it) }//1 2
    }
}