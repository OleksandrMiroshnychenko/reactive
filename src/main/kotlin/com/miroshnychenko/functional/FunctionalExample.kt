package com.miroshnychenko.functional

import java.util.stream.Collectors

class FunctionalExample

fun main() {
    val namesList = listOf("alex", "ben", "chloe", "adam", "adam");
    val newNamesList = namesGreaterThanSize(namesList, 3)
    println("newNamesList: $newNamesList")
}

private fun namesGreaterThanSize(namesList: List<String>, size: Int): List<String> {
    return namesList.parallelStream().filter { s -> s.length > size }.map(String::uppercase).distinct().sorted().collect(Collectors.toList())
}