# Builder Pattern - Ghi chú Kiến trúc & Hướng dẫn Sử dụng

## 1. Định nghĩa (Definition)

**Builder Pattern** là một mẫu thiết kế khởi tạo (**Creational Design Pattern**). Mẫu thiết kế này cho phép bạn **xây dựng các đối tượng phức tạp từng bước một** (step-by-step). Nó giúp tách biệt mã nguồn dựng đối tượng (construction) ra khỏi mã nguồn biểu diễn đối tượng (representation), cho phép cùng một quá trình dựng có thể tạo ra các biểu diễn khác nhau.

> **Triết lý cốt lõi:** Thay vì bắt buộc truyền toàn bộ tham số vào một Constructor khổng lồ, Builder chia quá trình khởi tạo thành các phương thức nhỏ, rõ ràng. Người dùng chỉ cần gọi những phương thức thiết lập thuộc tính mà họ thực sự cần, sau đó hoàn tất bằng lệnh `build()`.

---

## 2. Cấu trúc của Builder Pattern

Một mô hình Builder Pattern chuẩn thường bao gồm các thành phần sau:

```
┌───────────────────────────────┐
│            Product            │
└───────────────▲───────────────┘
                │ (creates)
┌───────────────┴───────────────┐        ┌───────────────────────────────┐
│        Builder (Inner)        │───────►│           Director            │
├───────────────────────────────┤        │ (Tùy chọn - Quản lý quy trình)│
│ + setPartA()                  │        └───────────────────────────────┘
│ + setPartB()                  │
│ + build(): Product            │
└───────────────────────────────┘
```

1. **Product (Sản phẩm):** Đối tượng phức tạp cần được khởi tạo (thường chứa nhiều thuộc tính, có thể có các thuộc tính bắt buộc và tùy chọn).
2. **Builder Class / Static Inner Class:** Lớp chịu trách nhiệm lưu trữ tạm thời các thuộc tính và cung cấp các hàm thiết lập (`step methods`) dạng chuỗi (Fluent API).
3. **Director (Đạo diễn - Tùy chọn):** Lớp định nghĩa các quy trình dựng mẫu sẵn (ví dụ: `buildFullFeaturedProduct()`, `buildMinimalProduct()`).

---

## 3. Mã nguồn minh họa (Implementation - Java)

### Kịch bản thực tế
Khởi tạo đối tượng **Pizza** trong ứng dụng đặt đồ ăn. 
* **Bắt buộc:** `size` (kích thước), `crust` (đế bánh).
* **Tùy chọn:** `cheese` (phô mai), `pepperoni` (xúc xích), `mushroom` (nấm), `pineapple` (dứa).

#### Triển khai chuẩn bằng Static Inner Class

```java

```

#### Client sử dụng


---

## 4. Trường hợp sử dụng (Use Cases)

Nên áp dụng **Builder Pattern** trong các tình huống:

1. **Giải quyết vấn đề "Telescoping Constructor":** Khi một Class có quá nhiều Constructor quá tải (Overloaded Constructors) với vô số tham số, dễ gây nhầm lẫn thứ tự truyền dữ liệu.
2. **Khởi tạo đối tượng có thuộc tính Tùy chọn (Optional):** Khi đối tượng có hàng chục trường dữ liệu nhưng mỗi trường hợp sử dụng chỉ cần thiết lập một vài trường trong số đó.
3. **Cần tạo đối tượng Bất biến (Immutable Objects):** Khi bạn muốn các thuộc tính của đối tượng là `final` (không thể thay đổi sau khi tạo) mà không muốn dùng hàm `setter`.
4. **Quy trình dựng đối tượng phức tạp:** Khi việc tạo đối tượng đòi hỏi nhiều bước tính toán, validate kiểm tra dữ liệu đầu vào trước khi chính thức tạo instance.

---

## 5. So sánh với các Pattern khởi tạo khác

| Tiêu chí | Builder Pattern | Factory Method | Prototype Pattern |
| :--- | :--- | :--- | :--- |
| **Mục đích** | Dựng đối tượng **phức tạp theo từng bước**. | Tạo đối tượng **thuộc một họ** dựa trên giao diện chung. | **Sao chép (clone)** một đối tượng mẫu có sẵn. |
| **Trọng tâm** | Tập trung vào **chi tiết cấu tạo từng phần** của 1 đối tượng. | Tập trung vào **quyết định lớp cụ thể** nào sẽ được tạo. | Tập trung vào **hiệu năng khởi tạo** bằng cách nhân bản. |
| **Đầu ra** | Trả về một đối tượng tùy chỉnh theo các bước lặp. | Trả về instance của một trong các Subclass. | Trả về một bản sao độc lập của đối tượng gốc. |

---

## 6. Ưu điểm & Nhược điểm

### Ưu điểm (Pros)
* **Code rõ ràng, dễ đọc (Fluent Interface):** Tên các hàm giải thích chính xác thuộc tính đang được gán.
* **Đảm bảo tính Bất biến (Immutability):** An toàn trong môi trường Đa luồng (Thread-safe) do đối tượng không có Setter.
* **Kiểm soát quy trình khởi tạo:** Cho phép hoãn bước dựng đối tượng hoặc chạy các hàm kiểm tra tính hợp lệ dữ liệu (Validation) trước khi gọi `.build()`.

### Nhược điểm (Cons)
* **Tăng độ phức tạp mã nguồn:** Phải tạo thêm Lớp Builder phụ trợ.
* **Tốn bộ nhớ phụ:** Tạo ra đối tượng Builder trung gian trước khi thu được đối tượng chính.