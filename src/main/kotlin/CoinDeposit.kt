
object CoinDeposit {

    class Coin(val value : Int, var amount : Int)

    val bank = mutableListOf<Coin>()

    const val FILE_NAME = "CoinDeposit.txt"

    fun init(){
        val coinArray = FileAccess.readFile(FILE_NAME)
        for (i in coinArray.indices){
            val details = coinArray[i].split(";")
            bank.add( Coin(details[0].toInt(), details[1].toInt()) )
        }
    }

    operator fun get(i : Int) = bank[i]

    fun depositCoin(value : Int) = bank.first{ it.value == value }.amount++

    fun update() {
        val lines = bank.map{ "${it.value};${it.amount}" } as MutableList<String>
        FileAccess.writeFile(FILE_NAME, lines)
    }

}