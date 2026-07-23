package behavioral.visitor

class PriceCalculatorVisitor: ShoppingCartVisitor {
    override fun visitBook(book: Book): Double {
        var cost = book.price
        // Nếu sách giá trên 100$, giảm ngay 10$
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