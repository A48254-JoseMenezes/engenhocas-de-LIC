
object CoinDeposit {

    class Coin(val value : Int, var amount : Int)

    private val bank = mutableListOf<Coin>()

    private const val FILE_NAME = "CoinDeposit.txt"

    fun init(){
        val coinArray = FileAccess.readFile(FILE_NAME)
        for (i in coinArray.indices){
            val details = coinArray[i].split(";")
            bank.add( Coin(details[0].toInt(), details[1].toInt()) )
        }
    }

    fun reset() {
        for (i in bank)
            i.amount = 0
    }

    fun depositCoin(value : Int) = bank.first{ it.value == value }.amount++

    fun update() {
        val lines = bank.map{ "${it.value};${it.amount}" } as MutableList<String>
        FileAccess.writeFile(FILE_NAME, lines)
    }

}