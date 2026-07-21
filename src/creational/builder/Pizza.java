package creational.builder;

public class Pizza {
    // 1. Required properties
    private final String size;
    private final String crust;

    // 2. Optional properties
    private final boolean hasCheese;
    private final boolean hasPepperoni;
    private final boolean hasMushroom;
    private final boolean hasPineapple;

    private Pizza(PizzaBuilder builder) {
        this.size = builder.size;
        this.crust = builder.crust;
        this.hasCheese = builder.hasCheese;
        this.hasPepperoni = builder.hasPepperoni;
        this.hasMushroom = builder.hasMushroom;
        this.hasPineapple = builder.hasPineapple;

    }
    public String getCrust() {
        return crust;
    }
    public String getSize() {
        return size;
    }
    public boolean hasCheese() {
        return hasCheese;
    }
    public boolean hasMushroom() {
        return hasMushroom;
    }
    public boolean hasPepperoni() {
        return hasPepperoni;
    }
    public boolean hasPineapple() {
        return hasPineapple;
    }

    public String toString() {
        return "Pizza [" + size + ", d " + crust + "]"
                + (hasCheese ? " + Cheese" : "")
                + (hasPepperoni ? " + hotdog" : "")
                + (hasMushroom ? " + mush" : "")
                + (hasPineapple ? " + Pineapple" : "");
    }
    public static class PizzaBuilder {
        // required
        private final String size;
        private final String crust;

        // otional, default value
        private boolean hasCheese = false;
        private boolean hasPepperoni = false;
        private boolean hasMushroom = false;
        private boolean hasPineapple = false;

        // Builder constructor, has required properties
        public PizzaBuilder(String size, String crust) {
            this.size = size;
            this.crust = crust;
        }
        // optional method (return this- Fluent API)
        public PizzaBuilder addCheese() {
            this.hasCheese = true;
            return this;
        }

        public PizzaBuilder addPepperoni() {
            this.hasPepperoni = true;
            return this;
        }

        public PizzaBuilder addMushroom() {
            this.hasMushroom = true;
            return this;
        }

        public PizzaBuilder addPineapple() {
            this.hasPineapple = true;
            return this;
        }

        // important method(required): create and return  Object
        public Pizza build() {
            return new Pizza(this);
        }
    }
}
