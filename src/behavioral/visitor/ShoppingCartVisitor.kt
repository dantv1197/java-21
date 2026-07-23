package behavioral.visitor

interface ShoppingCartVisitor {
    fun visitBook(book: Book): Double
    fun visitFruit(fruit: Fruit): Double
}