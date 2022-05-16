// Escreve no LCD usando a interface a 8 bits
object LCD {
    // Dimensão do display
    private const val LINES = 2
    private const val COLS = 16

    // Escreve um byte de comando/dados no LCD em série
    private fun writeByteSerial(rs: Boolean, data: Int) =
        SerialEmitter.send(SerialEmitter.Destination.LCD, (if (rs) 1 else 0).shl(8) + data)

    // Escreve um byte de comando/dados no LCD
    private fun writeByte(rs: Boolean, data: Int) =
        writeByteSerial(rs, data)

    // Escreve um comando no LCD
    private fun writeCMD(data: Int) =
        writeByte(false, data)

    // Escreve um dado no LCD
    private fun writeDATA(data: Int) =
        writeByte(true, data)

    // Envia a sequência de iniciação para comunicação a 8 bits.
    fun init() {
        SerialEmitter.init()

        Thread.sleep(15, 5000)

        writeCMD(1.shl(5) + 1.shl(4))

        Thread.sleep(4, 5000)

        writeCMD(1.shl(5) + 1.shl(4))

        Thread.sleep(0, 500000)

        writeCMD(1.shl(5) + 1.shl(4))

        writeCMD(1.shl(5) + 1.shl(4) + 1.shl(3))
        writeCMD(1.shl(3))
        writeCMD(1)
        writeCMD(1.shl(2) + 1.shl(1))
    }
    // Escreve um caráter na posição corrente.
    fun write(c: Char) {
        writeDATA(c.code)
    }
    // Escreve uma string na posição corrente.
    fun write(text: String) {
        for (i in text.indices){
            write(text[i])
        }
    }
    // Envia comando para posicionar cursor (‘line’:0..LINES-1 , ‘column’:0..COLS-1)
    fun cursor(line: Int, column: Int) {
        writeCMD(
            1.shl(7)
            + line.shl(6)
            + column
        )
    }

    // Envia comando para limpar o ecrã e posicionar o cursor em (0,0)
    fun clear() {
        writeCMD(1)
    }
}

fun main(){
    LCD.init()
    LCD.write("U")
}