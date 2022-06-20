// Controla o estado do mecanismo de dispensa de bilhetes
object TicketDispenser {

    private const val DESTINYID_LASTIDX = 1
    private const val ORIGINID_LASTIDX = 5
    private const val ROUNDTRIP_IDX = 0

    // Inicia a classe, estabelecendo os valores iniciais
    fun init() {
        SerialEmitter.init()
    }

    // Envia comando para imprimir e dispensar um bilhete
    fun print(destinyId: Int, originId: Int, roundTrip: Boolean) {
        SerialEmitter.send(
            SerialEmitter.Destination.TICKET_DISPENSER,
            destinyId.shl(DESTINYID_LASTIDX) +
                    originId.shl(ORIGINID_LASTIDX) +
                    (if(roundTrip) 1 else 0).shl(ROUNDTRIP_IDX)
        )
    }
}

fun main() {
    TicketDispenser.init()

    val startingTime = System.currentTimeMillis()
    TicketDispenser.print(2, 5, true)
    val endingTime = System.currentTimeMillis()
    println("Latência da transmissão da trama: ${endingTime - startingTime}")

    /*
    for (i in 0..15) {
        for (j in 0..15) {
            for (m in 0..1) {
                val startingTime = System.currentTimeMillis()
                TicketDispenser.print(i, j, m == 1)
                val endingTime = System.currentTimeMillis()
                println("Latência da transmissão da trama: ${endingTime - startingTime}")
            }
        }
    }
     */
}