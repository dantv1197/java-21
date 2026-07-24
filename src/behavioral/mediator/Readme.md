# Mediator Design Pattern (Mô hình Trung gian)

> **Nhóm Pattern:** Behavioral (Hành vi)  
> **Tên gọi khác:** Intermediary, Controller

---

## 1. Định nghĩa & Khái niệm

**Mediator Pattern** cho phép bạn giảm thiểu sự phụ thuộc lẫn nhau giữa các đối tượng bằng cách **ngăn các đối tượng tương tác trực tiếp với nhau** và buộc chúng phải giao tiếp thông qua một **đối tượng trung gian (Mediator)**.

### Ý tưởng cốt lõi
Khi hệ thống có $N$ đối tượng tương tác chéo, số lượng kết nối sẽ tăng theo cấp số nhân ($N 	imes (N-1) / 2$), tạo thành một mảng lưới phụ thuộc chằng chịt.

Mediator Pattern chuyển mô hình từ **Nhiều-Nhiều (Many-to-Many)** về **1-Nhiều (1-to-Many)**. Mọi đối tượng (**Colleague**) chỉ giữ tham chiếu và giao tiếp với duy nhất một **Mediator**.

> **Hình ảnh thực tế:** Trạm điều hành không lưu (Air Traffic Control Tower) ở sân bay. Các máy bay không trao đổi trực tiếp với nhau để tranh đường bay/băng tải hạ cánh, mà tất cả đều liên lạc qua Trạm điều hành.

---

## 2. Cấu trúc Mô hình (Class Diagram)

```
┌───────────────────────────┐               ┌───────────────────────────┐
│       <<interface>>       │               │       <<interface>>       │
│         Mediator          │               │         Colleague         │
├───────────────────────────┤               ├───────────────────────────┤
│ + notify(sender, event)   │               │ - mediator: Mediator      │
└───────────────────────────┘               └───────────────────────────┘
              ▲                                           ▲
              │ implements                                │ implements
┌─────────────┴─────────────┐               ┌─────────────┴─────────────┐
│     ConcreteMediator      │               │     ConcreteColleague     │
│   (ChatRoom / Controller) │──────────────►│    (User / Component)     │
└───────────────────────────┘  coordinates  └───────────────────────────┘
```

### Các thành phần chính

| Thành phần | Vai trò |
| :--- | :--- |
| **Mediator (Interface)** | Khai báo các phương thức giao tiếp với các Colleague. |
| **ConcreteMediator** | Triển khai logic điều phối, quản lý danh sách và mối quan hệ giữa các Colleague. |
| **Colleague (Base Class/Interface)** | Chứa logic nghiệp vụ riêng biệt. Giữ tham chiếu đến `Mediator` và gửi/nhận thông điệp qua Mediator. |

---

## 3. Minh họa Code mẫu hoàn chỉnh (Kotlin)

Ví dụ hệ thống **Phòng Chat (ChatRoom)** đóng vai trò Mediator điều phối tin nhắn giữa các **Người dùng (User)**.

### Bước 1: Khai báo Interface & Abstract Class

```kotlin
// Interface cho Mediator
interface ChatMediator {
    fun sendMessage(message: String, sender: User)
    fun addUser(user: User)
}

// Base Class cho Colleague
abstract class User(
    protected val mediator: ChatMediator,
    val name: String
) {
    abstract fun send(message: String)
    abstract fun receive(message: String)
}
```

### Bước 2: Triển khai ConcreteColleague (User cụ thể)

```kotlin
class ChatUser(mediator: ChatMediator, name: String) : User(mediator, name) {

    override fun send(message: String) {
        println("💬 [$name] Gửi tin nhắn: "$message"")
        // Gửi qua Mediator chứ không gọi trực tiếp User khác
        mediator.sendMessage(message, this)
    }

    override fun receive(message: String) {
        println("📩 [$name] Nhận tin nhắn: "$message"")
    }
}
```

### Bước 3: Triển khai ConcreteMediator (ChatRoom)

```kotlin
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
```

### Bước 4: Client (Hàm Main)

```kotlin
fun main() {
    val chatRoom = ChatRoom()

    val alex = ChatUser(chatRoom, "Alex")
    val bob = ChatUser(chatRoom, "Bob")
    val charlie = ChatUser(chatRoom, "Charlie")

    chatRoom.addUser(alex)
    chatRoom.addUser(bob)
    chatRoom.addUser(charlie)

    println("
--- BẮT ĐẦU CHAT ---")
    alex.send("Chào mọi người!")
    println()
    bob.send("Chào Alex nhé!")
}
```

---

## 4. Ưu điểm & Nhược điểm

### Ưu điểm
* **Single Responsibility Principle (SRP):** Gom toàn bộ logic giao tiếp và điều phối phức tạp giữa các thành phần vào một lớp Mediator duy nhất.
* **Open/Closed Principle (OCP):** Thêm các Colleague mới mà không làm ảnh hưởng đến mã nguồn sẵn có.
* **Tái sử dụng các thành phần:** Do các lớp Colleague hoàn toàn độc lập với nhau, bạn dễ dàng tái sử dụng chúng trong các ngữ cảnh khác.
* **Giảm sự phụ thuộc chằng chịt:** Chuyển giao tiếp phức tạp $N 	imes N$ thành mô hình điều phối đơn giản $1 	imes N$.

### Nhược điểm
* **Nguy cơ trở thành God Object:** Nếu hệ thống quá lớn, lớp Mediator có thể gánh quá nhiều logic điều phối, trở nên cồng kềnh, phức tạp và rất khó bảo trì.

---

## 5. Khi nào nên dùng Mediator Pattern?

1. **Giao diện UI phức tạp (Complex UI Form):** Khi sự thay đổi của một widget tác động đến hàng loạt widget khác (VD: Bấm Checkbox $A$ $
   ightarrow$ Disable Textfield $B$, hiện Dropdown $C$, enable Button $D$).
2. **Hệ thống Chat / Message Broker:** Điều phối tin nhắn tập trung giữa các Client hoặc Service.
3. **Khi các lớp bị phụ thuộc lẫn nhau quá chặt chẽ:** Làm bạn không thể tái sử dụng một lớp ở nơi khác nếu không kéo theo danh sách dài các lớp liên quan.