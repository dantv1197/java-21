package behavioral.visitor

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