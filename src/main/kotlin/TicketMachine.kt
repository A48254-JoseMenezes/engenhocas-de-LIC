object TicketMachine {

    fun init(){
        M.init()
        CoinAcceptor.init()
        TicketDispenser.init()
        TUI.init()
        Stations.init()
        CoinDeposit.init()
    }

    fun bootup(){
        TUI.write("----", 0, TUI.Location.CENTER)
        Thread.sleep(1000)
        TUI.write("*---", 0, TUI.Location.CENTER)
        Thread.sleep(1000)
        TUI.write("**--", 0, TUI.Location.CENTER)
        Thread.sleep(1000)
        TUI.write("***-", 0, TUI.Location.CENTER)
        Thread.sleep(1000)
        TUI.write("****", 0, TUI.Location.CENTER)
        Thread.sleep(1000)
        TUI.write("TICKET MACHINE", 0, TUI.Location.CENTER)
        Thread.sleep(2000)
        TUI.write("'#' to begin.", 1, TUI.Location.CENTER)
    }

}

fun main(){
    TicketMachine.init()
    TicketMachine.bootup()
}