package behavioral.mediator

fun main() {
    val chatRoom = ChatRoom()

    val alex = ChatUser(chatRoom, "Alex")
    val bob = ChatUser(chatRoom, "Bob")
    val charlie = ChatUser(chatRoom, "Charlie")

    chatRoom.addUser(alex)
    chatRoom.addUser(bob)
    chatRoom.addUser(charlie)

    println("\n--- BẮT ĐẦU CHAT ---")
    alex.send("Chào mọi người!")
    println()
    bob.send("Chào Alex nhé!")
}