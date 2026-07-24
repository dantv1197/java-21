package behavioral.strategy

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