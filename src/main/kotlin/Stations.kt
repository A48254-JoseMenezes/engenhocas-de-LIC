
//Possibilita o armazenamento da informação das estações durante a execução do programa
object Stations {

    //Representa uma estação
    class Station(val price: Int, var ticketsSold: Int, val name: String)

    //Lista que armazena toda a informação das estações
    private var stations = mutableListOf<Station>()

    //Ficheiro usado para armazenar toda a informação armazenada
    private const val FILE_NAME = "stations.txt"

    //Quantidade de estações
    var size = 0

    //Inicia a classe
    fun init() {
        val stationsArray = FileAccess.readFile(FILE_NAME)
        for (i in stationsArray.indices) {
            val details = stationsArray[i].split(";")
            stations.add( Station(details[0].toInt(), details[1].toInt(), details[2]) )
        }
        size = stations.size
    }

    //Retorna a informação de uma estação passada como parâmetro
    operator fun get(i: Int) = stations[i]

    //Coloca os bilhetes vendidos de todas as estações a 0, começando uma nova contagem
    fun resetStations() {
        for (i in stations)
            i.ticketsSold = 0
    }

    //Incrementa a quantidade de bilhetes vendido de uma determinada estação
    fun ticketSold(station: Station) = station.ticketsSold++

    //Guarda toda a informação armazenada para um ficheiro
    fun update() {
        val lines = stations.map {"${it.price};${it.ticketsSold};${it.name}"} as MutableList<String>
        FileAccess.writeFile(FILE_NAME, lines)
    }
}

fun main(){
    Stations.init()
    val station = Stations[2]
    Stations.ticketSold(station)
    Stations.update()
}