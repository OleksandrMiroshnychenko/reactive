package com.miroshnychenko.imperative

import java.util.ArrayList

fun main() {
    val namesList = listOf("alex", "ben", "chloe", "adam", "adam")
    val newNamesList = namesGreaterThanSize(namesList)
    println("newNamesList: $newNamesList")
}

private fun namesGreaterThanSize(namesList: List<String>): List<String> {
    val newNamesList = ArrayList<String>()
    for (name in namesList) {
        if (name.length > 3 && !newNamesList.contains(name)) {
            newNamesList.add(name.uppercase())
        }
    }
    return newNamesList
}
