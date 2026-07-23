# Visitor Design Pattern (Mô hình Tham quan)

> **Nhóm Pattern:** Behavioral (Hành vi)  
> **Tên gọi khác:** Double Dispatch

---

## 1. Tổng quan & Khái niệm

**Visitor Pattern** cho phép bạn **thêm các thao tác/tính năng mới vào một cấu trúc đối tượng có sẵn mà không cần sửa đổi mã nguồn của các lớp đối tượng đó**.

### Ý tưởng cốt lõi
Tách rời **thuật toán/thao tác (Algorithm)** ra khỏi **cấu trúc dữ liệu (Data Structure)**. Thay vì viết các phương thức tính toán trực tiếp bên trong các lớp dữ liệu, ta định nghĩa một lớp `Visitor` riêng chứa các phương thức xử lý tương ứng cho từng lớp dữ liệu.

---

## 2. Cấu trúc Mô hình (Class Diagram)

```
┌─────────────────────────┐               ┌─────────────────────────┐
│      <<interface>>      │               │      <<interface>>      │
│       ItemElement       │               │   ShoppingCartVisitor   │
├─────────────────────────┤               ├─────────────────────────┤
│ + accept(v: Visitor)    │               │ + visitBook(book)       │
└─────────────────────────┘               │ + visitFruit(fruit)     │
             ▲                            └─────────────────────────┘
             │ implements                              ▲
 ┌───────────┴───────────┐                             │ implements
 │                       │               ┌─────────────┴─────────────┐
┌┴────────────┐  ┌───────┴────┐   ┌──────┴─────────────┐   ┌─────────┴──────────┐
│    Book     │  │   Fruit    │   │ PriceCalculatorVis │   │  TaxCalculatorVis  │
└─────────────┘  └────────────┘   └────────────────────┘   └────────────────────┘
```

### Các thành phần chính

| Thành phần | Vai trò |
| :--- | :--- |
| **Element (Interface)** | Khai báo phương thức `accept(visitor)`, cho phép Visitor truy cập vào đối tượng. |
| **ConcreteElement** | Thực thi `accept()`, gọi lại phương thức `visit()` tương ứng dành cho nó trên đối tượng Visitor (**Double Dispatch**). |
| **Visitor (Interface)** | Khai báo các phương thức `visit(...)` cho từng lớp `ConcreteElement` trong hệ thống. |
| **ConcreteVisitor** | Cài đặt thuật toán/thao tác cụ thể cho từng lớp `ConcreteElement`. |

---

## 3. Minh họa Code mẫu hoàn chỉnh (Kotlin)

Ví dụ hệ thống **Giỏ hàng siêu thị**: Có các mặt hàng như **Sách (Book)** và **Hoa quả (Fruit)**. Cần thực hiện các thao tác riêng biệt như **Tính tổng tiền** và **Tính thuế VAT**.

### Bước 1: Khai báo Element Interface & Concrete Elements

```kotlin
// Interface cho các món hàng
interface ItemElement {
    fun accept(visitor: ShoppingCartVisitor): Double
}

// Lớp Sách
class Book(
    val title: String,
    val price: Double,
    val isbnNumber: String
) : ItemElement {
    override fun accept(visitor: ShoppingCartVisitor): Double {
        // Double Dispatch: Gọi hàm visit tương ứng với kiểu Book
        return visitor.visitBook(this)
    }
}

// Lớp Hoa quả
class Fruit(
    val name: String,
    val pricePerKg: Double,
    val weightKg: Double
) : ItemElement {
    override fun accept(visitor: ShoppingCartVisitor): Double {
        // Double Dispatch: Gọi hàm visit tương ứng với kiểu Fruit
        return visitor.visitFruit(this)
    }
}
```

### Bước 2: Khai báo Visitor Interface

```kotlin
interface ShoppingCartVisitor {
    fun visitBook(book: Book): Double
    fun visitFruit(fruit: Fruit): Double
}
```

### Bước 3: Triển khai các Concrete Visitors

#### A. Visitor tính tiền giỏ hàng (Áp dụng khuyến mãi)

```kotlin
class PriceCalculatorVisitor : ShoppingCartVisitor {

    override fun visitBook(book: Book): Double {
        var cost = book.price
        // Khuyến mãi: Sách trên 100$ được giảm 10$
        if (cost > 100) {
            cost -= 10
        }
        println("📚 Sách [${book.title}] - ISBN: ${book.isbnNumber} | Giá: $cost$")
        return cost
    }

    override fun visitFruit(fruit: Fruit): Double {
        val cost = fruit.pricePerKg * fruit.weightKg
        println("🍎 Hoa quả [${fruit.name}] - ${fruit.weightKg}kg | Giá: $cost$")
        return cost
    }
}
```

#### B. Visitor tính thuế VAT

```kotlin
class TaxCalculatorVisitor : ShoppingCartVisitor {

    override fun visitBook(book: Book): Double {
        // Sách chịu thuế 5%
        val tax = book.price * 0.05
        println("🧾 Thuế sách [${book.title}]: $tax$")
        return tax
    }

    override fun visitFruit(fruit: Fruit): Double {
        // Hoa quả/Nông sản chịu thuế 0%
        val tax = 0.0
        println("🧾 Thuế hoa quả [${fruit.name}]: $tax$")
        return tax
    }
}
```

### Bước 4: Client (Hàm Main)

```kotlin
fun main() {
    val items = listOf<ItemElement>(
        Book("Clean Code", 120.0, "12345"),
        Book("Design Patterns", 80.0, "67890"),
        Fruit("Táo Envy", 5.0, 2.5),
        Fruit("Chuối Laba", 2.0, 3.0)
    )

    println("=== 1. TÍNH TỔNG TIỀN ĐƠN HÀNG ===")
    val priceVisitor = PriceCalculatorVisitor()
    var totalPrice = 0.0
    for (item in items) {
        totalPrice += item.accept(priceVisitor)
    }
    println("➡️ TỔNG TIỀN PHẢI THANH TOÁN: $totalPrice$\n")

    println("=== 2. TÍNH TỔNG THUẾ VAT ===")
    val taxVisitor = TaxCalculatorVisitor()
    var totalTax = 0.0
    for (item in items) {
        totalTax += item.accept(taxVisitor)
    }
    println("➡️ TỔNG THUẾ VAT: $totalTax$")
}
```

---

## 4. Cơ chế Double Dispatch

Kỹ thuật cốt lõi giúp Visitor Pattern hoạt động chính là **Double Dispatch**:

1. **Dispatch lần 1:** Client gọi `item.accept(visitor)` $
   ightarrow$ Xác định chính xác đối tượng `item` thực tế là `Book` hay `Fruit`.
2. **Dispatch lần 2:** Bên trong `accept()`, đối tượng gọi `visitor.visitBook(this)` hoặc `visitor.visitFruit(this)` $
   ightarrow$ Xác định chính xác phương thức xử lý tương ứng trên `visitor`.

---

## 5. Ưu điểm & Nhược điểm

### Ưu điểm
* **Open/Closed Principle (OCP):** Dễ dàng thêm các tính năng/thao tác mới bằng cách tạo một lớp Visitor mới mà không cần đụng chạm hay sửa đổi mã nguồn của các lớp Element.
* **Single Responsibility Principle (SRP):** Gom tất cả các đoạn mã xử lý liên quan đến một tính năng/thuật toán vào chung một lớp Visitor.

### Nhược điểm
* **Khó khăn khi thêm Element mới:** Nếu hệ thống bổ sung một loại Element mới (ví dụ `Electronics`), bạn bắt buộc phải sửa interface `Visitor` và thêm hàm `visitElectronics()` cho **tất cả** các lớp `ConcreteVisitor` hiện có.
* **Phá vỡ tính đóng gói (Encapsulation):** Để Visitor có thể tính toán, các Element thường phải công khai (public) các thuộc tính/dữ liệu nội bộ của mình.

---

## 6. Khi nào nên dùng Visitor Pattern?

* Khi cấu trúc đối tượng (các lớp Element) **cố định và ít khi thay đổi**, nhưng bạn lại **thường xuyên phải bổ sung thêm các tính năng/thao tác mới** trên cấu trúc đó.
* Khi viết các công cụ phân tích cú pháp (Compilers / Abstract Syntax Tree - AST), Linter, Export dữ liệu ra nhiều định dạng khác nhau (JSON, XML, PDF).