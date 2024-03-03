public class Customer implements Reviewable {
    String name;

    public Customer(String name) {
        this.name = name;
    }

    @Override
    public void review() {
        /* System.out.println("Customer " + this.name + " has left a review."); */
    }
    
}