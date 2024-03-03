public class DeliveryPerson implements Reviewable {
    String name;

    public DeliveryPerson(String name) {
        this.name = name;
    }

    @Override
    public void review() {
        /* System.out.println("Delivery person " + this.name + " has been reviewed."); */
    }
}