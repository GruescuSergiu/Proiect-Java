public class Order implements Deliverable {
    Restaurant restaurant;
    FoodItem[] items;

    public Order(Restaurant restaurant, FoodItem[] items) {
        this.restaurant = restaurant;
        this.items = items;
    }

    @Override
    public void deliver() {
        System.out.print("Order from " + this.restaurant.name + " has been delivered with the following items: ");
        for (int i = 0; i < this.items.length; i++) {
            System.out.print(this.items[i].name);
            if (i < this.items.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println(".");
    }
}