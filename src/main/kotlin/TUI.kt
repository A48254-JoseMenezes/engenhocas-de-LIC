object TUI {

    enum class Location(val offset: Int) { LEFT(0), CENTER(8), RIGHT(16) }

    const val TIMEOUT = 5000L
    const val NONE = 0

    fun init() {
        LCD.init()
        LCD.clear()
    }

    fun clear() = LCD.clear()

    fun write(text: String, line: Int, location: Location) {
        when (location) {
            Location.LEFT -> LCD.cursor(line, location.offset)
            Location.RIGHT -> LCD.cursor(line, (location.offset - text.length))
            Location.CENTER -> LCD.cursor(line, ( location.offset - (text.length / 2) ))
        }
        LCD.write(text)
    }

    fun read() = KBD.waitKey(TIMEOUT)

    fun readInteger() {
        val key = read()
        when (key) {
            '*' -> write("",0, Location.CENTER)
            '#' -> LCD.clear()
            else -> write(key.toString(), 0, Location.LEFT)
        }
    }

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