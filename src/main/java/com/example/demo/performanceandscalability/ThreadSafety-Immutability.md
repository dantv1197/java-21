

# Tóm Tắt Java Concurrency: Thread Safety & Immutability

Bài học tập trung vào hai khái niệm nền tảng trong lập trình đa luồng: Làm thế nào để đảm bảo dữ liệu dùng chung không bị sai lệch (**Thread Safety**) và tại sao việc sử dụng các đối tượng không thể thay đổi (**Immutable Objects**) lại là giải pháp tối ưu cho bài toán này.

---

## 1. Thread Safety (An toàn đa luồng)

### Khái niệm

**Thread Safety** là đặc tính của một chương trình đảm bảo dữ liệu dùng chung (shared data) khi được truy cập và chỉnh sửa bởi nhiều luồng đồng thời vẫn hoạt động chính xác, dự đoán được và không gây ra sai lệch dữ liệu.

### Hai thuật ngữ cốt lõi

* **Race Condition (Tranh chấp dữ liệu):** Xảy ra khi nhiều luồng cùng truy cập và chỉnh sửa tài nguyên dùng chung đồng thời, kết quả cuối cùng phụ thuộc vào **thời điểm/thứ tự chạy** của các luồng.
* **Critical Section (Đoạn mã găng):** Đoạn mã truy cập tài nguyên dùng chung mà tại một thời điểm **chỉ được phép cho duy nhất một luồng** thực thi.

### Các kỹ thuật đảm bảo Thread Safety

1. **Synchronization:** Dùng khối/phương thức `synchronized`.
2. **Explicit Locks:** Dùng `ReentrantLock` để kiểm soát linh hoạt.
3. **Volatile Variables:** Đảm bảo tính nhìn thấy (visibility) của biến giữa các bộ nhớ đệm luồng.
4. **Atomic Variables:** Sử dụng các lớp như `AtomicInteger` cho các thao tác nguyên tử (atomic).
5. **Immutability:** Tạo các đối tượng không thể sửa đổi sau khi khởi tạo.

### Mã nguồn minh họa: `ThreadSafeCounter.java`

```java
// Đảm bảo Thread Safety bằng từ khóa synchronized
public class ThreadSafeCounter {
    private int counter = 0;

    // Phương thức synchronized đảm bảo chỉ 1 luồng thực thi tại 1 thời điểm
    public synchronized void increment() {
        counter++;
    }

    public synchronized int getCounter() {
        return counter;
    }
}

```

---

## 2. Immutability (Tính bất biến)

### Khái niệm

**Immutability** là nguyên tắc thiết kế trong đó **trạng thái của đối tượng không thể bị thay đổi sau khi đã tạo xong**.

* Do không thể sửa đổi, đối tượng Immutable có tính **Thread-safe tự nhiên (inherently thread-safe)**.
* Có thể chia sẻ tự do giữa các luồng mà **không cần dùng cơ chế đồng bộ (Locks hay `synchronized`)**.

### Lợi ích của Immutability

* **An toàn đa luồng:** Loại bỏ hoàn toàn nguy cơ Race Condition.
* **Tính nhất quán:** Dữ liệu giữ nguyên trạng thái suốt vòng đời ứng dụng.
* **Dễ Debug:** Trạng thái cố định giúp việc theo dõi mã nguồn đơn giản hơn.
* **Hiệu năng cao:** Không mất chi phí overhead cho việc khóa (lock/unlock) khi đọc dữ liệu.

### Quy tắc tạo một lớp Immutable chuẩn trong Java

1. Khai báo lớp là **`final`** (ngăn chặn việc kế thừa làm thay đổi hành vi).
2. Khai báo tất cả các thuộc tính là **`private final`** (ngăn truy cập trực tiếp và chỉ gán giá trị 1 lần).
3. **Không cung cấp phương thức Setter** (không cho phép chỉnh sửa thuộc tính).
4. **Deep Copy đối với thuộc tính mutable:** Nếu đối tượng chứa biến tham chiếu đến một đối tượng có thể sửa đổi khác (như `Date`, `List`), phải trả về/gán bản sao độc lập.
5. Chỉ cung cấp phương thức Getter hoặc trả về đối tượng mới khi có thao tác cập nhật.

### Mã nguồn minh họa: `Person.java` (Immutable)

```java
// 1. Lớp được khai báo final
public final class Person {
    // 2. Thuộc tính private final
    private final String name;
    private final int age;

    // Khởi tạo tất cả thuộc tính qua Constructor
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // 3. Chỉ cung cấp Getter, không có Setter
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}

```

### Sử dụng đối tượng Immutable trong đa luồng

```java
public class ImmutabilityTest {
    public static void main(String[] args) {
        // Tạo 1 đối tượng Immutable duy nhất
        Person person = new Person("Alice", 30);

        Runnable task = () -> {
            // Nhiều luồng cùng đọc dữ liệu an toàn mà KHÔNG CẦN synchronized
            System.out.println(Thread.currentThread().getName() + 
                               " - Name: " + person.getName() + 
                               ", Age: " + person.getAge());
        };

        new Thread(task, "Thread-1").start();
        new Thread(task, "Thread-2").start();
        new Thread(task, "Thread-3").start();
    }
}

```

---

## 3. Rủi ro khi sử dụng Mutable Objects (Đối tượng thay đổi được)

Nếu đối tượng có thể thay đổi (`setter`) được chia sẻ giữa các luồng mà không đồng bộ hóa:

* **Race Conditions:** Nhiều luồng cập nhật đồng thời dẫn đến kết quả sai lệch.
* **Data Corruption:** Trạng thái đối tượng bị hỏng, có thể dẫn đến crash ứng dụng.
* **Phức tạp hóa mã nguồn:** Bắt buộc phải thêm các khối `synchronized` hoặc `ReentrantLock`, làm giảm hiệu năng hệ thống.

---

## 4. Bảng So Sánh

| Đặc tính | Thread-Safe via Synchronization | Thread-Safe via Immutability |
| --- | --- | --- |
| **Cách tiếp cận** | Kiểm soát quyền truy cập bằng Lock/Synchronization. | Thiết kế đối tượng không thể bị sửa đổi. |
| **Cơ chế** | Chờ lượt (Blocking/Waiting). | Chia sẻ tự do, không cần chờ (Non-blocking). |
| **Chi phí hiệu năng** | Có overhead do khóa và chuyển ngữ cảnh luồng. | Gần như bằng 0 khi đọc dữ liệu. |
| **Độ phức tạp code** | Cao (dễ gặp lỗi Deadlock/Race Condition nếu thiếu sót). | Thấp (code rõ ràng, an toàn ngay từ thiết kế). |

---