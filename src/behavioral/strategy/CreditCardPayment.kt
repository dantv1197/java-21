package behavioral.strategy

class CreditCardPayment(
    private val cardNumber: String,
    private val cvv: String
) : PaymentStrategy {
    override fun pay(amount: Double) {
        println("💳 Thanh toán $amount$ bằng Thẻ tín dụng [***${cardNumber.takeLast(4)}]")
    }
}


class MomoPayment(private val phoneNumber: String) : PaymentStrategy {
    override fun pay(amount: Double) {
        println("📱 Thanh toán $amount$ qua Ví MoMo [SĐT: $phoneNumber]")
    }
}


class CashPayment : PaymentStrategy {
    override fun pay(amount: Double) {
        println("💵 Thanh toán $amount$ bằng Tiền mặt khi nhận hàng (COD)")
    }
}