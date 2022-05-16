object TUI {

    const val SIZE_LINE_LCD = 15

    enum class Location(val offset: Int) { LEFT(0), CENTER(7), RIGHT(10) }


    fun write(text: String, line: Int, location: Location) {
        LCD.clear()
        LCD.cursor(line, location.offset)
        LCD.write(text)

    }

}

fun main(){
    TUI.write("hellloo", 1, TUI.Location.LEFT)
}