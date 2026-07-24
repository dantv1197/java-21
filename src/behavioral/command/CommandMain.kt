package behavioral.command

fun main() {
    // 1. Tạo các Receiver
    val livingRoomLight = Light()
    val ceilingFan = Fan()

    // 2. Tạo các Command
    val lightOn = LightOnCommand(livingRoomLight)
    val lightOff = LightOffCommand(livingRoomLight)
    val fanStart = FanStartCommand(ceilingFan)

    // 3. Tạo Invoker
    val remote = RemoteControl()

    println("=== THỰC THI CÁC THAO TÁC ===")
    remote.pressButton(lightOn)  // Bật đèn
    remote.pressButton(fanStart) // Bật quạt
    remote.pressButton(lightOff) // Tắt đèn

    println("\n=== THỰC THI HOÀN TÁC (UNDO) ===")
    remote.pressUndo() // Undo lệnh Tắt đèn -> Đèn BẬT lại
    remote.pressUndo() // Undo lệnh Bật quạt -> Quạt DỪNG
    remote.pressUndo() // Undo lệnh Bật đèn -> Đèn TẮT
    remote.pressUndo() // Không còn gì để undo
}