# State Design Pattern (Mô hình Trạng thái)

> **Nhóm Pattern:** Behavioral (Hành vi)  
> **Tên gọi khác:** Objects for States

---

## 1. Tổng quan & Khái niệm

**State Pattern** cho phép một đối tượng **thay đổi hành vi của nó khi trạng thái nội bộ (internal state) thay đổi**. Đối tượng sẽ trông như thể nó đã thay đổi lớp (class) của chính nó.

### Ý tưởng cốt lõi
Thay vì dùng các cấu trúc điều khiển `if-else` hoặc `switch-case` phức tạp để kiểm tra trạng thái hiện tại và đưa ra hành vi tương ứng, State Pattern đóng gói từng trạng thái thành một Class riêng biệt. Đối tượng gốc (**Context**) sẽ ủy nhiệm (delegate) công việc xử lý hành vi cho đối tượng State hiện tại.

---

## 2. Cấu trúc Mô hình (Class Diagram)

```
┌──────────────────────────┐              ┌──────────────────────────┐
│         Context          │              │      <<interface>>       │
│      (MusicPlayer)       │              │        State             │
├──────────────────────────┤              ├──────────────────────────┤
│ - state: State           │─────────────►│ + clickPlay(player)      │
│ + changeState(state)     │  delegates   │ + clickStop(player)      │
│ + clickPlay()            │              └──────────────────────────┘
│ + clickStop()            │                            ▲
└──────────────────────────┘                            │ implements
                                 ┌──────────────────────┼──────────────────────┐
                                 │                      │                      │
                   ┌─────────────┴────────────┐ ┌───────┴──────────────┐ ┌─────┴──────────────┐
                   │       StoppedState       │ │     PlayingState     │ │    PausedState     │
                   └──────────────────────────┘ └──────────────────────┘ └────────────────────┘
```

### Các thành phần chính

| Thành phần | Vai trò |
| :--- | :--- |
| **Context** | Lớp duy trì một tham chiếu đến đối tượng `State` hiện tại và cung cấp interface cho Client tương tác. |
| **State (Interface)** | Khai báo các hành vi chung mà tất cả các trạng thái cụ thể phải triển khai. |
| **ConcreteStates** | Các lớp thực thi giao diện State, chứa logic cụ thể khi ở trạng thái đó và chịu trách nhiệm chuyển đổi trạng thái (`changeState`). |

---

## 3. Minh họa Code mẫu hoàn chỉnh (Kotlin)

Ví dụ hệ thống **Trình phát nhạc (Music Player)** quản lý 3 trạng thái: **Stopped (Đã dừng)**, **Playing (Đang phát)**, và **Paused (Tạm dừng)**.

### Bước 1: Khai báo State Interface

```kotlin
interface State {
    fun clickPlay(player: MusicPlayer)
    fun clickStop(player: MusicPlayer)
}
```

### Bước 2: Tạo lớp Context (`MusicPlayer`)

```kotlin
class MusicPlayer {
    // Trạng thái mặc định ban đầu
    var state: State = StoppedState()

    fun changeState(newState: State) {
        println("🔄 Chuyển trạng thái: ${state::class.simpleName} ──► ${newState::class.simpleName}")
        this.state = newState
    }

    // Các hàm ủy nhiệm cho State hiện tại xử lý
    fun clickPlay() {
        state.clickPlay(this)
    }

    fun clickStop() {
        state.clickStop(this)
    }

    // Các hành động playback thực tế
    fun startAudio() = println("🎵 Đang phát nhạc...")
    fun pauseAudio() = println("⏸️ Đang tạm dừng...")
    fun stopAudio()  = println("⏹️ Đã dừng hẳn bài hát.")
}
```

### Bước 3: Triển khai các Concrete States

#### A. StoppedState (Trạng thái Đã dừng)

```kotlin
class StoppedState : State {
    override fun clickPlay(player: MusicPlayer) {
        println("👉 Bấm Play khi đang STOP:")
        player.startAudio()
        player.changeState(PlayingState())
    }

    override fun clickStop(player: MusicPlayer) {
        println("👉 Bấm Stop khi đang STOP: Nhạc đã dừng rồi, không làm gì thêm.")
    }
}
```

#### B. PlayingState (Trạng thái Đang phát)

```kotlin
class PlayingState : State {
    override fun clickPlay(player: MusicPlayer) {
        println("👉 Bấm Play khi đang PLAYING:")
        player.pauseAudio()
        player.changeState(PausedState())
    }

    override fun clickStop(player: MusicPlayer) {
        println("👉 Bấm Stop khi đang PLAYING:")
        player.stopAudio()
        player.changeState(StoppedState())
    }
}
```

#### C. PausedState (Trạng thái Tạm dừng)

```kotlin
class PausedState : State {
    override fun clickPlay(player: MusicPlayer) {
        println("👉 Bấm Play khi đang PAUSED:")
        player.startAudio()
        player.changeState(PlayingState())
    }

    override fun clickStop(player: MusicPlayer) {
        println("👉 Bấm Stop khi đang PAUSED:")
        player.stopAudio()
        player.changeState(StoppedState())
    }
}
```

### Bước 4: Client (Hàm Main)

```kotlin
fun main() {
    val player = MusicPlayer()

    println("=== LẦN 1: Bấm Play từ StoppedState ===")
    player.clickPlay() // Phát nhạc -> Chuyển sang PlayingState

    println("\n=== LẦN 2: Bấm Play khi đang PlayingState ===")
    player.clickPlay() // Tạm dừng -> Chuyển sang PausedState

    println("\n=== LẦN 3: Bấm Play khi đang PausedState ===")
    player.clickPlay() // Tiếp tục phát -> Chuyển sang PlayingState

    println("\n=== LẦN 4: Bấm Stop khi đang PlayingState ===")
    player.clickStop() // Dừng hẳn -> Chuyển sang StoppedState
}
```

---

## 4. Ưu điểm & Nhược điểm

### Ưu điểm
* **Single Responsibility Principle (SRP):** Tách bạch rõ ràng logic xử lý của từng trạng thái vào từng lớp riêng biệt.
* **Open/Closed Principle (OCP):** Dễ dàng thêm trạng thái mới (như `BufferingState`, `FastForwardState`) mà không cần sửa đổi code của các State cũ hay Context.
* **Loại bỏ vòng lặp/điều kiện phức tạp:** Loại bỏ hoàn toàn các khối `if-else` hoặc `switch-case` cồng kềnh trong Context.

### Nhược điểm
* **Tăng số lượng Class:** Có thể dẫn đến việc bùng nổ số lượng class nếu hệ thống có quá nhiều trạng thái nhỏ hoặc ít khi thay đổi.
* **Độ phức tạp tăng nhẹ:** Yêu cầu phải quản lý mối liên kết giữa Context và State cũng như việc chuyển đổi giữa các State với nhau.

---

## 5. Ứng dụng thực tế

* **State Machine trong Mobile / Web / Game Dev:** Quản lý State của UI (Loading, Success, Error), Animation State Machine hay AI State trong Game (Idle, Run, Attack, Dead).
* **Quy trình Đơn hàng (E-commerce):** Đơn hàng qua các trạng thái: `Created` ──► `Paid` ──► `Shipping` ──► `Delivered` ──► `Cancelled`.
* **Máy bán hàng / Két sắt tự động:** Quản lý trạng thái chờ tiền, đã nhận tiền, nhả hàng và trả tiền thừa.