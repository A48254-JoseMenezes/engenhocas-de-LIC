object TicketMachine {

    const val NONE = 0

    enum class SelectionMode {NUMBERS, ARROWS}

    var currentSelectionMode = SelectionMode.NUMBERS
    val changeMode = -1
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
        //TUI.write("TICKET MACHINE", 0, TUI.Location.CENTER)
        //Thread.sleep(500)
        //TUI.write("'#' to begin", 1, TUI.Location.CENTER)
        //Thread.sleep(1500)
    }

    fun select(): Int? {
        TUI.clear()
        TUI.write("TICKET MACHINE", 0, TUI.Location.CENTER)
        TUI.write("'#' to begin", 1, TUI.Location.CENTER)
        val key = TUI.read()
        if (key == '#')
            while (true)
                when (currentSelectionMode) {
                    SelectionMode.NUMBERS -> {
                                                val idx = selectNumbers() ?: return null
                                                if (idx == changeMode) continue else return idx
                                             }
                    SelectionMode.ARROWS ->  {
                                                val idx = selectArrows() ?: return null
                                                if (idx == changeMode) continue else return idx
                                             }
                }
        return null
    }

    fun selectNumbers(): Int? {
        var idx = 0
        while (true) {
            if (idx < Stations.size)
                writeStation(idx)
            val key = TUI.read()
            when (key) {
                NONE.toChar() -> return null
                '*'           -> {
                                    currentSelectionMode = SelectionMode.ARROWS
                                    return changeMode
                                 }
                '#'           -> return idx
                else          -> idx = key.digitToInt()
            }
        }
    }

    fun selectArrows(): Int? {
        var idx = 0
        while (true) {
            writeStation(idx)
            val key = TUI.read()
            when (key) {
                NONE.toChar() -> return null
                '*'           -> {
                                    currentSelectionMode = SelectionMode.NUMBERS
                                    return changeMode
                                 }
                '#'           -> return idx
                '2'           -> idx = (idx + 1).mod(Stations.size)
                '8'           -> idx = (idx - 1).mod(Stations.size)
                else          -> continue
            }
        }
    }

    fun writeStation(idx: Int) {
        TUI.clear()
        TUI.write(idx.toString(), 1, TUI.Location.LEFT)
        TUI.write(Stations[idx].name, 0, TUI.Location.CENTER)
        TUI.write(Stations[idx].price.toString(), 1, TUI.Location.RIGHT)
    }

    fun startPurchase(idx: Int) {
        var roundTrip = false
        val amount = Stations[idx].price
        var acc = amount
        var moneyInsert = 0

        TUI.clear()
        TUI.write(Stations[idx].name, 0, TUI.Location.CENTER)
        TUI.write(amount.toString(), 1, TUI.Location.CENTER)

        while (true) {
            val key = TUI.read()
            //println(key)
            when {
                key == '#' -> {
                                cancelPurchase(moneyInsert)
                                return
                              }
                key == '0' -> {
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
                moneyInsert += CoinAcceptor.getCoinValue()
                acc -= CoinAcceptor.getCoinValue()
                CoinAcceptor.collectCoins()
            }

            if (acc <= 0) {
                collectTicket(idx, moneyInsert, roundTrip)
                return
            } else {
                TUI.clear()
                TUI.write(Stations[idx].name, 0, TUI.Location.CENTER)
                TUI.write("$acc", 1, TUI.Location.CENTER)
            }
        }
    }

    private fun cancelPurchase(moneyInsert: Int) {
        TUI.clear()
        TUI.write("Cancelled", 0, TUI.Location.CENTER)
        CoinAcceptor.ejectCoins()
        TUI.write("Return $moneyInsert", 1, TUI.Location.CENTER)
        Thread.sleep(2000)
        return
    }

    private fun collectTicket(idx: Int, moneyInsert: Int, roundTrip: Boolean) {
        TUI.write(Stations[idx].name, 0, TUI.Location.CENTER)
        TUI.write("Collect Ticket", 1, TUI.Location.CENTER)
        Stations.ticketSold(Stations[idx])
        Stations.update()
        //CoinDeposit.depositCoin(moneyInsert)
        //CoinDeposit.update()
        TicketDispenser.print(0, idx, roundTrip)                         // ERRO !!!
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
        val idx = TicketMachine.select() ?: continue
        TicketMachine.startPurchase(idx)
    }
}