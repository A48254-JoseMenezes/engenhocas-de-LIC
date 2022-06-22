
//Possibilita um modo de manutenção
object M {

    //Máscara que representa a entrada 'M'
    const val M_MASK = 0x40

    //Inicia a classe
    fun init() {
        HAL.init()
    }

    //Verifica se houve registo de uma chave de manutenção
    fun maintenanceCheck() = HAL.isBit(M_MASK)
}

fun main() {
    M.init()

    while (true) {
        println(M.maintenanceCheck())
    }
}