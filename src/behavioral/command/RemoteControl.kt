package behavioral.command

class RemoteControl {
    private val history = ArrayDeque<Command>()

    // Bấm nút để thực thi lệnh
    fun pressButton(command: Command) {
        command.execute()
        history.addFirst(command) // Lưu lệnh vào lịch sử
    }

    // Bấm nút Undo để hoàn tác lệnh vừa chạy
    fun pressUndo() {
        if (history.isNotEmpty()) {
            val lastCommand = history.removeFirst()
            println("↩️ [UNDO] Đang hoàn tác thao tác vừa rồi...")
            lastCommand.undo()
        } else {
            println("⚠️ Không có thao tác nào để Undo!")
        }
    }
}