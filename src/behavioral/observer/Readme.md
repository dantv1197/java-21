# Observer Design Pattern (Mô hình Quan sát)

> **Nhóm Pattern:** Behavioral (Hành vi)  
> **Tên gọi khác:** Publish-Subscribe, Listener, Event-Subscriber

---

## 1. Tổng quan & Khái niệm

**Observer Pattern** định nghĩa mối quan hệ **1-nhiều (1-to-many)** giữa các đối tượng. Khi trạng thái của đối tượng chính (**Subject / Publisher**) thay đổi, tất cả các đối tượng phụ thuộc (**Observers / Subscribers**) sẽ tự động nhận được thông báo và cập nhật.

### Ý tưởng cốt lõi
Tránh việc **Polling** (liên tục truy vấn kiểm tra xem dữ liệu có thay đổi hay không). Thay vào đó, chuyển sang mô hình **Event-driven** (chờ sự kiện kích hoạt gửi về).

---

## 2. Cấu trúc Mô hình (Class Diagram)

```
┌────────────────────────┐              ┌────────────────────────┐
│     <<interface>>      │              │     <<interface>>      │
│        Subject         │              │        Observer        │
├────────────────────────┤              ├────────────────────────┤
│ + attach(o: Observer)  │              │ + update(data)         │
│ + detach(o: Observer)  │              └────────────────────────┘
│ + notifyObservers()    │                          ▲
└────────────────────────┘                          │ implements
            ▲                                       │
            │ implements                ┌────────────────────────┐
┌────────────────────────┐              │    ConcreteObserver    │
│    ConcreteSubject     │              ├────────────────────────┤
├────────────────────────┤─────────────►│ + update(data)         │
│ - state                │ notifies     └────────────────────────┘
└────────────────────────┘
```

### Các thành phần chính

| Thành phần | Vai trò |
| :--- | :--- |
| **Subject (Interface)** | Khai báo các phương thức đăng ký (`attach`), hủy đăng ký (`detach`) và thông báo (`notify`). |
| **ConcreteSubject** | Lưu trữ trạng thái gốc. Khi trạng thái thay đổi, gọi `notifyObservers()`. |
| **Observer (Interface)** | Khai báo phương thức `update()` dùng để nhận thông báo từ Subject. |
| **ConcreteObserver** | Thực thi giao diện Observer, định nghĩa hành động cụ thể khi nhận thông tin mới. |

---

## 3. Min họa Code mẫu (Kotlin)

### Bước 1: Khai báo Interfaces

```kotlin
interface Observer {
    fun update(temperature: Float)
}

interface Subject {
    fun registerObserver(o: Observer)
    fun removeObserver(o: Observer)
    fun notifyObservers()
}
```

### Bước 2: Dựng ConcreteSubject (Trạm thời tiết)

```kotlin
class WeatherStation : Subject {
    private val observers = mutableListOf<Observer>()
    private var temperature: Float = 0f

    override fun registerObserver(o: Observer) {
        observers.add(o)
    }

    override fun removeObserver(o: Observer) {
        observers.remove(o)
    }

    override fun notifyObservers() {
        for (observer in observers) {
            observer.update(temperature)
        }
    }

    fun setTemperature(newTemp: Float) {
        println("\n[WeatherStation] Nhiệt độ cập nhật: $newTemp°C")
        this.temperature = newTemp
        notifyObservers()
    }
}
```

### Bước 3: Dựng các ConcreteObservers

```kotlin
class PhoneDisplay : Observer {
    override fun update(temperature: Float) {
        println("[Phone Display] Nhận nhiệt độ mới: $temperature°C")
    }
}

class WindowDisplay : Observer {
    override fun update(temperature: Float) {
        println("[Window Display] Màn hình hiển thị: $temperature°C")
    }
}
```

### Bước 4: Thực thi

```kotlin
fun main() {
    val station = WeatherStation()

    val phone = PhoneDisplay()
    val window = WindowDisplay()

    // Đăng ký nhận tin
    station.registerObserver(phone)
    station.registerObserver(window)

    // Thay đổi trạng thái -> cả 2 đều nhận
    station.setTemperature(28.5f)

    // Hủy đăng ký bớt 1 bên
    station.removeObserver(phone)

    // Thay đổi trạng thái -> chỉ còn Window nhận
    station.setTemperature(30.0f)
}
```

---

## 4. Ưu điểm & Nhược điểm

### Ưu điểm
* **Loose Coupling (Lỏng lẻo phụ thuộc):** Subject không cần biết chi tiết logic bên trong Observer.
* **Open/Closed Principle:** Dễ dàng thêm Observer mới mà không làm thay đổi code của Subject.
* **Tương tác thời gian thực:** Tránh lãng phí tài nguyên CPU do việc Polling liên tục.

### Nhược điểm
* **Rò rỉ bộ nhớ (Lapsed Listener Problem):** Nếu quên hủy đăng ký Observer, GC sẽ không thể thu gom đối tượng đó.
* **Thứ tự không đảm bảo:** Sự kiện bắn ra cho các Observer không theo thứ tự ưu tiên cố định.
* **Quản lý phức tạp:** Nếu chuỗi Observer - Subject liên kết vòng tròn, dễ gây ra lỗi tràn bộ nhớ / lặp vô tận.

---

## 5. Ứng dụng thực tế

* **UI Event Listeners:** `OnClickListener`, `TextWatcher` trong Android SDK / Swing / DOM Events.
* **Reactive Programming:** RxJava/RxKotlin (`Observable`, `Subscriber`), Kotlin `StateFlow`/`SharedFlow`.
* **State Management:** Redux, Vuex, LiveData trong phát triển ứng dụng di động & web.