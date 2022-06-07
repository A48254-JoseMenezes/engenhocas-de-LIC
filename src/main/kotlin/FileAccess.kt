import java.io.File

object FileAccess {

    fun readFile(fileName: String ): MutableList<String> {
        val bufferOfLines = mutableListOf<String>()
        File(fileName).forEachLine{bufferOfLines.add(it)}
        return bufferOfLines
    }
}

fun main() {
    println(FileAccess.readFile("BILHETES_VENDIDOS"))
}