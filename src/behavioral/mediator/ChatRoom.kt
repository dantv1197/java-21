package behavioral.mediator

class ChatRoom : ChatMediator {
    private val users = mutableListOf<User>()

    override fun addUser(user: User) {
        users.add(user)
        println("📢 System: ${user.name} đã tham gia phòng chat.")
    }

    override fun sendMessage(message: String, sender: User) {
        // Điều phối tin nhắn đến tất cả người dùng khác, trừ người gửi
        for (user in users) {
            if (user != sender) {
                user.receive("${sender.name}: $message")
            }
        }
    }
}