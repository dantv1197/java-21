# Strategy Design Pattern (Mô hình Chiến lược)

> **Nhóm Pattern:** Behavioral (Hành vi)  
> **Tên gọi khác:** Policy

---

## 1. Định nghĩa & Khái niệm

**Strategy Pattern** cho phép bạn **đóng gói một họ các thuật toán (algorithms) vào các lớp riêng biệt** và làm cho chúng **có thể hoán đổi cho nhau (interchangeable)** tại thời điểm runtime mà không làm thay đổi mã nguồn của Client sử dụng.

### Ý tưởng cốt lõi
Tách phần logic tính toán/thuật toán hay thay đổi ra khỏi lớp chứa dữ liệu (**Context**). Chuyển từ việc cài đặt cứng (hard-code) các thuật toán bằng chuỗi lệnh `if-else` / `switch-case` cồng kềnh sang mô hình ủy nhiệm (delegation) thông qua một interface chung.

> **Hình ảnh thực tế:** Ứng dụng Bản đồ (Google Maps / Apple Maps). Khi bạn chọn đường đi từ A đến B, ứng dụng cung cấp nhiều chiến lược: *Đi xe máy/ô tô*, *Đi bộ*, *Phương tiện công cộng*. Mỗi chiến lược có thuật toán tính quãng đường và thời gian khác nhau nhưng giao diện hiển thị cho người dùng là thống nhất.

---

## 2. Cấu trúc Mô hình (Class Diagram)

```
┌───────────────────────────┐               ┌───────────────────────────┐
│          Context          │               │       <<interface>>       │
│      (ShoppingCart)       │               │      PaymentStrategy      │
├───────────────────────────┤               ├───────────────────────────┤
│ - strategy: PaymentSt...  │──────────────►│ + pay(amount: Double)     │
│ + setPaymentStrategy(s)   │               └───────────────────────────┘
│ + checkout(totalAmount)   │                             ▲
└───────────────────────────┘                             │ implements
                                             ┌────────────┴────────────┐
                                             │                         │
                                ┌────────────┴───────────┐ ┌───────────┴───────────┐
                                │     CreditCardPayment  │ │       MomoPayment     │
                                └────────────────────────┘ └───────────────────────┘
```

### Các thành phần chính

| Thành phần | Vai trò |
| :--- | :--- |
| **Strategy (Interface)** | Khai báo phương thức chung cho tất cả các thuật toán/chiến lược. |
| **ConcreteStrategies** | Thực thi giao diện `Strategy`, triển khai logic thuật toán cụ thể (VD: `CreditCardPayment`, `MomoPayment`, `CashPayment`). |
| **Context** | Duy trì một tham chiếu đến đối tượng `Strategy` hiện tại và ủy nhiệm công việc cho Strategy đó xử lý. |
| **Client** | Khởi tạo chiến lược cụ thể và truyền nó vào cho `Context`. |

---

## 3. Minh họa Code mẫu hoàn chỉnh (Kotlin)

Ví dụ hệ thống **Thanh toán đơn hàng (Payment System)** hỗ trợ nhiều phương thức: **Thẻ tín dụng (Credit Card)**, **Ví MoMo**, và **Tiền mặt (Cash)**.

### Bước 1: Khai báo Strategy Interface

```kotlin
interface PaymentStrategy {
    fun pay(amount: Double)
}
```

### Bước 2: Triển khai các ConcreteStrategies

```kotlin
// Thanh toán qua Thẻ tín dụng
class CreditCardPayment(
    private val cardNumber: String,
    private val cvv: String
) : PaymentStrategy {
    override fun pay(amount: Double) {
        println("💳 Thanh toán $amount$ bằng Thẻ tín dụng [***${cardNumber.takeLast(4)}]")
    }
}

// Thanh toán qua Ví MoMo
class MomoPayment(private val phoneNumber: String) : PaymentStrategy {
    override fun pay(amount: Double) {
        println("📱 Thanh toán $amount$ qua Ví MoMo [SĐT: $phoneNumber]")
    }
}

// Thanh toán Tiền mặt khi nhận hàng (COD)
class CashPayment : PaymentStrategy {
    override fun pay(amount: Double) {
        println("💵 Thanh toán $amount$ bằng Tiền mặt khi nhận hàng (COD)")
    }
}
```

### Bước 3: Triển khai Context (`ShoppingCart`)

```kotlin
class ShoppingCart {
    private var paymentStrategy: PaymentStrategy? = null

    // Cho phép thay đổi chiến lược thanh toán linh hoạt lúc runtime
    fun setPaymentStrategy(strategy: PaymentStrategy) {
        this.paymentStrategy = strategy
    }

    fun checkout(totalAmount: Double) {
        val strategy = paymentStrategy 
            ?: throw IllegalStateException("Chưa chọn phương thức thanh toán!")
        
        strategy.pay(totalAmount)
    }
}
```

### Bước 4: Client (Hàm Main)

```kotlin
fun main() {
    val cart = ShoppingCart()
    val totalAmount = 250.0

    println("=== LẦN 1: KHÁCH CHỌN THANH TOÁN MOMO ===")
    cart.setPaymentStrategy(MomoPayment("0901234567"))
    cart.checkout(totalAmount)

    println("
=== LẦN 2: KHÁCH ĐỔI SANG THẺ TÍN DỤNG ===")
    cart.setPaymentStrategy(CreditCardPayment("1234567890123456", "123"))
    cart.checkout(totalAmount)

    println("
=== LẦN 3: KHÁCH CHỌN THANH TOÁN TIỀN MẶT ===")
    cart.setPaymentStrategy(CashPayment())
    cart.checkout(totalAmount)
}
```

---

## 4. Ưu điểm & Nhược điểm

### Ưu điểm
* **Open/Closed Principle (OCP):** Dễ dàng thêm các chiến lược mới (như `VNPayPayment`, `ZaloPayPayment`) mà không làm ảnh hưởng đến mã nguồn sẵn có của lớp Context.
* **Loại bỏ `if-else / switch-case` cồng kềnh:** Tách biệt rõ ràng các luồng điều khiển phức tạp thành các lớp riêng biệt.
* **Hoán đổi linh hoạt lúc Runtime:** Cho phép ứng dụng tự động hoặc cho người dùng chủ động đổi thuật toán xử lý khi đang chạy.
* **Tách biệt logic nghiệp vụ:** Đóng gói chi tiết thuật toán riêng biệt khỏi lớp quản lý dữ liệu.

### Nhược điểm
* **Tăng số lượng Class:** Khiến số lượng file/class trong dự án tăng lên nếu chỉ có 1-2 thuật toán đơn giản.
* **Client phải hiểu các Strategy:** Client phải biết sự khác biệt giữa các chiến lược để lựa chọn chiến lược phù hợp truyền vào Context.

---

## 5. Khi nào nên dùng Strategy Pattern?

1. **Khi có nhiều biến thể của một thuật toán:** Khi bạn cần nhiều cách để xử lý một công việc (VD: Các thuật toán sắp xếp `QuickSort`/`MergeSort`, các định dạng Export file `PDF`/`Excel`/`CSV`).
2. **Khi có nhiều câu lệnh điều kiện `if-else / switch-case` chọn thuật toán:** Khi phương thức chứa chuỗi kiểm tra điều kiện quá dài chỉ để quyết định dùng logic nào.
3. **Khi muốn ẩn dữ liệu/logic phức tạp của thuật toán:** Giấu các cấu trúc dữ liệu hoặc thư viện tính toán phức tạp bên trong các lớp Strategy riêng biệt.

---

## 6. Phân biệt Strategy Pattern & State Pattern

| Đặc điểm | Strategy Pattern | State Pattern |
| :--- | :--- | :--- |
| **Mục đích** | Đóng gói các thuật toán độc lập để hoán đổi linh hoạt. | Quản lý trạng thái nội bộ và thay đổi hành vi theo trạng thái. |
| **Mối quan hệ** | Các Strategy độc lập và **không biết** về nhau. | Các State thường **biết về nhau** và tự kích hoạt chuyển trạng thái. |
| **Điều khiển** | **Client** chủ động chọn/truyền Strategy vào Context. | **Context** hoặc bản thân các **State** tự chuyển đổi qua lại. |