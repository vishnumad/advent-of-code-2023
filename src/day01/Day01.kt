package day01

import readLines

fun main() {

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val firstDigit = line.first { it.isDigit() }
            val lastDigit = line.last { it.isDigit() }

            Integer.parseInt("$firstDigit$lastDigit")
        }
    }

    val numberMap = mapOf(
        "one" to '1',
        "two" to '2',
        "three" to '3',
        "four" to '4',
        "five" to '5',
        "six" to '6',
        "seven" to '7',
        "eight" to '8',
        "nine" to '9'
    )

    fun part2(input: List<String>): Int {
        val searchValues = numberMap.values
            .map { it.toString() }
            .plus(numberMap.keys)

        return input.sumOf { line ->
            val (_, firstDigitOrString) = line.findAnyOf(searchValues)!!
            val (_, lastDigitOrString) = line.findLastAnyOf(searchValues)!!

            val firstDigit = if (firstDigitOrString[0].isDigit()) {
                firstDigitOrString[0]
            } else {
                numberMap[firstDigitOrString]
            }

            val lastDigit = if (lastDigitOrString[0].isDigit()) {
                lastDigitOrString[0]
            } else {
                numberMap[lastDigitOrString]
            }

            Integer.parseInt("$firstDigit$lastDigit")
        }
    }

    val input = readLines("day01/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
