package com.example.practice

import org.junit.Test
import java.util.Arrays
import java.util.Comparator

/**
 * 大多数内置类型是可比较的：
 * 数值类型使用传统的数值顺序：1 大于 0； -3.4f 大于 -5f，以此类推。
 * Char 和 String 使用字典顺序： b 大于 a； world 大于 hello。
 */
class CollectionSort {

    @Test
    fun test() {
//        versionCompareTo()
//        userCompareTo()
//        studentCompare()
//        sortMethod()
//        sortedMethod()
//        sortedMethod2()
//        sortByMethod()
//        sortedByMethod()
//        sortWith()
        sortedWith()
    }

    /**
     * https://www.bezkoder.com/kotlin-sort-list/
     * sortedWith() and reversed() return a sorted List instead of changing original List order.
     */
    private fun sortedWith() {
        val students = mutableListOf(
            Student("BB", 16),
            Student("AA", 17),
            Student("AA", 19),
            Student("CC", 18),
            Student("CC", 16)
        )
        val sortedStudents = students.sortedWith(compareBy<Student> { it.name }.thenBy { it.age })
        students.forEach {
            println("${it.name} \t ${it.age}")//顺序不变
        }
        println()
        sortedStudents.forEach {
            println("${it.name} \t ${it.age}")
            //AA 	 17
            //AA 	 19
            //BB 	 16
            //CC 	 16
            //CC 	 18
        }
        println("\n--------------")
        val reversedStudents = sortedStudents.reversed()
        reversedStudents.forEach {
            println("${it.name} \t ${it.age}")
            //CC 	 18
            //CC 	 16
            //BB 	 16
            //AA 	 19
            //AA 	 17
        }
    }

    /**
     * How about continue to sort age after sorting name?
     * We’re gonna use sortWith() for ascending order and additional method reverse() for descending order.
     * 和sortBy相比，sortWith适合需要对多个判断条件排序的情况
     */
    private fun sortWith() {
        val students = mutableListOf(
            Student("BB", 16),
            Student("AA", 17),
            Student("AA", 19),
            Student("CC", 18),
            Student("CC", 16)
        )
        students.sortWith(compareBy<Student> { it.name }.thenBy { it.age })
        students.forEach {
            println("${it.name} \t ${it.age}")
            //AA 	 17
            //AA 	 19
            //BB 	 16
            //CC 	 16
            //CC 	 18
        }
        println("\n-------------------reversed----------------")
//        students.reverse()
//        students.forEach {
//            println("${it.name} \t ${it.age}")
//            //CC 	 18
//            //CC 	 16
//            //BB 	 16
//            //AA 	 19
//            //AA 	 17
//        }
        println("\n-------------------compareByDescending----------------")
        students.sortWith(compareByDescending<Student> { it.name }.thenBy { it.age })
//        students.sortWith(compareByDescending<Student> { it.name }.thenByDescending { it.age })//这个结果和reverse一样
        students.forEach {
            println("${it.name} \t ${it.age}")
            //CC 	 16
            //CC 	 18
            //BB 	 16
            //AA 	 17
            //AA 	 19
        }
    }

    /**
     * Instead of changing the order of original List. sortedBy() and sortedByDescending() return a sorted List, the original List isn’t affected.
     */
    private fun sortedByMethod() {
        val students = mutableListOf(Student("BB", 16), Student("AA", 17), Student("CC", 18))
        val sortedStudents = students.sortedBy { it.name }
        students.forEach {
            print("${it.name} ")//BB AA CC
        }
        println()
        sortedStudents.forEach {
            print("${it.name} ")//AA BB CC
        }
        println("\n---------sortedByDescending----------------")
        val sortedByDescendingStudents = students.sortedByDescending { it.name }
        students.forEach {
            print("${it.name} ")//BB AA CC
        }
        println()
        sortedByDescendingStudents.forEach {
            print("${it.name} ")//CC BB AA
        }
    }

    /**
     * sortBy() helps us to sort a Mutable List in-place by specific field. We need to pass a selector function as an argument.
     * For descending order, we use sortByDescending().
     */
    private fun sortByMethod() {
        val students = mutableListOf(Student("AA", 16), Student("BB", 17), Student("CC", 18))
        students.sortBy { it.name }
        students.forEach {
            print("${it.name} ")//AA BB CC
        }
        println()
        students.sortByDescending { it.name }
        students.forEach {
            print("${it.name} ")//CC BB AA
        }
    }

    /**
     * You can use sort() method to sort a Mutable List in-place, and sortDescending() for descending order.
     */
    private fun sortMethod() {
        val numbs = mutableListOf(3, 1, 7, 2, 8, 6)
        numbs.sort()
        numbs.forEach {
            print("$it ")//1 2 3 6 7 8
        }

        println()
        numbs.sortDescending()
        numbs.forEach {
            print("$it ")//8 7 6 3 2 1
        }
    }

    /**
     * sorted() and sortedDescending() don’t change the original List. Instead, they return another sorted List.
     */
    private fun sortedMethod() {
        val numbs = mutableListOf(3, 1, 7, 2, 8, 6)
        val sortedNumbs = numbs.sorted()
        numbs.forEach {
            print("$it ")//3 1 7 2 8 6
        }
        println()
        sortedNumbs.forEach {
            print("$it ")//1 2 3 6 7 8
        }
    }

    private fun sortedMethod2() {
        val numbs = mutableListOf(3, 1, 7, 2, 8, 6)
        val sortedNumbs = numbs.sortedDescending()
        numbs.forEach {
            print("$it ")//3 1 7 2 8 6
        }
        println()
        sortedNumbs.forEach {
            print("$it ")//8 7 6 3 2 1
        }
    }

    private fun studentCompare() {
        val students = arrayOf(Student("AA", 16), Student("BB", 17), Student("CC", 18))
        Arrays.sort(students, object : Comparator<Student> {
            /**
             * 返回负数的时候，第一个参数排在前面
             * 返回正数的时候，第二个参数排在前面
             * 返回0的时候，谁在前面无所谓
             */
            override fun compare(o1: Student, o2: Student): Int {
                //第一种写法，易懂
//                if (o1.age < o2.age) {
//                    return -1//o1 o2，这是age升序排列
//                }
//                if (o1.age > o2.age) {
//                    return 1//o2 o1，这也是age升序排列
//                }
//                return 0//o1 o2 或者 o2 o1

                //第二种写法，简便
                return o1.age - o2.age;//age升序排列
            }
        })
        students.forEach {
            println(it.name + it.age)
            //AA16
            //BB17
            //CC18
        }
    }

    class Student(val name: String, val age: Int)

    private fun userCompareTo() {
        //Char 和 String 使用字典顺序： b 大于 a； world 大于 hello。
        println(User("A") > User("B"))
        val userList = mutableListOf(User("A"), User("B"), User("C"))
        //等价于Collections.sort(userList)
        userList.sort()
        userList.forEach {
            println(it.name)
        }
    }

    class User(val name: String) : Comparable<User> {
        override fun compareTo(other: User): Int {
            //可以把compareTo方法左边的参数当作第一个参数，右边的当作第二个参数，然后类比compare方法
            return this.name.compareTo(other.name)//A B C，升序，User("A") < User("B")
//            return other.name.compareTo(this.name)//C B A，降序，User("A") > User("B")
        }
    }

    private fun versionCompareTo() {
        println(Version(1, 2) > Version(1, 3))//false
        println(Version(2, 0) > Version(1, 5))//true
    }

    /**
     * 1、比较者大于被比较者（也就是compareTo方法里面的对象），那么返回正整数
     * 2、比较者等于被比较者，那么返回0
     * 3、比较者小于被比较者，那么返回负整数
     */
    class Version(private val major: Int, private val minor: Int) : Comparable<Version> {
        /**
         * Compares this object with the specified object for order.
         * Returns zero if this object is equal to the specified other object, a negative number if it's less than other,
         * or a positive number if it's greater than other.
         */
        override fun compareTo(other: Version): Int {
            return if (this.major != other.major) {
                this.major - other.major
            } else if (this.minor != other.minor) {
                this.minor - other.minor
            } else {
                0
            }
        }
    }
}