# Prototype Pattern - Ghi chú Kiến trúc & Hướng dẫn Sử dụng

## 1. Định nghĩa (Definition)

**Prototype Pattern** là một mẫu thiết kế khởi tạo (**Creational Design Pattern**). Mẫu thiết kế này cho phép bạn **sao chép (clone) một đối tượng đã tồn tại** để tạo ra một đối tượng mới hoàn toàn độc lập, thay vì phải khởi tạo lại từ đầu bằng từ khóa `new`.

> **Triết lý cốt lõi:** Nhân bản từ một "mẫu chuẩn" (Prototype) có sẵn trong bộ nhớ RAM để tiết kiệm chi phí thời gian và tài nguyên phần cứng (CPU, Memory, I/O) so với việc khởi tạo và thiết lập thuộc tính phức tạp từ đầu.

---

## 2. Cấu trúc của Prototype Pattern

Mô hình Prototype chuẩn bao gồm các thành phần chính sau:

```
┌──────────────────────────────────────┐
│        Prototype (Interface)         │
├──────────────────────────────────────┤
│ + clone(): Prototype                 │
└──────────────────▲───────────────────┘
                   │
         ┌─────────┴─────────┐
         │                   │
┌────────┴─────────┐ ┌───────┴──────────┐
│ ConcreteProductA │ │ ConcreteProductB │
├──────────────────┤ ├──────────────────┤
│ + clone()        │ │ + clone()        │
└──────────────────┘ └──────────────────┘
```

1. **Prototype (Interface/Abstract Class):** Khai báo giao diện chứa phương thức `clone()`.
2. **Concrete Prototype (Lớp cụ thể):** Cài đặt phương thức `clone()` để tự nhân bản chính nó.
3. **Prototype Registry (Kho quản lý mẫu - Tùy chọn):** Nơi lưu trữ và quản lý tập hợp các đối tượng mẫu sẵn có (Cache), hỗ trợ lấy bản sao nhanh chóng theo khóa (Key).
4. **Client:** Yêu cầu tạo đối tượng mới bằng cách gọi hàm `clone()` từ đối tượng mẫu.

---

## 3. Mã nguồn minh họa (Implementation - Java)

### Kịch bản thực tế
Trong một game RPG, việc tải quái vật (**Monster**) từ Database hoặc File 3D tốn rất nhiều thời gian. Chúng ta sẽ nạp 1 đối tượng quái vật mẫu vào bộ nhớ, sau đó nhân bản (**clone**) ra hàng loạt quái vật trên bản đồ.

#### Bước 1: Khai báo Prototype Interface
```java
public interface Prototype<T> {
    T clone();
}
```

#### Bước 2: Tạo Concrete Prototype
```java
public class Monster implements Prototype<Monster> {
    private String type;
    private int hp;
    private int damage;
    private String modelData; // Dữ liệu 3D Model đắt đỏ

    // Constructor đắt đỏ (Tốn thời gian IO/DB)
    public Monster(String type, int hp, int damage) {
        this.type = type;
        this.hp = hp;
        this.damage = damage;
        this.modelData = loadHeavyGraphicModel(type);
    }

    // Constructor phục vụ việc Clone (Sao chép nhanh từ RAM)
    private Monster(Monster target) {
        if (target != null) {
            this.type = target.type;
            this.hp = target.hp;
            this.damage = target.damage;
            this.modelData = target.modelData; // Dùng chung dữ liệu đồ họa đã nạp
        }
    }

    private String loadHeavyGraphicModel(String type) {
        System.out.println("[DATABASE] Đang đọc file 3D đắt đỏ cho: " + type + "...");
        return "Model_Data_" + type;
    }

    @Override
    public Monster clone() {
        return new Monster(this); // Nhân bản tức thì
    }

    // Setters
    public void setHp(int hp) { this.hp = hp; }

    public void showInfo() {
        System.out.println("Monster: " + type + " | HP: " + hp + " | HashCode: " + this.hashCode());
    }
}
```

#### Bước 3: Tạo Prototype Registry (Kho quản lý bản mẫu)
```java
import java.util.HashMap;
import java.util.Map;

public class MonsterRegistry {
    private Map<String, Monster> cache = new HashMap<>();

    public MonsterRegistry() {
        // Tải các bản mẫu sẵn vào RAM lúc bắt đầu
        cache.put("GOBLIN", new Monster("Goblin Rừng", 100, 15));
        cache.put("DRAGON", new Monster("Rồng Lửa", 5000, 300));
    }

    public Monster getMonster(String key) {
        Monster prototype = cache.get(key);
        if (prototype != null) {
            return prototype.clone(); // Trả về bản sao
        }
        return null;
    }
}
```

#### Bước 4: Client sử dụng
```java
public class Application {
    public static void main(String[] args) {
        MonsterRegistry registry = new MonsterRegistry();

        System.out.println("
--- SPAWN HÀNG LOẠT QUÁI VẬT BẰNG CLONE ---");

        // Spawn Goblin 1
        Monster goblin1 = registry.getMonster("GOBLIN");
        
        // Spawn Goblin 2 và biến đổi thuộc tính riêng
        Monster goblin2 = registry.getMonster("GOBLIN");
        goblin2.setHp(150); // Goblin đột biến

        goblin1.showInfo();
        goblin2.showInfo();

        System.out.println("Kiểm tra 2 object riêng biệt: " + (goblin1 != goblin2)); // true
    }
}
```

---

## 4. Trường hợp sử dụng (Use Cases)

Nên áp dụng **Prototype Pattern** trong các tình huống:

1. **Chi phí khởi tạo quá đắt đỏ:** Việc khởi tạo đối tượng bằng `new` đòi hỏi đọc file dung lượng lớn, thực hiện các truy vấn SQL phức tạp, hoặc tính toán ngốn CPU.
2. **Cần lưu Snapshot / Lịch sử trạng thái:** Khi ứng dụng cần nhân bản một đối tượng ở một thời điểm nhất định để làm tính năng **Undo / Redo** (Ctrl + Z) hoặc lưu lại Restore Point.
3. **Tránh sự bùng nổ của các Lớp con (Subclasses):** Thay vì tạo quá nhiều Subclass chỉ để cài đặt các bộ cấu hình thuộc tính khác nhau, ta chỉ cần tạo một vài Prototype mẫu và điều chỉnh thuộc tính sau khi clone.
4. **Không muốn phụ thuộc vào Class cụ thể:** Khi mã nguồn client chỉ tương tác qua Interface và không muốn dính chặt với Concrete Class của đối tượng cần tạo.

---

## 5. Khái niệm quan trọng: Shallow Copy vs Deep Copy

Khi cài đặt phương thức `clone()`, bạn phải đặc biệt lưu ý trường hợp đối tượng có chứa biến tham chiếu (Reference Objects):

| Tiêu chí | Shallow Copy (Sao chép nông) | Deep Copy (Sao chép sâu) |
| :--- | :--- | :--- |
| **Cơ chế** | Chỉ copy giá trị nguyên thủy (primitive). Các thuộc tính kiểu Object sẽ **dùng chung địa chỉ ô nhớ**. | Copy toàn bộ giá trị nguyên thủy và **khởi tạo lại hoàn toàn** các Object con bên trong. |
| **Độ an toàn** | Đơn giản nhưng dễ gây lỗi rò rỉ hoặc sai lệch dữ liệu dây chuyền nếu thay đổi object con. | An toàn tuyệt đối, hai đối tượng độc lập 100%. |
| **Trường hợp áp dụng**| Đối tượng chỉ chứa kiểu dữ liệu cơ bản hoặc immutable (String, Integer...). | Đối tượng chứa List, Array, hoặc các Object phức tạp khác. |

---

## 6. Ưu điểm & Nhược điểm

### Ưu điểm (Pros)
* **Tối ưu hiệu năng:** Nhân bản đối tượng trong RAM nhanh hơn nhiều so với khởi tạo lại từ nguồn đắt đỏ.
* **Giảm kết nối chặt (Decoupling):** Client có thể tạo đối tượng mà không cần biết lớp cụ thể của nó.
* **Linh hoạt:** Dễ dàng tạo và thêm các mẫu đối tượng mới vào Registry lúc Runtime.

### Nhược điểm (Cons)
* **Phức tạp khi triển khai Deep Copy:** Khi đối tượng có cấu trúc lồng nhau phức tạp hoặc bị tham chiếu vòng (Circular Reference), việc viết hàm `clone()` rất dễ sót lỗi.