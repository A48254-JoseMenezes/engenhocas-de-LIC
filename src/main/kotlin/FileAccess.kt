import java.io.File

//Possibilita o armazenamento de informação para um ficheiro
object FileAccess {

    //Lê um ficheiro e retorna as linhas presentes
    fun readFile(fileName: String): MutableList<String> {
        val bufferOfLines = mutableListOf<String>()
        File(fileName).forEachLine{bufferOfLines.add(it)}
        return bufferOfLines
    }

    //Escreve num fichiero o conteúdo de linhas presentes numa lista
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