package behavioral.strategy

fun main() {
    val cart = ShoppingCart()
    val totalAmount = 250.0

    // 1. Khách hàng chọn thanh toán MoMo
    cart.setPaymentStrategy(MomoPayment("0901234567"))
    cart.checkout(totalAmount)

    // 2. Khách đổi ý chuyển sang thanh toán Thẻ
    cart.setPaymentStrategy(CreditCardPayment("1234567890123456", "123"))
    cart.checkout(totalAmount)
}