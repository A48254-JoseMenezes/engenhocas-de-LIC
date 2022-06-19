import java.io.File
import kotlin.concurrent.timer

object FileAccess {

    fun readFile(fileName: String): MutableList<String> {
        val bufferOfLines = mutableListOf<String>()
        File(fileName).forEachLine{bufferOfLines.add(it)}
        return bufferOfLines
    }

    fun writeFile(fileName: String, lines: MutableList<String>) {
        val writer = File(fileName).printWriter()
        for (i in lines){
            writer.println(i)
        }
        writer.close()
    }
}

fun main() {
    val lines = mutableListOf<String>("Olá", "Como estás?", "Adeus")
    FileAccess.writeFile("Test.txt", lines)
    FileAccess.readFile("Test.txt")
}