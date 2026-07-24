package behavioral.command

interface Command {
    fun execute()
    fun undo()
}

// Command điều khiển Đèn Bật
class LightOnCommand(private val light: Light) : Command {
    override fun execute() = light.turnOn()
    override fun undo() = light.turnOff()
}

// Command điều khiển Đèn Tắt
class LightOffCommand(private val light: Light) : Command {
    override fun execute() = light.turnOff()
    override fun undo() = light.turnOn()
}

// Command điều khiển Quạt
class FanStartCommand(private val fan: Fan) : Command {
    override fun execute() = fan.start()
    override fun undo() = fan.stop()
}