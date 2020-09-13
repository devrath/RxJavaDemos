package com.demo.code

import com.demo.code.models.Task


object  Utils {
    fun getArrayOfTasks(): Array<Task> {
        return Array(5) {
            Task("Task1", true, 3)
            Task("Task2", false, 2)
            Task("Task3", true, 1)
            Task("Task4", false, 0)
            Task("Task5", true, 5)
        }
    }
    fun getListOfTasks(): MutableList<Task> {
        val list: MutableList<Task> = ArrayList()
        list.add(Task("Task1", true, 3))
        list.add(Task("Task2", false, 3))
        list.add(Task("Task3", true, 1))
        list.add(Task("Task4", false, 0))
        list.add(Task("Task5", true, 5))
        return list
    }
}