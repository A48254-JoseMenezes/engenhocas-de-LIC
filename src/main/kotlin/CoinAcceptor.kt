object CoinAcceptor { // Implementa a interface com o moedeiro.

    const val COIN_MASK = 0x02
    const val CID_MASK = 0x1C // bit 2, bit 3, bit 4


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
        TODO()
    }
    // Devolve as moedas que estão no moedeiro.
    fun ejectCoins() {
        TODO()
    }
    // Recolhe as moedas que estão no moedeiro.
    fun collectCoins() {
        TODO()
    }
}