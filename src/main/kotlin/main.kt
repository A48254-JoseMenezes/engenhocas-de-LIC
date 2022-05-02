import isel.leic.UsbPort
import kotlin.math.pow

// Virtualiza o acesso ao sistema UsbPort
object HAL {
    var currentOut = 0
    // Inicia a classe
    fun init() {
        clrBits(0xFF)
    }

    // Retorna true se o bit tiver o valor lógico ‘1’
    fun isBit(mask: Int): Boolean{
        println(UsbPort.read() )
        return UsbPort.read() and mask != 0}

    // Retorna os valores dos bits representados por mask presentes no UsbPort
    fun readBits(mask: Int) = UsbPort.read() and mask

    // Escreve nos bits representados por mask o valor de value
    fun writeBits(mask: Int, value: Int){
        val notMask = mask.inv()
        currentOut = ( notMask and currentOut ) or value
        println(currentOut)
        UsbPort.write(currentOut)
    }

    // Coloca os bits representados por mask no valor lógico ‘1’
    fun setBits(mask: Int) {
        currentOut = currentOut or mask
        UsbPort.write(currentOut)
    }

    // Coloca os bits representados por mask no valor lógico ‘0’
    fun clrBits(mask: Int) {
        val notMask = mask.inv()
        currentOut = currentOut and notMask
        UsbPort.write(currentOut)
    }
}

object SerialEmitter {
    // Envia tramas para os diferentes módulos Serial Receiver.
    enum class Destination {LCD, TICKET_DISPENSER }

    const val maskSS = 0x80
    const val maskSClock = 0x40
    const val maskSDX = 0x20

    const val sizeBits = 10

    // Inicia a classe
    fun init(){
        HAL.setBits(maskSS)
        HAL.clrBits(maskSS + maskSClock)
    }

    // Envia uma trama para o SerialReceiver identificado o destino em addr e os bits de dados em ‘data’.
    fun send(addr: Destination, data: Int){
        val valueLCD = 0x80


        if (addr == Destination.LCD){
            HAL.setBits(maskSDX)
            HAL.setBits(maskSClock)
        }
        repeat(sizeBits){
            if(data and 2.toDouble().pow(sizeBits-it-1).toInt() > 0){HAL.setBits(maskSDX)}

            HAL.setBits(maskSClock)
        }


    }

    // Retorna true se o canal série estiver ocupado
    //fun isBusy(): Boolean ...
}

fun main(){
    HAL.init()
    HAL.writeBits(0xFF, 254)
    Thread.sleep(10000)
    println(HAL.readBits(3))
    HAL.clrBits(0xFF)
    Thread.sleep(1000)
    HAL.setBits(0x3C)
}