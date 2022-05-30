
object KBD { // Ler teclas. Métodos retornam ‘0’..’9’,’#’,’*’ ou NONE.
    const val NONE = 0;
    // Inicia a classe
    fun init() {
        KeyReceiver.init()
    }

    // Retorna de imediato a tecla premida ou NONE se não há tecla premida.
    fun getKey(): Char {
        val keyCode = KeyReceiver.rcv()

        if(keyCode == -1) return NONE.toChar()

        val listChar = listOf('1','4','7', '*', '2', '5', '8', '0', '3', '6', '9', '#')

        return listChar[keyCode]
    }

    // Retorna quando a tecla for premida ou NONE após decorrido ‘timeout’ milisegundos.
    fun waitKey(timeout: Long): Char{
        val startTime = System.currentTimeMillis()
        var time: Long = 0L

        while(time < timeout){
            println("estamos a ver o tempo")
            val value = getKey()
            if (value != NONE.toChar()) return value
            time = System.currentTimeMillis() - startTime
        }

        return NONE.toChar()
    }
}


fun main(){
    KBD.init()
    println(KBD.waitKey(5000L))

}