
# Tóm Tắt Java Concurrency: Thread-Safe Collections

**Thread-Safe Collections** là các cấu trúc dữ liệu được thiết kế sẵn trong Java nhằm giúp quản lý dữ liệu an toàn trong môi trường đa luồng mà **không cần viết các đoạn mã đồng bộ hóa thủ công** (như `synchronized` hay `Lock`).

---

## 1. Tổng quan các bộ sưu tập an toàn đa luồng

Trong gói `java.util.concurrent`, Java cung cấp nhiều lớp tập hợp tùy theo nhu cầu và cơ chế hoạt động riêng biệt:

* **Bảng ánh xạ / Tập hợp nâng cao:**
* `ConcurrentHashMap`: Hỗ trợ truy cập đồng thời cao, tối ưu cho các thao tác đọc/ghi thường xuyên.
* `ConcurrentSkipListMap` / `ConcurrentSkipListSet`: Bảng ánh xạ và tập hợp được sắp xếp theo thứ tự, an toàn đa luồng.


* **Danh sách / Hàng chờ không chặn (Non-blocking):**
* `CopyOnWriteArrayList`: Danh sách an toàn đa luồng phù hợp cho bài toán **Đọc nhiều - Ghi ít**.
* `ConcurrentLinkedQueue`: Hàng chờ FIFO không chặn (non-blocking) dựa trên liên kết chuỗi node.


* **Hàng chờ chặn (Blocking Queues):**
* `ArrayBlockingQueue`, `LinkedBlockingQueue`, `PriorityBlockingQueue`.



---

## 2. Chi tiết về `CopyOnWriteArrayList`

### Cơ chế hoạt động

* **Write Operation (Thao tác ghi):** Mỗi khi có thao tác thêm, sửa, hoặc xóa (`add`, `set`, `remove`), lớp này sẽ **tạo ra một bản sao mới (copy)** của mảng bên dưới để thực hiện thay đổi.
* **Read Operation (Thao tác đọc):** Các luồng thực hiện thao tác đọc dữ liệu trực tiếp trên mảng hiện tại mà không cần khóa (lock-free), giúp đạt hiệu năng rất cao.
* **Đặc điểm lưu ý:**
* **Ưu điểm:** Đảm bảo tính nhất quán dữ liệu mà không cần dùng `synchronized` hay `ReentrantLock`.
* **Nhược điểm:** Chi phí bộ nhớ và hiệu năng cao cho thao tác ghi (do phải nhân bản mảng). Vì vậy, chỉ áp dụng khi **tần suất đọc nhiều hơn rất nhiều so với tần suất ghi**.



---

## 3. Cấu trúc Triển khai Mã nguồn Ví dụ

Dưới đây là mô phỏng hệ thống quản lý danh sách đơn hàng (`orderList`) được cập nhật an toàn bởi nhiều luồng đồng thời:

```java
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ThreadSafeCollectionsExample {

    // Phương thức thêm đơn hàng vào danh sách dùng chung
    public static void addOrder(List<String> orderList, String order) {
        orderList.add(order);
        System.out.println(Thread.currentThread().getName() + " đã thêm: " + order);
    }

    public static void main(String[] args) throws InterruptedException {
        // Khởi tạo tập hợp an toàn đa luồng bằng CopyOnWriteArrayList
        List<String> orderList = new CopyOnWriteArrayList<>();

        // Tạo các luồng xử lý đồng thời
        Thread t1 = new Thread(() -> addOrder(orderList, "Order 1"), "Thread-1");
        Thread t2 = new Thread(() -> addOrder(orderList, "Order 2"), "Thread-2");

        // Bắt đầu chạy các luồng
        t1.start();
        t2.start();

        // Chờ các luồng hoàn thành trước khi tiếp tục
        t1.join();
        t2.join();

        // In ra danh sách kết quả cuối cùng
        System.out.println("Danh sách đơn hàng cuối cùng: " + orderList);
    }
}

```

---

## 4. Điểm quan trọng cần nhớ (Key Takeaways)

1. **Thay thế mã đồng bộ thủ công:** Sử dụng Thread-Safe Collections giúp code gọn gàng, giảm thiểu nguy cơ bế tắc (Deadlock) và lỗi tranh chấp dữ liệu (Race Conditions).
2. **Chọn đúng cấu trúc dữ liệu:**
* Dùng `CopyOnWriteArrayList` cho các danh sách cấu hình, danh sách người lắng nghe (listeners) ít thay đổi nhưng đọc liên tục.
* Dùng `ConcurrentHashMap` khi cần tra cứu dữ liệu theo dạng Key-Value trong môi trường đa luồng có mật độ truy cập lớn.


3. **Hiệu năng thao tác ghi:** Cần cẩn trọng khi dùng `CopyOnWriteArrayList` nếu số lượng phần tử lớn hoặc tần suất ghi diễn ra liên tục.

---