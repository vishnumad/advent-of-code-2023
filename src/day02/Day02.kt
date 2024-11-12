package day02

import readLines

fun main() {

    data class Round(val r: Int, val g: Int, val b: Int)

    data class Game(val id: Int, val rounds: List<Round>) {
        val power: Int
            get() {
                var minRed = 0
                var minGreen = 0
                var minBlue = 0

                rounds.forEach {
                    if (it.r > minRed) minRed = it.r
                    if (it.g > minGreen) minGreen = it.g
                    if (it.b > minBlue) minBlue = it.b
                }

                return minRed * minGreen * minBlue
            }
    }

    fun parseGame(id: Int, line: String): Game {
        val rounds = line
            .substringAfter(": ")
            .split(';')
            .map { round ->
                var rCount = 0
                var gCount = 0
                var bCount = 0

                round.split(',')
                    .map { it.trim().split(' ') }
                    .forEach {
                        val count = Integer.parseInt(it[0])
                        when (it[1]) {
                            "red" -> rCount += count
                            "green" -> gCount += count
                            "blue" -> bCount += count
                        }
                    }

                Round(rCount, gCount, bCount)
            }

        return Game(id, rounds)
    }

    fun part1(input: List<String>): Int {
        val maxRedCubeCount = 12
        val maxGreenCubeCount = 13
        val maxBlueCubeCount = 14

        val games = input.mapIndexed { index, line ->
            parseGame(index + 1, line)
        }

        return games.sumOf { game ->
            val hasInvalidRound = game.rounds.any {
                it.r > maxRedCubeCount || it.g > maxGreenCubeCount || it.b > maxBlueCubeCount
            }
            if (hasInvalidRound) 0 else game.id
        }
    }

    fun part2(input: List<String>): Int {
        val games = input.mapIndexed { index, line ->
            parseGame(index + 1, line)
        }

        return games.sumOf { it.power }
    }

    val input = readLines("day02/test")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
