// Escreve no LCD usando a interface a 8 bits
object LCD {

    // Dimensão do display
    private const val LINES = 2
    private const val COLS = 16

    private const val CMD_CLEAR = 0x01
    private const val CMD_CURSOR_SHIFT = 0x80
    private const val CMD_INIT_SET = 0x30
    private const val CMD_INIT_SET_LINES = 0x38
    private const val CMD_INIT_DISPLAY_OFF = 0x08
    private const val CMD_INIT_ENTRY = 0x06
    private const val CMD_INIT_DISPLAY_ON = 0x0C

    // Escreve um byte de comando/dados no LCD em série
    private fun writeByteSerial(rs: Boolean, data: Int) =
        SerialEmitter.send(SerialEmitter.Destination.LCD, data.shl(1) + (if (rs) 1 else 0))

    // Escreve um byte de comando/dados no LCD
    private fun writeByte(rs: Boolean, data: Int) =
        writeByteSerial(rs, data)

    // Escreve um comando no LCD
    private fun writeCMD(data: Int) =
        writeByte(false, data)

    // Escreve um dado no LCD
    private fun writeDATA(data: Int) =
        writeByte(true, data)

    // Envia a sequência de iniciação para comunicação a 8 bits
    fun init() {
        SerialEmitter.init()

        Thread.sleep(20)

        writeCMD(CMD_INIT_SET)

        Thread.sleep(6)

        writeCMD(CMD_INIT_SET)

        Thread.sleep(1)

        writeCMD(CMD_INIT_SET)

        writeCMD(CMD_INIT_SET_LINES)
        writeCMD(CMD_INIT_DISPLAY_OFF)
        writeCMD(CMD_CLEAR)
        writeCMD(CMD_INIT_ENTRY)

        writeCMD(CMD_INIT_DISPLAY_ON)
    }

    // Escreve um caráter na posição corrente
    fun write(c: Char) {
        writeDATA(c.code)
    }

    // Escreve uma string na posição corrente
    fun write(text: String) {
        for (i in text.indices){
            write(text[i])
        }
    }

    // Envia comando para posicionar cursor (‘line’:0..LINES-1 , ‘column’:0..COLS-1)
    fun cursor(line: Int, column: Int) {
        writeCMD(
            CMD_CURSOR_SHIFT
            + line.shl(6)
            + column
        )
    }

    // Envia comando para limpar o ecrã e posicionar o cursor em (0,0)
    fun clear() {
        writeCMD(CMD_CLEAR)
    }
}

fun main(){
    LCD.init()

    LCD.write("teste")
}