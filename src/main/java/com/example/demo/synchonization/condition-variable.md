
# Tóm Tắt Java Concurrency: Condition Variables

**Condition** (Biến điều kiện) là một công cụ mạnh mẽ trong lập trình đa luồng của Java, luôn đi kèm với `Lock` (như `ReentrantLock`). Nó cho phép các luồng tạm dừng (`wait`) hoặc tiếp tục (`resume`) dựa trên các điều kiện cụ thể, giúp quản lý luồng linh hoạt và tinh chỉnh tốt hơn hẳn so với cơ chế truyền thống (`wait()`, `notify()`, `notifyAll()` đi kèm khối `synchronized`).

---

## 1. Khái niệm & Phương thức cốt lõi

* **Khởi tạo:** Được tạo ra từ đối tượng `Lock` thông qua phương thức `lock.newCondition()`. Một `Lock` có thể tạo ra nhiều `Condition` khác nhau.
* **Các phương thức chính:**
* `await()`: Đưa luồng hiện tại vào trạng thái chờ cho đến khi được luồng khác phát tín hiệu (thay thế cho `Object.wait()`).
* `signal()`: Đánh thức **một** luồng đang chờ trên điều kiện đó (thay thế cho `Object.notify()`).
* `signalAll()`: Đánh thức **tất cả** các luồng đang chờ trên điều kiện đó (thay thế cho `Object.notifyAll()`).



---

## 2. Mô hình Producer - Consumer (Hàng chờ dùng chung)

Bài toán điển hình: **Producer** tạo dữ liệu đưa vào Hàng chờ (Queue), **Consumer** lấy dữ liệu ra khỏi Hàng chờ.

* **Producer phải chờ** khi Queue đầy (`notFull.await()`). Khi thêm xong phần tử, phát tín hiệu báo Queue hết rỗng (`notEmpty.signal()`).
* **Consumer phải chờ** khi Queue rỗng (`notEmpty.await()`). Khi lấy xong phần tử, phát tín hiệu báo Queue hết đầy (`notFull.signal()`).

---

## 3. Cấu trúc Triển khai Mã nguồn

### Lớp quản lý hàng chờ: `SharedQueue.java`

```java
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SharedQueue {
    private final Queue<Integer> queue = new LinkedList<>();
    private final int capacity = 5;

    private final Lock lock = new ReentrantLock();
    // Điều kiện 1: Dành cho Producer chờ khi hàng chờ ĐẦY
    private final Condition notFull = lock.newCondition();
    // Điều kiện 2: Dành cho Consumer chờ khi hàng chờ RỖNG
    private final Condition notEmpty = lock.newCondition();

    // Thao tác của Producer (Thêm dữ liệu)
    public void produce(int item) throws InterruptedException {
        lock.lock();
        try {
            // Sử dụng vòng lặp while để tránh Spurious Wakeup
            while (queue.size() == capacity) {
                System.out.println("Queue đầy! Producer tạm dừng chờ...");
                notFull.await(); // Producer chờ cho đến khi có chỗ trống
            }

            queue.add(item);
            System.out.println("Producer đã sản xuất: " + item);

            // Báo tín hiệu cho Consumer biết đã có dữ liệu để lấy
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    // Thao tác của Consumer (Lấy dữ liệu)
    public int consume() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                System.out.println("Queue rỗng! Consumer tạm dừng chờ...");
                notEmpty.await(); // Consumer chờ cho đến khi có dữ liệu
            }

            int item = queue.poll();
            System.out.println("Consumer đã tiêu thụ: " + item);

            // Báo tín hiệu cho Producer biết đã có thêm chỗ trống
            notFull.signal();
            return item;
        } finally {
            lock.unlock();
        }
    }
}

```

### Lớp kiểm thử và chạy Luồng: `Main.java`

```java
public class Main {
    public static void main(String[] args) {
        SharedQueue sharedQueue = new SharedQueue();

        // Luồng Producer
        Thread producerThread = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    sharedQueue.produce(i);
                    Thread.sleep(100); // Giả lập thời gian sản xuất
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Luồng Consumer
        Thread consumerThread = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    sharedQueue.consume();
                    Thread.sleep(150); // Giả lập thời gian tiêu thụ
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producerThread.start();
        consumerThread.start();
    }
}

```

---

## 4. Điểm quan trọng cần nhớ (Key Takeaways)

1. **Phải sử dụng `while` thay vì `if` khi kiểm tra điều kiện chờ (`await()`):** Điều này giúp phòng tránh trường hợp luồng bị đánh thức ngẫu nhiên khi điều kiện chưa thỏa mãn (*Spurious Wakeup*).
2. **Quản lý đa luồng chính xác:** Khác với `wait/notify` truyền thống áp dụng chung trên 1 đối tượng, `Condition` phân loại chính xác nhóm luồng cần đánh thức (`notFull` đánh thức Producer, `notEmpty` đánh thức Consumer).
3. **An toàn bộ nhớ và không giật lag/bế tắc:** Đảm bảo hệ thống vận hành mượt mà, tránh lỗi tranh chấp dữ liệu (Race condition) hoặc Bế tắc (Deadlock).