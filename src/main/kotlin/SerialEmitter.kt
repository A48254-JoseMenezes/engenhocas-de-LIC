// Envia tramas para os diferentes módulos Serial Receiver
object SerialEmitter {

    const val SSMASK = 0x80
    const val SCLKMASK = 0x40
    const val SDXMASK = 0x20
    const val BUSYMASK = 0x80

    enum class Destination(val x: Int) { LCD(0), TICKET_DISPENSER(1) }

    // Inicia a classe
    fun init() {
        HAL.init()
        HAL.writeBits(SSMASK+SCLKMASK+SDXMASK, 0x80)
    }

    // Envia uma trama para o SerialReceiver identificado o destino em addr e os bits de dados em ‘data’
    fun send(addr: Destination, data: Int) {
        var isPair :Boolean
        while ( isBusy() ) { }
        HAL.clrBits(SSMASK)
        HAL.writeBits(SDXMASK, addr.x * SDXMASK)
        if (addr.x == 1) isPair = false else isPair = true
        sclkCycle()
        for (i in 0..8) {
            val SDX = (data.shr(i) % 2)
            HAL.writeBits(SDXMASK,  SDX * SDXMASK )
            if (SDX == 1) isPair = !isPair
            sclkCycle()
        }
        if (isPair)
            HAL.writeBits(SDXMASK, 0)
        else
            HAL.writeBits(SDXMASK, 1 * SDXMASK)
        sclkCycle()
        HAL.setBits(SSMASK)
    }

    // Retorna true se o canal série estiver ocupado
    fun isBusy(): Boolean =  HAL.isBit(BUSYMASK)

    // Simula uma subida e descida de CLK
    private fun sclkCycle(){
        HAL.setBits(SCLKMASK)
        HAL.clrBits(SCLKMASK)
    }
}

fun main(){
    SerialEmitter.init()

    SerialEmitter.send(SerialEmitter.Destination.TICKET_DISPENSER, 0x1BD)
    println(SerialEmitter.isBusy())
}