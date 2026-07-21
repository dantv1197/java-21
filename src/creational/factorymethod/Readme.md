# Factory Method Pattern - Ghi chú kiến trúc & Ứng dụng

## 1. Định nghĩa (Definition)

**Factory Method Pattern** là một mẫu thiết kế khởi tạo (**Creational Design Pattern**). Mẫu thiết kế này định nghĩa một giao diện (interface hoặc abstract class) cho việc khởi tạo đối tượng, nhưng để các lớp con (**subclasses**) quyết định chính xác lớp nào sẽ được khởi tạo. 

> **Triết lý cốt lõi:** Tách rời (decouple) mã nguồn thực thi nghiệp vụ khỏi mã nguồn tạo đối tượng. Thay vì gọi trực tiếp từ khóa `new` tại lớp client, việc khởi tạo sẽ được ủy quyền cho các factory chuyên biệt.

---

## 2. Cấu trúc của Factory Method Pattern

Một mô hình Factory Method chuẩn bao gồm **4 thành phần chính**:

```
 ┌──────────────────────┐                     ┌──────────────────────┐
 │   Product (Interface)│                     │  Creator (Abstract)  │
 └──────────▲───────────┘                     └──────────▲───────────┘
            │                                            │
  ┌─────────┴─────────┐                        ┌─────────┴─────────┐
  │                   │                        │                   │
┌─┴─────────────────┐ ┌┴──────────────────┐  ┌─┴─────────────────┐ ┌┴──────────────────┐
│ ConcreteProductA  │ │ ConcreteProductB  │  │ ConcreteCreatorA  │ │ ConcreteCreatorB  │
└───────────────────┘ └───────────────────┘  └───────────────────┘ └───────────────────┘
```

1. **Product (Interface / Abstract Class):** Định nghĩa giao diện chung cho tất cả các đối tượng được tạo ra.
2. **Concrete Product:** Các lớp cài đặt cụ thể của `Product`.
3. **Creator (Abstract Class):** Khai báo **Factory Method** (`createProduct()`). Hàm này trả về một đối tượng thuộc kiểu `Product`. Lớp Creator cũng có thể chứa các hàm xử lý nghiệp vụ chung sử dụng sản phẩm được tạo ra.
4. **Concrete Creator:** Lớp con ghi đè (**override**) lại Factory Method để trả về một instance của `ConcreteProduct` tương ứng.

---

## 3. Mã nguồn minh họa (Implementation - Java)

### Kịch bản
Hệ thống xử lý thông báo (**Notification System**) hỗ trợ gửi qua **Email** và **SMS**. Sau này hệ thống có thể mở rộng sang **Zalo**, **Telegram** mà không làm thay đổi logic chính.

#### Bước 1: Khai báo Product Interface
```java
public interface Notification {
    void send(String message);
}
```

#### Bước 2: Tạo các Concrete Product
```java
public class EmailNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("[EMAIL] Sending email: " + message);
    }
}

public class SMSNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("[SMS] Sending SMS: " + message);
    }
}
```

#### Bước 3: Tạo Abstract Creator (Chứa Factory Method)
```java
public abstract class NotificationFactory {

    // Factory Method - Các lớp con bắt buộc phải override
    public abstract Notification createNotification();

    // Core business logic sử dụng product được tạo
    public void processAndSend(String message) {
        Notification notification = createNotification();
        // Các logic phụ trợ như log, audit...
        System.out.println("[LOG] Preparing to send message...");
        notification.send(message);
    }
}
```

#### Bước 4: Tạo các Concrete Creator
```java
public class EmailNotificationFactory extends NotificationFactory {
    @Override
    public Notification createNotification() {
        return new EmailNotification();
    }
}

public class SMSNotificationFactory extends NotificationFactory {
    @Override
    public Notification createNotification() {
        return new SMSNotification();
    }
}
```

#### Bước 5: Sử dụng tại Client
```java
public class Application {
    public static void main(String[] args) {
        // Tùy theo cấu hình runtime hoặc logic đầu vào để chọn Factory
        NotificationFactory factory;

        String config = "EMAIL"; // Có thể lấy từ config file/DB

        if ("EMAIL".equalsIgnoreCase(config)) {
            factory = new EmailNotificationFactory();
        } else {
            factory = new SMSNotificationFactory();
        }

        // Khách hàng sử dụng dịch vụ không quan tâm class cụ thể là gì
        factory.processAndSend("Mã xác thực OTP của bạn là 888888");
    }
}
```

---

## 4. Trường hợp sử dụng (Use Cases)

Nên cân nhắc áp dụng **Factory Method Pattern** trong các tình huống sau:

1. **Không biết trước các kiểu đối tượng cụ thể:** Khi ứng dụng chưa thể xác định chính xác các lớp đối tượng cần làm việc trước khi chạy (runtime).
2. **Muốn mở rộng thư viện / Framework:** Cho phép người dùng mở rộng các thành phần nội bộ của thư viện bằng cách kế thừa và ghi đè Factory Method.
3. **Quản lý vòng đời và tái sử dụng đối tượng:** Khi việc khởi tạo đối tượng tốn kém tài nguyên (Resource-heavy) và bạn muốn tích hợp caching/pooling vào quá trình khởi tạo mà không làm phồng mã nguồn của client.
4. **Đảm bảo nguyên tắc Open/Closed (SOLID):** Khi bạn muốn dễ dàng thêm các chủng loại sản phẩm mới (ví dụ: thêm `ZaloNotificationFactory`) mà **không cần sửa đổi** các đoạn code đã chạy ổn định trước đó.

---

## 5. Ưu điểm & Nhược điểm

### Ưu điểm (Pros)
* **Tách biệt phụ thuộc (Decoupling):** Tránh sự gắn kết chặt chẽ (**Tight Coupling**) giữa Creator và Concrete Products.
* **Single Responsibility Principle (SRP):** Gom mã nguồn khởi tạo đối tượng về một nơi duy nhất, giúp code dễ đọc và dễ bảo trì.
* **Open/Closed Principle (OCP):** Dễ dàng thêm các Concrete Product và Creator mới vào hệ thống mà không làm ảnh hưởng đến mã nguồn hiện tại.

### Nhược điểm (Cons)
* **Số lượng Class tăng nhanh:** Với mỗi sản phẩm mới, bạn phải tạo thêm ít nhất 2 lớp mới (1 `ConcreteProduct` và 1 `ConcreteCreator`), khiến cấu trúc thư mục dự án trở nên phức tạp nếu bài toán quá nhỏ.

---

## 6. So sánh nhanh

| Tiêu chí | Simple Factory | Factory Method |
| :--- | :--- | :--- |
| **Bản chất** | Là một idiom/hàm đơn giản, dùng `switch-case` hoặc `if-else` | Là một Design Pattern hoàn chỉnh dựa trên tính đa hình (Polymorphism) |
| **Khả năng mở rộng** | Phải sửa hàm factory cũ khi có sản phẩm mới (Vi phạm OCP) | Tạo Factory class con mới mà không sửa class cũ (Đạt OCP) |
| **Độ phức tạp** | Thấp | Trung bình |