package com.example.practice

import org.junit.Test

class MemoryTheory {

    @Test
    fun test() {
        testStringList()
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
        println("******list************")
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
        println("------dataList------>")
        dataList.forEach {
            println(it)
        }
    }

    fun updateItem(position: Int) {
        dataList[position] = dataList[position] + "222"
        println("------updateDataList------>")
        dataList.forEach {
            println(it)
        }
    }

    /************************************************************************************/
}