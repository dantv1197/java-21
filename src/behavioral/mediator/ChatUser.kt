package behavioral.mediator

class ChatUser(mediator: ChatMediator, name: String) : User(mediator, name) {

    override fun send(message: String) {
        println("💬 [$name] Gửi tin nhắn: \"$message\"")
        // Gửi qua Mediator chứ không gọi trực tiếp User khác
        mediator.sendMessage(message, this)
    }

    override fun receive(message: String) {
        println("📩 [$name] Nhận tin nhắn: \"$message\"")
    }
}