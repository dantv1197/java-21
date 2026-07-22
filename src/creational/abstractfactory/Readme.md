# Abstract Factory Pattern - Ghi chú Kiến trúc & Hướng dẫn Sử dụng

## 1. Định nghĩa (Definition)

**Abstract Factory Pattern** là một mẫu thiết kế khởi tạo (**Creational Design Pattern**). Mẫu thiết kế này cho phép bạn **tạo ra các họ sản phẩm có liên quan đến nhau** (families of related or dependent objects) mà không cần chỉ định rõ các lớp cụ thể của chúng.

> **Triết lý cốt lõi:** Đảm bảo rằng các đối tượng được tạo ra phải tương thích và hoạt động đồng bộ với nhau (ví dụ: các thành phần giao diện của Windows phải đi cùng nhau, không được lẫn lộn với Mac). Nó nâng tầm Factory Method lên một cấp độ cao hơn bằng cách quản lý nhiều factory con bên trong một Factory lớn.

---

## 2. Cấu trúc của Abstract Factory Pattern

Mô hình Abstract Factory chuẩn gồm các thành phần chính sau:

```
┌────────────────────────────────────────────────────────┐
│               Abstract Factory (Interface)             │
│  + createProductA(): AbstractProductA                  │
│  + createProductB(): AbstractProductB                  │
└──────────────────────────▲─────────────────────────────┘
                           │
         ┌─────────────────┴─────────────────┐
         │                                   │
┌────────┴─────────────┐          ┌──────────┴───────────┐
│ ConcreteFactory1     │          │ ConcreteFactory2     │
├──────────────────────┤          ├──────────────────────┤
│ + createProductA()   │          │ + createProductA()   │
│ + createProductB()   │          │ + createProductB()   │
└──────────────────────┘          └──────────────────────┘
```

1. **Abstract Factory (Giao diện trừu tượng):** Khai báo tập hợp các phương thức để tạo ra các sản phẩm khác nhau thuộc một họ sản phẩm.
2. **Concrete Factory (Factory cụ thể):** Cài đặt các phương thức khởi tạo của Abstract Factory để tạo ra một họ sản phẩm cụ thể tương ứng (ví dụ: họ sản phẩm Windows hoặc họ sản phẩm Mac).
3. **Abstract Product (Giao diện sản phẩm trừu tượng):** Khai báo interface cho một loại sản phẩm.
4. **Concrete Product (Sản phẩm cụ thể):** Các cài đặt thực tế của sản phẩm, được phân chia theo từng họ tương ứng với Concrete Factory.
5. **Client:** Chỉ sử dụng các giao diện của Abstract Factory và Abstract Product, hoàn toàn độc lập với các lớp cụ thể.

---

## 3. Mã nguồn minh họa (Implementation - Java)

### Kịch bản thực tế
Xây dựng hệ thống giao diện ứng dụng đa nền tảng (**Cross-Platform UI**). Ứng dụng cần tạo các thành phần giao diện (`Button`, `Checkbox`) đồng bộ theo hệ điều hành đang chạy (**Windows** hoặc **Mac**).

#### Bước 1: Khai báo Abstract Products
```java
public interface Button {
    void render();
}

public interface Checkbox {
    void render();
}
```

#### Bước 2: Tạo Concrete Products cho từng họ
```java
// Họ sản phẩm cho Windows
public class WindowsButton implements Button {
    @Override
    public void render() {
        System.out.println("[Windows] Vẽ nút bấm vuông vức.");
    }
}

public class WindowsCheckbox implements Checkbox {
    @Override
    public void render() {
        System.out.println("[Windows] Vẽ ô tích chọn Checkbox vuông.");
    }
}

// Họ sản phẩm cho Mac
public class MacButton implements Button {
    @Override
    public void render() {
        System.out.println("[Mac] Vẽ nút bấm bo tròn viền mờ.");
    }
}

public class MacCheckbox implements Checkbox {
    @Override
    public void render() {
        System.out.println("[Mac] Vẽ ô tích chọn Checkbox bo tròn.");
    }
}
```

#### Bước 3: Khai báo Abstract Factory
```java
public interface GUIFactory {
    Button createButton();
    Checkbox createCheckbox();
}
```

#### Bước 4: Tạo các Concrete Factories
```java
public class WindowsFactory implements GUIFactory {
    @Override
    public Button createButton() { return new WindowsButton(); }
    @Override
    public Checkbox createCheckbox() { return new WindowsCheckbox(); }
}

public class MacFactory implements GUIFactory {
    @Override
    public Button createButton() { return new MacButton(); }
    @Override
    public Checkbox createCheckbox() { return new MacCheckbox(); }
}
```

#### Bước 5: Client sử dụng
```java
public class Application {
    private Button button;
    private Checkbox checkbox;

    // Client hoàn toàn độc lập, chỉ làm việc với Interface
    public Application(GUIFactory factory) {
        this.button = factory.createButton();
        this.checkbox = factory.createCheckbox();
    }

    public void paint() {
        button.render();
        checkbox.render();
    }
}

// Main class
public class Main {
    public static void main(String[] args) {
        GUIFactory factory;
        String os = "WINDOWS"; // Giả lập cấu hình hệ thống

        if ("WINDOWS".equalsIgnoreCase(os)) {
            factory = new WindowsFactory();
        } else {
            factory = new MacFactory();
        }

        Application app = new Application(factory);
        app.paint();
    }
}
```

---

## 4. Trường hợp sử dụng (Use Cases)

Nên áp dụng **Abstract Factory Pattern** khi:

1. **Hệ thống cần độc lập với cách tạo và cấu trúc sản phẩm:** Khi ứng dụng cần làm việc với nhiều họ sản phẩm khác nhau nhưng không muốn bị dính chặt (`tight coupling`) vào các class cụ thể.
2. **Đảm bảo tính đồng bộ (Consistency):** Khi các đối tượng trong cùng một họ được thiết kế để đi kèm và hoạt động chung với nhau, tránh việc vô tình trộn lẫn các linh kiện không tương thích.
3. **Mở rộng họ sản phẩm dễ dàng:** Khi hệ thống có khả năng cao sẽ bổ sung thêm các họ sản phẩm hoàn toàn mới (ví dụ: thêm giao diện `LinuxFactory`).

---

## 5. So sánh: Factory Method vs. Abstract Factory

| Tiêu chí | Factory Method | Abstract Factory |
| :--- | :--- | :--- |
| **Quy mô** | Tạo ra **một sản phẩm đơn lẻ** qua 1 phương thức ảo. | Tạo ra **cả một họ các sản phẩm liên quan** qua nhiều phương thức. |
| **Cơ chế** | Thường dựa trên **Kế thừa** (Subclasses override phương thức). | Thường dựa trên **Tổng hợp / Ủy quyền** (Composition / Delegation). |
| **Độ phức tạp** | Thấp đến trung bình. | Cao hơn do quản lý nhiều tầng interface và factory con. |

---

## 6. Ưu điểm & Nhược điểm

### Ưu điểm (Pros)
* **Đảm bảo tính tương thích:** Các sản phẩm được tạo ra từ một Factory chắc chắn khớp và hoạt động tốt với nhau.
* **Tránh sự phụ thuộc cứng nhắc:** Tách rời mã nguồn client khỏi các lớp cài đặt chi tiết cụ thể.
* **Open/Closed Principle (OCP):** Dễ dàng thêm họ sản phẩm mới bằng cách viết thêm Concrete Factory mới mà không sửa code cũ.

### Nhược điểm (Cons)
* **Khó mở rộng loại sản phẩm mới:** Nếu bạn muốn thêm một loại sản phẩm mới vào giao diện `GUIFactory` (ví dụ thêm `TextField`), bạn sẽ phải **sửa đổi toàn bộ** Abstract Factory interface và tất cả các Concrete Factory con liên quan.
* **Mã nguồn trở nên phức tạp:** Số lượng Interface và Class tăng lên đáng kể trong dự án.