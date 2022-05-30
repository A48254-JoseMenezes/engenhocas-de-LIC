object KeyReceiver { // Recebe trama do Keyboard Reader.

    const val TXD_MASK = 0x01
    const val TXCLK_MASK = 0x01

    // Inicia a classe
    fun init() {
        HAL.init()
    }
    // Recebe uma trama e retorna o código de uma tecla caso exista
    fun rcv(): Int {
        var keyCode = 0
        if( HAL.isBit(TXD_MASK) ) return -1
        txclkRise()
        if( !HAL.isBit(TXD_MASK) ) return -1
        txclkRise()
        repeat(4){ idx ->
            val tdx = HAL.readBits(TXD_MASK)
            keyCode += tdx.shl(idx)
            txclkRise()
        }
        if ( HAL.isBit(TXD_MASK) ) return -1
        else return keyCode
    }

    private fun txclkRise(){
        HAL.setBits(TXCLK_MASK)
        HAL.clrBits(TXCLK_MASK)
    }
}