
# Tóm Tắt Java Concurrency: Blocking Queues

**BlockingQueue** là một giao diện (interface) đặc biệt thuộc gói `java.util.concurrent`, chuyên dùng để quản lý hàng chờ an toàn đa luồng. Điểm khác biệt cốt lõi là cơ chế **tự động chặn (blocking)** luồng mà không cần tự viết mã điều khiển bằng `Lock` hay `Condition`.

---

## 1. Khái niệm & Cơ chế Chặn (Blocking Behavior)

* **Khi Hàng chờ ĐẦY (Full):** Tác vụ thêm phần tử (`put()`) sẽ **tự động tạm dừng (block)** luồng Producer cho đến khi có chỗ trống trong hàng chờ.
* **Khi Hàng chờ RỖNG (Empty):** Tác vụ lấy phần tử (`take()`) sẽ **tự động tạm dừng (block)** luồng Consumer cho đến khi có phần tử mới được thêm vào.
* **An toàn đa luồng (Thread-Safety):** Tự động xử lý tranh chấp dữ liệu mà không cần đến các khối `synchronized` hay `ReentrantLock` thủ công.

---

## 2. Các phương thức cốt lõi (`put` và `take`)

Khác với các phương thức thông thường như `add()`, `offer()`, `poll()`, hay `remove()`:

* **`put(E e)`:** Thêm một phần tử vào cuối hàng chờ; chờ nếu hàng chờ đã đầy.
* **`take()`:** Lấy và xóa phần tử ở đầu hàng chờ; chờ nếu hàng chờ đang rỗng.

*(Lưu ý: Cả 2 phương thức đều có thể ném ra `InterruptedException` nếu luồng bị ngắt trong lúc đang chờ).*

---

## 3. Lớp triển khai phổ biến: `LinkedBlockingQueue`

`LinkedBlockingQueue` là lớp triển khai dựa trên cấu trúc dữ liệu danh sách liên kết (linked list), cho phép giới hạn sức chứa tối đa (capacity).

---

## 4. Cấu trúc Triển khai Mã nguồn Ví dụ

Dưới đây là mô hình **Producer - Consumer** sử dụng `LinkedBlockingQueue` với dung lượng tối đa là 3:

```java
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueExample {

    // Thao tác của Producer (Sản xuất)
    public static void produce(BlockingQueue<Integer> queue) {
        try {
            for (int i = 1; i <= 5; i++) {
                // put() sẽ tự động CHẶN nếu hàng chờ đã đầy (max 3)
                queue.put(i);
                System.out.println("Producer đã sản xuất: " + i);
                Thread.sleep(500); // Giả lập thời gian xử lý
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Thao tác của Consumer (Tiêu thụ)
    public static void consume(BlockingQueue<Integer> queue) {
        try {
            for (int i = 1; i <= 5; i++) {
                // take() sẽ tự động CHẶN nếu hàng chờ đang rỗng
                int number = queue.take();
                System.out.println("Consumer đã lấy ra: " + number);
                Thread.sleep(500); // Giả lập thời gian xử lý
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Khởi tạo BlockingQueue với sức chứa tối đa là 3 phần tử
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(3);

        // Tạo luồng Producer và Consumer
        Thread producerThread = new Thread(() -> produce(queue), "Producer");
        Thread consumerThread = new Thread(() -> consume(queue), "Consumer");

        // Bắt đầu chạy các luồng
        producerThread.start();
        consumerThread.start();

        // Chờ các luồng hoàn tất
        producerThread.join();
        consumerThread.join();
    }
}

```

---

## 5. Điểm quan trọng cần nhớ (Key Takeaways)

1. **Ứng dụng thực tế:** Được ứng dụng phổ biến trong mô hình **Producer - Consumer**, hệ thống xử lý tác vụ bất đồng bộ (Thread Pool / Task Scheduling), và luân chuyển dữ liệu giữa các luồng.
2. **Code đơn giản, an toàn:** Không cần viết các câu lệnh `await()`, `signal()`, `wait()`, hay `notify()`. `BlockingQueue` tự quản lý điều kiện chờ và đánh thức luồng.
3. **Các lớp BlockingQueue phổ biến khác:** `ArrayBlockingQueue` (sử dụng mảng cố định), `PriorityBlockingQueue` (sắp xếp theo độ ưu tiên), `SynchronousQueue` (hàng chờ dung lượng bằng 0, chuyển giao trực tiếp).

---