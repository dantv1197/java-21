package behavioral.visitor

interface ItemElement {
    fun accept(visitor: ShoppingCartVisitor): Double
}

class Book(
    val title: String,
    val price: Double,
    val isbnNumber: String
) : ItemElement {
    override fun accept(visitor: ShoppingCartVisitor): Double {
        // Double Dispatch: Gọi lại hàm visit tương ứng với kiểu Book
        return visitor.visitBook(this)
    }
}


class Fruit(
    val name: String,
    val pricePerKg: Double,
    val weightKg: Double
) : ItemElement {
    override fun accept(visitor: ShoppingCartVisitor): Double {
        // Double Dispatch: Gọi lại hàm visit tương ứng với kiểu Fruit
        return visitor.visitFruit(this)
    }
}