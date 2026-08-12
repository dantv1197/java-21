
# Tóm Tắt Java Concurrency: ReentrantLock & ReentrantReadWriteLock

Lớp khóa linh hoạt (**Lock API**) trong gói `java.util.concurrent.locks` cung cấp khả năng quản lý đồng bộ hóa nâng cao, vượt qua các hạn chế của từ khóa truyền thống `synchronized`.

---

## 1. ReentrantLock

### Khái niệm chính

* **ReentrantLock** là lớp triển khai của giao diện `Lock`.
* **Khả năng tái vào (Reentrancy):** Cho phép một luồng (thread) có thể tái sở hữu (re-acquire) chính ổ khóa mà nó đang nắm giữ mà không tự gây ra bế tắc (deadlock).
* **Ưu điểm so với `synchronized`:**
* Kiểm soát việc khóa/mở khóa linh hoạt hơn (không bị giới hạn trong phạm vi block).
* Hỗ trợ cơ chế công bằng (Fairness policy - FIFO) khi cấp khóa.
* Tích hợp tốt với **Condition variables** (thay thế cho `wait/notify`).



### Cấu trúc triển khai chuẩn

> **Quy tắc vàng:** Luôn gọi `lock()` ngay trước khối `try`, và gọi `unlock()` trong khối `finally` để đảm bảo giải phóng khóa ngay cả khi có ngoại lệ xảy ra.

```java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockExample {
    private final Lock lock = new ReentrantLock();
    private int counter = 0;

    public void increment() {
        lock.lock(); // Bắt đầu chiếm giữ khóa
        try {
            counter++;
            System.out.println(Thread.currentThread().getName() + " - Counter: " + counter);
        } finally {
            lock.unlock(); // Luôn giải phóng khóa trong finally
        }
    }
}

```

---

## 2. ReentrantReadWriteLock

### Khái niệm chính

`ReentrantReadWriteLock` duy trì một cặp khóa liên kết: **Read Lock** (Khóa đọc) và **Write Lock** (Khóa ghi).

* **Read Lock (Shared):** Nhiều luồng có thể cùng sở hữu khóa đọc đồng thời, miễn là không có luồng nào đang giữ khóa ghi.
* **Write Lock (Exclusive):** Chỉ duy nhất một luồng có thể nắm giữ khóa ghi tại một thời điểm. Khi khóa ghi được kích hoạt, tất cả các thao tác đọc và ghi khác đều phải chờ.
* **Trường hợp sử dụng:** Rất hiệu quả đối với các hệ thống có **tần suất đọc nhiều (Read-heavy)** và **tần suất ghi ít**.

### So sánh các chế độ khóa

| Thao tác | Read Lock đang giữ | Write Lock đang giữ |
| --- | --- | --- |
| **Thêm Read Lock** | **Cho phép** (Concurrent Read) | **Chờ** (Blocked) |
| **Thêm Write Lock** | **Chờ** (Blocked) | **Chờ** (Blocked) |

### Cấu trúc triển khai chuẩn

```java
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockExample {
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();

    private int data = 0;

    // Thao tác Ghi (Độc quyền)
    public void write(int value) {
        writeLock.lock();
        try {
            this.data = value;
            System.out.println(Thread.currentThread().getName() + " đã GHI value: " + value);
        } finally {
            writeLock.unlock();
        }
    }

    // Thao tác Đọc (Dùng chung)
    public void read() {
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " đã ĐỌC value: " + data);
        } finally {
            readLock.unlock();
        }
    }
}

```

---

## 3. Tổng kết các điểm quan trọng

1. **Bắt buộc sử dụng `try-finally`:** Thiếu `unlock()` trong khối `finally` sẽ dẫn đến nguy cơ **Deadlock** vĩnh viễn nếu đoạn code xử lý gặp lỗi.
2. **Khi nào dùng `ReentrantLock`?**
* Khi cần kiểm soát khóa nâng cao (tryLock với timeout, interruptible lock).
* Khi cần tính năng Fair Locking (đảm bảo thứ tự cho các thread chờ).


3. **Khi nào dùng `ReentrantReadWriteLock`?**
* Khi ứng dụng thực hiện các tác vụ đọc dữ liệu liên tục và hiếm khi cập nhật/ghi dữ liệu.



---