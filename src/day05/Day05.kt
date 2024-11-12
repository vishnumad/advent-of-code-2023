package day05

import readText
import kotlin.math.max
import kotlin.math.min

fun main() {

    class Range(val start: UInt, val len: UInt) {
        val end = start + (len - 1u)

        fun overlap(input: Range): Range? {
            val constrainedStart = max(input.start, this.start)
            val constrainedEnd = min(input.end, this.end)

            if (constrainedEnd <= constrainedStart) {
                // no overlap
                return null
            }

            val constrainedLen = constrainedEnd - constrainedStart + 1u
            return Range(constrainedStart, constrainedLen)
        }

        override fun toString() = "[$start-$end, $len]"
    }

    class Mapper(dest: UInt, src: UInt, len: UInt) {
        val src = Range(src, len)
        val dest = Range(dest, len)

        fun map(input: UInt): UInt? {
            if (input < src.start || input > src.end) return null

            val delta = input - src.start
            return delta + dest.start
        }

        fun map(input: Range): Triple<Range?, Range?, Range?> {
            val overlap = input.overlap(src) ?: return Triple(null, null, null)

            // map overlapping portion to destination
            val delta = overlap.start - src.start
            val mapped = Range(delta + dest.start, overlap.len)

            // left remainder
            var left: Range? = null
            if (overlap.start - input.start > 0u) {
                val length = overlap.start - input.start
                left = Range(input.start, length)
            }

            // right remainder
            var right: Range? = null
            if (input.end - overlap.end > 0u) {
                val length = input.end - overlap.end
                right = Range(overlap.end + 1u, length)
            }

            return Triple(mapped, left, right)
        }

        override fun toString() = "$src -> $dest"
    }

    class RangeMap(data: String) {
        private val name: String
        private val mappers = mutableListOf<Mapper>()

        init {
            val mapData = data.trim().split('\n')
            name = mapData.first()
            for (i in 1..mapData.lastIndex) {
                val rangeParts = mapData[i].trim().split(' ')
                mappers.add(
                    Mapper(
                        dest = rangeParts[0].toUInt(),
                        src = rangeParts[1].toUInt(),
                        len = rangeParts[2].toUInt()
                    )
                )
            }
        }

        fun map(input: UInt): UInt {
            mappers.forEach { mapper ->
                val mapped = mapper.map(input)
                if (mapped != null) {
                    return mapped
                }
            }

            return input
        }

        fun map(initial: List<Range>): List<Range> {
            val results = mutableListOf<Range>()

            val remainders = mappers.fold(initial) { inputs, mapper ->
                val outputs = mutableListOf<Range>()
                for (input in inputs) {
                    val (mapped, left, right) = mapper.map(input)
                    if (mapped == null) {
                        // no mapped value; add the input to output to be checked in next mapper
                        outputs.add(input)
                        continue
                    }

                    // add mapped value to result of map
                    results.add(mapped)

                    // add remainders to output to be checked in next mapper
                    if (left != null) outputs.add(left)
                    if (right != null) outputs.add(right)
                }
                return@fold outputs // pipe output as input of next mapper
            }

            results.addAll(remainders)
            return results
        }

        override fun toString() = "${this.name}\n${this.mappers.joinToString("\n")}"
    }


    fun part1(input: String): UInt {
        val parts = input.split("\n\r")

        val seedData = parts[0]
            .substringAfter(':')
            .trim()
            .split(' ')
            .map { it.toUInt() }

        val maps = mutableListOf<RangeMap>()
        for (i in 1..parts.lastIndex) {
            val mapper = RangeMap(parts[i])
            maps.add(mapper)
        }

        val result = seedData.minOf { seed ->
            maps.fold(seed) { acc, mapper -> mapper.map(acc) }
        }

        return result
    }


    fun part2(input: String): UInt {
        val parts = input.split("\n\r")

        val seedData = parts[0]
            .substringAfter(':')
            .trim()
            .split(' ')
            .map { it.toUInt() }

        val seedRanges = seedData
            .windowed(size = 2, step = 2)
            .map { Range(start = it[0], len = it[1]) }

        val maps = mutableListOf<RangeMap>()
        for (i in 1..parts.lastIndex) {
            val map = RangeMap(parts[i])
            maps.add(map)
        }

        val result = seedRanges
            .flatMap { maps.fold(listOf(it)) { input, mapper -> mapper.map(input) } }
            .minOf { it.start }

        return result
    }

    val input = readText("day05/test")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
