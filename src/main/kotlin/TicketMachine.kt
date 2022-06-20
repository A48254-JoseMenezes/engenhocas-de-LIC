import java.util.LinkedList

object TicketMachine {

    private const val NONE = 0
    private const val CHANGE_MODE = -1

    enum class SelectionMode {NUMBERS, ARROWS}

    var currentSelectionMode = SelectionMode.NUMBERS
    var maintenanceMode = false

    fun init() {
        M.init()
        CoinAcceptor.init()
        TicketDispenser.init()
        TUI.init()
        Stations.init()
        CoinDeposit.init()
    }

    fun bootUp() {
        TUI.write("----", 0, TUI.Location.CENTER)
        Thread.sleep(500)
        TUI.write("*---", 0, TUI.Location.CENTER)
        Thread.sleep(500)
        TUI.write("**--", 0, TUI.Location.CENTER)
        Thread.sleep(500)
        TUI.write("***-", 0, TUI.Location.CENTER)
        Thread.sleep(500)
        TUI.write("****", 0, TUI.Location.CENTER)
        Thread.sleep(500)
        TUI.write("TICKET MACHINE", 0, TUI.Location.CENTER)
        Thread.sleep(500)
        TUI.write("'#' to begin", 1, TUI.Location.CENTER)
    }

    fun standby() {
        TUI.clear()
        TUI.write("TICKET MACHINE", 0, TUI.Location.CENTER)
        TUI.write("'#' to begin", 1, TUI.Location.CENTER)
        while(true){
            val key = TUI.read()
            if (key == '#') {
                maintenanceMode = false
                return
            }
        }
    }

    fun select(): Int? {
            while (true)
                when (currentSelectionMode) {
                    SelectionMode.NUMBERS -> {
                                                val idx = selectNumbers() ?: return null
                                                if (idx == CHANGE_MODE) continue else return idx
                                             }
                    SelectionMode.ARROWS ->  {
                                                val idx = selectArrows() ?: return null
                                                if (idx == CHANGE_MODE) continue else return idx
                                             }
                }
    }

    private fun selectNumbers(): Int? {
        var idx = 0
        while (true) {
            if (idx < Stations.size)
                writeStation(idx)
            val key = TUI.read()
            when (key) {
                NONE.toChar() -> return null
                '*'           -> {
                                    currentSelectionMode = SelectionMode.ARROWS
                                    return CHANGE_MODE
                                 }
                '#'           -> return idx
                else          -> idx = key.digitToInt()
            }
        }
    }

    private fun selectArrows(): Int? {
        var idx = 0
        while (true) {
            writeStation(idx)
            val key = TUI.read()
            when (key) {
                NONE.toChar() -> return null
                '*' -> {
                    currentSelectionMode = SelectionMode.NUMBERS
                    return CHANGE_MODE
                }
                '#' -> return idx
                '2' -> idx = (idx + 1).mod(Stations.size)
                '8' -> idx = (idx - 1).mod(Stations.size)
            }
        }
    }

    private fun writeStation(idx: Int) {
        TUI.clear()
        TUI.write(idx.toString(), 1, TUI.Location.LEFT)
        TUI.write(Stations[idx].name, 0, TUI.Location.CENTER)
        TUI.write(Stations[idx].price.toString(), 1, TUI.Location.RIGHT)
    }

    fun startPurchase(idx: Int) {
        var roundTrip = false
        val amount = Stations[idx].price
        var acc = amount
        val money = LinkedList<Int>()

        TUI.clear()
        TUI.write(Stations[idx].name, 0, TUI.Location.CENTER)
        TUI.write(amount.toString(), 1, TUI.Location.CENTER)

        while (true) {
            val key = TUI.read()
            when (key) {
                '#' -> {
                    cancelPurchase(money)
                    return
                }
                '0' -> {
                    if (!roundTrip) {
                        acc *= 2
                        roundTrip = true
                    } else {
                        acc /= 2
                        roundTrip = false
                    }
                }
            }

            if (CoinAcceptor.hasCoin()) {
                CoinAcceptor.acceptCoin()
                val coin = CoinAcceptor.getCoinValue()
                money.add(coin)
                acc -= coin
            }

            if (acc <= 0) {
                while(money.isNotEmpty()){
                    val i = money.poll()
                    CoinDeposit.depositCoin(i)
                }
                CoinAcceptor.collectCoins()
                collectTicket(idx, roundTrip)
                return
            } else {
                TUI.clear()
                TUI.write(Stations[idx].name, 0, TUI.Location.CENTER)
                TUI.write("$acc", 1, TUI.Location.CENTER)
            }
        }
    }

    private fun cancelPurchase(money: LinkedList<Int>) {
        TUI.clear()
        TUI.write("Cancelled", 0, TUI.Location.CENTER)
        var acc = 0
        while(money.isNotEmpty()){
            val i = money.poll()
            acc += i
        }
        TUI.write("Returned $acc", 1, TUI.Location.CENTER)
        CoinAcceptor.ejectCoins()
        return
    }

    private fun collectTicket(idx: Int, roundTrip: Boolean) {
        TUI.write(Stations[idx].name, 0, TUI.Location.CENTER)
        TUI.write("Collect Ticket", 1, TUI.Location.CENTER)
        Stations.ticketSold(Stations[idx])
        TicketDispenser.print(0, idx, roundTrip)
        TUI.clear()
        TUI.write("Have a nice trip", 0, TUI.Location.LEFT)
        Thread.sleep(1000)
        return
    }
}

fun main(){
    TicketMachine.init()
    TicketMachine.bootUp()
    while(true) {
        TicketMachine.standby()
        if (TicketMachine.maintenanceMode) {

        }
        else {
            val idx = TicketMachine.select() ?: continue
            TicketMachine.startPurchase(idx)
        }

    }
}