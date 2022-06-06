object CoinAcceptor { // Implementa a interface com o moedeiro.

    const val COIN_MASK = 0x02
    const val CID_MASK = 0x1C // bit 2, bit 3, bit 4
    const val ACCEPT_MASK = 0x08
    const val COLLECT_MASK = 0x04
    const val EJECT_MASK = 0x02


    // Inicia a classe
    fun init() {
        HAL.init()
    }
    // Retorna true se foi introduzida uma nova moeda.
    fun hasCoin(): Boolean = HAL.isBit(COIN_MASK)
    // Retorna o valor facial da moeda introduzida.
    fun getCoinValue(): Int {
        val id =  HAL.readBits(CID_MASK)
        val listCents = listOf(5, 10, 20, 50, 100, 200)
        return listCents[id]
    }
    // Informa o moedeiro que a moeda foi contabilizada.
    fun acceptCoin() {
        while(hasCoin()){
            HAL.setBits(ACCEPT_MASK)
        }
        HAL.clrBits(ACCEPT_MASK)
    }
    // Devolve as moedas que estão no moedeiro.
    fun ejectCoins() {
        while(hasCoin()){
            HAL.setBits(EJECT_MASK)
        }
        HAL.clrBits(EJECT_MASK)
    }
    // Recolhe as moedas que estão no moedeiro.
    fun collectCoins() {
        while(hasCoin()){
            HAL.setBits(COLLECT_MASK)
        }
        HAL.clrBits(COLLECT_MASK)
    }
}