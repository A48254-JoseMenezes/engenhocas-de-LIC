import java.util.LinkedList
import kotlin.system.exitProcess

// Aplicação da máquina de venda de bilhetes
object TicketMachine {

    // Valor que, traduzido para char, representa ausência de caracter
    private const val NONE = 0

    // Valor que representa a mudança do modo de seleção
    private const val CHANGE_MODE = -1

    // Valor que representa a estação de origem na compra de bilhete
    private const val STATION_ID = 6

    // Valor que representa o número de opções/menus em modo de manutenção
    private const val MAINTENANCE_OPTIONS = 5

    // Tempos de timeout das teclas
    private const val NO_TIMEOUT = 500L
    private const val SHORT_TIMEOUT = 1500L

    // Representa o modo atual de seleção
    private var currentSelectionMode = SelectionMode.NUMBERS

    // Representa as opções de seleção de informação
    enum class SelectionMode {NUMBERS, ARROWS}

    // Representa as opções de informação
    enum class Information {STATION_PRICE, STATION_TICKETS, COINS}

    // Representa o modo atual de execução
    var maintenanceMode = false

    // Inicia a classe
    fun init() {
        M.init()
        CoinAcceptor.init()
        TicketDispenser.init()
        TUI.init()
        Stations.init()
        CoinDeposit.init()
    }

    // Apresentação inicial da aplicação no LCD
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

    // Aguarda início da compra de um bilhete ou da mudança para modo de manutenção
    fun standby() {
        TUI.clear()
        TUI.write("TICKET MACHINE", 0, TUI.Location.CENTER)
        TUI.write("'#' to begin", 1, TUI.Location.CENTER)
        while (true) {
            if (M.maintenanceCheck() || maintenanceMode) {
                maintenanceMode = true
                return
            }
            val key = TUI.read(NO_TIMEOUT)
            if (key == '#') {
                maintenanceMode = false
                return
            }
        }
    }

    // Seleção o módulo de seleção de informação, números ou setas
    fun select(info: Information): Int? {
        while (true)
            when (currentSelectionMode) {
                SelectionMode.NUMBERS -> {
                                            val idx = selectNumbers(info) ?: return null
                                            if (idx == CHANGE_MODE) continue else return idx
                                         }
                SelectionMode.ARROWS ->  {
                                            val idx = selectArrows(info) ?: return null
                                            if (idx == CHANGE_MODE) continue else return idx
                                         }
            }
    }

    // Modo de seleção por números
    private fun selectNumbers(info: Information): Int? {
        val infoSize = if (info == Information.COINS) CoinDeposit.size else Stations.size
        var idx = 0
        while (true) {
            if (idx < infoSize)
                if (info == Information.COINS) writeCoins(idx) else writeStation(idx, info)

            val key = TUI.read()
            when (key) {
                NONE.toChar() -> return null
                '*'           -> {
                                    currentSelectionMode = SelectionMode.ARROWS
                                    return CHANGE_MODE
                                 }
                '#'           -> return if (idx != STATION_ID || info == Information.COINS) idx else continue
                else          -> {
                                    val newIdx = idx * 10 + key.digitToInt()
                                    if (idx < 10 && newIdx < Stations.size)
                                        idx = idx*10 + key.digitToInt()
                                    else
                                        idx = key.digitToInt()
                                 }
            }
        }
    }

    // Modo se seleção por setas
    private fun selectArrows(info: Information): Int? {
        val infoSize = if (info == Information.COINS) CoinDeposit.size else Stations.size
        var idx = 0
        while (true) {
            if (info == Information.COINS) writeCoins(idx) else writeStation(idx, info)
            val key = TUI.read()
            when (key) {
                NONE.toChar() -> return null
                '*'           -> {
                                  currentSelectionMode = SelectionMode.NUMBERS
                                  return CHANGE_MODE
                                 }
                '#'           -> return if (idx != STATION_ID || info == Information.COINS) idx else continue
                '2'           -> idx = (idx + 1).mod(infoSize)
                '8'           -> idx = (idx - 1).mod(infoSize)
            }
        }
    }

    // Compra de um bilhete
    fun startPurchase(idx: Int) {
        var roundTrip = false
        val amount = Stations[idx].price
        var acc = amount
        val money = LinkedList<Int>()

        writePurchase(idx, roundTrip, acc)

        while (true) {
            val key = TUI.read(NO_TIMEOUT)
            when (key) {
                '#'     -> {
                            cancelPurchase(money)
                            return
                           }
                '0'     -> {
                             if (!roundTrip) {
                                acc += amount
                                roundTrip = true
                             } else {
                                acc -= amount
                                roundTrip = false
                             }
                             writePurchase(idx, roundTrip, acc)
                           }
            }

            if (CoinAcceptor.hasCoin()) {
                val coin = CoinAcceptor.getCoinValue()
                money.add(coin)
                acc -= coin
                CoinAcceptor.acceptCoin()
                if (acc > 0) writePurchase(idx, roundTrip, acc)
            }

            if (acc <= 0) {
                while (money.isNotEmpty()) {
                    val i = money.poll()
                    CoinDeposit.depositCoin(i)
                }
                CoinAcceptor.collectCoins()
                collectTicket(idx, roundTrip)
                return
            }
        }
    }

    // Cancelamento de uma compra de bilhete
    private fun cancelPurchase(money: LinkedList<Int>) {
        TUI.clear()
        TUI.write("Canceled", 0, TUI.Location.CENTER)
        var acc = 0
        while (money.isNotEmpty()) {
            val i = money.poll()
            acc += i
        }
        if (acc != 0) {
            TUI.write(acc.toEur(), 1, TUI.Location.CENTER)
        }
        Thread.sleep(1000)
        CoinAcceptor.ejectCoins()
        return
    }

    // Aguarda colheita do bilhete
    private fun collectTicket(idx: Int, roundTrip: Boolean) {
        TUI.clear()
        TUI.write(Stations[idx].name, 0, TUI.Location.CENTER)
        TUI.write("Collect Ticket", 1, TUI.Location.CENTER)
        TicketDispenser.print(idx, STATION_ID, roundTrip)
        if (!maintenanceMode) {
            Stations.ticketSold(Stations[idx])
        }
        TUI.clear()
        TUI.write("Have a nice trip", 0, TUI.Location.LEFT)
        Thread.sleep(1000)
        return
    }

    // Modo de manutenção selecionado
    fun maintenanceSelect() {
        var idx = 0
        while (true) {
            maintenanceSelectWrite(idx)
            val key = TUI.read(SHORT_TIMEOUT)
            if (!M.maintenanceCheck()) {
                maintenanceMode = false
                return
            }
            when (key) {
                NONE.toChar() -> if (idx + 1 >= MAINTENANCE_OPTIONS) idx = 0 else idx++
                '1'           -> {
                                    val idx = select(Information.STATION_PRICE) ?: return
                                    testPurchase(idx)
                                    return
                                 }

                '2'           -> select(Information.STATION_TICKETS)
                '3'           -> select(Information.COINS)
                '4'           -> reset()
                '5'           -> shutdown()

            }
        }
    }

    // Simulação de uma compra de bilhete em modo de manutenção (manutenção, opção 1)
    private fun testPurchase(idx: Int) {
        var roundTrip = false
        writePurchase(idx, roundTrip)
        while (true) {
            val key = TUI.read(NO_TIMEOUT)
            when (key) {
                '0' -> {
                        roundTrip = !roundTrip
                        writePurchase(idx, roundTrip)
                      }
                '#' -> return
                '*' -> {
                        collectTicket(idx, roundTrip)
                        writePurchase(idx, roundTrip)
                        return
                       }
            }
        }
    }

    // Reset do contador de estações (stations) e reset do contador de moedas (bank) (manutenção, opção 4)
    private fun reset(){
        if (areYouSure()) {
            TUI.clear()
            TUI.write("Resetting...", 0, TUI.Location.CENTER)
            Stations.resetStations()
            CoinDeposit.resetBank()
            TUI.clear()
            TUI.write("Reset success!", 0, TUI.Location.CENTER)
            Thread.sleep(2000)
        }
    }

    // Atualiza o ficheiro das estações e o ficheiro das moedas e termina a execução da aplicação (manutenção, opção 5)
    private fun shutdown() {
        if (areYouSure()) {
            TUI.clear()
            TUI.write("Shutting down...", 0, TUI.Location.CENTER)
            Stations.update()
            CoinDeposit.update()
            TUI.clear()
            Thread.sleep(500)
            exitProcess(0)
        }
    }

    // Retorna true se for para avançar o processo em curso ou false no caso contário
    private fun areYouSure(): Boolean {
        TUI.clear()
        TUI.write("Are you sure?", 0, TUI.Location.CENTER)
        TUI.write("5 - Y, Other - N", 1, TUI.Location.CENTER)
        while (true) {
            val key = TUI.read()
            when (key) {
                '5' -> return true
                else -> return false
            }
        }
    }

    // Escreve as informações de uma estação no LCD
    private fun writeStation(idx: Int, info: Information) {
        TUI.clear()
        val writeIdx = if (idx < 10) "0$idx:" else "$idx:"
        TUI.write(writeIdx, 1, TUI.Location.LEFT)
        TUI.write(Stations[idx].name, 0, TUI.Location.CENTER)
        if (info == Information.STATION_TICKETS) {
            TUI.write(Stations[idx].ticketsSold.toString(), 1, TUI.Location.RIGHT)
        }
        else {
            TUI.write(Stations[idx].price.toEur(), 1, TUI.Location.RIGHT)
        }
    }

    // Escreve as informações da compra de um bilhete (normal ou simulado) no LCD
    private fun writePurchase(idx: Int, roundTrip: Boolean, price: Int = 0) {
        TUI.clear()
        TUI.write(Stations[idx].name, 0, TUI.Location.CENTER)
        if (price == 0) {
            TUI.write("'*' - Print.", 1, TUI.Location.RIGHT)
        } else {
            TUI.write(price.toEur(), 1, TUI.Location.CENTER)
        }
        if (roundTrip) {
            TUI.write("RT.", 1, TUI.Location.LEFT)
        } else {
            TUI.write("OW.", 1, TUI.Location.LEFT)
        }
    }

    // Escreve as informações das moedas no LCD
    private fun writeCoins(idx: Int) {
        TUI.clear()
        val writeIdx = if(idx < 10) "0$idx:" else "$idx:"
        TUI.write(writeIdx, 1, TUI.Location.LEFT)
        TUI.write(CoinDeposit[idx].value.toEur(), 0, TUI.Location.CENTER)
        TUI.write(CoinDeposit[idx].amount.toString(), 1, TUI.Location.RIGHT)
    }

    // Escreve as opções/menus do modo de manutenção
    private fun maintenanceSelectWrite(idx: Int) {
        TUI.clear()
        TUI.write("Maintenance", 0, TUI.Location.CENTER)
        val text = when (idx) {
            0     -> "1 - Test"
            1     -> "2 - Tickets Chk."
            2     -> "3 - Coins Chk."
            3     -> "4 - Reset Cnt."
            4     -> "5 - Shutdown"
            else  -> "E: IDX INVALID."
        }
        TUI.write(text, 1, TUI.Location.CENTER)
    }

    // Converte o valor de uma moeda em cêntimos para euros para apresentar no LCD
    private fun Int.toEur(): String {
        val eur = "${this/100}"
        val cent = if(this%100 < 10) "0${this%100}" else "${this%100}"
        return "$eur.$cent EUR"
    }
}

fun main() {
    TicketMachine.init()
    TicketMachine.bootUp()
    while (true) {
        TicketMachine.standby()
        if (TicketMachine.maintenanceMode) {
            TicketMachine.maintenanceSelect()
        } else {
            val idx = TicketMachine.select(TicketMachine.Information.STATION_PRICE) ?: continue
            TicketMachine.startPurchase(idx)
        }
    }
}