import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class MainTest {

    @Test
    void testRestaurantConstructor() {
        Restaurant restaurant = new Restaurant("Test Restaurant");
        assertEquals("Test Restaurant", restaurant.name);
    }

    @Test
    void testFoodItemConstructor() {
        FoodItem foodItem = new FoodItem("Test Food Item");
        assertEquals("Test Food Item", foodItem.name);
    }

    @Test
    void testOrderConstructor() {
        Restaurant restaurant = new Restaurant("Test Restaurant");
        FoodItem[] items = {new FoodItem("Test Food Item 1"), new FoodItem("Test Food Item 2")};
        Order order = new Order(restaurant, items);
        assertEquals(restaurant, order.restaurant);
        assertArrayEquals(items, order.items);
    }

    @Test
    void testReviewDeliveryPersonWithValidData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("review.txt"))) {
            writer.write("Test Delivery Person\nTest Customer");
        } catch (IOException e) {
            fail("Error writing to review.txt.");
        }
    
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Thread reviewThread = Main.reviewDeliveryPerson();
        try {
            reviewThread.join();  
        } catch (InterruptedException e) {
            fail("Error in test: review thread was interrupted.");
        }
    
        assertTrue(outContent.toString().contains("Customer Test Customer has left a review for delivery person Test Delivery Person."));
    }
    
    @Test
    void testReviewDeliveryPersonWithInvalidData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("review.txt"))) {
            writer.write("Test Delivery Person\n");
        } catch (IOException e) {
            fail("Error writing to review.txt.");
        }
    
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Thread reviewThread = Main.reviewDeliveryPerson();
        try {
            reviewThread.join(); 
        } catch (InterruptedException e) {
            fail("Error in test: review thread was interrupted.");
        }
    
        assertTrue(outContent.toString().contains("Invalid review data."));
    }
    @Test
    void testDeliverOrderWithIncompleteData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("order.txt"))) {
            writer.write("Test Restaurant\n");
        } catch (IOException e) {
            fail("Error setting up test: could not write to order file.");
        }

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Thread deliverThread = Main.deliverOrder();
        try {
            deliverThread.join();  
        } catch (InterruptedException e) {
            fail("Error in test: deliver thread was interrupted.");
        }
    
        assertTrue(outContent.toString().contains("Order file is empty or incomplete."));
    }
    
}