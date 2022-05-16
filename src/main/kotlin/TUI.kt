object TUI {
    enum class Location(val offset: Int) { LEFT(0), CENTER(7), RIGHT(15) }


    fun write(text: String, line: Int, location: Location) {
        LCD.clear()
        when(location) {
            Location.LEFT -> LCD.cursor(line, location.offset)
            Location.RIGHT -> LCD.cursor(line, (location.offset - text.length))
            Location.CENTER -> LCD.cursor(line, ( location.offset - (text.length / 2) ))
        }
        //LCD.cursor(line, location.offset)
        LCD.write(text)

    }

}

fun main(){
    LCD.init()
    TUI.write("hellloo", 1, TUI.Location.LEFT)
}