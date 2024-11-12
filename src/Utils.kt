import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText


fun readLines(name: String) = Path("src/$name.txt").readLines()

fun readText(name: String) = Path("src/$name.txt").readText()
