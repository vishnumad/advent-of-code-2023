package day03

import readInput

typealias Gear = Pair<Int, Int>

fun main() {

    fun List<String>.checkSurrounding(x: Int, y: Int, callback: (Char, Int, Int) -> Unit) {
        val isLeftBound = x == 0
        val isRightBound = x == this[y].lastIndex
        val isTopBound = y == 0
        val isBottomBound = y == this.lastIndex

        if (!isLeftBound) callback(this[y][x - 1], x - 1, y)                            // left
        if (!isTopBound && !isLeftBound) callback(this[y - 1][x - 1], x - 1, y - 1)     // top-left
        if (!isTopBound) callback(this[y - 1][x], x, y - 1)                             // top
        if (!isTopBound && !isRightBound) callback(this[y - 1][x + 1], x + 1, y - 1)    // top-right
        if (!isRightBound) callback(this[y][x + 1], x + 1, y)                           // right
        if (!isBottomBound && !isRightBound) callback(this[y + 1][x + 1], x + 1, y + 1) // bottom-right
        if (!isBottomBound) callback(this[y + 1][x], x, y + 1)                          // bottom
        if (!isBottomBound && !isLeftBound) callback(this[y + 1][x - 1], x - 1, y + 1)  // bottom-left
    }

    fun part1(input: List<String>): Int {
        var sum = 0

        val numberData = mutableListOf<Char>()
        var isPartNumber = false

        for (y in input.indices) {
            for (x in input[y].indices) {
                val c = input[y][x]
                if (!c.isDigit()) continue

                numberData.add(c)

                input.checkSurrounding(x, y) { s, _, _ ->
                    if (!(s.isDigit() || s == '.')) {
                        isPartNumber = true
                    }
                }

                val isLastDigit = x == input[y].lastIndex || !input[y][x + 1].isDigit()
                if (isLastDigit) {
                    if (isPartNumber) {
                        sum += numberData.joinToString("").toInt()
                    }

                    numberData.clear()
                    isPartNumber = false
                }
            }
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        val gearMap = HashMap<Gear, MutableList<Int>>()

        val numberData = mutableListOf<Char>()
        val nearbyGears = mutableSetOf<Gear>()

        for (y in input.indices) {
            for (x in input[y].indices) {
                val c = input[y][x]
                if (!c.isDigit()) continue

                numberData.add(c)

                input.checkSurrounding(x, y) { s, sx, sy ->
                    if (s == '*') {
                        nearbyGears.add(Gear(sx, sy))
                    }
                }

                val isLastDigit = x == input[y].lastIndex || !input[y][x + 1].isDigit()
                if (isLastDigit) {
                    val num = numberData.joinToString("").toInt()
                    nearbyGears.forEach {
                        val nearbyNumbers = gearMap.getOrDefault(it, mutableListOf())
                        nearbyNumbers.add(num)
                        gearMap[it] = nearbyNumbers
                    }

                    numberData.clear()
                    nearbyGears.clear()
                }
            }
        }

        return gearMap.values.sumOf { if (it.size == 2) it[0] * it[1] else 0 }
    }

    val input = readInput("day03/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
