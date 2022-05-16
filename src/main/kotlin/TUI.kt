object TUI {

    const val SIZE_LINE_LCD = 15

    enum class Location(val offset:Int){ LEFT(0), CENTER(7), RIGHT(14) }


    fun write(text: String, line: Int, location: Location){
        LCD.clear()
        if(text.length > SIZE_LINE_LCD ){
            val lowSubstring = text.substring(startIndex = 0,endIndex = SIZE_LINE_LCD)
            LCD.cursor(0,0)
            LCD.write(lowSubstring)

            val highSubstring = text.substring(SIZE_LINE_LCD)
            LCD.cursor(1,0)
            LCD.write(highSubstring)

        }
    }
}



fun main(){
    TUI.write("hellloo", 1, TUI.Location.LEFT)
}