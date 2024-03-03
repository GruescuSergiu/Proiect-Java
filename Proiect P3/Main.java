import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String action = "";
            while (!action.equals("deliver") && !action.equals("review")) {
                System.out.println("Enter action (deliver or review): ");
                action = scanner.nextLine();
                if (!action.equals("deliver") && !action.equals("review")) {
                    System.out.println("Invalid action. Please enter either 'deliver' or 'review'.");
                }
            }

            if (action.equals("deliver")) {
                System.out.println("Enter restaurant name: ");
                String restaurantName = scanner.nextLine();

                System.out.println("Enter food item: ");
                String foodItem = scanner.nextLine();

                try (BufferedWriter writer = new BufferedWriter(new FileWriter("order.txt"))) {
                    writer.write(restaurantName);
                    writer.newLine();
                    writer.write(foodItem);
                } catch (IOException e) {
                    System.out.println("Error writing to order file.");
                }

                Thread deliverThread = new Thread(Main::deliverOrder);
                deliverThread.start();
            } else if (action.equals("review")) {
                Thread reviewThread = new Thread(Main::reviewDeliveryPerson);
                reviewThread.start();
            }
        }
    }


    static Thread deliverOrder() {
        File orderFile = new File("order.txt");
        if (!orderFile.exists()) {
            System.out.println("Order file does not exist.");
            return null;
        }
        Thread readThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new FileReader(orderFile))) {
                String restaurantName = reader.readLine();
                String foodItem = reader.readLine();
                if (restaurantName != null && foodItem != null) {
                    System.out.println("Delivering order from " + restaurantName + ": " + foodItem);
                } else {
                    System.out.println("Order file is empty or incomplete.");
                }
            } catch (IOException e) {
                System.out.println("Error reading order file.");
            }
        });
        readThread.start();
        return readThread;
    }


    static Thread reviewDeliveryPerson() {
        File reviewFile = new File("review.txt");
        if (!reviewFile.exists()) {
            try {
                if (reviewFile.createNewFile()) {
                    System.out.println("Review file created.");
                }
            } catch (IOException e) {
                System.out.println("Error creating review file.");
            }
        }
        Thread reviewThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new FileReader(reviewFile))) {
                String deliveryPersonName = reader.readLine();
                String customerName = reader.readLine();
                if (deliveryPersonName == null || deliveryPersonName.trim().isEmpty() ||
                    customerName == null || customerName.trim().isEmpty()) {
                    throw new InvalidReviewException("Invalid review data.");
                }

                Customer customer = new Customer(customerName);
                System.out.println("Customer " + customer.name + " has left a review for delivery person " + deliveryPersonName + ".");

            } catch (FileNotFoundException e) {
                System.out.println("Review file not found.");
            } catch (IOException e) {
                System.out.println("Error reading review file.");
            } catch (InvalidReviewException e) {
                System.out.println(e.getMessage());
            }
        });
        reviewThread.start();
        return reviewThread;
    }
}

class InvalidOrderException extends Exception {
    public InvalidOrderException(String message) {
        super(message);
    }
}

class InvalidReviewException extends Exception {
    public InvalidReviewException(String message) {
        super(message);
    }
}