object TicketMachine {

    const val NONE = 0

    fun init(){
        M.init()
        CoinAcceptor.init()
        TicketDispenser.init()
        TUI.init()
        Stations.init()
        CoinDeposit.init()
    }

    var timeout = false

    fun bootup(){
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
        TUI.write("'#' to begin.", 1, TUI.Location.CENTER)
        Thread.sleep(1500)
    }
    fun selectModeNumbers(){
        var key = '0'
        while(true) {
            if (key.digitToInt() < Stations.size){
                TUI.clear()
                TUI.write(key.toString(), 1, TUI.Location.LEFT)
                TUI.write(Stations[key.digitToInt()].name, 0, TUI.Location.CENTER)
                TUI.write(Stations[key.digitToInt()].price.toString(), 1, TUI.Location.RIGHT)
            }
            key = TUI.read()
            if (key == NONE.toChar()) {
                TUI.write("Timeout", 0, TUI.Location.CENTER)
                break
            }
            if (key == '*') {
                selectModeArrows()
                if (timeout) {
                    timeout = false
                    return
                }
                key = '0'
            }
        }
    }
    fun selectModeArrows(){
        var idx = 0
        while(true){
            TUI.write(idx.toString(), 1, TUI.Location.LEFT)
            TUI.write(Stations[idx].name, 0, TUI.Location.CENTER)
            TUI.write(Stations[idx].price.toString(), 1, TUI.Location.RIGHT)
            val key1 = TUI.read()
            TUI.clear()
            if (key1 == NONE.toChar()) {
                timeout = true
                return
            }
            if (key1 == '*') return
            if (key1 == '2') idx = (idx + 1) % Stations.size
            if (key1 == '8') idx = (idx - 1) % Stations.size
        }
    }

    fun ticketProcessing() {
        while (true) {
            TUI.write("TICKET MACHINE", 0, TUI.Location.CENTER)
            TUI.write("'#' to begin.", 1, TUI.Location.CENTER)
            val key = TUI.read()
            if (key == '#') {
                TUI.clear()
                selectModeNumbers()
            }
        }
    }
}


fun main(){
    TicketMachine.init()
    TicketMachine.bootup()
    TicketMachine.ticketProcessing()
}