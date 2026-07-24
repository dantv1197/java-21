package behavioral.mediator

interface ChatMediator {
    fun sendMessage(message: String, sender: User)
    fun addUser(user: User)
}

// Colleague Base Class
abstract class User(
    protected val mediator: ChatMediator,
    val name: String
) {
    abstract fun send(message: String)
    abstract fun receive(message: String)
}