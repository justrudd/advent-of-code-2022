package dev.justrudd.aoc2022.day01

import java.io.File
import kotlin.text.Charsets.US_ASCII

@JvmInline
value class Food(val calories: Long)

data class Elf(
    val id: Int,
    val foods: MutableList<Food> = mutableListOf()
    ) {

    fun totalCalories() =
        foods.sumOf { it.calories }

}

fun main() {
    val elves = mutableListOf<Elf>()
    elves += Elf(1)
    File("day01/input.txt").forEachLine(US_ASCII) { line ->
        if (line.isBlank()) {
            elves += Elf(elves.last().id + 1)
        } else {
            elves.last().foods += Food(line.toLong())
        }
    }

    val elf = elves.maxBy { it.totalCalories() }
    println("Part One - ${elf.totalCalories()}")

    val caloriesOfTop3 =
        elves
            .sortedByDescending { it.totalCalories() }
            .take(3)
            .sumOf { it.totalCalories() }
    println("Part Two - $caloriesOfTop3")
}
