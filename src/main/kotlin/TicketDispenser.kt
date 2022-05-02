object TicketDispenser { // Controla o estado do mecanismo de dispensa de bilhetes.

    const val DESTINYID_LASTIDX = 5
    const val ORIGINID_LASTIDX = 1
    const val ROUNDTRIP_IDX = 0

// Inicia a classe, estabelecendo os valores iniciais.
    fun init() {
        SerialEmitter.init()
    }
    // Envia comando para imprimir e dispensar um bilhete
    fun print(destinyId: Int, originId: Int, roundTrip: Boolean){
        SerialEmitter.send(
            SerialEmitter.Destination.TICKET_DISPENSER,
            destinyId.shl(DESTINYID_LASTIDX) +
                    originId.shl(ORIGINID_LASTIDX) +
                    (if(roundTrip) 0 else 1).shl(ROUNDTRIP_IDX)
        )
    }
}

fun main(){
    TicketDispenser.init()
    TicketDispenser.print(2, 5, true)
}