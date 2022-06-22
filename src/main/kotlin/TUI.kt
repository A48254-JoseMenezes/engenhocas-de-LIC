//Possibilita a junção das funcionalidades to LCD e do keyboard
object TUI {

    //Representa os locais onde pode ser escrito a informação
    enum class Location(val offset: Int) { LEFT(0), CENTER(8), RIGHT(16) }

    //Tempo normal de timeout
    const val TIMEOUT = 5000L
    //Valor que, traduzido para char, representa a ausência de caracter
    const val NONE = 0

    //Inicia a classe
    fun init() {
        LCD.init()
        LCD.clear()
    }

    //Limpa o ecrã LCD
    fun clear() = LCD.clear()

    //Escreve no LCD, numa linha e num local passado como parâmetro
    fun write(text: String, line: Int, location: Location) {
        when (location) {
            Location.LEFT -> LCD.cursor(line, location.offset)
            Location.RIGHT -> LCD.cursor(line, (location.offset - text.length))
            Location.CENTER -> LCD.cursor(line, ( location.offset - (text.length / 2) ))
        }
        LCD.write(text)
    }

    //Lê a tecla do keyboard. Se ocorreu timeout, ele retorna ausência de tecla
    fun read(timeout: Long = TIMEOUT) = KBD.waitKey(timeout)

    //Lê do keyboard uma tecla e escreve-a no LCD
    fun writeFromKeyboard() {
        val key = read()
        if (key == NONE.toChar()) print("ERROR: Timeout reached.")
        else {
            write(key.toString(), 0, Location.LEFT )
        }
    }
}

fun main(){
    TUI.init()

/*
    TUI.write("bilhete 1", 0, TUI.Location.LEFT)
    TUI.write("0.50", 0, TUI.Location.RIGHT)
    TUI.write("bilhete 2", 1, TUI.Location.LEFT)
    TUI.write("1.50", 1, TUI.Location.RIGHT)

 */

    while (true) {
        TUI.writeFromKeyboard()
    }
}