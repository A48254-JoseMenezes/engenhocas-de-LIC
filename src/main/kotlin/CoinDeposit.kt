
//Possibilita o armazenamento de moedas durante a execução do programa
object CoinDeposit {

    //Representação de uma moeda
    class Coin(val value: Int, var amount: Int)

    //Lista onde se armazena a quantidade de moedas durante a execução do programa
    private val bank = mutableListOf<Coin>()

    //Nome do ficheiro usado para armazenar a quantidade de moedas
    private const val FILE_NAME = "CoinDeposit.txt"

    //Quantidade de tipo de moedas a armazenar
    var size = 0

    //Inicia a classe
    fun init() {
        val coinArray = FileAccess.readFile(FILE_NAME)
        for (i in coinArray.indices) {
            val details = coinArray[i].split(";")
            bank.add( Coin(details[0].toInt(), details[1].toInt()) )
        }
        size = bank.size
    }

    //Retorna a informação de uma moeda através de um índice
    operator fun get(idx: Int) = bank[idx]

    //Coloca a quantidade de todas as moedas a 0, começando uma nova contagem
    fun resetBank() {
        for (i in bank)
            i.amount = 0
    }

    //Incrementa a quantidade de um tipo de moeda com um valor passado como parâmetro
    fun depositCoin(value: Int) = bank.first{ it.value == value }.amount++

    //Guarda toda a informação armazenada para um ficheiro
    fun update() {
        val lines = bank.map{ "${it.value};${it.amount}" } as MutableList<String>
        FileAccess.writeFile(FILE_NAME, lines)
    }
}

fun main() {
    CoinDeposit.init()
    CoinDeposit.update()
}