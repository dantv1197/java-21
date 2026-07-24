package behavioral.command

class Light {
    fun turnOn() = println("💡 Đèn đã BẬT")
    fun turnOff() = println("🌑 Đèn đã TẮT")
}

class Fan {
    fun start() = println("🌀 Quạt đang QUAY")
    fun stop() = println("🛑 Quạt đã DỪNG")
}