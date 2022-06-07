object M {

    const val M_MASK = 0x40

    fun init() {
        HAL.init()
    }

    fun maintenanceCheck() = HAL.isBit(M_MASK)
}

fun main() {
    M.init()

    while (true) {
        println(M.maintenanceCheck())
    }
}