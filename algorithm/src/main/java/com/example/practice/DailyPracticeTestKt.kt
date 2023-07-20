package com.example.practice

import org.junit.Test

class DailyPracticeTestKt {

    @Test
    fun test() {
        testQuoteList()
    }

    /************************************************************************************/
    //对于对象，他们是在堆中存储的，引用变量是指向他们的指针，引用变量赋值传递过去给其他引用变量的也是指针
    //所以如果其中的某个引用变量修改了对象中的值，那么其他指向这个对象的引用变量再获取这个对象时，获取到的值也会发生变化
    //所以这里面的初始list和quoteModelList最后都发生了变化
    data class QuoteModel(var name: String, var female: Boolean) {
        override fun toString(): String {
            return "$name is female:$female"
        }
    }

    fun testQuoteList() {
        val list = listOf<QuoteModel>(
            QuoteModel("hong", true),
            QuoteModel("lan", false),
            QuoteModel("hui", false)
        )
        setQuoteModelListUpdate(list)
        updateQuoteItem(2)
        println("-----originList------->")
        list.forEach {
            println(it)
        }
    }

    val quoteModelList = mutableListOf<QuoteModel>()

    fun setQuoteModelListUpdate(data: List<QuoteModel>?) {
        quoteModelList.clear()
        data?.let {
            quoteModelList.addAll(it)
        }
        println("-----originQuoteModelList------->")
        quoteModelList.forEach {
            println(it)
        }
    }

    fun updateQuoteItem(position: Int) {
        quoteModelList[position].female = quoteModelList[position].female != true
        println("-----updateQuoteItem------->")
        quoteModelList.forEach {
            println(it)
        }
    }


    /************************************************************************************/


    /************************************************************************************/
    // "aa", "bb", "cc" 作为String常量，同样还有基本数据类型，存放在线程共享区-->方法区-->运行时常量池
    // 所以它们的list引用变量传出去后再进行修改，并不会改变原来的值
    fun testStringList() {
        val list = listOf<String>("aa", "bb", "cc")
        setDataAndUpdate(list)
        updateItem(2)
        println("******************")
        list.forEach {
            println(it)
        }
    }

    val dataList = mutableListOf<String>()

    fun setDataAndUpdate(data: List<String>?) {
        dataList.clear()
        data?.let {
            dataList.addAll(it)
        }
        dataList.forEach {
            println(it)
        }
    }

    fun updateItem(position: Int) {
        dataList[position] = dataList[position] + "222"
        println("------------>")
        dataList.forEach {
            println(it)
        }
    }

    /************************************************************************************/

    fun testIterator2() {
        val list = mutableListOf<Int>(1, 2, 3, 4, 5)
        val iterator = list.iterator()
        while (iterator.hasNext()) {
            val i = iterator.next()
            if (i == 3) {
                iterator.remove()
            }
        }
        list.forEach {
            println(it)
        }
        //1 2 4 5
    }

    fun testIterator() {
        val list = listOf(1, 2, 3, 4, 5)
        val iterator = list.toMutableList().iterator()
        while (iterator.hasNext()) {
            val i = iterator.next()
            if (i == 3) {
                iterator.remove()
            }
        }
        list.forEach {
            println(it)
        }
        //1 2 3 4 5
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