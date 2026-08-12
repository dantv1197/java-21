# Tóm Tắt Java Concurrency: Parallel Stream

**Parallel Stream** (Luồng song song) là một tính năng được giới thiệu từ Java 8, cho phép tự động chia nhỏ một danh sách dữ liệu (Collection) thành nhiều phần nhỏ (chunks) để xử lý song song trên nhiều luồng (threads) khác nhau. Tính năng này giúp tối ưu hóa hiệu năng cho các tác vụ tốn nhiều CPU (**CPU-intensive**) nhờ tận dụng các bộ vi xử lý đa nhân modern (multi-core processors).

---

## 1. Cơ Chế Hoạt Động & Đặc Điểm Cốt Lõi

* **Song song hóa (Parallelism):** Chia tập dữ liệu thành nhiều khối nhỏ và xử lý chúng đồng thời trên nhiều luồng.
* **`ForkJoinPool.commonPool()`:** Mặc định, Parallel Stream sử dụng chung `ForkJoinPool` của JVM.
* Số lượng luồng tối đa trong pool mặc định được tính theo công thức:

$$\text{Default Pool Size} = \text{Số nhân CPU khả dụng} - 1$$




* **Stateless Operations (Thao tác không trạng thái):** Parallel Stream hoạt động tốt nhất và an toàn nhất với các hàm **stateless** (không phụ thuộc/không thay đổi trạng thái biến bên ngoài).
* **Thứ tự xử lý không cố định (Non-deterministic Order):** Do được xử lý trên nhiều luồng độc lập, thứ tự hoàn thành của các phần tử có thể khác hoàn toàn so với thứ tự ban đầu trong danh sách.

---

## 2. Khi Nào Nên Và Không Nên Dùng Parallel Stream?

### NÊN dùng khi:

* Dữ liệu kích thước lớn (**Large Data Sets**).
* Tác vụ tính toán nặng, đòi hỏi nhiều CPU (**Computational-heavy Tasks**).
* Các thao tác độc lập hoàn toàn, không sửa đổi dữ liệu dùng chung (**Stateless & Non-blocking**).

### KHÔNG NÊN dùng khi:

* Dữ liệu kích thước nhỏ (Chi phí tạo/quản lý luồng **Thread Overhead** sẽ lớn hơn thời gian xử lý thực tế, làm giảm hiệu năng so với Sequential Stream).
* Cần duy trì chính xác thứ tự xử lý của dữ liệu.
* Thao tác có chứa việc thay đổi biến dùng chung (**Shared Mutable State** - dễ gây lỗi Race Condition).
* Thực hiện các tác vụ I/O nặng (Đọc/ghi file, gọi Network API) vì có thể làm nghẽn `ForkJoinPool.commonPool()` dùng chung của toàn bộ ứng dụng.

---

## 3. Mã Nguồn Minh Họa

Đoạn mã dưới đây minh họa việc dùng Parallel Stream để tính bình phương danh sách các số và in ra tên luồng thực thi:

```java
import java.util.List;

public class ParallelStreamExample {
    public static void main(String[] args) {
        // Tạo danh sách các số từ 1 đến 10
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        System.out.println("--- Xử lý song song bằng Parallel Stream ---");
        
        // Chuyển sang Parallel Stream bằng phương thức .parallelStream()
        numbers.parallelStream().forEach(number -> {
            int square = number * number;
            // In ra tên luồng đang xử lý và kết quả tính toán
            System.out.println("Thread: " + Thread.currentThread().getName() 
                               + " | Số: " + number 
                               + " -> Bình phương: " + square);
        });
    }
}

```

### Kết quả đầu ra mẫu (Output):

*(Thứ tự kết quả sẽ thay đổi ngẫu nhiên giữa các lần chạy)*

```text
Thread: main | Số: 7 -> Bình phương: 49
Thread: ForkJoinPool.commonPool-worker-1 | Số: 2 -> Bình phương: 4
Thread: ForkJoinPool.commonPool-worker-2 | Số: 1 -> Bình phương: 1
Thread: ForkJoinPool.commonPool-worker-3 | Số: 4 -> Bình phương: 16
Thread: main | Số: 10 -> Bình phương: 100
...

```

---

## 4. Điểm Quan Trọng Cần Nhớ (Key Takeaways)

1. **Rủi ro cạn kiệt luồng (Thread Starvation):** Do Parallel Stream mặc định chia sẻ `ForkJoinPool.commonPool()` toàn cục, việc lạm dụng nó cho các tác vụ nghẽn I/O có thể làm đơ/chậm các phần khác trong ứng dụng.
2. **Nguyên tắc an toàn đa luồng:** Tránh thực hiện các thao tác side-effect (ví dụ: `list.add()` vào một `ArrayList` thông thường bên trong `.forEach()` của Parallel Stream).
3. **Đo lường hiệu năng (Benchmarking):** Không phải lúc nào đổi từ `.stream()` sang `.parallelStream()` cũng làm ứng dụng chạy nhanh hơn. Cần benchmark kỹ lưỡng với dữ liệu thực tế trước khi áp dụng.

---