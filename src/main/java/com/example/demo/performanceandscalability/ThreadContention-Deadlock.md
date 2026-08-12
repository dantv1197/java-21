
# Tóm Tắt Java Concurrency: Thread Contention & Deadlocks

Khi lập trình đa luồng (multithreading), việc quản lý tài nguyên dùng chung là tối quan trọng. Hai vấn đề phổ biến nhất làm giảm hiệu năng hoặc treo hệ thống là **Thread Contention** và **Deadlock**.

---

## 1. Thread Contention (Tranh chấp luồng)

### Khái niệm

**Thread Contention** xảy ra khi nhiều luồng (threads) **cùng lúc cạnh tranh để truy cập vào một tài nguyên dùng chung** (như khóa `Lock`, biến dùng chung, hoặc tài nguyên I/O). Luồng nào không lấy được khóa sẽ phải chờ (waiting/blocked), gây ra hiện tượng giảm hiệu năng và tăng độ trễ (latency).

### Nguyên nhân chính

* Lạm dụng khối hoặc phương thức `synchronized`.
* Phạm vi khóa (Lock Scope) quá lớn, giữ khóa trong thời gian dài.
* Cạnh tranh trên các cấu trúc dữ liệu không tối ưu đa luồng (như `HashMap` thông thường) hoặc tài nguyên I/O.

### Cách giảm thiểu (Reduce Contention)

1. **Thu nhỏ phạm vi khóa (Minimize Lock Scope):** Chỉ khóa đúng đoạn mã quan trọng (Critical Section), giải phóng khóa ngay khi xử lý xong.
2. **Sử dụng cấu trúc dữ liệu Concurrent:** Thay thế các tập hợp thông thường bằng `ConcurrentHashMap`, `CopyOnWriteArrayList`,...
3. **Cơ chế Fair Lock:** Sử dụng `new ReentrantLock(true)` nếu cần đảm bảo các luồng chờ được cấp khóa theo thứ tự (FIFO), tránh tình trạng *Thread Starvation*.

### Cấu trúc Mã nguồn Minh họa Contention

```java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadContentionExample {
    private static final Lock lock = new ReentrantLock();
    private static int counter = 0;

    public static void main(String[] args) {
        Runnable task = () -> {
            lock.lock(); // Các luồng phải xếp hàng chờ ở đây
            try {
                counter++;
                System.out.println(Thread.currentThread().getName() + " - Counter: " + counter);
            } finally {
                lock.unlock(); // Giải phóng khóa cho luồng tiếp theo
            }
        };

        // Tạo 3 luồng cùng tranh chấp tài nguyên 'lock'
        new Thread(task, "Thread-1").start();
        new Thread(task, "Thread-2").start();
        new Thread(task, "Thread-3").start();
    }
}

```

---

## 2. Deadlock (Bế tắc)

### Khái niệm

**Deadlock** là hiện tượng hai hoặc nhiều luồng **bị treo vĩnh viễn** vì luồng này đang chờ khóa mà luồng kia đang nắm giữ và ngược lại (phụ thuộc vòng tròn - *Circular Dependency*).

### Kịch bản Deadlock điển hình

* **Thread 1:** Giữ `Lock A` $\rightarrow$ Thử lấy `Lock B`
* **Thread 2:** Giữ `Lock B` $\rightarrow$ Thử lấy `Lock A`
* **Kết quả:** Cả hai luồng cùng đứng chờ nhau vĩnh viễn, chương trình bị đóng băng (hang).

```
   [Thread 1]  --- Giữ Lock A --->  Thử lấy Lock B (Bị treo)
       |                                   ^
       v                                   |
Thử lấy Lock A (Bị treo) <--- Giữ Lock B ---  [Thread 2]

```

### Cấu trúc Mã nguồn Gây ra Deadlock

```java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockExample {
    private static final Lock lock1 = new ReentrantLock();
    private static final Lock lock2 = new ReentrantLock();

    public static void main(String[] args) {
        // Luồng 1: Lấy lock1 trước, rồi đến lock2
        Thread thread1 = new Thread(() -> {
            lock1.lock();
            System.out.println("Thread 1: Đã giữ lock 1, đang chờ lock 2...");
            try {
                Thread.sleep(50); // Giả lập thời gian xử lý
                lock2.lock(); // Bị chặn vì Thread 2 đang giữ lock2
                try {
                    System.out.println("Thread 1: Đã lấy được cả 2 lock.");
                } finally {
                    lock2.unlock();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock1.unlock();
            }
        });

        // Luồng 2: Lấy lock2 trước, rồi đến lock1 (Thứ tự ngược lại!)
        Thread thread2 = new Thread(() -> {
            lock2.lock();
            System.out.println("Thread 2: Đã giữ lock 2, đang chờ lock 1...");
            try {
                Thread.sleep(50);
                lock1.lock(); // Bị chặn vì Thread 1 đang giữ lock1
                try {
                    System.out.println("Thread 2: Đã lấy được cả 2 lock.");
                } finally {
                    lock1.unlock();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock2.unlock();
            }
        });

        thread1.start();
        thread2.start();
    }
}

```

---

## 3. Cách Phòng Chống Deadlock (Deadlock Avoidance)

1. **Tránh lồng khóa (Avoid Nested Locks):** Hạn chế việc yêu cầu thêm khóa mới khi đang nắm giữ một khóa khác.
2. **Thứ tự lấy khóa cố định (Lock Ordering):** Tất cả các luồng trong hệ thống **phải bắt buộc lấy khóa theo cùng một thứ tự** (Ví dụ: Luôn lấy `Lock 1` trước `Lock 2`).
3. **Sử dụng Timeout với `tryLock()`:** Dùng `lock.tryLock(timeout, timeUnit)` thay cho `lock.lock()`. Nếu quá thời gian chờ mà không lấy được khóa, luồng sẽ tự rút lui và giải phóng các khóa đang giữ để luồng khác xử lý.

---

## 4. Bảng So Sánh Tổng Quan

| Đặc điểm | Thread Contention | Deadlock |
| --- | --- | --- |
| **Trạng thái luồng** | Luồng vẫn xử lý xong, nhưng phải **xếp hàng chờ** nhau. | Luồng bị **treo vĩnh viễn** (Indefinitely stuck). |
| **Tác động** | Làm giảm hiệu năng, tăng latency hệ thống. | Làm ngừng hoạt động (freeze) một phần hoặc toàn bộ ứng dụng. |
| **Cách khắc phục** | Giảm Lock Scope, dùng `Concurrent Collections`. | Quy định Lock Ordering, dùng `tryLock()` có Timeout. |

---