# Iterator Design Pattern (Mô hình Con trỏ)

> **Nhóm Pattern:** Behavioral (Hành vi)  
> **Tên gọi khác:** Cursor

---

## 1. Tổng quan & Khái niệm

**Iterator Pattern** cho phép bạn truy cập/duyệt qua tuần tự các phần tử của một tập hợp (Collection) mà **không cần lộ cấu trúc lưu trữ bên trong** của tập hợp đó (dù nó là Array, ArrayList, LinkedList, Tree hay Graph).

### Ý tưởng cốt lõi
Tách biệt **trách nhiệm quản lý dữ liệu** (của Collection) và **trách nhiệm duyệt dữ liệu** (của Iterator). Điều này giúp Client duyệt qua các tập hợp khác nhau bằng một giao diện chung thống nhất mà không cần quan tâm đến logic lưu trữ bên dưới.

---

## 2. Cấu trúc Mô hình (Class Diagram)

```
┌──────────────────────────────┐              ┌──────────────────────────────┐
│        <<interface>>         │              │        <<interface>>         │
│      PlaylistCollection      │              │     BidirectionalIterator    │
├──────────────────────────────┤              ├──────────────────────────────┤
│ + createIterator(mode):      │              │ + hasNext(): Boolean         │
│   BidirectionalIterator      │              │ + next(): T                  │
└──────────────────────────────┘              │ + hasPrevious(): Boolean     │
               ▲                              │ + previous(): T              │
               │ implements                   └──────────────────────────────┘
 ┌─────────────┴──────────────┐                              ▲
 │          Playlist          │                              │ implements
 ├────────────────────────────┤         ┌────────────────────┴────────────────────┐
 │ - songs: List<Song>        │         │                                         │
 │ + createIterator(mode)...  │─────────┼────────────────────────┐                │
 └────────────────────────────┘ creates │                        │                │
                               ┌────────┴─────────────┐ ┌────────┴──────────────┐ │
                               │ SequentialIterator   │ │ PureRandomShuffle... │ │
                               └──────────────────────┘ └───────────────────────┘ │
```

### Các thành phần chính

| Thành phần | Vai trò |
| :--- | :--- |
| **Iterator (Interface)** | Khai báo các phương thức duyệt (`hasNext()`, `next()`, `previous()`,...). |
| **ConcreteIterator** | Triển khai logic duyệt cụ thể (duyệt tuần tự, duyệt xáo trộn kèm lịch sử history stack,...). |
| **Aggregate / Collection (Interface)** | Khai báo phương thức khởi tạo Iterator (`createIterator()`). |
| **ConcreteAggregate** | Lưu trữ tập hợp dữ liệu gốc và trả về instance Iterator tương ứng. |

---

## 3. Minh họa Code mẫu hoàn chỉnh (Kotlin)

Ví dụ hệ thống **Trình phát nhạc (Music Player)** hỗ trợ duyệt 2 chế độ: **Phát tuần tự (Sequential)** và **Phát ngẫu nhiên hoàn toàn kèm Lịch sử (Pure Random Shuffle + History Stack)**.

### Bước 1: Data Model & Iterator Interface

```kotlin
data class Song(
    val title: String,
    val artist: String
)

interface BidirectionalIterator<T> {
    fun hasNext(): Boolean
    fun next(): T
    fun hasPrevious(): Boolean
    fun previous(): T
}
```

### Bước 2: Triển khai các ConcreteIterators

#### A. Duyệt tuần tự (Sequential)

```kotlin
class SequentialIterator(private val songs: List<Song>) : BidirectionalIterator<Song> {
    private var currentIndex = -1

    override fun hasNext(): Boolean = currentIndex + 1 < songs.size

    override fun next(): Song {
        if (!hasNext()) throw NoSuchElementException("Đã đến bài hát cuối cùng!")
        currentIndex++
        return songs[currentIndex]
    }

    override fun hasPrevious(): Boolean = currentIndex > 0

    override fun previous(): Song {
        if (!hasPrevious()) throw NoSuchElementException("Đã ở bài hát đầu tiên!")
        currentIndex--
        return songs[currentIndex]
    }
}
```

#### B. Duyệt ngẫu nhiên hoàn toàn + Lịch sử (Pure Random Shuffle)

```kotlin
import kotlin.random.Random

class PureRandomShuffleIterator(private val songs: List<Song>) : BidirectionalIterator<Song> {
    private val history = mutableListOf<Song>()
    private var historyIndex = -1

    override fun hasNext(): Boolean = songs.isNotEmpty()

    override fun next(): Song {
        if (!hasNext()) throw NoSuchElementException("Danh sách bài hát rỗng!")

        // Nếu đang lùi trong lịch sử mà bấm Next -> Đi tiếp trong lịch sử cũ
        if (historyIndex < history.size - 1) {
            historyIndex++
            return history[historyIndex]
        }

        // Nếu đang ở cuối lịch sử -> Bốc ngẫu nhiên 1 bài mới hoàn toàn
        val randomIndex = Random.nextInt(songs.size)
        val nextSong = songs[randomIndex]

        history.add(nextSong)
        historyIndex++

        return nextSong
    }

    override fun hasPrevious(): Boolean = historyIndex > 0

    override fun previous(): Song {
        if (!hasPrevious()) throw NoSuchElementException("Đã ở đầu lịch sử phát nhạc!")
        historyIndex--
        return history[historyIndex]
    }
}
```

### Bước 3: Triển khai Aggregate (Playlist)

```kotlin
enum class PlayMode {
    SEQUENTIAL,
    PURE_SHUFFLE
}

interface PlaylistCollection {
    fun createIterator(mode: PlayMode): BidirectionalIterator<Song>
}

class Playlist : PlaylistCollection {
    private val songs = mutableListOf<Song>()

    fun addSong(song: Song) {
        songs.add(song)
    }

    override fun createIterator(mode: PlayMode): BidirectionalIterator<Song> {
        return when (mode) {
            PlayMode.SEQUENTIAL -> SequentialIterator(songs)
            PlayMode.PURE_SHUFFLE -> PureRandomShuffleIterator(songs)
        }
    }
}
```

### Bước 4: Client (Music Player) & Hàm Main

```kotlin
class MusicPlayer(private val playlist: PlaylistCollection) {
    private var iterator: BidirectionalIterator<Song>? = null

    fun setPlayMode(mode: PlayMode) {
        println("\n>>> ĐỔI CHẾ ĐỘ PHÁT: $mode <<<")
        iterator = playlist.createIterator(mode)
    }

    fun onNextButtonClicked() {
        iterator?.let {
            if (it.hasNext()) {
                val song = it.next()
                println("▶️ Next -> Đang phát: ${song.title}")
            } else {
                println("⚠️ Không thể Next (Hết danh sách)")
            }
        }
    }

    fun onPreviousButtonClicked() {
        iterator?.let {
            if (it.hasPrevious()) {
                val song = it.previous()
                println("◀️ Previous -> Quay lại: ${song.title}")
            } else {
                println("⚠️ Không thể Previous (Đang ở đầu)")
            }
        }
    }
}

fun main() {
    val playlist = Playlist().apply {
        addSong(Song("Bài A", "Ca sĩ 1"))
        addSong(Song("Bài B", "Ca sĩ 2"))
        addSong(Song("Bài C", "Ca sĩ 3"))
    }

    val player = MusicPlayer(playlist)

    // Chạy chế độ Tuần tự
    player.setPlayMode(PlayMode.SEQUENTIAL)
    player.onNextButtonClicked() // Bài A
    player.onNextButtonClicked() // Bài B
    player.onPreviousButtonClicked() // Bài A

    // Chuyển sang chế độ Shuffle Ngẫu nhiên + Lịch sử
    player.setPlayMode(PlayMode.PURE_SHUFFLE)
    player.onNextButtonClicked() // Ngẫu nhiên 1
    player.onNextButtonClicked() // Ngẫu nhiên 2
    player.onPreviousButtonClicked() // Quay lại Ngẫu nhiên 1
    player.onNextButtonClicked() // Tiến lên lại Ngẫu nhiên 2
}
```

---

## 4. Ưu điểm & Nhược điểm

### Ưu điểm
* **Single Responsibility Principle (SRP):** Tách biệt cấu trúc lưu trữ dữ liệu và thuật toán duyệt dữ liệu ra các lớp riêng biệt.
* **Open/Closed Principle (OCP):** Dễ dàng thêm các thuật toán duyệt mới (như `LoopIterator`, `SmartShuffleIterator`) mà không làm ảnh hưởng đến class Playlist hay Player.
* **Duyệt song song:** Có thể khởi tạo nhiều Iterator chạy độc lập trên cùng một tập hợp dữ liệu.

### Nhược điểm
* **Phức tạp hóa code:** Với các danh sách đơn giản chỉ cần duyệt tuần tự, việc tạo thêm nhiều Interface/Class có thể làm phình kiến trúc ứng dụng.
* **Tốn thêm bộ nhớ:** Tạo đối tượng Iterator mới và lưu trữ trạng thái duyệt (như History Stack) sẽ tiêu tốn thêm tài nguyên RAM.

---

## 5. Ứng dụng thực tế

* **JDK / Kotlin Stdlib:** Interfaces `Iterable<T>`, `Iterator<T>`, `ListIterator<T>` được tích hợp sẵn trong các Collection (`ArrayList`, `HashSet`, `HashMap`).
* **Vòng lặp `for-in`:** Các ngôn ngữ hiện đại tự động gọi Iterator ở phía dưới khi thực hiện vòng lặp `for (item in collection)`.
* **Database Cursors:** Cơ chế fetch dữ liệu từng dòng trong Database mà không nạp toàn bộ Bảng vào RAM.