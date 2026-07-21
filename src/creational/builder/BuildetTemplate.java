package creational.builder;

public class BuildetTemplate {
    public static void main(String[] args) {
        // Khách hàng 1: Đặt Pizza Hải Sản đơn giản (chỉ thêm Phô mai & Nấm)
        Pizza seafoodPizza = new Pizza.PizzaBuilder("Large", "Thick")
                .addCheese()
                .addMushroom()
                .build();

        // Khách hàng 2: Đặt Pizza Hawaiian đặc biệt (thêm Phô mai, Xúc xích, Dứa)
        Pizza pizzaHawaiian = new Pizza.PizzaBuilder("Medium", "Thin")
                .addCheese()
                .addPepperoni()
                .addPineapple()
                .build();

        // Khách hàng 3: Đặt Pizza chay (Không thêm topping nào)
        Pizza vegetarianPizza = new Pizza.PizzaBuilder("Small", "Thin")
                .build();

        System.out.println(seafoodPizza);
        System.out.println(pizzaHawaiian);
        System.out.println(vegetarianPizza);
    }
}
