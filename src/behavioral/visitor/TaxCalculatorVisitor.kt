package behavioral.visitor

class TaxCalculatorVisitor: ShoppingCartVisitor {
    override fun visitBook(book: Book): Double {
        // Sách chịu thuế 5%
        val tax = book.price * 0.05
        println("🧾 Thuế sách [${book.title}]: $tax$")
        return tax
    }

    override fun visitFruit(fruit: Fruit): Double {
        // Nông sản/Hoa quả chịu thuế 0%
        val tax = 0.0
        println("🧾 Thuế hoa quả [${fruit.name}]: $tax$")
        return tax
    }
}