data class CoinDeposit(val coin:Int, val number: Int)

fun String.parseToCoinDepost(): CoinDeposit{
    val coindDetails = this.split(";")
    return CoinDeposit(coindDetails[0].toInt(), coindDetails[1].toInt())
}

