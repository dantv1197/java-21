# Tóm Tắt Java Concurrency: Optimizing Thread Pools

**Thread Pool** (Tập hợp luồng) là một tập hợp các luồng được khởi tạo trước và tái sử dụng để thực thi nhiều tác vụ (tasks). Thay vì liên tục tạo mới và hủy bỏ luồng cho mỗi tác vụ (gây tốn chi phí CPU và bộ nhớ), các tác vụ sẽ được đưa vào hàng chờ (queue) và chờ luồng rảnh trong pool xử lý.

---

## 1. Lợi ích & Các loại Thread Pool chính (`Executors`)

### Lợi ích cốt lõi

* **Giảm Overhead:** Tái sử dụng luồng sẵn có, tránh chi phí khởi tạo/hủy luồng liên tục.
* **Tăng tính Mở rộng (Scalability):** Giới hạn số lượng luồng hoạt động đồng thời, tránh cạn kiệt tài nguyên hệ thống.
* **Quản lý hàng chờ (Task Queuing):** Tự động đưa các tác vụ vượt quá khả năng xử lý vào hàng chờ.

### Các loại Thread Pool phổ biến từ `Executors`

| Loại Thread Pool | Đặc điểm & Cơ chế | Trường hợp sử dụng phù hợp |
| --- | --- | --- |
| **Fixed Thread Pool** | Khởi tạo số lượng luồng cố định (`nThreads`). Tác vụ dư thừa sẽ xếp hàng chờ. | Tải công việc ổn định, dự đoán được số lượng luồng (ví dụ: Web Server xử lý request). |
| **Cached Thread Pool** | Tạo luồng mới khi cần, tái sử dụng luồng rảnh. Tự hủy luồng rảnh sau 60s. | Tác vụ bất đồng bộ ngắn hạn, tải đột biến không dự đoán trước. *(Cần cẩn trọng tránh tràn bộ nhớ)*. |
| **Single Thread Executor** | Chỉ có 1 luồng duy nhất xử lý lần lượt các tác vụ theo thứ tự (Sequential/FIFO). | Tác vụ cần xử lý tuần tự để đảm bảo Thread-safe (ví dụ: Ghi log ra file). |
| **Scheduled Thread Pool** | Hỗ trợ lập lịch thực thi tác vụ sau một khoảng trì hoãn (delay) hoặc định kỳ. | Làm mới dữ liệu cache định kỳ, chạy tác vụ cron-job background. |
| **Work Stealing Pool** *(Java 8+)* | Dùng `ForkJoinPool` bên dưới, các luồng rảnh tự động "đánh cắp" tác vụ từ hàng chờ của luồng khác. | Xử lý song song dữ liệu lớn, các tác vụ có thể chia nhỏ (Divide and Conquer). |

---

## 2. Các Interface & Class nòng cốt

* **`Executor`:** Giao diện cơ sở, cung cấp phương thức `execute(Runnable)` để tách biệt việc nộp tác vụ với việc quản lý luồng.
* **`ExecutorService`:** Mở rộng từ `Executor`, hỗ trợ quản lý vòng đời pool (`shutdown()`, `shutdownNow()`) và nộp tác vụ trả về kết quả qua `submit()`.
* **`Callable<V>`:** Tương tự `Runnable`, nhưng phương thức `call()` có thể **trả về kết quả** và **ném ra Checked Exception**.
* **`Future<V>`:** Đại diện cho kết quả của tác vụ bất đồng bộ. Cung cấp `get()` (lấy kết quả - blocking), `isDone()` (kiểm tra trạng thái), và `cancel()` (hủy tác vụ).
* **`Executors`:** Lớp tiện ích (Factory Class) cung cấp các phương thức khởi tạo Thread Pool nhanh chóng.

---

## 3. Cấu trúc Triển khai Mã nguồn Ví dụ

Ví dụ tính bình phương các số sử dụng `FixedThreadPool`, `Callable`, và `Future`:

```java
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPoolExample {
    public static void main(String[] args) {
        // 1. Khởi tạo Fixed Thread Pool với 3 luồng
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // 2. Định nghĩa các tác vụ dạng Callable
        Callable<Integer> task1 = () -> 5 * 5;
        Callable<Integer> task2 = () -> 7 * 7;
        Callable<Integer> task3 = () -> 10 * 10;

        try {
            // 3. Nộp các tác vụ vào pool và nhận về đối tượng Future
            Future<Integer> result1 = executor.submit(task1);
            Future<Integer> result2 = executor.submit(task2);
            Future<Integer> result3 = executor.submit(task3);

            // 4. Lấy và in kết quả xử lý
            System.out.println("Result 1 (5^2): " + result1.get());
            System.out.println("Result 2 (7^2): " + result2.get());
            System.out.println("Result 3 (10^2): " + result3.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            // 5. Luôn đóng ExecutorService để giải phóng tài nguyên
            executor.shutdown();
        }
    }
}

```

---

## 4. Best Practices & Tối ưu hóa Thread Pool

1. **Chọn đúng loại Pool:**
* Khai báo số lượng luồng cố định cho công việc ổn định.
* Cẩn trọng với `CachedThreadPool` nếu số lượng tác vụ quá lớn để tránh làm cạn kiệt tài nguyên CPU/RAM.


2. **Tính toán kích thước Thread Pool hợp lý:**
* **CPU-bound tasks (Tính toán nặng):** Số luồng nên xấp xỉ số nhân CPU (`Runtime.getRuntime().availableProcessors()`).
* **I/O-bound tasks (Đọc/Ghi file, gọi API/DB):** Cần số luồng lớn hơn để bù đắp cho thời gian luồng bị chờ I/O.


3. **Quản lý Vòng đời (Shutdown):** Luôn gọi `shutdown()` hoặc `shutdownNow()` sau khi hoàn thành để dừng các luồng chờ, tránh tình trạng rò rỉ luồng (thread leak) làm chương trình không thể kết thúc.
4. **Cài đặt Timeout:** Khi gọi `future.get(timeout, unit)`, nên thiết lập thời gian chờ tối đa để tránh bị treo vô tận do tác vụ chạy quá lâu.