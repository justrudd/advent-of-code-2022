package dev.justrudd.aoc2022.day02

import java.io.File

private const val DEBUG = false

enum class Outcome(val points: Int) {
    Win(6),
    Lose(0),
    Draw(3)
}

sealed class ExpectedOutcome {
    abstract fun choose(other: Choice): Choice

    object Win : ExpectedOutcome() {

        override fun choose(other: Choice) = when(other) {
            is Choice.Rock -> Choice.Paper
            is Choice.Paper -> Choice.Scissors
            is Choice.Scissors -> Choice.Rock
        }
    }

    object Lose : ExpectedOutcome() {

        override fun choose(other: Choice)= when(other) {
            is Choice.Rock -> Choice.Scissors
            is Choice.Paper -> Choice.Rock
            is Choice.Scissors -> Choice.Paper
        }
    }

    object Draw : ExpectedOutcome() {

        override fun choose(other: Choice) = other
    }

    companion object {

        fun fromChar(c: Char) =
            when (c) {
                'X' -> Lose
                'Y' -> Draw
                'Z' -> Win
                else -> throw IllegalArgumentException("Unexpected character $c")
            }
    }
}

sealed class Choice(val points: Int) {

    abstract fun challenge(other: Choice): Outcome

    object Rock : Choice(1) {

        override fun challenge(other: Choice) = when (other) {
            is Rock -> Outcome.Draw
            is Paper -> Outcome.Lose
            is Scissors -> Outcome.Win
        }

        override fun toString() = "Rock"
    }

    object Paper : Choice(2) {

        override fun challenge(other: Choice) = when (other) {
            is Rock -> Outcome.Win
            is Paper -> Outcome.Draw
            is Scissors -> Outcome.Lose
        }

        override fun toString() = "Paper"
    }

    object Scissors : Choice(3) {

        override fun challenge(other: Choice) = when (other) {
            is Rock -> Outcome.Lose
            is Paper -> Outcome.Win
            is Scissors -> Outcome.Draw
        }

        override fun toString() = "Scissors"
    }

    companion object {

        fun fromChar(c: Char) = when (c) {
            'A', 'X' -> Rock
            'B', 'Y' -> Paper
            'C', 'Z' -> Scissors
            else -> throw IllegalArgumentException("Unexpected character $c")
        }
    }
}

data class Round(val elfChoice: Choice, val myChoice: Choice) {

    private val outcome = myChoice.challenge(elfChoice)

    fun score(): Int {
        val points = outcome.points + myChoice.points

        if (DEBUG) {
            println("My $myChoice vs Elf $elfChoice == $outcome scoring $points points")
        }

        return points
    }

    companion object {

        fun fromPartOneString(s: String) =
            parseChoiceChars(s).run {
                val (elfChar, myChar) = this

                Round(Choice.fromChar(elfChar), Choice.fromChar(myChar))
            }

        fun fromPartTwoString(s: String) =
            parseChoiceChars(s).run {
                val (elfChar, eoChar) = this

                val elfChoice = Choice.fromChar(elfChar)
                val myChoice = ExpectedOutcome.fromChar(eoChar).choose(elfChoice)

                Round(elfChoice, myChoice)
            }

        private fun parseChoiceChars(s: String): Pair<Char, Char> {
            val choices = s.split(" ").map { it[0] }
            choices.ifEmpty { throw IllegalArgumentException("Expected only 2 choices in string - $s") }

            return Pair(choices[0], choices[1])
        }
    }
}

fun main() {
    val partOneRounds = mutableListOf<Round>()
    val partTwoRounds = mutableListOf<Round>()
    File("day02/input.txt").forEachLine { line ->
        partOneRounds += Round.fromPartOneString(line)
        partTwoRounds += Round.fromPartTwoString(line)
    }

    val part1TotalScore = partOneRounds.sumOf { it.score() }
    println("Part 1 - $part1TotalScore")

    val part2TotalScore = partTwoRounds.sumOf { it.score() }
    println("Part 2 - $part2TotalScore")
}
