import isel.leic.UsbPort

// Virtualiza o acesso ao sistema UsbPort
object HAL {
    var currentOut = 0
    // Inicia a classe
    fun init() {
        writeBits(0xFF, 0)
    }

    // Retorna true se o bit tiver o valor lógico ‘1’
    fun isBit(mask: Int): Boolean{
        return UsbPort.read() and mask != 0}

    // Retorna os valores dos bits representados por mask presentes no UsbPort
    fun readBits(mask: Int) = UsbPort.read() and mask

    // Escreve nos bits representados por mask o valor de value
    fun writeBits(mask: Int, value: Int){
        val notMask = mask.inv()
        currentOut = ( notMask and currentOut ) or value
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

fun main(){
    HAL.init()
    HAL.writeBits(0xFF, 254)
    println(HAL.readBits(3))
    HAL.clrBits(0xFF)
    HAL.setBits(0x3C)
}