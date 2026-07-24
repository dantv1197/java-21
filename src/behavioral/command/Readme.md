# Command Design Pattern (Mô hình Mệnh lệnh)

> **Nhóm Pattern:** Behavioral (Hành vi)  
> **Tên gọi khác:** Action, Transaction

---

## 1. Tổng quan & Khái niệm

**Command Pattern** chuyển đổi một yêu cầu (request) hoặc một hành động thành một **đối tượng độc lập (Command object)**.

### Ý tưởng cốt lõi
Tách biệt đối tượng kích hoạt hành động (**Invoker**) khỏi đối tượng thực sự biết cách thực hiện hành động đó (**Receiver**). Việc đóng gói thao tác thành đối tượng riêng giúp dễ dàng tham số hóa các thao tác, đưa công việc vào hàng đợi (Queue), ghi nhật ký (Log), cũng như hỗ trợ tính năng **Hoàn tác (Undo / Redo)**.

---

## 2. Cấu trúc Mô hình (Class Diagram)

```
┌──────────────────────────┐               ┌──────────────────────────┐
│         Invoker          │               │      <<interface>>       │
│     (RemoteControl)      │               │         Command          │
├──────────────────────────┤               ├──────────────────────────┤
│ - history: Deque<Command>│──────────────►│ + execute()              │
│ + pressButton(c: Command)│               │ + undo()                 │
│ + pressUndo()            │               └──────────────────────────┘
└──────────────────────────┘                            ▲
                                                        │ implements
                                           ┌────────────┴────────────┐
                                           │     ConcreteCommand     │
                                           │    (LightOnCommand)     │
                                           ├─────────────────────────┤
                                           │ - light: Light          │
                                           ├─────────────────────────┤
                                           │ + execute()             │
                                           │ + undo()                │
                                           └─────────────────────────┘
                                                        │
                                                        │ controls
                                                        ▼
                                           ┌─────────────────────────┐
                                           │        Receiver         │
                                           │         (Light)         │
                                           ├─────────────────────────┤
                                           │ + turnOn()              │
                                           │ + turnOff()             │
                                           └─────────────────────────┘
```

### Các thành phần chính

| Thành phần | Vai trò |
| :--- | :--- |
| **Command (Interface)** | Khai báo phương thức `execute()` và tùy chọn `undo()`. |
| **ConcreteCommand** | Liên kết giữa một hành động và lớp `Receiver`. Triển khai `execute()` và `undo()` bằng cách gọi phương thức tương ứng trên `Receiver`. |
| **Receiver** | Lớp chứa logic xử lý nghiệp vụ thực sự (ví dụ: `Light`, `Fan`, `DatabaseManager`). |
| **Invoker** | Yêu cầu Command thực thi hành động (ví dụ: Button UI, Remote Control, Job Queue). |
| **Client** | Khởi tạo các đối tượng `ConcreteCommand` và gán `Receiver` tương ứng cho chúng. |

---

## 3. Minh họa Code mẫu hoàn chỉnh (Kotlin)

Ví dụ hệ thống **Điều khiển từ xa nhà thông minh (Smart Remote Control)** hỗ trợ Bật/Tắt thiết bị và chức năng **Undo (Hoàn tác)** chuẩn LIFO Stack.

### Bước 1: Khai báo Receiver (Các thiết bị thực tế)

```kotlin
class Light {
    fun turnOn() = println("💡 Đèn đã BẬT")
    fun turnOff() = println("🌑 Đèn đã TẮT")
}

class Fan {
    fun start() = println("🌀 Quạt đang QUAY")
    fun stop() = println("🛑 Quạt đã DỪNG")
}
```

### Bước 2: Khai báo Command Interface

```kotlin
interface Command {
    fun execute()
    fun undo()
}
```

### Bước 3: Triển khai các ConcreteCommands

```kotlin
// Command Bật Đèn
class LightOnCommand(private val light: Light) : Command {
    override fun execute() = light.turnOn()
    override fun undo() = light.turnOff()
}

// Command Tắt Đèn
class LightOffCommand(private val light: Light) : Command {
    override fun execute() = light.turnOff()
    override fun undo() = light.turnOn()
}

// Command Bật Quạt
class FanStartCommand(private val fan: Fan) : Command {
    override fun execute() = fan.start()
    override fun undo() = fan.stop()
}
```

### Bước 4: Khai báo Invoker (Remote Control hỗ trợ Lịch sử Undo bằng ArrayDeque)

```kotlin
import java.util.ArrayDeque

class RemoteControl {
    // Lưu lịch sử các lệnh theo cơ chế LIFO Stack
    private val history = ArrayDeque<Command>()

    // Bấm nút để thực thi lệnh
    fun pressButton(command: Command) {
        command.execute()
        history.addFirst(command) // Đưa lệnh vừa chạy vào đỉnh Stack
    }

    // Bấm nút Undo để hoàn tác lệnh gần nhất
    fun pressUndo() {
        if (history.isNotEmpty()) {
            val lastCommand = history.removeFirst() // Lấy lệnh gần nhất ở đỉnh Stack
            println("↩️ [UNDO] Đang hoàn tác thao tác vừa rồi...")
            lastCommand.undo()
        } else {
            println("⚠️ Không có thao tác nào để Undo!")
        }
    }
}
```

### Bước 5: Client (Hàm Main)

```kotlin
fun main() {
    // 1. Khởi tạo Receiver
    val livingRoomLight = Light()
    val ceilingFan = Fan()

    // 2. Khởi tạo Command
    val lightOn = LightOnCommand(livingRoomLight)
    val lightOff = LightOffCommand(livingRoomLight)
    val fanStart = FanStartCommand(ceilingFan)

    // 3. Khởi tạo Invoker
    val remote = RemoteControl()

    println("=== THỰC THI CÁC THAO TÁC ===")
    remote.pressButton(lightOn)  // Bật đèn
    remote.pressButton(fanStart) // Bật quạt
    remote.pressButton(lightOff) // Tắt đèn

    println("
=== THỰC THI HOÀN TÁC (UNDO) ===")
    remote.pressUndo() // Undo lệnh Tắt đèn -> Đèn BẬT lại
    remote.pressUndo() // Undo lệnh Bật quạt -> Quạt DỪNG
    remote.pressUndo() // Undo lệnh Bật đèn -> Đèn TẮT
    remote.pressUndo() // Không còn gì để undo
}
```

---

## 4. Ưu điểm & Nhược điểm

### Ưu điểm
* **Decoupling (Lỏng lẻo phụ thuộc):** Tách biệt hoàn toàn đối tượng phát ra yêu cầu (`Invoker`) và đối tượng xử lý yêu cầu (`Receiver`).
* **Single Responsibility Principle (SRP):** Tách rời lớp kích hoạt giao diện/sự kiện và lớp logic xử lý công việc.
* **Open/Closed Principle (OCP):** Dễ dàng bổ sung các `Command` mới mà không cần chỉnh sửa mã nguồn sẵn có.
* **Dễ dàng cài đặt Undo / Redo:** Lưu các đối tượng Command vào `Stack` giúp khôi phục trạng thái cũ cực kỳ linh hoạt.
* **Macro Commands:** Có thể gom nhiều Command nhỏ thành một chuỗi lệnh phức tạp (Composite Command).

### Nhược điểm
* **Bùng nổ số lượng lớp (Class Explosion):** Code có thể trở nên cồng kềnh vì phải viết thêm nhiều class Command riêng lẻ cho từng thao tác.

---

## 5. Khi nào nên dùng Command Pattern?

* Khi muốn **tham số hóa các đối tượng** bằng các hành động (như gán nút bấm UI với một Command cụ thể).
* Khi hệ thống cần **tính năng Undo / Redo** (ứng dụng văn bản, chỉnh sửa ảnh, canvas).
* Khi cần **đưa công việc vào hàng đợi (Job Queue)** hoặc lập lịch chạy (Task Scheduler/Background Worker).
* Khi cần **ghi nhật ký thao tác (Transaction Log)** để có thể khôi phục dữ liệu khi ứng dụng gặp sự cố.