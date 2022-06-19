
object Stations {

    class Station(val price: Int, var ticketsSold: Int, val name: String)

    private var stations : MutableList<Station> = mutableListOf()

    private const val FILE_NAME = "stations.txt"

    fun init(){
        val stationsArray = FileAccess.readFile(FILE_NAME)
        for (i in stationsArray.indices) {
            val details = stationsArray[i].split(";")
            stations.add( Station(details[0].toInt(), details[1].toInt(), details[2]) )
        }
    }

    operator fun get(i : Int) = stations[i]

    fun ticketSold(station : Station) = station.ticketsSold++

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