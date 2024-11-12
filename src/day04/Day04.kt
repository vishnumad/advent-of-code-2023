package day04

import readLines
import kotlin.math.pow

fun main() {

    fun matchingNumbers(line: String): Set<String> {
        val cardData = line
            .substringAfter(':')
            .split('|')

        val winningNumbers = cardData[0].split(' ').filter(String::isNotBlank)
        val scratchedNumbers = cardData[1].split(' ').filter(String::isNotBlank)

        return winningNumbers intersect scratchedNumbers
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val matchingNumbers = matchingNumbers(line)
            if (matchingNumbers.isEmpty()) return@sumOf 0

            (2.0).pow(matchingNumbers.size - 1).toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val matchingList = input.map { matchingNumbers(it) }
        val copies = HashMap<Int, Int>()

        matchingList.forEachIndexed { i, matches ->
            val numCopies = copies.getOrPut(i) { 1 }
            val numMatches = matches.size

            repeat(numMatches) { j ->
                val idx = i + j + 1
                val v = copies.getOrPut(idx) { 1 }
                copies[idx] = v + numCopies
            }
        }

        return copies.values.sum()
    }

    val input = readLines("day04/test")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
