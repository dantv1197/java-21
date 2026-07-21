# Singleton Pattern - Ghi chú kiến trúc & Ứng dụng

## 1. Định nghĩa (Definition)

**Singleton Pattern** là một mẫu thiết kế khởi tạo (**Creational Design Pattern**). Mẫu thiết kế này đảm bảo rằng một lớp (class) **chỉ có duy nhất một thể hiện (instance)** trong suốt vòng đời của ứng dụng và cung cấp một **điểm truy cập toàn cục (global access point)** đến instance đó.

> **Triết lý cốt lõi:** Kiểm soát quyền khởi tạo đối tượng. Ngăn chặn việc khởi tạo tự do thông qua từ khóa `new` từ bên ngoài, đồng thời duy trì một instance duy nhất được dùng chung cho toàn bộ ứng dụng.

---

## 2. Cấu trúc của Singleton Pattern

Một lớp Singleton tiêu chuẩn gồm **3 thành phần bắt buộc**:

```
┌──────────────────────────────────────────────┐
│                  Singleton                   │
├──────────────────────────────────────────────┤
│ - instance: Singleton                        │
├──────────────────────────────────────────────┤
│ - Singleton()                                │
│ + getInstance(): Singleton                   │
│ + someBusinessLogic()                        │
└──────────────────────────────────────────────┘
```

1. **Private Constructor:** Ngăn không cho các lớp khác gọi `new Singleton()`.
2. **Private Static Variable:** Lưu giữ instance duy nhất của lớp đó.
3. **Public Static Method (`getInstance()`):** Hàm trả về instance duy nhất đó (có thể khởi tạo instance khi được gọi lần đầu - Lazy Loading).

---

## 3. Mã nguồn minh họa (Implementation - Java)

### Cách 1: Enum Singleton (Khuyên dùng trong Production - Best Practice)

Enum là cách tốt nhất để triển khai Singleton trong Java vì **Thread-safe tuyệt đối** và **chống lại tấn công qua Reflection API / Serialization**.



### Cách 2: Bill Pugh Singleton (Lazy Loading & Thread-Safe)

Dành cho các trường hợp không thể dùng Enum (ví dụ: cần kế thừa từ một Abstract Class khác). Cách này tận dụng cơ chế Class Loader của JVM giúp đảm bảo **Thread-safe** mà **không cần dùng khóa Synchronized** gây giảm hiệu năng.

```java
public class AppConfig {

    // 1. Private constructor ngăn chặn instantiation từ bên ngoài
    private AppConfig() {
        // Chống phá vỡ Singleton bằng Java Reflection API
        if (Holder.INSTANCE != null) {
            throw new RuntimeException("Instance đã tồn tại! Không thể khởi tạo thêm.");
        }
    }

    // 2. Inner static class - Chỉ được load vào RAM khi hàm getInstance() được gọi
    private static class Holder {
        private static final AppConfig INSTANCE = new AppConfig();
    }

    // 3. Public static method cung cấp điểm truy cập
    public static AppConfig getInstance() {
        return Holder.INSTANCE;
    }

    public void showConfig() {
        System.out.println("System Config: Environment = Production");
    }
}
```

---

## 4. Trường hợp sử dụng (Use Cases)

Nên áp dụng **Singleton Pattern** khi:

1. **Quản lý tài nguyên dùng chung (Shared Resources):** Khi nhiều thành phần trong hệ thống cần truy cập chung một tài nguyên đắt đỏ như Database Connection Pool, Thread Pool, Logger, File System Access.
2. **Quản lý Cấu hình (Configuration Management):** Khi ứng dụng cần đọc file cấu hình (`app.properties`, `settings.json`) 1 lần duy nhất và chia sẻ dữ liệu này cho toàn hệ thống.
3. **Caching (Bộ nhớ tạm):** Lưu trữ các dữ liệu ít thay đổi ở bộ nhớ trong (In-Memory Cache) dùng chung để tránh truy vấn DB liên tục.
4. **Quản lý Trạng thái phần cứng / Thiết bị:** Ví dụ: Lớp điều khiển máy in (Printer Spooler), điều khiển cổng Serial / USB.

---

## 5. Ưu điểm & Nhược điểm

### Ưu điểm (Pros)
* **Đảm bảo duy nhất 1 Instance:** Tiết kiệm RAM và tài nguyên hệ thống, tránh xung đột dữ liệu.
* **Global Access Point:** Dễ dàng truy cập instance ở bất kỳ đâu trong codebase.
* **Lazy Initialization:** Có thể hoãn việc khởi tạo đối tượng cho tới khi nó thực sự được cần đến (tiết kiệm tài nguyên lúc ứng dụng khởi động).

### Nhược điểm (Cons)
* **Khó Unit Test:** Do dùng trạng thái toàn cục (Global State) và constructor bị private, rất khó để mock dữ liệu khi viết Unit Test.
* **Vi phạm Single Responsibility Principle (SRP):** Lớp Singleton vừa phải tự quản lý vòng đời khởi tạo của chính nó, vừa đảm nhận nghiệp vụ chuyên môn.
* **Nguy cơ nghẽn cổ chai (Bottleneck):** Trong ứng dụng Đa luồng (Multi-threading), nếu nhiều thread cùng truy cập một Singleton không được thiết kế tối ưu, có thể gây giảm hiệu năng hệ thống.

---

## 6. So sánh các cách triển khai Singleton trong Java

| Phương pháp | Lazy Loading | Thread-Safe | Chống Reflection | Mức độ khuyến nghị |
| :--- | :---: | :---: | :---: | :--- |
| **Enum Singleton** | ❌ No | ✅ High | ✅ Có | ⭐️ **Ưu tiên số 1** |
| **Bill Pugh (Inner Static Class)** | ✅ Yes | ✅ High | ⚠️ Cần code phụ | ⭐️ **Ưu tiên số 2 (khi cần Lazy)** |
| **Double-Checked Locking** | ✅ Yes | ✅ High | ❌ Không | ⚠️ Phức tạp, dễ sót từ khóa `volatile` |
| **Eager Initialization** | ❌ No | ✅ High | ❌ Không | ⚠️ Tốn RAM nếu không dùng đến |
| **Lazy Basic (Non-Thread-Safe)** | ✅ Yes | ❌ Không | ❌ Không | ❌ Không dùng trong Production |